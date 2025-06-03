
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InmateFrame extends javax.swing.JFrame {

    private String selectedImagePath = null;
    private int selectedInmateId = -1;
    


    public InmateFrame() {
        initComponents();
        loadInmateDetailsOnTableClick();
        loadInmates();
        loadInmateDetails();
        loadPrisons();
        loadCellBlocks();
        loadCrimes();
 
        
        // Add button actions
        btnUploadImage.addActionListener(e -> uploadImage());
        btnAdd.addActionListener(e -> addInmate());
        btnUpdate.addActionListener(e -> updateInmate());
        btnDelete.addActionListener(e -> deleteInmate());
        btnReset.addActionListener(e -> resetForm());
        
     
        
    }

    // Load Inmates
private void loadInmates() {
    DefaultTableModel model = (DefaultTableModel) table.getModel();
    model.setRowCount(0);
    try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(
            "SELECT i.id, i.code, p.name AS prison_name, p.id AS prison_id, c.name AS cell_block_name, c.id AS cell_block_id, "
            + "i.first_name, i.last_name, i.birth_date, i.address, i.marital_status, "
            + "i.complexion, i.eye_color, cr.name AS crime_name, cr.crime_id AS crime_id, "
            + "i.sentence, i.time_served, i.time_end, i.emergency_contact_name, "
            + "i.emergency_contact_relation, i.emergency_contact_phone, "
            + "i.visitor_privilege, i.status, i.image "
            + "FROM inmates i "
            + "JOIN prisons p ON i.prison_id = p.id "
            + "JOIN cell_blocks c ON i.cell_block_id = c.id "
            + "JOIN crimes cr ON i.crime_id = cr.crime_id")) {


        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String imagePath = rs.getString("image");


            // Convert image path to ImageIcon
            ImageIcon inmateImage = null;
            if (imagePath != null && !imagePath.isEmpty()) {
                inmateImage = new ImageIcon(new ImageIcon(imagePath).getImage()
                        .getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            }


            model.addRow(new Object[] {
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("prison_name"),
                rs.getString("cell_block_name"),
                rs.getString("first_name") + " " + rs.getString("last_name"),
                rs.getDate("birth_date"),
                rs.getString("address"),
                rs.getString("marital_status"),
                rs.getString("complexion"),
                rs.getString("eye_color"),
                rs.getString("crime_name"),
                rs.getString("sentence"),
                rs.getDate("time_served"),
                rs.getDate("time_end"),
                rs.getString("emergency_contact_name"),
                rs.getString("emergency_contact_relation"),
                rs.getString("emergency_contact_phone"),
                rs.getBoolean("visitor_privilege") ? "Yes" : "No",
                rs.getString("status"),
                inmateImage // Display image in table
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading inmates: " + e.getMessage());
    }
}


// Load Inmate Details when a table row is clicked
private void loadInmateDetailsOnTableClick() {
    table.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = table.rowAtPoint(e.getPoint());
            if (row >= 0) {
                selectedInmateId = (int) table.getValueAt(row, 0);
                loadInmateDetails();
            }
        }
    });
}


