package org.example;
import org.example.push.GetDataFromBase;
import org.example.push.GetDataFromIps;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static GetDataFromBase getDataFromBase = new GetDataFromBase();
    private static GetDataFromIps getDataFromIps = new GetDataFromIps();

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        //停车场数据推送
//        getDataFromBase.getParking();
        //停车场车位推送
//        getDataFromBase.getParkingBerth();
        //停车记录推送
//        getDataFromBase.getParkingRecord();
        //动态数据监控
//        getDataFromBase.listen();

        //停车场经营单位推送
//        getDataFromBase.pushSingleData();

        //ips 数据推送
//        getDataFromIps.getParkingRecord();

//        getDataFromIps.getParking();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        TimerTask liuChengTimerTask = new TimerTask() {
            @Override
            public void run() {
                getDataFromBase.getParkingRecord();
                System.out.println("定时任务执行中...");
            }
        };
        TimerTask ipsTimerTask = new TimerTask() {
            @Override
            public void run() {
                getDataFromIps.getParkingRecord();
                System.out.println("定时任务执行中...");
            }
        };
        //创建定时任务command：
        //要执行的任务。
        //initialDelay：首次执行任务之前的延迟时间。
        //period：两次连续任务之间的时间间隔。
        //unit：时间单位。
        executorService.scheduleAtFixedRate(liuChengTimerTask, 0, 86400, java.util.concurrent.TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(ipsTimerTask, 0, 86400, java.util.concurrent.TimeUnit.SECONDS);


//        // 创建定时任务在初始延迟时间后开始执行任务，并以固定的时间间隔重复执行任务。
//        Future<?> future = executorService.scheduleAtFixedRate(liuChengTimerTask, 0, 10, java.util.concurrent.TimeUnit.SECONDS);
//        // 检查任务是否执行完成
//        if (!future.isDone()) {
//            // 任务还未执行完成，可以选择等待一段时间或进行其他操作
//            System.out.println("任务还未执行完成");
//        }



    }

}