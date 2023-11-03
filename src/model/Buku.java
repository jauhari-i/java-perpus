package model;

public class Buku {
  private int id;
  private String nama_buku;
  private String penulis;
  private String isbn;
  private String genre;

  public Buku(int id, String nama_buku, String penulis, String isbn, String genre) {
    this.id = id;
    this.nama_buku = nama_buku;
    this.penulis = penulis;
    this.isbn = isbn;
    this.genre = genre;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNama_buku() {
    return nama_buku;
  }

  public void setNama_buku(String nama_buku) {
    this.nama_buku = nama_buku;
  }

  public String getPenulis() {
    return penulis;
  }

  public void setPenulis(String penulis) {
    this.penulis = penulis;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }
}
