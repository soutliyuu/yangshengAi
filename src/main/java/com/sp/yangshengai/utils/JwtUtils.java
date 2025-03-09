package com.sp.yangshengai.utils;

import com.sp.yangshengai.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    //private final RedisCache redisCache;

    /**
     * 密钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 有效时间
     */
    @Value("#{${token.expiration}}")
    private long expiration;

    /**
     * 生成 token
     */
    public String generateToken(String username, Long userId) {
        HashMap<String, Object> extraClaims = new HashMap<>();

        String token = Jwts.builder()
                .setClaims(extraClaims) // 设置额外信息
                .setSubject(username) // 设置主题
                .setIssuedAt(new Date(System.currentTimeMillis())) // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 设置过期时间
                .signWith(getSigningKey(),SignatureAlgorithm.HS256) // 使用 HS256 算法签名
                .compact(); // 生成 token

        //redisCache.set(RedisKeys.getAccessTokenKey(userId), token, expiration / 1000);

        return token;
    }

    /**
     * 获取缓存中的 token
     */
    // public String getCacheToken(Long userId) {
    //     return (String) redisCache.get(RedisKeys.getAccessTokenKey(userId));
    // }

    /**
     * 删除缓存中的 token
     */
    // public void delCacheToken(Long userId) {
    //     redisCache.delete(RedisKeys.getAccessTokenKey(userId));
    // }

    /**
     * 解析 token 中的所有声明
     */
    public Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSigningKey()) // 设置验证密钥
                    .build()
                    .parseSignedClaims(token) // 解析 token
                    .getPayload(); // 获取声明
        } catch (ExpiredJwtException e) {
            throw CustomException.of("过期 token");
        } catch (JwtException e) {
            throw CustomException.of("非法 token");
        }
        return claims;
    }

    /**
     * 获取签名密钥
     */
    private Key getSigningKey() {
        // 直接使用 secret 字符串生成 Key
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}