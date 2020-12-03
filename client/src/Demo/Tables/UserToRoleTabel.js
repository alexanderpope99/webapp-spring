import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Edit from '@material-ui/icons/Edit';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';

class UserToRoleTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addUserToRole = this.addUserToRole.bind(this);
    this.deleteUserToRole = this.deleteUserToRole.bind(this);
    this.editUserToRole = this.editUserToRole.bind(this);
    this.updateUserToRole = this.updateUserToRole.bind(this);

    this.state = {
      userToRoles: [],
      UserToRoleComponent: null,

      // add modal:
      userid: '',
      roleid: '',

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
    if (typeof this.state.userToRoles === 'undefined') return;
    //? fetch
    const userToRoles = await fetch(`${server.address}/usertorole/`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((res) => (res.status !== 200 ? null : res.json()))
      .catch((err) => console.error('err', err));

    if (userToRoles !== null) {
      this.setState(
        {
          userToRoles: userToRoles,
        },
        this.renderUserToRole
      );
    } else {
      this.setState(
        {
          userToRoles: [],
        },
        this.renderUserToRole
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      roleid: '',
      userid: '',

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
        roleid: '',
        userid: '',
        isEdit: false,
      });
  }

  async deleteUserToRole(roleid, userid) {
    await fetch(`${server.address}/usertorole/role=${roleid}/user=${userid}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async editUserToRole(userToRole) {
    if (typeof this.state.userToRoles === 'undefined') return;

    this.setState({
      roleid: '',
      userid: '',
      show: true,
      isEdit: true,
    });
  }

  async addUserToRole() {
    if (typeof this.state.userToRoles === 'undefined') return;
    const userToRole_body = {
      roleid: this.state.roleid,
      userid: this.state.userid,
    };

    let ok = await fetch(`${server.address}/usertorole`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userToRole_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'UserToRole adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  async updateUserToRole() {
    const userToRole_body = {
      roleid: this.state.roleid,
      userid: this.state.userid,
    };

    let ok = await fetch(`${server.address}/usertorole/${this.state.roleid}+${this.state.userid}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userToRole_body),
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
  renderUserToRole() {
    this.setState({
      userToRoleComponent: this.state.userToRoles.map((userToRole, index) => {
        for (let key in userToRole) {
          if (userToRole[key] === 'null' || userToRole[key] === null) userToRole[key] = '-';
        }
        return (
          <tr key={userToRole.roleid + userToRole.userid}>
            <th>{userToRole.roleid}</th>
            <th>{userToRole.userid}</th>
            <th className="d-inline-flex flex-row justify-content-around">
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      variant="outline-secondary"
                      className="m-0 p-1 rounded-circle border-0"
                      {...bindTrigger(popupState)}
                    >
                      <Trash2 fontSize="small" />
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
                        <Typography>Sigur ștergeți această relație?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteUserToRole(userToRole.roleid, userToRole.userid);
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
            <Modal.Title>UserToRole</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="roleid">
                <Form.Label>RoleId</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.roleid}
                  onChange={(e) => {
                    this.setState({ roleid: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="userid">
                <Form.Label>UserId</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.userid}
                  onChange={(e) => {
                    this.setState({ userid: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.state.isEdit ? this.updateUserToRole : this.addUserToRole}
            >
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
                <Card.Title as="h5">Listă relații</Card.Title>
                <Button
                  variant="outline-primary"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
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
                      <th>RoleId</th>
                      <th>UserId</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.userToRoleComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default UserToRoleTabel;
