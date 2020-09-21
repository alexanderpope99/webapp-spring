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

class RoleTabel extends React.Component {
  constructor(props) {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addRole = this.addRole.bind(this);
    this.deleteRole = this.deleteRole.bind(this);
    this.editRole = this.editRole.bind(this);
    this.updateRole = this.updateRole.bind(this);

    this.state = {
      roles: [],
      roleComponent: null,

      // add modal:
      show: false,
      id: '',
      nume: '',
      descriere: '',

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
    if (typeof this.state.roles === 'undefined') return;
    //? fetch
    const roles = await fetch(`${server.address}/role/`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((res) => (res.status !== 200 ? null : res.json()))
      .catch((err) => console.error('err', err));

    if (roles !== null) {
      this.setState(
        {
          roles: roles,
        },
        this.renderRole
      );
    } else {
      this.setState(
        {
          roles: [],
        },
        this.renderRole
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      id: '',
      show: false,
      nume: '',
      descriere: '',

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
        nume: '',
        descriere: '',
        isEdit: false,
      });
  }

  async deleteRole(id) {
    await fetch(`${server.address}/role/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async editRole(role) {
    if (typeof this.state.roles === 'undefined') return;

    this.setState({
      id: role.id,
      nume: '',
      descriere: '',
      show: true,
      isEdit: true,
    });
  }

  async addRole() {
    if (typeof this.state.roles === 'undefined') return;
    const role_body = {
      nume: this.state.nume,
      descriere: this.state.descriere,
      societateselectată: this.state.societateselectată,
    };

    let ok = await fetch(`${server.address}/role`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(role_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Role adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  async updateRole() {
    const role_body = {
      id: this.state.id,
      nume: this.state.nume,
      descriere: this.state.descriere,
      societateselectată: this.state.societateselectată,
    };

    let ok = await fetch(`${server.address}/role/${this.state.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(role_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Role actualizat ✔',
      });
      this.fillTable();
    }
  }

  // function to create react component with fetched data
  renderRole() {
    this.setState({
      roleComponent: this.state.roles.map((role, index) => {
        for (let key in role) {
          if (role[key] === 'null' || role[key] === null) role[key] = '-';
        }
        return (
          <tr key={role.id}>
            <th>{role.nume}</th>
            <th>{role.descriere}</th>
            <th className="d-inline-flex flex-row justify-content-around">
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      onClick={() => this.editRole(role)}
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
                        <Typography>Sigur ștergeți role-ul?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteRole(role.id);
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
            <Modal.Title>Role</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
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
              <Form.Group id="nume">
                <Form.Label>Descriere</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.descriere}
                  onChange={(e) => {
                    this.setState({ descriere: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.state.isEdit ? this.updateRole : this.addRole}>
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
                <Card.Title as="h5">Listă roluri</Card.Title>
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
                      <th>Name</th>
                      <th>Desc</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.roleComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default RoleTabel;
