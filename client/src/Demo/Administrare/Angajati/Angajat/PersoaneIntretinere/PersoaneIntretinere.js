import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
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

class PersoaneIntretinereTabel extends React.Component {
  constructor() {
    super();

    this.fillTable = this.fillTable.bind(this);
    this.addPersoanaIntretinere = this.addPersoanaIntretinere.bind(this);
    this.updatePersoanaIntretinere = this.updatePersoanaIntretinere.bind(this);
    this.editPersoanaIntretinere = this.editPersoanaIntretinere.bind(this);
    this.deletePersoana = this.deletePersoana.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.getDatanasteriiByCNP = this.getDatanasteriiByCNP.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajat: getAngajatSel(),
      persoane: [],
      persoaneComponent: null,

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      show: false,
      nume: '',
      prenume: '',
      cnp: '',
      datanasterii: '',
      grad: 'Soț/Soție',
      gradinvaliditate: 'valid',
      intretinut: false,
      coasigurat: false,
      idangajat: null,

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

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
            toastMessage: 'Nu am prelua angajatul: ' + err.response.data.message,
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

  async addPersoanaIntretinere() {
    const persoana_body = {
      nume: this.state.nume || null,
      prenume: this.state.prenume || null,
      cnp: this.state.cnp || null,
      datanasterii: this.state.datanasterii || null,
      grad: this.state.grad || null,
      gradinvaliditate: this.state.gradinvaliditate.toLowerCase() || null,
      coasigurat: this.state.coasigurat,
      intretinut: this.state.intretinut,
      idangajat: this.state.angajat.idpersoana,
    };

    let ok = await axios
      .post(`${server.address}/persoanaintretinere`, persoana_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga persoana întreținere: ' + err.response.data.message,
        })
      );
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Persoana întreținută adaugată pentru ' + this.state.angajat.numeintreg,
        },
        this.fillTable
      );
    }
  }

  async updatePersoanaIntretinere(idpers) {
    const persoana_body = {
      nume: this.state.nume || null,
      prenume: this.state.prenume || null,
      cnp: this.state.cnp || null,
      datanasterii: this.state.datanasterii || null,
      grad: this.state.grad || null,
      gradinvaliditate: this.state.gradinvaliditate.toLowerCase() || null,
      coasigurat: this.state.coasigurat,
      intretinut: this.state.intretinut,
      idangajat: this.state.angajat.idpersoana || null,
    };

    const ok = await axios
      .put(`${server.address}/persoanaintretinere/${idpers}`, persoana_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut modifica persoana întreținere: ' + err.response.data.message,
        })
      );

    if (ok) {
      this.fillTable();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Persoană in intretinere actualizată',
      });
    }
  }

  async editPersoanaIntretinere(pers) {
    this.setState({
      isEdit: true,
      show: true,

      id: pers.id,
      nume: pers.nume,
      prenume: pers.prenume,
      cnp: pers.cnp || '',
      datanasterii: pers.datanasterii ? pers.datanasterii.substring(0, 10) : '',
      grad: pers.grad,
      gradinvaliditate: pers.gradinvaliditate,
      coasigurat: pers.coasigurat,
      intretinut: pers.intretinut,
      idangajat: pers.idangajat,
    });
  }

  async deletePersoana(id) {
    axios
      .delete(`${server.address}/persoanaintretinere/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge persoana întreținere: ' + err.response.data.message,
        })
      );
  }

  getDatanasteriiByCNP(cnp) {
    // console.log('getDatanasteriiByCNP called |', cnp);
    if (!cnp) return '';

    if (cnp.length > 6) {
      const an = cnp.substring(1, 3);
      const luna = cnp.substring(3, 5);
      const zi = cnp.substring(5, 7);
      if (cnp[0] <= 2) return `19${an}-${luna}-${zi}`;
      else return `20${an}-${luna}-${zi}`;
    } else return '';
  }

  onChangeCnp(e) {
    const cnp = e.target.value;
    this.setState({
      cnp: cnp,
      datanasterii: this.getDatanasteriiByCNP(cnp),
    });
  }

  // function to create react component with fetched data
  async fillTable() {
    if (this.state.angajat) {
      const persoane = await axios
        .get(`${server.address}/persoanaintretinere/ida=${this.state.angajat.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua persoanele întreținere: ' + err.response.data.message,
          })
        );
      if (persoane) {
        this.setState(
          {
            persoane: persoane,
          },
          this.renderPersoane
        );
      }
    } else {
      this.setState({
        persoane: [],
        persoaneComponent: null,
      });
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updatePersoanaIntretinere(this.state.id);
    else this.addPersoanaIntretinere();
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      nume: '',
      prenume: '',
      cnp: '',
      datanasterii: '',
      grad: '',
      gradinvaliditate: 'valid',
      intretinut: false,
      coasigurat: false,
      idangajat: null,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }
  renderPersoane() {
    this.setState({
      persoaneComponent: this.state.persoane.map((pers, index) => {
        return (
          // TODO
          <tr key={pers.id}>
            <th>{pers.nume || '-'}</th>
            <th>{pers.prenume || '-'}</th>
            <th>{pers.cnp || '-'}</th>
            <th>{pers.grad}</th>
            <th>{pers.gradinvaliditate}</th>
            <th>{pers.coasigurat ? 'DA' : 'NU'}</th>
            <th>{pers.intretinut ? 'DA' : 'NU'}</th>
            <th>
              <Row>
                <Button
                  onClick={() => this.editPersoanaIntretinere(pers)}
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
                          <Typography>
                            Sigur ștergeți persoana {pers.nume} {pers.prenume}?
                          </Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deletePersoana(pers.id, pers.nume, pers.prenume);
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

  render() {
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
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Detalii persoană întreținere</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addPersoanaIntretinere}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>Nume</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.nume}
                      onChange={(e) => this.setState({ nume: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Preume</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.prenume}
                      onChange={(e) => this.setState({ prenume: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>CNP</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.cnp}
                      onChange={(e) => this.onChangeCnp(e)}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Data nasterii</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.datanasterii}
                      onChange={(e) => this.setState({ datanasterii: e.target.value })}
                    />
                  </Form.Group>
                  <Row>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Grad</Form.Label>
                        <Form.Control
                          as="select"
                          value={this.state.grad}
                          onChange={(e) => this.setState({ grad: e.target.value })}
                        >
                          <option>Soț/Soție</option>
                          <option>Părinte</option>
                          <option>Copil</option>
                          <option>Altă rudă</option>
                        </Form.Control>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Grad Invaliditate</Form.Label>
                        <Form.Control
                          as="select"
                          value={this.state.gradinvaliditate}
                          onChange={(e) => this.setState({ gradinvaliditate: e.target.value })}
                        >
                          <option>Valid</option>
                          <option>Invalid</option>
                        </Form.Control>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="ml-3">
                        <Form.Check
                          custom
                          type="switch"
                          id="coasiguratCheck"
                          label="Coasigurat"
                          checked={this.state.coasigurat}
                          onChange={(e) => this.setState({ coasigurat: e.target.checked })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="ml-3">
                        <Form.Check
                          custom
                          type="switch"
                          id="intretinutCheck"
                          label="Intretinut"
                          checked={this.state.intretinut}
                          onChange={(e) => this.setState({ intretinut: e.target.checked })}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
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
            <Modal.Title>Detalii persoană întreținere</Modal.Title>
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
                <Card.Title as="h5">Persoane Întreținere</Card.Title>

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
                  onClick={() => this.setState({ isEdit: false, show: true })}
                >
                  <Plus className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>CNP</th>
                      <th>Grad</th>
                      <th>Grad Invaliditate</th>
                      <th>Coasigurat</th>
                      <th>întreținut</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.persoaneComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default PersoaneIntretinereTabel;
