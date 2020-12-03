import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class CereriConcediuTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addCerereConcediu = this.addCerereConcediu.bind(this);
    this.updateCerereConcediu = this.updateCerereConcediu.bind(this);
    this.editCerereConcediu = this.editCerereConcediu.bind(this);
    this.deleteCerere = this.deleteCerere.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),
      cereriConcediu: [],
      cereriConcediuComponent: null,

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      id: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
      status: '',
      show: false,
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async addCerereConcediu() {
    let pentruId = await axios
      .get(`${server.address}/angajat/userid/${JSON.parse(localStorage.getItem('user')).id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    const cerereConcediu_body = {
      pentru: pentruId || null,
      dela: this.state.dela || null,
      panala: this.state.panala || null,
      tip: this.state.tip || null,
      motiv: this.state.motiv || null,
      societate: getSocSel().id,
      status: 'Propus',
    };

    let ok = await axios
      .post(`${server.address}/cerericoncediu`, cerereConcediu_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Cerere adăugată cu succes',
        },
        this.onRefresh
      );
    }
  }

  async updateCerereConcediu(idcerere) {
    let pentruId = await axios
      .get(`${server.address}/angajat/userid/${JSON.parse(localStorage.getItem('user')).id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    const cerereConcediu_body = {
      pentru: pentruId,
      dela: this.state.dela,
      panala: this.state.panala,
      tip: this.state.tip,
      motiv: this.state.motiv,
      societate: getSocSel().id,
      status: 'Propus (Modificat)',
    };

    console.log(idcerere + ' ' + this.state.tip);

    const ok = await axios
      .put(`${server.address}/cerericoncediu/${idcerere}`, cerereConcediu_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Cerere Concediu actualizată',
      });
    }
  }

  async editCerereConcediu(cer) {
    console.log(cer);
    this.setState({
      isEdit: true,
      show: true,

      id: cer.id,
      pentru: cer.pentru,
      tip: cer.tip,
      motiv: cer.motiv,
      dela: cer.dela ? cer.dela.substring(0, 10) : '',
      panala: cer.panala ? cer.panala.substring(0, 10) : '',
    });
  }

  async deleteCerere(id) {
    await axios
      .delete(`${server.address}/cerericoncediu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
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
              <th>
                {
                  await axios
                    .get(
                      `${server.address}/cerericoncediu/zilelucratoareintre?date1=${cer.dela}&date2=${cer.panala}`,
                      { headers: authHeader() }
                    )
                    .then((response) => response.data)
                    .catch((err) => console.error(err))
                }
              </th>
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
        `${server.address}/cerericoncediu/usersoc/${JSON.parse(localStorage.getItem('user')).id}&${
          getSocSel().id
        }`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));
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
      tip: '',
      motiv: '',
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
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Cerere Concediu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addCerereConcediu}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>De la</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.dela}
                      onChange={(e) => this.setState({ dela: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Până la</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.panala}
                      onChange={(e) => this.setState({ panala: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Tip</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.tip}
                      onChange={(e) => this.setState({ tip: e.target.value })}
                    />
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
                <Card.Title as="h5">Cereri Concediu</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Plus className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Status</th>
                      <th>De la</th>
                      <th>Până la</th>
                      <th>Zile</th>
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
