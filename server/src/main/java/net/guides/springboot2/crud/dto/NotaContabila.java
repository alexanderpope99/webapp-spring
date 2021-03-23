package net.guides.springboot2.crud.dto;

public class NotaContabila {
  private float cmFirma = 0f;
  private float cmFonduri = 0f;
  private float salariiDatorate = 0f;
  private float avans = 0f;
  private float cas25 = 0f;
  private float casCM = 0f;
  private float cass10 = 0f;
  private float impozit = 0f;
  private float impozitCM = 0f;
  private float contributieCAM = 0f;
  private float fonduriHandicap = 0f;


  public NotaContabila() {
  }

  public NotaContabila(float cmFirma, float cmFonduri, float salariiDatorate, float avans, float cas25, float casCM, float cass10, float impozit, float impozitCM, float contributieCAM, float fonduriHandicap) {
    this.cmFirma = cmFirma;
    this.cmFonduri = cmFonduri;
    this.salariiDatorate = salariiDatorate;
    this.avans = avans;
    this.cas25 = cas25;
    this.casCM = casCM;
    this.cass10 = cass10;
    this.impozit = impozit;
    this.impozitCM = impozitCM;
    this.contributieCAM = contributieCAM;
    this.fonduriHandicap = fonduriHandicap;
  }

  public float getCmFirma() {
    return this.cmFirma;
  }

  public void setCmFirma(float cmFirma) {
    this.cmFirma = cmFirma;
  }

  public float getCmFonduri() {
    return this.cmFonduri;
  }

  public void setCmFonduri(float cmFonduri) {
    this.cmFonduri = cmFonduri;
  }

  public float getSalariiDatorate() {
    return this.salariiDatorate;
  }

  public void setSalariiDatorate(float salariiDatorate) {
    this.salariiDatorate = salariiDatorate;
  }

  public float getAvans() {
    return this.avans;
  }

  public void setAvans(float avans) {
    this.avans = avans;
  }

  public float getCas25() {
    return this.cas25;
  }

  public void setCas25(float cas25) {
    this.cas25 = cas25;
  }

  public float getCasCM() {
    return this.casCM;
  }

  public void setCasCM(float casCM) {
    this.casCM = casCM;
  }

  public float getCass10() {
    return this.cass10;
  }

  public void setCass10(float cass10) {
    this.cass10 = cass10;
  }

  public float getImpozit() {
    return this.impozit;
  }

  public void setImpozit(float impozit) {
    this.impozit = impozit;
  }

  public float getImpozitCM() {
    return this.impozitCM;
  }

  public void setImpozitCM(float impozitCM) {
    this.impozitCM = impozitCM;
  }

  public float getContributieCAM() {
    return this.contributieCAM;
  }

  public void setContributieCAM(float contributieCAM) {
    this.contributieCAM = contributieCAM;
  }

  public float getFonduriHandicap() {
    return this.fonduriHandicap;
  }

  public void setFonduriHandicap(float fonduriHandicap) {
    this.fonduriHandicap = fonduriHandicap;
  }
}
