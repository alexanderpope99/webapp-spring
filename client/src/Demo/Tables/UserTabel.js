import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';
import DropdownMultiselect from 'react-multiselect-dropdown-bootstrap';

class UserTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addUser = this.addUser.bind(this);
    this.updateUser = this.updateUser.bind(this);
    this.editUser = this.editUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      users: [],
      userComponent: null,
      socsel: getSocSel(),
      logged: authService.getCurrentUser(),

      user: null,
      username: '',
      email: '',
      numeAngajat: '',
      roles: [], // [...id's as strings] selected in multiselect-dropdown
      societati: [], // [...id's as strings] selected in multiselect-dropdown

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      status: '',
      show: false,
      socs: [],

      // da
      all_roles: [],
      all_societati: [],
    };
  }

  clearInput() {
    this.setState({
      user: null,
      username: '',
      email: '',
      numeAngajat: '',
      roles: [],

      isEdit: false,
    });
  }

  async componentDidMount() {
    // get all societati
    var all_societati = await axios
      .get(`${server.address}/societate`, { headers: authHeader() })
      .then((res) =>
        res.data ? res.data.map((societate) => ({ key: societate.id, label: societate.nume })) : []
      )
      .catch((err) => console.error(err));
    console.log(all_societati);
    // get all roles
    var all_roles = await axios
      .get(`${server.address}/role`, { headers: authHeader() })
      .then((res) => (res.data ? res.data.map((role) => ({ key: role.id, label: role.name })) : []))
      .catch((err) => console.error(err));
    console.log(all_roles);

    this.setState({ all_roles: all_roles, all_societati: all_societati }, this.onRefresh);
    window.scrollTo(0, 0);
  }

  async addUser() {}

  async updateUser() {
    if (!this.state.roles || !this.state.user) return;

    var user = this.state.user;
    const user_roles = [
      ...this.state.roles.map((idrole) => ({
        id: Number(idrole),
        name: this.state.all_roles[Number(idrole) - 1].label,
      })),
    ];
    user.roles = user_roles;

    const iduri_societati_selectate = this.state.societati;
    var user_societati = {};
    // iterate throgh all_societati
    for (let soc of this.state.all_societati) {
      if (iduri_societati_selectate.indexOf(String(soc.key)) !== -1) {
        user_societati[String(soc.key)] = soc.label;
			}
    }
		user.societati = user_societati;
		console.log(user);		
    const ok = await axios
      .put(`${server.address}/user/${user.id}`, user, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));
    if (ok) {
      this.setState(
        {
          show: false,
          showConfirm: true,
          modalMessage: 'Utilizator actualizat',
        },
        this.onRefresh
      );
    }
  }

  async editUser(user) {
    console.log(user);
    var societati = [];
    for (let idsoc in user.societati) {
      societati.push(idsoc);
    }

    this.setState({
      isEdit: true,
      show: true,

      user: user,
      username: user.username,
      email: user.email,
      numeAngajat: user.angajati[0].persoana.nume + ' ' + user.angajati[0].persoana.prenume,
      roles: user.roles.map((role) => String(role.id)),
      societati: societati,
    });
  }

  async deleteUser(id) {
    await axios
      .delete(`${server.address}/user/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  async onRefresh() {
    const socsel = this.state.socsel;
    // get users
    const users = await axios
      .get(`${server.address}/user/ids=${socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    // render table
    if (users) {
      this.setState({ users: users }, this.renderUsers);
    }
  }

  // function to create react component with fetched data
  async renderUsers() {
    this.setState({
      usersComponent: this.state.users.map((user, index) => (
        <tr key={user.id}>
          <th>{user.username || '-'}</th>
          <th>{user.email || '-'}</th>
          <th>{user.angajati[0].persoana.nume + ' ' + user.angajati[0].persoana.prenume}</th>
          <th>
            <div className="d-flex">
              <Button
                variant="outline-secondary"
                className="ml-2 p-1 rounded-circle border-0"
                onClick={() => this.editUser(user)}
              >
                <Edit3 size={20} />
              </Button>
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      variant="outline-secondary"
                      className="m-0 p-1 rounded-circle border-0"
                      {...bindTrigger(popupState)}
                    >
                      <Trash2 size={20} />
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
                        <Typography>Sigur ștergeți userul {user.username}?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteSocietate(user.id);
                          }}
                          className="mt-2"
                        >
                          Da
                        </Button>
                        <Button
                          variant="outline-secondary"
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
            </div>
          </th>
        </tr>
      )),
    });
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateUser(this.state.id);
    else this.addUser();
  }

  async handleClose() {
    this.setState(
      {
        show: false,
      },
      this.clearInput
    );
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }

  render() {
    return (
      <Aux>
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addCerereConcediu}>
              <Row>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Username</Form.Label>
                  <Form.Control type="text" disabled value={this.state.username} />
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Email</Form.Label>
                  <Form.Control type="text" disabled value={this.state.email} />
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Nume angajat</Form.Label>
                  <Form.Control type="text" disabled value={this.state.numeAngajat} />
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Roluri/Permisiuni</Form.Label>
                  <DropdownMultiselect
                    // optionKey="id"
                    // optionLabel="name"
                    options={this.state.all_roles}
                    selected={this.state.roles}
                    handleOnChange={(selected) => this.setState({ roles: selected })}
                    name="roles"
                  />
                </Form.Group>
                <Form.Group as={Col} lg="12">
                  <Form.Label>Societati</Form.Label>
                  <DropdownMultiselect
                    // optionKey="id"
                    // optionLabel="name"
                    options={this.state.all_societati}
                    selected={this.state.societati}
                    handleOnChange={(selected) => this.setState({ societati: selected })}
                    name="societati"
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.onSubmit} type="submit">
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* confirm modal */}
        <Modal show={this.state.showConfirm} onHide={this.handleCloseConfirm}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleCloseConfirm}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Useri</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Username</th>
                      <th>Email</th>
                      <th>Nume angajat</th>
                      <th></th>
                      {/* <th>Role-uri</th>
                      <th>Societăți Acces</th>
                      <th>Societate</th>
                      <th>Persoană</th>
                      <th>Superior</th> */}
                    </tr>
                  </thead>
                  <tbody>{this.state.usersComponent}</tbody>
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
