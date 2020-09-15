package net.guides.springboot2.crud.model;


public class RealizariRetineri {

    private int nrtichete = 0;
    private int zileco = 0;
    private int zileconeplatit = 0;
    private int zilecm = 0;
    private int zilec  = 0;
    private int duratazilucru = 0; // contract.normalucru
    private int norma = 0; // nr zile lucratoare in luna
    private int orelucrate = 0;
    private float cas = 0;
    private float cass = 0;
    private float cam = 0;
    private float impozit = 0;
    private float valoaretichete = 0;
    private int totaldrepturi = 0;
    private int nrpersoaneintretinere = 0;
    
    public RealizariRetineri() { }

    public RealizariRetineri( int nrtichete, int zileco, int zilecm, int zileconeplatit, int duratazilucru, int norma, float cas, float cass, float cam, float impozit, float valoareTichete, int totalDrepturi, int nrpersoaneintretinere )
    {
        this.nrtichete = nrtichete;
        this.zileco = zileco;
        this.zileconeplatit = zileconeplatit;
        this.zilecm = zilecm;
        this.zilec  = zileco + zilecm;
        this.duratazilucru = duratazilucru;
        this.norma = norma;
        this.orelucrate = norma * duratazilucru;
        this.cas = cas;
        this.cass = cass;
        this.cam = cam;
        this.impozit = impozit;
        this.valoaretichete = valoareTichete;
        this.totaldrepturi = totalDrepturi;
        this.nrpersoaneintretinere = nrpersoaneintretinere;
    }

    //! GETTERS
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
    public int getZileconeplatit() {
      return zileconeplatit;
    }
    public int getDuratazilucru() {
      return duratazilucru;
    }
    public int getNorma() {
      return norma;
    }
    public int getOrelucrate() {
      return orelucrate;
    }
    public float getCam() {
      return cam;
    }
    public float getCas() {
      return cas;
    }
    public float getCass() {
      return cass;
    }
    public float getImpozit() {
      return impozit;
    }
    public float getValoaretichete() {
      return valoaretichete;
    }
    public int getTotaldrepturi() {
      return totaldrepturi;
    }
    public int getNrpersoaneintretinere() {
      return nrpersoaneintretinere;
    }

    //! SETTERS
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
    public void setZileconeplatit(int zileconeplatit) {
      this.zileconeplatit = zileconeplatit;
    }
    public void setDuratazilucru(int duratazilucru) {
      this.duratazilucru = duratazilucru;
    }
    public void setNorma(int norma) {
      this.norma = norma;
    }
    public void setOrelucrate(int orelucrate) {
      this.orelucrate = orelucrate;
    }
    public void setCam(float cam) {
      this.cam = cam;
    }
    public void setCas(float cas) {
      this.cas = cas;
    }
    public void setCass(float cass) {
      this.cass = cass;
    }
    public void setImpozit(float impozit) {
      this.impozit = impozit;
    }
    public void setValoaretichete(float valoaretichete) {
      this.valoaretichete = valoaretichete;
    }
    public void setTotaldrepturi(int totaldrepturi) {
      this.totaldrepturi = totaldrepturi;
    }
    public void setNrpersoaneintretinere(int nrpersoaneintretinere) {
      this.nrpersoaneintretinere = nrpersoaneintretinere;
    }
}

