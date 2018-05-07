package com.example;

import io.jsonwebtoken.*;
import org.junit.Test;

import java.util.Date;

public class JwtTest {

    @Test
    public void test() throws Exception {
        Claims claims = Jwts.claims();
        claims.setId("00001");
        claims.setIssuer("svr-xproject-user");
        claims.setIssuedAt(new Date());
        claims.setSubject("13750033560");
        claims.setAudience("XP_SDK");
        claims.put("uid", "00000000000001");
        String token = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "123456")
                .compact();
        System.out.println(token);
    }

}
