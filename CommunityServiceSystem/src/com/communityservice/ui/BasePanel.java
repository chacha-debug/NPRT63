package com.communityservice.ui;

import java.awt.*;
import javax.swing.*;

public abstract class BasePanel extends JPanel {
    protected JPanel leftSidebarPanel;
    protected JPanel rightContentPanel;
    private final JPanel stepsHost;

    public BasePanel(String sidebarTitle, String sidebarDescription) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        leftSidebarPanel = new JPanel(new BorderLayout());
        leftSidebarPanel.setBackground(AppTheme.NAVY_PRIMARY);
        leftSidebarPanel.setPreferredSize(new Dimension(360, 0));
        leftSidebarPanel.setBorder(BorderFactory.createEmptyBorder(34, 30, 34, 30));

        JPanel intro = new JPanel();
        intro.setOpaque(false);
        intro.setLayout(new BoxLayout(intro, BoxLayout.Y_AXIS));
        RoundPersonIcon logo = new RoundPersonIcon(64, Color.WHITE, AppTheme.NAVY_PRIMARY);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        intro.add(logo);
        intro.add(Box.createVerticalStrut(12));
        JLabel brand = centered("Community Service+", AppTheme.FONT_HEADER, new Color(224,231,255));
        intro.add(brand);
        intro.add(Box.createVerticalStrut(28));
        JLabel title = centered(sidebarTitle, AppTheme.FONT_TITLE, Color.WHITE);
        intro.add(title);
        intro.add(Box.createVerticalStrut(12));
        JLabel desc = new JLabel("<html><div style='text-align:center;width:250px'>" + sidebarDescription + "</div></html>");
        desc.setFont(AppTheme.FONT_BODY); desc.setForeground(new Color(224,231,255)); desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        intro.add(desc);
        JPanel introHost = new JPanel(new GridBagLayout());
        introHost.setOpaque(false);
        GridBagConstraints ig = new GridBagConstraints();
        ig.gridx=0; ig.gridy=0; ig.weightx=1; ig.weighty=1; ig.anchor=GridBagConstraints.CENTER; ig.fill=GridBagConstraints.HORIZONTAL;
        introHost.add(intro, ig);
        leftSidebarPanel.add(introHost, BorderLayout.CENTER);

        stepsHost = new JPanel(new BorderLayout());
        stepsHost.setOpaque(false);
        leftSidebarPanel.add(stepsHost, BorderLayout.SOUTH);

        rightContentPanel = new JPanel(new GridBagLayout());
        rightContentPanel.setBackground(Color.WHITE);
        rightContentPanel.setBorder(BorderFactory.createEmptyBorder(30, 42, 30, 42));
        add(leftSidebarPanel, BorderLayout.WEST);
        add(rightContentPanel, BorderLayout.CENTER);
    }

    private JLabel centered(String text, Font font, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER); l.setFont(font); l.setForeground(color); l.setAlignmentX(Component.CENTER_ALIGNMENT); return l;
    }

    public void addStepSidebar(int currentStep, String... stepNames) {
        stepsHost.removeAll();
        JPanel steps = new JPanel();
        steps.setLayout(new BoxLayout(steps, BoxLayout.Y_AXIS));
        steps.setOpaque(false);
        for (int i=0;i<stepNames.length;i++) {
            int n=i+1; boolean active=n==currentStep;
            JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,12,5)); row.setOpaque(false);
            JLabel circle=new JLabel(String.valueOf(n),SwingConstants.CENTER);
            circle.setFont(AppTheme.FONT_BODY.deriveFont(Font.BOLD)); circle.setPreferredSize(new Dimension(34,34)); circle.setOpaque(true);
            circle.setBackground(active?AppTheme.PRIMARY:new Color(255,255,255,40)); circle.setForeground(Color.WHITE);
            circle.setBorder(BorderFactory.createLineBorder(active?Color.WHITE:new Color(255,255,255,90),1,true));
            JLabel text=new JLabel(stepNames[i]); text.setFont(AppTheme.FONT_BODY); text.setForeground(active?Color.WHITE:new Color(191,219,254));
            row.add(circle); row.add(text); steps.add(row);
        }
        stepsHost.add(steps, BorderLayout.CENTER); stepsHost.revalidate(); stepsHost.repaint();
    }

    protected JLabel requiredLabel(String text) {
        JLabel label=new JLabel("<html>"+text+" <font color='#D25050'>*</font></html>"); label.setFont(AppTheme.FONT_BODY); label.setForeground(AppTheme.TEXT_DARK); return label;
    }

    protected void bindEnterToButton(JButton button) {
        InputMap im=getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT); ActionMap am=getActionMap();
        im.put(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER,0),"submit-form");
        am.put("submit-form",new AbstractAction(){@Override public void actionPerformed(java.awt.event.ActionEvent e){if(button.isEnabled())button.doClick();}});
    }
}
