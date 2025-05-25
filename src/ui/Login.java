import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
      private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JComboBox<String> cmbRole;
    private JCheckBox adminCheck;
private JCheckBox staffCheck;
   public Login() {
    setTitle("Prison Management System - Login");
    setSize(400, 250);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    JLabel lblUsername = new JLabel("Username:");
    JLabel lblPassword = new JLabel("Password:");
    JLabel lblRole = new JLabel("Role:");
    txtUsername = new JTextField(15);
    txtPassword = new JPasswordField(15);
    btnLogin = new JButton("Login");

    adminCheck = new JCheckBox("Admin");
    staffCheck = new JCheckBox("Staff");
    ButtonGroup group = new ButtonGroup();
    group.add(adminCheck);
    group.add(staffCheck);

    gbc.gridx = 0;
    gbc.gridy = 0;
    add(lblUsername, gbc);
    gbc.gridx = 1;
    gbc.gridy = 0;
    add(txtUsername, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    add(lblPassword, gbc);
    gbc.gridx = 1;
    gbc.gridy = 1;
    add(txtPassword, gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    add(lblRole, gbc);
    gbc.gridx = 1;
    gbc.gridy = 2;
    add(adminCheck, gbc);
    gbc.gridx = 1;
    gbc.gridy = 3;
    add(staffCheck, gbc);
    gbc.gridx = 1;
    gbc.gridy = 4;
    add(btnLogin, gbc);

    btnLogin.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            authenticateUser();
        }
    });

    } private void authenticateUser() {
    String username = txtUsername.getText();
    String password = new String(txtPassword.getPassword());
    String role = adminCheck.isSelected() ? "Admin" : staffCheck.isSelected() ? "Staff" : "";

    if (role.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a role.", "Role Required", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND user_type=?")) {
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, role);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (role.equals("Admin")) {
                JOptionPane.showMessageDialog(null, "Welcome Admin");
                Dashboard dashboard = new Dashboard("admin");
                dashboard.setVisible(true);
                dispose();
            } else if (role.equals("Staff")) {
                JOptionPane.showMessageDialog(null, "Welcome Staffl");
                Dashboard2 dashboard2 = new Dashboard2("staff");
                dashboard2.setVisible(true);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    public static void main(String[] args) {
        FlatArcDarkIJTheme.setup();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
