package model;

import java.sql.Timestamp;

public class TaiKhoan {
    private long id;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro; // "admin" hoặc "nhanvien"
    private Timestamp thoiGianDangNhapCuoi;

    public TaiKhoan() {
    }

    public TaiKhoan(long id, String tenDangNhap, String matKhau, String vaiTro, Timestamp thoiGianDangNhapCuoi) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.thoiGianDangNhapCuoi = thoiGianDangNhapCuoi;
    }
    
    // Getters và Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
    
    public Timestamp getThoiGianDangNhapCuoi() {
        return thoiGianDangNhapCuoi;
    }

    public void setThoiGianDangNhapCuoi(Timestamp thoiGianDangNhapCuoi) {
        this.thoiGianDangNhapCuoi = thoiGianDangNhapCuoi;
    }
}
