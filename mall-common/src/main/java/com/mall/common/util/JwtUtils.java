package com.mall.common.util;

import com.mall.common.constant.CommonConstant;
import com.mall.common.model.UserToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {
    public static String generateToken(UserToken userToken, int expire) throws Exception {
        String token = Jwts.builder()
                .setSubject(userToken.getUsername())
                .claim(CommonConstant.CONTEXT_PASSWORD, userToken.getPassword())
                .claim(CommonConstant.RENEWAL_TIME, new Date(System.currentTimeMillis() + expire / 2))
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS256, CommonConstant.JWT_PRIVATE_KEY)
                .compact();
        return token;
    }


    public static UserToken getInfoFromToken(String token) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(CommonConstant.JWT_PRIVATE_KEY).parseClaimsJws(token)
                .getBody();
        return new UserToken(claims.getSubject(), claims.get(CommonConstant.CONTEXT_PASSWORD).toString());
    }
}
