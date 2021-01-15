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
import { downloadFactura } from '../Resources/download';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import 'react-dropzone-uploader/dist/styles.css';
import Dropzone from 'react-dropzone-uploader';

class FacturiTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addFactura = this.addFactura.bind(this);
    this.updateFactura = this.updateFactura.bind(this);
    this.editFactura = this.editFactura.bind(this);
    this.deleteFactura = this.deleteFactura.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeCentruCost = this.onChangeCentruCost.bind(this);
    this.onChangeAprobator = this.onChangeAprobator.bind(this);
    this.changeSortOrder = this.changeSortOrder.bind(this);
    this.getStatusColor = this.getStatusColor.bind(this);

    this.state = {
      socsel: getSocSel(),
      factura: [],
      centreCostComponent: [],
      aprobatoriComponent: [],
      facturaComponent: [],

      isEdit: false,
      sortBy: 'data',
      sortAsc: true,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      id: '',
      denumirefurnizor: '',
      ciffurnizor: '',
      nr: '',
      data: '',
      moneda: '',
      sumafaratva: 0,
      termenscadenta: '',
      tipachizitie: '',
      descriereactivitati: '',
      aprobat: false,
      observatii: '',
      centrucost: '',
      numeaprobator: '',
      dataplatii: '',
      sumaachitata: '',
      idsocietate: null,
      show: false,

      idcentrucost: null,
      idaprobator: null,

      fisier: null,
      numefisier: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addFactura() {
    const formData = new FormData();
    if (this.state.numefisier) formData.append('fisier', this.state.fisier);

    const factura_body = {
      status: 'În așteptare',
      denumirefurnizor: this.state.denumirefurnizor || null,
      ciffurnizor: this.state.ciffurnizor || null,
      nr: this.state.nr || null,
      data: this.state.data || null,
      moneda: this.state.moneda || 'RON',
      sumafaratva: this.state.sumafaratva || null,
      termenscadenta: this.state.termenscadenta || null,
      tipachizitie: this.state.tipachizitie || null,
      descriereactivitati: this.state.descriereactivitati || null,
      idaprobator: this.state.idaprobator || null,
      aprobat: this.state.aprobat || false,
      observatii: this.state.observatii || null,
      idcentrucost: this.state.idcentrucost || null,
      dataplatii: this.state.dataplatii || null,
      sumaachitata: this.state.sumaachitata || null,
      idsocietate: this.state.socsel.id,
    };

    for (let key in factura_body) {
      if (factura_body[key]) formData.append(key, factura_body[key]);
    }

    let ok = await axios
      .post(`${server.address}/factura/`, formData, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga factura\n' + err.response.data.message,
        })
      );
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Factură adăugată cu succes',
        },
        this.onRefresh
      );
    }
  }

  async updateFactura(idfactura) {
    const formData = new FormData();
    var withFileUri = 'keep-file';
    if (this.state.sterge) withFileUri = 'new-file';
    if (this.state.fisier) {
      formData.append('fisier', this.state.fisier);
      withFileUri = 'new-file';
    }

    const factura_body = {
      status: 'În așteptare',
      denumirefurnizor: this.state.denumirefurnizor || null,
      ciffurnizor: this.state.ciffurnizor || null,
      nr: this.state.nr || null,
      data: this.state.data || null,
      moneda: this.state.moneda || null,
      sumafaratva: this.state.sumafaratva || null,
      termenscadenta: this.state.termenscadenta || null,
      tipachizitie: this.state.tipachizitie || null,
      descriereactivitati: this.state.descriereactivitati || null,
      idaprobator: this.state.idaprobator || null,
      aprobat: this.state.aprobat || null,
      observatii: this.state.observatii || null,
      codproiect: this.state.codproiect || null,
      idcentrucost: this.state.idcentrucost || null,
      dataplatii: this.state.dataplatii || null,
      sumaachitata: this.state.sumaachitata || null,
      idsocietate: this.state.socsel.id,
    };

    for (let key in factura_body) {
      if (factura_body[key]) formData.append(key, factura_body[key]);
    }

    const ok = await axios
      .put(`${server.address}/factura/${idfactura}/${withFileUri}`, formData, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza factura\n' + err.response.data.message,
        })
      );

    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Factură actualizată',
        },
        this.onRefresh
      );
    }
  }

  async editFactura(fact) {
    this.setState({
      isEdit: true,
      show: true,

      id: fact.id,
      denumirefurnizor: fact.denumirefurnizor || '',
      ciffurnizor: fact.ciffurnizor || '',
      nr: fact.nr || '',
      data: fact.data ? fact.data.substring(0, 10) : '',
      moneda: fact.moneda || '',
      sumafaratva: fact.sumafaratva || '',
      termenscadenta: fact.termenscadenta || '',
      tipachizitie: fact.tipachizitie || '',
      descriereactivitati: fact.descriereactivitati || '',
      numeaprobator: fact.aprobator
        ? fact.aprobator.persoana.nume + ' ' + fact.aprobator.persoana.prenume
        : '-',
      idaprobator: fact.aprobator ? fact.aprobator.persoana.id : null,
      aprobat: fact.aprobat,
      observatii: fact.observatii || '',
      codproiect: fact.codproiect || '',
      centrucost: fact.centrucost ? fact.centrucost : '-',
      idcentrucost: fact.centrucost ? fact.centrucost.id : null,
      dataplatii: fact.dataplatii || '',
      sumaachitata: fact.sumaachitata || '',

      numefisier: fact.numefisier || '',
      sterge: false,
    });
  }

  async deleteFactura(id) {
    await axios
      .delete(`${server.address}/factura/${id}`, { headers: authHeader() })
      .then(this.onRefresh)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge factura\n' + err.response.data.message,
        })
      );
    // if(ok) this.onRefresh();
  }

  getStatusColor(factStatus) {
    if (factStatus === 'Respinsă') return 'rgba(255,0,0,0.05)';
    if (factStatus === 'Aprobată') return 'rgba(0,255,0,0.05)';
    if (factStatus === 'Amânată') return 'rgba(191,191,63,0.05)';
  }

  // function to create react component with fetched data
  async renderFacturi() {
    const compare = (f1, f2) => {
      var sortBy = this.state.sortBy;
      var sortAsc = this.state.sortAsc;
      if (sortAsc) return f1[sortBy] < f2[sortBy] ? -1 : 1;
      else return f1[sortBy] > f2[sortBy] ? -1 : 1;
    };

    const facturi = this.state.factura.sort(compare);
    this.setState({
      facturaComponent: facturi.map((fact, index) => {
        return (
          // TODO
          <tr key={fact.id}>
            <th>
              <div className="d-flex">
                <Button
                  onClick={() => this.editFactura(fact)}
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
                            Sigur ștergeți factura {fact.dela} {fact.panala}?
                          </Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteFactura(fact.id);
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
            <th>
              {fact.status === 'În așteptare' ? (
                <i className="fa fa-circle text-c-gray f-10 mr-2" />
              ) : fact.status === 'Aprobată' ? (
                <i className="fa fa-circle text-c-green f-10 mr-2" />
              ) : fact.status === 'Respinsă' ? (
                <i className="fa fa-circle text-c-red f-10 mr-2" />
              ) : (
                <i className="fa fa-circle text-c-yellow f-10 mr-2" />
              )}
              {fact.status}
            </th>
            <th>{fact.codproiect ? fact.codproiect : '-'}</th>
            <th>{fact.observatii ? fact.observatii : '-'}</th>
            <th>{fact.denumirefurnizor}</th>
            <th>{fact.ciffurnizor}</th>
            <th>{fact.nr}</th>
            <th>{fact.data}</th>
            <th>{fact.moneda}</th>
            <th>{fact.sumafaratva}</th>
            <th>{fact.termenscadenta}</th>
            <th>{fact.tipachizitie}</th>
            <th>{fact.descriereactivitati}</th>
            <th>
              {fact.aprobator
                ? fact.aprobator.persoana.nume + ' ' + fact.aprobator.persoana.prenume
                : '-'}
            </th>
            <th>{fact.centrucost ? fact.centrucost.nume : '-'}</th>
            <th>{fact.dataplatii}</th>
            <th>{fact.sumaachitata}</th>
            <th>
              {fact.numefisier ? (
                <Button variant="link" onClick={() => downloadFactura(fact.numefisier, fact.id)}>
                  {fact.numefisier}
                </Button>
              ) : (
                'Niciun fișier încărcat'
              )}
            </th>
          </tr>
        );
      }),
    });
  }

  changeSortOrder(sortBy) {
    this.state.sortBy === sortBy
      ? this.setState({ sortAsc: !this.state.sortAsc }, this.renderFacturi)
      : this.setState({ sortBy: sortBy }, this.renderFacturi);
  }

  async onRefresh() {
    const centreCost = await axios
      .get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua centrele de cost\n' + err.response.data.message,
        })
      );
    const aprobatori = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}&u`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua aprobatorii\n' + err.response.data.message,
        })
      );
    const fact = await axios
      .get(`${server.address}/factura/idsoc/${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua facturile\n' + err.response.data.message,
        })
      );
    if (centreCost) {
      this.setState(
        {
          centreCostComponent: centreCost,
        },
        this.renderFacturi
      );
    }
    if (aprobatori) {
      this.setState(
        {
          aprobatoriComponent: aprobatori,
        },
        this.renderFacturi
      );
    }
    if (fact) {
      this.setState(
        {
          factura: fact,
        },
        this.renderFacturi
      );
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateFactura(this.state.id);
    else this.addFactura();
  }

  onChangeCentruCost(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idcentru = e.target.options[selectedIndex].getAttribute('data-key');
    this.setState({
      idcentrucost: idcentru,
      centrucost: e.target.value,
    });
  }

  onChangeAprobator(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idapb = e.target.options[selectedIndex].getAttribute('data-key');
    this.setState({
      idaprobator: idapb,
      numeaprobator: e.target.value,
    });
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      denumirefurnizor: '',
      ciffurnizor: '',
      nr: '',
      data: '',
      moneda: '',
      sumafaratva: 0,
      termenscadenta: '',
      tipachizitie: '',
      descriereactivitati: '',
      numeaprobator: '',
      aprobat: false,
      observatii: '',
      centrucost: '',
      dataplatii: '',
      sumaachitata: '',
      idsocietate: '',
      fisier: null,
      numefisier: null,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }

  render() {
    var centreCost = [];
    if (this.state.centreCostComponent.length > 0)
      centreCost = this.state.centreCostComponent.map((centrucost, index) => (
        <option key={index} data-key={centrucost.id}>
          {centrucost.nume}
        </option>
      ));

    var aprobatori = [];
    if (this.state.aprobatoriComponent.length > 0)
      aprobatori = this.state.aprobatoriComponent.map((angajat, index) => (
        <option key={index} data-key={angajat.persoana.id}>
          {angajat.persoana.nume + ' ' + angajat.persoana.prenume}
        </option>
      ));

    const handleChangeStatus = ({ file }, status) => {
      if (status === 'done') {
        console.log(status, file);
        this.setState({ fisier: file, numefisier: file.name });
      }
    };

    return (
      <Aux>
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
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Adaugă Factură</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addFactura}>
              <Row>
                <Form.Group as={Col} md="6" controlId="denumirefurnizor">
                  <Form.Label>Denumire Furnizor</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.denumirefurnizor}
                    onChange={(e) => this.setState({ denumirefurnizor: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>CIF Furnizor</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.ciffurnizor}
                    onChange={(e) => this.setState({ ciffurnizor: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Nr</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nr}
                    onChange={(e) => this.setState({ nr: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Data</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.data}
                    onChange={(e) => this.setState({ data: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
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
                <Form.Group as={Col} md="6">
                  <Form.Label>Sumă fără TVA</Form.Label>
                  <Form.Control
                    type="number"
                    value={this.state.sumafaratva}
                    onChange={(e) => this.setState({ sumafaratva: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Termen scadență</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.termenscadenta}
                    onChange={(e) => this.setState({ termenscadenta: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Tip Achiziție</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.tipachizitie}
                    onChange={(e) => this.setState({ tipachizitie: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Descriere Activități</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.descriereactivitati}
                    onChange={(e) => this.setState({ descriereactivitati: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Cod proiect</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.codproiect}
                    onChange={(e) => this.setState({ codproiect: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Observații</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.observatii || ''}
                    onChange={(e) => this.setState({ observatii: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Centru Cost</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.centrucost.nume}
                    onChange={this.onChangeCentruCost}
                  >
                    <option>-</option>
                    {centreCost}
                  </Form.Control>
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Aprobator</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.numeaprobator || '-'}
                    onChange={this.onChangeAprobator}
                  >
                    <option>-</option>
                    {aprobatori}
                  </Form.Control>
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Data Plății</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.dataplatii}
                    onChange={(e) => this.setState({ dataplatii: e.target.value })}
                  />
                </Form.Group>
                <Form.Group as={Col} md="6">
                  <Form.Label>Sumă Achitată</Form.Label>
                  <Form.Control
                    type="number"
                    value={this.state.sumaachitata}
                    onChange={(e) => this.setState({ sumaachitata: e.target.value })}
                  />
                </Form.Group>
                {/* file upload below */}
                <Form.Group as={Col} md="12">
                  <Form.Label>Factura</Form.Label>
                  {this.state.numefisier ? (
                    <div>
                      <Button
                        variant="dark"
                        onClick={() => downloadFactura(this.state.numefisier, this.state.id)}
                      >
                        {this.state.numefisier}
                      </Button>
                      <Button
                        variant="link"
                        onClick={() =>
                          this.setState({ fisier: undefined, numefisier: undefined, sterge: true })
                        }
                      >
                        Șterge
                      </Button>
                    </div>
                  ) : (
                    <Dropzone
                      inputLabel="Trage fișierul"
                      onChangeStatus={handleChangeStatus}
                      maxFiles={1}
                    />
                  )}
                </Form.Group>
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
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">Societăți</Breadcrumb.Item>
              <Breadcrumb.Item active>Facturi</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Facturi</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus size="25" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Status</th>
                      <th>Cod Proiect</th>
                      <th>Observații</th>
                      <th
                        onClick={() => this.changeSortOrder('denumirefurnizor')}
                        style={{ cursor: 'pointer' }}
                      >
                        Denumire Furnizor
                        {this.state.sortBy === 'denumirefurnizor'
                          ? this.state.sortAsc
                            ? ' ↓'
                            : ' ↑'
                          : ''}
                      </th>
                      <th>CIF Furnizor</th>
                      <th>Nr.</th>
                      <th
                        onClick={() => this.changeSortOrder('data')}
                        style={{ cursor: 'pointer' }}
                      >
                        Dată{this.state.sortBy === 'data' ? (this.state.sortAsc ? ' ↓' : ' ↑') : ''}
                      </th>
                      <th>Monedă</th>
                      <th>Sumă fără TVA</th>
                      <th>Termen Scadență</th>
                      <th>Tip Achiziție</th>
                      <th>Descriere Activități</th>
                      <th>Aprobator</th>
                      <th>Centru Cost</th>
                      <th>Data plății</th>
                      <th>Suma Achitată</th>
                      <th>Fișier atașat</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.facturaComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default FacturiTabel;
