import React from 'react';
import { Row, Col, Card, Form, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';

import { server } from '../Resources/server-address';

class AddUser extends React.Component {
  constructor(props) {
    super();
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);

    this.state = {
      username: '',
      password: '',
      nume: '',
      prenume: '',
      societateselectată: '',
      show: false,
      modalMessage: '', // text,
    };
  }

  clearFields() {
    this.setState({
      username: '',
      password: '',
      nume: '',
      prenume: '',
      societateselectată: '',
      show: false,
      modalMessage: '', // text,
    });
  }

  handleClose(e) {
    this.setState({
      show: false,
    });
  }

  async onSubmit(e) {
    try {
      e.preventDefault();
    } catch (err) {
      console.log(err);
    }

    // build user JSON for POST
    const user_body = {
      username: this.state.username,
      password: this.state.password,
      nume: this.state.nume,
      prenume: this.state.prenume,
      societateselectată: this.state.societateselectată,
    };
    // ADD User TO DATABASE
    await axios
      .post(`${server.address}/user`, { headers: authHeader(), body: JSON.stringify(user_body) })
      .then((user) => user.data)
      .then(() => {
        this.setState({
          show: true,
          message: 'User adăugat cu succes!',
        });
      })
      .then(this.clearFields());
  }

  render() {
    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Date incomplete</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.message}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Adaugă User</Card.Title>
              </Card.Header>
              <Card.Body>
                <h5>Datele Userului</h5>
                <hr />
                <Form onSubmit={this.onSubmit}>
                  <Row>
                    <Col md={6}>
                      <Row>
                        {/* <Form> */}
                        <Col md={6}>
                          <Form.Group controlId="username">
                            <Form.Label>Username</Form.Label>
                            <Form.Control
                              required
                              type="text"
                              placeholder="Username"
                              value={this.state.username}
                              onChange={(e) =>
                                this.setState({
                                  username: e.target.value,
                                })
                              }
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="password">
                            <Form.Label>Parolă</Form.Label>
                            <Form.Control
                              type="password"
                              placeholder="Parolă"
                              value={this.state.password}
                              onChange={(e) =>
                                this.setState({
                                  password: e.target.value,
                                })
                              }
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="nume">
                            <Form.Label>Nume</Form.Label>
                            <Form.Control
                              type="text"
                              placeholder="Nume"
                              value={this.state.nume}
                              onChange={(e) =>
                                this.setState({
                                  nume: e.target.value,
                                })
                              }
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="prenume">
                            <Form.Label>Prenume</Form.Label>
                            <Form.Control
                              type="text"
                              placeholder="Prenume"
                              value={this.state.prenume}
                              onChange={(e) =>
                                this.setState({
                                  prenume: e.target.value,
                                })
                              }
                            />
                          </Form.Group>
                        </Col>
                      </Row>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Button variant="outline-primary" type="submit">
                        Adaugă
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AddUser;
