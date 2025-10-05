package dao;
import java.sql.SQLException;
import java.util.List;
import model.KhachHang;

public interface KhachHangDAO {
    List<KhachHang> getAllKhachHang();
    KhachHang getKhachHangById(long id);
    boolean createKhachHang(KhachHang khachHang);
    boolean updateKhachHang(KhachHang khachHang);
    boolean deleteKhachHang(long id);
    boolean khachHangCoHoaDon(long khachHangId) throws SQLException;
    
    // BỔ SUNG: Phương thức tìm kiếm khách hàng
    List<KhachHang> searchKhachHang(String keyword);
}