package com.communityservice.ui;

import com.communityservice.models.NGO;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

/** Administrator review surface for email-verified NGOs awaiting approval. */
public class NgoVerificationPanel extends JPanel {
    private final DefaultListModel<NgoListItem> listModel = new DefaultListModel<>();
    private final JList<NgoListItem> ngoList = new JList<>(listModel);
    private final JLabel emptyLabel = new JLabel("No email-verified NGOs are awaiting approval.", JLabel.CENTER);
    private final JTextArea detailsArea = new JTextArea();
    private final JButton approveButton = new JButton("Approve NGO");
    private final JButton rejectButton = new JButton("Reject NGO");

    public NgoVerificationPanel() {
        setLayout(new BorderLayout(0, 16)); setBackground(UIColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        JPanel header = new JPanel(); header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS)); header.setOpaque(false);
        JLabel h = new JLabel("Pending NGO Registrations"); h.setFont(new Font("Arial", Font.BOLD, 22)); h.setForeground(UIColors.TEXT_DARK);
        JLabel sub = new JLabel("Review an email-verified organisation before allowing NGO dashboard access."); sub.setFont(new Font("Arial", Font.PLAIN, 13)); sub.setForeground(UIColors.TEXT_GREY);
        header.add(h); header.add(Box.createVerticalStrut(5)); header.add(sub); add(header, BorderLayout.NORTH);

        ngoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); ngoList.setFont(new Font("Arial", Font.PLAIN, 14)); ngoList.setFixedCellHeight(44);
        JScrollPane listScroll = new JScrollPane(ngoList); listScroll.setBorder(BorderFactory.createTitledBorder("Awaiting approval")); listScroll.setPreferredSize(new Dimension(300, 300));
        detailsArea.setEditable(false); detailsArea.setLineWrap(true); detailsArea.setWrapStyleWord(true); detailsArea.setFont(new Font("Arial", Font.PLAIN, 14)); detailsArea.setMargin(new Insets(10,10,10,10));
        JScrollPane detailsScroll = new JScrollPane(detailsArea); detailsScroll.setBorder(BorderFactory.createTitledBorder("Organisation details"));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailsScroll); split.setResizeWeight(.42); split.setBorder(null); split.setDividerSize(8);
        JPanel center = new JPanel(new BorderLayout()); center.setOpaque(false); emptyLabel.setForeground(UIColors.TEXT_GREY); emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14)); center.add(emptyLabel, BorderLayout.NORTH); center.add(split, BorderLayout.CENTER); add(center, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); actions.setOpaque(false);
        style(approveButton, UIColors.PRIMARY); style(rejectButton, UIColors.RED); approveButton.setEnabled(false); rejectButton.setEnabled(false); actions.add(rejectButton); actions.add(approveButton); add(actions, BorderLayout.SOUTH);
        ngoList.addListSelectionListener(this::selectionChanged);
    }
    private void style(JButton b, Color c){b.setBackground(c);b.setForeground(Color.WHITE);b.setFont(new Font("Arial",Font.BOLD,13));b.setFocusPainted(false);b.setBorderPainted(false);b.setOpaque(true);b.setPreferredSize(new Dimension(145,40));}
    private void selectionChanged(ListSelectionEvent e){ if(e.getValueIsAdjusting()) return; NgoListItem x=ngoList.getSelectedValue(); boolean yes=x!=null; approveButton.setEnabled(yes);rejectButton.setEnabled(yes); detailsArea.setText(yes?x.details():"Select an organisation to review its submitted details."); detailsArea.setCaretPosition(0); }
    public void setNGOs(List<NGO> ngos){listModel.clear();for(NGO n:ngos)listModel.addElement(new NgoListItem(n));boolean empty=ngos.isEmpty();emptyLabel.setVisible(empty);if(empty)detailsArea.setText("There are currently no email-verified NGO registrations awaiting administrator approval.");else if(!listModel.isEmpty())ngoList.setSelectedIndex(0);}
    public void setBusy(boolean busy){ngoList.setEnabled(!busy);boolean selected=ngoList.getSelectedValue()!=null;approveButton.setEnabled(!busy&&selected);rejectButton.setEnabled(!busy&&selected);approveButton.setText(busy?"Working...":"Approve NGO");rejectButton.setText(busy?"Working...":"Reject NGO");}
    private Integer id(){NgoListItem x=ngoList.getSelectedValue();return x==null?null:x.ngo.getNgoId();}
    public void setApproveListener(IntConsumer l){approveButton.addActionListener(e->{Integer id=id();if(id==null)return;if(JOptionPane.showConfirmDialog(this,"Approve this organisation? It will be able to sign in immediately.","Approve NGO",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)l.accept(id);});}
    public void setRejectListener(BiConsumer<Integer,String> l){rejectButton.addActionListener(e->{Integer id=id();if(id==null)return;JTextArea reason=new JTextArea(4,34);reason.setLineWrap(true);reason.setWrapStyleWord(true);int result=JOptionPane.showConfirmDialog(this,new Object[]{"Reason for rejection (required):",new JScrollPane(reason)},"Reject NGO",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);if(result==JOptionPane.OK_OPTION){String r=reason.getText().trim();if(r.isEmpty()){JOptionPane.showMessageDialog(this,"Please provide a rejection reason.","Reason Required",JOptionPane.WARNING_MESSAGE);return;}l.accept(id,r);}});}
    private static class NgoListItem{final NGO ngo;NgoListItem(NGO n){ngo=n;}public String toString(){String n=ngo.getOrgName();return n==null||n.isBlank()?"NGO #"+ngo.getNgoId():n;}String details(){return "Organisation: "+safe(ngo.getOrgName())+"\nRegistration number: "+safe(ngo.getRegistrationNumber())+"\nContact person: "+safe(ngo.getContactPerson())+"\nContact phone: "+safe(ngo.getContactPhone())+"\n\nThe organisation's email has been verified. Approval grants access to the NGO dashboard.";}static String safe(String s){return s==null||s.isBlank()?"Not provided":s;}}
}
