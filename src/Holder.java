import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

import model.Buku;
import ui.Dashboard.ButtonEditor;
import ui.Dashboard.ButtonRenderer;

public class Holder {
  public JPanel getBukuListPanel(ArrayList<Buku> bukuList, ActionListener listener, ActionListener search) {
    JPanel bukuListPanel = new JPanel(new BorderLayout());

    JLabel titleLabel = new JLabel("Daftar Buku");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel searchPanel = new JPanel(new BorderLayout());
    JLabel searchTitle = new JLabel("Cari Buku: ");
    searchTitle.setFont(new Font("Arial", Font.BOLD, 12));
    searchPanel.add(searchTitle, BorderLayout.WEST);
    searchPanel.add(titleLabel, BorderLayout.NORTH);

    JTextField searchBukuField = new JTextField(20);
    searchBukuField.setForeground(Color.GRAY);
    String searchPlaceHolder = "";
    searchBukuField.setText(searchPlaceHolder);
    searchBukuField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (searchBukuField.getText().equals(searchPlaceHolder)) {
          searchBukuField.setText("");
          searchBukuField.setForeground(Color.BLACK);
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (searchBukuField.getText().isEmpty()) {
          searchBukuField.setForeground(Color.GRAY);
          searchBukuField.setText(searchPlaceHolder);
        }
      }
    });
    JButton searchBukuButton = new JButton("Search");
    searchBukuField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          searchBukuButton.doClick();
        }
      }
    });
    searchBukuButton.addActionListener(listener);
    searchBukuButton.setActionCommand(getSearchBuku());
    searchPanel.add(searchBukuField, BorderLayout.CENTER);
    searchPanel.add(searchBukuButton, BorderLayout.EAST);
    bukuListPanel.add(searchPanel, BorderLayout.PAGE_START);

    String[] columnNames = { "ID", "Nama Buku", "Penulis", "ISBN", "Genre", "Aksi" };
    Object[][] data = new Object[bukuList.size()][6];
    for (int i = 0; i < bukuList.size(); i++) {
      Buku buku = bukuList.get(i);
      data[i][0] = buku.getId();
      data[i][1] = buku.getNama_buku();
      data[i][2] = buku.getPenulis();
      data[i][3] = buku.getIsbn();
      data[i][4] = buku.getGenre();
      data[i][5] = "Edit";
    }
    DefaultTableModel bukuTable = new DefaultTableModel(data, columnNames);
    JTable tableBuku = new JTable(bukuTable);
    tableBuku.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    tableBuku.getTableHeader().setReorderingAllowed(false);
    tableBuku.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    tableBuku.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    // tableBuku.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
    // tableBuku.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(),
    // listener, 0));
    tableBuku.getFillsViewportHeight();

    TableColumn idColumn = tableBuku.getColumnModel().getColumn(0);
    idColumn.setMaxWidth(30);

    TableColumn nameColumn = tableBuku.getColumnModel().getColumn(1);
    nameColumn.setMinWidth(300);

    JScrollPane scrollPane = new JScrollPane(tableBuku);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    bukuListPanel.add(scrollPane, BorderLayout.CENTER);

    return bukuListPanel;
  }

  private String getSearchBuku() {
    return null;
  }

}
