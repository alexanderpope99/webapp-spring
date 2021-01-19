import React from 'react';
import Aux from '../../hoc/_Aux';
import { Button, Toast } from 'react-bootstrap';
import { server } from '../Resources/server-address';

import axios from 'axios';
import authHeader from '../../services/auth-header';

class Hidden extends React.Component {
  constructor() {
    super();

    this.fixContracte = this.fixContracte.bind(this);
    this.getColor = this.getColor.bind(this);
    this.setColor = this.setColor.bind(this);

    this.state = {
      toastMessage: '',
      color: '',
      showToast: false,
    };
  }

  async fixContracte() {
    const ok = await axios
      .get(`${server.address}/contract/fix-missing-values`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (ok) {
      this.setState({
        toastMessage: 'Contractele au fost completate',
        showToast: true,
      });
    } else {
      this.setState({
        toastMessage: 'O valoare care nu poate fi predefinita lipseste. Aceasta poate fi  ',
        showToast: true,
      });
    }
  }

  // / => gets color, /all => gets all, /add => changes color to blue
  async getColor() {
    const color = await axios
      .get(`${server.address}/cookie`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (color) {
      this.setState({
        color: color,
      });
    }
  }
  async setColor() {
    const color = await axios
      .get(`${server.address}/cookie/add`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (color) {
      this.setState({
        color: color,
      });
    }
  }

  render() {
    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={5000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Verificat</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>

        <Button onClick={this.fixContracte}>
          Introdu date prestabilite in contracte acolo unde lipsesc
        </Button>
        <br />
        <Button onClick={this.getColor}>get color</Button>
        <Button onClick={this.setBlue}>set color to blue</Button>
        <Button onClick={this.getAllCookies}>get all cookies</Button>
        <p>{this.state.color}</p>
      </Aux>
    );
  }
}

export default Hidden;
