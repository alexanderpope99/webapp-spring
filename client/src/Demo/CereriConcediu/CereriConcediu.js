import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
import Aux from '../../hoc/_Aux/index';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { countWeekendDays } from '../Resources/cm';
import { tip_concedii } from '../Resources/tip-concedii';

import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';


const tip_concedii_component = tip_concedii.map((tip, index) => <option key={index}>{tip}</option>);

class CereriConcediuTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addCerereConcediu = this.addCerereConcediu.bind(this);
    this.updateCerereConcediu = this.updateCerereConcediu.bind(this);
    this.editCerereConcediu = this.editCerereConcediu.bind(this);
    this.deleteCerere = this.deleteCerere.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.renderModal = this.renderModal.bind(this);
    this.setNrZile = this.setNrZile.bind(this);

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      cereriConcediu: [],
      cereriConcediuComponent: null,

      isEdit: false,
      an: '',

      zile_co_disponibile: 21,
      nr_zile: '',
      nr_zile_weekend: '',

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      id: '',
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',
      motiv: '',
      status: '',
      show: false,

      today: '',
      angajat: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    let today = new Date();
    this.setState({
      today: today.toISOString().substring(0, 10),
    });
    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addCerereConcediu() {
    const cerereConcediu_body = {
      dela: this.state.dela || null,
      panala: this.state.panala || null,
      tip: this.state.tip || null,
      motiv: this.state.motiv || null,
      idsocietate: this.state.socsel.id,
      iduser: this.state.user.id,
      status: 'Propus',
    };

    let ok = await axios
      .post(`${server.address}/cerericoncediu`, cerereConcediu_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga cereri concediu ' + err.response.data.message,
        })
      );
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Cerere adăugată ',
        },
        this.onRefresh
      );
    }
  }

  async updateCerereConcediu(idcerere) {
    const cerereConcediu_body = {
      dela: this.state.dela,
      panala: this.state.panala,
      tip: this.state.tip,
      motiv: this.state.motiv,
      idsocietate: this.state.socsel.id,
      iduser: this.state.user.id,
      status: 'Propus (Modificat)',
    };

    console.log(idcerere + ' ' + this.state.tip);

    const ok = await axios
      .put(`${server.address}/cerericoncediu/${idcerere}`, cerereConcediu_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza cereri concediu ' + err.response.data.message,
        })
      );

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Cerere Concediu actualizată',
      });
    }
  }

  async getZileCoDisponibile() {
    const angajat = await axios
      .get(`${server.address}/angajat/socid=${this.state.socsel.id}/usrid=${this.state.user.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua angajatul ' + err.response.data.message,
        })
      );
    this.setState({
      angajat: angajat,
    });
    await axios
      .get(`${server.address}/co/zilecodisponibile/idc=${angajat.contract.id}`, {
        headers: authHeader(),
      })
      .then((res) => this.setState({ zile_co_disponibile: res.data }))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut prelua zilele de concediu disponibile ' + err.response.data.message,
        })
      );
  }

  setNrZile() {
    var nr_zile = 0,
      nr_zile_weekend = 0;
    if (this.state.dela && this.state.panala) {
      let date1 = new Date(this.state.dela);
      let date2 = new Date(this.state.panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
      nr_zile_weekend = countWeekendDays(date1, date2);
    }
    this.setState({ nr_zile: nr_zile, nr_zile_weekend: nr_zile_weekend });
  }

  async editCerereConcediu(cer) {
    console.log(cer);
    this.setState(
      {
        isEdit: true,
        show: true,

        id: cer.id,
        tip: cer.tip,
        motiv: cer.motiv,
        dela: cer.dela ? cer.dela.substring(0, 10) : '',
        panala: cer.panala ? cer.panala.substring(0, 10) : '',
      },
      this.setNrZile
    );
  }

  async deleteCerere(id) {
    await axios
      .delete(`${server.address}/cerericoncediu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge cererea ' + err.response.data.message,
        })
      );
  }

  // function to create react component with fetched data
  async renderCereri() {
    this.setState({
      cereriConcediuComponent: await Promise.all(
        this.state.cereriConcediu.map(async (cer, index) => {
          return (
            // TODO
            <tr key={cer.id}>
              <th>
                {cer.status === 'Propus' || cer.status === 'Propus (Modificat)' ? (
                  <i className="fa fa-circle text-c-gray f-10 mr-2" />
                ) : cer.status === 'Aprobat' ? (
                  <i className="fa fa-circle text-c-green f-10 mr-2" />
                ) : (
                  <i className="fa fa-circle text-c-red f-10 mr-2" />
                )}
                {cer.status}
              </th>
              <th>{cer.dela || '-'}</th>
              <th>{cer.panala}</th>
              <th>{cer.tip}</th>
              <th>{cer.motiv}</th>
              <th>
                <Row>
                  <Button
                    onClick={() => this.editCerereConcediu(cer)}
                    variant="outline-secondary"
                    className="m-1 p-1 rounded-circle border-0"
                    disabled={cer.status === 'Aprobat' || cer.status === 'Respins'}
                  >
                    <Edit3 size={20} />
                  </Button>

                  <PopupState variant="popover" popupId="demo-popup-popover">
                    {(popupState) => (
                      <div>
                        <Button
                          variant="outline-secondary"
                          className="m-1 p-1 rounded-circle border-0"
                          disabled={cer.status === 'Aprobat' || cer.status === 'Respins'}
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
                            <Typography>
                              Sigur ștergeți cererea {cer.dela} {cer.panala}?
                            </Typography>
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
                            <br />
                            <Button
                              variant="outline-danger"
                              onClick={() => {
                                popupState.close();
                                this.deleteCerere(cer.id);
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
    const cereriConcediu = await axios
      .get(
        `${server.address}/cerericoncediu/usersoc/${this.state.user.id}&${this.state.socsel.id}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua cererile de concediu ' + err.response.data.message,
        })
      );
    if (cereriConcediu) {
      this.setState(
        {
          cereriConcediu: cereriConcediu,
        },
        this.renderCereri
      );
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateCerereConcediu(this.state.id);
    else this.addCerereConcediu();
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      pentru: '',
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',
      motiv: '',
    });
  }

  renderModal() {
    this.setState(
      {
        isEdit: false,
        show: true,
        dela: this.state.today,
        panala: this.state.today,
      },
      () => {
        this.getZileCoDisponibile();
        this.setNrZile();
      }
    );
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }

  render() {
    const concediuIsValid = this.state.dela && this.state.dela <= this.state.panala;

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
            <Modal.Title>Cerere Concediu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addCerereConcediu}>
              <Form.Group id="zilecodisponibile">
                <Form.Label>
                  {this.state.zile_co_disponibile} zile concediu de odihnă disponibile
                </Form.Label>
              </Form.Group>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>Începând cu</Form.Label>
                    <Form.Control
                      required
                      type="date"
                      value={this.state.dela}
                      max={this.state.panala === this.state.dela ? null : this.state.panala}
                      onChange={(e) => this.setState({ dela: e.target.value }, this.setNrZile)}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Până la</Form.Label>
                    <Form.Control
                      type="date"
                      required
                      value={this.state.panala}
                      min={this.state.dela}
                      onChange={(e) => this.setState({ panala: e.target.value }, this.setNrZile)}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Tip</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.tip}
                      onChange={(e) => this.setState({ tip: e.target.value })}
                    >
                      {tip_concedii_component}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Motiv</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.motiv}
                      onChange={(e) => this.setState({ motiv: e.target.value })}
                    />
                  </Form.Group>
                </Col>
              </Row>
              <Form.Group>
                <Form.Label>
                  {this.state.nr_zile +
                    (this.state.nr_zile > 1
                      ? ` zile concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`
                      : ` zi concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`)}
                </Form.Label>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.onSubmit}
              type="submit"
              disabled={!concediuIsValid}
            >
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
                  {this.state.socsel ? this.state.socsel.nume + ' - ' : ''}Cereri Concediu
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
                      <th>Status</th>
                      <th>De la</th>
                      <th>Până la</th>
                      <th>Tip</th>
                      <th>Motiv</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.cereriConcediuComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default CereriConcediuTabel;
