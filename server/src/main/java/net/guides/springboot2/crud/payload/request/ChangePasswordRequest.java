package net.guides.springboot2.crud.payload.request;

import javax.validation.constraints.*;

public class ChangePasswordRequest {
    @NotBlank
    @Size(min = 6, max = 40)
		private String password;
		
		@NotBlank
		@Size(min = 6, max = 40)
		private String newpassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
}
