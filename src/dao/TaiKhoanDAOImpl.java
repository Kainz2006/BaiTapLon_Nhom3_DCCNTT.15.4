package dao;

import helper.DBHelper;
import model.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAOImpl implements TaiKhoanDAO {
    @Override
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setId(rs.getLong("id"));
                tk.setTenDangNhap(rs.getString("tenDangNhap"));
                tk.setMatKhau(rs.getString("matKhau"));
                tk.setVaiTro(rs.getString("vaiTro"));
                tk.setThoiGianDangNhapCuoi(rs.getTimestamp("thoiGianDangNhapCuoi"));
                danhSach.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    @Override
    public TaiKhoan getTaiKhoanById(long id) {
        String sql = "SELECT * FROM taikhoan WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setId(rs.getLong("id"));
                    tk.setTenDangNhap(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setVaiTro(rs.getString("vaiTro"));
                    tk.setThoiGianDangNhapCuoi(rs.getTimestamp("thoiGianDangNhapCuoi"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM taikhoan WHERE tenDangNhap = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setId(rs.getLong("id"));
                    tk.setTenDangNhap(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setVaiTro(rs.getString("vaiTro"));
                    tk.setThoiGianDangNhapCuoi(rs.getTimestamp("thoiGianDangNhapCuoi"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean createTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO taikhoan (tenDangNhap, matKhau, vaiTro) VALUES (?, ?, ?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE taikhoan SET matKhau = ?, vaiTro = ? WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tk.getMatKhau());
            ps.setString(2, tk.getVaiTro());
            ps.setLong(3, tk.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTaiKhoan(long id) {
        String sql = "DELETE FROM taikhoan WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateThoiGianDangNhapCuoi(long id, Timestamp thoiGian) {
        String sql = "UPDATE taikhoan SET thoiGianDangNhapCuoi = ? WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, thoiGian);
            ps.setLong(2, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Giả định đây là phần bổ sung vào cuối lớp TaiKhoanDAOImpl hiện tại của bạn

@Override
public List<TaiKhoan> searchTaiKhoan(String keyword) {
    List<TaiKhoan> list = new ArrayList<>();
    String sql = "SELECT * FROM taikhoan WHERE tenDangNhap LIKE ? OR vaiTro LIKE ?";
    String searchKeyword = "%" + keyword + "%";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DBHelper.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        rs = ps.executeQuery();

        while (rs.next()) {
            TaiKhoan tk = new TaiKhoan();
            tk.setId(rs.getLong("id"));
            tk.setTenDangNhap(rs.getString("tenDangNhap"));
            tk.setMatKhau(rs.getString("matKhau"));
            tk.setVaiTro(rs.getString("vaiTro"));
            tk.setThoiGianDangNhapCuoi(rs.getTimestamp("thoiGianDangNhapCuoi"));
            list.add(tk);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đảm bảo bạn có logic đóng tài nguyên (con, ps, rs) ở đây
    }
    return list;
}
}