import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import Add from '@material-ui/icons/Add';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel } from '../Resources/angajatsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class PersoaneIntretinereTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);
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
      angajatsel: getAngajatSel(),
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
      grad: '',
      gradinvaliditate: 'valid',
      intretinut: false,
      coasigurat: false,
      idangajat: null,
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  updateAngajatSel() {
    this.setState({ angajatsel: getAngajatSel() });
  }

  async addPersoanaIntretinere() {
    const persoana_body = {
      nume: this.state.nume,
      prenume: this.state.prenume,
      cnp: this.state.cnp,
      datanasterii: this.state.datanasterii,
      grad: this.state.grad,
      gradinvaliditate: this.state.gradinvaliditate.toLowerCase(),
      coasigurat: this.state.coasigurat,
      intretinut: this.state.intretinut,
      idangajat: this.state.angajatsel.idpersoana,
    };

    let ok = await axios
      .post(`${server.address}/persoanaintretinere`, persoana_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Persoana întreținută adaugată pentru ' + this.state.angajatsel.numeintreg,
        },
        this.onRefresh
      );
    }
  }

  async updatePersoanaIntretinere(idpers) {
    const persoana_body = {
      nume: this.state.nume,
      prenume: this.state.prenume,
      cnp: this.state.cnp,
      datanasterii: this.state.datanasterii,
      grad: this.state.grad,
      gradinvaliditate: this.state.gradinvaliditate.toLowerCase(),
      coasigurat: this.state.coasigurat,
      intretinut: this.state.intretinut,
      idangajat: this.state.angajatsel.idpersoana,
    };

    const ok = await axios
      .put(`${server.address}/persoanaintretinere/${idpers}`, persoana_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Persoană in intretinere actualizată',
      });
    }
  }

  async editPersoanaIntretinere(pers) {
    console.log(pers);
    this.setState({
      isEdit: true,
      show: true,

      id: pers.id,
      nume: pers.nume,
      prenume: pers.prenume,
      cnp: pers.cnp,
      datanasterii: pers.datanasterii ? pers.datanasterii.substring(0, 10) : '',
      grad: pers.grad,
      gradinvaliditate: pers.gradinvaliditate,
      coasigurat: pers.coasigurat,
      intretinut: pers.intretinut,
      idangajat: pers.idangajat,
    });
  }

  async deletePersoana(id, nume, prenume) {
    axios
      .delete(`${server.address}/persoanaintretinere/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  onChangeCnp(e) {
    const cnp = e.target.value;
    this.setState({
      cnp: cnp,
      datanasterii: this.getDatanasteriiByCNP(cnp),
    });
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

  // function to create react component with fetched data
  renderPersoane() {
    this.setState({
      persoaneComponent: this.state.persoane.map((pers, index) => {
        return (
          // TODO
          <tr key={pers.id}>
            <th>{pers.nume || '-'}</th>
            <th>{pers.prenume || '-'}</th>
            <th>{pers.cnp}</th>
            <th>{pers.grad}</th>
            <th>{pers.gradinvaliditate}</th>
            <th>{pers.intretinut ? 'DA' : 'NU'}</th>
            <th>{pers.coasigurat ? 'DA' : 'NU'}</th>
            <th>
              <Row>
                <Button
                  onClick={() => this.editPersoanaIntretinere(pers)}
                  variant="outline-secondary"
                  className="m-1 p-1 rounded-circle border-0"
                >
                  <Edit fontSize="small" />
                </Button>

                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-1 p-1 rounded-circle border-0"
                        {...bindTrigger(popupState)}
                      >
                        <DeleteIcon fontSize="small" />
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

  async onRefresh() {
    if (this.state.angajatsel) {
      const persoane = await axios
        .get(`${server.address}/persoanaintretinere/ida=${this.state.angajatsel.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) => console.error(err));
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

  render() {
    return (
      <Aux>
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
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
                          type="checkbox"
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
                          type="checkbox"
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
              <Card.Header>
                <Card.Title as="h5">
                  Persoane Întreținere
                  {this.state.angajatsel
                    ? ' - ' + this.state.angajatsel.numeintreg
                    : ' *niciun angajat selectat'}
                </Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Add className="m-0 p-0" />
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
