import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import Add from '@material-ui/icons/Add';
import Refresh from '@material-ui/icons/Refresh';
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

    this.state = {
      socsel: getSocSel(),
      factura: [],
      centreCostComponent: [],
      facturaComponent: null,

      isEdit: false,

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
      idaprobator: null,
      aprobat: false,
      observatii: '',
      centrucost: '',
      dataplatii: '',
      sumaachitata: '',
      idsocietate: null,
      show: false,

      idcentrucost: null,

      fisier: null,
      numefisier: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addFactura() {
    const formData = new FormData();
    if (this.state.fisier) formData.append('fisier', this.state.fisier);

    const factura_body = {
      denumirefurnizor: this.state.denumirefurnizor || null,
      ciffurnizor: this.state.ciffurnizor || null,
      nr: this.state.nr || null,
      data: this.state.data || null,
      moneda: this.state.moneda || 'RON',
      sumafaratva: this.state.sumafaratva || null,
      termenscadenta: this.state.termenscadenta || null,
      tipachizitie: this.state.tipachizitie || null,
      descriereactivitati: this.state.descriereactivitati || null,
      idaprobator: null,
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

    // for (var pair of formData.entries()) {
    //   console.log(pair[0] + ', ' + pair[1]);
    // }

    let ok = await axios
      .post(
        `${server.address}/factura/${this.state.fisier ? 'file' : ''}`,
        this.state.fisier ? formData : factura_body,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));
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
    if (this.state.fisier) {
      formData.append('fisier', this.state.fisier);
    }

    const factura_body = {
      denumirefurnizor: this.state.denumirefurnizor || null,
      ciffurnizor: this.state.ciffurnizor || null,
      nr: this.state.nr || null,
      data: this.state.data || null,
      moneda: this.state.moneda || null,
      sumafaratva: this.state.sumafaratva || null,
      termenscadenta: this.state.termenscadenta || null,
      tipachizitie: this.state.tipachizitie || null,
      descriereactivitati: this.state.descriereactivitati || null,
      idaprobator: null,
      aprobat: this.state.aprobat || null,
      observatii: this.state.observatii || null,
      idcentrucost: this.state.idcentrucost || null,
      dataplatii: this.state.dataplatii || null,
      sumaachitata: this.state.sumaachitata || null,
      idsocietate: this.state.socsel.id,
    };

    for (let key in factura_body) {
      if (factura_body[key]) formData.append(key, factura_body[key]);
    }

    // for (var pair of formData.entries()) {
    //   console.log(pair[0] + ', ' + pair[1]);
    // }

    const ok = await axios
      .put(`${server.address}/factura/${idfactura}`, formData, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

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
      denumirefurnizor: fact.denumirefurnizor,
      ciffurnizor: fact.ciffurnizor,
      nr: fact.nr,
      data: fact.data ? fact.data.substring(0, 10) : '',
      moneda: fact.moneda,
      sumafaratva: fact.sumafaratva,
      termenscadenta: fact.termenscadenta,
      tipachizitie: fact.tipachizitie,
      descriereactivitati: fact.descriereactivitati,
      idaprobator: fact.idaprobator,
      aprobat: fact.aprobat,
      observatii: fact.observatii,
      centrucost: fact.centrucost ? fact.centrucost : '-',
      idcentrucost: fact.centrucost ? fact.centrucost.id : null,
      dataplatii: fact.dataplatii,
      sumaachitata: fact.sumaachitata,

			fisier: {name: fact.numefisier, size: fact.dimensiunefisier},
			numefisier: fact.numefisier,
    }, () => console.log(this.state));
  }

  async deleteFactura(id) {
    await axios
      .delete(`${server.address}/factura/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .catch((err) => console.error(err));
  }

  // function to create react component with fetched data
  async renderFacturi() {
    this.setState({
      facturaComponent: await Promise.all(
        this.state.factura.map(async (fact, index) => {
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
                    <Edit fontSize="small" />
                  </Button>

                  <PopupState variant="popover" popupId="demo-popup-popover">
                    {(popupState) => (
                      <div>
                        <Button
                          variant="outline-secondary"
                          className="m-1 p-1 rounded-circle border-0"
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
                            <Typography>
                              Sigur ștergeți factura {fact.dela} {fact.panala}?
                            </Typography>
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
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
                  'Niciun fisier âncarcat'
                )}
              </th>
            </tr>
          );
        })
      ),
    });
  }

  async onRefresh() {
    const centreCost = await axios
      .get(`${server.address}/centrucost/idsoc/${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    const fact = await axios
      .get(`${server.address}/factura/idsoc/${this.state.socsel.id}`, {
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
      idaprobator: null,
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

    const handleChangeStatus = ({ file }, status) => {
      if (status === 'done') {
        console.log(status, file);
        this.setState({ fisier: file });
      }
    };

    // const getUploadParams = ({file, meta}) => {
    // 	this.setState({fisier: file});
    // }

    // const handleSubmit = (files, allFiles) => {
    // 	console.log(files[0].meta);
    // 	allFiles.forEach(f => f.remove());
    // }
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
									{this.state.numefisier ? 
									<div>
										<Button variant="dark" onClick={() => downloadFactura(this.state.numefisier, this.state.id)}>
											{this.state.numefisier}
										</Button>
										<Button variant="link" onClick={() => this.setState({fisier: undefined, numefisier: undefined})}>Șterge</Button>
									</div>
                  : <Dropzone
                    onChangeStatus={handleChangeStatus}
                    // getUploadParams={getUploadParams}
                    // onSubmit={handleSubmit}
                    maxFiles={1}
                  />
									}
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
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Facturi</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Add className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Denumire Furnizor</th>
                      <th>CIF Furnizor</th>
                      <th>Nr.</th>
                      <th>Dată</th>
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

export default FacturiTabel;
