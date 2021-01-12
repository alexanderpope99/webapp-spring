import React from 'react';
import { Row, Col, Card, Form, Button, Modal, InputGroup } from 'react-bootstrap';
import { Eye, EyeOff } from 'react-feather';

import AuthService from '../../services/auth.service';
import Avatar1 from '../../assets/images/user/avatar-1.jpg';
import Avatar2 from '../../assets/images/user/avatar-2.jpg';

import Aux from '../../hoc/_Aux';

import authService from '../../services/auth.service';

export default class Profile extends React.Component {
  /*
   *	user can change his email or password
   * schimbare parola:
   * 	1) verifica parola actuala
   * 	2) verifica daca cele 2 parole se potrivesc
   * 	3) parolaActuala != parolaNoua
   * 	4) actualizeaza parola
   */
  constructor() {
    super();
    this.changeEdit = this.changeEdit.bind(this);
    this.getUserDetails = this.getUserDetails.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.schimbaParola = this.schimbaParola.bind(this);
    this.updateProfile = this.updateProfile.bind(this);

    this.state = {
      id: null,
      username: '',
      email: '',
      roles: [],
      gen: false,
      isEdit: false,

      // modal:
      show: false,
      parolaActuala: '',
      parolaNoua: '',
      parolaNouaCheck: '',
      validated: true,
    };
  }

  componentDidMount() {
    this.getUserDetails();
  }

  getUserDetails() {
    const user = authService.getCurrentUser();
    this.setState({
      id: user.id,
      username: user.username,
      email: user.email,
      roles: user.roles,
      gen: user.gen,

      // modal:
      show: false,
      parolaActuala: '',
      parolaNoua: '',
      parolaNouaCheck: '',
      validated: true,
      errorMessage: 'Parolele nu coincid',

      // modal hide/show password
      hideParolaActuala: true,
      hideParolaNoua: true,
      hideParolaNouaCheck: true,
    });
  }

  async updateProfile() {
    AuthService.updateProfile(this.state.id, this.state.email, this.state.gen);
  }

  changeEdit() {
    if (this.state.isEdit) {
      this.updateProfile();
    }
    this.setState({ isEdit: !this.state.isEdit });
  }

  handleClose() {
    this.setState({
      show: false,
      parolaActuala: '',
      parolaNoua: '',
      parolaNouaCheck: '',
      validated: true,
      errorMessage: 'Parolele nu coincid',
    });
  }

  passwordsMatch() {
    const res = this.state.parolaNoua === this.state.parolaNouaCheck;
    if (res) return res;

    return false;
  }

