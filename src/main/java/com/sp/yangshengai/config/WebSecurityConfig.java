package com.sp.yangshengai.config;

import cn.hutool.extra.spring.SpringUtil;
import com.sp.yangshengai.exception.CustomException;
import com.sp.yangshengai.exception.ErrorEnum;
import com.sp.yangshengai.filter.ExceptionFilter;
import com.sp.yangshengai.filter.JwtAuthenticationFilter;
import com.sp.yangshengai.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//开启SpringSecurity的默认行为
//bean注解
// 新版不需要继承WebSecurityConfigurerAdapter
public class WebSecurityConfig {

	private final CorsFilter corsFilter;

	private  final ExceptionFilter exceptionFilter;


	//private final PermitResource permitResource;

	private  final ApplicationEventPublisher applicationEventPublisher;

   private JwtAuthenticationFilter getJwtAuthenticationFilter(){
	   return SpringUtil.getBean(JwtAuthenticationFilter.class);
   }

	private UserServiceImpl getMyUserDetailsService() {
		return SpringUtil.getBean(UserServiceImpl.class);
	}


    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// 无需认证的地址列表
		//List<String> permitList = permitResource.getPermitList();
		//String[] permits = permitList.toArray(new String[0]);
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request
						.requestMatchers("/user/login","/user/signup","/upload/**", "/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/webjars/**",
								"/doc.html").permitAll()
						.requestMatchers(HttpMethod.OPTIONS).permitAll()
						//.requestMatchers("/admin/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.SUPER_ADMIN.name())
						.anyRequest().authenticated()
				)
				// 禁用 session
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 使用自定义 provider
				.authenticationProvider(authenticationProvider())
				// 定义过滤器调用顺序
				.addFilterBefore(this.getJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(exceptionFilter, JwtAuthenticationFilter.class)
				.addFilterBefore(corsFilter, ExceptionFilter.class);

		// 异常处理
		http.exceptionHandling(handler -> handler
				.authenticationEntryPoint((request, response, authenticationException) -> {
					if (authenticationException instanceof DisabledException){
						// 用户被禁用
						throw CustomException.of(ErrorEnum.USER_IS_DISABLED);
					}
					if (authenticationException instanceof UsernameNotFoundException ||
							authenticationException instanceof BadCredentialsException){
						// 用户名或密码错误
						throw CustomException.of(ErrorEnum.USERNAME_OR_PASSWORD_ERROR);
					}
					throw CustomException.of(ErrorEnum.AUTHENTICATION_FAILURE);
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					throw CustomException.of(ErrorEnum.AUTHORIZATION_FAILURE);
				})
		);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/**
	 * 提供用户详情获取以及密码加密方式
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(this.getMyUserDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setHideUserNotFoundExceptions(false);
		return authProvider;
	}

	/**
	 * 调用其中 authenticate 方法
	 * 底层会使用 AuthenticationProvider 从数据库获取用户信息进行登录校验
	 */
	@Bean
	public AuthenticationManager authenticationManager() {
		List<AuthenticationProvider> providerList = new ArrayList<>();
		providerList.add(authenticationProvider());

		ProviderManager providerManager = new ProviderManager(providerList);
		providerManager.setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));

		return providerManager;
	}

}