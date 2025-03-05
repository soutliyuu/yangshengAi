package com.sp.yangshengai.config;

import com.sp.yangshengai.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity //开启SpringSecurity的默认行为
@RequiredArgsConstructor//bean注解
// 新版不需要继承WebSecurityConfigurerAdapter
public class WebSecurityConfig {
 
	// 这个类主要是获取库中的用户信息，交给security
	private final UserServiceImpl userDetailsService;
	// 这个的类是认证失败处理（我在这里主要是把错误消息以json方式返回）
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	// 鉴权失败的时候的处理类

	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	//	登录成功处理
	private final LoginSuccessHandler loginSuccessHandler;
	//	登录失败处理
	private final LoginFailureHandler loginFailureHandler;
	//	登出成功处理
	private final LoginLogoutSuccessHandler loginLogoutSuccessHandler;
	//	token过滤器
	private final JwtTokenFilter jwtTokenFilter;
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration
	) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
 
	// 加密方式
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
 
	/**
	 * 核心配置
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
log.info("------------filterChain------------");
		http
				//  禁用basic明文验证
				.httpBasic(Customizer.withDefaults())
				//  基于 token ，不需要 csrf
				.csrf(AbstractHttpConfigurer::disable)
				//  禁用默认登录页
				.formLogin(fl ->
						fl.loginPage(PathMatcherUtil.FORM_LOGIN_URL)
						.loginProcessingUrl(PathMatcherUtil.TO_LOGIN_URL)
						.usernameParameter("username")
						.passwordParameter("password")
						.successHandler(loginSuccessHandler)
						.failureHandler(loginFailureHandler)
						.permitAll())
				//  禁用默认登出页
				.logout(lt -> lt.logoutSuccessHandler(loginLogoutSuccessHandler))
				//  基于 token ， 不需要 session
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				//  设置 处理鉴权失败、认证失败
				.exceptionHandling(
						exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint)
								.accessDeniedHandler(jwtAccessDeniedHandler)
				)
				//  下面开始设置权限
				.authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
						//  允许所有 OPTIONS 请求
						.requestMatchers(PathMatcherUtil.AUTH_WHITE_LIST).permitAll()
						//  允许直接访问 授权登录接口
//						.requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
						//  允许 SpringMVC 的默认错误地址匿名访问
//						.requestMatchers("/error").permitAll()
						//  其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailImpl对象中默认设置“ROLE_USER”
						//.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
//						.requestMatchers("/heartBeat/**", "/main/**").permitAll()
						//  允许任意请求被已登录用户访问，不检查Authority
						.anyRequest().authenticated()
				)
				//  添加过滤器
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		//可以加载fram嵌套页面
		http.headers( headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
		return http.build();
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return userDetailsService::loadUserByUsername;
	}
 
	/**
	 * 调用loadUserByUserName获取userDetail信息，在AbstractUserDetailsAuthenticationProvider里执行用户状态检查
	 *
	 * @return
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	/**
	 * 配置跨源访问(CORS)
	 *
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}