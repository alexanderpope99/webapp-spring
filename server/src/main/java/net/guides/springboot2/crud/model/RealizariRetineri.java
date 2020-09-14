package net.guides.springboot2.crud.model;


public class RealizariRetineri {

    private int nrtichete = 0;
    private int zileco = 0;
    private int zilecm = 0;
    private int zilec  = 0;
    public RealizariRetineri() { }

    public RealizariRetineri( int nrtichete, int zileco, int zilecm ) {
        this.nrtichete = nrtichete;
        this.zileco = zileco;
        this.zilecm = zilecm;
        this.zilec  = zileco + zilecm;
    }

    // GETTERS
    public int getNrtichete() {
      return nrtichete;
    }
    public int getZilecm() {
      return zilecm;
    }
    public int getZileco() {
      return zileco;
    }
    public int getZilec() {
      return zilec;
    }
    
    // SETTERS
    public void setNrtichete(int nrtichete) {
      this.nrtichete = nrtichete;
    }
    public void setZilecm(int zilecm) {
      this.zilecm = zilecm;
    }
    public void setZileco(int zileco) {
      this.zileco = zileco;
    }
    public void setZilec(int zilec) {
      this.zilec = zilec;
    }
}

