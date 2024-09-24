package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static GetRemoteIpsData getRemoteIpsData = new GetRemoteIpsData();
    private static GetRemoteMoreData getRemoteMoreData = new GetRemoteMoreData();

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

//        getRemoteIpsData.getParkingRecord();

        getRemoteMoreData.getParkingRecord();
    }
}