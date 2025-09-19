package model;

public class SanPham {
    private int id;
    private String ten;
    private String hang;
    private double gia;
    private int tonKho;
    private String loai;
    private String tinhTrang;

    public SanPham() {
    }

    public SanPham(int id, String ten, String hang, double gia, int tonKho, String loai, String tinhTrang) {
        this.id = id;
        this.ten = ten;
        this.hang = hang;
        this.gia = gia;
        this.tonKho = tonKho;
        this.loai = loai;
        this.tinhTrang = tinhTrang;
    }
    
    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }
    
    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
    
    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
