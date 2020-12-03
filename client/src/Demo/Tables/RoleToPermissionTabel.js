import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, RotateCw } from 'react-feather';

import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';

class RoleToPermissionTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addRoleToPermission = this.addRoleToPermission.bind(this);
    this.deleteRoleToPermission = this.deleteRoleToPermission.bind(this);
    this.editRoleToPermission = this.editRoleToPermission.bind(this);
    this.updateRoleToPermission = this.updateRoleToPermission.bind(this);

    this.state = {
      rolesToPermissions: [],
      roleToPermissionComponent: null,

      // add modal:
      roleid: '',
      permissionid: '',

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
    if (typeof this.state.rolesToPermissions === 'undefined') return;
    //? fetch
    const rolesToPermissions = await fetch(`${server.address}/roletopermission/`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((res) => (res.status !== 200 ? null : res.json()))
      .catch((err) => console.error('err', err));

    if (rolesToPermissions !== null) {
      this.setState(
        {
          rolesToPermissions: rolesToPermissions,
        },
        this.renderRoleToPermission
      );
    } else {
      this.setState(
        {
          rolesToPermissions: [],
        },
        this.renderRoleToPermission
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      roleid: '',
      permissionid: '',

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
        permissionid: '',
        isEdit: false,
      });
  }

  async deleteRoleToPermission(roleid, permissionid) {
    await fetch(`${server.address}/roletopermission/${roleid}+${permissionid}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async editRoleToPermission(roleToPermission) {
    if (typeof this.state.rolesToPermissions === 'undefined') return;

    this.setState({
      roleid: '',
      permissionid: '',
      show: true,
      isEdit: true,
    });
  }

  async addRoleToPermission() {
    if (typeof this.state.rolesToPermissions === 'undefined') return;
    const roleToPermission_body = {
      roleid: this.state.roleid,
      permissionid: this.state.permissionid,
    };

    let ok = await fetch(`${server.address}/roletopermission`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(roleToPermission_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'RoleToPermission adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  async updateRoleToPermission() {
    const roleToPermission_body = {
      roleid: this.state.roleid,
      permissionid: this.state.permissionid,
    };

    let ok = await fetch(
      `${server.address}/roletopermission/${this.state.roleid}+${this.state.permissionid}`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(roleToPermission_body),
      }
    )
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
  renderRoleToPermission() {
    this.setState({
      roleToPermissionComponent: this.state.rolesToPermissions.map((roleToPermission, index) => {
        for (let key in roleToPermission) {
          if (roleToPermission[key] === 'null' || roleToPermission[key] === null)
            roleToPermission[key] = '-';
        }
        return (
          <tr key={roleToPermission.roleid + roleToPermission.permissionid}>
            <th>
              {roleToPermission.roleid}+' '+{roleToPermission.roleName}
            </th>
            <th>
              {roleToPermission.permissionid}+' '+{roleToPermission.permissionName}
            </th>
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
                            this.deleteRoleToPermission(
                              roleToPermission.roleid,
                              roleToPermission.permissionid
                            );
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
            <Modal.Title>RoleToPermission</Modal.Title>
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
              <Form.Group id="permissionid">
                <Form.Label>PermissionId</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.permissionid}
                  onChange={(e) => {
                    this.setState({ permissionid: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.state.isEdit ? this.updateRoleToPermission : this.addRoleToPermission}
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
                      <th>PermissionId</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.roleToPermissionComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default RoleToPermissionTabel;
