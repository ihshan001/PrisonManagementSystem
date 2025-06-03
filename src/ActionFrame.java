import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ActionFrame extends javax.swing.JFrame {
    private int selectedActionId = -1;

    
    public ActionFrame() {
        initComponents();
        loadInmates();
        loadActions();
        
        
        
        // Add Action Listeners for buttons
        btnAddAction.addActionListener(e -> addAction());
        btnEditAction.addActionListener(e -> editAction());
        btnDeleteAction.addActionListener(e -> deleteAction());

        // Table selection listener to view and edit selected action
        actionTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = actionTable.getSelectedRow();
                if (row != -1) {
                    selectedActionId = (Integer) actionTable.getValueAt(row, 0);
                    comboInmate.setSelectedItem(actionTable.getValueAt(row, 1));
                    txtActionName.setText(actionTable.getValueAt(row, 2).toString());

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        ActionDate.setDate(sdf.parse((String) actionTable.getValueAt(row, 3)));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    comboPunishment.setSelectedItem(actionTable.getValueAt(row, 4).toString());
                    txtDuration.setText(actionTable.getValueAt(row, 5).toString());
                    txtDescription.setText(actionTable.getValueAt(row, 6).toString());
                }
            }
        });
        
        // Load initial data in the table
        
        // Add reset button functionality
        btnReset.addActionListener(e -> {
            comboInmate.setSelectedIndex(0);
            txtActionName.setText("");
            ActionDate.setDate(new Date());
            comboPunishment.setSelectedIndex(0);
            txtDuration.setText("");
            txtDescription.setText("");
            selectedActionId = -1;
        });
    }

    // Load inmates into the combo box
    private void loadInmates() {
        comboInmate.removeAllItems();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, CONCAT(first_name, ' ', last_name) AS full_name FROM inmates");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int inmateId = rs.getInt("id");
                String fullName = rs.getString("full_name");
                comboInmate.addItem(fullName + " (ID: " + inmateId + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Action
    private void addAction() {
        String selectedInmate = (String) comboInmate.getSelectedItem();
        if (selectedInmate == null) {
            JOptionPane.showMessageDialog(this, "Please select an inmate.");
            return;
        }
        int inmateId = extractInmateId(selectedInmate);
        String actionName = txtActionName.getText().trim();
        if (actionName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an action name.");
            return;
        }
        Date actionDate = (Date) ActionDate.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String actionDateStr = sdf.format(actionDate);
        String punishment = (String) comboPunishment.getSelectedItem();
        String duration = txtDuration.getText().trim();
        if (duration.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the duration.");
            return;
        }
        String description = txtDescription.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO disciplinary_actions (inmate_id, action_name, action_date, punishment, description, duration) VALUES (?,?,?,?,?,?)")) {
            ps.setInt(1, inmateId);
            ps.setString(2, actionName);
            ps.setString(3, actionDateStr);
            ps.setString(4, punishment);
            ps.setString(5, description);
            ps.setString(6, duration);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Disciplinary Action Added!");
            loadActions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Edit Action
    private void editAction() {
        if (selectedActionId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a disciplinary action to edit.");
            return;
        }
        String selectedInmate = (String) comboInmate.getSelectedItem();
        if (selectedInmate == null) {
            JOptionPane.showMessageDialog(this, "Please select an inmate.");
            return;
        }
        int inmateId = extractInmateId(selectedInmate);
        String actionName = txtActionName.getText().trim();
        if (actionName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an action name.");
            return;
        }
        Date actionDate = (Date) ActionDate.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String actionDateStr = sdf.format(actionDate);
        String punishment = (String) comboPunishment.getSelectedItem();
        String duration = txtDuration.getText().trim();
        if (duration.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the duration.");
            return;
        }
        String description = txtDescription.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE disciplinary_actions SET inmate_id = ?, action_name = ?, action_date = ?, punishment = ?, description = ?, duration = ? WHERE id = ?")) {
            ps.setInt(1, inmateId);
            ps.setString(2, actionName);
            ps.setString(3, actionDateStr);
            ps.setString(4, punishment);
            ps.setString(5, description);
            ps.setString(6, duration);
            ps.setInt(7, selectedActionId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Disciplinary Action Updated!");
            loadActions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Action
    private void deleteAction() {
        if (selectedActionId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a disciplinary action to delete.");
            return;
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM disciplinary_actions WHERE id = ?")) {
            ps.setInt(1, selectedActionId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Disciplinary Action Deleted!");
            loadActions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load Actions into Table
    private void loadActions() {
        DefaultTableModel model = (DefaultTableModel) actionTable.getModel();
        model.setRowCount(0); // Clear existing rows

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT da.id, CONCAT(i.first_name, ' ', i.last_name) AS inmate_name, da.action_name, da.action_date, da.punishment, da.duration, da.description " +
                 "FROM disciplinary_actions da " +
                 "JOIN inmates i ON da.inmate_id = i.id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String inmateName = rs.getString("inmate_name");
                String actionName = rs.getString("action_name");
                Date actionDate = rs.getDate("action_date");
                String actionDateStr = new SimpleDateFormat("yyyy-MM-dd").format(actionDate);
                String punishment = rs.getString("punishment");
                String duration = rs.getString("duration");
                String description = rs.getString("description");

                model.addRow(new Object[] { id, inmateName, actionName, actionDateStr, punishment, duration, description });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Extracts inmate ID from the combo box item text (e.g., "John Doe (ID: 1)")
    private int extractInmateId(String comboText) {
        if (comboText == null) return -1;
        int startIndex = comboText.lastIndexOf("ID:") + 4;
        int endIndex = comboText.indexOf(")", startIndex);
        String idString = comboText.substring(startIndex, endIndex).trim();
        return Integer.parseInt(idString);
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtActionName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        comboInmate = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        ActionDate = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        comboPunishment = new javax.swing.JComboBox<>();
        txtDuration = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        actionTable = new javax.swing.JTable();
        btnAddAction = new javax.swing.JButton();
        btnEditAction = new javax.swing.JButton();
        btnDeleteAction = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(735, 645));
        setMinimumSize(new java.awt.Dimension(735, 645));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel1.setText("Action");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("Action Name");

        txtActionName.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel7.setText("Inmate");

        comboInmate.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboInmate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel8.setText("Action Date");

        ActionDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        ActionDate.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel9.setText("Punishment");

        comboPunishment.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        comboPunishment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Solitary Confinement", "Loss of Privilegde", "Cell Restriction – Confined to cell except for essentials", "Reduced Visitation Rights – Temporarily ban or limit visitors", "Extra Duty – Assign additional cleaning or labor tasks", "Loss of Recreational Time – No access to yard, TV, or games", "Confiscation of Personal Items – Radios, books, etc.", "Restricted Commissary Access – Limited or no access to purchase items", "Verbal/Written Warning – For minor infractions", "Behavior Monitoring Status – Increased surveillance", "Transfer to Higher Security Wing", "Prolonged Sentence – Extend imprisonment period (if applicable)", "Court Re-investigation – Escalate offense for legal actions", "Loss of Work Assignments – Remove from prison job programs", "No Contact Order – For issues between inmates" }));
        comboPunishment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtDuration.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel10.setText("Duration");

        jLabel11.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel11.setText("Description");

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane2.setViewportView(txtDescription);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtActionName)
                            .addComponent(comboInmate, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(comboPunishment, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDuration)
                            .addComponent(ActionDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(45, 45, 45))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboInmate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtActionName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ActionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboPunishment, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel11)))
                .addGap(72, 72, 72))
        );

        actionTable.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 12)); // NOI18N
        actionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Action ID", "Inmate", "Action Name", "Action Date", "Punishment", "Duration", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(actionTable);

        btnAddAction.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnAddAction.setText("Add");
        btnAddAction.setBorder(null);

        btnEditAction.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnEditAction.setText("Update");
        btnEditAction.setBorder(null);

        btnDeleteAction.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnDeleteAction.setText("Delete");
        btnDeleteAction.setBorder(null);

        btnReset.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(294, 294, 294)
                        .addComponent(jLabel1))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
            .addGroup(layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(btnAddAction, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEditAction, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeleteAction, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditAction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteAction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddAction, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

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
                new ActionFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser ActionDate;
    private javax.swing.JTable actionTable;
    private javax.swing.JButton btnAddAction;
    private javax.swing.JButton btnDeleteAction;
    private javax.swing.JButton btnEditAction;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> comboInmate;
    private javax.swing.JComboBox<String> comboPunishment;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtActionName;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtDuration;
    // End of variables declaration//GEN-END:variables
}
