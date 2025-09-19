package dao;

import model.TaiKhoan;
import java.sql.Timestamp; // Thêm import này
import java.util.List;

public interface TaiKhoanDAO {
    List<TaiKhoan> getAllTaiKhoan();
    TaiKhoan getTaiKhoanById(long id);
    TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap);
    boolean createTaiKhoan(TaiKhoan taiKhoan);
    boolean updateTaiKhoan(TaiKhoan taiKhoan);
    boolean deleteTaiKhoan(long id);
    boolean updateThoiGianDangNhapCuoi(long id, Timestamp thoiGian);
}