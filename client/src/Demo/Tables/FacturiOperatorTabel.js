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
} from 'react-bootstrap';
import { X, Check, Clock, RotateCw } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { downloadFactura } from '../Resources/download';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';
import 'react-dropzone-uploader/dist/styles.css';
import Dropzone from 'react-dropzone-uploader';

class FacturiOperatorTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeCentruCost = this.onChangeCentruCost.bind(this);
    this.onChangeAprobator = this.onChangeAprobator.bind(this);
    this.changeSortOrder = this.changeSortOrder.bind(this);
    this.approveFactura = this.approveFactura.bind(this);
    this.rejectFactura = this.rejectFactura.bind(this);
    this.postponeFactura = this.postponeFactura.bind(this);
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
      user: authService.getCurrentUser(),

      idcentrucost: null,
      idaprobator: null,

      fisier: null,
      numefisier: '',
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
      .catch((err) => console.error(err));
    if (ok) this.onRefresh();
  }

  async rejectFactura(fact) {
    const ok = await axios
      .get(`${server.address}/factura/${fact.id}/respinge`, { headers: authHeader() })
      .then((response) => response.status === 200)
      .catch((err) => console.error(err));
    if (ok) this.onRefresh();
  }

  async postponeFactura(fact) {
    console.log(`${server.address}/factura/${fact.id}/amana`);
    const ok = await axios
      .get(`${server.address}/factura/${fact.id}/amana`, { headers: authHeader() })
      .then((response) => response.status === 200)
      .catch((err) => console.error(err));
    if (ok) this.onRefresh();
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
            <tr style={{ backgroundColor: this.getStatusColor(fact.status) }} key={fact.id}>
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
              <th>{fact.aprobat}</th>
              <th>{fact.observatii}</th>
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
      .catch((err) => console.error(err));
    const aprobatori = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}&c`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    const fact = await axios
      .get(`${server.address}/factura/idsoc/approved/${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));
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
      centreCost = this.state.centreCostComponent.map((cod, index) => (
        <option key={index} data-key={cod.id}>
          {cod.nume}
        </option>
      ));

    var aprobatori = [];
    if (this.state.aprobatoriComponent.length > 0)
      aprobatori = this.state.aprobatoriComponent.map((cod, index) => (
        <option key={index} data-key={cod.persoana.id}>
          {cod.persoana.nume + ' ' + cod.persoana.prenume}
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
                    <option key="1">RON</option>
                    <option key="2">EUR</option>
                    <option key="3">USD</option>
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
                  <Form.Label>Observații</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.observatii}
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
                    <Dropzone onChangeStatus={handleChangeStatus} maxFiles={1} />
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
              <Breadcrumb.Item active>Operare Facturi</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Operare Facturi Aprobate</Card.Title>

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
                      <th>Aprobat</th>
                      <th>Observații</th>
                      <th>Centru Cost</th>
                      <th>Data plății</th>
                      <th>Suma Achitată</th>
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

export default FacturiOperatorTabel;
