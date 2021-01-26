import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Plus, Trash2, Edit3, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class PlatiContTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addPlataCont = this.addPlataCont.bind(this);
    this.updatePlataCont = this.updatePlataCont.bind(this);
    this.editPlataCont = this.editPlataCont.bind(this);
    this.deletePlataCont = this.deletePlataCont.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.renderModal = this.renderModal.bind(this);

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      platiCont: [],
      platiContComponent: null,

      isEdit: false,
      an: '',

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      id: '',
      idcontbancar: null,
      contBancar: null,
      pentru: '',
      show: false,

      today: '',
      angajat: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel() || authService.isAngajatSimplu())
      window.location.href = '/dashboard/societati';

    let today = new Date();
    this.setState({
      today: today.toISOString().substring(0, 10),
    });
    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addPlataCont() {
    const plataCont_body = {
      idcontbancar: this.state.idcontbancar || null,
      pentru: this.state.pentru || null,
    };

    let ok = await axios
      .post(`${server.address}/platicont`, plataCont_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga plată cont\n' + err.response.data.message,
        })
      );
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Plată Cont adăugată ',
        },
        this.onRefresh
      );
    }
  }

  async updatePlataCont(idPlataCont) {
    const plataCont_body = {
      idcontbancar: this.state.idcontbancar,
      pentru: this.state.pentru,
    };

    const ok = await axios
      .put(`${server.address}/platacont/${idPlataCont}`, plataCont_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza plată cont\n' + err.response.data.message,
        })
      );

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Plată Cont actualizată',
      });
    }
  }

  async editPlataCont(plata) {
    this.setState({
      isEdit: true,
      show: true,

      id: plata.id,
      idcontbancar: plata.idcontbancar,
      contBancar: plata.contBancar,
      pentru: plata.pentru,
    });
  }

  async deletePlataCont(id) {
    await axios
      .delete(`${server.address}/platicont/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge plăți cont\n' + err.response.data.message,
        })
      );
  }

  // function to create react component with fetched data
  async renderPlataCont() {
    this.setState({
      platiContComponent: await Promise.all(
        this.state.platiCont.map(async (plata, index) => {
          return (
            // TODO
            <tr key={plata.id}>
              <th>{plata.contBancar}</th>
              <th>{plata.pentru}</th>
              <th>
                <Row>
                  <Button
                    onClick={() => this.editPlataCont(plata)}
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
                            <Typography>Sigur ștergeți plata cont?</Typography>
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
                            <br />
                            <Button
                              variant="outline-danger"
                              onClick={() => {
                                popupState.close();
                                this.deletePlataCont(plata.id);
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
        })
      ),
    });
  }

  async onRefresh() {
    const platiCont = await axios
      .get(`${server.address}/platicont/`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua plățile cont\n' + err.response.data.message,
        })
      );
    if (platiCont) {
      this.setState(
        {
          platiCont: platiCont,
        },
        this.renderPlataCont
      );
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updatePlataCont(this.state.id);
    else this.addPlataCont();
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      idcontbancar: null,
      contBancar: '',
      pentru: '',
    });
  }

  renderModal() {
    this.setState({
      isEdit: false,
      show: true,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
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
            <Modal.Title>Plată Cont</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addPlataCont}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>Cont Bancar</Form.Label>
                    <Form.Control
                      required
                      type="text"
                      value={this.state.contBancar}
                      onChange={(e) =>
                        this.setState({ contBancar: e.target.value }, this.setNrZile)
                      }
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Pentru (iban)</Form.Label>
                    <Form.Control
                      type="text"
                      required
                      value={this.state.pentru}
                      onChange={(e) => this.setState({ pentru: e.target.value })}
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
                <Card.Title as="h5">
                  {this.state.socsel ? this.state.socsel.nume + ' - ' : ''}Plăți Cont
                </Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={this.renderModal}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus size="25" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Din (Cod)</th>
                      <th>Pentru (Cod)</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.platiContComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default PlatiContTabel;
