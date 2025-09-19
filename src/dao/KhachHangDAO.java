package dao;

import model.KhachHang;
import java.sql.SQLException;
import java.util.List;

public interface KhachHangDAO {
    List<KhachHang> getAllKhachHang();
    KhachHang getKhachHangById(long id);
    boolean createKhachHang(KhachHang khachHang);
    boolean updateKhachHang(KhachHang khachHang);
    boolean deleteKhachHang(long id);
    // Thêm dòng này để khai báo phương thức mới
    boolean khachHangCoHoaDon(long khachHangId) throws SQLException;
}