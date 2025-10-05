 package dao;

import helper.DBHelper;
import model.SanPham;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAOImpl implements SanPhamDAO {

    @Override
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new SanPham(
                    rs.getInt("id"),
                    rs.getString("ten"),
                    rs.getString("hang"),
                    rs.getDouble("gia"),
                    rs.getInt("tonKho"),
                    rs.getString("loai"),
                    rs.getString("tinhTrang")
                ));
            }
        } catch (SQLException e) {
        }
        return list;
    }

    @Override
    public SanPham getSanPhamById(int id) {
        String sql = "SELECT * FROM sanpham WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SanPham(
                        rs.getInt("id"),
                        rs.getString("ten"),
                        rs.getString("hang"),
                        rs.getDouble("gia"),
                        rs.getInt("tonKho"),
                        rs.getString("loai"),
                        rs.getString("tinhTrang")
                    );
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    @Override
    public boolean createSanPham(SanPham sanPham) {
        String sql = "INSERT INTO sanpham (ten, hang, gia, tonKho, loai, tinhTrang) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sanPham.getTen());
            ps.setString(2, sanPham.getHang());
            ps.setDouble(3, sanPham.getGia());
            ps.setInt(4, sanPham.getTonKho());
            ps.setString(5, sanPham.getLoai());
            ps.setString(6, sanPham.getTinhTrang());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        sanPham.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public boolean updateSanPham(SanPham sanPham) {
        String sql = "UPDATE sanpham SET ten = ?, hang = ?, gia = ?, tonKho = ?, loai = ?, tinhTrang = ? WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sanPham.getTen());
            ps.setString(2, sanPham.getHang());
            ps.setDouble(3, sanPham.getGia());
            ps.setInt(4, sanPham.getTonKho());
            ps.setString(5, sanPham.getLoai());
            ps.setString(6, sanPham.getTinhTrang());
            ps.setInt(7, sanPham.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    @Override
    public boolean deleteSanPham(int id) {
        String sql = "DELETE FROM sanpham WHERE id = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        }
        return false;
    }
    @Override
public boolean updateSoLuongTon(long sanPhamId, int soLuong) throws SQLException {
    String sql = "UPDATE sanpham SET tonKho = tonKho - ? WHERE id = ?";
    try (Connection con = DBHelper.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, soLuong);
        ps.setLong(2, sanPhamId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}
@Override
public List<SanPham> searchSanPham(String keyword) {
    List<SanPham> list = new ArrayList<>();
    String sql = "SELECT * FROM sanpham WHERE ten LIKE ? OR hang LIKE ? OR loai LIKE ?";
    String searchKeyword = "%" + keyword + "%";
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DBHelper.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);
        rs = ps.executeQuery();

        while (rs.next()) {
            SanPham sp = new SanPham(
                rs.getInt("id"),
                rs.getString("ten"),
                rs.getString("hang"),
                rs.getDouble("gia"),
                rs.getInt("tonKho"),
                rs.getString("loai"),
                rs.getString("tinhTrang")
            );
            list.add(sp);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đảm bảo bạn có logic đóng tài nguyên (con, ps, rs) ở đây
    }
    return list;
}
}
