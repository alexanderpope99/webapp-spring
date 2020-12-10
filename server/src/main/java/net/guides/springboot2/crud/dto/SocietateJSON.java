package net.guides.springboot2.crud.dto;

public class SocietateJSON {
  private int id;
  private String nume;

  public SocietateJSON() {}

  public SocietateJSON(int id, String nume) {
    this.id = id;
    this.nume=nume;
  }

  public int getId() {
    return id;
  }
  public String getNume() {
    return nume;
  }

  public void setId(int id) {
    this.id = id;
  }
  public void setNume(String nume) {
    this.nume = nume;
  }
}