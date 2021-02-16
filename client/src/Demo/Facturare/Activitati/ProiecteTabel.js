import React from 'react';
import axios from 'axios';

import { Row, Col, Card, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, Edit3, Plus } from 'react-feather';
import BootstrapTable from 'react-bootstrap-table-next';
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

    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);

    this.state = {
      showConfirm: false,
      modalMessage: '',

      showModal: false,
      isEdit: false,

      showModalProiect: false,
      isEditProiect: false,

      socsel: getSocSel(),

      proiecte: [],

      id: null,
      nume: '',
    };
  }

  edit(item) {
    this.setState({
      showConfirm: false,
      modalMessage: '',

      showModal: true,
      isEdit: true,
      id: item.id,
      nume: item.nume,
    });
  }

  async delete(index) {
		var produse = this.state.produse;
    const ok = await axios
      .delete(`${server.address}/proiect/${produse[index].id}`, { headers: authHeader() })
      .then((res) => res.status === '200')
      .catch((err) =>
        this.props.showError('Nu am putut sterge proiectul: ' + err.response.data.message)
      );
		if(ok) {
			produse.splice(index, 1);
		}
  }

  async onSubmit() {
    const proiect_body = {
      nume: this.state.nume,
    };
    var proiect = false;
    if (this.state.isEdit) {
      proiect['id'] = this.state.id;
      proiect = await axios
        .put(`${server.address}/proiect/${this.state.id}`, proiect_body, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) =>
          this.props.showError('Nu am putut modifica proiectul: ' + err.response.data.message)
        );
    } else {
      proiect = await axios
        .post(`${server.address}/proiect/ida=${this.state.socsel.id}`, proiect_body, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
          this.props.showError('Nu am putut adauga proiectul: ' + err.response.data.message)
        );
    }

    if (proiect) {
      this.setState(
        {
          showModal: false,

          showConfirm: true,
          modalMessage: this.state.isEdit ? 'Proiect modificat' : 'Proiect adăugat',
					proiecte: [...this.state.proiecte, proiect],
        },
        this.handleClose
      );
    }
  }

  clearUserInput() {
    this.setState({
      id: null,
      nume: '',
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
                <Typography>Sigur ștergeți proiectul?</Typography>
                <Typography variant="caption">Facturile nu se vor sterge.</Typography>
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
			}
    ];

    return (
      <Aux>
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
        <Modal show={this.state.showModal} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Detalii activitate</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>Nume activitate</Form.Label>
              <Form.Control
                type="text"
                value={this.state.nume}
                onChange={(e) => this.setState({ nume: e.target.value })}
              />
            </Form.Group>
            {this.state.isEdit ? (
              <BootstrapTable
                bootstrap4
                keyField="idpersoana"
                data={this.props.proiecte}
                columns={columns}
                wrapperClasses="table-responsive"
                hover
                bordered={false}
              />
            ) : null}
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
                <Card.Title as="h5">Proiecte</Card.Title>
                <Button
                  variant="outline-primary"
                  size="sm"
                  className="float-right"
                  onClick={() => this.setState({ showModal: true })}
                >
                  <Plus />
                </Button>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={this.props.proiecte}
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
