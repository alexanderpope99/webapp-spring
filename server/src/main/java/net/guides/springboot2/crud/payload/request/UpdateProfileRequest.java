package net.guides.springboot2.crud.payload.request;

public class UpdateProfileRequest {
	private String email;

	private boolean gen;

	public String getEmail() {
		return email;
	}
	public boolean isGen() {
		return gen;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setGen(boolean gen) {
		this.gen = gen;
	}
}
