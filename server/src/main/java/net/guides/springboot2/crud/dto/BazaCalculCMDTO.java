package net.guides.springboot2.crud.dto;

public class BazaCalculCMDTO {
	float bazaCalcul = 0;
	int zileBazaCalcul = 0;
	float medieZilnica = 0;

	public BazaCalculCMDTO(float bc, int zbc, float mz) {
		this.bazaCalcul = bc;
		this.zileBazaCalcul = zbc;
		this.medieZilnica = mz;
	}

	public float getBazacalcul() {
		return bazaCalcul;
	}
	public float getMediezilnica() {
		return medieZilnica;
	}
	public int getZilebazacalcul() {
		return zileBazaCalcul;
	}

	public void setBazacalcul(float bazaCalcul) {
		this.bazaCalcul = bazaCalcul;
	}
	public void setMediezilnica(float medieZilnica) {
		this.medieZilnica = medieZilnica;
	}
	public void setZilebazacalcul(int zileBazaCalcul) {
		this.zileBazaCalcul = zileBazaCalcul;
	}
}
