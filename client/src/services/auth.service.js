import axios from 'axios';
import { server } from '../Demo/Resources/server-address';

const API_URL = `${server.address}/api/auth/`;

class AuthService {
  async login(username, password) {
    const response = await axios.post(API_URL + 'signin', {
      username,
      password,
    });
    if (response.data.accessToken) {
      sessionStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  }

  logout() {
    sessionStorage.removeItem('user');
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
      .catch((err) => console.error('auth.service.js :: line: 38\n', err));
    return ok;
  }

  async updateProfile(uid, email, gen) {
    var ok = false;
    ok = await axios
      .put(API_URL + `update-profile/${uid}`, { email: email, gen: gen })
      .then((res) => res.status === 200)
      .catch((err) => console.error('auth.service.js :: line: 44\n', err));
    if (ok) {
      console.log('Updated profile!');
      let user = JSON.parse(sessionStorage.getItem('user'));
      user.gen = gen;
      user.email = email;
      sessionStorage.setItem('user', JSON.stringify(user));
      window.location.reload();
    }
    return ok;
  }

  getCurrentUser() {
    return JSON.parse(sessionStorage.getItem('user'));
	}
	
	isAdmin() {
		const user = this.getCurrentUser();
		return user.roles.includes('ROLE_ADMIN');
	}

  isDirectorOrContabil() {
    const user = this.getCurrentUser();
    return user.roles.includes('ROLE_DIRECTOR') || user.roles.includes('ROLE_CONTABIL');
  }

  isAngajatSimplu() {
    const user = this.getCurrentUser();
    return !(
      user.roles.includes('ROLE_ADMIN') ||
      user.roles.includes('ROLE_DIRECTOR') ||
      user.roles.includes('ROLE_CONTABIL')
    );
  }

  setUser(user) {
    sessionStorage.setItem('user', JSON.stringify(user));
  }
}

export default new AuthService();
