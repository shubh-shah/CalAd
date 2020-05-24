
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.calendar.Calendar;

class CalAdgui {
    private Calendar service;
    private Border btnborder = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(100, 100, 100));
    private Color btncolor = new Color(60, 60, 60);
    private Color btnfg = Color.WHITE;
    private Color bg = new Color(40, 40, 40);
    private Font btnfont = new Font("Century Gothic", Font.PLAIN, 13);
    JLabel labCID;
    JTextField tboxCID;
    JButton saveCID;

    CalAdgui() throws IOException, GeneralSecurityException {
        JFrame frame = new JFrame("CalAd");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        service = CalAd.connectToGoogle();

        JPanel paneMaster = new JPanel();
        paneMaster.setBackground(bg);
        paneMaster.setLayout(new BoxLayout(paneMaster, BoxLayout.PAGE_AXIS));

        JPanel pane0 = createUpper();
        JPanel pane1 = createMiddle();
        JPanel pane2 = createMiddle2();
        JPanel pane3 = createLower();
        paneMaster.add(pane0);
        paneMaster.add(Box.createRigidArea(new Dimension(0, 30)));
        paneMaster.add(pane1);
        paneMaster.add(Box.createRigidArea(new Dimension(0, 10)));
        paneMaster.add(pane2);
        paneMaster.add(pane3);

        frame.setContentPane(paneMaster);
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    private JPanel createUpper() {
        JLabel caladlabel = new JLabel(" CalAd");
        caladlabel.setFont(new Font("Century Gothic", Font.BOLD, 48));
        caladlabel.setForeground(new Color(255, 255, 255));

        JPanel pane = new JPanel();
        pane.setBackground(new Color(30, 30, 30));
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        pane.add(caladlabel);
        return pane;
    }

    private JPanel createMiddle() {
        // Get Current Calendar
        String WorkingCalendar = Contest.getWorkingCalendar();
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
        pane.setBackground(bg);

        labCID = new JLabel("Please Enter Calendar ID : ");
        labCID.setForeground(Color.WHITE);
        labCID.setFont(btnfont);

        tboxCID = new JTextField(WorkingCalendar, 30);
        tboxCID.setSize(300, 70);
        tboxCID.setMinimumSize(tboxCID.getSize());
        tboxCID.setSize(1000, 80);
        tboxCID.setMaximumSize(tboxCID.getSize());

        saveCID = new JButton("Save");
        tboxCID.setSize(80, 70);
        tboxCID.setMinimumSize(tboxCID.getSize());
        saveCID.setSize(1000, 80);
        saveCID.setMaximumSize(saveCID.getSize());
        saveCID.setBackground(btncolor);
        saveCID.setBorder(btnborder);
        saveCID.setForeground(btnfg);
        saveCID.setFont(btnfont);

        if (WorkingCalendar != null) {
            labCID.setText("Current Calendar ID : ");
            tboxCID.setEditable(false);
            saveCID.setText("Change");
        }
        saveCID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (saveCID.getText() == "Save") {
                    Contest.setWorkingCalendar(tboxCID.getText());
                    labCID.setText("Current Calendar ID : ");
                    tboxCID.setEditable(false);
                    saveCID.setText("Change");
                } else {
                    labCID.setText("Please Enter Calendar ID : ");
                    tboxCID.setEditable(true);
                    saveCID.setText("Save");
                }
            }
        });
        pane.add(Box.createRigidArea(new Dimension(30, 0)));
        pane.add(labCID);
        pane.add(tboxCID);
        pane.add(Box.createRigidArea(new Dimension(10, 0)));
        pane.add(saveCID);
        pane.add(Box.createRigidArea(new Dimension(30, 0)));
        pane.setSize(800, 50);
        return pane;
    }

    private JPanel createMiddle2() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        pane.setBackground(bg);
        GridBagConstraints c = new GridBagConstraints();

        JLabel labTag = new JLabel("Add tags to exclude   : ");
        labTag.setHorizontalAlignment(JLabel.CENTER);
        labTag.setForeground(Color.WHITE);
        labTag.setBorder(btnborder);
        labTag.setBackground(new Color(50, 50, 50));
        labTag.setFont(btnfont);
        labTag.setOpaque(true);

        JTextField tboxTag = new JTextField(30);
        tboxTag.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(180, 180, 180)));

        JButton pushTag = new JButton("Push");
        pushTag.setBackground(btncolor);
        pushTag.setBorder(btnborder);
        pushTag.setForeground(btnfg);
        pushTag.setFont(btnfont);

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        pushTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Contest.exclusionList.add(tboxTag.getText());
                listModel.addElement(tboxTag.getText());
                tboxTag.setText("");
            }
        });
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridy = 1;
        c.ipady = 10;

        c.gridx = 0;
        c.ipadx = 50;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(labTag, c);
        c.gridx = 1;
        c.ipadx = 400;
        pane.add(tboxTag, c);
        c.gridx = 2;
        c.ipadx = 0;
        pane.add(pushTag, c);

        Contest.getPreference();
        listModel.addAll(Contest.exclusionList);
        JList<String> table = new JList<String>(listModel);
        table.setBackground(new Color(130, 130, 130));
        table.setForeground(new Color(0, 0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        table.setSelectedIndex(0);
        table.setVisibleRowCount(7);
        JScrollPane scrlP = new JScrollPane(table);
        scrlP.setBackground(new Color(130, 130, 130));
        scrlP.setBorder(btnborder);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.ipady = 100;
        c.insets = new Insets(0, 0, 0, 0);
        pane.add(scrlP, c);

        JButton rm = new JButton("Remove Tag");
        rm.setBackground(btncolor);
        rm.setBorder(btnborder);
        rm.setForeground(btnfg);
        rm.setFont(btnfont);
        rm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int index = table.getSelectedIndex();
                listModel.remove(index);
                Contest.exclusionList.remove(index);
            }
        });
        JButton reset = new JButton("Reset to Defaults");
        reset.setBackground(btncolor);
        reset.setBorder(btnborder);
        reset.setForeground(btnfg);
        reset.setFont(btnfont);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Contest.EXCLUDE_TAG_PATH = "user/excludeTagsDefault";
                Contest.getPreference();
                Contest.EXCLUDE_TAG_PATH = "user/excludeTagsUser";
                listModel.clear();
                listModel.addAll(Contest.exclusionList);
            }
        });
        JButton save5 = new JButton("Save Preferences to Disk");
        save5.setBackground(btncolor);
        save5.setBorder(btnborder);
        save5.setForeground(btnfg);
        save5.setFont(btnfont);
        save5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Contest.setPreference();
            }
        });
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 10;
        c.insets = new Insets(0, 0, 0, 0);
        pane.add(rm, c);
        c.gridx = 1;
        pane.add(reset, c);
        c.gridx = 2;
        c.ipadx = 30;
        pane.add(save5, c);
        return pane;
    }

    /*
     * private JPanel createMidLower(){ 
     * //add time constraints 
     * //Add options for platforms 
     * //Tabs for past logs 
     * //View Contests Tab and option to select/deselect them before pushing
     * //add info on how to get calendar id 
     * //backgroung process option 
     * //Make contest checking offline
     * }
     */
    private JPanel createLower() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        pane.setBackground(bg);

        GridBagConstraints c = new GridBagConstraints();
        JButton logout = new JButton("Logout");
        JLabel cnfrm = new JLabel("Logged in");
        logout.setBackground(btncolor);
        logout.setBorder(btnborder);
        logout.setForeground(btnfg);
        logout.setFont(btnfont);
        logout.setSize(500, 80);
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (logout.getText() == "Logout") {
                    CalAd.logout();
                    service = null;
                    tboxCID.setText("");
                    labCID.setText("Please Enter Calendar ID : ");
                    tboxCID.setEditable(true);
                    saveCID.setText("Save");
                    logout.setText("Login");
                    cnfrm.setText("Logged Out");
                } else {
                    try {
                        service = CalAd.connectToGoogle();
                    } catch (Exception e) {
                        System.exit(0);
                    }
                    logout.setText("Logout");
                    cnfrm.setText("Logged In");
                }
            }
        });

        cnfrm.setFont(btnfont);
        cnfrm.setForeground(Color.WHITE);
        cnfrm.setHorizontalAlignment(JLabel.CENTER);

        JButton push = new JButton("Push Contests to Calendar");
        push.setSize(500, 80);
        push.setBackground(btncolor);
        push.setBorder(btnborder);
        push.setForeground(btnfg);
        push.setFont(btnfont);
        push.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    cnfrm.setText("...");
                    int changes = CalAd.pushContests(service);
                    CalAd.writeLog(changes,true);
                    cnfrm.setText("Contests Successfully Pushed to Calendar | Changes Made = " + changes);
                } catch (IOException ioe) {
                    cnfrm.setText("Contests were not Pushed to Calendar");
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.ipadx = 10;
        c.ipady = 10;
        c.weightx = 1;
        c.insets = new Insets(0, 20, 0, 20);
        pane.add(push, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 1;
        c.insets = new Insets(0, 20, 0, 20);
        pane.add(logout, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(cnfrm, c);
        return pane;
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        new CalAdgui();
    }
}

//by shubh-shah