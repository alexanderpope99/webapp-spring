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

class PermissionTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addPermission = this.addPermission.bind(this);
    this.deletePermission = this.deletePermission.bind(this);
    this.editPermission = this.editPermission.bind(this);
    this.updatePermission = this.updatePermission.bind(this);

    this.state = {
      permissions: [],
      permissionComponent: null,

      // add modal:
      id: '',
      show: false,
      nume: '',
      desc: '',

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
    if (typeof this.state.permissions === 'undefined') return;
    //? fetch
    const permissions = await fetch(`${server.address}/permission/`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((res) => (res.status !== 200 ? null : res.json()))
      .catch((err) => console.error('err', err));

    if (permissions !== null) {
      this.setState(
        {
          permissions: permissions,
        },
        this.renderPermission
      );
    } else {
      this.setState(
        {
          permissions: [],
        },
        this.renderPermission
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      id: '',
      show: false,
      nume: '',

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
        isEdit: false,
      });
  }

  async deletePermission(id) {
    await fetch(`${server.address}/permission/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async editPermission(permission) {
    if (typeof this.state.permissions === 'undefined') return;

    this.setState({
      id: permission.id,
      nume: permission.nume,
      show: true,
      isEdit: true,
    });
  }

  async addPermission() {
    if (typeof this.state.permissions === 'undefined') return;
    const permission_body = {
      name: this.state.nume,
    };

    let ok = await fetch(`${server.address}/permission`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(permission_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Permission adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  async updatePermission() {
    const permission_body = {
      id: this.state.id,
      name: this.state.nume,
    };

    let ok = await fetch(`${server.address}/permission/${this.state.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(permission_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Permission actualizat ✔',
      });
      this.fillTable();
    }
  }

  // function to create react component with fetched data
  renderPermission() {
    this.setState({
      permissionComponent: this.state.permissions.map((permission, index) => {
        for (let key in permission) {
          if (permission[key] === 'null' || permission[key] === null) permission[key] = '-';
        }
        return (
          <tr key={permission.id}>
            <th>{permission.id}</th>
            <th>{permission.name}</th>
            <th className="d-inline-flex flex-row justify-content-around">
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      onClick={() => this.editPermission(permission)}
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
                        <Typography>Sigur ștergeți permission-ul?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deletePermission(permission.id);
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
            <Modal.Title>Permission</Modal.Title>
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
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.state.isEdit ? this.updatePermission : this.addPermission}
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
                <Card.Title as="h5">Listă permission-uri</Card.Title>
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
                      <th>Id</th>
                      <th>Nume</th>
                      <th>Desc</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.permissionComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default PermissionTabel;
