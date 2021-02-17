import React from 'react';
import axios from 'axios';

import { Row, Col, Card, Button, Breadcrumb, Toast, Modal, Form } from 'react-bootstrap';
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

export default class ActivitatiTabel extends React.Component {
  constructor() {
    super();

    this.getActivitati = this.getActivitati.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.showError = this.showError.bind(this);
    this.renderActivitati = this.renderActivitati.bind(this);

    this.state = {
      showToast: false,
      toastMessage: '',
      showConfirm: false,
      modalMessage: '',

      showModal: false,
      isEdit: false,

      showModalProiect: false,
      isEditProiect: false,

      socsel: getSocSel(),

      activitati: [],

      id: null,
      nume: '',
      proiecte: [],

      proiect_id: '',
      proiect_nume: '',
    };
  }

  componentDidMount() {
    this.getActivitati();
  }

  showError(error) {
    this.setState({
      showToast: true,
      toastMessage: error,
    });
  }

  async getActivitati() {
    const activitati = await axios
      .get(`${server.address}/activitate/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.showError('Nu am putut prelua activitatile: ' + err.response.data.message)
      );
    if (activitati) {
      this.setState({ activitati: activitati });
    }
  }

  renderActivitati(noTimeout) {
    if (noTimeout === 'no-timeout') {
      this.getActivitati();
    } else {
      this.setState({ activitati: [] });
      setTimeout(this.getActivitati, 100);
    }
  }

  edit(item) {
    this.setState({
      showConfirm: false,
      modalMessage: '',

      showModal: true,
			isEdit: true,
      id: item.id,
      nume: item.nume,
      proiecte: item.proiecte,
    });
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/activitate/${id}`, { headers: authHeader() })
      .then(this.getActivitati)
      .catch((err) =>
        this.showError('Nu am putut sterge activitatea: ' + err.response.data.message)
      );
  }

  async onSubmit(e) {
		e.preventDefault();

    const activitate = {
      nume: this.state.nume,
    };
    var ok = false;
    if (this.state.isEdit) {
      activitate['id'] = this.state.id;
      ok = await axios
        .put(`${server.address}/activitate/${this.state.id}`, activitate, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.showError('Nu am putut modifica activitatea: ' + err.response.data.message)
        );
    } else {
      ok = await axios
        .post(`${server.address}/activitate/ids=${this.state.socsel.id}`, activitate, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.showError('Nu am putut adauga activitatea: ' + err.response.data.message)
        );
    }

    if (ok) {
      this.setState(
        {
          showToast: false,
          toastMessage: '',
          showModal: false,

          showConfirm: true,
          modalMessage: this.state.isEdit ? 'Activitate modificată' : 'Activitate adăugată',
        },
        this.getActivitati
      );
    }
  }

  clearUserInput() {
    this.setState({
      id: null,
      nume: '',
      proiecte: [],
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
      this.setState({ showModal: false, isEdit: false, }, this.clearUserInput);
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
                <Typography>Sigur ștergeți activitatea?</Typography>
                <Typography variant="caption">
                  Se vor șterge și proiectele aferente activității. Facturile nu se vor șterge.
                </Typography>
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

  render() {
    const columns = [
      {
        dataField: '',
        text: '#',
        formatter: (cell, row, rowIndex) => rowIndex + 1,
      },
      {
        dataField: 'nume',
        text: 'Nume',
        sort: true,
      },
      {
        dataField: 'actiuni',
        text: '',
        formatter: this.buttons,
      },
    ];

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
        <Modal show={this.state.showConfirm} onHide={() => this.hanldeClose('confirm')}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" onClick={() => this.handleClose('confirm')}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* ADD/EDIT ACTIVITATE MODAL */}
        <Modal show={this.state.showModal} onHide={this.handleClose} size="sm">
          <Modal.Header closeButton>
            <Modal.Title>Detalii activitate</Modal.Title>
          </Modal.Header>
          <Modal.Body>
						<Form onSubmit={this.onSubmit}>
            <Form.Group>
              <Form.Label>Nume activitate</Form.Label>
              <Form.Control
                type="text"
                value={this.state.nume}
                onChange={(e) => this.setState({ nume: e.target.value })}
              />
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
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Activitati</Card.Title>
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
                  onClick={this.renderActivitati}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={this.state.activitati}
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
