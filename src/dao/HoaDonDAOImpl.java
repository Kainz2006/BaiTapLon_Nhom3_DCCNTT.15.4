package dao;

import helper.DBHelper;
import model.HoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAOImpl implements HoaDonDAO {

    @Override
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
            while (rs.next()) {
                list.add(new HoaDon(
                    rs.getLong("id"),
                    rs.getTimestamp("ngayLap"),
                    rs.getDouble("tongTien"),
                    rs.getLong("khachHangId"),
                    rs.getLong("taiKhoanId")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public HoaDon getHoaDonById(long id) {
        String sql = "SELECT * FROM hoadon WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HoaDon(
                        rs.getLong("id"),
                        rs.getTimestamp("ngayLap"),
                        rs.getDouble("tongTien"),
                        rs.getLong("khachHangId"),
                        rs.getLong("taiKhoanId")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long createHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO hoadon (tongTien, khachHangId, taiKhoanId) VALUES (?, ?, ?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, hoaDon.getTongTien());
            ps.setLong(2, hoaDon.getKhachHangId());
            ps.setLong(3, hoaDon.getTaiKhoanId());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean deleteHoaDon(long id) {
        String sql = "DELETE FROM hoadon WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // BỔ SUNG: Phương thức tìm kiếm hóa đơn
    @Override
    public List<HoaDon> searchHoaDon(String keyword) {
        List<HoaDon> list = new ArrayList<>();
        // Câu lệnh SQL này tìm kiếm theo ID Hóa đơn, Tên Đăng nhập của Nhân viên (từ bảng TaiKhoan) 
        // hoặc Tên Khách hàng (từ bảng KhachHang).
        // Chúng ta sử dụng LEFT JOIN để đảm bảo hóa đơn vẫn được trả về ngay cả khi thông tin KH/TK bị thiếu.
        String sql = "SELECT h.* " +
                     "FROM hoadon h " +
                     "LEFT JOIN taikhoan tk ON h.taiKhoanId = tk.id " +
                     "LEFT JOIN khachhang kh ON h.khachHangId = kh.id " +
                     "WHERE CAST(h.id AS CHAR) LIKE ? OR tk.tenDangNhap LIKE ? OR kh.hoTen LIKE ?";

        String searchKeyword = "%" + keyword + "%";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBHelper.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, searchKeyword); // Tìm theo ID Hóa đơn (dạng chuỗi)
            ps.setString(2, searchKeyword); // Tìm theo Tên Đăng nhập Nhân viên
            ps.setString(3, searchKeyword); // Tìm theo Họ Tên Khách hàng
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new HoaDon(
                    rs.getLong("id"),
                    rs.getTimestamp("ngayLap"),
                    rs.getDouble("tongTien"),
                    rs.getLong("khachHangId"),
                    rs.getLong("taiKhoanId")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng tài nguyên
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }
}