// Load Inmate Details
private void loadInmateDetails() {
    try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT i.id, i.code, p.name AS prison_name, p.id AS prison_id, c.name AS cell_block_name, c.id AS cell_block_id, "
            + "i.first_name, i.last_name, i.birth_date, i.address, i.marital_status, "
            + "i.complexion, i.eye_color, cr.name AS crime_name, cr.crime_id AS crime_id, "
            + "i.sentence, i.time_served, i.time_end, i.emergency_contact_name, "
            + "i.emergency_contact_relation, i.emergency_contact_phone, "
            + "i.visitor_privilege, i.status, i.image "
            + "FROM inmates i "
            + "JOIN prisons p ON i.prison_id = p.id "
            + "JOIN cell_blocks c ON i.cell_block_id = c.id "
            + "JOIN crimes cr ON i.crime_id = cr.crime_id "
            + "WHERE i.id = ?")) {
        ps.setInt(1, selectedInmateId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            txtCode.setText(rs.getString("code"));
            comboPrison.setSelectedItem(rs.getInt("prison_id") + " - " + rs.getString("prison_name"));
            comboCellBlock.setSelectedItem(rs.getInt("cell_block_id") + " - " + rs.getString("cell_block_name"));
            txtFirstName.setText(rs.getString("first_name"));
            txtLastName.setText(rs.getString("last_name"));
            birthDateChooser.setDate(rs.getDate("birth_date"));
            txtAddress.setText(rs.getString("address"));
            comboMaritalStatus.setSelectedItem(rs.getString("marital_status"));
            txtComplexion.setText(rs.getString("complexion"));
            txtEyeColor.setText(rs.getString("eye_color"));
            comboCrime.setSelectedItem(rs.getInt("crime_id") + " - " + rs.getString("crime_name"));
            txtSentence.setText(rs.getString("sentence"));
            timeServedChooser.setDate(rs.getDate("time_served"));
            timeEndChooser.setDate(rs.getDate("time_end"));
            txtEmergencyContact.setText(rs.getString("emergency_contact_name"));
            txtEmergencyRelation.setText(rs.getString("emergency_contact_relation"));
            txtEmergencyPhone.setText(rs.getString("emergency_contact_phone"));
            checkVisitorPrivilege.setSelected(rs.getBoolean("visitor_privilege"));
            comboStatus.setSelectedItem(rs.getString("status"));
            selectedImagePath = rs.getString("image");
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                lblImage.setIcon(new ImageIcon(new ImageIcon(selectedImagePath).getImage()
                        .getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading inmate details: " + e.getMessage());
    }
}

    
// Upload Image Method
private void uploadImage() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png"));
    int option = fileChooser.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        selectedImagePath = file.getAbsolutePath();
        ImageIcon icon = new ImageIcon(selectedImagePath);
        Image img = icon.getImage();
        int width = lblImage.getWidth();
        int height = lblImage.getHeight();
        int imgWidth = img.getWidth(null);
        int imgHeight = img.getHeight(null);
        int x = (width - imgWidth) / 2;
        int y = (height - imgHeight) / 2;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, x, y, null);
        g.dispose();
        lblImage.setIcon(new ImageIcon(bi));
    }
}
    // Load Prisons
    private void loadPrisons() {
        comboPrison.removeAllItems();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT id, name FROM prisons WHERE status = 'active'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboPrison.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading prisons: " + e.getMessage());
        }
    }

    // Load Cell Blocks
    private void loadCellBlocks() {
        comboCellBlock.removeAllItems();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT id, name FROM cell_blocks WHERE status = 'active'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboCellBlock.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cell blocks: " + e.getMessage());
        }
    }

    // Load Crimes
  // Load Crimes
    private void loadCrimes() {
        comboCrime.removeAllItems();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT crime_id, name FROM crimes WHERE status = 'active'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comboCrime.addItem(rs.getInt("crime_id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading crimes: " + e.getMessage());
        }
    }

    // Add Inmate
