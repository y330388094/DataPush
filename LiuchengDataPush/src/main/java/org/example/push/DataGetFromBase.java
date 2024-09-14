package org.example.push;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DataGetFromBase {

    void getParking();
    void getParkingBerth();
    void getParkingRecord();
    void listen();
    boolean pushArrayData(List<JSONObject> list, String urlStr) throws InterruptedException;

}
