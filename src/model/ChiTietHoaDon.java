package model;

public class ChiTietHoaDon {
    private long hoaDonId;
    private int sanPhamId;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(long hoaDonId, int sanPhamId, int soLuong, double donGia) {
        this.hoaDonId = hoaDonId;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters và Setters
    public long getHoaDonId() {
        return hoaDonId;
    }

    public void setHoaDonId(long hoaDonId) {
        this.hoaDonId = hoaDonId;
    }

    public int getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(int sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}
