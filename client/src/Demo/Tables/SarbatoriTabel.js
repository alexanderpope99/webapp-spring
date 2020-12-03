import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class SarbatoriTabel extends React.Component {
  constructor(props) {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addSarbatoare = this.addSarbatoare.bind(this);
    this.deleteSarbatoare = this.deleteSarbatoare.bind(this);
    this.setCurrentYear = this.setCurrentYear.bind(this);
    this.formatDate = this.formatDate.bind(this);

    this.state = {
      isEdit: false,

      sarbatori: [],
      sarbatoriComponent: null,

      an: '',

      // edit modal:
      show: false,
      id: '',
      dela: '',
      panala: '',
      nume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }

  componentDidMount() {
    this.setCurrentYear();
    this.fillTable();
    console.log(this.formatDate('2020-01-01'));
  }

  setCurrentYear() {
    let today = new Date();
    let an = today.getFullYear();

    this.setState({ an: an });
  }

  async fillTable() {
    //? fetch must be with idcontract
    const sarbatori = await axios
      .get(`${server.address}/sarbatori`, { headers: authHeader() })
      .then((res) => (res.status !== 200 ? null : res.data))
      .catch((err) => console.error('err', err));

    if (sarbatori) {
      this.setState(
        {
          sarbatori: sarbatori,
        },
        this.renderSarbatori
      );
    } else {
      this.setState(
        {
          sarbatori: [],
        },
        this.renderSarbatori
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      show: false,
      dela: '',
      panala: '',
      nume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    });
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        // reset data
        id: '',
        dela: '',
        panala: '',
        nume: '',
      });
  }

  async deleteSarbatoare(id) {
    await axios
      .delete(`${server.address}/sarbatori/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async addSarbatoare() {
    const sarbatoare_body = {
      dela: this.state.dela || null,
      panala: this.state.panala || null,
      nume: this.state.nume || null,
      // in DB also has sporuripermanente
    };

    let ok = false;
    if (this.state.isEdit) {
      ok = await axios
        .put(`${server.address}/sarbatori/${this.state.id}`, sarbatoare_body, {
          headers: authHeader(),
        })
        .then((res) => res.status === 200)
        .catch((err) => console.error('err:', err));
    } else {
      ok = await axios
        .post(`${server.address}/sarbatori`, sarbatoare_body, { headers: authHeader() })
        .then((res) => res.status === 200)
        .catch((err) => console.error('err:', err));
    }

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal
      this.setState({
        show_confirm: true,
        modalMessage:
          'Sărbătoarea ' +
          this.state.nume +
          (this.state.isEdit ? ' actualizată' : ' adăugată') +
          ' cu succes 💾',
      });

      this.fillTable();
    }
  }

  editSarbatoare(sarbatoare) {
    this.setState({
      isEdit: true,
      show: true,
      id: sarbatoare.id,
      dela: sarbatoare.dela,
      panala: sarbatoare.panala,
      nume: sarbatoare.nume,
    });
  }

  onChangeAn(an) {
    this.setState({ an: an }, this.renderSarbatori);
  }
  // function to create react component with fetched data
  renderSarbatori() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      sarbatoriComponent: this.state.sarbatori.map((sarbatoare, index) => {
        for (let key in sarbatoare) if (!sarbatoare[key]) sarbatoare[key] = '-';
        if (sarbatoare.dela.includes(this.state.an)) {
          return (
            <tr key={sarbatoare.id}>
              <th>{this.formatDate(sarbatoare.dela.substring(0, 10))}</th>
              <th>{this.formatDate(sarbatoare.panala.substring(0, 10))}</th>
              <th>{sarbatoare.nume}</th>
              <th className="d-inline-flex flex-row justify-content-around">
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  onClick={() => this.editSarbatoare(sarbatoare)}
                >
                  <Edit3 size={20} />
                </Button>
                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
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
                          <Typography>Sigur ștergeți sărbătoarea?</Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteSarbatoare(sarbatoare.id);
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
              </th>
            </tr>
          );
        }
      }),
    });
  }

  formatDate(date) {
    let luna = date.substring(5, 7);
    let ziua = date.substring(8, 10).match('[1-9][0-9]*');

    switch (luna) {
      case '01':
        luna = 'Ianuarie';
        break;
      case '02':
        luna = 'Februarie';
        break;
      case '03':
        luna = 'Martie';
        break;
      case '04':
        luna = 'Aprilie';
        break;
      case '05':
        luna = 'Mai';
        break;
      case '06':
        luna = 'Iunie';
        break;
      case '07':
        luna = 'Iulie';
        break;
      case '08':
        luna = 'August';
        break;
      case '09':
        luna = 'Septembrie';
        break;
      case '10':
        luna = 'Octombrie';
        break;
      case '11':
        luna = 'Noiembrie';
        break;
      case '12':
        luna = 'Decembrie';
        break;

      default:
        break;
    }

    return ziua + ' ' + luna;
  }

  render() {
    return (
      <Aux>
        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Sărbătoare nouă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  value={this.state.dela}
                  onChange={(e) => {
                    this.setState({ dela: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="panala">
                <Form.Label>Până la (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  value={this.state.panala}
                  onChange={(e) => {
                    this.setState({ panala: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="nume">
                <Form.Label>Nume</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.nume}
                  onChange={(e) => {
                    this.setState({ nume: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.addSarbatoare}>
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CONFIRM Modal */}
        <Modal show={this.state.show_confirm} onHide={() => this.handleClose(true)}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Sărbatori</Card.Title>

                <Button
                  variant="outline-primary"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>

                <Button
                  variant="outline-primary"
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
                >
                  Adaugă
                </Button>
              </Card.Header>
              <Card.Body>
                <Form.Group as={Col} sm="3">
                  <Form.Control
                    as="select"
                    value={this.state.an}
                    onChange={(e) => this.onChangeAn(e.target.value)}
                  >
                    <option>2019</option>
                    <option>2020</option>
                    <option>2021</option>
                  </Form.Control>
                </Form.Group>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Începând cu (inclusiv) ↓</th>
                      <th>Până la (inclusiv)</th>
                      <th>Nume</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.sarbatoriComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default SarbatoriTabel;
