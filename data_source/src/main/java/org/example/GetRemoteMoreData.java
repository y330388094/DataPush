package org.example;

import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class GetRemoteMoreData {

    private SaveData saveData = new SaveData();

    private List<DBConnectVo> connections = new ArrayList<>();

    public void addConnection() {
        DBConnectVo conn1 = new DBConnectVo("jdbc:mysql://36.140.21.46:38569/pigxx", "lcdba", "123QWEasd@");
        connections.add(conn1);

        DBConnectVo conn2 = new DBConnectVo("jdbc:mysql://rm-wz9z3ymc75p1slr8k5o.mysql.rds.aliyuncs.com:3306/ips_test", "root", "Yg@_+123");
        connections.add(conn2);

    }

    //获取数据库连接,线程池
    public void getDataFromDB() {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                connections.size(),  //线程池中的常驻核心线程数
                connections.size(), //线程池能够容纳同时执行的最大线程数
                0L, //多余的空闲线程的存活时间
                java.util.concurrent.TimeUnit.MILLISECONDS, //单位
                new java.util.concurrent.LinkedBlockingQueue<Runnable>(), //任务队列
                Executors.defaultThreadFactory(),  //线程工厂
                new ThreadPoolExecutor.AbortPolicy() //拒绝策略
        );


        for (DBConnectVo conn : connections) {
            pool.submit(new Callable<String>(){
                @Override
                public String call() throws Exception {
                    String url = conn.getUrl();
                    String username = conn.getUsername();
                    String password = conn.getPassword();

                    //连接数据库导入数据
                    getParkingRecord();


                    return null;
                }
            });
        }

    }




    /**
     * 停车场数据导入
     */
    public void getParking(){
        //连接数据库导入数据
        String url = "jdbc:mysql://rm-wz9z3ymc75p1slr8k5o.mysql.rds.aliyuncs.com:3306/ips";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "Yg@_+123";

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
        String url = "jdbc:mysql://36.140.21.46:38569/pigxx";
        // 数据库用户名
        String user = "lcdba";
        // 数据库密码
        String password = "123QWEasd@";

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

            String countSql = "Select count(*) from order_info where del_flag = 0";
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
                // 执行查询并获取结果2024-01-22 18:18:12
                String sql = "SELECT O.order_no,O.park_id,O.park_no,O.car_id,O.entrance_time,O.exit_time,O.park_time,O.total_amount,O.pay_state,O.which_pay,O.which_pay,O.transaction_id," +
                        "O.out_trade_no,O.trade_no,O.entrance_pic,O.exit_pic,O.custom_order_no,O.refunded_money,O.car_no_color,O.invoice,O.free_parking,O.park_event," +
                        "O.car_type,O.error_state,O.car_business_type,O.park_state,O.updated_time,O.created_time,P.park_name FROM order_info as O LEFT JOIN car_park as P ON O.park_id = P.id WHERE " +
                        "O.del_flag = 0 LIMIT " + i + ",100";


                ResultSet rs1 = stmt.executeQuery(sql);

                Future<String> future = executorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {

                        ResultSet rs = stmt.executeQuery(sql);
                        List<OrderDataVo> list = new ArrayList<>();

                        while (rs.next()) {
                            // 获取数据
//                            System.out.println("rs---：" + rs.getString("order_no") + "---" + rs.getString("order_no"));
                            OrderDataVo orderDataVo = new OrderDataVo();
                            orderDataVo.setOrder_no(rs.getString("order_no")); //订单编号
                            orderDataVo.setPay_order_no(rs.getString("trade_no")); //支付宝交易流水号
                            orderDataVo.setAli_trade_no(rs.getString("out_trade_no"));
                            orderDataVo.setApplication_refund_money(rs.getString("refunded_money"));  //
                            orderDataVo.setCar_business_type(rs.getString("car_business_type"));  //车辆业务类型：0：临时车1：特殊车辆月租车
                            orderDataVo.setCar_id(rs.getString("car_id"));
                            orderDataVo.setCar_no_color(rs.getString("car_no_color"));
                            orderDataVo.setCar_type(rs.getString("car_type"));

                            if (rs.getString("error_state") == "1"){
                                orderDataVo.setCheck_state("0");  //异常状态 0正常1未处理2已处理
                            } else {
                                orderDataVo.setCheck_state("1");  //4恶意欠费
                            }

                            orderDataVo.setEntrance_pic(rs.getString("entrance_pic"));
                            orderDataVo.setEntrance_time(rs.getString("entrance_time"));
                            orderDataVo.setError_state(rs.getString("error_state")); //异常状态 1正常2异常3法院预警4恶意欠费
                            orderDataVo.setError_type("");//异常类型 0正常1车牌不一致2遮挡离场3一位多车4一车多位5有离位无入位
                            orderDataVo.setExit_pic(rs.getString("exit_pic"));
                            orderDataVo.setExit_time(rs.getString("exit_time") == null ? rs.getString("entrance_time") : rs.getString("exit_time"));
                            orderDataVo.setFree_parking(rs.getString("free_parking")); //免费停车 1免费2收费
                            orderDataVo.setInvoice(rs.getString("invoice"));  //是否开票
                            orderDataVo.setPark_event(rs.getString("park_event"));  //停车事件1 正常位停车 2 斜位停车 3 跨位停车 4 半侧位停车 5 逆向停车 6 反复入位 7 压线停车 8 遮挡号牌
                            orderDataVo.setPark_id(rs.getString("park_id"));
                            orderDataVo.setPark_name(rs.getString("park_name"));
                            orderDataVo.setPark_no("");
                            orderDataVo.setPark_state(rs.getString("park_state"));//1已入场2已离场
                            orderDataVo.setPay_state(rs.getString("pay_state"));  // 1未支付2已支付3已退款4线下结算

                            if (rs.getString("which_pay") != null){
                                if(rs.getString("which_pay").equals("1")){
                                    orderDataVo.setPay_way("微信支付"); //支付方式 1微信小程序2支付宝小程序3APP
                                } else if (rs.getString("which_pay").equals("2")) {
                                    orderDataVo.setPay_way("支付宝");
                                } else if (rs.getString("which_pay").equals("3")) {
                                    orderDataVo.setPay_way("APP");
                                }
                            }else {
                                orderDataVo.setPay_way("未知");
                            }

                            orderDataVo.setRefund_appeal_state("");
                            orderDataVo.setRefund_reason("");
                            orderDataVo.setRefund_user("");
                            orderDataVo.setTotal_amount(rs.getString("total_amount") == null ? "0" : rs.getString("total_amount"));
                            orderDataVo.setUpdated_time(rs.getString("updated_time") == null ? rs.getString("created_time") : rs.getString("updated_time"));
                            orderDataVo.setWechat_trade_id(rs.getString("transaction_id"));
                            orderDataVo.setBerth_no(rs.getString("park_no"));

                            list.add(orderDataVo);
                        }
                        String ret = saveData.connectToDb(list);
                        rs.close();
                        return ret;
                    }

                });
                String result = future.get(); //等待执行完获取结果
                System.out.println("result----" + result + "----" + i);
            }
            // 关闭结果集、Statement和连接
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
