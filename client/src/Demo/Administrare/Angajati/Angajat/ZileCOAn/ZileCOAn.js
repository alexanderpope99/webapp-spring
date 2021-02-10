import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Edit3, Plus, RotateCw, Trash2 } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../../../../hoc/_Aux/index';
import { server } from '../../../../Resources/server-address';
import { getSocSel } from '../../../../Resources/socsel';
import { getAngajatSel } from '../../../../Resources/angajatsel';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';

class ZileCOAn extends React.Component {
  constructor() {
    super();

    this.fillTable = this.fillTable.bind(this);
    this.addZileCOAn = this.addZileCOAn.bind(this);
    this.updateZileCOAn = this.updateZileCOAn.bind(this);
    this.editZileCOAn = this.editZileCOAn.bind(this);
    this.deleteZileCOAn = this.deleteZileCOAn.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.renderZileCOAn = this.renderZileCOAn.bind(this);
    this.openModalAdd = this.openModalAdd.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajat: getAngajatSel(),
      zilecoan: [],
      zilecoanComponent: null,

      an_sel: '-',

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      show: false,
      id: null,
      idangajat: null,
      an: '',
      zilecoefectuat: '',
      zileconeefectuat: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';
    this.setState({ an_sel: new Date().getFullYear() });
    this.fillTable();
    window.scrollTo(0, 0);
  }

  async updateAngajatSel() {
    let angajatSel = getAngajatSel();
    if (angajatSel) {
      let angajat = await axios
        .get(`${server.address}/angajat/${angajatSel.idpersoana}`, { headers: authHeader() })
        .then((res) => (res.status === 200 ? res.data : null))
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua angajatul\n' + err.response.data.message,
          })
        );
      if (angajat)
        this.setState(
          { angajat: { ...angajat, numeintreg: getAngajatSel().numeintreg } },
          this.fillTable
        );
    } else {
      this.setState({ angajat: null }, this.fillTable);
    }
  }

  async addZileCOAn() {
    const zilecoan_body = {
      idangajat: this.state.angajat.idpersoana,
      an: this.state.an || null,
      zilecoefectuat: this.state.zilecoefectuat || null,
      zileconeefectuat: this.state.zileconeefectuat || null,
    };

    let ok = await axios
      .post(`${server.address}/zilecoan`, zilecoan_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga zilele de concediu\n' + err.response.data.message,
        })
      );

    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Zile concediu adaugate pentru ' + this.state.angajat.numeintreg,
        },
        this.fillTable
      );
    }
  }

  async updateZileCOAn(idzilecoan) {
    const zilecoan_body = {
      idangajat: this.state.angajat.idpersoana,
      an: this.state.an || null,
      zilecoefectuat: this.state.zilecoefectuat || null,
      zileconeefectuat: this.state.zileconeefectuat || null,
    };

    const ok = await axios
      .put(`${server.address}/zilecoan/${idzilecoan}`, zilecoan_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza zilele de concediu\n' + err.response.data.message,
        })
      );

    if (ok) {
      this.fillTable();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Zile Concediu actualizate',
      });
    }
  }

  async editZileCOAn(zc) {
    this.setState({
      isEdit: true,
      show: true,

      id: zc.id,
      idangajat: zc.idangajat,
      an: zc.an,
      zilecoefectuat: zc.zilecoefectuat,
      zileconeefectuat: zc.zileconeefectuat,
    });
  }

  async deleteZileCOAn(idzilecoan) {
    axios
      .delete(`${server.address}/zilecoan/${idzilecoan}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge zile concediu\n' + err.response.data.message,
        })
      );
  }

  // function to create react component with fetched data
  renderZileCOAn() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      zilecoanComponent: this.state.zilecoan.map((zc, index) => {
        // eslint-disable-next-line eqeqeq
          return (
            <tr key={zc.id}>
              <th>{zc.an}</th>
              <th>{zc.zilecoefectuat}</th>
              <th>{zc.zileconeefectuat}</th>
              <th>
                <Row>
                  <Button
                    onClick={() => this.editZileCOAn(zc)}
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
                            <Typography>Confirmare ștergere</Typography>
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
                            <br />
                            <Button
                              variant="outline-danger"
                              onClick={() => {
                                popupState.close();
                                this.deleteZileCOAn(zc.id);
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
                </Row>
              </th>
            </tr>
          );
      }),
    });
  }

  async fillTable() {
    if (this.state.angajat) {
      const zilecoan = await axios
        .get(`${server.address}/zilecoan/ida=${this.state.angajat.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua zilele concediu\n' + err.response.data.message,
          })
        );
      if (zilecoan) {

        this.setState(
          {
            zilecoan: zilecoan,
          },
          this.renderZileCOAn
        );
      }
    } else {
      this.setState({
        zilecoan: [],
        zilecoanComponent: null,
        ani_cu_baza: [],
      });
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateZileCOAn(this.state.id);
    else this.addZileCOAn();
  }

  openModalAdd() {
    this.setState({
      isEdit: false,
      show: true,
      an: this.state.an_sel,
    });
  }

  async handleClose() {
    this.setState({
      show: false,
      idangajat: null,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
      an: '-',
      zilecoefectuat: '',
      zileconeefectuat: '',
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
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Zile CO An</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addZileCOAn}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.an}
                      onChange={(e) => this.setState({ an: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Zile CO Efectuat</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.zilecoefectuat}
                      onChange={(e) => this.setState({ zilecoefectuat: e.target.value })}
                    />
                  </Form.Group>
				  <Form.Group>
                    <Form.Label>Zile CO Neefectuat</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.zileconeefectuat}
                      onChange={(e) => this.setState({ zileconeefectuat: e.target.value })}
                    />
                  </Form.Group>
                </Col>
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
            <Modal.Title>Zile CO An</Modal.Title>
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
                <Card.Title as="h5">Zile CO An</Card.Title>

                <Button
                  variant={this.state.angajat ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!this.state.angajat}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  variant={this.state.angajat ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!this.state.angajat}
                  onClick={this.openModalAdd}
                >
                  <Plus className="m-0 p-0" />
                </Button>
                <Row>
                </Row>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>An</th>
                      <th>Zile CO Efectuate</th>
                      <th>Zile CO Neefectuate</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.zilecoanComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default ZileCOAn;
