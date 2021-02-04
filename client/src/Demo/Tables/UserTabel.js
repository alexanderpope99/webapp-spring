/* eslint-disable eqeqeq */
import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Breadcrumb, Toast } from 'react-bootstrap';
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
    this.onChangeAngajat = this.onChangeAngajat.bind(this);
    this.addUser = this.addUser.bind(this);
    this.updateUser = this.updateUser.bind(this);
    this.editUser = this.editUser.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.folosesteDateleAngajatului = this.folosesteDateleAngajatului.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      users: [],
      userComponent: null,
      socsel: getSocSel(),
      logged: authService.getCurrentUser(),

      user: null,
      username: '',
      email: '',
      gen: false,
      idAngajat: '',
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

      // for select/multiselect components
      all_roles: [], // array of {key: role.id, label: role.name}  object - used in multiselect
      all_societati: [], // array of {key: soc.id, label: soc.nume} objects - used in multiselect
      all_angajati_of_socsel: [], // array of Angajat - used in ?
      all_angajati_of_socsel_nouser: [], // array of Angajat with User=null- user in select

      showToast: false,
      toastMessage: '',
    };
  }

  clearInput() {
    this.setState({
      user: null,
      username: '',
      email: '',
      gen: false,
      idAngajat: '',
      numeAngajat: '',
      roles: [],
      societati: [],

      isEdit: false,
    });
  }

  async getAngajatiFaraUser() {
    return await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}&nu`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua angajații fără user ' + err.response.data.message,
        })
      );
  }

  async init() {
    // get all societati
    var all_societati = await axios
      .get(`${server.address}/societate`, { headers: authHeader() })
      .then((res) =>
        res.data ? res.data.map((societate) => ({ key: societate.id, label: societate.nume })) : []
      )
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua societățile ' + err.response.data.message,
        })
      );
    // get all roles
    var all_roles = await axios
      .get(`${server.address}/role`, { headers: authHeader() })
      .then((res) => (res.data ? res.data.map((role) => ({ key: role.id, label: role.name })) : []))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua role-urile ' + err.response.data.message,
        })
      );

    var all_angajati_of_socsel = [],
      all_angajati_of_socsel_nouser = [];
    if (this.state.socsel) {
      all_angajati_of_socsel = await axios
        .get(`${server.address}/angajat/ids=${this.state.socsel.id}`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua angajații ' + err.response.data.message,
          })
        );

      all_angajati_of_socsel_nouser = await this.getAngajatiFaraUser();
    }

    this.setState(
      {
        all_roles: all_roles,
        all_societati: all_societati,
        all_angajati_of_socsel: all_angajati_of_socsel,
        all_angajati_of_socsel_nouser: all_angajati_of_socsel_nouser,
      },
      this.onRefresh
    );
    window.scrollTo(0, 0);
  }

  componentDidMount() {
    // if (!getSocSel()) window.location.href = '/dashboard/societati';
    this.init();
  }

  onChangeAngajat(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idangajat = e.target.options[selectedIndex].getAttribute('data-key');
    this.setState({
      idAngajat: idangajat,
      numeAngajat: e.target.value,
    });
  }

  async submitUser(user, method) {
    if (method === 'put') {
      const endpoint = this.state.socsel
        ? `${server.address}/user/${user.id}/ids=${this.state.socsel.id}`
        : `${server.address}/user/${user.id}`;
      const ok = await axios
        .put(endpoint, user, {
          headers: authHeader(),
        })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut actualiza user-ul ' + err.response.data.message,
          })
        );
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
    } else if (method === 'post') {
      const ok = await axios
        .post(`${server.address}/user`, user, { headers: authHeader() })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut creea user-ul ' + err.response.data.message,
          })
        );
      if (ok) {
        this.setState(
          {
            show: false,
            showConfirm: true,
            modalMessage: 'Utilizator adăugat',
          },
          this.onRefresh
        );
      }
    }
  }

  async addUser() {
    if (
      this.state.roles.length === 0 ||
      this.state.societati.length === 0 ||
      !this.state.username ||
      !this.state.email
    )
      return;

    // rebuild user.roles
    const user_roles = [
      ...this.state.roles.map((idrole) => ({
        id: Number(idrole),
        name: this.state.all_roles.find((role) => role.key == idrole).label,
      })),
    ];

    // rebuild user.societati
    const user_societati = [
      ...this.state.societati.map((idsoc) => ({
        id: Number(idsoc),
        nume: this.state.all_societati.find((soc) => soc.key == idsoc).label,
      })),
    ];

    // angajat
    var angajat = null;
    if (this.state.idAngajat) {
      angajat = this.state.all_angajati_of_socsel.find(
        (ang) => ang.idpersoana == this.state.idAngajat
      );
    }

    // build user
    const user = {
      username: this.state.username,
      email: this.state.email,
      gen: this.state.gen,
      angajati: [angajat],
      roles: user_roles,
      societati: user_societati,
    };

    this.submitUser(user, 'post');
  }

  async updateUser() {
    if (!this.state.roles || !this.state.societati || !this.state.user) return;

    var user = this.state.user;
    const user_roles = [
      ...this.state.roles.map((idrole) => ({
        id: Number(idrole),
        name: this.state.all_roles.find((role) => role.key == idrole).label,
      })),
    ];
    user.roles = user_roles;

    // rebuild user.societati as it was recieved | for compatibility convenience
    const user_societati = [
      ...this.state.societati.map((idsoc) => ({
        id: Number(idsoc),
        nume: this.state.all_societati.find((soc) => soc.key == idsoc).label,
      })),
    ];
    user.societati = user_societati;

    // filter user.angajati to not contain angajat from this socsel
    user.angajati = user.angajati.filter((ang) => ang.societate.id !== this.state.socsel.id);

    // push selected angajat in user.angajati only if it doesn't exist
    // if another angajat exists and has same idsocietate, replace it
    if (this.state.idAngajat) {
      let angajat = this.state.all_angajati_of_socsel.find(
        (ang) => ang.idpersoana == this.state.idAngajat
      );
      if (Array.isArray(user.angajati)) user.angajati.push(angajat);
      else user.angajati = [angajat];
    }
    user.gen = this.state.gen;

    // console.log(user);
    this.submitUser(user, 'put');
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateUser();
    else this.addUser();
  }

  async editUser(user) {
    // filter user.angajati to only keep the angajat from socsel
    let angajat = user.angajati.find((ang) => ang.societate.id === this.state.socsel.id);
    // let angajati = user.angajati.filter((angajat) => console.log(angajat));

    // console.log(user);
    this.setState({
      isEdit: true,
      show: true,

      user: user,
      username: user.username,
      email: user.email,
      gen: user.gen,
      idAngajat: angajat ? angajat.idpersoana : '',
      numeAngajat: angajat ? angajat.persoana.nume + ' ' + angajat.persoana.prenume : '',
      roles: user.roles.map((role) => String(role.id)),
      societati: user.societati.map((soc) => String(soc.id)),
    });
  }

  async deleteUser(id) {
    await axios
      .delete(`${server.address}/user/${id}`, { headers: authHeader() })
      .then(this.onRefresh)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge user-ul ' + err.response.data.message,
        })
      );
  }

  async onRefresh() {
    const socsel = this.state.socsel;
    // get users
    if (socsel) {
      const users = await axios
        .get(`${server.address}/user/ids=${socsel.id}`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua userii ' + err.response.data.message,
          })
        );
      const all_angajati_of_socsel_nouser = await this.getAngajatiFaraUser();
      // render table
      if (users) {
        this.setState(
          {
            users: users,
            all_angajati_of_socsel_nouser: all_angajati_of_socsel_nouser,
          },
          this.renderUsers
        );
      }
    } else {
      const users = await axios
        .get(`${server.address}/user/nos`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua userii fără societate ' + err.response.data.message,
          })
        );
      if (users) {
        this.setState(
          {
            users: users,
            all_angajati_of_socsel_nouser: [],
          },
          this.renderUsers
        );
      }
    }
  }

  // function to create react component with fetched data
  async renderUsers() {
    this.setState({
      usersComponent: this.state.users.map((user, index) => {
        user.angajati = user.angajati.filter((ang) => ang.societate.id === this.state.socsel.id);
        return (
          <tr key={user.id}>
            <th>{user.username || '-'}</th>
            <th>{user.email || '-'}</th>
            <th>
              {user.angajati[0]
                ? user.angajati[0].persoana.nume + ' ' + user.angajati[0].persoana.prenume
                : 'lipsă angajat asociat'}
            </th>
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
                              this.deleteUser(user.id);
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
        );
      }),
    });
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
    this.setState(
      {
        modalMessage: '',
        showConfirm: false,
      },
      this.clearInput
    );
  }

  scoateDiacritice(str) {
    str = str.replace(/[ăâĂÂ]/, 'a');
    str = str.replace(/[îÎ]/, 'i');
    str = str.replace(/[șȘ]/, 's');
    str = str.replace(/[țȚ]/, 't');
    return str;
  }

  folosesteDateleAngajatului() {
    const idAngajatSel = Number(this.state.idAngajat);
    if (!idAngajatSel) return;

    const angajatSel = this.state.all_angajati_of_socsel_nouser.find(
      (angajat) => angajat.idpersoana === idAngajatSel
    );
    const username = (
      this.scoateDiacritice(angajatSel.persoana.nume) +
      '.' +
      this.scoateDiacritice(angajatSel.persoana.prenume.split(/-| /)[0])
    ).toLowerCase();
    const email = angajatSel.persoana.email || '';
    this.setState({
      username: username,
      email: email,
    });
  }

  render() {
    var angajati = [];
    if (this.state.all_angajati_of_socsel_nouser.length > 0) {
      if (this.state.isEdit && this.state.user.angajati.length > 0) {
        let angajatSel = this.state.all_angajati_of_socsel.find(
          (angajat) => angajat.idpersoana === Number(this.state.user.angajati[0].idpersoana)
        );
        angajati.unshift(
          <option key={angajatSel.persoana.id} data-key={angajatSel.persoana.id}>
            {angajatSel.persoana.nume + ' ' + angajatSel.persoana.prenume}
          </option>
        );
      }

      this.state.all_angajati_of_socsel_nouser.map((angajat) =>
        angajati.push(
          <option key={angajat.persoana.id} data-key={angajat.persoana.id}>
            {angajat.persoana.nume + ' ' + angajat.persoana.prenume}
          </option>
        )
      );
    }

    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.show} onHide={this.handleClose} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Row>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    disabled={this.state.isEdit}
                    value={this.state.username}
                    onChange={(e) => this.setState({ username: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Email</Form.Label>
                  <Button
                    variant="link"
                    size="sm"
                    className="float float-right m-0 p-0"
                    onClick={this.folosesteDateleAngajatului}
                  >
                    {this.state.isEdit ? '' : 'Folosește datele angajatului'}
                  </Button>
                  <Form.Control
                    type="text"
                    disabled={this.state.isEdit}
                    value={this.state.email}
                    onChange={(e) => this.setState({ email: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>
                    Angajat al {this.state.socsel ? this.state.socsel.nume : null} asociat cu acest
                    user
                  </Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.numeAngajat || ''}
                    onChange={this.onChangeAngajat}
                  >
                    <option>-</option>
                    {angajati}
                  </Form.Control>
                </Form.Group>
                <Form.Group as={Col} lg="6">
                  <Form.Label>Roluri/Permisiuni</Form.Label>
                  <DropdownMultiselect
                    options={this.state.all_roles}
                    selected={this.state.roles}
                    handleOnChange={(selected) => this.setState({ roles: selected })}
                    name="roles"
                  />
                </Form.Group>
                <Form.Group as={Col} lg="12">
                  <Form.Label>Societati</Form.Label>
                  <DropdownMultiselect
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

        {/* CONFIRM MODAL */}
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

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">Societăți</Breadcrumb.Item>
              <Breadcrumb.Item active>Utilizatori</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">
                  {this.state.socsel
                    ? this.state.socsel.nume + ' - Useri'
                    : 'Useri fara acces la nicio societate'}
                </Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus size="25" />
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
