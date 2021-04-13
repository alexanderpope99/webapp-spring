import React from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

import { Row, Col, Card, Button, Toast, Modal, Form, Breadcrumb } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw, Plus } from 'react-feather';
import BootstrapTable from 'react-bootstrap-table-next';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux/index';

import { getSocSel } from '../../Resources/socsel';
import { server } from '../../Resources/server-address';
import authHeader from '../../../services/auth-header';

export default class CaieteTabel extends React.Component {
  constructor() {
    super();

    this.getCaiete = this.getCaiete.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.showError = this.showError.bind(this);
    this.renderTable = this.renderTable.bind(this);

    if(!getSocSel()) {
      window.location.href = '/dashboard/societati';
      return;
    }

    this.state = {
      showToast: false,
      toastMessage: '',
      showConfirm: false,
      modalMessage: '',

      showModal: false,
      isEdit: false,

      socsel: getSocSel(),

      caiete: [],

      societati: [],
      societate: { id: null, nume: '' },

      id: null,
      serie: '',
      primulNumar: '',
      ultimulNumar: '',
      status: 'ACTIV',
    };
  }

  componentDidMount() {
    this.getCaiete();
    this.getSocietati();
  }

  showError(error) {
    this.setState({
      showToast: true,
      toastMessage: error,
    });
  }

  async getSocietati() {
    const societati = await axios
      .get(`${server.address}/societate`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.showError('Nu am putut prelua societatile: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'))
      );
    if (societati) {
      this.setState({ societati: societati });
    }
  }

  async getCaiete() {
    const caiete = await axios
      .get(`${server.address}/caiet`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => this.showError('Nu am putut prelua caietele: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server')));
    if (caiete) {
      this.setState({ caiete: caiete });
    }
  }

  renderTable(noTimeout) {
    if (noTimeout === 'no-timeout') {
      this.getCaiete();
    } else {
      this.setState({ caiete: [] });
      setTimeout(this.getCaiete, 100);
    }
  }

  edit(item) {
    this.setState({
      showConfirm: false,
      modalMessage: '',

      showModal: true,
      isEdit: true,
      id: item.id,
      serie: item.serie,
      primulNumar: item.primulnumar,
      ultimulNumar: item.ultimulnumar,
      status: item.status,
      societate: item.societate,
    });
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/caiet/${id}`, { headers: authHeader() })
      .then(this.getCaiete)
      .catch((err) => this.showError('Nu am putut sterge caietul: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server')));
  }

  clearUserInput() {
    this.setState({
      id: null,
      serie: '',
      primulNumar: '',
      ultimulNumar: '',
      status: 'ACTIV',
      societate: { id: null, nume: '' },
    });
  }

  handleClose(type) {
    if (type === 'confirm') {
      this.setState(
        {
          showConfirm: false,
          modalMessage: '',
          isEdit: false,
        },
        this.clearUserInput
      );
    } else {
      this.setState({ showModal: false, isEdit: false }, this.clearUserInput);
    }
  }

  buttons = (cell, row) => (
    <div className="d-inline-flex">
      <Button
        onClick={() => this.edit(row)}
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
                <Typography>Sigur ștergeți caietul?</Typography>
                <Typography variant="caption">Facturile nu se vor șterge.</Typography>
                <br />
                <Button
                  variant="outline-danger"
                  onClick={() => {
                    popupState.close();
                    this.delete(row.id);
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

  onChangeSocietate(e) {
    if (e.target.value === '-') {
      this.setState({ societate: { id: null, nume: '' } });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const societate = this.state.societati[selectedIndex - 1];
    this.setState({ societate: societate ? societate : { id: null, nume: '' } });
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.state.societate.id) {
      this.showError('Selectați o societate');
      return;
    }

    const caiet = {
      serie: this.state.serie,
      primulnumar: this.state.primulNumar,
      ultimulnumar: this.state.ultimulNumar,
      status: this.state.status,
    };
    var ok = false;
    if (this.state.isEdit) {
      ok = await axios
        .put(`${server.address}/caiet/ids=${this.state.societate.id}/${this.state.id}`, caiet, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
          this.showError('Nu am putut modifica caietul: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server').message)
        );
    } else {
      ok = await axios
        .post(`${server.address}/caiet/ids=${this.state.socsel.id}`, caiet, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) => this.showError('Nu am putut adauga caietul: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server').message));
    }

    if (ok) {
      this.setState(
        {
          showToast: false,
          toastMessage: '',
          showModal: false,

          showConfirm: true,
          modalMessage: this.state.isEdit ? 'Caiet modificat' : 'Caiet adăugat',
        },
        this.getCaiete
      );
    }
  }

  render() {
    const columns = [
      {
        dataField: 'societate.nume',
        text: 'Societate',
        sort: true,
      },
      {
        dataField: 'serie',
        text: 'Serie',
        sort: true,
      },
      {
        dataField: 'primulnumar',
        text: 'Primul număr',
        sort: true,
      },
      {
        dataField: 'ultimulnumar',
        text: 'Ultimul număr',
        sort: true,
      },
      {
        dataField: 'status',
        text: 'Status',
        sort: true,
      },
      {
        dataField: 'actiuni',
        text: 'Acțiuni',
        formatter: this.buttons,
      },
    ];

    const societatiComponent = this.state.societati.map((societate) => (
      <option key={societate.id}>{societate.nume}</option>
    ));

    return (
      <Aux>
        {/* ERROR TOAST */}
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

        {/* CONFIRM MODAL */}
        <Modal show={this.state.showConfirm} onHide={() => this.handleClose('confirm')}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" onClick={() => this.handleClose('confirm')}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.showModal} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Detalii caiet</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Form.Group>
                <Form.Label>Societatea</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.societate.nume}
                  onChange={(e) => this.onChangeSocietate(e)}
                >
                  <option>-</option>
                  {societatiComponent}
                </Form.Control>
              </Form.Group>
              <Form.Group>
                <Form.Label>Serie</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.serie}
                  onChange={(e) => this.setState({ serie: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Primul număr</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.primulNumar}
                  onChange={(e) => this.setState({ primulNumar: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Ultimul număr</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.ultimulNumar}
                  onChange={(e) => this.setState({ ultimulNumar: e.target.value })}
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Status</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.status}
                  onChange={(e) => this.setState({ status: e.target.value })}
                >
                  <option>ACTIV</option>
                  <option>DEZACTIVAT</option>
                  <option>INCHIS</option>
                </Form.Control>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.onSubmit}>
              {this.state.isEdit ? 'Modifică' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* TABLE */}
        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item linkAs={Link} linkProps={{ to: '/dashboard/societati' }}>
                {this.state.socsel.nume}
              </Breadcrumb.Item>
              <Breadcrumb.Item active>Caiete</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Caiete - {this.state.socsel.nume}</Card.Title>
                <Button
                  variant="outline-primary"
                  size="sm"
                  className="float-right"
                  onClick={() => this.setState({ showModal: true })}
                >
                  <Plus />
                </Button>

                <Button
                  variant="outline-primary"
                  size="sm"
                  className="float-right"
                  onClick={this.renderTable}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={this.state.caiete}
                  columns={columns}
                  wrapperClasses="table-responsive"
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
