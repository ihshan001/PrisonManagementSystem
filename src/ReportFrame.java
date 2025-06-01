
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Vector;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.properties.TextAlignment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import java.sql.Date;
import javax.imageio.ImageIO;

public class ReportFrame extends javax.swing.JFrame {

    // Database connection
    private Connection connection;
    // Selected inmate ID
    private int selectedInmateId = -1;
    
    

    public ReportFrame() {
        initComponents();
        initializeDatabase();
        loadInmateComboBox();
        inmateComboBox.addActionListener(e -> onInmateSelected());
        
        
        
        //Button
        searchButton.addActionListener(e -> performSearch());
        dateFilterCombo.addActionListener(e -> onDateFilterChanged());
        resetButton.addActionListener(e -> resetFilters());
        exportButton.addActionListener(e -> exportReport());
        exportActivityButton.addActionListener(e -> exportActivityLog());
        exportVisitButton.addActionListener(e -> exportVisitHistory());
        
        imageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showImagePopup();
            }
        });
        
        
        fromDateChooser = new JDateChooser();
        
        fromDateChooser.setVisible(false);
        

        toDateChooser = new JDateChooser();
        
        toDateChooser.setVisible(false);
        

    }

    private JLabel createWidget(String title, String value, Color color) {
        JLabel widget = new JLabel("<html><center><b>" + title + "</b><br/><font size='4'>" + value + "</font></center></html>");
        widget.setHorizontalAlignment(SwingConstants.CENTER);
        widget.setBorder(BorderFactory.createLineBorder(color, 2));
        widget.setOpaque(true);
        widget.setBackground(color.brighter().brighter());
        return widget;
    }
    
    private void initializeDatabase() {
        try {
            // Update with your database connection details
            String url = "jdbc:mysql://localhost:3306/prison_management";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }
    }

    private void loadInmateComboBox() {
        try {
            String sql = "SELECT id, CONCAT(first_name, ' ', last_name, ' (', code, ')') as display_name FROM inmates WHERE status = 'active'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            inmateComboBox.removeAllItems();
            inmateComboBox.addItem("Select Inmate...");

            while (rs.next()) {
                String item = rs.getInt("id") + " - " + rs.getString("display_name");
                inmateComboBox.addItem(item);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading inmates: " + e.getMessage());
        }
    }

    private void onInmateSelected() {
        String selected = (String) inmateComboBox.getSelectedItem();
        if (selected != null && !selected.equals("Select Inmate...")) {
            selectedInmateId = Integer.parseInt(selected.split(" - ")[0]);
            loadInmateDetails(selectedInmateId);
            loadReports(selectedInmateId);
        }
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter search term");
            return;
        }

        try {
            String sql = "SELECT id FROM inmates WHERE first_name LIKE ? OR last_name LIKE ? OR code LIKE ? OR status = 'active'";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                selectedInmateId = rs.getInt("id");
                loadInmateDetails(selectedInmateId);
                loadReports(selectedInmateId);

                // Update combo box selection
                for (int i = 0; i < inmateComboBox.getItemCount(); i++) {
                    String item = (String) inmateComboBox.getItemAt(i);
                    if (item.startsWith(selectedInmateId + " - ")) {
                        inmateComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No inmate found with search term: " + searchTerm);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search error: " + e.getMessage());
        }
    }

    private void loadInmateDetails(int inmateId) {
    try {
        String sql = "SELECT i.*, c.name as crime_name, p.name as prison_name, cb.name as cell_block_name, i.image "
                + "FROM inmates i "
                + "LEFT JOIN crimes c ON i.crime_id = c.crime_id "
                + "LEFT JOIN prisons p ON i.prison_id = p.id "
                + "LEFT JOIN cell_blocks cb ON i.cell_block_id = cb.id "
                + "WHERE i.id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            nameLabel.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
            codeLabel.setText(rs.getString("code"));
            crimeLabel.setText(rs.getString("crime_name"));
            sentenceLabel.setText(rs.getString("sentence"));
            timeServedLabel.setText(rs.getString("time_served"));
            timeEndLabel.setText(rs.getString("time_end"));
            statusLabel.setText(rs.getString("status"));

            // Calculate remaining time
            calculateRemainingTime(rs.getDate("time_end"));

            // Update summary widgets
            updateSummaryWidgets(inmateId);

            // Load image using inmate ID (file path approach)
            loadInmateImage(inmateId);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading inmate details: " + e.getMessage());
        e.printStackTrace();
    }
}



    private void updateWidget(JLabel widget, String title, String value, Color color) {
        widget.setText("<html><center><b>" + title + "</b><br/><font size='4'>" + value + "</font></center></html>");
        widget.setBorder(BorderFactory.createLineBorder(color, 2));
        widget.setBackground(color.brighter().brighter());
    }

    private void calculateRemainingTime(Date timeEnd) {
        if (timeEnd != null) {
            LocalDate endDate = timeEnd.toLocalDate();
            LocalDate currentDate = LocalDate.now();
            long daysRemaining = ChronoUnit.DAYS.between(currentDate, endDate);

            String remainingText = daysRemaining + " days";
            Color color = Color.darkGray; // More than 1 year

            if (daysRemaining < 30) {
                color = Color.RED; // Less than 30 days
            } else if (daysRemaining < 365) {
                color = Color.YELLOW; // Less than 1 year
            }

            remainingTimeLabel.setText(remainingText);
            remainingTimeLabel.setForeground(color);

            // Update widget
            updateWidget(remainingTimeWidget, "Remaining Time", remainingText, color);
        }
    }

    private void updateSummaryWidgets(int inmateId) {
        try {
            // Total time served calculation
            String timeSql = "SELECT DATEDIFF(CURDATE(), time_served) as days_served FROM inmates WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(timeSql);
            pstmt.setInt(1, inmateId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                updateWidget(totalTimeServedWidget, "Total Time Served", rs.getInt("days_served") + " days", Color.BLUE);
            }

            // Visitor count
            String visitorSql = "SELECT COUNT(*) as visitor_count FROM visitors WHERE inmate_id = ?";
            pstmt = connection.prepareStatement(visitorSql);
            pstmt.setInt(1, inmateId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                updateWidget(visitorCountWidget, "Total Visitors", String.valueOf(rs.getInt("visitor_count")), Color.ORANGE);
            }

            // Status
            String statusSql = "SELECT status FROM inmates WHERE id = ?";
            pstmt = connection.prepareStatement(statusSql);
            pstmt.setInt(1, inmateId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status");
                Color statusColor = status.equals("active") ? Color.GREEN : Color.RED;
                updateWidget(statusBadge, "Status", status.toUpperCase(), statusColor);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating summary: " + e.getMessage());
        }
    }

    private void loadInmateImage(int inmateId) {
    try {
        String sql = "SELECT image FROM inmates WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String imagePath = rs.getString("image");

            if (imagePath != null && !imagePath.isEmpty()) {
                imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath).getImage()
                        .getScaledInstance(180, 300, Image.SCALE_SMOOTH)));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("No Image");
            }
        }
    } catch (SQLException e) {
        imageLabel.setText("Image Load Error");
        e.printStackTrace();
    }
}






    private void loadReports(int inmateId) {
        loadActivityLog(inmateId);
        loadVisitHistory(inmateId);
    }

    private void loadActivityLog(int inmateId) {
        try {
            DefaultTableModel model = (DefaultTableModel) activityTableModel.getModel();
            model.setRowCount(0);

            String sql = "SELECT da.action_date, da.action_name, da.description, da.status "
                    + "FROM disciplinary_actions da WHERE da.inmate_id = ? "
                    + "ORDER BY da.action_date DESC";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, inmateId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getDate("action_date"));
                row.add(rs.getString("action_name"));
                row.add(rs.getString("description"));
                row.add(rs.getString("status"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading activity log: " + e.getMessage());
        }
    }

    private void loadVisitHistory(int inmateId) {
        try {
            DefaultTableModel model = (DefaultTableModel) visitTableModel.getModel();
            model.setRowCount(0);

            String sql = "SELECT v.created_at, v.full_name, v.relation, v.contact "
                    + "FROM visitors v WHERE v.inmate_id = ? "
                    + "ORDER BY v.created_at DESC";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, inmateId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getTimestamp("created_at"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("relation"));
                row.add(rs.getString("contact"));
                row.add("N/A"); // Duration - you can add this field if needed
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading visit history: " + e.getMessage());
        }
    }

   private void onDateFilterChanged() {
    String selected = (String) dateFilterCombo.getSelectedItem();
    boolean showCustom = "Custom Range".equals(selected);
    
    // The date choosers will only be visible if "Custom Range" is selected.
    fromDateChooser.setVisible(showCustom);
    toDateChooser.setVisible(showCustom);

    if (selectedInmateId != -1 && !showCustom) {
        loadReports(selectedInmateId);
    }
}


    private void resetFilters() {
        inmateComboBox.setSelectedIndex(0);
        searchField.setText("");
        dateFilterCombo.setSelectedIndex(0);
        fromDateChooser.setVisible(false);
        toDateChooser.setVisible(false);

        // Clear all displays
        clearInmateDetails();
        ((DefaultTableModel) activityTableModel.getModel()).setRowCount(0);
        ((DefaultTableModel) visitTableModel.getModel()).setRowCount(0);
        selectedInmateId = -1;
    }

    private void clearInmateDetails() {
        nameLabel.setText("-");
        codeLabel.setText("-");
        crimeLabel.setText("-");
        sentenceLabel.setText("-");
        timeServedLabel.setText("-");
        timeEndLabel.setText("-");
        statusLabel.setText("-");
        remainingTimeLabel.setText("-");
        imageLabel.setIcon(null);
        imageLabel.setText("No Image");

        // Reset widgets
        updateWidget(totalTimeServedWidget, "Total Time Served", "0 days", Color.BLUE);
        updateWidget(remainingTimeWidget, "Remaining Time", "0 days", Color.GREEN);
        updateWidget(visitorCountWidget, "Total Visitors", "0", Color.ORANGE);
        updateWidget(statusBadge, "Status", "Unknown", Color.GRAY);
    }

    private void showImagePopup() {
        if (imageLabel.getIcon() != null) {
            JDialog imageDialog = new JDialog(this, "Inmate Image", true);
            JLabel largeImageLabel = new JLabel(imageLabel.getIcon());
            imageDialog.add(new JScrollPane(largeImageLabel));
            imageDialog.setSize(400, 500);
            imageDialog.setLocationRelativeTo(this);
            imageDialog.setVisible(true);
        }
    }

    private void exportReport() {
        if (selectedInmateId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inmate first");
            return;
        }

        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            String fileName = "reports/Inmate_Complete_Report_" + selectedInmateId + ".pdf";

            // Create PDF document
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add fonts
            PdfFont boldFont = PdfFontFactory.createFont();
            PdfFont normalFont = PdfFontFactory.createFont();

            // Header
            Paragraph header = new Paragraph("PRISON MANAGEMENT SYSTEM")
                    .setFont(boldFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(header);

            Paragraph subHeader = new Paragraph("COMPLETE INMATE REPORT")
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(subHeader);

            // Inmate Details Section
            addInmateDetailsToDocument(document, boldFont, normalFont);

            // Summary Section
            addSummaryToDocument(document, boldFont, normalFont);

            // Activity Log Section
            addActivityLogToDocument(document, boldFont, normalFont);

            // Visit History Section
            addVisitHistoryToDocument(document, boldFont, normalFont);

            // Footer
            Paragraph footer = new Paragraph("Generated on: ")
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            document.add(footer);

            document.close();

            // Show success message and ask to open
            int result = JOptionPane.showConfirmDialog(this,
                    "Report generated successfully!\nWould you like to open it?",
                    "Export Successful",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                openPDF(fileName);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addInmateDetailsToDocument(Document document, PdfFont boldFont, PdfFont normalFont) throws SQLException {
        // Inmate Details Header
        Paragraph detailsHeader = new Paragraph("INMATE DETAILS")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(detailsHeader);

        // Create table for inmate details
        Table detailsTable = new Table(new float[]{2, 3});
        detailsTable.setWidth(400);

        // Get inmate details from database
        String sql = "SELECT i.*, c.name as crime_name, p.name as prison_name, cb.name as cell_block_name "
                + "FROM inmates i "
                + "LEFT JOIN crimes c ON i.crime_id = c.crime_id "
                + "LEFT JOIN prisons p ON i.prison_id = p.id "
                + "LEFT JOIN cell_blocks cb ON i.cell_block_id = cb.id "
                + "WHERE i.id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, selectedInmateId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            addDetailRow(detailsTable, "Name:", rs.getString("first_name") + " " + rs.getString("last_name"), boldFont, normalFont);
            addDetailRow(detailsTable, "Inmate Code:", rs.getString("code"), boldFont, normalFont);
            addDetailRow(detailsTable, "Birth Date:", rs.getString("birth_date"), boldFont, normalFont);
            addDetailRow(detailsTable, "Address:", rs.getString("address"), boldFont, normalFont);
            addDetailRow(detailsTable, "Marital Status:", rs.getString("marital_status"), boldFont, normalFont);
            addDetailRow(detailsTable, "Complexion:", rs.getString("complexion"), boldFont, normalFont);
            addDetailRow(detailsTable, "Eye Color:", rs.getString("eye_color"), boldFont, normalFont);
            addDetailRow(detailsTable, "Crime:", rs.getString("crime_name"), boldFont, normalFont);
            addDetailRow(detailsTable, "Sentence:", rs.getString("sentence"), boldFont, normalFont);
            addDetailRow(detailsTable, "Time Served:", rs.getString("time_served"), boldFont, normalFont);
            addDetailRow(detailsTable, "Time End:", rs.getString("time_end"), boldFont, normalFont);
            addDetailRow(detailsTable, "Status:", rs.getString("status"), boldFont, normalFont);
            addDetailRow(detailsTable, "Prison:", rs.getString("prison_name"), boldFont, normalFont);
            addDetailRow(detailsTable, "Cell Block:", rs.getString("cell_block_name"), boldFont, normalFont);
            addDetailRow(detailsTable, "Emergency Contact:", rs.getString("emergency_contact_name"), boldFont, normalFont);
            addDetailRow(detailsTable, "Contact Relation:", rs.getString("emergency_contact_relation"), boldFont, normalFont);
            addDetailRow(detailsTable, "Contact Phone:", rs.getString("emergency_contact_phone"), boldFont, normalFont);
        }

        document.add(detailsTable);
    }

    private void addDetailRow(Table table, String label, String value, PdfFont boldFont, PdfFont normalFont) {
        Cell labelCell = new Cell().add(new Paragraph(label).setFont(boldFont)).setBorder(Border.NO_BORDER);
        Cell valueCell = new Cell().add(new Paragraph(value != null ? value : "N/A").setFont(normalFont)).setBorder(Border.NO_BORDER);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addSummaryToDocument(Document document, PdfFont boldFont, PdfFont normalFont) throws SQLException {
        // Summary Header
        Paragraph summaryHeader = new Paragraph("SUMMARY")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(summaryHeader);

        // Create summary table
        Table summaryTable = new Table(new float[]{1, 1, 1, 1});
        summaryTable.setWidth(500);

        // Headers
        summaryTable.addCell(new Cell().add(new Paragraph("Total Time Served").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add(new Paragraph("Remaining Time").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add(new Paragraph("Total Visitors").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add(new Paragraph("Status").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Calculate values
        String timeServed = calculateTimeServed(selectedInmateId);
        String remainingTime = calculateRemainingTimeForPDF(selectedInmateId);
        String visitorCount = getVisitorCount(selectedInmateId);
        String status = getInmateStatus(selectedInmateId);

        summaryTable.addCell(new Cell().add(new Paragraph(timeServed).setFont(normalFont)));
        summaryTable.addCell(new Cell().add(new Paragraph(remainingTime).setFont(normalFont)));
        summaryTable.addCell(new Cell().add(new Paragraph(visitorCount).setFont(normalFont)));
        summaryTable.addCell(new Cell().add(new Paragraph(status).setFont(normalFont)));

        document.add(summaryTable);
    }

    private void addActivityLogToDocument(Document document, PdfFont boldFont, PdfFont normalFont) throws SQLException {
        // Activity Log Header
        Paragraph activityHeader = new Paragraph("DISCIPLINARY ACTIONS / ACTIVITY LOG")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(activityHeader);

        // Create activity table
        Table activityTable = new Table(new float[]{2, 3, 4, 2});
        activityTable.setWidth(500);

        // Headers
        activityTable.addCell(new Cell().add(new Paragraph("Date").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        activityTable.addCell(new Cell().add(new Paragraph("Action").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        activityTable.addCell(new Cell().add(new Paragraph("Description").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        activityTable.addCell(new Cell().add(new Paragraph("Status").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Get activity data
        String sql = "SELECT da.action_date, da.action_name, da.description, da.status "
                + "FROM disciplinary_actions da WHERE da.inmate_id = ? "
                + "ORDER BY da.action_date DESC";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, selectedInmateId);
        ResultSet rs = pstmt.executeQuery();

        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            activityTable.addCell(new Cell().add(new Paragraph(rs.getString("action_date")).setFont(normalFont)));
            activityTable.addCell(new Cell().add(new Paragraph(rs.getString("action_name")).setFont(normalFont)));
            activityTable.addCell(new Cell().add(new Paragraph(rs.getString("description")).setFont(normalFont)));
            activityTable.addCell(new Cell().add(new Paragraph(rs.getString("status")).setFont(normalFont)));
        }

        if (!hasData) {
            activityTable.addCell(new Cell(1, 4).add(new Paragraph("No disciplinary actions recorded").setFont(normalFont).setTextAlignment(TextAlignment.CENTER)));
        }

        document.add(activityTable);
    }

    private void addVisitHistoryToDocument(Document document, PdfFont boldFont, PdfFont normalFont) throws SQLException {
        // Visit History Header
        Paragraph visitHeader = new Paragraph("VISIT HISTORY")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(visitHeader);

        // Create visit table
        Table visitTable = new Table(new float[]{2, 3, 2, 2});
        visitTable.setWidth(500);

        // Headers
        visitTable.addCell(new Cell().add(new Paragraph("Date").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        visitTable.addCell(new Cell().add(new Paragraph("Visitor Name").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        visitTable.addCell(new Cell().add(new Paragraph("Relation").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        visitTable.addCell(new Cell().add(new Paragraph("Contact").setFont(boldFont)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Get visit data
        String sql = "SELECT v.created_at, v.full_name, v.relation, v.contact "
                + "FROM visitors v WHERE v.inmate_id = ? "
                + "ORDER BY v.created_at DESC";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, selectedInmateId);
        ResultSet rs = pstmt.executeQuery();

        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            visitTable.addCell(new Cell().add(new Paragraph(rs.getString("created_at")).setFont(normalFont)));
            visitTable.addCell(new Cell().add(new Paragraph(rs.getString("full_name")).setFont(normalFont)));
            visitTable.addCell(new Cell().add(new Paragraph(rs.getString("relation")).setFont(normalFont)));
            visitTable.addCell(new Cell().add(new Paragraph(rs.getString("contact")).setFont(normalFont)));
        }

        if (!hasData) {
            visitTable.addCell(new Cell(1, 4).add(new Paragraph("No visits recorded").setFont(normalFont).setTextAlignment(TextAlignment.CENTER)));
        }

        document.add(visitTable);
    }

    private void exportActivityLog() {
        if (selectedInmateId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inmate first");
            return;
        }

        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            String fileName = "reports/Activity_Log_" + selectedInmateId + ".pdf";

            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont boldFont = PdfFontFactory.createFont();
            PdfFont normalFont = PdfFontFactory.createFont();

            // Header
            Paragraph header = new Paragraph("ACTIVITY LOG REPORT")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);

            // Inmate basic info
            addBasicInmateInfo(document, boldFont, normalFont);

            // Activity log
            addActivityLogToDocument(document, boldFont, normalFont);

            // Footer
            Paragraph footer = new Paragraph("Generated on: ")
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            document.add(footer);

            document.close();

            int result = JOptionPane.showConfirmDialog(this,
                    "Activity Log exported successfully!\nWould you like to open it?",
                    "Export Successful",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                openPDF(fileName);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting activity log: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exportVisitHistory() {
        if (selectedInmateId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an inmate first");
            return;
        }

        try {
            File reportsDir = new File("reports");
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }

            String fileName = "reports/Visit_History_" + selectedInmateId + "pdf";

            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont boldFont = PdfFontFactory.createFont();
            PdfFont normalFont = PdfFontFactory.createFont();

            // Header
            Paragraph header = new Paragraph("VISIT HISTORY REPORT")
                    .setFont(boldFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);

            // Inmate basic info
            addBasicInmateInfo(document, boldFont, normalFont);

            // Visit history
            addVisitHistoryToDocument(document, boldFont, normalFont);

            // Footer
            Paragraph footer = new Paragraph("Generated on: ")
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20);
            document.add(footer);

            document.close();

            int result = JOptionPane.showConfirmDialog(this,
                    "Visit History exported successfully!\nWould you like to open it?",
                    "Export Successful",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                openPDF(fileName);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting visit history: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addBasicInmateInfo(Document document, PdfFont boldFont, PdfFont normalFont) throws SQLException {
        String sql = "SELECT i.first_name, i.last_name, i.code, c.name as crime_name "
                + "FROM inmates i LEFT JOIN crimes c ON i.crime_id = c.crime_id "
                + "WHERE i.id = ?";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, selectedInmateId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            Paragraph inmateInfo = new Paragraph("Inmate: " + rs.getString("first_name") + " "
                    + rs.getString("last_name") + " (" + rs.getString("code") + ")")
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setMarginBottom(5);
            document.add(inmateInfo);

            Paragraph crimeInfo = new Paragraph("Crime: " + rs.getString("crime_name"))
                    .setFont(normalFont)
                    .setFontSize(10)
                    .setMarginBottom(15);
            document.add(crimeInfo);
        }
    }

    private void openPDF(String fileName) {
        try {
            File pdfFile = new File(fileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                JOptionPane.showMessageDialog(this, "PDF saved at: " + pdfFile.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open PDF. File saved at: " + fileName);
        }
    }

    private String calculateTimeServed(int inmateId) throws SQLException {
        String sql = "SELECT DATEDIFF(CURDATE(), time_served) as days_served FROM inmates WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("days_served") + " days";
        }
        return "N/A";
    }

    private String calculateRemainingTimeForPDF(int inmateId) throws SQLException {
        String sql = "SELECT time_end FROM inmates WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Date timeEnd = rs.getDate("time_end");
            if (timeEnd != null) {
                LocalDate endDate = timeEnd.toLocalDate();
                LocalDate currentDate = LocalDate.now();
                long daysRemaining = ChronoUnit.DAYS.between(currentDate, endDate);
                return daysRemaining + " days";
            }
        }
        return "N/A";
    }

    private String getVisitorCount(int inmateId) throws SQLException {
        String sql = "SELECT COUNT(*) as visitor_count FROM visitors WHERE inmate_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return String.valueOf(rs.getInt("visitor_count"));
        }
        return "0";
    }

    private String getInmateStatus(int inmateId) throws SQLException {
        String sql = "SELECT status FROM inmates WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, inmateId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("status").toUpperCase();
        }
        return "UNKNOWN";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        inmateComboBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        dateFilterCombo = new javax.swing.JComboBox<>();
        exportButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        timeEndLabel = new javax.swing.JLabel();
        timeServedLabel = new javax.swing.JLabel();
        sentenceLabel = new javax.swing.JLabel();
        crimeLabel = new javax.swing.JLabel();
        remainingTimeLabel = new javax.swing.JLabel();
        codeLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        visitorCountWidget = new javax.swing.JLabel();
        totalTimeServedWidget = new javax.swing.JLabel();
        remainingTimeWidget = new javax.swing.JLabel();
        statusBadge = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        activityTableModel = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        visitTableModel = new javax.swing.JTable();
        exportActivityButton = new javax.swing.JButton();
        exportVisitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1280, 670));
        setMinimumSize(new java.awt.Dimension(1280, 670));

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 18)); // NOI18N
        jLabel1.setText("Reports");

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 85));

        jLabel2.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel2.setText("Search & Filter");

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel8.setText("Select Inmate :");

        inmateComboBox.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        inmateComboBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel9.setText("or Search :");

        searchField.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        searchField.setBorder(null);

        searchButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        searchButton.setText("Search");
        searchButton.setBorder(null);

        jLabel10.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel10.setText("Filter :");

        dateFilterCombo.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        dateFilterCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "\"All Time\"", "\"This Month\"", "\"Last Month\"", "\"This Year\"", "\"Last Year\"", "\"Custom Range\"" }));
        dateFilterCombo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        exportButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        exportButton.setText("Export PDF");
        exportButton.setBorder(null);

        resetButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        resetButton.setText("Reset");
        resetButton.setBorder(null);

        fromDateChooser.setEnabled(false);
        fromDateChooser.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel16.setText("From");

        toDateChooser.setEnabled(false);
        toDateChooser.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel18.setText("To");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inmateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFilterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(331, 331, 331))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(fromDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(dateFilterCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(inmateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        imageLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        imageLabel.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel2.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 175, 300));

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("Name :");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 23, -1, -1));

        jLabel11.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel11.setText("Code :");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 56, -1, -1));

        jLabel13.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel13.setText("Crime :");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 95, -1, -1));

        jLabel15.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel15.setText("Sentence");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 134, -1, -1));

        jLabel17.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel17.setText("Time Served");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 173, -1, -1));

        jLabel19.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel19.setText("Time End");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 212, -1, -1));

        jLabel21.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel21.setText("Remaining");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 251, -1, -1));

        timeEndLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        timeEndLabel.setText("-");
        jPanel2.add(timeEndLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 212, 120, -1));

        timeServedLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        timeServedLabel.setText("-");
        jPanel2.add(timeServedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 173, 120, -1));

        sentenceLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        sentenceLabel.setText("-");
        jPanel2.add(sentenceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 134, 120, -1));

        crimeLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        crimeLabel.setText("-");
        jPanel2.add(crimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 95, 120, -1));

        remainingTimeLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        remainingTimeLabel.setText("-");
        jPanel2.add(remainingTimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 251, 120, -1));

        codeLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        codeLabel.setText("-");
        jPanel2.add(codeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 56, 120, -1));

        nameLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        nameLabel.setText("-");
        jPanel2.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 23, 120, -1));

        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel4.setText("Summary");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, -1, -1));

        visitorCountWidget.setText("jLabel3");
        jPanel3.add(visitorCountWidget, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 100, 70));

        totalTimeServedWidget.setText("jLabel3");
        totalTimeServedWidget.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jPanel3.add(totalTimeServedWidget, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 100, 70));

        remainingTimeWidget.setText("jLabel3");
        jPanel3.add(remainingTimeWidget, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 97, 70));
        jPanel3.add(statusBadge, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, 90, 40));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 326, 371, 160));

        jLabel22.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel22.setText("Status");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(195, 287, -1, -1));

        statusLabel.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        statusLabel.setText("-");
        jPanel2.add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 287, 120, -1));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jTabbedPane1.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N

        activityTableModel.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 12)); // NOI18N
        activityTableModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Action", "Description", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(activityTableModel);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Activity Log", jPanel4);

        visitTableModel.setFont(new java.awt.Font("Source Code Pro Medium", 0, 12)); // NOI18N
        visitTableModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Visitor Name", "Relation", "Contact", "Duration"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(visitTableModel);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Visitor Log", jPanel5);

        exportActivityButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        exportActivityButton.setText("Export Activity ");
        exportActivityButton.setBorder(null);

        exportVisitButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        exportVisitButton.setText("Export Visitor");
        exportVisitButton.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(607, 607, 607)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jTabbedPane1))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exportActivityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(exportVisitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(122, 122, 122)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(exportActivityButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportVisitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        FlatArcDarkIJTheme.setup();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable activityTableModel;
    private javax.swing.JLabel codeLabel;
    private javax.swing.JLabel crimeLabel;
    private javax.swing.JComboBox<String> dateFilterCombo;
    private javax.swing.JButton exportActivityButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JButton exportVisitButton;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JComboBox<String> inmateComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel remainingTimeLabel;
    private javax.swing.JLabel remainingTimeWidget;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel sentenceLabel;
    private javax.swing.JLabel statusBadge;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel timeEndLabel;
    private javax.swing.JLabel timeServedLabel;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JLabel totalTimeServedWidget;
    private javax.swing.JTable visitTableModel;
    private javax.swing.JLabel visitorCountWidget;
    // End of variables declaration//GEN-END:variables
}
