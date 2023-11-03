import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import ui.*;
import model.*;

public class Controller {
  public class LoginButton implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      LoginAction(loginUI.getEmail(), loginUI.getPassword());
    }

  }

  public class LogoutUser implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      user = new User("", "", "", "");
      dashboard.getFrame().dispose();
      loginUI.getFrame().setVisible(true);
    }
  }

  public class ProfileUser implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      GetProfile();
    }
  }

  public class ListAnggota implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      GetListAnggota();
    }
  }

  public class FormAnggota implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      JPanel formAnggotaPanel = dashboard.getAnggotaFormPanel(new AddFormAnggota());
      dashboard.getFrame().getContentPane().removeAll();
      dashboard.getFrame().setContentPane(formAnggotaPanel);
      dashboard.getFrame().revalidate();
      dashboard.getFrame().repaint();
    }
  }

  public class AddFormAnggota implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      AddAnggota(dashboard.getNameAnggota(), dashboard.getNimAnggota(),
          dashboard.getJurusanAnggota(), dashboard.getStatusAnggota(), dashboard.getEmailAnggota());
    }
  }

  public class RedirectEditAnggota implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String selectedValue = e.getActionCommand();
      GetAnggotaByEmail(selectedValue);
    }
  }

  public class EditFormAnggota implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      UpdateAnggotaByEmail(dashboard.getEmailAnggota());
    }
  }

  public class RedirectListBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      GetListBuku();
    }
  }

  public class RedirectEditBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String selectedValue = e.getActionCommand();
      GetBukuById(Integer.parseInt(selectedValue));
      dashboard.setIdBuku(Integer.parseInt(selectedValue));
    }
  }

  public class EditFormBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      UpdateBukuById(dashboard.getIdBuku());
    }
  }

  public class AddFormBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      AddBuku(dashboard.getNamaBuku(), dashboard.getPenulisBuku(), dashboard.getIsbnBuku(), dashboard.getGenreBuku());
    }
  }

  public class RedirectFormBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      JPanel formBukuPanel = dashboard.getBukuFormPanel(new AddFormBuku());
      dashboard.getFrame().getContentPane().removeAll();
      dashboard.getFrame().setContentPane(formBukuPanel);
      dashboard.getFrame().revalidate();
      dashboard.getFrame().repaint();
    }
  }

  public class SearchBuku implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String keyword = dashboard.getSearchBuku();
      if (keyword.isEmpty()) {
        GetListBuku();
        return;
      } else {

        ArrayList<Buku> bukuList = SearchBukuArray(dashboard.getSearchBuku());
        JPanel bukuListPanel = dashboard.getBukuListPanel(bukuList, new RedirectEditBuku(), new SearchBuku());
        dashboard.addActionListenerSearchBuku(new SearchBuku());
        dashboard.getFrame().getContentPane().removeAll();
        dashboard.getFrame().setContentPane(bukuListPanel);
        dashboard.getFrame().revalidate();
        dashboard.getFrame().repaint();
        dashboard.setSearchBuku(keyword);

        return;
      }
    }
  }

  public class RedirectListPeminjaman implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      GetPeminjamanList();
    }
  }

  private Connection conn;

  Buku buku = new Buku(0, "", "", "", "");

  User user = new User("", "", "", "");

  Anggota anggota = new Anggota(0, "", "", "", 0, "");

  LoginUI loginUI = new LoginUI();

  Dashboard dashboard = new Dashboard();

  // Constructor

  public Controller() {
    GetMysqlConnection();

    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Mac OS X".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }

    loginUI.addActionListenerLoginButton(new LoginButton());
    dashboard.addActionListenerLogout(new LogoutUser());
    dashboard.addActionListenerProfileUser(new ProfileUser());
    dashboard.addActionListenerFormAnggota(new FormAnggota());
    dashboard.addListAnggotaListener(new ListAnggota());
    dashboard.addActionListenerListBuku(new RedirectListBuku());
    dashboard.addActionListenerFormBuku(new RedirectFormBuku());
    dashboard.addActionListenerListPeminjamanBuku(new RedirectListPeminjaman());
    loginUI.getFrame().setVisible(true);

  }

  // Database Connection

  public void GetMysqlConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_perpus", "root", "");
      Logger.getLogger(Controller.class.getName()).log(Level.INFO, "Connected to database");
    } catch (ClassNotFoundException | SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  // Action Listener

  public void LoginAction(String email, String password) {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery(
              "SELECT * FROM tb_karyawan WHERE email = '" + email + "' AND password =MD5( '" + password + "' )");
      if (rs.next()) {
        user = new User(rs.getString("email"), rs.getString("password"), rs.getString("nama"),
            rs.getString("no_karyawan"));

        loginUI.setEmail("");
        loginUI.setPassword("");
        loginUI.getFrame().dispose();
        loginUI.getFrame().setVisible(false);
        dashboard.setUserData(user);
        dashboard.getFrame().setVisible(true);
        JPanel dashboardGreet = dashboard.getDashboardPanel();
        dashboard.getFrame().getContentPane().removeAll();
        dashboard.getFrame().setContentPane(dashboardGreet);
        dashboard.getFrame().revalidate();
        dashboard.getFrame().repaint();

      } else {
        loginUI.setEmail("");
        loginUI.setPassword("");
      }
    } catch (SQLException e) {
      loginUI.getFrame().dispose();
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetProfile() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tb_karyawan WHERE email = '" + user.getEmail() + "'");
      if (rs.next()) {
        user = new User(rs.getString("email"), rs.getString("password"), rs.getString("nama"),
            rs.getString("no_karyawan"));
        JPanel profilePanel = dashboard.getProfilePanel(user, new LogoutUser());

        dashboard.getFrame().getContentPane().removeAll();
        dashboard.getFrame().setContentPane(profilePanel);
        dashboard.getFrame().revalidate();
        dashboard.getFrame().repaint();

      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetListAnggota() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tb_anggota");
      ArrayList<Anggota> anggotaList = new ArrayList<Anggota>();

      while (rs.next()) {
        String nim = rs.getString("nim");
        anggota = new Anggota(rs.getInt("id"), rs.getString("nama"), nim, rs.getString("jurusan"),
            rs.getInt("status"), rs.getString("email"));
        anggotaList.add(anggota);
      }

      JPanel anggotaListPanel = dashboard.getAnggotaListPanel(anggotaList, new RedirectEditAnggota());
      dashboard.getFrame().getContentPane().removeAll();
      dashboard.getFrame().setContentPane(anggotaListPanel);
      dashboard.getFrame().revalidate();
      dashboard.getFrame().repaint();

    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetListBuku() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tb_buku");
      ArrayList<Buku> bukuList = new ArrayList<Buku>();

      while (rs.next()) {
        buku = new Buku(rs.getInt("id"), rs.getString("nama_buku"), rs.getString("penulis"), rs.getString("isbn"),
            rs.getString("genre"));
        bukuList.add(buku);
      }

      JPanel bukuListPanel = dashboard.getBukuListPanel(bukuList, new RedirectEditBuku(), new SearchBuku());
      dashboard.addActionListenerSearchBuku(new SearchBuku());
      dashboard.getFrame().getContentPane().removeAll();
      dashboard.getFrame().setContentPane(bukuListPanel);
      dashboard.getFrame().revalidate();
      dashboard.getFrame().repaint();

    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetBukuById(int id) {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tb_buku WHERE id = '" + id + "'");
      if (rs.next()) {
        buku = new Buku(rs.getInt("id"), rs.getString("nama_buku"), rs.getString("penulis"), rs.getString("isbn"),
            rs.getString("genre"));

        JPanel formBukuPanel = dashboard.getBukuFormPanel(new EditFormBuku());
        dashboard.setNamaBuku(buku.getNama_buku());
        dashboard.setPenulisBuku(buku.getPenulis());
        dashboard.setIsbnBuku(buku.getIsbn());
        dashboard.setGenreBuku(buku.getGenre());
        dashboard.getFrame().getContentPane().removeAll();
        dashboard.getFrame().setContentPane(formBukuPanel);
        dashboard.getFrame().revalidate();
        dashboard.getFrame().repaint();

      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public ArrayList<Buku> SearchBukuArray(String keyword) {
    ArrayList<Buku> bukuList = new ArrayList<Buku>();
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt
          .executeQuery("SELECT * FROM tb_buku WHERE nama_buku LIKE '%" + keyword + "%' OR penulis LIKE '%" + keyword
              + "%' OR isbn LIKE '%" + keyword + "%' OR genre LIKE '%" + keyword + "%'");
      while (rs.next()) {
        buku = new Buku(rs.getInt("id"), rs.getString("nama_buku"), rs.getString("penulis"), rs.getString("isbn"),
            rs.getString("genre"));
        bukuList.add(buku);
      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
    return bukuList;
  }

  public void AddBuku(String nama_buku, String penulis, String isbn, String genre) {
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("INSERT INTO tb_buku (nama_buku, penulis, isbn, genre) VALUES ('" + nama_buku + "', '"
          + penulis + "', '" + isbn + "', '" + genre + "')");

      if (stmt.getUpdateCount() > 0) {
        GetListBuku();
      } else {
        JOptionPane.showMessageDialog(null, "Failed to add Buku", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void UpdateBukuById(int id) {
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("UPDATE tb_buku SET nama_buku = '" + dashboard.getNamaBuku() + "', penulis = '"
          + dashboard.getPenulisBuku() + "', isbn = '" + dashboard.getIsbnBuku() + "', genre = '"
          + dashboard.getGenreBuku() + "' WHERE id = '" + id + "'");

      if (stmt.getUpdateCount() > 0) {
        GetListBuku();
      } else {
        JOptionPane.showMessageDialog(null, "Failed to update Buku", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetAnggotaByEmail(String email) {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tb_anggota WHERE email = '" + email + "'");
      if (rs.next()) {
        anggota = new Anggota(rs.getInt("id"), rs.getString("nama"), rs.getString("nim"), rs.getString("jurusan"),
            rs.getInt("status"), rs.getString("email"));

        JPanel formAnggotaPanel = dashboard.getAnggotaFormPanel(new EditFormAnggota());
        dashboard.setNameAnggota(anggota.getNama());
        dashboard.setNimAnggota(new BigInteger(anggota.getNim()));
        dashboard.setJurusanAnggota(anggota.getJurusan());
        int status = 0;

        if (anggota.getStatus() == "Aktif") {
          status = 1;
        }

        dashboard.setStatusAnggota(status);
        dashboard.setEmailAnggota(anggota.getEmail());
        dashboard.getFrame().getContentPane().removeAll();
        dashboard.getFrame().setContentPane(formAnggotaPanel);
        dashboard.getFrame().revalidate();
        dashboard.getFrame().repaint();

      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void UpdateAnggotaByEmail(String email) {
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(
          "UPDATE tb_anggota SET nama = '" + dashboard.getNameAnggota() + "', nim = '" + dashboard.getNimAnggota()
              + "', jurusan = '" + dashboard.getJurusanAnggota() + "', status = '" + dashboard.getStatusAnggota()
              + "', email = '" + dashboard.getEmailAnggota() + "' WHERE email = '" + email + "'");

      if (stmt.getUpdateCount() > 0) {
        GetListAnggota();
      } else {
        JOptionPane.showMessageDialog(null, "Failed to update Anggota", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void AddAnggota(String nama, BigInteger nim, String jurusan, int status, String email) {
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("INSERT INTO tb_anggota (nama, nim, jurusan, status, email) VALUES ('" + nama + "', '" + nim
          + "', '" + jurusan + "', '" + status + "', '" + email + "')");

      if (stmt.getUpdateCount() > 0) {
        GetListAnggota();
      } else {
        JOptionPane.showMessageDialog(null, "Failed to add Anggota", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public void GetPeminjamanList() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(
          "SELECT tb_peminjaman.id, tb_anggota.nama AS nama_anggota, tb_buku.nama_buku AS nama_buku, tb_peminjaman.tanggal_pinjam, tb_peminjaman.tanggal_kembali, tb_peminjaman.durasi FROM tb_peminjaman INNER JOIN tb_anggota ON tb_peminjaman.id_anggota = tb_anggota.id INNER JOIN tb_buku ON tb_peminjaman.id_buku = tb_buku.id");
      ArrayList<Peminjaman> peminjamanList = new ArrayList<>();
      while (rs.next()) {
        Buku buku = new Buku(0, rs.getString("nama_buku"), "", "", "");
        Anggota anggota = new Anggota(0, rs.getString("nama_anggota"), "", "", 0, "");
        LocalDate tanggalPinjam = rs.getDate("tanggal_pinjam").toLocalDate();
        Date tanggalKembali = rs.getDate("tanggal_kembali");
        Peminjaman peminjaman = new Peminjaman(buku, anggota, tanggalPinjam,
            tanggalKembali, rs.getInt("durasi"));
        peminjamanList.add(peminjaman);
      }
      dashboard.getFrame().getContentPane().removeAll();
      dashboard.getFrame().setContentPane(dashboard.getPeminjamanList(peminjamanList, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("Hehe");
        }
      }));
      dashboard.getFrame().revalidate();
      dashboard.getFrame().repaint();

    } catch (SQLException e) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }

}
