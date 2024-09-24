package org.example;

public class OrderDataVo {
    private String order_no;
    private String updated_time;
    private String park_id;
    private String park_name;
    private String park_no;
    private String car_id;
    private String entrance_time;
    private String exit_time;
    private String park_time;
    private String total_amount;
    private String park_state;
    private String pay_state;
    private String pay_way;
    private String wechat_trade_id;
    private String pay_order_no;
    private String ali_trade_no;
    private String trade_no;
    private String entrance_pic;
    private String exit_pic;
    private String car_no_color;
    private String invoice;
    private String free_parking;
    private String car_type;
    private String park_event;
    private String error_state;
    private String order_type;
    private String car_business_type;
    private String check_state;
    private String error_type;
    private String application_refund_money;
    private String refund_reason;
    private String refund_user;
    private String refund_appeal_state;
    private String berth_no;


    public OrderDataVo() {

        this.order_no = "";
        this.updated_time = "";
        this.park_id = "";
        this.park_name = "";
        this.park_no = "";
        this.car_id = "";
        this.entrance_time = "";
        this.exit_time = "";
        this.park_time = "";
        this.total_amount = "";
        this.park_state = "";
        this.pay_state = "";
        this.pay_way = "";
        this.wechat_trade_id = "";
        this.ali_trade_no = "";
        this.pay_order_no = "";
        this.trade_no = "";
        this.entrance_pic = "";
        this.exit_pic = "";
        this.car_no_color = "";
        this.invoice = "";
        this.free_parking = "";
        this.car_type = "";
        this.park_event = "";
        this.error_state = "";
        this.order_type = "";
        this.car_business_type = "";
        this.check_state = "";
        this.error_type = "";
        this.application_refund_money = "";
        this.refund_reason = "";
        this.refund_user = "";
        this.refund_appeal_state = "";
        this.berth_no = "";

    }
    public String getPay_berth_no() {return berth_no;}

    public void setBerth_no(String berth_no) {
        this.berth_no = berth_no;
    }

    public String getRefund_appeal_state() {
        return refund_appeal_state;
    }

    public void setRefund_appeal_state(String refund_appeal_state) {
        this.refund_appeal_state = refund_appeal_state;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public void setParking_name(String parkingName) {
        this.park_name = parkingName;
    }

    public String getPark_id() {
        return park_id;
    }

    public void setPark_id(String park_id) {
        this.park_id = park_id;
    }

    public String getPark_name() {
        return park_name;
    }

    public String getCar_business_type() {
        return car_business_type;
    }

    public void setCar_business_type(String car_business_type) {
        this.car_business_type = car_business_type;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public String getPark_no() {
        return park_no;
    }

    public void setPark_no(String park_no) {
        this.park_no = park_no;
    }

    public String getEntrance_time() {
        return entrance_time;
    }

    public void setEntrance_time(String entrance_time) {
        this.entrance_time = entrance_time;
    }

    public String getExit_time() {
        return exit_time;
    }

    public void setExit_time(String exit_time) {
        this.exit_time = exit_time;
    }

    public String getPark_time() {
        return park_time;
    }

    public void setPark_time(String park_time) {
        this.park_time = park_time;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTrade_no() {
        return this.trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getPark_state() {
        return park_state;
    }

    public void setPark_state(String park_state) {
        this.park_state = park_state;
    }

    public String getPay_state() {
        return pay_state;
    }

    public void setPay_state(String pay_state) {
        this.pay_state = pay_state;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getWechat_trade_id() {
        return wechat_trade_id;
    }

    public String getAli_trade_no() {
        return ali_trade_no;
    }

    public void setAli_trade_no(String ali_trade_no) {
        this.ali_trade_no = ali_trade_no;
    }

    public void setWechat_trade_id(String wechat_transaction_id) {
        this.wechat_trade_id = wechat_transaction_id;
    }

    public String getEntrance_pic() {
        return entrance_pic;
    }

    public void setEntrance_pic(String entrance_pic) {
        this.entrance_pic = entrance_pic;
    }

    public String getExit_pic() {
        return exit_pic;
    }

    public void setExit_pic(String exit_pic) {
        this.exit_pic = exit_pic;
    }

    public String getCar_no_color() {
        return car_no_color;
    }

    public void setCar_no_color(String car_no_color) {
        this.car_no_color = car_no_color;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getFree_parking() {
        return free_parking;
    }

    public void setFree_parking(String free_parking) {
        this.free_parking = free_parking;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getPark_event() {
        return park_event;
    }

    public void setPark_event(String park_event) {
        this.park_event = park_event;
    }

    public String getError_state() {
        return error_state;
    }

    public void setError_state(String error_state) {
        this.error_state = error_state;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getCheck_state() {
        return check_state;
    }

    public void setCheck_state(String check_state) {
        this.check_state = check_state;
    }

    public String getError_type() {
        return error_type;
    }

    public void setError_type(String error_type) {
        this.error_type = error_type;
    }

    public String getApplication_refund_money() {
        return application_refund_money;
    }

    public void setApplication_refund_money(String application_refund_money) {
        this.application_refund_money = application_refund_money;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }

    public String getRefund_user() {
        return refund_user;
    }

    public void setRefund_user(String refund_user) {
        this.refund_user = refund_user;
    }

    public String getPay_order_no() {
        return pay_order_no;
    }

    public void setPay_order_no(String pay_order_no) {
        this.pay_order_no = pay_order_no;
    }

}
