package net.guides.springboot2.crud.model;


public class RealizariRetineri {

    private int nrTichete = 0;

    public RealizariRetineri() { }

    public RealizariRetineri( int nrTichete ) {
        this.nrTichete = nrTichete;
    }

    // GETTERS
    public int getNrtichete() {
      return nrTichete;
    }
    
    // SETTERS
    public void setNrtichete(int nrTichete) {
      this.nrTichete = nrTichete;
    }
}

