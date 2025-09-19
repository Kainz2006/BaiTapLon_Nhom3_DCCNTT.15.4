package model;

import java.sql.Timestamp;

public class HoaDon {
    private long id;
    private Timestamp ngayLap;
    private double tongTien;
    private long khachHangId;
    private long taiKhoanId;

    public HoaDon() {
    }

    public HoaDon(long id, Timestamp ngayLap, double tongTien, long khachHangId, long taiKhoanId) {
        this.id = id;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.khachHangId = khachHangId;
        this.taiKhoanId = taiKhoanId;
    }
    
    // Getters và Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public long getKhachHangId() {
        return khachHangId;
    }

    public void setKhachHangId(long khachHangId) {
        this.khachHangId = khachHangId;
    }

    public long getTaiKhoanId() {
        return taiKhoanId;
    }

    public void setTaiKhoanId(long taiKhoanId) {
        this.taiKhoanId = taiKhoanId;
    }
}
