import React from 'react';
import { Row, Col, Card, Form, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';

export default class Profile extends React.Component {
  4;
  /*
   *	user can change his email or password
   */
  constructor() {
    super();
    this.changeEdit = this.changeEdit.bind(this);
    this.getUserDetails = this.getUserDetails.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      id: null,
      username: '',
      email: '',
      roles: [],
      isEdit: false,
    };
  }

  componentDidMount() {
    this.getUserDetails();
  }

  getUserDetails() {
    const user = JSON.parse(localStorage.getItem('user'));
    this.setState({
      id: user.id,
      username: user.username,
      email: user.email,
      roles: user.roles,

      // modal:
      show: false,
			modalMessage: '',
			parolaActuala: '',
			parolaNoua: '',
			parolaNouaCheck: ''
    });
  }

  changeEdit() {
    this.setState({ isEdit: !this.state.isEdit });
  }
  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
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
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group>
                <Form.Label>Parola actuală</Form.Label>
                <Form.Control
                  type="password"
                  value={this.state.parolaActuala}
                  onChange={(e) => this.setState({ parolaActuala: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Parola actuală</Form.Label>
                <Form.Control
                  type="password"
                  value={this.state.parolaNoua}
                  onChange={(e) => this.setState({ parolaNoua: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Parola actuală</Form.Label>
                <Form.Control
                  type="password"
                  value={this.state.parolaNouaCheck}
                  onChange={(e) => this.setState({ parolaNouaCheck: e.target.value })}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              Schimbă
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          <Col>
            <Form onSubmit={this.onSubmit}>
              <Card>
                <Card.Header>
                  <Card.Title as="h5">Profil</Card.Title>
                  <Button
                    type="submit"
                    variant={this.state.isEdit ? 'primary' : 'link'}
                    className="float float-right m-0 p-1"
                  >
                    Schimbă parola
                  </Button>
                </Card.Header>
                <Card.Body>
                  <Row>
                    <Form.Group as={Col} md="6">
                      <Form.Label>Username</Form.Label>
                      <Form.Control
                        type="text"
                        value={this.state.username}
                        disabled={!this.state.isEdit}
                        onChange={(e) => this.setState({ username: e.target.value })}
                      />
                    </Form.Group>
                    <Form.Group as={Col} md="6">
                      <Form.Label>e-mail</Form.Label>
                      <Form.Control
                        type="text"
                        value={this.state.email}
                        disabled={!this.state.isEdit}
                        onChange={(e) => this.setState({ email: e.target.value })}
                      />
                    </Form.Group>
                  </Row>
                </Card.Body>
              </Card>
            </Form>
          </Col>
        </Row>
      </Aux>
    );
  }
}
