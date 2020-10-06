import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import Add from '@material-ui/icons/Add';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { Multiselect } from 'multiselect-react-dropdown';

class UserTabel extends React.Component {
  constructor(props) {
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
      user: [],
      userComponent: null,

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      id: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
      status: '',
      show: false,
      socs: [],
    };
  }

  componentDidMount() {
    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addUser() {
    const user_body = {};

    let ok = await axios
      .post(`${server.address}/user`, user_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Cerere adăugată cu succes',
        },
        this.onRefresh
      );
    }
  }

  async updateUser(iduser) {
    let pentruId = await axios
      .get(`${server.address}/user/${JSON.parse(localStorage.getItem('user')).id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    const user_body = {
      pentru: pentruId,
      dela: this.state.dela,
      panala: this.state.panala,
      tip: this.state.tip,
      motiv: this.state.motiv,
      societate: getSocSel().id,
      status: 'Propus (Modificat)',
    };

    const ok = await axios
      .put(`${server.address}/user/${iduser}`, user_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'User actualizat',
      });
    }
  }

  async editUser(usr) {
    console.log(usr);
    this.setState({
      isEdit: true,
      show: true,

      id: usr.id,
    });
  }

  async deleteUser(id) {
    await axios
      .delete(`${server.address}/user/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  async onSelectRole(id) {
    await axios
      .delete(`${server.address}/user/roles/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  async onRemoveRole(id) {
    await axios
      .delete(`${server.address}/user/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  // function to create react component with fetched data
  async renderUsers() {
    this.setState({
      userComponent: await Promise.all(
        this.state.user.map(async (usr, index) => {
          return (
            // TODO
            <tr key={usr.id}>
              <th>{usr.username}</th>
              <th>{usr.email}</th>
              <th>
                <Multiselect
                  placeholder=""
                  selectedValues={usr.roles.map((val) => {
                    return val['name'];
                  })}
                  options={['ROLE_ADMIN', 'ROLE_DIRECTOR', 'ROLE_CONTABIL', 'ROLE_ANGAJAT']}
                  onSelect={this.onSelectRole}
                  onRemove={this.onRemoveRole}
                  isObject={false}
                />
              </th>
              <th>
                <Multiselect
                  placeholder=""
                  selectedValues={usr.societati.map((val) => {
                    return val['nume'];
                  })}
                  options={this.state.socs}
                  isObject={false}
                />
              </th>
              <th>
                <Multiselect
                  placeholder=""
                  singleSelect="true"
                  selectedValues={usr.societate.map((val) => {
                    return val['nume'];
                  })}
                  options={this.state.socs}
                  isObject={false}
                />
              </th>
              <th>
                <Multiselect
                  placeholder=""
                  singleSelect="true"
                  selectedValues={usr.persoana.map((val) => {
                    return val['nume'] + ' ' + val['prenume'] === 'null null'
                      ? ''
                      : val['nume'] + ' ' + val['prenume'];
                  })}
                  options={this.state.socs}
                  isObject={false}
                />
              </th>
              <th></th>
              <th>
                <Row>
                  <Button
                    onClick={() => this.editUser(usr)}
                    variant="outline-secondary"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <Edit fontSize="small" />
                  </Button>

                  <PopupState variant="popover" popupId="demo-popup-popover">
                    {(popupState) => (
                      <div>
                        <Button
                          variant="outline-secondary"
                          className="m-1 p-1 rounded-circle border-0"
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
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
                            <br />
                            <Button
                              variant="outline-danger"
                              onClick={() => {
                                popupState.close();
                                this.deleteUser(usr.id);
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
                </Row>
              </th>
            </tr>
          );
        })
      ),
    });
  }

  async onRefresh() {
    let user = await axios
      .get(`${server.address}/user`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    let thesocs = await axios
      .get(`${server.address}/societate`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    this.setState({
      socs: thesocs.map((val) => {
        return val.nume;
      }),
    });

    user = await Promise.all(
      user.map(async (usr) => {
        let roles = await axios
          .get(`${server.address}/user/roles/${usr.id}`, { headers: authHeader() })
          .then((res) => res.data);
        let societati = await axios
          .get(`${server.address}/user/societati/${usr.id}`, { headers: authHeader() })
          .then((res) => res.data);
        let persoana = await axios
          .get(`${server.address}/user/persoana/${usr.id}`, { headers: authHeader() })
          .then((res) => res.data);
        let societate = await axios
          .get(`${server.address}/user/societate/${usr.id}`, { headers: authHeader() })
          .then((res) => res.data);
        let superior = await axios
          .get(`${server.address}/user/superior/${usr.id}`, { headers: authHeader() })
          .then((res) => res.data);

        return {
          ...usr,
          roles: roles,
          societati: societati,
          persoana: persoana,
          societate: societate,
          superior: superior,
        };
      })
    );

    if (user) {
      this.setState(
        {
          user: user,
        },
        this.renderUsers
      );
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateUser(this.state.id);
    else this.addUser();
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      pentru: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
    });
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
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addCerereConcediu}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>De la</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.dela}
                      onChange={(e) => this.setState({ dela: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Până la</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.panala}
                      onChange={(e) => this.setState({ panala: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Tip</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.tip}
                      onChange={(e) => this.setState({ tip: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Motiv</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.motiv}
                      onChange={(e) => this.setState({ motiv: e.target.value })}
                    />
                  </Form.Group>
                </Col>
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
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Add className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Username</th>
                      <th>Email</th>
                      <th>Role-uri</th>
                      <th>Societăți Acces</th>
                      <th>Societate</th>
                      <th>Persoană</th>
                      <th>Superior</th>
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
