
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;




public class VisitorFrame extends javax.swing.JFrame {
    private int selectedVisitorId = -1;
    private int selectedInmateId = -1;

   
    public VisitorFrame() {
        initComponents();
        // Load Inmates for ComboBox
        loadInmates();
        // Add Action Listeners for buttons
        btnAddVisitor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addVisitor();
            }
        }); btnEditVisitor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editVisitor();
            }
        });
        btnDeleteVisitor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteVisitor();
            }
        });
        // Add ActionListener for inmate JComboBox
        comboInmate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inmateComboBoxActionPerformed();
            }
        }); // Table selection listener to view and edit selected visitor
        visitorTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = visitorTable.getSelectedRow();
                if (row != -1) {
                    selectedVisitorId = (int) visitorTable.getValueAt(row, 0);
                    txtVisitorName.setText((String) visitorTable.getValueAt(row, 1));
                    txtContact.setText((String) visitorTable.getValueAt(row, 2));
                    txtRelation.setText((String) visitorTable.getValueAt(row, 3));
                }
            }
        });
        
    }
        // Load Inmates into ComboBox
    private void loadInmates() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, first_name, last_name FROM inmates WHERE visitor_privilege = true");
             ResultSet rs = ps.executeQuery()) {
            comboInmate.removeAllItems();
            while (rs.next()) {
                int inmateId = rs.getInt("id");
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                comboInmate.addItem(inmateId + " - " + fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // Load Visitors for selected inmate
    private void loadVisitors(int inmateId) {
        DefaultTableModel model = (DefaultTableModel) visitorTable.getModel();
        model.setRowCount(0); // Clear existing rows
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT v.id, v.full_name, v.contact, v.relation, i.first_name, i.last_name FROM visitors v JOIN inmates i ON v.inmate_id = i.id WHERE v.inmate_id = ?")) {
            ps.setInt(1, inmateId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int visitorId = rs.getInt("id");
                String visitorName = rs.getString("full_name");
                String contact = rs.getString("contact");
                String relation = rs.getString("relation");
                String inmateFullName = rs.getString("first_name") + " " + rs.getString("last_name");
                model.addRow(new Object[] { visitorId, visitorName, contact, relation, inmateFullName });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // ActionListener for inmate JComboBox
    private void inmateComboBoxActionPerformed() {
        String selectedInmate = (String) comboInmate.getSelectedItem();
        if (selectedInmate != null) {
            selectedInmateId = Integer.parseInt(selectedInmate.split(" - ")[0]);
            loadVisitors(selectedInmateId);
        }
    }
    // Add Visitor
    private void addVisitor() {
        String visitorName = txtVisitorName.getText().trim();
        String contact = txtContact.getText().trim();
        String relation = txtRelation.getText().trim();
        try (Connection con = DBConnection.getConnection();
              PreparedStatement ps = con.prepareStatement("INSERT INTO visitors (inmate_id,full_name, contact, relation, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setInt(1, selectedInmateId);
            ps.setString(2, visitorName);
            ps.setString(3, contact);
            ps.setString(4, relation);
            ps.executeUpdate();   JOptionPane.showMessageDialog(this, "Visitor Added!");
            loadVisitors(selectedInmateId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Edit Visitor
    private void editVisitor() {
        if (selectedVisitorId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a visitor to edit.");
            return;
        }
        String visitorName = txtVisitorName.getText().trim();
        String contact = txtContact.getText().trim();
        String relation = txtRelation.getText().trim();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE visitors SET full_name = ?, contact = ?, relation = ? WHERE id = ?")) {
            ps.setString(1, visitorName);
            ps.setString(2, contact);
            ps.setString(3, relation); ps.setInt(4, selectedVisitorId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Visitor Updated!");
            loadVisitors(selectedInmateId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Delete Visitor
    private void deleteVisitor() {
        if (selectedVisitorId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a visitor to delete.");
            return;
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM visitors WHERE id = ?")) {
            ps.setInt(1, selectedVisitorId);
            ps.executeUpdate();  JOptionPane.showMessageDialog(this, "Visitor Deleted!");
            loadVisitors(selectedInmateId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtVisitorName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        visitorTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        comboInmate = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        txtRelation = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnAddVisitor = new javax.swing.JButton();
        btnEditVisitor = new javax.swing.JButton();
        btnDeleteVisitor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(735, 560));
        setMinimumSize(new java.awt.Dimension(735, 560));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel1.setText("Visitor");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("Visitor Name");

        txtVisitorName.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        visitorTable.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 12)); // NOI18N
        visitorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Visitor Name", "Contact", "Relation", "Inmate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(visitorTable);

        jLabel7.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel7.setText("Inmate");

        comboInmate.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboInmate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel8.setText("Contact");

        txtContact.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        txtRelation.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel9.setText("Relation");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtVisitorName, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                            .addComponent(txtRelation, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboInmate, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboInmate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVisitorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRelation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        btnAddVisitor.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnAddVisitor.setText("Add");
        btnAddVisitor.setBorder(null);

        btnEditVisitor.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnEditVisitor.setText("Update");
        btnEditVisitor.setBorder(null);

        btnDeleteVisitor.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnDeleteVisitor.setText("Delete");
        btnDeleteVisitor.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(327, 327, 327)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(btnAddVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddVisitor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VisitorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VisitorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VisitorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisitorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VisitorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddVisitor;
    private javax.swing.JButton btnDeleteVisitor;
    private javax.swing.JButton btnEditVisitor;
    private javax.swing.JComboBox<String> comboInmate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtRelation;
    private javax.swing.JTextField txtVisitorName;
    private javax.swing.JTable visitorTable;
    // End of variables declaration//GEN-END:variables
}
