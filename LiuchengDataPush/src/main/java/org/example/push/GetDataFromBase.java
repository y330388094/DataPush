package org.example.push;

import com.alibaba.fastjson.JSONObject;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by yy on 2017/7/19.
 * 柳城数据库监听
 * 监听数据变化
 * 组织jsonobject数据并推送
 *
 */
public class GetDataFromBase implements DataGetFromBase{

    private DataTool dataTool = new DataTool();
    private final Map<Long,String> eventTableMap = new HashMap<>();
    private final DataPush dataPush = new DataPush();
    private final AccessToken accessToken = new AccessToken();

    public void test(){

        accessToken.getToken();

    }
    


    /**
     * 监听数据变化
     *
     */
    public void listen(){
        //连接数据库导入数据
//        String url = "jdbc:mysql://36.140.21.46:38569/liucheng_push";
//        // 数据库用户名
//        String user = "root";
//        // 数据库密码
//        String password = "plat123456";


        BinaryLogClient client = new BinaryLogClient("36.140.21.46", 38569, "lcdba", "123QWEasd@");
        // 反序列化配置
        EventDeserializer eventDeserializer = new EventDeserializer();
        // 设置自己的client作为服务器的id
        client.setServerId(3);

        eventDeserializer.setCompatibilityMode(EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG
//             EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        // 设置反序列化配置
        client.setEventDeserializer(eventDeserializer);

        client.registerEventListener(event -> {
            EventData data = event.getData();
//            System.out.println("data--"+data);

            //
            if (data instanceof TableMapEventData) {
                TableMapEventData tableMapEventData = (TableMapEventData) data;
//                System.out.println("0--"+tableMapEventData.getTableId()+": ["+tableMapEventData.getDatabase() + "-" + tableMapEventData.getTable()+"]");
                //存储表id与表名称对应关系
                eventTableMap.put(tableMapEventData.getTableId(), tableMapEventData.getTable());
            }

            //更新操作
            if (data instanceof UpdateRowsEventData) {
                UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) data;
                String table = eventTableMap.get(updateRowsEventData.getTableId());
                //订单完成监控
                if("order_info".equals(table)){

                    System.out.println("1--updateRowsEventData----"+updateRowsEventData.getRows());

                    if(!updateRowsEventData.getRows().isEmpty()){
                        for (Map.Entry<Serializable[], Serializable[]> row : updateRowsEventData.getRows()) {
                            Object[] objects = row.getValue();

                            String carType = "";
                            JSONObject objectData = new JSONObject();
                            objectData.put("parkingLotCode", objects[6]);   //停车场编码
                            objectData.put("parkingLotName", objects[6]);  //停车场名称
                            objectData.put("plateNumber", objects[8]);
                            objectData.put("inTime", objects[9]);
                            objectData.put("outTime", objects[10]);
                            objectData.put("parkingSpaceCode", objects[7]);  //车位编号
                            objectData.put("entranceName", "-");   //入口名称
                            objectData.put("exitName", "-");  //出口名称
                            objectData.put("inPicture", objects[20]);  //进场图片地址
                            objectData.put("outPicture", objects[21]); //出场图片地址

                            if(objects[37] != null){
                                carType = objects[37].toString();
                                if (carType != null){
                                    if (carType.equals("4")){
                                        objectData.put("isNewEnergy", true);  //是否是新能源车
                                    }else{
                                        objectData.put("isNewEnergy", false);  //是否是新能源车
                                    }
                                }else {
                                    objectData.put("isNewEnergy", false);
                                }
                            }else {
                                objectData.put("isNewEnergy", false);
                            }

                            String car_business_type = objects[43].toString();
                            if(car_business_type.equals("0")){
                                objectData.put("parkingType", "临时停车");
                            }
                            else {
                                objectData.put("parkingType", "特殊车辆月租车");  //停车类型（临时停车，月保车，其他）
                            }

                            System.out.println("objectData----："+ objectData.toString());

                            String encryptedData = null;
                            // 发送数据
                            List<JSONObject> list = new ArrayList<>();
                            JSONObject obj = new JSONObject();
                            try {
                                encryptedData = dataTool.encrypt(objectData.toString());
                                Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
                                //加密后的数据与当前时间戳拼接后进行签名
                                String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

                                obj.put("data", encryptedData);
                                obj.put("timestamp", timeStamp);
                                obj.put("signature", signature);
                                obj.put("operatorCode", CommonParam.operator_code);

                                list.add(obj);
                                // 推送数据
                                pushArrayData(list, CommonParam.api_ip + CommonParam.add_parkingRecord_url);

                            } catch (Exception e) {
                                System.out.println("未知错误----："+ e.toString());
                                throw new RuntimeException(e);
                            }

                        }



                    }
                }
            }
            //插入操作
            else if (data instanceof WriteRowsEventData) {

                WriteRowsEventData writeRowsEventData = (WriteRowsEventData) data;

                String table = eventTableMap.get(writeRowsEventData.getTableId());
                //车位信息增加
                if("space".equals(table)){

                    for (Serializable[] row : writeRowsEventData.getRows()) {
                        String dataStr = "";
                        System.out.println("insertUser--------" + dataStr);


                    }
                }

            }

        });

        try {
            client.connect();
        } catch (IOException e) {
            System.out.println("连接失败"+e.toString());
            throw new RuntimeException(e);
        }
    }


