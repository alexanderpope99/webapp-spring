import React from 'react';
import { Col, Row, Table, Button, Form, Modal, Toast } from 'react-bootstrap';
import { Edit3, Trash2, RotateCw, Plus } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { judete, sectoare } from '../../Resources/judete';
import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

const judeteOptions = judete.map((judet, index) => {
  return <option key={index}>{judet}</option>;
});

const sectoareOptions = sectoare.map((sector, index) => {
  return <option key={index}>{sector}</option>;
});

class CentruCostTabel extends React.Component {
  constructor(props) {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.clearCC = this.clearCC.bind(this);
    this.deleteCC = this.deleteCC.bind(this);
    this.addCC = this.addCC.bind(this);
    this.updateCC = this.updateCC.bind(this);
    this.editCC = this.editCC.bind(this);
    this.folosesteAdresaSocietatii = this.folosesteAdresaSocietatii.bind(this);

    this.state = {
      socsel: getSocSel(),

      centreCost: [],
      centreCostComponent: null,

      // add/edit modal
      show: false,
      isEdit: false,

      // info toast
      showToast: false,
      toastMessage: '',
      toastTitle: '',
      toastColor: '',

      // detalii centru cost
      id: 0,
      nume: '',
      adresaSocietatii: '', // used for endpoint only
      idadresa: '',
      adresa: '',
      localitate: '',
      tipJudet: 'Județ',
      judet: '',
    };
  }

  clearCC() {
    this.setState({
      // detalii centru cost
      id: 0,
      nume: '',
      adresaSocietatii: '', // used for endpoint only
      adresa: '',
      localitate: '',
      tipJudet: 'Județ',
      judet: '',
    });
  }

  onChangeLocalitate(localitate) {
    if (!localitate) {
      this.setState({ localitatte: '', tipJudet: 'Județ' });
      return;
    }

    if (
      localitate.toLowerCase() === 'bucuresti' ||
      localitate.toLowerCase() === 'bucurești' ||
      localitate.toLowerCase() === 'bucharest'
    )
      this.setState({
        tipJudet: 'Sector',
        localitate: localitate,
      });
    else
      this.setState({
        tipJudet: 'Județ',
        localitate: localitate,
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
      adresaSocietatii: '', // used for endpoint only
      idadresa: '',
      adresa: '',
      localitate: '',
      tipJudet: 'Județ',
      judet: '',
    });
  }

  renderTabel() {
    this.setState({
      centreCostComponent: this.state.centreCost.map((cc) => {
        return (
          <tr key={cc.id}>
            <th>{cc.nume}</th>
            <th>
              {cc.adresa ? `${cc.adresa.adresa}, ${cc.adresa.localitate}, ${cc.adresa.judet}` : '-'}
            </th>

            <th>
              <div className="d-flex">
                {/* edit button below */}
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  size="sm"
                  onClick={() => this.editCC(cc)}
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
              </div>
            </th>
          </tr>
        );
      }),
    });
  }

  componentDidMount() {
    this.fillTable();
  }

