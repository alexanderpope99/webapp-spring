import React from 'react';
import { Col, Table, Button, Form, Modal, Toast } from 'react-bootstrap';
import { Edit3, Trash2, RotateCw, Plus } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

class ContBancarTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.clearCB = this.clearCB.bind(this);
    this.delete = this.delete.bind(this);
    this.add = this.add.bind(this);
    this.update = this.update.bind(this);
    this.edit = this.edit.bind(this);

    this.state = {
      socsel: getSocSel(),

      conturiBancare: [],
      conturiBancareComponent: null,

      // add/edit modal
      show: false,
      isEdit: false,

      // info toast
      showToast: false,
      toastMessage: '',
      toastTitle: '',
      toastColor: 'lightgreen',

      // detalii cont bancar
      id: 0,
      iban: '',
      numeBanca: '',
    };
  }

  clearCB() {
    this.setState({
      // detalii cont bancar
      id: 0,
      iban: '',
      numeBanca: '',
    });
  }

  handleClose() {
    this.setState(
      {
        // add/edit modal
        show: false,
        isEdit: false,
      },
      this.clearCB
    );
  }

  componentDidMount() {
    this.fillTable();
  }

  async fillTable() {
    if (!this.state.socsel) return;

    const conturiBancare = await axios
      .get(`${server.address}/contbancar/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua conturile ' + err.response.data.message,
          toastTitle: 'Eroare',
          toastColor: 'white',
        })
      );

    if (conturiBancare) this.setState({ conturiBancare: conturiBancare }, this.renderTabel);
	}
	
	renderTabel() {
    this.setState({
      conturiBancareComponent: this.state.conturiBancare.map((item) => {
        return (
          <tr key={item.id}>
            <th>{item.numebanca}</th>
            <th>{item.iban}</th>

            <th>
              <div className="d-flex">
                {/* edit button below */}
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  size="sm"
                  onClick={() => this.edit(item)}
                >
                  <Edit3 size={18} />
                </Button>
                {/* delete button below */}
                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
                        size="sm"
                        {...bindTrigger(popupState)}
                      >
                        <Trash2 size={18} />
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
                          <Typography>Confirmare ștergere</Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.delete(item.id);
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
              </div>
            </th>
          </tr>
        );
      }),
    });
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/contbancar/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge centrul de cost\n' + err.response.data.message,
          toastTitle: 'Eroare',
          toastColor: 'white',
        })
      );
  }

  async add() {
    const contbancar = {
      numebanca: this.state.numeBanca,
      iban: this.state.iban,
    };

    const ok = await axios
      .post(
        `${server.address}/contbancar/ids=${this.state.socsel.id}`,
        contbancar,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
				toastMessage: 'Nu am putut adăuga ' + err.response.data.message,
          toastTitle: 'Eroare',
          toastColor: 'white',
        })
      );

    if (ok)
      this.setState(
        {
          show: false,
          isEdit: false,
          showToast: true,
				toastMessage: `Cont bancar adăugat! `,
          toastColor: 'lightgreen',
        },
        this.fillTable
      );
  }

  async update() {
    const contbancar = {
      id: this.state.id,
      numebanca: this.state.numeBanca,
      iban: this.state.iban,
    };

    const ok = await axios
      .put(`${server.address}/contbancar/${contbancar.id}/ids=${this.state.socsel.id}`, contbancar, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza' + err.response.data.message,
          toastTitle: 'Eroare',
          toastColor: 'white',
        })
      );

    if (ok)
      this.setState(
        {
          show: false,
          isEdit: false,
          showToast: true,
          toastMessage: `Cont bancar actualizat!`,
          toastColor: 'lightgreen',
        },
        this.fillTable
      );
  }

  edit(item) {
    this.setState({
			show: true,
			isEdit: true,
      id: item.id,
      iban: item.iban,
      numeBanca: item.numebanca,
    });
  }

  render() {
    return (
      <React.Fragment>
        {/* ON ADD/EDIT SUCCESS TOAST */}
        <Toast
          onClose={() => this.setState({ showToast: false, toastMessage: '' })}
          show={this.state.showToast}
          delay={2500}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: this.state.toastColor }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">{this.state.toastTitle}</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>

        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Centru cost</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group controlId="numeBanca" as={Col} md="12">
              <Form.Label>Nume bancă</Form.Label>
              <Form.Control
                required
                type="text"
                value={this.state.numeBanca}
                onChange={(e) => this.setState({ numeBanca: e.target.value })}
              />
            </Form.Group>

            <Form.Group id="iban" as={Col} md="12">
              <Form.Label>Nume bancă</Form.Label>
              <Form.Control
                required
                type="text"
                value={this.state.iban}
                style={{ fontFamily: 'Consolas, Courier New' }}
                onChange={(e) => this.setState({ iban: e.target.value })}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.state.isEdit ? this.update : this.add}>
              {this.state.isEdit ? 'Acualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        <Table responsive hover>
          <thead>
            <tr>
              <th>Nume bancă</th>
              <th>IBAN</th>
              <th>
                <Button size="sm" className="float-right" onClick={this.fillTable}>
                  <RotateCw size={20} />
                </Button>
                <Button
                  size="sm"
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
                >
                  <Plus size={20} />
                </Button>
              </th>
            </tr>
          </thead>
          <tbody>{this.state.conturiBancareComponent}</tbody>
        </Table>
      </React.Fragment>
    );
  }
}

export default ContBancarTabel;
