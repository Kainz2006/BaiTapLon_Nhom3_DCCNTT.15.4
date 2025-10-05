package panel;

import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // NEW

public class HoaDonPanel extends JPanel {
    private final HoaDonDAO hoaDonDAO;
    private final ChiTietHoaDonDAO chiTietHoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final SanPhamDAO sanPhamDAO;
    private final TaiKhoanDAO taiKhoanDAO;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TaiKhoan currentTaiKhoan;
    
    // Các thành phần tìm kiếm
    private JTextField txtTimKiem; 
    private JButton btnTimKiem;    
    private JButton btnLamMoi;

    // Phương thức trợ giúp định dạng tiền tệ (Khắc phục lỗi Malformed pattern)
    private String formatMoney(double amount) {
        // Sử dụng Locale Vietnam để định dạng chuẩn (ví dụ: 10.000.000)
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        // Cần ép kiểu sang DecimalFormat để áp dụng pattern chuẩn cho số nguyên
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).applyPattern("###,###,###,###,##0");
        }
        return formatter.format(amount) + " VND";
    }

    public HoaDonPanel(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;
        hoaDonDAO = new HoaDonDAOImpl();
        chiTietHoaDonDAO = new ChiTietHoaDonDAOImpl();
        khachHangDAO = new KhachHangDAOImpl();
        sanPhamDAO = new SanPhamDAOImpl();
        taiKhoanDAO = new TaiKhoanDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#F0F2F5"));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#007BFF"));

        // 2. Bảng dữ liệu
        String[] columnNames = {"ID Hóa Đơn", "Ngày Lập", "Tên Khách Hàng", "SĐT", "Tổng Tiền", "Nhân Viên"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.decode("#E9ECEF"));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách hóa đơn",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.decode("#343A40")
        ));
        
        // 3. Panel Tìm kiếm Setup
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtTimKiem = new JTextField(30);
        btnTimKiem = new JButton("Tìm Kiếm");
        
        searchPanel.setBackground(Color.decode("#F0F2F5"));
        searchPanel.add(new JLabel("Tìm kiếm (ID HĐ, Tên KH, Tên NV):"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        
        // Panel trung tâm chứa tìm kiếm và bảng
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 4. Panel chức năng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.setBackground(Color.decode("#F0F2F5"));
        
        JButton btnTaoHoaDon = new JButton("Tạo Hóa Đơn");
        JButton btnXemHoaDon = new JButton("Xem Chi Tiết");
        JButton btnXoaHoaDon = new JButton("Xóa Hóa Đơn");
        btnLamMoi = new JButton("Làm mới");
        
        // Thiết lập màu sắc và font cho các nút
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        btnTaoHoaDon.setFont(buttonFont);
        btnTaoHoaDon.setBackground(Color.decode("#28A745")); btnTaoHoaDon.setForeground(Color.WHITE);
        btnXemHoaDon.setFont(buttonFont);
        btnXemHoaDon.setBackground(Color.decode("#007BFF")); btnXemHoaDon.setForeground(Color.WHITE);
        btnXoaHoaDon.setFont(buttonFont);
        btnXoaHoaDon.setBackground(Color.decode("#DC3545")); btnXoaHoaDon.setForeground(Color.WHITE);
        btnLamMoi.setFont(buttonFont);
        btnLamMoi.setBackground(Color.decode("#6C757D")); btnLamMoi.setForeground(Color.WHITE);
        
        controlPanel.add(btnTaoHoaDon);
        controlPanel.add(btnXemHoaDon);
        
        if ("admin".equalsIgnoreCase(currentTaiKhoan.getVaiTro())) {
            controlPanel.add(btnXoaHoaDon);
        }
        controlPanel.add(btnLamMoi);
        
        // 5. Add Panels
        add(lblTitle, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // 6. Thêm sự kiện
        btnTaoHoaDon.addActionListener(e -> taoHoaDon());
        btnXemHoaDon.addActionListener(e -> xemHoaDon());
        btnXoaHoaDon.addActionListener(e -> xoaHoaDon());
        
        // Sự kiện tìm kiếm và làm mới
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            loadHoaDonData(keyword);
        });
        
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadHoaDonData(null);
        });

        loadHoaDonData(null);
    }
    
    // THAY THẾ: Phương thức loadHoaDonData đã hoàn chỉnh
    private void loadHoaDonData(String keyword) {
        tableModel.setRowCount(0);
        try {
            List<HoaDon> danhSach;
            
            if (keyword != null && !keyword.isEmpty()) {
                danhSach = hoaDonDAO.searchHoaDon(keyword);
            } else {
                danhSach = hoaDonDAO.getAllHoaDon();
            }

            if (danhSach.isEmpty()) {
                String message = keyword != null && !keyword.isEmpty() 
                                 ? "Không tìm thấy hóa đơn nào khớp với từ khóa: '" + keyword + "'."
                                 : "Chưa có dữ liệu hóa đơn nào trong hệ thống.";
                JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (HoaDon hd : danhSach) {
                // Lấy thông tin Khách hàng và Nhân viên
                KhachHang kh = khachHangDAO.getKhachHangById(hd.getKhachHangId());
                TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(hd.getTaiKhoanId());
                
                String tenKhachHang = (kh != null) ? kh.getHoTen() : "Không xác định";
                String sdt = (kh != null) ? kh.getSoDienThoai() : "N/A";
                String tenNhanVien = (tk != null) ? tk.getTenDangNhap() : "Không xác định";

                Object[] row = {
                    hd.getId(),
                    hd.getNgayLap(),
                    tenKhachHang,
                    sdt,
                    formatMoney(hd.getTongTien()), // Dùng phương thức format an toàn
                    tenNhanVien
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Phương thức taoHoaDon giữ nguyên logic, chỉ sửa lỗi định dạng tiền
    private void taoHoaDon() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tạo Hóa Đơn", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setBackground(Color.decode("#F8F9FA"));

        // Panel chọn khách hàng
        JPanel khachHangPanel = new JPanel();
        khachHangPanel.setBackground(Color.decode("#E9ECEF"));
        khachHangPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        JComboBox<KhachHang> cmbKhachHang = new JComboBox<>();
        try {
            khachHangDAO.getAllKhachHang().forEach(cmbKhachHang::addItem);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải danh sách khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        khachHangPanel.add(new JLabel("Chọn Khách Hàng:"));
        khachHangPanel.add(cmbKhachHang);

        // Panel sản phẩm
        JPanel sanPhamPanel = new JPanel(new BorderLayout());
        sanPhamPanel.setBorder(BorderFactory.createTitledBorder("Chọn sản phẩm"));
        DefaultTableModel sanPhamTableModel = new DefaultTableModel(
            new String[]{"ID", "Tên Sản Phẩm", "Giá", "Tồn Kho", "Số lượng mua"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        JTable sanPhamTable = new JTable(sanPhamTableModel);
        JScrollPane sanPhamScrollPane = new JScrollPane(sanPhamTable);
        sanPhamTable.setRowHeight(25);
        sanPhamTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        try {
            List<SanPham> sanPhamList = sanPhamDAO.getAllSanPham();
            for (SanPham sp : sanPhamList) {
                sanPhamTableModel.addRow(new Object[]{sp.getId(), sp.getTen(), sp.getGia(), sp.getTonKho(), 0});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Lỗi tải danh sách sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        sanPhamPanel.add(sanPhamScrollPane, BorderLayout.CENTER);

        // Panel tổng tiền và nút
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTongTien = new JLabel(formatMoney(0), SwingConstants.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnTinhTien = new JButton("Tính tiền");
        btnTinhTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTinhTien.setBackground(Color.decode("#FFC107")); btnTinhTien.setForeground(Color.BLACK);

        JButton btnTao = new JButton("Lưu Hóa Đơn");
        btnTao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTao.setBackground(Color.decode("#28A745")); btnTao.setForeground(Color.WHITE);

        buttonPanel.add(btnTinhTien);
        buttonPanel.add(btnTao);

        southPanel.add(lblTongTien, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện tính tiền
        btnTinhTien.addActionListener(e -> {
            try {
                double tongTien = 0;
                for (int i = 0; i < sanPhamTable.getRowCount(); i++) {
                    int soLuong = Integer.parseInt(sanPhamTable.getValueAt(i, 4).toString());
                    double donGia = (double) sanPhamTable.getValueAt(i, 2);
                    tongTien += soLuong * donGia;
                }
                lblTongTien.setText(formatMoney(tongTien));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng không hợp lệ. Vui lòng nhập số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sự kiện lưu hóa đơn
        btnTao.addActionListener(e -> {
            KhachHang selectedKh = (KhachHang) cmbKhachHang.getSelectedItem();
            if (selectedKh == null) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn khách hàng.");
                return;
            }

            try {
                double tongTien = 0;
                boolean coSanPham = false;
                List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();

                for (int i = 0; i < sanPhamTable.getRowCount(); i++) {
                    int soLuong = Integer.parseInt(sanPhamTable.getValueAt(i, 4).toString());
                    if (soLuong > 0) {
                        int tonKhoHienTai = (int) sanPhamTable.getValueAt(i, 3);
                        if (soLuong > tonKhoHienTai) {
                            JOptionPane.showMessageDialog(dialog, "Sản phẩm '" + sanPhamTable.getValueAt(i, 1) + "' không đủ số lượng trong kho. Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        coSanPham = true;
                        int sanPhamId = (int) sanPhamTable.getValueAt(i, 0);
                        double donGia = (double) sanPhamTable.getValueAt(i, 2);
                        tongTien += soLuong * donGia;

                        danhSachChiTiet.add(new ChiTietHoaDon(0, sanPhamId, soLuong, donGia));
                    }
                }

                if (!coSanPham) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ít nhất một sản phẩm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                HoaDon hdMoi = new HoaDon();
                hdMoi.setKhachHangId(selectedKh.getId());
                hdMoi.setTongTien(tongTien);
                hdMoi.setTaiKhoanId(currentTaiKhoan.getId());

                long hoaDonId = hoaDonDAO.createHoaDon(hdMoi);
                if (hoaDonId != -1) {
                    boolean chiTietThanhCong = true;
                    for (ChiTietHoaDon cthd : danhSachChiTiet) {
                        cthd.setHoaDonId(hoaDonId);
                        if (!chiTietHoaDonDAO.createChiTietHoaDon(cthd)) {
                            chiTietThanhCong = false;
                            break;
                        }
                        sanPhamDAO.updateSoLuongTon(cthd.getSanPhamId(), cthd.getSoLuong());
                    }

                    if (chiTietThanhCong) {
                        JOptionPane.showMessageDialog(dialog, "Tạo hóa đơn thành công!");
                        loadHoaDonData(null);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Tạo chi tiết hóa đơn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Tạo hóa đơn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Số lượng không hợp lệ. Vui lòng nhập số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialog.add(khachHangPanel, BorderLayout.NORTH);
        dialog.add(sanPhamPanel, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void xemHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xem.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long hoaDonId = (long) tableModel.getValueAt(selectedRow, 0);
        try {
            HoaDon hd = hoaDonDAO.getHoaDonById(hoaDonId);
            if (hd == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi Tiết Hóa Đơn", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(this);
            dialog.setBackground(Color.decode("#F8F9FA"));
            
            // Thông tin chung
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));
            KhachHang kh = khachHangDAO.getKhachHangById(hd.getKhachHangId());
            TaiKhoan tk = taiKhoanDAO.getTaiKhoanById(hd.getTaiKhoanId());
            
            infoPanel.add(new JLabel("ID Hóa Đơn:")); infoPanel.add(new JLabel(String.valueOf(hd.getId())));
            infoPanel.add(new JLabel("Ngày Lập:")); infoPanel.add(new JLabel(String.valueOf(hd.getNgayLap())));
            infoPanel.add(new JLabel("Khách Hàng:")); infoPanel.add(new JLabel(kh != null ? kh.getHoTen() : "Không xác định"));
            infoPanel.add(new JLabel("Nhân Viên:")); infoPanel.add(new JLabel(tk != null ? tk.getTenDangNhap() : "Không xác định"));
            infoPanel.add(new JLabel("Tổng Tiền:")); infoPanel.add(new JLabel(formatMoney(hd.getTongTien()))); // Dùng phương thức format an toàn
            
            dialog.add(infoPanel, BorderLayout.NORTH);

            // Bảng chi tiết
            String[] chiTietColumnNames = {"Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
            DefaultTableModel chiTietTableModel = new DefaultTableModel(chiTietColumnNames, 0);
            JTable chiTietTable = new JTable(chiTietTableModel);
            JScrollPane chiTietScrollPane = new JScrollPane(chiTietTable);
            chiTietScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));
            dialog.add(chiTietScrollPane, BorderLayout.CENTER);
            
            List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietByHoaDonId(hoaDonId);
            for (ChiTietHoaDon cthd : chiTietList) {
                SanPham sp = sanPhamDAO.getSanPhamById(cthd.getSanPhamId());
                double thanhTien = cthd.getSoLuong() * cthd.getDonGia();
                Object[] row = {sp != null ? sp.getTen() : "Không xác định", cthd.getSoLuong(), cthd.getDonGia(), thanhTien};
                chiTietTableModel.addRow(row);
            }

            // Nút In hóa đơn
            JButton btnInHoaDon = new JButton("In Hóa Đơn");
            btnInHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnInHoaDon.setBackground(Color.decode("#20C997")); btnInHoaDon.setForeground(Color.WHITE);
            btnInHoaDon.addActionListener(e -> inHoaDon(hd, chiTietList, kh));
            dialog.add(btnInHoaDon, BorderLayout.SOUTH);

            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xem chi tiết hóa đơn: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void inHoaDon(HoaDon hd, List<ChiTietHoaDon> chiTiet, KhachHang kh) {
        String fileName = "HoaDon_" + hd.getId() + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("========================================\n");
            writer.write("             HÓA ĐƠN BÁN HÀNG\n");
            writer.write("========================================\n");
            writer.write("ID Hóa Đơn: " + hd.getId() + "\n");
            writer.write("Ngày Lập: " + hd.getNgayLap() + "\n");
            writer.write("Nhân Viên: " + currentTaiKhoan.getTenDangNhap() + "\n");
            writer.write("Khách Hàng: " + (kh != null ? kh.getHoTen() : "N/A") + " - SĐT: " + (kh != null ? kh.getSoDienThoai() : "N/A") + "\n");
            writer.write("----------------------------------------\n");
            writer.write(String.format("%-30s | %-10s | %-15s | %-15s\n", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"));
            writer.write("----------------------------------------\n");

            for (ChiTietHoaDon cthd : chiTiet) {
                SanPham sp = sanPhamDAO.getSanPhamById(cthd.getSanPhamId());
                double thanhTien = cthd.getSoLuong() * cthd.getDonGia();
                
                // Sửa đổi định dạng số trong file in ấn để an toàn hơn
                String formattedDonGia = formatMoney(cthd.getDonGia()).replace(" VND", "");
                String formattedThanhTien = formatMoney(thanhTien).replace(" VND", "");
                
                writer.write(String.format("%-30s | %-10d | %-15s | %-15s\n", 
                    sp != null ? sp.getTen() : "N/A", cthd.getSoLuong(), formattedDonGia, formattedThanhTien));
            }

            writer.write("----------------------------------------\n");
            writer.write(String.format("Tổng tiền: %s\n", formatMoney(hd.getTongTien())));
            writer.write("========================================\n");
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được in ra file " + fileName + " trong thư mục gốc của dự án.", "In Hóa Đơn", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn ra file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void xoaHoaDon() {
        if (!"admin".equalsIgnoreCase(currentTaiKhoan.getVaiTro())) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa hóa đơn.", "Từ chối", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        long id = (long) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Việc xóa hóa đơn sẽ xóa cả chi tiết. Bạn có chắc chắn muốn xóa?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Xóa chi tiết trước, sau đó xóa hóa đơn để đảm bảo toàn vẹn dữ liệu
                if (chiTietHoaDonDAO.deleteChiTietByHoaDonId(id) && hoaDonDAO.deleteHoaDon(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                    loadHoaDonData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}