  async fillTable() {
    if (!this.state.socsel) return;

    const centreCost = await axios
      .get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua centrele de cost\n' + err.response.data.message,
          toastTitle: 'Eroare',
          toastColor: 'white',
        })
      );

    if (centreCost) this.setState({ centreCost: centreCost }, this.renderTabel);
  }

  async deleteCC(id) {
    await axios
      .delete(`${server.address}/centrucost/${id}`, { headers: authHeader() })
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

  async addCC() {
    const centruCost = {
      nume: this.state.nume,
      adresa: {
        adresa: this.state.adresa,
        localitate: this.state.localitate,
        judet: this.state.judet,
      },
    };

    let ok = await axios
      .post(
        `${server.address}/centrucost/ids=${this.state.socsel.id}/${this.state.adresaSocietatii}`,
        centruCost,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga centrul de cost\n' + err.response.data.message,
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
          toastMessage: `Centru cost "${centruCost.nume}" adăugat!`,
        },
        this.fillTable
      );
  }

  async updateCC() {
    const centruCost = {
      nume: this.state.nume,
      adresa: {
        id: this.state.idadresa,
        adresa: this.state.adresa,
        localitate: this.state.localitate,
        judet: this.state.judet,
      },
    };
    if (this.state.adresaSocietatii) centruCost.adresa.id = this.state.idadresa;

    let ok = await axios
      .put(
        `${server.address}/centrucost/${this.state.id}/${this.state.adresaSocietatii}`,
        centruCost,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza centrul de cost\n' + err.response.data.message,
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
          toastMessage: `Centru cost "${centruCost.nume}" actualizat!`,
        },
        this.fillTable
      );
  }

  editCC(centruCost) {
    if (centruCost.adresa)
      this.setState(
        {
          show: true,
          isEdit: true,

          id: centruCost.id,
          nume: centruCost.nume,

          idadresa: centruCost.adresa.id,
          adresa: centruCost.adresa.adresa,
          judet: centruCost.adresa.judet,
        },
        () => this.onChangeLocalitate(centruCost.adresa.localitate)
      );
    else
      this.setState({
        show: true,
        isEdit: true,
        id: centruCost.id,
        nume: centruCost.nume,
      });
  }

  folosesteAdresaSocietatii() {
    if (this.state.adresaSocietatii)
      this.setState({
        adresaSocietatii: null,
        idadresa: '',
        adresa: '',
        tipJudet: 'Județ',
        judet: '',
        localitate: '',
      });
    else
      this.setState({
        adresaSocietatii: 'adrsoc', // used for endpoint only
        idadresa: this.props.adresaSocietate.id,
        adresa: this.props.adresaSocietate.adresa,
        tipJudet: this.props.adresaSocietate.tipJudet,
        judet: this.props.adresaSocietate.judet,
        localitate: this.props.adresaSocietate.localitate,
      });
  }

  render() {
    const judeteComponent = () => {
      if (this.state.tipJudet === 'Județ') return judeteOptions;
      return sectoareOptions;
    };
    const isAdresaSocietatii = this.state.idadresa === this.props.adresaSocietate.id;

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
            <Form.Group id="nume" as={Col} md="12">
              <Form.Label>Nume centru cost</Form.Label>
              <Form.Control
                required
                type="text"
                value={this.state.nume}
                onChange={(e) => this.setState({ nume: e.target.value })}
              />
            </Form.Group>

            <Row className="border rounded pt-3 pb-3 m-3">
              <Form.Group id="adresa" as={Col} md="12">
                <Form.Label>Adresa</Form.Label>
                <Form.Control
                  disabled={isAdresaSocietatii}
                  required
                  type="text"
                  value={this.state.adresa || ''}
                  onChange={(e) => this.setState({ adresa: e.target.value })}
                />
              </Form.Group>
              <Form.Group id="localitate" as={Col} md="12">
                <Form.Label>Localitate</Form.Label>
                <Form.Control
                  disabled={isAdresaSocietatii}
                  required
                  type="text"
                  value={this.state.localitate || ''}
                  onChange={(e) => this.onChangeLocalitate(e.target.value)}
                />
              </Form.Group>
              <Form.Group id="judet" as={Col} md="12">
                <Form.Label>{this.state.tipJudet}</Form.Label>
                <Form.Control
                  disabled={isAdresaSocietatii}
                  required
                  as="select"
                  value={this.state.judet}
                  onChange={(e) => this.setState({ judet: e.target.value })}
                >
                  <option>-</option>
                  {judeteComponent()}
                </Form.Control>
              </Form.Group>
              <Form.Group id="automatic" as={Col} md="12">
                <Form.Check
                  custom
                  type="checkbox"
                  id="folosesteAdresaSocietatii"
                  label="Folosește adresa societății"
                  checked={isAdresaSocietatii}
                  onChange={this.folosesteAdresaSocietatii}
                />
              </Form.Group>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.state.isEdit ? this.updateCC : this.addCC}>
              {this.state.isEdit ? 'Acualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        <Table responsive hover>
          <thead>
            <tr>
              <th>Nume centru cost</th>
              <th>Adresa, Localitate, Judet</th>
              <th>
                <Button size="sm" className="float-right" onClick={this.fillTable}>
                  <RotateCw size={20} />
                  {/* ↺ */}
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
          <tbody>{this.state.centreCostComponent}</tbody>
        </Table>
      </React.Fragment>
    );
  }
}

export default CentruCostTabel;
