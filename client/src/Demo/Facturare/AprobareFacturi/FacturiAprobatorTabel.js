import React from 'react';
import {
  Row,
  Col,
  Card,
  Table,
  Button,
  OverlayTrigger,
  Tooltip,
  Modal,
  Form,
  Breadcrumb,
  Toast,
} from 'react-bootstrap';
import { X, Check, Clock, RotateCw } from 'react-feather';

import Aux from '../../../hoc/_Aux';
import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { downloadFactura } from '../../Resources/download';
import axios from 'axios';
import authHeader from '../../../services/auth-header';
import authService from '../../../services/auth.service';
import 'react-dropzone-uploader/dist/styles.css';

class FacturiAprobatorTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseApprover = this.handleCloseApprover.bind(this);
    this.handleRejectApprover = this.handleRejectApprover.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeCentruCost = this.onChangeCentruCost.bind(this);
    this.onChangeAprobator = this.onChangeAprobator.bind(this);
    this.changeSortOrder = this.changeSortOrder.bind(this);
    this.approveFactura = this.approveFactura.bind(this);
    this.rejectFactura = this.rejectFactura.bind(this);
    this.postponeFactura = this.postponeFactura.bind(this);
    this.getStatusColor = this.getStatusColor.bind(this);
    this.exitCloseApprover = this.exitCloseApprover.bind(this);

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

      // approver modal
      showApprover: false,
      showRejecter: false,

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
      codproiect: '',
      idsocietate: null,
      show: false,
      user: authService.getCurrentUser(),

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

  async approveFactura(fact) {
    const ok = await axios
      .get(`${server.address}/factura/${fact.id}/aproba`, { headers: authHeader() })
      .then((response) => response.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut aproba factura: ' + err.response.data.message,
        })
      );
    if (ok) {
      this.onRefresh();
      this.setState({
        showApprover: true,
        id: fact.id,
        codproiect: fact.codproiect || '',
        observatii: fact.observatii || '',
      });
    }
  }

  async rejectFactura(fact) {
    const ok = await axios
      .get(`${server.address}/factura/${fact.id}/respinge`, { headers: authHeader() })
      .then((response) => response.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut rejecta factura: ' + err.response.data.message,
        })
      );
    if (ok) {
      this.onRefresh();
      this.setState({
        showRejecter: true,
        id: fact.id,
        codproiect: fact.codproiect || '',
        observatii: fact.observatii || '',
      });
    }
  }

  async postponeFactura(fact) {
    console.log(`${server.address}/factura/${fact.id}/amana`);
    const ok = await axios
      .get(`${server.address}/factura/${fact.id}/amana`, { headers: authHeader() })
      .then((response) => response.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut amâna factura: ' + err.response.data.message,
        })
      );
    if (ok) {
      this.onRefresh();
      this.setState({
        showApprover: true,
        id: fact.id,
        codproiect: fact.codproiect || '',
        observatii: fact.observatii || '',
      });
    }
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
      facturaComponent: await Promise.all(
        facturi.map(async (fact, index) => {
          return (
            // TODO
            <tr key={fact.id}>
              <th>
                <div className="d-flex">
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Respinge
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.rejectFactura(fact)}
                      variant="outline-danger"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <X size={20} />
                    </Button>
                  </OverlayTrigger>
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="postpone-button" style={{ opacity: '.4' }}>
                        Amână
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.postponeFactura(fact)}
                      variant="outline-warning"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <Clock size={20} />
                    </Button>
                  </OverlayTrigger>
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Acceptă
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.approveFactura(fact)}
                      variant="outline-success"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <Check size={20} />
                    </Button>
                  </OverlayTrigger>
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
                  'Niciun fisier încărcat'
                )}
              </th>
            </tr>
          );
        })
      ),
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
          toastMessage: 'Nu am putut prelua centrele de cost: ' + err.response.data.message,
        })
      );

    const aprobatori = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}&c`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua aprobatorii: ' + err.response.data.message,
        })
      );

    const fact = await axios
      .get(`${server.address}/factura/idsocuid/${this.state.socsel.id}&${this.state.user.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua facturile: ' + err.response.data.message,
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

  exitCloseApprover() {
    this.setState({
      showApprover: false,
      showRejecter: false,
    });
  }

  async handleCloseApprover(e) {
    const factura_body = {
      codproiect: this.state.codproiect || null,
      observatii: this.state.observatii || null,
    };
    const ok = await axios
      .put(`${server.address}/factura/${this.state.id}/obs&codp`, factura_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut introduce observații și cod proiect: ' + err.response.data.message,
        })
      );
    if (ok) {
      this.onRefresh();
      this.setState({
        showApprover: false,
        observatii: '',
        codproiect: '',
      });
    }
  }

  async handleRejectApprover(e) {
    const factura_body = {
      codproiect: null,
      observatii: this.state.observatii || null,
    };
    const ok = await axios
      .put(`${server.address}/factura/${this.state.id}/obs&codp`, factura_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut introduce observații și cod proiect PDF: ' + err.response.data.message,
        })
      );
    if (ok) {
      this.onRefresh();
      this.setState({
        showRejecter: false,
        observatii: '',
        codproiect: '',
      });
    }
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
      codproiect: '',
      centrucost: '',
      dataplatii: '',
      sumaachitata: '',
      idsocietate: '',
      fisier: null,
      numefisier: null,
    });
  }

  render() {
    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
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

        {/* Approver Info modal */}
        <Modal show={this.state.showApprover} onHide={this.exitCloseApprover}>
          <Modal.Header closeButton>
            <Modal.Title>Aprobator</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Form.Group as={Col} md="12">
                <Form.Label>Cod Proiect</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codproiect}
                  onChange={(e) => this.setState({ codproiect: e.target.value })}
                />
              </Form.Group>
              <Form.Group as={Col} md="12">
                <Form.Label>Observații</Form.Label>
                <Form.Control
                  as="textarea"
                  value={this.state.observatii}
                  onChange={(e) => this.setState({ observatii: e.target.value })}
                />
              </Form.Group>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleCloseApprover}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Reject Info modal */}
        <Modal show={this.state.showRejecter} onHide={this.exitCloseApprover}>
          <Modal.Header closeButton>
            <Modal.Title>Aprobator</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Form.Group as={Col} md="6">
                <Form.Label>Observații</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.observatii}
                  onChange={(e) => this.setState({ observatii: e.target.value })}
                />
              </Form.Group>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleRejectApprover}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">{this.state.socsel.nume}</Breadcrumb.Item>
              <Breadcrumb.Item active>Aprobare Facturi</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Aprobare Facturi</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                  {/* ↺ */}
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

export default FacturiAprobatorTabel;
