package org.example.push;

import com.alibaba.fastjson.JSONObject;

/**
 * token使用类
 * 未使用
 */
public class AccessToken {

    private DataTool dataTool = new DataTool();
    private final DataPush dataPush = new DataPush();

    private String token = "";
    private int create_time = 0;
    private int available_time = 0;

    public String getToken(){
        if (isValid() || token.equals("")){
            try {
                resetToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.token;
    }

    /**
     * 判断token是否过期
     * 有效时间为1天， 单位秒
     * @return
     */
    public boolean isValid(){
        return (System.currentTimeMillis() / 1000) >= (create_time + this.available_time);
    }

    public void resetToken () throws Exception {
        JSONObject data = new JSONObject();
        data.put("operatorSecret", CommonParam.operator_pwd);

        // 转换为字符串
        String dataStr = data.toString();

        // 字符串加密
        String encryptedData = dataTool.encrypt(dataStr);
        // 进行签名
        Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
        //加密后的数据与当前时间戳拼接后进行签名
        String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

        // 发送数据
        JSONObject message = new JSONObject();
        message.put("data", encryptedData);
        message.put("timestamp", timeStamp);
        message.put("signature", signature);
        message.put("operatorCode", CommonParam.operator_code);

        //将token进行解密
        String tokenStr = dataTool.decrypt(dataPush.getToken(message));
        JSONObject jsonObject = JSONObject.parseObject(tokenStr);

        System.out.println("tokenStr: " + tokenStr + "\n");
        this.create_time = (int) (System.currentTimeMillis() / 1000);
        this.available_time = (int) jsonObject.get("TokenAvailableTime");
        this.token = jsonObject.get("TokenType").toString() + " " + jsonObject.get("AccessToken").toString() ;
    }

}
