
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class staffDash extends javax.swing.JFrame {

    /**
     * Creates new form adminDash
     */
    public staffDash(String userType) {
        initComponents();
        initChartPanels();
        initStatCards();
        initActionPanels();
        // Add action listeners to each button

        viewInmateBtn.addActionListener(e -> showActiveInmates());

        Prisons.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PrisonFrame().setVisible(true);
            }
        });
        CellBlocks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CellBlockFrame().setVisible(true);
            }
        });

        Inmates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new InmateFrame().setVisible(true);
            }
        });
        Visitors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VisitorFrame().setVisible(true);
            }
        });
        Crimes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CrimeFrame().setVisible(true);
            }
        });
        Actions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ActionFrame().setVisible(true);
            }
        });
        Reports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ReportFrame().setVisible(true);
            }
        });
       

        searchButton.addActionListener(e -> {
            String text = searchField.getText().trim();
            if (!text.isEmpty()) {
                searchInmateDetails(text);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter search text.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Prisons = new javax.swing.JButton();
        CellBlocks = new javax.swing.JButton();
        Inmates = new javax.swing.JButton();
        Visitors = new javax.swing.JButton();
        Crimes = new javax.swing.JButton();
        Reports = new javax.swing.JButton();
        Actions = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        themeComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        CellBlockCard = new javax.swing.JPanel();
        CurrentInmateCard = new javax.swing.JPanel();
        ReleasedInmateCard = new javax.swing.JPanel();
        TodayVisitCard = new javax.swing.JPanel();
        ChartPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CrimeTrendChart = new javax.swing.JPanel();
        PrisonChart = new javax.swing.JPanel();
        VisitorPieChart = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ReleaseCountdownPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        RiskAlertsPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        DisciplinaryActionPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        viewInmateBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(1, 1, 1), 2));
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 50));

        Prisons.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Prisons.setText("Prison");
        Prisons.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        CellBlocks.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        CellBlocks.setText("CellBlock");
        CellBlocks.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        CellBlocks.setMaximumSize(new java.awt.Dimension(47, 24));
        CellBlocks.setMinimumSize(new java.awt.Dimension(47, 24));
        CellBlocks.setPreferredSize(new java.awt.Dimension(47, 24));

        Inmates.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Inmates.setText("Inmates");
        Inmates.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        Visitors.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Visitors.setText("Visitors");
        Visitors.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        Crimes.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Crimes.setText("Crimes");
        Crimes.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        Reports.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Reports.setText("Reports");
        Reports.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        Actions.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        Actions.setText("Actions");
        Actions.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel12.setText("Staff Dashbaord");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel12)
                .addGap(153, 153, 153)
                .addComponent(Prisons, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CellBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Inmates, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Visitors, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Crimes, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Actions, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Reports, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(250, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(Prisons, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CellBlocks, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Inmates, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Visitors, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Crimes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Actions, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Reports, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 5, 1, new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel6.setText("Theme :");

        searchField.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N

        searchButton.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        searchButton.setText("Search");
        searchButton.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        themeComboBox.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        themeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Flat Atom One Light Contrast", "Flat Atom One Dark Contrast", "Flat Carbon", "Flat Darcula" }));
        themeComboBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        themeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeComboBoxActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Source Sans 3 Medium", 1, 14)); // NOI18N
        jLabel7.setText("Search Inmate :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(themeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 5, 1, new java.awt.Color(0, 0, 0)));

        CellBlockCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout CellBlockCardLayout = new javax.swing.GroupLayout(CellBlockCard);
        CellBlockCard.setLayout(CellBlockCardLayout);
        CellBlockCardLayout.setHorizontalGroup(
            CellBlockCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );
        CellBlockCardLayout.setVerticalGroup(
            CellBlockCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        CurrentInmateCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout CurrentInmateCardLayout = new javax.swing.GroupLayout(CurrentInmateCard);
        CurrentInmateCard.setLayout(CurrentInmateCardLayout);
        CurrentInmateCardLayout.setHorizontalGroup(
            CurrentInmateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );
        CurrentInmateCardLayout.setVerticalGroup(
            CurrentInmateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ReleasedInmateCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout ReleasedInmateCardLayout = new javax.swing.GroupLayout(ReleasedInmateCard);
        ReleasedInmateCard.setLayout(ReleasedInmateCardLayout);
        ReleasedInmateCardLayout.setHorizontalGroup(
            ReleasedInmateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 152, Short.MAX_VALUE)
        );
        ReleasedInmateCardLayout.setVerticalGroup(
            ReleasedInmateCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        TodayVisitCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout TodayVisitCardLayout = new javax.swing.GroupLayout(TodayVisitCard);
        TodayVisitCard.setLayout(TodayVisitCardLayout);
        TodayVisitCardLayout.setHorizontalGroup(
            TodayVisitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 136, Short.MAX_VALUE)
        );
        TodayVisitCardLayout.setVerticalGroup(
            TodayVisitCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 44, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(CellBlockCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentInmateCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReleasedInmateCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TodayVisitCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(TodayVisitCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CellBlockCard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CurrentInmateCard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ReleasedInmateCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );

        ChartPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel2.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel2.setText("Dashboard Charts");

        CrimeTrendChart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        CrimeTrendChart.setPreferredSize(new java.awt.Dimension(362, 180));

        javax.swing.GroupLayout CrimeTrendChartLayout = new javax.swing.GroupLayout(CrimeTrendChart);
        CrimeTrendChart.setLayout(CrimeTrendChartLayout);
        CrimeTrendChartLayout.setHorizontalGroup(
            CrimeTrendChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );
        CrimeTrendChartLayout.setVerticalGroup(
            CrimeTrendChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        PrisonChart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        PrisonChart.setPreferredSize(new java.awt.Dimension(362, 180));

        javax.swing.GroupLayout PrisonChartLayout = new javax.swing.GroupLayout(PrisonChart);
        PrisonChart.setLayout(PrisonChartLayout);
        PrisonChartLayout.setHorizontalGroup(
            PrisonChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );
        PrisonChartLayout.setVerticalGroup(
            PrisonChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        VisitorPieChart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        VisitorPieChart.setPreferredSize(new java.awt.Dimension(362, 180));

        javax.swing.GroupLayout VisitorPieChartLayout = new javax.swing.GroupLayout(VisitorPieChart);
        VisitorPieChart.setLayout(VisitorPieChartLayout);
        VisitorPieChartLayout.setHorizontalGroup(
            VisitorPieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );
        VisitorPieChartLayout.setVerticalGroup(
            VisitorPieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel3.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel3.setText("Action Panels");

        ReleaseCountdownPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        ReleaseCountdownPanel.setPreferredSize(new java.awt.Dimension(400, 100));

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N
        jLabel8.setText("Inmate Release Countdown");

        javax.swing.GroupLayout ReleaseCountdownPanelLayout = new javax.swing.GroupLayout(ReleaseCountdownPanel);
        ReleaseCountdownPanel.setLayout(ReleaseCountdownPanelLayout);
        ReleaseCountdownPanelLayout.setHorizontalGroup(
            ReleaseCountdownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReleaseCountdownPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(218, Short.MAX_VALUE))
        );
        ReleaseCountdownPanelLayout.setVerticalGroup(
            ReleaseCountdownPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReleaseCountdownPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RiskAlertsPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        RiskAlertsPanel.setPreferredSize(new java.awt.Dimension(400, 100));

        jLabel9.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N
        jLabel9.setText("High Risk Inmate Alert");

        javax.swing.GroupLayout RiskAlertsPanelLayout = new javax.swing.GroupLayout(RiskAlertsPanel);
        RiskAlertsPanel.setLayout(RiskAlertsPanelLayout);
        RiskAlertsPanelLayout.setHorizontalGroup(
            RiskAlertsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RiskAlertsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(248, Short.MAX_VALUE))
        );
        RiskAlertsPanelLayout.setVerticalGroup(
            RiskAlertsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RiskAlertsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        DisciplinaryActionPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        DisciplinaryActionPanel.setPreferredSize(new java.awt.Dimension(400, 100));

        jLabel10.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N
        jLabel10.setText("Diciplinary & Punishment Countdown");

        javax.swing.GroupLayout DisciplinaryActionPanelLayout = new javax.swing.GroupLayout(DisciplinaryActionPanel);
        DisciplinaryActionPanel.setLayout(DisciplinaryActionPanelLayout);
        DisciplinaryActionPanelLayout.setHorizontalGroup(
            DisciplinaryActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisciplinaryActionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(152, Short.MAX_VALUE))
        );
        DisciplinaryActionPanelLayout.setVerticalGroup(
            DisciplinaryActionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisciplinaryActionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(574, 574, 574)
                        .addComponent(jLabel3))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(ReleaseCountdownPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RiskAlertsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(DisciplinaryActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(RiskAlertsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DisciplinaryActionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ReleaseCountdownPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ChartPanelLayout = new javax.swing.GroupLayout(ChartPanel);
        ChartPanel.setLayout(ChartPanelLayout);
        ChartPanelLayout.setHorizontalGroup(
            ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChartPanelLayout.createSequentialGroup()
                .addGroup(ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ChartPanelLayout.createSequentialGroup()
                        .addGroup(ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ChartPanelLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(PrisonChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(CrimeTrendChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(VisitorPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ChartPanelLayout.createSequentialGroup()
                                .addGap(575, 575, 575)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ChartPanelLayout.setVerticalGroup(
            ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChartPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VisitorPieChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CrimeTrendChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PrisonChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel5.setFont(new java.awt.Font("Source Sans 3 Black", 0, 16)); // NOI18N
        jLabel5.setText("Extra Features");

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jTabbedPane2.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 13)); // NOI18N
        jLabel11.setText("No incident reports available");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addContainerGap(1161, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Parole Eligibility", jPanel16);

        jLabel4.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 13)); // NOI18N
        jLabel4.setText("No health alerts available");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addContainerGap(1179, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Inmate Status", jPanel17);

        jLabel1.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 13)); // NOI18N
        jLabel1.setText("No incident reports available");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(1161, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Incident Reports", jPanel18);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(598, 598, 598)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        viewInmateBtn.setFont(new java.awt.Font("Source Sans 3 Black", 0, 14)); // NOI18N
        viewInmateBtn.setText("View Inmate");
        viewInmateBtn.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(ChartPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(viewInmateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(viewInmateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateTheme() {
        SwingUtilities.updateComponentTreeUI(this);
    }


    private void themeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themeComboBoxActionPerformed

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

        // TODO add your handling code here:
    }//GEN-LAST:event_themeComboBoxActionPerformed

    // Search function to look up inmate details.
    private void searchInmateDetails(String searchText) {
        StringBuilder sb = new StringBuilder();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT first_name, last_name, time_served, time_end FROM inmates WHERE first_name LIKE ? OR last_name LIKE ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String timeServed = rs.getString("time_served");
                String timeEnd = rs.getString("time_end");
                long remainingDays = calculateRemainingDays(timeEnd);
                sb.append("Inmate: ").append(firstName).append(" ").append(lastName).append("\n")
                        .append("Time Served: ").append(timeServed).append("\n")
                        .append("Release Date: ").append(timeEnd).append("\n")
                        .append("Remaining Days: ").append(remainingDays).append(" days\n\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (sb.length() == 0) {
            sb.append("No inmate found.");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    // Helper method to get a count from the database.
    private int getCount(Connection con, String query) {
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // Calculate the remaining days until release.
    private long calculateRemainingDays(String timeEnd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date releaseDate = sdf.parse(timeEnd);
            long diffMillis = releaseDate.getTime() - new Date().getTime();
            return TimeUnit.MILLISECONDS.toDays(diffMillis);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    // Parse duration from a string.
    private int parseDuration(String durationStr) {
        try {
            return Integer.parseInt(durationStr.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // Calculate the remaining punishment days.
    private long calculatePunishmentRemainingDays(Date actionDate, int durationDays) {
        try {
            long startMillis = actionDate.getTime();
            long totalMillis = TimeUnit.DAYS.toMillis(durationDays);
            long punishmentEnd = startMillis + totalMillis;
            long remainingMillis = punishmentEnd - new Date().getTime();
            return Math.max(TimeUnit.MILLISECONDS.toDays(remainingMillis), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void initChartPanels() {
        // Use a proper layout manager so that the chart fills its container.
        PrisonChart.setLayout(new java.awt.BorderLayout());
        CrimeTrendChart.setLayout(new java.awt.BorderLayout());
        VisitorPieChart.setLayout(new java.awt.BorderLayout());

        // Add the Prison Chart to its container
        PrisonChart.removeAll();
        PrisonChart.add(createPrisonChart(), java.awt.BorderLayout.CENTER);
        PrisonChart.validate();
        PrisonChart.repaint();

        // Add the Crime Trend Chart to its container
        CrimeTrendChart.removeAll();
        CrimeTrendChart.add(createCrimeTrendChart(), java.awt.BorderLayout.CENTER);
        CrimeTrendChart.validate();
        CrimeTrendChart.repaint();

        // Add the Visitor Pie Chart to its container
        VisitorPieChart.removeAll();
        VisitorPieChart.add(createVisitorPieChart(), java.awt.BorderLayout.CENTER);
        VisitorPieChart.validate();
        VisitorPieChart.repaint();

    }
// Chart: Bar chart for Prison Status Overview.

    private ChartPanel createPrisonChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection con = DBConnection.getConnection()) {
            int active = getCount(con, "SELECT COUNT(*) FROM cell_blocks WHERE status='active'");
            int inactive = getCount(con, "SELECT COUNT(*) FROM cell_blocks WHERE status='inactive'");
            dataset.addValue(active, "Active", "Cell");
            dataset.addValue(inactive, "Inactive", "Cell");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart("Prison Status Overview", "Status", "Count", dataset);
        return new ChartPanel(chart);
    }

// Chart: Line chart for Crime Trends.
    private ChartPanel createCrimeTrendChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT DATE(created_at) as crime_date, COUNT(*) as crime_count FROM crimes GROUP BY crime_date";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String dateStr = rs.getString("crime_date");
                int count = rs.getInt("crime_count");
                dataset.addValue(count, "Crimes", dateStr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createLineChart("Crime Trends", "Date", "Crime Count", dataset);
        return new ChartPanel(chart);
    }

// Chart: Pie chart for Visitor Distribution.
    private ChartPanel createVisitorPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT relation, COUNT(*) as count FROM visitors GROUP BY relation";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String relation = rs.getString("relation");
                int count = rs.getInt("count");
                dataset.setValue(relation, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createPieChart("Visitor Distribution", dataset, true, true, false);
        return new ChartPanel(chart);
    }

    // Helper for creating a dashboard card.
    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.foreground"), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>" + title
                + "<br/><b>" + value + "</b></div></html>",
                SwingConstants.CENTER);
        label.setFont(new Font("Sans Source Black", Font.BOLD, 14));
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private void initStatCards() {
        // Get your stats using your existing helper (make sure getStat is implemented)
        String cellBlocks = getStat("SELECT COUNT(*) FROM cell_blocks");
        String currentInmates = getStat("SELECT COUNT(*) FROM inmates WHERE status='active'");
        String releasedInmates = getStat("SELECT COUNT(*) FROM inmates WHERE status='released'");
        String todaysVisits = getStat("SELECT COUNT(*) FROM visitors WHERE DATE(created_at) = CURDATE()");

        // Set a proper layout so that the card fills the container.
        CellBlockCard.setLayout(new BorderLayout());
        CurrentInmateCard.setLayout(new BorderLayout());
        ReleasedInmateCard.setLayout(new BorderLayout());
        TodayVisitCard.setLayout(new BorderLayout());

        // Clear each panel and add the card to it.
        CellBlockCard.removeAll();
        CellBlockCard.add(createCard("Cell Blocks", cellBlocks), BorderLayout.CENTER);
        CellBlockCard.validate();
        CellBlockCard.repaint();

        CurrentInmateCard.removeAll();
        CurrentInmateCard.add(createCard("Current Inmates", currentInmates), BorderLayout.CENTER);
        CurrentInmateCard.validate();
        CurrentInmateCard.repaint();

        ReleasedInmateCard.removeAll();
        ReleasedInmateCard.add(createCard("Released Inmates", releasedInmates), BorderLayout.CENTER);
        ReleasedInmateCard.validate();
        ReleasedInmateCard.repaint();

        TodayVisitCard.removeAll();
        TodayVisitCard.add(createCard("Today's Visits", todaysVisits), BorderLayout.CENTER);
        TodayVisitCard.validate();
        TodayVisitCard.repaint();
    }

    // Initializes the action panels when the dashboard loads.
    private void initActionPanels() {
        // For the Release Countdown Panel
        ReleaseCountdownPanel.setLayout(new java.awt.BorderLayout());
        ReleaseCountdownPanel.removeAll();
        ReleaseCountdownPanel.add(createReleaseCountdownPanel(), java.awt.BorderLayout.CENTER);
        ReleaseCountdownPanel.validate();
        ReleaseCountdownPanel.repaint();

        // For the Risk Alerts Panel
        RiskAlertsPanel.setLayout(new java.awt.BorderLayout());
        RiskAlertsPanel.removeAll();
        RiskAlertsPanel.add(createRiskAlertsPanel(), java.awt.BorderLayout.CENTER);
        RiskAlertsPanel.validate();
        RiskAlertsPanel.repaint();

        // For the Disciplinary Action Panel
        DisciplinaryActionPanel.setLayout(new java.awt.BorderLayout());
        DisciplinaryActionPanel.removeAll();
        DisciplinaryActionPanel.add(createDisciplinaryActionPanel(), java.awt.BorderLayout.CENTER);
        DisciplinaryActionPanel.validate();
        DisciplinaryActionPanel.repaint();
    }
// Action Panel: Inmate Release Countdown.

    private JPanel createReleaseCountdownPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.foreground")),
                "Inmate Release Countdown", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Sans Source 3 Black", Font.BOLD, 14)));
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT first_name, last_name, time_end FROM inmates WHERE status='active'";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String timeEnd = rs.getString("time_end");
                long remainingDays = calculateRemainingDays(timeEnd);
                JLabel label = new JLabel("⏳ " + name + " – Release in: " + remainingDays + " days");
                label.setFont(new Font("Sans Source 3 Medium", Font.PLAIN, 14));
                panel.add(label);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return panel;
    }

    // Action Panel: High-Risk Inmate Alerts.
    private JPanel createRiskAlertsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.foreground")),
                "High-Risk Inmate Alerts", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Sans Source 3 Black", Font.BOLD, 14)));
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT first_name, last_name FROM inmates WHERE crime_id = (SELECT crime_id FROM crimes WHERE name='Murder')";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                JLabel label = new JLabel("⚠️ High-Risk Inmate: " + name);
                label.setFont(new Font("Sans Source 3 Medium", Font.PLAIN, 14));
                panel.add(label);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return panel;
    }

    // Action Panel: Disciplinary Actions & Punishment Countdown.
    private JPanel createDisciplinaryActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.foreground")),
                "Disciplinary Actions & Punishment Countdown", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Sans Source 3 Black", Font.BOLD, 14)));
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT inmate_id, action_name, punishment, duration, action_date FROM disciplinary_actions WHERE status='Active'";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int inmateId = rs.getInt("inmate_id");
                String actionName = rs.getString("action_name");
                String punishment = rs.getString("punishment");
                String durationStr = rs.getString("duration");
                Date actionDate = rs.getDate("action_date");
                int durationDays = parseDuration(durationStr);
                long remainingDays = calculatePunishmentRemainingDays(actionDate, durationDays);
                JLabel label = new JLabel("⚠️ ID " + inmateId + " – " + actionName
                        + " | Punishment: " + punishment + " | Ends in: " + remainingDays + " days");
                label.setFont(new Font("Sans Source 3 Medium", Font.PLAIN, 14));
                panel.add(label);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return panel;
    }

    // Helper method to extract a single statistic.
    private String getStat(String query) {
        String result = "0";
        try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private void showActiveInmates() {
        JDialog dialog = new JDialog(this, "Active Inmates", true);
        dialog.setSize(500, 550);
        dialog.setLayout(new BorderLayout());

        // Center the dialog before making it visible
        dialog.setLocationRelativeTo(null);

        JPanel inmatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
//        inmatePanel.setBackground(new Color(245, 245, 245));

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT first_name, last_name, image, cell_block_id, sentence, time_end FROM inmates WHERE status='active'";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String imagePath = rs.getString("image");
                int cellBlockId = rs.getInt("cell_block_id");
                String sentence = rs.getString("sentence");
                String timeEnd = rs.getString("time_end");

                long remainingDays = calculateRemainingDays(timeEnd);
                String cellBlockName = getCellBlockName(con, cellBlockId);

                JPanel inmateCard = new JPanel(new BorderLayout());
                inmateCard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                inmateCard.setBackground(Color.WHITE);
                inmateCard.setPreferredSize(new Dimension(230, 80));

                JLabel imageLabel = createImageLabel(imagePath);

                JLabel nameLabel = new JLabel(name);
                nameLabel.setFont(new Font("Source Sans 3 Black", Font.BOLD, 14));

                JLabel infoLabel = new JLabel("<html>📍 " + cellBlockName
                        + "<br>⏳ " + remainingDays + " days</html>");
                infoLabel.setFont(new Font("Source Sans 3 Medium", Font.PLAIN, 13));

                JPanel textPanel = new JPanel(new BorderLayout());
                textPanel.add(nameLabel, BorderLayout.NORTH);
                textPanel.add(infoLabel, BorderLayout.CENTER);

                inmateCard.add(imageLabel, BorderLayout.WEST);
                inmateCard.add(textPanel, BorderLayout.CENTER);
                inmatePanel.add(inmateCard);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dialog.add(new JScrollPane(inmatePanel), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JLabel createImageLabel(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage()
                        .getScaledInstance(60, 60, Image.SCALE_SMOOTH)); // Smaller Avatar
                return new JLabel(icon);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new JLabel("No Image", SwingConstants.CENTER);
    }

    private String getCellBlockName(Connection con, int cellBlockId) {
        String cellBlockName = "Unknown";
        try {
            String query = "SELECT name FROM cell_blocks WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, cellBlockId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cellBlockName = rs.getString("name");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cellBlockName;
    }

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
            java.util.logging.Logger.getLogger(adminDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(adminDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(adminDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(adminDash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SwingUtilities.invokeLater(() -> new adminDash("staff").setVisible(true));
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Actions;
    private javax.swing.JPanel CellBlockCard;
    private javax.swing.JButton CellBlocks;
    private javax.swing.JPanel ChartPanel;
    private javax.swing.JPanel CrimeTrendChart;
    private javax.swing.JButton Crimes;
    private javax.swing.JPanel CurrentInmateCard;
    private javax.swing.JPanel DisciplinaryActionPanel;
    private javax.swing.JButton Inmates;
    private javax.swing.JPanel PrisonChart;
    private javax.swing.JButton Prisons;
    private javax.swing.JPanel ReleaseCountdownPanel;
    private javax.swing.JPanel ReleasedInmateCard;
    private javax.swing.JButton Reports;
    private javax.swing.JPanel RiskAlertsPanel;
    private javax.swing.JPanel TodayVisitCard;
    private javax.swing.JPanel VisitorPieChart;
    private javax.swing.JButton Visitors;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JComboBox<String> themeComboBox;
    private javax.swing.JButton viewInmateBtn;
    // End of variables declaration//GEN-END:variables
}
