package com.example.doancuoiky.hostel.configuration;

import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.LoginRq;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtToken {
    public String generateToken(Users users){
        String Key = "KunHostel";
        long TimelineToken = 3600;

        String token = Jwts.builder()
                .setSubject(Long.toString(users.getId())) //dang ky token la ID nguoi dung
                .setIssuedAt(new Date())//Time start
                .setExpiration(new Date(System.currentTimeMillis() + TimelineToken)) //time end
                .signWith(SignatureAlgorithm.ES512,Key) //key theo thuat toan ES512, khoa bi mat
                .compact();
        return token;
    }
    public String generateTokena(LoginRq userModels){
        String Key = "KunHostel";
        long TimelineToken = 3600;

        String token = Jwts.builder()
                .setSubject(new String(userModels.getPhone())) //dang ky token la ID nguoi dung
                .setIssuedAt(new Date())//Time start
                .setExpiration(new Date(System.currentTimeMillis() + TimelineToken)) //time end
                .signWith(SignatureAlgorithm.ES512,Key) //key theo thuat toan ES512, khoa bi mat
                .compact();
        return token;
    }
}
