package dao;

import helper.DBHelper;
import model.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAOImpl implements ChiTietHoaDonDAO {

    @Override
    public List<ChiTietHoaDon> getChiTietByHoaDonId(long hoaDonId) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM chitiethoadon WHERE hoaDonId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, hoaDonId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ChiTietHoaDon(
                        rs.getLong("hoaDonId"),
                        rs.getInt("sanPhamId"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean createChiTietHoaDon(ChiTietHoaDon chiTiet) {
        String sql = "INSERT INTO chitiethoadon (hoaDonId, sanPhamId, soLuong, donGia) VALUES (?, ?, ?, ?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, chiTiet.getHoaDonId());
            ps.setInt(2, chiTiet.getSanPhamId());
            ps.setInt(3, chiTiet.getSoLuong());
            ps.setDouble(4, chiTiet.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteChiTietByHoaDonId(long hoaDonId) {
        String sql = "DELETE FROM chitiethoadon WHERE hoaDonId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, hoaDonId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
