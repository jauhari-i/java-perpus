package model;

import java.io.Serializable;

public class User implements Serializable {
  private String email;
  private String password;
  private String name;
  private String noKaryawan;

  public User() {

  }

  public User(String email, String password, String name, String noKaryawan) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.noKaryawan = noKaryawan;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPassword() {
    return this.password;
  }

  public String getName() {
    return this.name;
  }

  public String getNoKaryawan() {
    return this.noKaryawan;
  }

  public User getUser() {
    return this;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
