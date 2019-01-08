package com.moving.admin.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.moving.admin.exception.WebException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JwtUtil {
    /**
     * 密钥
     */
    private final String secret = "crm.admin";
    /**
     * Token过期时间, 一天
     */
    private final long exp = 3600 * 1000 * 24;

    private final String EXP = "exp";
    private final String PAYLOAD = "payload";

    /**
     * 加密
     *
     * @param object 加密数据（用户登录信息）
     * @return
     */
    public <T> String encode(T object) {
        try {
            final JWTSigner signer = new JWTSigner(secret);
            Map<String, Object> data = new HashMap<>(10);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(object);
            data.put(PAYLOAD, jsonString);
            data.put(EXP, System.currentTimeMillis() + exp);
            return signer.sign(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密并验证
     *
     * @param jwt    客户端请求对应的Token字符串
     * @param tClass Token中用户信息
     * @return
     * @throws JWTVerifyException
     * @throws IOException
     * @throws SignatureException
     * @throws IllegalStateException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public <T> Map<String, Object> decode(String jwt, Class<T> tClass) {
        final JWTVerifier jwtVerifier = new JWTVerifier(secret);
        Map<String, Object> map = new HashMap<>();
        try {
            final Map<String, Object> data = jwtVerifier.verify(jwt);
            // 判断数据是否超时且符合标准
            if (data.containsKey(EXP) && data.containsKey(PAYLOAD)) {
                long exp = (long) data.get(EXP);
                long currentTimeMills = System.currentTimeMillis();
                Boolean overtime = false;
                if (exp < currentTimeMills) {
                    overtime = true;
                }
                String json = (String) data.get(PAYLOAD);
                ObjectMapper objectMapper = new ObjectMapper();
                map.put("token", objectMapper.readValue(json, tClass));
                map.put("overtime", overtime);
                return map;
            }
            return null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


}