    /**
     * 静态数据传送
     * 数据加密
     *
     */
    public void pushSingleData() {

        //停车场运营单位信息增加
        JSONObject obj = new JSONObject();
        obj.put("unitName", "广西政兴投资集团有限公司");    //单位名称
        obj.put("socialCode", "0");  //统一社会信用代码
        obj.put("responsivePersonPhone", "无"); //责任人联系方式
        obj.put("licenseValidityBegin", "2022-09-02 00:00:00");  //经营许可证有效期起yyyy-MM-dd HH:mm:ss
        obj.put("licenseValidityEnd", "2024-09-02 00:00:00");  //经营许可证有效期止
        obj.put("divisionCode", "无");  //行政区划代码
        obj.put("officeAddress", "无");  //办公地址
        obj.put("registeredAddress", "无");  //注册地址

        // 转换为字符串
        String dataStr = obj.toString();

        // 字符串加密
        String encryptedData = null;
        try {
            encryptedData = dataTool.encrypt(dataStr);
            // 进行签名
            Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
            //加密后的数据与当前时间戳拼接后进行签名
            String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

            // 发送数据
            JSONObject message = new JSONObject();
            message.put("data", encryptedData);
            message.put("timestamp", System.currentTimeMillis());
            message.put("signature", signature);
            message.put("operatorCode", CommonParam.operator_code);

            dataPush.pushData(accessToken.getToken(),CommonParam.api_ip +CommonParam.add_propertyUnit_url, message);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }

    /**
     * 停车场数据导入
     */
    public void getParking(){
        //连接数据库导入数据
        String url = "jdbc:mysql://192.168.10.220:3356/liucheng_push";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "plat123456";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();
            // 执行查询并获取结果
            ResultSet rs = stmt.executeQuery("SELECT park_name,park_no,id,park_img,lon,lat,all_space,sub_start,sub_end FROM car_park");

            List<JSONObject> list = new ArrayList<>();

            while (rs.next()) {
                // 获取数据
                String park_img = rs.getString("park_img");
                String lon = rs.getString("lon");
                String lat = rs.getString("lat");
//                String sub_start = rs.getString("sub_start");
//                String sub_end = rs.getString("sub_end");

                JSONObject data = new JSONObject();
                data.put("parkingLotName", rs.getString("park_name"));
                data.put("parkingLotCode", rs.getString("park_no"));
                data.put("address", rs.getString("park_name"));
                data.put("operationUnitName", "广西长兴实业集团有限公司");  //运营单位名称
                data.put("propertyUnitName", "无");   //产权单位名称
                data.put("contactPerson", "无");  //联系人
                data.put("contactPhone", "无");  //联系电话
                data.put("longitude", lon);
                data.put("latitude", lat);
                data.put("pictureUrl", park_img);
                data.put("openTime", "全天开放");   //开放时间描述
                data.put("onRoadPosition", "车行道");  //路内位置
                data.put("parkingSpaceCount", rs.getString("all_space"));  //停车场泊位数
                data.put("parkingLotType", "公共");    //停车场类型
                data.put("buildingType", "地面停车场");  //建筑类型（地下停车场、地面停车场、地上停车楼）
                data.put("feeMode", "收费");  //收费模式（收费、免费）
                data.put("remark", "无");
                data.put("status", "0");  //状态
                data.put("parkingSpace", "0");
                data.put("parkingCount", "0");

//                System.out.println("1111111" + data.toString() + "\n");

                String encryptedData = null;
                // 发送数据
                JSONObject obj = new JSONObject();
                try {
                    encryptedData = dataTool.encrypt(data.toString());
                    Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
                    //加密后的数据与当前时间戳拼接后进行签名
                    String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

                    obj.put("data", encryptedData);
                    obj.put("timestamp", timeStamp);
                    obj.put("signature", signature);
                    obj.put("operatorCode", CommonParam.operator_code);

                    list.add(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 关闭结果集、Statement和连接
            rs.close();
            stmt.close();
            conn.close();

            // 推送数据
            pushArrayData(list, CommonParam.api_ip +CommonParam.add_parkingLot_url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * 停车场车位数据导入
     */
    public void getParkingBerth(){
        //连接数据库导入数据
        String url = "jdbc:mysql://192.168.10.220:3356/liucheng_push";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "plat123456";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();
            // 执行查询并获取结果
            ResultSet rs = stmt.executeQuery("SELECT space_no,park_id,position,space_type,car_park.park_no FROM space LEFT JOIN car_park ON car_park.id = space.park_id");

            List<JSONObject> list = new ArrayList<>();

            while (rs.next()) {
                // 获取数据
                String spaceType = rs.getString("space_type"); //车位类型 0-小型车 1-中型车  2-大型车22
                String parkingSpaceType = "";
                switch (spaceType){
                    case "0":
                        parkingSpaceType = "小型车";
                        break;
                    case "1":
                        parkingSpaceType = "中型车";
                        break;
                    case "2":
                        parkingSpaceType = "大型车";
                        break;
                }

                JSONObject data = new JSONObject();
                data.put("parkingSpaceCode", rs.getString("park_no"));
                data.put("parkingLotCode", rs.getString("space_no"));
                data.put("longitude", "0");
                data.put("latitude", "0");
                data.put("parkingSpaceType", parkingSpaceType);

                String encryptedData = null;
                // 发送数据
                JSONObject obj = new JSONObject();
                try {
                    encryptedData = dataTool.encrypt(data.toString());
                    Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
                    //加密后的数据与当前时间戳拼接后进行签名
                    String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

                    obj.put("data", encryptedData);
                    obj.put("timestamp", timeStamp);
                    obj.put("signature", signature);
                    obj.put("operatorCode", CommonParam.operator_code);

                    list.add(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 关闭结果集、Statement和连接
            rs.close();
            stmt.close();
            conn.close();

            // 推送数据
            pushArrayData(list, CommonParam.api_ip + CommonParam.add_parkingSpace_url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * 停车场进出记录导入
     */
    public void getParkingRecord(){

        //连接数据库导入数据
//        String url = "jdbc:mysql://192.168.10.220:3356/liucheng_push";
//        // 数据库用户名
//        String user = "root";
//        // 数据库密码
//        String password = "plat123456";

        //连接数据库导入数据--柳城
        String url = "jdbc:mysql://36.140.21.46:38569/pigxx";
        // 数据库用户名
        String user = "lcdba";
        // 数据库密码
        String password = "123QWEasd@";

        //获取当前时间
        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * 1));

        start = start + " 00:00:00";
        end = end + " 00:00:00";

//        start = "2024-08-03 00:00:00";
//        end = "2024-08-04 00:00:00";

        System.out.println(start + "----" + end);

        try {
            Class.forName("com.mysql.jdbc.Driver");

            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();
            // 执行查询并获取结果2024-01-22 18:18:12
            ResultSet rs = stmt.executeQuery("SELECT order_info.park_no as berth,car_id,entrance_time,exit_time,entrance_pic,exit_pic,car_type,car_business_type,car_park.park_no as park,car_park.park_name" +
                    " FROM order_info LEFT JOIN car_park ON car_park.id = order_info.park_id WHERE order_info.del_flag = 0 AND " +
                    "order_info.created_time > '" + start + "' AND order_info.created_time < '" + end + "'");


            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                // 获取数据
                String carType = "";
                JSONObject data = new JSONObject();
                data.put("parkingLotCode", rs.getString("park"));   //停车场编码
                data.put("parkingLotName", rs.getString("park_name"));  //停车场名称
                data.put("plateNumber", rs.getString("car_id"));
                data.put("inTime", rs.getString("entrance_time"));
                data.put("outTime", rs.getString("exit_time"));
                data.put("parkingSpaceCode", rs.getString("berth"));  //车位编号
                data.put("entranceName", rs.getString("park_name"));   //入口名称
                data.put("exitName", rs.getString("park_name"));  //出口名称
                data.put("inPicture", rs.getString("entrance_pic"));  //进场图片地址
                data.put("outPicture", rs.getString("exit_pic")); //出场图片地址

                carType = rs.getString("car_type");
                if (carType != null){
                    if (carType.equals("4")){
                        data.put("isNewEnergy", true);  //是否是新能源车
                    }else{
                        data.put("isNewEnergy", false);  //是否是新能源车
                    }
                }else {
                    data.put("isNewEnergy", false);
                }

                if(rs.getString("car_business_type").equals("0")){
                    data.put("parkingType", "临时停车");
                }
                else {
                    data.put("parkingType", "特殊车辆月租车");  //停车类型（临时停车，月保车，其他）
                }

                String encryptedData = null;
                // 发送数据
                JSONObject obj = new JSONObject();
                try {
                    encryptedData = dataTool.encrypt(data.toString());
                    Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
                    //加密后的数据与当前时间戳拼接后进行签名
                    String signature = dataTool.hmacMD5(encryptedData+timeStamp.toString(), CommonParam.signature_code);

                    obj.put("data", encryptedData);
                    obj.put("timestamp", timeStamp);
                    obj.put("signature", signature);
                    obj.put("operatorCode", CommonParam.operator_code);

                    list.add(obj);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 关闭结果集、Statement和连接
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("柳城-停车记录数据： -- " + list.size());
            // 推送数据
            pushArrayData(list, CommonParam.api_ip + CommonParam.add_parkingRecord_url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * 批量推送数据
     */
    public boolean pushArrayData(List<JSONObject> list, String urlStr) throws InterruptedException {

        //创建线程池
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        // 初始化计时器，用CountDownLatch（闭锁），来确保循环内的多线程都执行完成后，再执行后续代码
        final CountDownLatch cdl = new CountDownLatch(list.size());

        String token = accessToken.getToken();

        if(token.equals("")){
            System.out.println("token为空");
            return false;
        }

//        try {
//            dataPush.pushData(token, urlStr, list.get(0));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        for (JSONObject obj : list){
//            System.out.println("推送数据-" + obj.toString());

            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        int retCode = dataPush.pushData(token, urlStr, obj);
                        System.out.println("返回码--------------" + retCode);
                        cdl.countDown();
                        System.out.println("====== 柳城 线程计数 =====" + cdl.getCount());
                        // 401错误码，token失效，重新获取token
                        if (retCode == 401){
//                            System.out.println("返回码-" + retCode + "需要授权验证或token过期");
                            try {
                                accessToken.resetToken();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }

                }
            });
            // 模拟运行耗时
            Thread.sleep(1000);
        }

        // 调用闭锁的await()方法，该线程会被挂起，它会等待直到count值为0才继续执行
        // 这样我们就能确保上面多线程都执行完了才走后续代码
        System.out.println("====== 线程await =====");
        cdl.await();

        //关闭线程池
        executorService.shutdown();
        System.out.println("====== 线程结束 =====");
        return true;
    }

}
