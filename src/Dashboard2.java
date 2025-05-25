
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Dashboard2 extends JFrame {

    private JLabel lblActivePrisons, lblInactivePrisons, lblActiveCellBlocks, lblInactiveCellBlocks;
    private JLabel lblTotalCrimes, lblTotalActions, lblCurrentInmates, lblReleasedInmates, lblTodaysVisits;
    private JPanel dashboardPanel, navbarPanel;

    public Dashboard2(String userType) {
        setTitle("Prison Management System - StaffDashboard");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create Navbar Panel
        navbarPanel = new JPanel();
        navbarPanel.setLayout(new GridLayout(1, 6, 10, 10)); // 6 buttons for staff
        navbarPanel.setBackground(Color.DARK_GRAY);

        String[] features = {"Prisons", "Cell Blocks", "Inmates", "Visitors", "Crimes", "Actions"};
        for (String feature : features) {
            JButton button = new JButton(feature);
            button.setForeground(Color.WHITE);
            button.setBackground(Color.BLACK);
            button.setFocusPainted(false);
            button.addActionListener(new NavigationListener(feature));
            navbarPanel.add(button);
        }

        // Create Dashboard Panel
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(3, 3, 10, 10));

        // Initialize labels
        lblActivePrisons = new JLabel("Active Prisons: 0", SwingConstants.CENTER);
        lblInactivePrisons = new JLabel("Inactive Prisons: 0", SwingConstants.CENTER);
        lblActiveCellBlocks = new JLabel("Active Cell Blocks: 0", SwingConstants.CENTER);
        lblInactiveCellBlocks = new JLabel("Inactive Cell Blocks: 0", SwingConstants.CENTER);
        lblTotalCrimes = new JLabel("Total Crimes: 0", SwingConstants.CENTER);
        lblTotalActions = new JLabel("Total Actions: 0", SwingConstants.CENTER);
        lblCurrentInmates = new JLabel("Current Inmates: 0", SwingConstants.CENTER);
        lblReleasedInmates = new JLabel("Released Inmates: 0", SwingConstants.CENTER);
        lblTodaysVisits = new JLabel("Today's Visits: 0", SwingConstants.CENTER);

        // Add labels to dashboard panel
        dashboardPanel.add(lblActivePrisons);
        dashboardPanel.add(lblInactivePrisons);
        dashboardPanel.add(lblActiveCellBlocks);
        dashboardPanel.add(lblInactiveCellBlocks);
        dashboardPanel.add(lblTotalCrimes);
        dashboardPanel.add(lblTotalActions);
        dashboardPanel.add(lblCurrentInmates);
        dashboardPanel.add(lblReleasedInmates);
        dashboardPanel.add(lblTodaysVisits);

        // Create a dropdown button with several themes
        String[] themes = {"Flat Atom One Light Contrast", "Flat Atom One Dark Contrast", "Flat Carbon", "Flat Darcula"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);

        // Add an action listener to the dropdown button
        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheme = (String) themeComboBox.getSelectedItem();
                switch (selectedTheme) {
                    case "Flat Atom One Light Contrast":
                        FlatAtomOneLightContrastIJTheme.setup();
                        break;
                    case "Flat Atom One Dark Contrast":
                        FlatAtomOneDarkContrastIJTheme.setup();
                        break;
                    case "Flat Carbon":
                        FlatCarbonIJTheme.setup();
                        break;
                    case "Flat Darcula":
                        FlatDarculaLaf.setup();
                        break;
                    // Add more themes here
                }
                // Update the theme of all forms
                updateTheme();
            }
        });

        // Create a panel for the dropdown button
        JPanel themePanel = new JPanel();
        themePanel.add(themeComboBox);

        // Add panels to frame
        add(navbarPanel, BorderLayout.NORTH);
        add(dashboardPanel, BorderLayout.CENTER);
        add(themePanel, BorderLayout.SOUTH);

        // Create a label for the theme button
        JLabel themeLabel = new JLabel("Themes:");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        themePanel.add(themeLabel);
        themePanel.add(themeComboBox);

        // Fetch data from database
        fetchData();
    }

    private void fetchData() {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            lblActivePrisons.setText("Active Prisons: " + getCount(con, "SELECT COUNT(*) FROM prisons WHERE status='Active'"));
            lblInactivePrisons.setText("Inactive Prisons: " + getCount(con, "SELECT COUNT(*) FROM prisons WHERE status='Inactive'"));
            lblActiveCellBlocks.setText("Active Cell Blocks: " + getCount(con, "SELECT COUNT(*) FROM cell_blocks WHERE status='Active'"));
            lblInactiveCellBlocks.setText("Inactive Cell Blocks: " + getCount(con, "SELECT COUNT(*) FROM cell_blocks WHERE status='Inactive'"));
            lblTotalCrimes.setText("Total Crimes: " + getCount(con, "SELECT COUNT(*) FROM crimes"));
            lblTotalActions.setText("Total Actions: " + getCount(con, "SELECT COUNT(*) FROM actions"));
            lblCurrentInmates.setText("Current Inmates: " + getCount(con, "SELECT COUNT(*) FROM inmates WHERE status='Active'"));
            lblReleasedInmates.setText("Released Inmates: " + getCount(con, "SELECT COUNT(*) FROM inmates WHERE status='Released'"));
            lblTodaysVisits.setText("Today's Visits: " + getCount(con, "SELECT COUNT(*) FROM visitors WHERE DATE(created_at) = CURDATE()"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getCount(Connection con, String query) {
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Navigation Listener for buttons
    private class NavigationListener implements ActionListener {

        private String feature;

        public NavigationListener(String feature) {
            this.feature = feature;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (feature) {
                case "Prisons":
                    new PrisonManagement().setVisible(true);
                    break;
                case "Cell Blocks":
                    new CellBlockManagement().setVisible(true);
                    break;
                case "Inmates":
                    new InmateManagement().setVisible(true);
                    break;
                case "Visitors":
                    new VisitorManagement().setVisible(true);
                    break;
                case "Crimes":
                    new CrimeManagement().setVisible(true);
                    break;
                case "Actions":
                    new ActionManagement().setVisible(true);
                    break;
                
            }
        }
    }

    // Method to update the theme of all forms
    private void updateTheme() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("admin").setVisible(true));
    }
}
