import axios from 'axios';
import { server } from '../Demo/Resources/server-address';

const API_URL = `${server.address}/api/auth/`;
const URL = `${server.address}`;

class AuthService {

  constructor() {
    this.init();
    this.roles = [];
    this.user = null;

    console.log('auth.service constructed');
  }

  async init() {
    if (!this.token) {
      this.token = await this.getTokenFromCookie();
    }
  }

  getRoles() {
    return this.roles;
  }

  getToken() {
    return this.token;
  }

  async getCurrentUser() {
    const user = await axios.get(`${server.address}/user/from-cookie`, { withCredentials: true }).then(res => res.data).catch(err => console.error(err));
    this.user = user;
    this.token = user.accessToken;
    this.roles = user.roles;
    // console.log('user from cookies:', user);
    return user;
  }

  getCurrentUserSession() {
    return JSON.parse(sessionStorage.getItem('user'));
  }

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

      this.roles = response.data.roles;
      this.token = response.data.accessToken;
      this.user = {
        id: response.data.id,
        username: response.data.username,
        email: response.data.email,
        gen: response.data.gen,
      };

      // sessionStorage.setItem('user', JSON.stringify(response.data));
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
      .catch((err) => console.error(err));
    if (ok) {
      console.log('Updated profile!');
      let user = await this.getCurrentUser();
      user.gen = gen;
      user.email = email;
      // sessionStorage.setItem('user', JSON.stringify(user));
      window.location.reload();
    }
    return ok;
  }

  async getTokenFromCookie() {
    return await axios
      .get(URL + '/cookie/token', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
  }

  async getRolesFromCookie() {
    const roles = await axios
      .get(URL + '/cookie/roles', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
    console.log('roles:', roles);
    return roles;
  }

  async getIdFromCookie() {
    return await axios
      .get(URL + '/cookie/id', { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.log(err));
  }

  isAdmin() {
    console.log('isAdmin:', this.roles);
    return this.roles.includes('ROLE_ADMIN');
  }

  isDirectorOrContabil() {
    console.log('isDirectorOrContabil:', this.roles);
    return this.roles.includes('ROLE_DIRECTOR') || this.roles.includes('ROLE_CONTABIL');
  }

  isAngajatSimplu() {
    console.log('isAngajatSimplu:', this.roles);
    return !(
      this.roles.includes('ROLE_ADMIN') ||
      this.roles.includes('ROLE_DIRECTOR') ||
      this.roles.includes('ROLE_CONTABIL')
    );
  }
}

export default new AuthService();
