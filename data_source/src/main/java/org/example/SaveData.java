package org.example;

import java.sql.*;
import java.util.List;

public class SaveData {


    public String connectToDb(List<OrderDataVo> list) throws Exception {
        String url = "jdbc:mysql://192.168.10.194:3306/pre_data";
        String user = "root";
        String password = "123456";

        String value = "";
        for (OrderDataVo orderDataVo : list){

//            System.out.println("orderData：" + orderDataVo.toString());
            value += "(";
            value +="'" + orderDataVo.getOrder_no() + "',";
            value += "'" + orderDataVo.getUpdated_time() + "',";
            value += "'" + orderDataVo.getPark_id() + "',";
            value += "'" + orderDataVo.getPark_name() + "',";
            value += "'" + orderDataVo.getPark_no() + "',";
            value += "'" + orderDataVo.getCar_id() + "',";
            value += "'" + orderDataVo.getEntrance_time() + "',";
            value += "'" + orderDataVo.getExit_time() + "',";
            value += "'" + orderDataVo.getPark_time() + "',";
            value += "'" + orderDataVo.getTotal_amount() + "',";
            value += "'" + orderDataVo.getPark_state() + "',";
            value += "'" + orderDataVo.getPay_state() + "',";
            value += "'" + orderDataVo.getPay_way() + "',";
            value += "'" + orderDataVo.getWechat_trade_id() + "',";
            value += "'" + orderDataVo.getAli_trade_no() + "',";
            value += "'" + orderDataVo.getTrade_no() + "',";
            value += "'" + orderDataVo.getEntrance_pic() + "',";
            value += "'" + orderDataVo.getExit_pic() + "',";
            value += "'" + orderDataVo.getCar_no_color() + "',";
            value += "'" + orderDataVo.getInvoice() + "',";
            value += "'" + orderDataVo.getFree_parking() + "',";
            value += "'" + orderDataVo.getCar_type() + "',";
            value += "'" + orderDataVo.getPark_event() + "',";
            value += "'" + orderDataVo.getError_state() + "',";
            value += "'" + orderDataVo.getOrder_type() + "',";
            value += "'" + orderDataVo.getCar_business_type() + "',";
            value += "'" + orderDataVo.getCheck_state() + "',";
            value += "'" + orderDataVo.getError_type() + "',";
            value += "'" + orderDataVo.getApplication_refund_money() + "',";
            value += "'" + orderDataVo.getRefund_reason() + "',";
            value += "'" + orderDataVo.getRefund_user() + "',";
            value += "'" + orderDataVo.getRefund_appeal_state() + "',";
            value += "'" + orderDataVo.getPay_order_no() + "',";
            value += "'" + orderDataVo.getPay_berth_no() + "'";
            value += "),";
        }

        value = value.substring(0, value.length() - 1);

        String sql = "INSERT INTO order_liucheng (order_no, updated_time,park_id,park_name,park_no,car_id,entrance_time,exit_time," +
                "park_time,total_amount,park_state,pay_state,pay_way,wechat_trade_id,ali_trade_no,trade_no,entrance_pic," +
                "exit_pic,car_no_color,invoice,free_parking,car_type,park_event,error_state,order_type,car_business_type," +
                "check_state,error_type,application_refund_money,refund_reason,refund_user,refund_appeal_state,pay_order_no,berth_no) VALUES "+ value;



        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数值,sql 语句中的参数索引从1开始，如问号
//            pstmt.setString(1, "value1");
//            pstmt.setInt(2, 22);

            // 执行插入
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("插入成功----");
                return "1";
            }else {
                System.out.println("插入失败----");
                return "0";
            }


        } catch (SQLException e) {
            System.out.println("Failed to insert data!---" + sql);
            e.printStackTrace();

            return "2";
        }


    }

}
