package com.example.demo.JWTconfig;

import com.example.demo.Entities.User;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import com.example.demo.Services.LoggedService;
import com.example.demo.Services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JWT {
    private final SecretKey jwtSecret = Keys.hmacShaKeyFor("'s=very,very-secret_jwt57y763sh847wvq93C*CV^*F%$&DC".getBytes());
    private final Long jwtExpirationMs = (long) (60 * 60 * 1000);
//    public boolean isValid(String token) {
//
//        String[] chunks = token.split("\\.");
//
//        Base64.Decoder decoder = Base64.getUrlDecoder();
//
//        //String header = new String(decoder.decode(chunks[0]));
//        JSONObject payload = null;
//        try {
//            payload = new JSONObject(new String(decoder.decode(chunks[1])));
//        }
//        catch (IllegalArgumentException e) {
//            System.out.println("Invalid payload");
//        }
//        catch (Exception e) {
//            System.out.println("Exception(e);");
//        }
//
//        String sub = null;
//        try {
//            sub = (String) payload.get("sub");
//        }
//        catch (IllegalArgumentException e) {
//            System.out.println("Invalid payload - cannot get sub");
//        }
//        catch (Exception e) {
//            System.out.println("Exception(e)");
//        }
//
//        int exp = 0;
//        try {
//            exp = (int) payload.get("exp");
//        }
//        catch (IllegalArgumentException e) {
//            System.out.println("Invalid payload - cannot get exp");
//        }
//        catch (Exception e) {
//            System.out.println("Exception(e)");
//        }
//
//        // if not expired
//        if ((exp) > (new Date().getTime() / 1000)) {
//            // if the user is logged
//            if (loggedService.isLogged(userService.findByUsername(sub))) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    public String generate(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact();
    }

    public static String decode(String jwt) {

        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println("header: " + header);
        System.out.println("payload: " + payload);
        return payload;
    }

}