import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Edit from '@material-ui/icons/Edit';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';

class UserTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addUser = this.addUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.editUser = this.editUser.bind(this);
    this.updateUser = this.updateUser.bind(this);

    this.state = {
      users: [],
      userComponent: null,

      // add modal:
      id: '',
      show: false,
      username: '',
      password: '',
      passwordConfirm: '',
      nume: '',
      prenume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
      isEdit: false,
    };
  }

  componentDidMount() {
    this.fillTable();
  }

  async fillTable() {
    if (typeof this.state.users === 'undefined') return;
    //? fetch
    const users = await fetch(`${server.address}/user/`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((res) => (res.status !== 200 ? null : res.json()))
      .catch((err) => console.error('err', err));

    if (users !== null) {
      this.setState(
        {
          users: users,
        },
        this.renderUser
      );
    } else {
      this.setState(
        {
          users: [],
        },
        this.renderUser
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      id: '',
      show: false,
      username: '',
      password: '',
      passwordConfirm: '',
      nume: '',
      prenume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    });
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        // reset data
        id: '',
        username: '',
        password: '',
        passwordConfirm: '',
        nume: '',
        prenume: '',
        isEdit: false,
      });
  }

  async deleteUser(id) {
    await fetch(`${server.address}/user/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async editUser(user) {
    if (typeof this.state.users === 'undefined') return;

    this.setState({
      id: user.id,
      username: user.username,
      password: user.password,
      passwordConfirm: user.password,
      nume: user.nume,
      prenume: user.prenume,
      show: true,
      isEdit: true,
    });
  }

  async addUser() {
    if (typeof this.state.users === 'undefined') return;
    if (this.state.password !== this.state.passwordConfirm) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Parolele nu corespund',
      });
      return;
    }
    const user_body = {
      username: this.state.username,
      password: this.state.password,
      nume: this.state.nume,
      prenume: this.state.prenume,
      societateselectată: this.state.societateselectată,
    };

    let ok = await fetch(`${server.address}/user`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'User adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  async updateUser() {
    const user_body = {
      id: this.state.id,
      username: this.state.username,
      password: this.state.password,
      nume: this.state.nume,
      prenume: this.state.prenume,
      societateselectată: this.state.societateselectată,
    };

    let ok = await fetch(`${server.address}/user/${this.state.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'User actualizat ✔',
      });
      this.fillTable();
    }
  }

  // function to create react component with fetched data
  renderUser() {
    this.setState({
      userComponent: this.state.users.map((user, index) => {
        for (let key in user) {
          if (user[key] === 'null' || user[key] === null) user[key] = '-';
        }
        return (
          <tr key={user.id}>
            <th>{user.id}</th>
            <th>{user.username}</th>
            <th>{user.nume}</th>
            <th>{user.prenume}</th>
            <th className="d-inline-flex flex-row justify-content-around">
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      onClick={() => this.editUser(user)}
                      variant="outline-secondary"
                      className="ml-2 p-1 rounded-circle border-0"
                    >
                      <Edit fontSize="small" />
                    </Button>
                    <Button
                      variant="outline-secondary"
                      className="m-0 p-1 rounded-circle border-0"
                      {...bindTrigger(popupState)}
                    >
                      <DeleteIcon fontSize="small" />
                    </Button>
                    <Popover
                      {...bindPopover(popupState)}
                      anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'center',
                      }}
                      transformOrigin={{
                        vertical: 'top',
                        horizontal: 'center',
                      }}
                    >
                      <Box p={2}>
                        <Typography>Sigur ștergeți userul?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteUser(user.id);
                          }}
                          className="mt-2 "
                        >
                          Da
                        </Button>
                        <Button
                          variant="outline-persondary"
                          onClick={popupState.close}
                          className="mt-2"
                        >
                          Nu
                        </Button>
                      </Box>
                    </Popover>
                  </div>
                )}
              </PopupState>
            </th>
          </tr>
        );
      }),
    });
  }

  render() {
    return (
      <Aux>
        {/* // ADD MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>User</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="username">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.username}
                  onChange={(e) => {
                    this.setState({ username: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="password">
                <Form.Label>Parolă</Form.Label>
                <Form.Control
                  required
                  type="password"
                  value={this.state.password}
                  onChange={(e) => {
                    this.setState({ password: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="passwordConfirm">
                <Form.Label>Confirmare Parolă</Form.Label>
                <Form.Control
                  required
                  type="password"
                  value={this.state.passwordConfirm}
                  onChange={(e) => {
                    this.setState({ passwordConfirm: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="nume">
                <Form.Label>Nume</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.nume}
                  onChange={(e) => {
                    this.setState({ nume: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="prenume">
                <Form.Label>Preume</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.prenume}
                  onChange={(e) => {
                    this.setState({ prenume: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.state.isEdit ? this.updateUser : this.addUser}>
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CONFIRM Modal */}
        <Modal show={this.state.show_confirm} onHide={() => this.handleClose(true)}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Listă useri</Card.Title>
                <Button
                  variant="outline-primary"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.fillTable}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  variant="outline-primary"
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
                >
                  +
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Username</th>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.userComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default UserTabel;
