package panel;

import dao.KhachHangDAO;
import dao.KhachHangDAOImpl;
import model.KhachHang;
import model.TaiKhoan;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class KhachHangPanel extends JPanel {
    private final KhachHangDAO khachHangDAO;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final TaiKhoan currentTaiKhoan;
    
    // NEW: Các thành phần tìm kiếm
    private JTextField txtTimKiem; 
    private JButton btnTimKiem;    
    private JButton btnLamMoi; // Sử dụng lại nút này để reset tìm kiếm

    public KhachHangPanel(TaiKhoan taiKhoan) {
        this.currentTaiKhoan = taiKhoan;
        khachHangDAO = new KhachHangDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.decode("#F0F2F5"));

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.decode("#007BFF"));
        
        // 2. Bảng dữ liệu
        String[] columnNames = {"ID", "Họ Tên", "Số Điện Thoại", "Địa Chỉ"};
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
                BorderFactory.createLineBorder(Color.GRAY), "Danh sách khách hàng",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.decode("#343A40")
        ));
        
        // 3. Panel Tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        txtTimKiem = new JTextField(30);
        btnTimKiem = new JButton("Tìm Kiếm");
        
        searchPanel.setBackground(Color.decode("#F0F2F5"));
        searchPanel.add(new JLabel("Tìm kiếm (Họ Tên, SĐT, Địa Chỉ):"));
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        
        // Panel trung tâm chứa tìm kiếm và bảng
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 4. Panel chức năng (Control Panel)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        controlPanel.setBackground(Color.decode("#F0F2F5"));
        
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới"); // Sửa lại thành biến instance
        
        // Thiết lập màu sắc và font cho các nút
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        btnThem.setBackground(Color.decode("#28A745")); btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(Color.decode("#FFC107")); btnSua.setForeground(Color.BLACK);
        btnXoa.setBackground(Color.decode("#DC3545")); btnXoa.setForeground(Color.WHITE);
        btnLamMoi.setBackground(Color.decode("#6C757D")); btnLamMoi.setForeground(Color.WHITE);
        
        btnThem.setFont(buttonFont); btnSua.setFont(buttonFont); btnXoa.setFont(buttonFont); btnLamMoi.setFont(buttonFont);
        
        controlPanel.add(btnThem);
        controlPanel.add(btnSua);
        controlPanel.add(btnXoa);
        controlPanel.add(btnLamMoi);
        
        // 5. Add Panels
        add(lblTitle, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // 6. Thêm sự kiện
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        
        // NEW: Sự kiện tìm kiếm và làm mới
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            loadKhachHangData(keyword);
        });
        
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadKhachHangData(null);
        });
        
        // Tải dữ liệu ban đầu
        loadKhachHangData(null);
    }
    
    // THAY THẾ: Phương thức loadKhachHangData được cập nhật để nhận tham số tìm kiếm
    private void loadKhachHangData(String keyword) {
        try {
            tableModel.setRowCount(0);
            List<KhachHang> danhSach;
            
            if (keyword != null && !keyword.isEmpty()) {
                // Giả định phương thức searchKhachHang đã được thêm vào KhachHangDAO
                danhSach = khachHangDAO.searchKhachHang(keyword); 
            } else {
                danhSach = khachHangDAO.getAllKhachHang();
            }

            if (danhSach.isEmpty()) {
                String message = keyword != null && !keyword.isEmpty() 
                                 ? "Không tìm thấy khách hàng nào khớp với từ khóa: '" + keyword + "'."
                                 : "Chưa có dữ liệu khách hàng nào trong hệ thống.";
                JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            for (KhachHang kh : danhSach) {
                Object[] row = {kh.getId(), kh.getHoTen(), kh.getSoDienThoai(), kh.getDiaChi()};
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Các phương thức CRUD giữ nguyên logic

    private void themKhachHang() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Khách Hàng", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Color.decode("#F8F9FA"));
        
        JTextField txtHoTen = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtDiaChi = new JTextField();
        
        formPanel.add(new JLabel("Họ Tên:")); formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Số Điện Thoại:")); formPanel.add(txtSDT);
        formPanel.add(new JLabel("Địa Chỉ:")); formPanel.add(txtDiaChi);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setBackground(Color.decode("#007BFF"));
        btnLuu.setForeground(Color.WHITE);
        
        btnLuu.addActionListener(e -> {
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            
            if (hoTen.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ Họ Tên và Số Điện Thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            KhachHang khMoi = new KhachHang(0, hoTen, sdt, diaChi);
            if (khachHangDAO.createKhachHang(khMoi)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                loadKhachHangData(null);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.decode("#F8F9FA"));
        buttonPanel.add(btnLuu);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            long id = (long) tableModel.getValueAt(selectedRow, 0);
            KhachHang kh = khachHangDAO.getKhachHangById(id);
            if (kh == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng. Vui lòng làm mới bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Khách Hàng", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(400, 320);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.getContentPane().setBackground(Color.decode("#F8F9FA"));

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            formPanel.setBackground(Color.decode("#F8F9FA"));
            
            JTextField txtHoTen = new JTextField(kh.getHoTen());
            JTextField txtSDT = new JTextField(kh.getSoDienThoai());
            JTextField txtDiaChi = new JTextField(kh.getDiaChi());
            
            formPanel.add(new JLabel("ID:")); formPanel.add(new JLabel(String.valueOf(kh.getId())));
            formPanel.add(new JLabel("Họ Tên:")); formPanel.add(txtHoTen);
            formPanel.add(new JLabel("Số Điện Thoại:")); formPanel.add(txtSDT);
            formPanel.add(new JLabel("Địa Chỉ:")); formPanel.add(txtDiaChi);

            dialog.add(formPanel, BorderLayout.CENTER);
            
            JButton btnLuu = new JButton("Lưu");
            btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnLuu.setBackground(Color.decode("#007BFF"));
            btnLuu.setForeground(Color.WHITE);
            
            btnLuu.addActionListener(e -> {
                String hoTen = txtHoTen.getText().trim();
                String sdt = txtSDT.getText().trim();
                String diaChi = txtDiaChi.getText().trim();
                
                if (hoTen.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ Họ Tên và Số Điện Thoại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                kh.setHoTen(hoTen);
                kh.setSoDienThoai(sdt);
                kh.setDiaChi(diaChi);
                
                if (khachHangDAO.updateKhachHang(kh)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
                    loadKhachHangData(null);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(Color.decode("#F8F9FA"));
            buttonPanel.add(btnLuu);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lấy dữ liệu khách hàng: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long id = (long) tableModel.getValueAt(selectedRow, 0);
            String hoTen = (String) tableModel.getValueAt(selectedRow, 1);
            
            // Kiểm tra ràng buộc: Khách hàng có hóa đơn không
            if (khachHangDAO.khachHangCoHoaDon(id)) {
                JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng '" + hoTen + "' vì người này đã có giao dịch tại cửa hàng.", "Lỗi ngoại lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng '" + hoTen + "'?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (khachHangDAO.deleteKhachHang(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                    loadKhachHangData(null);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra trong quá trình xóa: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}