  getRoleName(role) {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'Admin';

      case 'ROLE_DIRECTOR':
        return 'Director';

      case 'ROLE_CONTABIL':
        return 'Contabil';

      case 'ROLE_ANGAJAT':
        return 'Angajat';

      default:
        return null;
    }
  }

  async schimbaParola(e) {
    e.preventDefault();
    const parolaActuala = this.state.parolaActuala;
    const parolaNoua = this.state.parolaNoua;

    if (!this.passwordsMatch() || !parolaActuala || parolaActuala === parolaNoua) {
      return;
    }

    if (parolaNoua.length < 6 || parolaNoua.length > 40) {
      this.setState({ errorMessage: 'Parola trebuie sa fie între 6 si 40 de caractere' });
      return;
    }

    // update password
    const ok = await AuthService.changePassword(this.state.id, parolaActuala, parolaNoua);

    if (!ok) {
      this.setState({ validated: false });
    } else {
      this.handleClose();
      alert('parola actualizata');
    }
  }

  onSubmit(e) {
    e.preventDefault();

    this.setState({
      show: true,
    });
  }

  render() {
    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Form noValidate onSubmit={this.schimbaParola}>
            <Modal.Header closeButton>
              <Modal.Title>Mesaj</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Form.Label>Parola actuală</Form.Label>
              <InputGroup>
                <Form.Control
                  required
                  type={this.state.hideParolaActuala ? 'password' : 'text'}
                  value={this.state.parolaActuala}
                  onChange={(e) =>
                    this.setState({ parolaActuala: e.target.value, validated: true })
                  }
                  className={this.state.validated ? 'form-control' : 'form-control is-invalid'}
                />
                <InputGroup.Append>
                  <Button
                    variant="link"
                    className="border"
                    checked={this.state.hideParolaActuala}
                    onClick={() =>
                      this.setState({ hideParolaActuala: !this.state.hideParolaActuala })
                    }
                  >
                    {this.state.hideParolaActuala ? <EyeOff size={18} /> : <Eye size={18} />}
                  </Button>
                </InputGroup.Append>
                <Form.Control.Feedback type="invalid">Parola incorectă</Form.Control.Feedback>
              </InputGroup>
              <Form.Label>Parola nouă | 6 - 40 caractere</Form.Label>
              <InputGroup>
                <Form.Control
                  required
                  type={this.state.hideParolaNoua ? 'password' : 'text'}
                  value={this.state.parolaNoua}
                  onChange={(e) => this.setState({ parolaNoua: e.target.value })}
                  className={
                    this.state.parolaNoua !== ''
                      ? this.state.parolaNoua !== this.state.parolaActuala
                        ? 'form-control'
                        : 'form-control is-invalid'
                      : 'form-control'
                  }
                />
                <InputGroup.Append>
                  <Button
                    variant="link"
                    className="border"
                    checked={this.state.hideParolaNoua}
                    onClick={() => this.setState({ hideParolaNoua: !this.state.hideParolaNoua })}
                  >
                    {this.state.hideParolaNoua ? <EyeOff size={18} /> : <Eye size={18} />}
                  </Button>
                </InputGroup.Append>
                <Form.Control.Feedback type="invalid">
                  Parola nouă trebuie să fie diferită de cea actuală
                </Form.Control.Feedback>
              </InputGroup>
              <Form.Label>Rescrie parola nouă</Form.Label>
              <InputGroup>
                <Form.Control
                  required
                  type={this.state.hideParolaNouaCheck ? 'password' : 'text'}
                  value={this.state.parolaNouaCheck}
                  onChange={(e) => this.setState({ parolaNouaCheck: e.target.value })}
                  className={
                    this.state.errorMessage === 'Parola trebuie sa fie între 6 si 40 de caractere'
                      ? 'form-control is-invalid'
                      : this.passwordsMatch()
                      ? 'form-control'
                      : 'form-control is-invalid'
                  }
                />
                <InputGroup.Append>
                  <Button
                    variant="link"
                    className="border"
                    checked={this.state.hideParolaNouaCheck}
                    onClick={() =>
                      this.setState({ hideParolaNouaCheck: !this.state.hideParolaNouaCheck })
                    }
                  >
                    {this.state.hideParolaNouaCheck ? <EyeOff size={18} /> : <Eye size={18} />}
                  </Button>
                </InputGroup.Append>
                <Form.Control.Feedback type="invalid">
                  {this.state.errorMessage}
                </Form.Control.Feedback>
              </InputGroup>
            </Modal.Body>
            <Modal.Footer>
              <Button variant="primary" type="submit">
                Schimbă
              </Button>
            </Modal.Footer>
          </Form>
        </Modal>

        <Row>
          <Col>
            <Form onSubmit={this.onSubmit}>
              <Card>
                <Card.Header>
                  <Card.Title as="h5">Profil</Card.Title>
                  <Button type="submit" variant="link" className="float float-right m-0 p-1">
                    Schimbă parola
                  </Button>
                </Card.Header>
                <Card.Body className="pl-5 pr-5">
                  <Col md={12}>
                    <Form.Group as={Row}>
                      <Form.Label column sm="4">
                        Username
                      </Form.Label>
                      <Col sm={8}>
                        <Form.Control
                          type="text"
                          value={this.state.username}
                          disabled
                          onChange={(e) => this.setState({ username: e.target.value })}
                        />
                      </Col>
                    </Form.Group>
                    <Form.Group as={Row}>
                      <Form.Label column sm="4">
                        e-mail
                      </Form.Label>
                      <Col sm={8}>
                        <Form.Control
                          type="text"
                          value={this.state.email}
                          disabled={!this.state.isEdit}
                          onChange={(e) => this.setState({ email: e.target.value })}
                        />
                      </Col>
                    </Form.Group>
                    <Form.Group as={Row}>
                      <Form.Label as="legend" column sm={5}>
                        Icon
                      </Form.Label>
                      <Col sm={7}>
                        <Row>
                          <Form.Check
                            custom
                            type="radio"
                            disabled={!this.state.isEdit}
                            checked={!this.state.gen}
                            onChange={() => this.setState({ gen: !this.state.gen })}
                            label={
                              <img className="img-radius" src={Avatar1} alt="Generic placeholder" />
                            }
                            name="icons"
                            id="icon1"
                          />
                          <Form.Check
                            custom
                            type="radio"
                            disabled={!this.state.isEdit}
                            checked={this.state.gen}
                            onChange={() => this.setState({ gen: !this.state.gen })}
                            label={
                              <img className="img-radius" src={Avatar2} alt="Generic placeholder" />
                            }
                            name="icons"
                            id="icon2"
                          />
                        </Row>
                      </Col>
                    </Form.Group>
                    <Button
                      variant={this.state.isEdit ? 'success' : 'link'}
                      className="p-2 pl-3 pr-3"
                      onClick={this.changeEdit}
                    >
                      {this.state.isEdit ? 'Salvează' : 'Editează'}
                    </Button>
                  </Col>
                </Card.Body>
              </Card>
            </Form>
          </Col>
        </Row>
      </Aux>
    );
  }
}
