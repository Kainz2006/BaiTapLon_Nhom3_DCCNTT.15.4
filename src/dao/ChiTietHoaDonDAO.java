package dao;

import model.ChiTietHoaDon;
import java.util.List;

public interface ChiTietHoaDonDAO {
    List<ChiTietHoaDon> getChiTietByHoaDonId(long hoaDonId);
    boolean createChiTietHoaDon(ChiTietHoaDon chiTiet);
    boolean deleteChiTietByHoaDonId(long hoaDonId);
}
