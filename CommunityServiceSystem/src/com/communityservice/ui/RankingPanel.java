/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import com.communityservice.dao.RankingDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RankingPanel extends JPanel {

    private JTable rankingTable;

    private DefaultTableModel tableModel;

    private RankingDAO rankingDAO;

    public RankingPanel() {

        rankingDAO =
                new RankingDAO();

        initializeUI();

        loadRanking();
    }

    private void initializeUI() {

        setLayout(
                new BorderLayout(
                        15,
                        15
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "Volunteer Leaderboard",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        add(
                title,
                BorderLayout.NORTH
        );

        String[] columns = {

                "Rank",
                "Volunteer ID",
                "Volunteer",
                "Total Hours",
                "Completed Events"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        rankingTable =
                new JTable(tableModel);

        rankingTable.setRowHeight(30);

        JScrollPane scrollPane =
                new JScrollPane(
                        rankingTable
                );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        JButton refreshButton =
                new JButton(
                        "Refresh Ranking"
                );

        refreshButton.addActionListener(
                e -> loadRanking()
        );

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottomPanel.add(
                refreshButton
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );
    }

    private void loadRanking() {

        tableModel.setRowCount(0);

        List<Object[]> ranking =
                rankingDAO
                        .getVolunteerRanking();

        for (Object[] row : ranking) {

            tableModel.addRow(row);
        }
    }

    public void refresh() {

        loadRanking();
    }
}