private void addInmate() {
    if (txtCode.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
            || birthDateChooser.getDate() == null || txtAddress.getText().isEmpty()
            || txtComplexion.getText().isEmpty() || txtEyeColor.getText().isEmpty()
            || txtSentence.getText().isEmpty() || timeServedChooser.getDate() == null
            || timeEndChooser.getDate() == null || txtEmergencyContact.getText().isEmpty()
            || txtEmergencyRelation.getText().isEmpty() || txtEmergencyPhone.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields");
        return;
    }

    // Validate string-only fields
    if (!txtFirstName.getText().matches("[a-zA-Z]+")) {
        JOptionPane.showMessageDialog(this, "First Name must contain only letters");
        return;
    }

    if (!txtLastName.getText().matches("[a-zA-Z]+")) {
        JOptionPane.showMessageDialog(this, "Last Name must contain only letters");
        return;
    }

    // Validate number-only fields
    if (!txtEmergencyContact.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Emergency Contact must be a number");
        return;
    }

    if (!txtEmergencyPhone.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Emergency Phone must be a number");
        return;
    }

    try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO inmates "
            + "(code, prison_id, cell_block_id, first_name, last_name, birth_date, address, marital_status, "
            + "complexion, eye_color, crime_id, sentence, time_served, time_end, emergency_contact_name, "
            + "emergency_contact_relation, emergency_contact_phone, visitor_privilege, status, image) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

        ps.setString(1, txtCode.getText());
        ps.setInt(2, getSelectedId(comboPrison));
        ps.setInt(3, getSelectedId(comboCellBlock));
        ps.setString(4, txtFirstName.getText());
        ps.setString(5, txtLastName.getText());
        ps.setDate(6, new java.sql.Date(birthDateChooser.getDate().getTime()));
        ps.setString(7, txtAddress.getText());
        ps.setString(8, comboMaritalStatus.getSelectedItem().toString());
        ps.setString(9, txtComplexion.getText());
        ps.setString(10, txtEyeColor.getText());
        ps.setInt(11, getSelectedId(comboCrime));
        ps.setString(12, txtSentence.getText());
        ps.setDate(13, new java.sql.Date(timeServedChooser.getDate().getTime()));
        ps.setDate(14, new java.sql.Date(timeEndChooser.getDate().getTime()));
        ps.setString(15, txtEmergencyContact.getText());
        ps.setString(16, txtEmergencyRelation.getText());
        ps.setString(17, txtEmergencyPhone.getText());
        ps.setBoolean(18, checkVisitorPrivilege.isSelected());
        ps.setString(19, comboStatus.getSelectedItem().toString());
        ps.setString(20, selectedImagePath);

        ps.executeUpdate();
        loadInmates();
        resetForm();
        JOptionPane.showMessageDialog(this, "Inmate added successfully");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding inmate: " + e.getMessage());
    }
}

