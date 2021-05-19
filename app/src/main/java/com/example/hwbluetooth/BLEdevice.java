package com.example.hwbluetooth;

public class BLEdevice {
    private String RSSI;
    private String content;

    public BLEdevice(String content,String RSSI) {
//        this.devicemac = devicemac;
        this.RSSI = RSSI;
        this.content = content;
    }
//
//    public void setDevicemac(String devicemac) {
//        this.devicemac = devicemac;
//    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }
    //
    public void setContent(String content) {
        this.content = content;
    }
    //
//    public String getDevicemac() {
//        return devicemac;
//    }
//
    public String getRSSI() {
        return RSSI;
    }

    public String getContent() {
        return content;
    }




}