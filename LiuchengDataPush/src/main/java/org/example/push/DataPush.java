package org.example.push;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by yy on 2017/7/24.
 * 数据推送类
 *
 * url格式： http(s)://[域名]/api/traffic/evcs/v[版本号]/[接口名称]
 * 采用POST方法提交
 * 统一采用UTF-8字符编码
 *
 * header格式：
 * 数据内容提交格式采用json格式，需要配置消息头中的Content-Type 为application/json;charset=UTF-8。
 * 授权信息Authorization字段用于证明客户端有权查看某个资源，本标准中所规范的授权信息采用令牌（Token）的方式，因此需要在配置消息头中的Authorization 为Bearer Token。
 *
 * body格式  ：
 * 数据内容提交格式采用json格式，业务数据加密传输，并对加后的数据与当前时间戳拼接后进行签名，一起传输。示例:
 * {"data":"[加密后业务数据]","timestamp"：1658736379,"signature":"[签名]"}
 *
 * 加密算法
 * 对业务内容进行加密，使用对称加密算法AES 128位加密，加密模式采用CBC，填充模式采用PKCS5Padding方式。
 *
 * 签名算法  HMACMD5
 * 签名要求  请求和接收数据均需要校验签名
 *
 * 返回结果  返回结果格式为json格式。示例：{"code":200,"msg":""}
 *
 *
 */
public class DataPush {

    /**
     * 获取token
     * @return
     *
     */
    public String getToken(JSONObject info) throws IOException {
        String urlStr = CommonParam.api_ip +CommonParam.get_token_url;
        URL url = new URL(urlStr);

//        System.out.println("url: " + urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 设置请求头
//        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", CommonParam.content_type);

        // 设置允许输出（写入请求参数）
        conn.setDoOutput(true);
        // 设置允许输入（读取响应）
        conn.setDoInput(true);

        // 设置请求体，构建JSON字符串，获取输出流并写入JSON数据
        OutputStream outputStream = conn.getOutputStream();

//        String jsonStr = java.net.URLEncoder.encode(info.toString(), "utf-8");
        String jsonStr = info.toString();
        System.out.println("request json: " + info.toString());
        byte[] input = jsonStr.getBytes(StandardCharsets.UTF_8);

//        String jsonInputString = "{\"key\":\"value\"}";
//        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

        outputStream.write(input, 0, input.length);
        outputStream.flush();
        outputStream.close();

        // 获取响应码
        int responseCode = conn.getResponseCode();
        System.out.println("token-Response Code: " + responseCode);

        // 读取响应内容.获取token
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // 打印响应内容
        JSONObject jsonObject = JSON.parseObject(response.toString());
        String token = jsonObject.get("data").toString();

        return token;
    }


    public int pushData(String token, String urlStr, JSONObject info) throws IOException {

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        // 设置请求头
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Content-Type", CommonParam.content_type);
        conn.setRequestProperty("Authorization", token);

        // 设置允许输出（写入请求参数）
        conn.setDoOutput(true);
        // 设置允许输入（读取响应）
        conn.setDoInput(true);
        conn.setConnectTimeout(5000); // 设置连接超时时间为5秒

        // 设置请求体，构建JSON字符串，获取输出流并写入JSON数据
        OutputStream outputStream = conn.getOutputStream();

        String json = info.toString();
//        System.out.println("request token: " + token + "\n");
        System.out.println("request json: " + json + "\n");

        byte[] input = json.getBytes(StandardCharsets.UTF_8);

        outputStream.write(input, 0, input.length);
        outputStream.flush();
        outputStream.close();

        // 获取响应码
        int responseCode = conn.getResponseCode();
        //token 失效
        System.out.println("Response Code：" + responseCode + "\n");
        if (responseCode == 401){
            System.out.println("Response Code: token 失效：" + responseCode + "\n");
        }else if(responseCode == 504){
            System.out.println("Response Code: 504：" + responseCode + "\n");
        } else{
            // 读取响应内容
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印响应内容
            System.out.println("response-----1"+response.toString() + "\n");
        }

        return responseCode;
    }




}
