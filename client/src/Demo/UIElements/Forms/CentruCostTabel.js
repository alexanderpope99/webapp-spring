import React from 'react';
import { Row, Col, Card, Table, Button, Form, Modal } from 'react-bootstrap';
import { Edit3, Trash2, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

class CentruCostTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.clearCC = this.clearCC.bind(this);
    this.deleteCC = this.deleteCC.bind(this);
    this.addCC = this.addCC.bind(this);
    this.updateCC = this.updateCC.bind(this);
    this.editCC = this.editCC.bind(this);

    this.state = {
      socsel: getSocSel(),

      centreCost: [],
      centreCostComponent: null,

      // ad/edit modal
      show: false,
      isEdit: false,

      // detalii centru cost
      id: 0,
      nume: '',
      adresa: null, // of type: { adresa: '', localitate: '', judet: '' }
    };
  }

  clearCC() {
    this.setState({
      // detalii centru cost
      id: 0,
      nume: '',
      adresa: null,
    });
  }

  handleClose() {
    this.setState({
      // add/edit modal
      show: false,
      isEdit: false,

      // detalii centru cost
      id: 0,
      nume: '',
      adresa: null, // of type: { adresa: '', localitate: '', judet: '' }
    });
  }

  renderTabel() {
    this.setState({
      centreCostComponent: this.state.map((cc, index) => {
        return (
          <tr>
            <th>{cc.nume}</th>
            <th>{`${cc.adresa.adresa}, ${cc.adresa.localitate}, ${cc.adresa.judet}`}</th>
            {/* edit button below */}
            <Button
              variant="outline-secondary"
              className="ml-2 p-1 rounded-circle border-0"
              onClick={() => this.editCC(cc)}
            >
              <Edit3 size={20} />
            </Button>
            {/* delete button below */}
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
                        <Typography>Confirmare ștergere</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteCC(cc.id);
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

  async fillTable() {
    const centreCost = await axios
      .get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));
    this.setState({ entreCost: centreCost }, this.renderTabel);
  }

  async deleteCC(id) {
    await axios
      .delete(`${server.address}/centrucost/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async addCC() {
    const centruCost = {
      nume: this.state.nume,
      adresa: this.state.adresa,
    };

    let ok = await axios
      .post(`${server.address}/centrucost/ids=${this.state.socsel.id}`, centruCost, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) this.fillTable();
  }

	async updateCC() {

	}

  editCC(centruCost) {
    this.setState({
			show: true,
			isEdit: true,
			
      id: centruCost.id,
      nume: centruCost.nume,
      adresa: centruCost.adresa,
    });
	}

  render() {
    return (
      <React.Fragment>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Centru cost</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="nume" as={Col} md="3">
                <Form.Label>Nume centru cost</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.nume}
                  onChange={(e) => this.setState({ nume: e.target.value })}
                />
              </Form.Group>

              <Form.Group id="adresa" as={Col} md="8">
                <Form.Label>Adresa</Form.Label>
                <Form.Control
                  required
                  type="text"
                  value={this.state.adresa}
                  onChange={(e) => this.setState({ adresa: e.target.value })}
                />
              </Form.Group>
                <Button className="display-flex m-0" onClick={this.addCC}>
                  +
                </Button>
            </Form>
            <Button variant="primary" onClick={this.state.isEdit ? this.updateCC : this.addCC}>
              {this.state.isEdit ? 'Acualizează' : 'Adaugă'}
            </Button>
          </Modal.Body>
        </Modal>

        <Table responsive hover>
          <thead>
            <th>Nume centru cost</th>
            <th>Adresa, Localitate, Judet</th>
          </thead>
          <tbody>{this.state.centreCostComponent}</tbody>
        </Table>
      </React.Fragment>
    );
  }
}

export default CentruCostTabel;
