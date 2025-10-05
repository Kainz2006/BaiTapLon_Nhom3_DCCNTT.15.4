package dao;

import java.sql.SQLException;
import java.util.List;
import model.SanPham;

public interface SanPhamDAO {
    List<SanPham> getAllSanPham();
    SanPham getSanPhamById(int id);
    boolean createSanPham(SanPham sanPham);
    boolean updateSanPham(SanPham sanPham);
    boolean deleteSanPham(int id);
    boolean updateSoLuongTon(long sanPhamId, int soLuong) throws SQLException;
    
    // BỔ SUNG: Phương thức tìm kiếm sản phẩm
    List<SanPham> searchSanPham(String keyword);
}