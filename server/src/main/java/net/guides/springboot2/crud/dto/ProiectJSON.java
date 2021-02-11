package net.guides.springboot2.crud.dto;

public class ProiectJSON {
  private int id;
  private String nume;

  public ProiectJSON() {}

  public ProiectJSON(int id, String nume) {
    this.id = id;
    this.nume=nume;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNume() {
	  return nume;
  }

  public void setNume(String nume) {
	  this.nume = nume;
  }
}