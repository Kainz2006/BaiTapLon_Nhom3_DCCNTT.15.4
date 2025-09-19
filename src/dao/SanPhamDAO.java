package dao;

import model.SanPham;
import java.sql.SQLException;
import java.util.List;

public interface SanPhamDAO {
    List<SanPham> getAllSanPham();
    SanPham getSanPhamById(int id);
    boolean createSanPham(SanPham sanPham);
    boolean updateSanPham(SanPham sanPham);
    boolean deleteSanPham(int id);
    boolean updateSoLuongTon(long sanPhamId, int soLuong) throws SQLException;
}