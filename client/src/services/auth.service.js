import axios from 'axios';
import { server } from '../Demo/Resources/server-address';

const API_URL = `${server.address}/api/auth/`;
const URL = `${server.address}`;

class AuthService {
  async login(username, password) {
    const response = await axios.post(
      API_URL + 'signin',
      {
        username,
        password,
      },
      { withCredentials: true }
    );
    if (response.data.accessToken) {
      const body = {
        username: response.data.username,
        email: response.data.email,
        gen: response.data.gen,
      };
      sessionStorage.setItem('user', JSON.stringify(body));
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

  async getTokenFromCookie() {
    return await axios
      .get(URL + '/cookie/token', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
  }

  async getRolesFromCookie() {
    return await axios
      .get(URL + '/cookie/roles', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
  }

  async getIdFromCookie() {
    return await axios
      .get(URL + '/cookie/id', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
  }

  isAdmin() {
    return this.getRolesFromCookie().includes('ROLE_ADMIN');
  }

  isDirectorOrContabil() {
    const roles = this.gerRolesFromCookie();
    return roles.includes('ROLE_DIRECTOR') || roles.includes('ROLE_CONTABIL');
  }

  isAngajatSimplu() {
    const roles = this.gerRolesFromCookie();
    return !(
      roles.includes('ROLE_ADMIN') ||
      roles.includes('ROLE_DIRECTOR') ||
      roles.includes('ROLE_CONTABIL')
    );
  }

  setUser(user) {
    sessionStorage.setItem('user', JSON.stringify(user));
  }
}

export default new AuthService();
