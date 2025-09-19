package dao;

import helper.DBHelper;
import model.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAOImpl implements KhachHangDAO {

    @Override
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new KhachHang(
                    rs.getLong("id"),
                    rs.getString("hoTen"),
                    rs.getString("soDienThoai"),
                    rs.getString("diaChi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public KhachHang getKhachHangById(long id) {
        String sql = "SELECT * FROM khachhang WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                        rs.getLong("id"),
                        rs.getString("hoTen"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createKhachHang(KhachHang khachHang) {
        String sql = "INSERT INTO khachhang (hoTen, soDienThoai, diaChi) VALUES (?, ?, ?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, khachHang.getHoTen());
            ps.setString(2, khachHang.getSoDienThoai());
            ps.setString(3, khachHang.getDiaChi());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        khachHang.setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateKhachHang(KhachHang khachHang) {
        String sql = "UPDATE khachhang SET hoTen = ?, soDienThoai = ?, diaChi = ? WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, khachHang.getHoTen());
            ps.setString(2, khachHang.getSoDienThoai());
            ps.setString(3, khachHang.getDiaChi());
            ps.setLong(4, khachHang.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteKhachHang(long id) {
        String sql = "DELETE FROM khachhang WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean khachHangCoHoaDon(long khachHangId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hoadon WHERE khachHangId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, khachHangId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}