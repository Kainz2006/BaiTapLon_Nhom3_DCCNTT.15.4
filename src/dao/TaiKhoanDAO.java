package dao;

import java.sql.Timestamp;
import java.util.List;
import model.TaiKhoan;

public interface TaiKhoanDAO {
    List<TaiKhoan> getAllTaiKhoan();
    TaiKhoan getTaiKhoanById(long id);
    TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap);
    boolean createTaiKhoan(TaiKhoan taiKhoan);
    boolean updateTaiKhoan(TaiKhoan taiKhoan);
    boolean deleteTaiKhoan(long id);
    boolean updateThoiGianDangNhapCuoi(long id, Timestamp thoiGian);
    
    // BỔ SUNG: Phương thức tìm kiếm tài khoản
    List<TaiKhoan> searchTaiKhoan(String keyword);
}