// Update Inmate
private void updateInmate() {
    if (selectedInmateId == -1) {
        return;
    }

    if (txtCode.getText().isEmpty() || txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty()
            || birthDateChooser.getDate() == null || txtAddress.getText().isEmpty()
            || txtComplexion.getText().isEmpty() || txtEyeColor.getText().isEmpty()
            || txtSentence.getText().isEmpty() || timeServedChooser.getDate() == null
            || timeEndChooser.getDate() == null || txtEmergencyContact.getText().isEmpty()
            || txtEmergencyRelation.getText().isEmpty() || txtEmergencyPhone.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields");
        return;
    }

    // Validate string-only fields
    if (!txtFirstName.getText().matches("[a-zA-Z]+")) {
        JOptionPane.showMessageDialog(this, "First Name must contain only letters");
        return;
    }

    if (!txtLastName.getText().matches("[a-zA-Z]+")) {
        JOptionPane.showMessageDialog(this, "Last Name must contain only letters");
        return;
    }

    // Validate number-only fields
    if (!txtEmergencyContact.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Emergency Contact must be a number");
        return;
    }

    if (!txtEmergencyPhone.getText().matches("\\d+")) {
        JOptionPane.showMessageDialog(this, "Emergency Phone must be a number");
        return;
    }

    try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE inmates SET "
            + "code=?, prison_id=?, cell_block_id=?, first_name=?, last_name=?, birth_date=?, address=?, "
            + "marital_status=?, complexion=?, eye_color=?, crime_id=?, sentence=?, time_served=?, time_end=?, "
            + "emergency_contact_name=?, emergency_contact_relation=?, emergency_contact_phone=?, "
            + "visitor_privilege=?, status=?, image=? WHERE id=?")) {

        ps.setString(1, txtCode.getText());
        ps.setInt(2, getSelectedId(comboPrison));
        ps.setInt(3, getSelectedId(comboCellBlock));
        ps.setString(4, txtFirstName.getText());
        ps.setString(5, txtLastName.getText());
        ps.setDate(6, new java.sql.Date(birthDateChooser.getDate().getTime()));
        ps.setString(7, txtAddress.getText());
        ps.setString(8, comboMaritalStatus.getSelectedItem().toString());
        ps.setString(9, txtComplexion.getText());
        ps.setString(10, txtEyeColor.getText());
        ps.setInt(11, getSelectedId(comboCrime));
        ps.setString(12, txtSentence.getText());
        ps.setDate(13, new java.sql.Date(timeServedChooser.getDate().getTime()));
        ps.setDate(14, new java.sql.Date(timeEndChooser.getDate().getTime()));
        ps.setString(15, txtEmergencyContact.getText());
        ps.setString(16, txtEmergencyRelation.getText());
        ps.setString(17, txtEmergencyPhone.getText());
        ps.setBoolean(18, checkVisitorPrivilege.isSelected());
        ps.setString(19, comboStatus.getSelectedItem().toString());
        ps.setString(20, selectedImagePath);
        ps.setInt(21, selectedInmateId);

        ps.executeUpdate();
        loadInmates();
        resetForm();
        JOptionPane.showMessageDialog(this, "Inmate updated successfully");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error updating inmate: " + e.getMessage());
    }
}


    // Delete Inmate
    private void deleteInmate() {
        if (selectedInmateId == -1) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this inmate?");
        if (option == JOptionPane.YES_OPTION) {
            try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM inmates WHERE id=?")) {
                ps.setInt(1, selectedInmateId);
                ps.executeUpdate();
                loadInmates();
                resetForm();
                JOptionPane.showMessageDialog(this, "Inmate deleted successfully");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting inmate: " + e.getMessage());
            }
        }
    }

    // Reset
    private void resetForm() {
        txtCode.setText("");
        comboPrison.setSelectedIndex(0);
        comboCellBlock.setSelectedIndex(0);
        txtFirstName.setText("");
        txtLastName.setText("");
        birthDateChooser.setDate(null);
        txtAddress.setText("");
        comboMaritalStatus.setSelectedIndex(0);
        txtComplexion.setText("");
        txtEyeColor.setText("");
        comboCrime.setSelectedIndex(0);
        txtSentence.setText("");
        timeServedChooser.setDate(null);
        timeEndChooser.setDate(null);
        txtEmergencyContact.setText("");
        txtEmergencyRelation.setText("");
        txtEmergencyPhone.setText("");
        checkVisitorPrivilege.setSelected(false);
        comboStatus.setSelectedIndex(0);
        lblImage.setIcon(null);
        selectedImagePath = null;
        selectedInmateId = -1;
    }

    // Check if cell block is already assigned
    private boolean isCellBlockAssigned(int cellBlockId) {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM inmates WHERE cell_block_id = ? AND id != ?")) {
            ps.setInt(1, cellBlockId);
            ps.setInt(2, selectedInmateId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking cell block assignment: " + e.getMessage());
            return false;
        }
    }

    private int getSelectedId(JComboBox<String> combo) {
        String selected = (String) combo.getSelectedItem();
        return Integer.parseInt(selected.split(" - ")[0]);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        imgPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        btnUploadImage = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboPrison = new javax.swing.JComboBox<>();
        comboCellBlock = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        birthDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        comboMaritalStatus = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtComplexion = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtEyeColor = new javax.swing.JTextField();
        comboCrime = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtSentence = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        timeServedChooser = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        timeEndChooser = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtEmergencyContact = new javax.swing.JTextField();
        txtEmergencyRelation = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtEmergencyPhone = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        checkVisitorPrivilege = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnReset = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 670));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel1.setText("Inmate Details");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 6, -1, -1));

        imgPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel2.setText("Image");

        lblImage.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));

        btnUploadImage.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnUploadImage.setText("Upload Image");
        btnUploadImage.setBorder(null);

        jLabel21.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel21.setText("Status");

        comboStatus.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "active", "inactive", "released" }));
        comboStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout imgPanelLayout = new javax.swing.GroupLayout(imgPanel);
        imgPanel.setLayout(imgPanelLayout);
        imgPanelLayout.setHorizontalGroup(
            imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imgPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(imgPanelLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnUploadImage, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, imgPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(91, 91, 91))
        );
        imgPanelLayout.setVerticalGroup(
            imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imgPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUploadImage, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(imgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(imgPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 45, -1, 345));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setForeground(new java.awt.Color(204, 204, 204));
        jPanel1.setMaximumSize(new java.awt.Dimension(968, 322));
        jPanel1.setMinimumSize(new java.awt.Dimension(968, 322));

        jLabel3.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel3.setText("Code");

        txtCode.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel4.setText("Prison");

        comboPrison.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboPrison.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        comboCellBlock.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboCellBlock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel5.setText("Cell Blocks");

        txtFirstName.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("First Name");

        txtLastName.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel7.setText("Last Name");

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel8.setText("Birth Day");

        birthDateChooser.setForeground(new java.awt.Color(204, 204, 204));
        birthDateChooser.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel9.setText("Address");

        txtAddress.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        comboMaritalStatus.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboMaritalStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "single", "married", "divorced" }));
        comboMaritalStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel10.setText("Marial Status");

        jLabel11.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel11.setText("Complexion");

        txtComplexion.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel12.setText("Eye Color");

        txtEyeColor.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 12)); // NOI18N

        comboCrime.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboCrime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboCrime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel13.setText("Crime");

        jLabel14.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel14.setText("Sentence");

        txtSentence.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel15.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel15.setText("Time Served");

        timeServedChooser.setForeground(new java.awt.Color(204, 204, 204));
        timeServedChooser.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel16.setText("Time End");

        timeEndChooser.setForeground(new java.awt.Color(204, 204, 204));
        timeEndChooser.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel17.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel17.setText("Emergency");

        jLabel18.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel18.setText("Emergency Contact");

        txtEmergencyContact.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        txtEmergencyRelation.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel19.setText("Relation");

        txtEmergencyPhone.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel20.setText("Phone");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmergencyContact, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmergencyRelation, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmergencyPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmergencyPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmergencyRelation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmergencyContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        checkVisitorPrivilege.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N
        checkVisitorPrivilege.setText("Visitor Priveledge");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(comboPrison, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(comboCellBlock, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboMaritalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtComplexion, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEyeColor, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(birthDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(checkVisitorPrivilege, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(18, 18, 18)
                                    .addComponent(comboCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtSentence, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(timeServedChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel16)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(timeEndChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(comboPrison, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(comboCellBlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(birthDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(comboMaritalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComplexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtEyeColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(comboCrime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSentence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addComponent(timeServedChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(timeEndChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(checkVisitorPrivilege)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(287, 45, -1, -1));

        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        table.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "PrisonerID", "Prison", "Cell Block", "Name", "Birth Date", "Address", "Marital Status", "Complexion", "Eye Color", "Crime", "Sentence", "Time Served", "Time End", "Emergency Contact", "Relation", "Phone", "Visitor Privilege", "Status", "Image"
            }
        ));
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 1230, 150));

        btnReset.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(null);
        getContentPane().add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 580, 140, 30));

        btnAdd.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.setBorder(null);
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 580, 140, 30));

        btnUpdate.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setBorder(null);
        getContentPane().add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 580, 140, 30));

        btnDelete.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setBorder(null);
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 580, 140, 30));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatGitHubDarkContrastIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InmateFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser birthDateChooser;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUploadImage;
    private javax.swing.JCheckBox checkVisitorPrivilege;
    private javax.swing.JComboBox<String> comboCellBlock;
    private javax.swing.JComboBox<String> comboCrime;
    private javax.swing.JComboBox<String> comboMaritalStatus;
    private javax.swing.JComboBox<String> comboPrison;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JPanel imgPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTable table;
    private com.toedter.calendar.JDateChooser timeEndChooser;
    private com.toedter.calendar.JDateChooser timeServedChooser;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtComplexion;
    private javax.swing.JTextField txtEmergencyContact;
    private javax.swing.JTextField txtEmergencyPhone;
    private javax.swing.JTextField txtEmergencyRelation;
    private javax.swing.JTextField txtEyeColor;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtSentence;
    // End of variables declaration//GEN-END:variables
}
