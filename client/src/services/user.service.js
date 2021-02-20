import axios from 'axios';
import authHeader from './auth-header';
import { server } from '../Demo/Resources/server-address';

const API_URL = `${server.address}/api/test/`;

class UserService {
  getPublicContent() {
    return axios.get(API_URL + 'all');
  }

  getUserBoard() {
    return axios.get(API_URL + 'user', { withCredentials: true });
  }

  getModeratorBoard() {
    return axios.get(API_URL + 'mod', { withCredentials: true });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'admin', { withCredentials: true });
  }
}

export default new UserService();
