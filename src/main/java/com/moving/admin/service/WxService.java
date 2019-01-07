package com.moving.admin.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.moving.admin.bean.WxInfor;


@Service
public class WxService {

    private final String secret = "b3e6ba538da6bb674c60f51c4aad2542";
    private final String appid = "wx80bdba4540f6ae2d";

    // 获取小程序用户信息
    public WxInfor getWXInfor(String code) throws Exception {
        String path = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("CHARSET", "UTF-8");

        // set outputStream connection
//		OutputStream outputStream = connection.getOutputStream();
//		outputStream.write(null);
//		outputStream.close();

        int responseCode = connection.getResponseCode();
        String readLine = null;
        if (HttpURLConnection.HTTP_OK == responseCode) { // 连接成功
            StringBuffer sBuffer = new StringBuffer();
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((readLine = reader.readLine()) != null) {
                sBuffer.append(readLine).append("\n");
            }
            reader.close();
            String result = sBuffer.toString();
            if (!"".equals(result)) {
                Gson gson = new Gson();
                WxInfor wxInfor = gson.fromJson(result, WxInfor.class);
                return wxInfor;
            }
        }
        return null;
    }
}
