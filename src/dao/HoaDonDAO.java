package dao;

import model.HoaDon;
import java.util.List;

public interface HoaDonDAO {
    List<HoaDon> getAllHoaDon();
    HoaDon getHoaDonById(long id);
    long createHoaDon(HoaDon hoaDon); // Trả về ID của hóa đơn mới
    boolean deleteHoaDon(long id);
}
