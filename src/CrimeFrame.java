
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class CrimeFrame extends javax.swing.JFrame {
    private int selectedCrimeId = -1;

    public CrimeFrame() {
        initComponents();
        
        btnAddCrime.addActionListener(e -> addCrime());
        btnEditCrime.addActionListener(e -> editCrime());
        btnDeleteCrime.addActionListener(e -> deleteCrime());
        btnReset.addActionListener(e -> clearFields());

        crimeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = crimeTable.getSelectedRow();
                if (row != -1) {
                    selectedCrimeId = (int) crimeTable.getValueAt(row, 0);
                    txtCrimeName.setText((String) crimeTable.getValueAt(row, 1));
                    comboStatus.setSelectedItem(crimeTable.getValueAt(row, 2));
                }
            }
        });

        loadCrimes();
    }

    private void addCrime() {
        String crimeName = txtCrimeName.getText().trim();
        String status = (String) comboStatus.getSelectedItem();
        if (crimeName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Crime Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement("SELECT COUNT(*) FROM crimes WHERE name = ?")) {
            checkPs.setString(1, crimeName);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Crime Name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO crimes (name, status) VALUES (?, ?)")) {
            ps.setString(1, crimeName);
            ps.setString(2, status);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Crime Added!");
            loadCrimes();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editCrime() {
        if (selectedCrimeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crime to edit.");
            return;
        }
        String crimeName = txtCrimeName.getText().trim();
        String status = (String) comboStatus.getSelectedItem();
        if (crimeName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Crime Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE crimes SET name = ?, status = ? WHERE crime_id = ?")) {
            ps.setString(1, crimeName);
            ps.setString(2, status);
            ps.setInt(3, selectedCrimeId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Crime Updated!");
            loadCrimes();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCrime() {
        if (selectedCrimeId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a crime to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this crime?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement("SELECT COUNT(*) FROM inmates WHERE crime_id = ?")) {
            checkPs.setInt(1, selectedCrimeId);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Cannot delete! Crime is linked to inmates.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM crimes WHERE crime_id = ?")) {
            ps.setInt(1, selectedCrimeId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Crime Deleted!");
            loadCrimes();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCrimes() {
        DefaultTableModel model = (DefaultTableModel) crimeTable.getModel();
        model.setRowCount(0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT crime_id, name, status, created_at FROM crimes");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("crime_id"), rs.getString("name"), rs.getString("status"), rs.getTimestamp("created_at")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtCrimeName.setText("");
        selectedCrimeId = -1;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtCrimeName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        crimeTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox<>();
        btnAddCrime = new javax.swing.JButton();
        btnEditCrime = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnDeleteCrime = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(735, 595));
        setMinimumSize(new java.awt.Dimension(735, 595));
        setPreferredSize(new java.awt.Dimension(735, 595));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel1.setText("Crime");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("Crime Name");

        txtCrimeName.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        crimeTable.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 12)); // NOI18N
        crimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Crime Name", "Status", "Date Created"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(crimeTable);

        jLabel7.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel7.setText("Status");

        comboStatus.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "active", "inactive" }));
        comboStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtCrimeName, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCrimeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        btnAddCrime.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnAddCrime.setText("Add");
        btnAddCrime.setBorder(null);

        btnEditCrime.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnEditCrime.setText("Update");
        btnEditCrime.setBorder(null);

        btnReset.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(null);

        btnDeleteCrime.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnDeleteCrime.setText("Delete");
        btnDeleteCrime.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAddCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEditCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(322, 322, 322)
                        .addComponent(jLabel1)))
                .addContainerGap(27, Short.MAX_VALUE))
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
                    .addComponent(btnEditCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteCrime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      FlatGitHubDarkContrastIJTheme.setup();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCrime;
    private javax.swing.JButton btnDeleteCrime;
    private javax.swing.JButton btnEditCrime;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JTable crimeTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCrimeName;
    // End of variables declaration//GEN-END:variables
}
