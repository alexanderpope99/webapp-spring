import React from 'react';
import { Row, Col, Card, Form, Button } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';

export default class Profile extends React.Component {
  constructor() {
    super();
    this.changeEdit = this.changeEdit.bind(this);
		this.getUserDetails = this.getUserDetails.bind(this);
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
    console.log(user);
    this.setState({
      id: user.id,
      username: user.username,
      email: user.email,
      roles: user.roles,
    });
  }

  changeEdit() {
    this.setState({ isEdit: !this.state.isEdit });
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
		
		// if(this.state.isEdit)
		this.changeEdit();

  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Form onSubmit={this.onSubmit}>
              <Card>
                <Card.Header>
                  <Card.Title as="h5">Profil</Card.Title>
                  <Button
										type="submit"
                    // onClick={this.onSubmit}
                    variant={this.state.isEdit ? 'primary' : 'link'}
                    className="float float-right m-0 p-1"
                  >
                    {this.state.isEdit ? 'Salvează' : 'Editează'}
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
                      <Form.Label>email</Form.Label>
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
