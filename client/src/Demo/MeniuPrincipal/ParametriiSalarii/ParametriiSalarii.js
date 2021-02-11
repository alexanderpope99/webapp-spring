import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Button, Modal, Form, Toast } from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import { Trash2, Plus, RotateCw, Edit } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Popover from '@material-ui/core/Popover';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux/index';

import { server } from '../../Resources/server-address';

import authHeader from '../../../services/auth-header';

class ParametriiSalarii extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addParametrii = this.addParametrii.bind(this);
    this.updateParametrii = this.updateParametrii.bind(this);

    this.state = {
      parametriiSalarii: [],
      parametriiSalariiComponent: null,
      salariumin: '',
      salariuminstudiivechime: '',
      salariumediubrut: '',
      impozit: '',
      cas: '',
      cass: '',
      cam: '',
      valtichet: '',
      tva: '',
      show: false,
      date: '',
      isEdit: false,
      id: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    this.onRefresh();
  }

  deleteParametrii(id) {
    axios
      .delete(`${server.address}/parametriisalariu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      })
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putu șterge taxele și impozitele: ' + err.response.data.message,
        })
      );
  }

  getDate() {
    var today = new Date();
    var dd = today.getDate();

    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) {
      dd = '0' + dd;
    }

    if (mm < 10) {
      mm = '0' + mm;
    }
    return yyyy + '-' + mm + '-' + dd;
  }

  async onRefresh() {
    // e.preventDefault();

    let parametriiSalarii = await axios
      .get(`${server.address}/parametriisalariu/`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua taxele și impozitele: ' + err.response.data.message,
        })
      );

    console.log(parametriiSalarii);

    this.state.parametriiSalarii = parametriiSalarii;

    if (parametriiSalarii.length !== 0) {
      this.setState({
        parametriiSalarii: parametriiSalarii,
        salariumin: parametriiSalarii[0].salariumin,
        salariuminstudiivechime: parametriiSalarii[0].salariuminstudiivechime,
        salariumediubrut: parametriiSalarii[0].salariumediubrut,
        impozit: parametriiSalarii[0].impozit,
        cas: parametriiSalarii[0].cas,
        cass: parametriiSalarii[0].cass,
        cam: parametriiSalarii[0].cam,
        valtichet: parametriiSalarii[0].valtichet,
        tva: parametriiSalarii[0].tva,
        date: parametriiSalarii[0].date,
      });
    }
  }

  async addParametrii() {
    await axios
      .post(
        `${server.address}/parametriisalariu`,
        {
          salariumin: this.state.salariumin,
          salariuminstudiivechime: this.state.salariuminstudiivechime,
          salariumediubrut: this.state.salariumediubrut,
          impozit: this.state.impozit,
          cas: this.state.cas,
          cass: this.state.cass,
          cam: this.state.cam,
          valtichet: this.state.valtichet,
          tva: this.state.tva,
          date: this.state.date,
        },
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga parametrii: ' + err.response.data.message,
        })
      );
    this.onRefresh();
    this.setState({ show: false, isEdit: false });
  }

  async updateParametrii() {
    await axios
      .put(
        `${server.address}/parametriisalariu/${this.state.id}`,
        {
          salariumin: this.state.salariumin,
          salariuminstudiivechime: this.state.salariuminstudiivechime,
          salariumediubrut: this.state.salariumediubrut,
          impozit: this.state.impozit,
          cas: this.state.cas,
          cass: this.state.cass,
          cam: this.state.cam,
          valtichet: this.state.valtichet,
          tva: this.state.tva,
          date: this.state.date,
        },
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut modifica parametrii: ' + err.response.data.message,
        })
      );
    this.onRefresh();
    this.setState({ show: false, isEdit: false });
  }

  buttons = (cell, row, rowIndex, formatExtraData) => {
    return (
      <div className="d-inline-flex">
        <Button
          variant="outline-secondary"
          className="m-0 p-1 rounded-circle border-0"
          onClick={() => {
            this.setState({ isEdit: true, id: row.id, show: true });
          }}
        >
          <Edit fontSize="small" />
        </Button>
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
                  <Typography>Sigur ștergeți taxele și impozitele respective?</Typography>
                  <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                  <br />
                  <Button
                    variant="outline-danger"
                    onClick={() => {
                      popupState.close();
                      this.deleteParametrii(row.id);
                    }}
                    className="mt-2 "
                  >
                    Da
                  </Button>
                  <Button variant="outline-persondary" onClick={popupState.close} className="mt-2">
                    Nu
                  </Button>
                </Box>
              </Popover>
            </div>
          )}
        </PopupState>
      </div>
    );
  };

  render() {
    const columns = [
      {
        dataField: 'date',
        text: 'Începând cu',
        sort: true,
      },
      {
        dataField: 'salariumin',
        text: 'Sal. Minim (RON)',
        sort: true,
      },
      {
        dataField: 'salariuminstudiivechime',
        text: 'Sal. Minim - SS,V (RON)',
        sort: true,
      },
      {
        dataField: 'salariumediubrut',
        text: 'Sal. Mediu Brut (RON)',
        sort: true,
      },
      {
        dataField: 'impozit',
        text: 'Impozit (%)',
        sort: true,
      },
      {
        dataField: 'cas',
        text: 'CAS (%)',
        sort: true,
      },
      {
        dataField: 'cass',
        text: 'CASS (%)',
        sort: true,
      },
      {
        dataField: 'cam',
        text: 'CAM (%)',
        sort: true,
      },
      {
        dataField: 'valtichet',
        text: 'Val tichet (RON)',
        sort: true,
      },
      {
        dataField: 'tva',
        text: 'TVA (%)',
        sort: true,
      },
      {
        dataField: 'follow',
        formatter: this.buttons,
      },
    ];
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
        <Modal show={this.state.show} onHide={() => this.setState({ show: false, isEdit: false })}>
          <Modal.Header closeButton>
            <Modal.Title>Taxe și impozite</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="incepandcu">
                <Form.Label>Începând cu</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.date}
                  onChange={(e) => {
                    this.setState({ date: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumin">
                <Form.Label>Sal. Minim (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariumin}
                  onChange={(e) => {
                    this.setState({ salariumin: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariuminstudiivechime">
                <Form.Label>Sal. Minim - SS,V (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariuminstudiivechime}
                  onChange={(e) => {
                    this.setState({ salariuminstudiivechime: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>Sal. Mediu Brut (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariumediubrut}
                  onChange={(e) => {
                    this.setState({ salariumediubrut: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="impozit">
                <Form.Label>Impozit (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.impozit}
                  onChange={(e) => {
                    this.setState({ impozit: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cas">
                <Form.Label>CAS (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cas}
                  onChange={(e) => {
                    this.setState({ cas: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cass">
                <Form.Label>CASS (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cass}
                  onChange={(e) => {
                    this.setState({ cass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>CAM (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cam}
                  onChange={(e) => {
                    this.setState({ cam: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="valtichet">
                <Form.Label>Val. tichet (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.valtichet}
                  onChange={(e) => {
                    this.setState({ valtichet: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="tva">
                <Form.Label>TVA (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.tva}
                  onChange={(e) => {
                    this.setState({ tva: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            {this.state.isEdit ? (
              <Button variant="primary" onClick={this.updateParametrii}>
                Confirmă
              </Button>
            ) : (
              <Button variant="primary" onClick={this.addParametrii}>
                Confirmă
              </Button>
            )}
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Taxe și impozite</Card.Title>
                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                </Button>
                <Button
                  onClick={() => this.setState({ date: this.getDate(), show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus size="25" />
                </Button>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  wrapperClasses="table-responsive"
                  keyField="id"
                  data={this.state.parametriiSalarii}
                  columns={columns}
                  hover
                  bordered={false}
                />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default ParametriiSalarii;
