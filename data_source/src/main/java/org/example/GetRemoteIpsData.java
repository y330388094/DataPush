package org.example;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetRemoteIpsData {


    private SaveData saveData = new SaveData();

    /**
     * 停车场数据导入
     */
    public void getParking(){
        //连接数据库导入数据
        String url = "jdbc:mysql://:3306/ips";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();
            // 执行查询并获取结果
            ResultSet rs = stmt.executeQuery("SELECT parkName,parkKey,parkAddress,parkTel,parkLinkman,parkLots,parkY,parkX,corpName,parkType FROM park_info WHERE " +
                    "createUserName <> 'test' AND id <> 39 AND id <> 38 AND id <> 35 AND id <> 34 AND id <> 33 AND " +
                    "id <> 27 AND id <> 26 AND id <> 28 AND id <> 25 AND id <> 32 ");

            List<OrderDataVo> list = new ArrayList<>();

            while (rs.next()) {
                // 获取数据


                JSONObject data = new JSONObject();
                data.put("parkingLotName", rs.getString("parkName"));
                data.put("parkingLotCode", rs.getString("parkKey"));
                data.put("address", rs.getString("parkAddress"));
                data.put("operationUnitName", rs.getString("corpName"));  //运营单位名称
                data.put("propertyUnitName", "无");   //产权单位名称
                data.put("contactPerson", rs.getString("parkLinkman"));  //联系人
                data.put("contactPhone", rs.getString("parkTel"));  //联系电话
                data.put("longitude", rs.getString("parkY"));
                data.put("latitude", rs.getString("parkX"));
                data.put("pictureUrl", "-");
                data.put("openTime", "全天开放");   //开放时间描述
                data.put("onRoadPosition", "车行道");  //路内位置（车行道，人行道）
                data.put("parkingSpaceCount", rs.getString("parkLots"));  //停车场泊位数
                data.put("parkingLotType", "公共");    //停车场类型（公共、专用、道路）
                data.put("buildingType", "地下停车场");  //建筑类型（地下停车场、地面停车场、地上停车楼）
                data.put("feeMode", "收费");  //收费模式（收费、免费）
                data.put("remark", "无");
                data.put("status", "0");  //状态
                data.put("parkingSpace", "0");
                data.put("parkingCount", "0");

            }

            // 关闭结果集、Statement和连接
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("停车记录数据： -- " + list.size() + "\n");



        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * 停车场进出记录导入
     */
    public void getParkingRecord(){

        //连接数据库导入数据--柳城
        String url = "jdbc:mysql://rm-wz9z3ymc75p1slr8k5o.mysql.rds.aliyuncs.com:3306/ips";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "Yg@_+123";

        //获取当前时间
        String end = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String start = new SimpleDateFormat("yyyy-MM-dd").format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000 * 1 ));

        start = start + " 00:00:00";
        end = end + " 00:00:00";

        System.out.println(start + "----" + end);

        try {
            Class.forName("com.mysql.jdbc.Driver");

            // 建立数据库连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 创建Statement对象执行SQL语句
            Statement stmt = conn.createStatement();

            String countSql = "Select count(*) from order_info_2020";
//
//            String sql = "SELECT orderNo,tradeNo,payOrderNo,parkId,parkName,carNo,enterTime,outTime,enterGateName,outGateName,enterImgPath,outImgPath," +
//                    "carTypeName,carType,orderStatus,payStatus,payTime,payTypeName,totalAmount FROM order_info_2019";

            ResultSet rst = stmt.executeQuery(countSql);
            Integer total = 0;
            while (rst.next()) {
                // 获取数据
                total = rst.getInt(1);
                System.out.println("总数：" + rst.getInt(1));
            }
            rst.close();

            ExecutorService executorService = Executors.newFixedThreadPool(1);

            for (int i = 0; i < total; i = i + 100){

                String sql = "SELECT orderNo,tradeNo,payOrderNo,parkId,parkName,carNo,enterTime,outTime,enterGateName,outGateName,enterImgPath,outImgPath," +
                        "carTypeName,carType,orderStatus,payStatus,payTime,payTypeName,totalAmount FROM order_info_2023 LIMIT " + i + ",100";
//                System.out.println("sql：" + sql);

                Future<String> future = executorService.submit(new Callable<String>(){
                    @Override
                    public String call() throws Exception {

                        ResultSet rs = stmt.executeQuery(sql);
                        List<OrderDataVo> list = new ArrayList<>();
                        while (rs.next()) {
                            // 获取数据
//                            System.out.println("rs---：" + rs.getString("orderNo") + "---" + rs.getString("payOrderNo"));
                            OrderDataVo orderDataVo = new OrderDataVo();
                            orderDataVo.setOrder_no(rs.getString("orderNo")); //系统内部订单号
                            orderDataVo.setPay_order_no(rs.getString("payOrderNo"));
                            orderDataVo.setAli_trade_no("");
                            orderDataVo.setTrade_no(rs.getString("tradeNo"));
                            orderDataVo.setApplication_refund_money("");  //
                            orderDataVo.setCar_business_type(rs.getString("carType"));  //车辆业务类型：0：临时车1：特殊车辆月租车
                            orderDataVo.setCar_id(rs.getString("carNo"));
                            orderDataVo.setCar_no_color("");
                            orderDataVo.setCar_type("");
                            orderDataVo.setCheck_state(rs.getString("orderStatus"));  //异常状态 0正常1未处理2已处理
                            orderDataVo.setEntrance_pic(rs.getString("enterImgPath"));
                            orderDataVo.setEntrance_time(rs.getString("enterTime"));
                            orderDataVo.setError_state(""); //异常状态 1正常2异常3法院预警4恶意欠费
                            orderDataVo.setError_type("");//异常类型 0正常1车牌不一致2遮挡离场3一位多车4一车多位5有离位无入位
                            orderDataVo.setExit_pic(rs.getString("outImgPath"));
                            orderDataVo.setExit_time(rs.getString("outTime") == null ? rs.getString("enterTime") : rs.getString("outTime"));
                            orderDataVo.setFree_parking(""); //免费停车 1免费2收费
                            orderDataVo.setInvoice("");  //是否开票
                            orderDataVo.setPark_event("");  //停车事件1 正常位停车 2 斜位停车 3 跨位停车 4 半侧位停车 5 逆向停车 6 反复入位 7 压线停车 8 遮挡号牌
                            orderDataVo.setPark_id(rs.getString("parkId"));
                            orderDataVo.setPark_name(rs.getString("parkName"));
                            orderDataVo.setPark_no("");
                            orderDataVo.setPark_state("");
                            orderDataVo.setPay_state(rs.getString("payStatus"));
                            orderDataVo.setPay_way(rs.getString("payTypeName")); //支付方式 1微信小程序2支付宝小程序3APP
                            orderDataVo.setRefund_appeal_state("");
                            orderDataVo.setRefund_reason("");
                            orderDataVo.setRefund_user("");
                            orderDataVo.setTotal_amount(rs.getString("totalAmount") == null ? "0" : rs.getString("totalAmount"));
                            orderDataVo.setUpdated_time(rs.getString("payTime") == null ? "2019-07-23 14:59:45" : rs.getString("payTime"));
                            orderDataVo.setWechat_trade_id("");

                            list.add(orderDataVo);
                        }

                        String ret = saveData.connectToDb(list);
                        // 关闭结果集
                        rs.close();
                        return ret;
                    }
                });

                String result = future.get(); //阻塞---等待执行完获取结果
                System.out.println("result----" + result + "----" + i);
            }

            // Statement和连接
            stmt.close();
            conn.close();

            executorService.shutdown();
            System.out.println("采集完成----");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }



}
