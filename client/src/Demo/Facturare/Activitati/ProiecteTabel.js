import React from 'react';
import axios from 'axios';

import { Row, Col, Card, Button, Toast, Modal, Form } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw, Plus } from 'react-feather';
import BootstrapTable from 'react-bootstrap-table-next';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux/index';

import { getSocSel } from '../../Resources/socsel';
import { server } from '../../Resources/server-address';
import authHeader from '../../../services/auth-header';

export default class ProiecteTabel extends React.Component {
  constructor() {
    super();

    this.init = this.init.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.init = this.init.bind(this);
    this.getProiecte = this.getProiecte.bind(this);
    this.renderProiecte = this.renderProiecte.bind(this);

    this.state = {
      showToast: false,
      toastMessage: '',
      showConfirm: false,
      modalMessage: '',

      showModal: false,
      isEdit: false,

      socsel: getSocSel(),

      activitati: [],
      proiecte: [],
			filter: null,

      activitate: { id: null, nume: '' },
      id: null,
      nume: '',
    };
  }

  componentDidMount() {
    this.init();
  }

  async init() {
    await this.getActivitati();
    this.getProiecte();
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

  async getProiecte() {
    const proiecte = await axios
      .get(`${server.address}/proiect/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.showError('Nu am putut prelua proiectele: ' + err.response.data.message)
      );
    if (proiecte) {
      this.setState({ proiecte: proiecte });
    }
  }

  renderProiecte(noTimeout) {
    if (noTimeout === 'no-timeout') {
      this.init();
    } else {
      this.setState({ proiecte: [] });
      setTimeout(this.init, 100);
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
      activitate: item.activitate,
    });
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/proiect/${id}`, { headers: authHeader() })
      .then(this.getActivitati)
      .catch((err) => this.showError('Nu am putut sterge proiectul: ' + err.response.data.message));
  }

  async onSubmit(e) {
    e.preventDefault();
    const proiect = {
      nume: this.state.nume,
    };
    if (!this.state.activitate.id) {
      this.showError('Selectați activitatea proiectului');
      return;
    }
    var ok = false;
    if (this.state.isEdit) {
      proiect['id'] = this.state.id;
      ok = await axios
        .put(
          `${server.address}/proiect/ida=${this.state.activitate.id}/${this.state.id}`,
          proiect,
          { headers: authHeader() }
        )
        .then((res) => res.data)
        .catch((err) =>
          this.showError('Nu am putut modifica proiectul: ' + err.response.data.message)
        );
    } else {
      ok = await axios
        .post(`${server.address}/proiect/ida=${this.state.activitate.id}`, proiect, {
          headers: authHeader(),
        })
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
          modalMessage: this.state.isEdit ? 'Proiect modificat' : 'Proiect adăugat',
        },
        this.getProiecte
      );
    }
  }

  clearUserInput() {
    this.setState({
      id: null,
      nume: '',
      activitate: { id: null, nume: '' },
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
    } else if (type === 'proiect') {
      this.setState({
        showModalProiect: false,
        isEdit: false,
      });
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
                <Typography>Sigur ștergeți proiectul?</Typography>
                <Typography variant="caption">SFacturile nu se vor șterge.</Typography>
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

  onChangeActivitate(e) {
    if (e.target.value === '-') {
      this.setState({ activitate: { id: null, nume: '' } });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const id = e.target.options[selectedIndex].getAttribute('data-key');
    // eslint-disable-next-line eqeqeq
    const activitate = this.state.activitati.find((c) => c.id == id);
    this.setState({ activitate: activitate ? activitate : { id: null, nume: '' } });
  }

	filterProiecteByActivitate() {
		if(!this.state.filter)
			return this.state.proiecte;
		return this.state.proiecte.filter(proiect => proiect.activitate.nume === this.state.filter);
	}

  render() {
		const proiecteFiltered = this.filterProiecteByActivitate();

    const columns = [
      {
        dataField: '',
        text: '#',
        formatter: (cell, row, rowIndex) => rowIndex + 1,
      },
      {
        dataField: 'nume',
        text: 'Nume proiect',
        sort: true,
      },
      {
        dataField: 'activitate.nume',
        text: 'Nume activitate',
      },
      {
        dataField: 'actiuni',
        text: 'Acțiuni',
        formatter: this.buttons,
      },
    ];

    const activitatiComponent = this.state.activitati.map((activitate) => (
      <option key={activitate.id} data-key={activitate.id}>
        {activitate.nume}
      </option>
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

        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.showModal} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Nume proiect</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Form.Group>
                <Form.Control
                  as="select"
                  value={this.state.activitate.nume}
                  onChange={(e) => this.onChangeActivitate(e)}
                >
                  <option>-</option>
                  {activitatiComponent}
                </Form.Control>
              </Form.Group>
              <Form.Group>
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
                  onClick={this.renderProiecte}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
								<Row>
									<Form.Group as={Col} sm="auto" className="mt-3 mb-0">
										<Form.Control
											as="select"
											value={this.state.activitateFilter}
											onChange={e => this.setState({filter: e.target.value === 'Toate activitățile' ? null : e.target.value})}
										>
											<option>Toate activitățile</option>
											{activitatiComponent}
										</Form.Control>
									</Form.Group>
								</Row>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={proiecteFiltered}
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
