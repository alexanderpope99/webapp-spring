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
import { judete, sectoare } from '../Resources/judete';
import axios from 'axios';
import authHeader from '../../services/auth-header';

export default class ClientiTabel extends React.Component {
  constructor() {
    super();

    this.getClienti = this.getClienti.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);

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
      statut: '',
      nrregcom: '',
      codfiscal: '',
      cotatva: '',
      client: false,
      furnizor: false,
      extern: false,
      banca: '',
      sucursala: '',
      cont: '',
      moneda: '',
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
      statut: '',
      nrregcom: '',
      codfiscal: '',
      cotatva: '',
      client: false,
      furnizor: false,
      extern: false,
      banca: '',
      sucursala: '',
      cont: '',
      moneda: '',
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
          toastMessage: 'Nu am putut prelua clientii ' + err.response.data.message,
        })
      );
    console.log(clienti);
    if (clienti) this.setState({ clienti: clienti });
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
    if (localitate)
      this.setState({
        tipJudet: this.getTipJudet(localitate),
        localitate: localitate,
      });
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        showConfirm: false,
        modalTitle: '',
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        isEdit: false,
      }, this.clearFields);
  }

  render() {

    const clientiComponent = this.state.clienti.map(item =>
    (
      <tr key={item.id}>
        <th>
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
        </th>

        <th>{item.numecomplet}</th>
        <th>{item.nume}</th>
        <th>{item.statut}</th>
        <th>{item.nrregcom}</th>
        <th>{item.codfiscal}</th>
        <th>{item.cotatva}</th>
        <th>{item.adresa.adresa}</th>
        <th>{item.adresa.localitate}</th>
        <th>{item.adresa.judet}</th>
        <th>{item.client}</th>
        <th>{item.furnizor}</th>
        <th>{item.extern}</th>
        <th>{item.banca}</th>
        <th>{item.sucursala}</th>
        <th>{item.cont}</th>
        <th>{item.moneda}</th>
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
                    type="textarea"
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

                <Form.Group as={Col} md="6" controlId="regcom">
                  <Form.Label>Număr Registrul Comerțului</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nrregcom}
                    onChange={(e) => this.setState({ nrregcom: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6" controlId="codfiscal">
                  <Form.Label>Cod Fiscal / CNP</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.codfiscal}
                    onChange={(e) => this.setState({ codfiscal: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6" controlId="cotatva">
                  <Form.Label>Cota TVA</Form.Label>
                  <Form.Control
                    type="number"
                    value={this.state.cotatva}
                    onChange={(e) => this.setState({ cotatva: e.target.value })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="6" controlId="adresa">
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

                <Form.Group as={Col} md="3">
                  <Form.Check
                    custom
                    type="switch"
                    id="clientCheck"
                    label="Client"
                    checked={this.state.client}
                    onChange={(e) => this.setState({ client: e.target.checked })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="3">
                  <Form.Check
                    custom
                    type="switch"
                    id="furnizorCheck"
                    label="Furnizor"
                    checked={this.state.furnizor}
                    onChange={(e) => this.setState({ furnizor: e.target.checked })}
                  />
                </Form.Group>

                <Form.Group as={Col} md="3">
                  <Form.Check
                    custom
                    type="switch"
                    id="externCheck"
                    label="Extern"
                    checked={this.state.extern}
                    onChange={(e) => this.setState({ extern: e.target.checked })}
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
        </Modal>

        {/* CONFIRM MODAL */}
        <Modal show={this.state.show_confirm} onHide={() => this.handleClose(true)}>
          <Modal.Header closeButton>
            <Modal.Title>Modificat</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="outline-info" onClick={this.handleClose}>
              Închide
            </Button>
          </Modal.Footer>
        </Modal>

        {/* TABLE */}
        <Row>
          <Col>
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
                  <Plus size={20}/>
                </Button>

              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Nume Complet</th>
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
                  <tbody>{this.state.clientiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>

      </Aux>
    )
  }
}