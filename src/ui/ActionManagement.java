import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActionManagement extends JFrame {
    private JTable actionTable;
    private JComboBox<String> comboInmate;
    private JTextField txtActionName;
    private JSpinner spinnerActionDate;
    private JButton btnSelectDate;
    private JComboBox<String> comboPunishment;
    private JTextField txtDuration;
    private JTextArea txtDescription;
    private JButton btnAddAction, btnEditAction, btnDeleteAction, btnReset;
    private int selectedActionId = -1;
    
    public void updateTheme() {
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public ActionManagement() {
        // Set up frame properties
        setTitle("Disciplinary Action Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize components
        comboInmate = new JComboBox<String>();
        loadInmates();
        txtActionName = new JTextField(20);
        
        // Action date spinner setup
        spinnerActionDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerActionDate, "yyyy-MM-dd");
        spinnerActionDate.setEditor(dateEditor);
        spinnerActionDate.setValue(new Date());  // Sets current date as default

        comboPunishment = new JComboBox<String>(new String[] { "Solitary Confinement", "Loss of Privileges", "Extra Duty" }); // Add more types as needed
        txtDuration = new JTextField(20);
        txtDescription = new JTextArea(3, 20);
        btnAddAction = new JButton("Add Action");
        btnEditAction = new JButton("Edit Action");
        btnDeleteAction = new JButton("Delete Action");
        btnReset = new JButton("Reset");
        
        // Table to display action records
        actionTable = new JTable();
        actionTable.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] { "Action ID", "Inmate", "Action Name", "Action Date", "Punishment", "Duration", "Description" }
        ));
        actionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(actionTable);

        // Set up input panel for action details
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Select Inmate:"));
        inputPanel.add(comboInmate);
        inputPanel.add(new JLabel("Action Name:"));
        inputPanel.add(txtActionName);
        inputPanel.add(new JLabel("Action Date:"));
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(spinnerActionDate, BorderLayout.CENTER);
        inputPanel.add(datePanel);
        inputPanel.add(new JLabel("Punishment:"));
        inputPanel.add(comboPunishment);
        inputPanel.add(new JLabel("Duration:"));
        inputPanel.add(txtDuration);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(txtDescription));
        add(inputPanel, BorderLayout.NORTH);

        // Add reset button functionality
        btnReset.addActionListener(e -> {
            comboInmate.setSelectedIndex(0);
            txtActionName.setText("");
            spinnerActionDate.setValue(new Date());
            comboPunishment.setSelectedIndex(0);
            txtDuration.setText("");
            txtDescription.setText("");
            selectedActionId = -1;
        });

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAddAction);
        buttonPanel.add(btnEditAction);
        buttonPanel.add(btnDeleteAction);
        buttonPanel.add(btnReset);
        add(buttonPanel, BorderLayout.SOUTH);

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
                        spinnerActionDate.setValue(sdf.parse((String) actionTable.getValueAt(row, 3)));
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
        loadActions();
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
        Date actionDate = (Date) spinnerActionDate.getValue();
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
        Date actionDate = (Date) spinnerActionDate.getValue();
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

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        EventQueue.invokeLater(() -> new ActionManagement().setVisible(true));
    }
}