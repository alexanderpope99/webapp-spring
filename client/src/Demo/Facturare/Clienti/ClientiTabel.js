import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Table, Button, Modal, Form, Breadcrumb, Toast } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { judete, sectoare } from '../../Resources/judete';

import authHeader from '../../../services/auth-header';

export default class ClientiTabel extends React.Component {
  constructor() {
    super();

    this.getClienti = this.getClienti.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),

      // error toast
      showToast: false,
      toastMessage: '',

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // toti clientii de la societatea selectata
      clienti: [],

      // add/edit modal
      show: false,
      isEdit: false,
      id: null,
      numecomplet: '',
      nume: '',
      statut: 'Persoana juridica',
      nrregcom: '',
      codfiscal: '',
      cotatva: 19,
      client: false,
      furnizor: false,
      extern: false,
      banca: '',
      sucursala: '',
      cont: '',
      moneda: 'RON',
      idadresa: null,
      adresa: '',
      judet: '',
      tipJudet: 'Județ',
      localitate: '',
    }
  }

  clearFields() {
    this.setState({
      id: null,
      numecomplet: '',
      nume: '',
      statut: 'Persoana juridica',
      nrregcom: '',
      codfiscal: '',
      cotatva: 19,
      client: false,
      furnizor: false,
      extern: false,
      banca: '',
      sucursala: '',
      cont: '',
      moneda: 'RON',
      idadresa: null,
      adresa: '',
      judet: '',
      tipJudet: 'Județ',
      localitate: '',
    });
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.getClienti();
    window.scrollTo(0, 0);
  }

  async getClienti() {
    const clienti = await axios
      .get(`${server.address}/client/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then(res => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua clientii: ' + err.response.data.message,
        })
      );
    if (clienti) this.setState({ clienti: clienti }, this.clearFields);
  }

  getTipJudet(tipJudet) {
    if (typeof tipJudet !== 'string') {
      return 'Județ';
    }
    if (
      tipJudet.toLowerCase() === 'bucuresti' ||
      tipJudet.toLowerCase() === 'bucurești' ||
      tipJudet.toLowerCase() === 'bucharest'
    )
      return 'Sector';
    else return 'Județ';
  }
  onChangeLocalitate(localitate) {
    if (localitate) {
      let tip_judet = this.getTipJudet(localitate);

      let judet = tip_judet === this.state.tipJudet
        ? this.state.judet
        : tip_judet === 'Județ' ? 'ALBA' : 'SECTOR 1';

      this.setState({
        tipJudet: tip_judet,
        judet: judet,
        localitate: localitate,
      });
    }
  }

  handleClose() {
    this.setState({
      showConfirm: false,
      modalMessage: '',
      show: false,
      isEdit: false,
    }, this.clearFields);
  }

  edit(client) {
    this.setState({
      showConfirm: false,
      modalMessage: '',
      show: true,
      isEdit: true,

      // detalii client
      id: client.id,
      numecomplet: client.numecomplet || '',
      nume: client.nume || '',
      statut: client.statut || '',
      nrregcom: client.nrregcom || '',
      codfiscal: client.codfiscal || '',
      cotatva: client.cotatva || '',
      client: client.client || false,
      furnizor: client.furnizor || false,
      extern: client.extern || false,
      banca: client.banca || '',
      sucursala: client.sucursala || '',
      cont: client.cont || '',
      moneda: client.moneda || 'RON',
      idadresa: client.adresa.id || null,
      adresa: client.adresa.adresa || '',
    }, () => this.onChangeLocalitate(client.adresa.localitate));
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/client/${id}`, { headers: authHeader() })
      .then(this.getClienti)
      .catch(err =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge clientul: ' + err.response.data.message,
        }));
  }

  async onSubmit() {
    const client = {
      numecomplet: this.state.numecomplet || '',
      nume: this.state.nume || '',
      statut: this.state.statut || '',
      nrregcom: this.state.nrregcom || '',
      codfiscal: this.state.codfiscal || '',
      cotatva: this.state.cotatva || '',
      client: this.state.client || false,
      furnizor: this.state.furnizor || false,
      extern: this.state.extern || false,
      banca: this.state.banca || '',
      sucursala: this.state.sucursala || '',
      cont: this.state.cont || '',
      moneda: this.state.moneda || 'RON',
      adresa: {
        id: this.state.idadresa || null,
        adresa: this.state.adresa || '',
        judet: this.state.judet || '',
        localitate: this.state.localitate || ''
      }
    }

    var res = null;
    if (this.state.isEdit) {
      res = await axios
        .put(`${server.address}/client/${this.state.id}/ids=${this.state.socsel.id}`, client, { headers: authHeader() })
        .then(res => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut modifica clientul: ' + err.response.data.message,
          })
        );
    } else {
      res = await axios
        .post(`${server.address}/client/ids=${this.state.socsel.id}`, client, { headers: authHeader() })
        .then(res => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut adăuga clientul: ' + err.response.data.message,
          })
        );
    }
    if (res) {

      this.setState({
        show: false,
        showConfirm: true,
        modalMessage: this.state.isEdit ? 'Client modificat' : 'Client adăugat',
      }, this.getClienti);
    }
  }

  render() {

    const clientiComponent = this.state.clienti.map(item =>
    (
      <tr key={item.id}>
        <td>
          <div>
            <div className="d-flex">
              <Button
                onClick={() => this.edit(item)}
                variant="outline-secondary"
                className="m-1 p-1 rounded-circle border-0"
              >
                <Edit3 size={20} />
              </Button>

              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button
                      variant="outline-secondary"
                      className="m-1 p-1 rounded-circle border-0"
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
                        <Typography>
                          Sigur ștergeți clientul?
                          </Typography>
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
          </div>
        </td>

        <td>{item.nume}</td>
        <td>{item.statut}</td>
        <td>{item.nrregcom}</td>
        <td>{item.codfiscal}</td>
        <td>{item.cotatva}</td>
        <td>{item.adresa.adresa.length > 15 ? item.adresa.adresa.substring(0, 15)+"..."  : '' }</td>
        <td>{item.adresa.localitate}</td>
        <td>{item.adresa.judet}</td>
        <td>{item.client ? 'DA' : 'NU'}</td>
        <td>{item.furnizor ? 'DA' : 'NU'}</td>
        <td>{item.extern ? 'DA' : 'NU'}</td>
        <td>{item.banca}</td>
        <td>{item.sucursala}</td>
        <td>{item.cont}</td>
        <td>{item.moneda}</td>
      </tr>

    ));

    var judeteComponent = [];
    if (this.state.tipJudet === 'Județ')
      judeteComponent = judete.map((judet, index) => <option key={index}>{judet}</option>);
    else judeteComponent = sectoare.map((sector, index) => <option key={index}>{sector}</option>)

    return (
      <Aux>
        {/* ERROR TOAST */}
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
            <Modal.Title>Detalii client</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Row>
                <Form.Group as={Col} md="12">
                  <Form.Label>Nume complet</Form.Label>
                  <Form.Control
                    as="textarea"
                    value={this.state.numecomplet}
                    onChange={(e) => this.setState({ numecomplet: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6" controlId="nume-client">
                  <Form.Label>Nume</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nume}
                    onChange={(e) => this.setState({ nume: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6">
                  <Form.Label>Statut</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.statut}
                    onChange={(e) => this.setState({ statut: e.target.value })}
                  >
                    <option>Persoană juridică</option>
                    <option>Persoană fizică</option>
                  </Form.Control>
                </Form.Group>

                <Form.Group as={Col} md="4" controlId="regcom">
                  <Form.Label>Număr Registrul Comerțului</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nrregcom}
                    onChange={(e) => this.setState({ nrregcom: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="4" controlId="codfiscal">
                  <Form.Label>Cod Fiscal / CNP</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.codfiscal}
                    onChange={(e) => this.setState({ codfiscal: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="4" controlId="cotatva">
                  <Form.Label>Cota TVA</Form.Label>
                  <Form.Control
                    type="number"
                    value={this.state.cotatva || 19}
                    onChange={(e) => this.setState({ cotatva: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="12" controlId="adresa">
                  <Form.Label>Adresa</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.adresa}
                    onChange={(e) => this.setState({ adresa: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6" controlId="localitate">
                  <Form.Label>Localitate</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.localitate}
                    onChange={(e) => this.onChangeLocalitate(e.target.value)}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6">
                  <Form.Label>{this.state.tipJudet}</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.judet}
                    onChange={(e) => this.setState({ judet: e.target.value })}
                  >
                    {judeteComponent}
                  </Form.Control>
                </Form.Group>

                <Form.Group as={Col} md="4">
                  <Form.Check
                    custom
                    type="switch"
                    id="clientCheck"
                    label="Client"
                    checked={this.state.client}
                    onChange={(e) => this.setState({ client: e.target.checked })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="4">
                  <Form.Check
                    custom
                    type="switch"
                    id="furnizorCheck"
                    label="Furnizor"
                    checked={this.state.furnizor}
                    onChange={(e) => this.setState({ furnizor: e.target.checked })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="4">
                  <Form.Check
                    custom
                    type="switch"
                    id="externCheck"
                    label="Extern"
                    checked={this.state.extern}
                    onChange={(e) => this.setState({ extern: e.target.checked })}
                  />
                </Form.Group>

                <Row className="border rounded pt-3 pb-3 m-3">
                  <Col md={12}>
                    <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                      Detalii cont principal (obligatoriu)
                    </Typography>
                  </Col>

                  <Form.Group as={Col} md="6" controlId="numebanca">
                    <Form.Label>Banca</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.banca}
                      onChange={(e) => this.setState({ banca: e.target.value })}
                    />
                  </Form.Group>

                  <Form.Group as={Col} md="6" controlId="numebanca">
                    <Form.Label>Sucursala</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.sucursala}
                      onChange={(e) => this.setState({ sucursala: e.target.value })}
                    />
                  </Form.Group>

                  <Form.Group as={Col} md="6">
                    <Form.Label>Cont</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.cont}
                      onChange={(e) => this.setState({ cont: e.target.value })}
                    />
                  </Form.Group>


                  <Form.Group as={Col} md="6" controlId="numebanca">
                    <Form.Label>Moneda</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.moneda}
                      onChange={(e) => this.setState({ moneda: e.target.value })}
                    >
                      <option>RON</option>
                      <option>EUR</option>
                      <option>USD</option>
                    </Form.Control>
                  </Form.Group>
                </Row>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button onClick={this.onSubmit}>Salvează</Button>
          </Modal.Footer>
        </Modal>

        {/* CONFIRM MODAL */}
        <Modal show={this.state.showConfirm} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="outline-info" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* TABLE */}
        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">{this.state.socsel.nume}</Breadcrumb.Item>
              <Breadcrumb.Item active>Clienți</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Button
                  variant="outline-primary"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.getClienti}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
                <Card.Title as="h5">Clienți</Card.Title>
                <Button
                  variant="outline-primary"
                  className="float-right"
                  onClick={() => this.setState({ show: true, showConfirm: false })}
                >
                  <Plus size={20} />
                </Button>

              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Nume</th>
                      <th>Statut</th>
                      <th>Nr. reg. com.</th>
                      <th>Cod fiscal</th>
                      <th>Cotă TVA</th>
                      <th>Adresa</th>
                      <th>Localitate</th>
                      <th>Județ</th>
                      <th>Client</th>
                      <th>Furnizor</th>
                      <th>Extern</th>
                      <th>Banca</th>
                      <th>Sucursala</th>
                      <th>Cont</th>
                      <th>Moneda</th>
                    </tr>
                  </thead>
                  <tbody>{clientiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>

      </Aux>
    )
  }
}