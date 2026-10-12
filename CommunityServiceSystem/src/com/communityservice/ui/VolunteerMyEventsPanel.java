package com.communityservice.ui;

import com.communityservice.dao.OpportunityApplicationDAO;
import com.communityservice.dao.VolunteerPortalVolunteerDAO;
import com.communityservice.models.OpportunityApplication;
import com.communityservice.models.PortalVolunteer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Shows only opportunities the volunteer has actually been approved to attend. */
public class VolunteerMyEventsPanel extends JPanel {
    private final int userId;
    private final JPanel content = new JPanel();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public VolunteerMyEventsPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout()); setBackground(UIColors.BACKGROUND);
        JPanel header=new JPanel(new BorderLayout()); header.setBackground(UIColors.PRIMARY); header.setBorder(new EmptyBorder(20,25,20,25));
        JLabel title=new JLabel("My Events"); title.setForeground(Color.WHITE); title.setFont(new Font("Segoe UI",Font.BOLD,24));
        JLabel hint=new JLabel("Events appear here after your application has been approved."); hint.setForeground(Color.WHITE); hint.setFont(new Font("Segoe UI",Font.PLAIN,13));
        JPanel text=new JPanel(); text.setOpaque(false); text.setLayout(new BoxLayout(text,BoxLayout.Y_AXIS)); text.add(title); text.add(Box.createVerticalStrut(4)); text.add(hint); header.add(text,BorderLayout.WEST);
        JButton refresh=new JButton("Refresh"); refresh.addActionListener(e->load()); header.add(refresh,BorderLayout.EAST); add(header,BorderLayout.NORTH);
        content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS)); content.setBackground(UIColors.BACKGROUND); content.setBorder(new EmptyBorder(20,25,30,25));
        JScrollPane scroll=new JScrollPane(content); scroll.setBorder(null); scroll.getViewport().setBackground(UIColors.BACKGROUND); add(scroll,BorderLayout.CENTER); load();
    }
    private void load(){
        content.removeAll();
        PortalVolunteer v=new VolunteerPortalVolunteerDAO().getVolunteerByUserID(userId);
        if(v==null){empty("Volunteer profile could not be found.");return;}
        List<OpportunityApplication> apps=new OpportunityApplicationDAO().getApplicationsByVolunteer(v.getVolunteerID());
        int count=0;
        if(apps!=null) for(OpportunityApplication a:apps){
            if(!"Approved".equalsIgnoreCase(a.getStatus())) continue;
            count++;
            JPanel card=new JPanel(new GridLayout(0,1,2,2)); card.setBackground(Color.WHITE); card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIColors.BORDER),new EmptyBorder(12,14,12,14)));
            JLabel t=new JLabel(a.getOpportunityTitle()==null?"Event":a.getOpportunityTitle()); t.setFont(new Font("Segoe UI",Font.BOLD,16)); card.add(t);
            card.add(new JLabel("Date: "+(a.getOpportunityDate()==null?"To be confirmed":a.getOpportunityDate().format(fmt))));
            card.add(new JLabel("Location: "+(a.getOpportunityLocation()==null?"To be confirmed":a.getOpportunityLocation())));
            card.add(new JLabel("Application status: Approved"));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE,110)); content.add(card); content.add(Box.createVerticalStrut(10));
        }
        if(count==0) empty("You do not have any approved events yet. Apply under Available Opportunities; approved applications will appear here.");
        content.revalidate(); content.repaint();
    }
    private void empty(String message){JLabel l=new JLabel("<html>"+message+"</html>");l.setFont(new Font("Segoe UI",Font.PLAIN,14));l.setForeground(UIColors.TEXT_GREY);content.add(l);content.revalidate();content.repaint();}
}
