package org.example.push;

/**
 * 公共参数
 *
 */
public interface CommonParam {



    // 加密算法
    String ALGORITHM = "AES/CBC/PKCS5Padding";

    // 推送接口
    String api_ip = "http://222.84.136.172:38889";

    // 运营商代码
    String operator_code = "574409725";

    // 运营商密码
    String operator_pwd = "qpy2ibJ03J9M9mzS";

    // 加密码
    String encryption_code = "r1hsmFCqmtdJBjuC";

    // 初始化向量
    String init_data = "7TZam2zfGfhU0Ezq";

    // 签名密钥
    String signature_code = "5W0oFKjpIQUIvX4D";

    String content_type = "application/json;charset=UTF-8";



    // 获取token接口
    String get_token_url = "/api/traffic/evcs/v1/parkingLot/token";

    // 停车场产权单位信息增加
    String add_propertyUnit_url = "/api/traffic/evcs/v1/parkingLot/propertyUnit/add";

    // 停车场运营单位信息增加
    String add_operatorUnit_url = "/api/traffic/evcs/v1/parkingLot/operatorUnit/add";

    // 停车场基础信息增加
    String add_parkingLot_url = "/api/traffic/evcs/v1/parkingLot/add";

    // 停车场出入口信息增加
    String add_entrance_url = "/api/traffic/evcs/v1/parkingLot/entrance/add";

    // 停车场车位信息增加
    String add_parkingSpace_url = "/api/traffic/evcs/v1/parkingLot/parkingSpace/add";

    // 停车场道闸信息增加
    String add_gate_url2 = "/api/traffic/evcs/v1/parkingLot/gate/add";

    // 停车场摄像头信息增加
    String add_camera_url = "/api/traffic/evcs/v1/parkingLot/camera/add";

    // 停车场停车记录增加
    String add_parkingRecord_url = "/api/traffic/evcs/v1/parkingLot/parkingRecord/add";


}


