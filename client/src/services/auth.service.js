import axios from 'axios';
import { server } from '../Demo/Resources/server-address';

const API_URL = `${server.address}/api/auth/`;

class AuthService {
  async login(username, password) {
    const response = await axios
			.post(API_URL + 'signin', {
				username,
				password,
			});
		if (response.data.accessToken) {
			localStorage.setItem('user', JSON.stringify(response.data));
		}
		return response.data;
  }

  logout() {
    localStorage.removeItem('user');
    sessionStorage.removeItem('socsel');
    sessionStorage.removeItem('angajatsel');
  }

  register(username, email, password, gen) {
    return axios.post(API_URL + 'signup', {
      username,
      email,
			password,
			gen,
    });
  }

  async changePassword(uid, reqPassword, newPassword) {
		var ok = false;
    ok = await axios
      .put(API_URL + `change-password/${uid}`, { password: reqPassword, newpassword: newPassword })
			.then((res) => res.status === 200)
			.catch(err => console.error("auth.service.js :: line: 38\n", err));
    return ok;
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }
}

export default new AuthService();
