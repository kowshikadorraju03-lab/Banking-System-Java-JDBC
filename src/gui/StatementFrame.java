package gui;

import service.BankService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class StatementFrame extends JFrame {

    BankService bs = new BankService();

    public StatementFrame(int userId) {

        setTitle("Mini Statement");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {
                "Transaction ID",
                "Type",
                "Amount (₹)",
                "Date & Time"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);

        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        try {

            ResultSet rs = bs.getMiniStatement(userId);

            while (rs != null && rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("transaction_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date")
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        JButton exportBtn = new JButton("Export CSV");
        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);

        add(exportBtn, BorderLayout.SOUTH);
        exportBtn.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            int option = chooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {

                java.io.File file = chooser.getSelectedFile();

                try (java.io.PrintWriter pw = new java.io.PrintWriter(file + ".csv")) {

                    // Column Names
                    for (int i = 0; i < table.getColumnCount(); i++) {

                        pw.print(table.getColumnName(i));

                        if (i < table.getColumnCount() - 1)
                            pw.print(",");

                    }

                    pw.println();

                    // Table Data
                    for (int row = 0; row < table.getRowCount(); row++) {

                        for (int col = 0; col < table.getColumnCount(); col++) {

                            pw.print(table.getValueAt(row, col));

                            if (col < table.getColumnCount() - 1)
                                pw.print(",");

                        }

                        pw.println();
                    }

                    JOptionPane.showMessageDialog(
                            this,
                            "Mini Statement exported successfully!"
                    );

                } catch (Exception ex) {

                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            this,
                            "Export Failed!"
                    );
                }
            }
        });
        setVisible(true);
    }
}