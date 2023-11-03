package model;

import java.sql.Date;
import java.time.LocalDate;

public class Peminjaman {
  private Buku buku;
  private Anggota anggota;
  private LocalDate tanggalPinjam;
  private Date tanggalKembali;
  private int durasi;

  public Peminjaman(Buku buku, Anggota anggota, LocalDate tanggalPinjam, Date tanggalKembali, int durasi) {
    this.buku = buku;
    this.anggota = anggota;
    this.tanggalPinjam = tanggalPinjam;
    this.tanggalKembali = tanggalKembali;
    this.durasi = durasi;
  }

  public Buku getBuku() {
    return buku;
  }

  public void setBuku(Buku buku) {
    this.buku = buku;
  }

  public Anggota getAnggota() {
    return anggota;
  }

  public void setAnggota(Anggota anggota) {
    this.anggota = anggota;
  }

  public String getStatusPeminjaman() {
    if (tanggalKembali == null) {
      return "Belum Kembali";
    } else {
      return "Sudah Kembali";
    }
  }

  public Boolean isTerlambat() {
    if (tanggalKembali == null) {
      return false;
    } else {
      LocalDate localTanggalKembali = tanggalKembali.toLocalDate();
      return localTanggalKembali.isAfter(tanggalPinjam.plusDays(durasi));
    }
  }

  public String getStatusKeterlambatan() {
    if (isTerlambat()) {
      int totalHariTerlambat = tanggalKembali.toLocalDate().getDayOfYear() - tanggalPinjam.getDayOfYear() - durasi;
      return "Terlambat " + totalHariTerlambat + " hari";
    } else {
      return "-";
    }
  }

  public int getDurasi() {
    return durasi;
  }

  public void setDurasi(int durasi) {
    this.durasi = durasi;
  }

  public LocalDate getTanggalPinjam() {
    return tanggalPinjam;
  }

  public void setTanggalPinjam(LocalDate tanggalPinjam) {
    this.tanggalPinjam = tanggalPinjam;
  }

  public LocalDate getTanggalKembali() {
    if (tanggalKembali == null) {
      return null;
    } else {
      return tanggalKembali.toLocalDate();
    }
  }

  public void setTanggalKembali(Date tanggalKembali) {
    this.tanggalKembali = tanggalKembali;
  }
}
