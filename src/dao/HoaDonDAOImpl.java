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
}
