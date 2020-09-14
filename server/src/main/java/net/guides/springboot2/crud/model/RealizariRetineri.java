package net.guides.springboot2.crud.model;


public class RealizariRetineri {

    private int nrtichete = 0;
    private int zileCO = 0;
    private int zileCM = 0;
    public RealizariRetineri() { }

    public RealizariRetineri( int nrtichete, int zileco, int zilecm ) {
        this.nrtichete = nrtichete;
        this.zileco = zileco;
        this.zilecm = zilecm;
    }

    // GETTERS
    public int getNrtichete() {
      return nrtichete;
    }
    public int getZilecm() {
      return zileCM;
    }
    public int getZileco() {
      return zileCO;
    }
    
    // SETTERS
    public void setNrtichete(int nrtichete) {
      this.nrtichete = nrtichete;
    }
    public void setZilecm(int zileCM) {
      this.zileCM = zileCM;
    }
    public void setZileco(int zileCO) {
      this.zileCO = zileCO;
    }
}

