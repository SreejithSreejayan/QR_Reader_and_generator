package com.sreejithsreejayan.qrreaderandgenerator;

import java.util.ArrayList;

public class QRHistory {
    private int id;
    private String QREncodedData;
    private String Date;

    public QRHistory() {
    }

    public QRHistory(int id, String QREncodedData, String date) {
        this.id = id;
        this.QREncodedData = QREncodedData;
        Date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQREncodedData() {
        return QREncodedData;
    }

    public void setQREncodedData(String QREncodedData) {
        this.QREncodedData = QREncodedData;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
