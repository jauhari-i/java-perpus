package model;

public class Anggota {
  private int id;
  private String nama;
  private String nim;
  private String jurusan;
  private int status;
  private String email;

  public Anggota(int id, String nama, String nim, String jurusan, int status, String email) {
    this.id = id;
    this.nama = nama;
    this.nim = nim;
    this.jurusan = jurusan;
    this.status = status;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNama() {
    return nama;
  }

  public void setNama(String nama) {
    this.nama = nama;
  }

  public String getNim() {
    return nim;
  }

  public void setNim(String nim) {
    this.nim = nim;
  }

  public String getJurusan() {
    return jurusan;
  }

  public void setJurusan(String jurusan) {
    this.jurusan = jurusan;
  }

  public String getStatus() {
    if (status == 1) {
      return "Aktif";
    } else {
      return "Tidak Aktif";
    }
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
