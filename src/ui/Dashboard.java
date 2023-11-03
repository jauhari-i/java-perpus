package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Anggota;
import model.Buku;
import model.Peminjaman;
import model.User;

public class Dashboard {
  public class ButtonRenderer extends JButton implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      setText((value == null) ? "" : value.toString());
      return this;
    }
  }

  public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private ActionListener listener;
    private int row;
    private int column;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, ActionListener listener, int column) {
      super(checkBox);
      this.listener = listener;
      this.column = column;
      button = new JButton();
      button.setOpaque(true);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          fireEditingStopped();
          listener.actionPerformed(
              new ActionEvent(this, ActionEvent.ACTION_PERFORMED, table.getValueAt(row, column).toString()));
        }
      });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.table = table;
      this.row = row;
      label = (value == null) ? "" : value.toString();
      button.setText(label);
      isPushed = true;
      return button;
    }

    public Object getCellEditorValue() {
      if (isPushed) {
        button.addActionListener(
            new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                fireEditingStopped();

                listener.actionPerformed(
                    new ActionEvent(this, ActionEvent.ACTION_PERFORMED, table.getValueAt(row, column).toString()));
              }
            });
      }
      isPushed = false;
      return label;
    }

    public boolean stopCellEditing() {
      isPushed = false;
      return super.stopCellEditing();
    }
  }

  public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (value != null && value.equals("Sudah Kembali")) {
        c.setForeground(Color.WHITE);
        c.setBackground(Color.GREEN);
        c.setFont(new Font("Arial", Font.BOLD, 12));
      } else {
        c.setBackground(table.getBackground());
        c.setForeground(Color.BLACK);
      }
      return c;
    }
  }

  public class TerlambatCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (value != null && value.equals("-")) {
        c.setForeground(Color.BLACK);
        c.setBackground(table.getBackground());
      } else {
        c.setForeground(Color.WHITE);
        c.setBackground(Color.RED);
        c.setFont(new Font("Arial", Font.BOLD, 12));
      }
      return c;
    }
  }

  private JFrame frame;
  private User user;
  private JMenu anggota;
  private JMenu buku;
  private JMenu peminjaman;
  private JMenu profile;
  private JMenuItem profileUser;
  private JMenuItem logout;
  private JPanel profilePanel;
  private JLabel nameLabel;
  private JLabel emailLabel;
  private JLabel noKaryawanLabel;
  private JButton logoutButton;
  private JMenuItem formAnggota;
  private JMenuItem listAnggota;
  private JPanel anggotaListPanel;
  private JButton submitButton;
  private JTextField nameAnggotaField;
  private JTextField nimAnggotaField;
  private JTextField jurusanAnggotaField;
  private JTable tableAnggota;
  private JTextField emailAnggotaField;
  private JComboBox<String> statusComboBox;
  private JMenuItem formBuku;
  private JMenuItem listBuku;
  private JTextField searchBukuField;
  private JButton searchBukuButton;
  private String searchPlaceHolder = "Cari Buku Berdasarkan Penulis, Pengarang, ISBN, atau Genre...";
  private JTextField namaBuku;
  private JTextField penulisBuku;
  private JTextField isbnBuku;
  private JTextField genreBuku;
  private int idBuku;
  private JMenuItem formPeminjamanBuku;
  private JMenuItem listPeminjamanBuku;
  private JButton addPeminjamanButton;
  private JComboBox<String> anggotaPeminjaman;
  private JComboBox<String> bukuPeminjaman;
  private JTextField durasiPeminjaman;
  private JDateChooser tanggalPinjam;
  private JDateChooser tanggalKembali;

  // Dashboard Menu & Constructor

  public Dashboard() {
    frame = new JFrame();
    frame.setTitle("Dashboard Perpustakaan");
    frame.setSize(1300, 700);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);

    JMenuBar menuBar = new JMenuBar();

    anggota = new JMenu("Anggota");
    buku = new JMenu("Buku");
    peminjaman = new JMenu("Peminjaman");
    profile = new JMenu("");

    menuBar.add(anggota);
    menuBar.add(buku);
    menuBar.add(peminjaman);
    menuBar.add(profile);

    listAnggota = new JMenuItem("List Anggota");
    formAnggota = new JMenuItem("Form Anggota");

    anggota.add(listAnggota);
    anggota.add(formAnggota);

    profileUser = new JMenuItem("Profile");
    logout = new JMenuItem("Logout");

    profile.add(profileUser);
    profile.add(logout);

    listBuku = new JMenuItem("List Buku");
    formBuku = new JMenuItem("Form Buku");

    buku.add(listBuku);
    buku.add(formBuku);

    formPeminjamanBuku = new JMenuItem("Form Peminjaman Buku");
    listPeminjamanBuku = new JMenuItem("List Peminjaman Buku");

    peminjaman.add(listPeminjamanBuku);
    peminjaman.add(formPeminjamanBuku);

    frame.setJMenuBar(menuBar);

  }

  // Get Panel

  public JPanel getProfilePanel(User user, ActionListener listener) {
    profilePanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    c.insets = new Insets(5, 5, 5, 5);
    profilePanel.add(new JLabel("Name: "), c);
    c.gridy++;
    profilePanel.add(new JLabel("Email: "), c);
    c.gridy++;
    profilePanel.add(new JLabel("No. Karyawan: "), c);
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    nameLabel = new JLabel(user.getName());
    profilePanel.add(nameLabel, c);
    c.gridy++;
    emailLabel = new JLabel(user.getEmail());
    profilePanel.add(emailLabel, c);
    c.gridy++;
    noKaryawanLabel = new JLabel(user.getNoKaryawan());
    profilePanel.add(noKaryawanLabel, c);

    logoutButton = new JButton("Logout");
    logoutButton.addActionListener(listener);
    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.CENTER;
    profilePanel.add(logoutButton, c);

    return profilePanel;
  }

  public JPanel getAnggotaListPanel(ArrayList<Anggota> anggotaList, ActionListener listener) {
    anggotaListPanel = new JPanel(new BorderLayout());

    JLabel titleLabel = new JLabel("Daftar Anggota");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    anggotaListPanel.add(titleLabel, BorderLayout.NORTH);

    String[] columnNames = { "No", "Name", "NIM", "Jurusan", "Status", "Email", "Aksi" };
    Object[][] data = new Object[anggotaList.size()][7];
    for (int i = 0; i < anggotaList.size(); i++) {
      Anggota anggota = anggotaList.get(i);
      data[i][0] = i + 1;
      data[i][1] = anggota.getNama();
      data[i][2] = anggota.getNim();
      data[i][3] = anggota.getJurusan();
      data[i][4] = anggota.getStatus();
      data[i][5] = anggota.getEmail();
      data[i][6] = "Edit";
    }
    DefaultTableModel anggotaTable = new DefaultTableModel(data, columnNames);
    tableAnggota = new JTable(anggotaTable);
    tableAnggota.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12)); // set font to bold
    tableAnggota.getTableHeader().setReorderingAllowed(false);
    tableAnggota.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    tableAnggota.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    tableAnggota.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
    tableAnggota.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(), listener, 5));
    tableAnggota.getFillsViewportHeight();

    JScrollPane scrollPane = new JScrollPane(tableAnggota);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    anggotaListPanel.add(scrollPane, BorderLayout.CENTER);

    return anggotaListPanel;
  }

  public JPanel getDashboardPanel() {
    JPanel dashboardPanel = new JPanel(new BorderLayout());

    JLabel greetingLabel = new JLabel("Selamat Datang di Applikasi Perpus");
    greetingLabel.setFont(new Font("Arial", Font.BOLD, 20));
    greetingLabel.setHorizontalAlignment(SwingConstants.CENTER); // set text center
    greetingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    dashboardPanel.add(greetingLabel, BorderLayout.NORTH);

    return dashboardPanel;
  }

  public JPanel getAnggotaFormPanel(ActionListener listener) {
    JPanel anggotaFormPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = 3;
    JLabel titleLabel = new JLabel("Form Anggota");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    anggotaFormPanel.add(titleLabel, c);

    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(5, 5, 5, 5);
    anggotaFormPanel.add(new JLabel("Nama: "), c);
    c.gridy++;
    anggotaFormPanel.add(new JLabel("NIM: "), c);
    c.gridy++;
    anggotaFormPanel.add(new JLabel("Jurusan: "), c);
    c.gridy++;
    anggotaFormPanel.add(new JLabel("Status: "), c);
    c.gridy++;
    anggotaFormPanel.add(new JLabel("Email: "), c);

    c.gridx = 2;
    c.gridy = 1;
    c.anchor = GridBagConstraints.CENTER;
    nameAnggotaField = new JTextField(20);
    anggotaFormPanel.add(nameAnggotaField, c);
    c.gridy++;
    nimAnggotaField = new JTextField(20);
    anggotaFormPanel.add(nimAnggotaField, c);
    c.gridy++;
    jurusanAnggotaField = new JTextField(20);
    anggotaFormPanel.add(jurusanAnggotaField, c);
    c.gridy++;
    statusComboBox = new JComboBox<String>();
    statusComboBox.setPreferredSize(new Dimension(25 * 10, statusComboBox.getPreferredSize().height));
    statusComboBox.addItem("Aktif");
    statusComboBox.addItem("Tidak Aktif");
    anggotaFormPanel.add(statusComboBox, c);
    c.gridy++;
    emailAnggotaField = new JTextField(20);
    anggotaFormPanel.add(emailAnggotaField, c);

    emailAnggotaField.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          submitButton.doClick();
        }
      }
    });

    submitButton = new JButton("Submit");
    submitButton.addActionListener(listener);

    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    anggotaFormPanel.add(submitButton, c);

    return anggotaFormPanel;
  }

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

    searchBukuField = new JTextField(20);
    searchBukuField.setForeground(Color.GRAY);
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
    searchBukuField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          searchBukuButton.doClick();
        }
      }
    });

    searchBukuButton = new JButton("Search");
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
    tableBuku.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
    tableBuku.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(), listener, 0));
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

  public JPanel getBukuFormPanel(ActionListener listener) {
    JPanel bukuFormPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = 3;
    JLabel titleLabel = new JLabel("Form Buku");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    bukuFormPanel.add(titleLabel, c);

    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(5, 5, 5, 5);
    bukuFormPanel.add(new JLabel("Nama Buku: "), c);
    c.gridy++;
    bukuFormPanel.add(new JLabel("Penulis: "), c);
    c.gridy++;
    bukuFormPanel.add(new JLabel("ISBN: "), c);
    c.gridy++;
    bukuFormPanel.add(new JLabel("Genre: "), c);

    c.gridx = 2;
    c.gridy = 1;
    c.anchor = GridBagConstraints.CENTER;
    namaBuku = new JTextField(20);
    bukuFormPanel.add(namaBuku, c);
    c.gridy++;
    penulisBuku = new JTextField(20);
    bukuFormPanel.add(penulisBuku, c);
    c.gridy++;
    isbnBuku = new JTextField(20);
    bukuFormPanel.add(isbnBuku, c);
    c.gridy++;
    genreBuku = new JTextField(20);
    bukuFormPanel.add(genreBuku, c);

    genreBuku.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          submitButton.doClick();
        }
      }
    });

    submitButton = new JButton("Submit");
    submitButton.addActionListener(listener);

    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    bukuFormPanel.add(submitButton, c);

    return bukuFormPanel;
  }

  public JPanel getPeminjamanList(ArrayList<Peminjaman> peminjamanList, ActionListener listener) {
    JPanel peminjamanListPanel = new JPanel(new BorderLayout());

    JLabel titleLabel = new JLabel("Daftar Peminjaman Buku");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel searchPanel = new JPanel(new BorderLayout());
    searchPanel.add(titleLabel, BorderLayout.NORTH);

    addPeminjamanButton = new JButton("Buat Peminjaman");
    searchPanel.add(addPeminjamanButton, BorderLayout.EAST);
    peminjamanListPanel.add(searchPanel, BorderLayout.PAGE_START);

    String[] columnNames = { "ID", "Nama Buku", "Nama Peminjam", "Tanggal Pinjam", "Tanggal Kembali", "Durasi",
        "Status", "Keterlambatan", "Aksi" };
    Object[][] data = new Object[peminjamanList.size()][9];
    for (int i = 0; i < peminjamanList.size(); i++) {
      Peminjaman peminjaman = peminjamanList.get(i);
      data[i][0] = peminjaman.getId();
      data[i][1] = peminjaman.getBuku().getNama_buku();
      data[i][2] = peminjaman.getAnggota().getNama();
      data[i][3] = peminjaman.getTanggalPinjam();

      if (peminjaman.getTanggalKembali() == null) {
        data[i][4] = "-";
      } else {
        data[i][4] = peminjaman.getTanggalKembali();
      }

      data[i][5] = peminjaman.getDurasi() + " hari";
      data[i][6] = peminjaman.getStatusPeminjaman();
      data[i][7] = peminjaman.getStatusKeterlambatan();
      data[i][8] = "Edit";
    }

    DefaultTableModel peminjamanTable = new DefaultTableModel(data, columnNames) {
      @Override
      public boolean isCellEditable(int row, int column) {
        if (column == 8) {
          return true;
        } else {
          return false;
        }
      }
    };

    JTable tablePeminjaman = new JTable(peminjamanTable);
    tablePeminjaman.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    tablePeminjaman.getTableHeader().setReorderingAllowed(false);
    tablePeminjaman.getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    tablePeminjaman.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    tablePeminjaman.getColumn("Aksi").setCellRenderer(new ButtonRenderer());
    tablePeminjaman.getColumn("Aksi").setCellEditor(new ButtonEditor(new JCheckBox(), listener, 0));
    tablePeminjaman.getColumn("Status").setCellRenderer(new StatusCellRenderer());
    tablePeminjaman.getColumn("Keterlambatan").setCellRenderer(new TerlambatCellRenderer());
    tablePeminjaman.getFillsViewportHeight();

    TableColumn idColumn = tablePeminjaman.getColumnModel().getColumn(0);
    idColumn.setMaxWidth(30);

    TableColumn nameColumn = tablePeminjaman.getColumnModel().getColumn(1);
    nameColumn.setMinWidth(300);

    JScrollPane scrollPane = new JScrollPane(tablePeminjaman);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    peminjamanListPanel.add(scrollPane);

    return peminjamanListPanel;
  }

  public JPanel getFormPeminjamanPanel(ArrayList<Anggota> anggotaList, ArrayList<Buku> bukuList,
      ActionListener listener) {
    JPanel formPeminjamanListPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = 3;
    JLabel titleLabel = new JLabel("Form Peminjaman Buku");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    formPeminjamanListPanel.add(titleLabel, c);

    c.gridx = 1;
    c.gridy = 1;
    c.gridwidth = 1;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(5, 5, 5, 5);
    formPeminjamanListPanel.add(new JLabel("Nama Peminjam: "), c);
    c.gridy++;
    formPeminjamanListPanel.add(new JLabel("Nama Buku: "), c);
    c.gridy++;
    formPeminjamanListPanel.add(new JLabel("Durasi Peminjaman: "), c);
    c.gridy++;
    formPeminjamanListPanel.add(new JLabel("Tanggal Pinjam: "), c);
    c.gridy++;
    formPeminjamanListPanel.add(new JLabel("Tanggal Kembali: "), c);

    c.gridx = 2;
    c.gridy = 1;
    c.anchor = GridBagConstraints.CENTER;
    anggotaPeminjaman = new JComboBox<String>();
    anggotaPeminjaman.setPreferredSize(new Dimension(25 * 10, anggotaPeminjaman.getPreferredSize().height));
    for (int i = 0; i < anggotaList.size(); i++) {
      anggotaPeminjaman.addItem(anggotaList.get(i).getNama());
    }
    formPeminjamanListPanel.add(anggotaPeminjaman, c);
    c.gridy++;
    bukuPeminjaman = new JComboBox<String>();
    bukuPeminjaman.setPreferredSize(new Dimension(25 * 10, bukuPeminjaman.getPreferredSize().height));
    for (int i = 0; i < bukuList.size(); i++) {
      bukuPeminjaman.addItem(bukuList.get(i).getNama_buku());
    }
    formPeminjamanListPanel.add(bukuPeminjaman, c);
    c.gridy++;
    durasiPeminjaman = new JTextField(20);
    durasiPeminjaman.addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
          e.consume();
        }
      }
    });

    formPeminjamanListPanel.add(durasiPeminjaman, c);
    c.gridy++;
    tanggalPinjam = new JDateChooser();
    tanggalPinjam.setDateFormatString("dd-MM-yyyy");
    tanggalPinjam.setPreferredSize(new Dimension(25 * 10, tanggalPinjam.getPreferredSize().height));
    tanggalPinjam.setMinSelectableDate(new Date());
    formPeminjamanListPanel.add(tanggalPinjam, c);
    c.gridy++;
    tanggalKembali = new JDateChooser();
    tanggalKembali.setDateFormatString("dd-MM-yyyy");
    tanggalKembali.setPreferredSize(new Dimension(25 * 10, tanggalKembali.getPreferredSize().height));
    tanggalKembali.setMinSelectableDate(new Date());
    tanggalKembali.setEnabled(false);
    formPeminjamanListPanel.add(tanggalKembali, c);

    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 3;
    c.anchor = GridBagConstraints.CENTER;
    submitButton = new JButton("Submit");
    submitButton.addActionListener(listener);
    formPeminjamanListPanel.add(submitButton, c);

    durasiPeminjaman.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if ("date".equals(evt.getPropertyName())) {
          Date tanggalPinjamValue = (Date) evt.getNewValue();
          int durasiPeminjamanValue = getDurasiPeminjaman();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(tanggalPinjamValue);
          calendar.add(Calendar.DATE, durasiPeminjamanValue);
          tanggalKembali.setDate(calendar.getTime());
        }
      }
    });

    tanggalPinjam.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if ("date".equals(evt.getPropertyName())) {
          Date tanggalPinjamValue = (Date) evt.getNewValue();
          int durasiPeminjamanValue = getDurasiPeminjaman();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(tanggalPinjamValue);
          calendar.add(Calendar.DATE, durasiPeminjamanValue);
          tanggalKembali.setDate(calendar.getTime());
        }
      }
    });
    formPeminjamanListPanel.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          submitButton.doClick();
        }
      }
    });

    return formPeminjamanListPanel;
  }

  // Get Peminjaman Form Value

  public String getNamaPeminjam() {
    return anggotaPeminjaman.getSelectedItem().toString();
  }

  public String getNamaBukuPeminjaman() {
    return bukuPeminjaman.getSelectedItem().toString();
  }

  public int getDurasiPeminjaman() {
    String durasi = durasiPeminjaman.getText();
    return Integer.parseInt(durasi);
  }

  public Date getTanggalPinjam() {
    return tanggalPinjam.getDate();
  }

  public Date getTanggalKembali() {
    return tanggalKembali.getDate();
  }

  // Set Peminjaman Form Value

  public void setNamaPeminjam(String nama) {
    anggotaPeminjaman.setSelectedItem(nama);
    anggotaPeminjaman.setEnabled(false);
  }

  public void setBukuPeminjaman(String buku) {
    bukuPeminjaman.setSelectedItem(buku);
    bukuPeminjaman.setEnabled(false);
  }

  public void setDurasiPeminjaman(int durasi) {
    durasiPeminjaman.setText(Integer.toString(durasi));
    durasiPeminjaman.setEnabled(false);
  }

  public void setTanggalPinjam(Date tanggal) {
    tanggalPinjam.setDate(tanggal);
    tanggalPinjam.setEnabled(false);
    tanggalKembali.setEnabled(true);
  }

  public void setTanggalKembali(Date tanggal, Date tglPinjam) {
    tanggalKembali.setDate(tanggal);
    tanggalKembali.setMinSelectableDate(tglPinjam);
    tanggalKembali.setEnabled(true);
  }

  // Reset Value

  public void resetPeminjamanFormValue() {
    anggotaPeminjaman.setSelectedIndex(0);
    bukuPeminjaman.setSelectedIndex(0);
    durasiPeminjaman.setText("");
    tanggalPinjam.setDate(new Date());

    anggotaPeminjaman.setEnabled(true);
    bukuPeminjaman.setEnabled(true);
    durasiPeminjaman.setEnabled(true);
    tanggalPinjam.setEnabled(true);
  }

  // Get Buku Form Value

  public int getIdBuku() {
    return idBuku;
  }

  public String getNamaBuku() {
    return namaBuku.getText();
  }

  public String getPenulisBuku() {
    return penulisBuku.getText();
  }

  public String getIsbnBuku() {
    return isbnBuku.getText();
  }

  public String getGenreBuku() {
    return genreBuku.getText();
  }

  // Set Buku Form Value

  public void setIdBuku(int id) {
    idBuku = id;
  }

  public void setNamaBuku(String nama) {
    namaBuku.setText(nama);
  }

  public void setPenulisBuku(String penulis) {
    penulisBuku.setText(penulis);
  }

  public void setIsbnBuku(String isbn) {
    isbnBuku.setText(isbn);
  }

  public void setGenreBuku(String genre) {
    genreBuku.setText(genre);
  }

  // Get Search Buku Value

  public String getSearchBuku() {
    if (searchBukuField.getText().equals(searchPlaceHolder)) {
      return "";
    } else {
      return searchBukuField.getText();
    }
  }

  // Get Anggota Form Value

  public String getNameAnggota() {
    return nameAnggotaField.getText();
  }

  public BigInteger getNimAnggota() {
    String nim = nimAnggotaField.getText();
    return new BigInteger(nim);
  }

  public String getJurusanAnggota() {
    return jurusanAnggotaField.getText();
  }

  public int getStatusAnggota() {
    String status = statusComboBox.getSelectedItem().toString();
    if (status == "Aktif") {
      return 1;
    } else {
      return 0;
    }
  }

  public String getEmailAnggota() {
    return emailAnggotaField.getText();
  }

  // Set Anggota Form Value

  public void setNameAnggota(String name) {
    nameAnggotaField.setText(name);
  }

  public void setNimAnggota(BigInteger nim) {
    nimAnggotaField.setText(nim.toString());
  }

  public void setJurusanAnggota(String jurusan) {
    jurusanAnggotaField.setText(jurusan);
  }

  public void setStatusAnggota(int status) {
    if (status == 1) {
      statusComboBox.setSelectedItem("Aktif");
    } else {
      statusComboBox.setSelectedItem("Tidak Aktif");
    }
  }

  public void setEmailAnggota(String email) {
    emailAnggotaField.setText(email);
  }

  // Action Listener

  public void addActionListenerSubmitAnggotaButton(ActionListener listener) {
    submitButton.addActionListener(listener);
  }

  public void addListAnggotaListener(ActionListener listener) {
    listAnggota.addActionListener(listener);
  }

  public void addActionListenerProfileUser(ActionListener listener) {
    profileUser.addActionListener(listener);
  }

  public void addActionListenerLogout(ActionListener listener) {
    logout.addActionListener(listener);
  }

  public void addActionListenerFormAnggota(ActionListener listener) {
    formAnggota.addActionListener(listener);
  }

  public void addActionListenerFormBuku(ActionListener listener) {
    formBuku.addActionListener(listener);
  }

  public void addActionListenerListBuku(ActionListener listener) {
    listBuku.addActionListener(listener);
  }

  public void addActionListenerSearchBuku(ActionListener listener) {
    searchBukuButton.addActionListener(listener);
  }

  public void addActionListenerFormPeminjamanBuku(ActionListener listener) {
    formPeminjamanBuku.addActionListener(listener);
  }

  public void addActionListenerListPeminjamanBuku(ActionListener listener) {
    listPeminjamanBuku.addActionListener(listener);
  }

  public void addActionListenerAddPeminjaman(ActionListener listener) {
    addPeminjamanButton.addActionListener(listener);
  }

  // Set User Data For Header

  public void setUserData(User user) {
    this.user = user;
    profile.setText("Akun Aktif: " + user.getName());
  }

  // Set Search Value

  public void setSearchBuku(String search) {
    searchBukuField.setText(search);
    searchBukuField.requestFocus();
  }

  // Get Frame

  public JFrame getFrame() {
    return frame;
  }
}