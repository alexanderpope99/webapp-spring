import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { RotateCw } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel } from '../Resources/angajatsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class PersoaneIntretinereView extends React.Component {
  constructor() {
    super();

    this.fillTable = this.fillTable.bind(this);
    this.viewPersoanaIntretinere = this.viewPersoanaIntretinere.bind(this);
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
      grad: '',
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

  async viewPersoanaIntretinere(pers) {
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
            toastMessage: 'Nu am putut prelua persoanele întreținere\n' + err.response.data.message,
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
            <th>{pers.cnp}</th>
            <th>{pers.grad}</th>
            <th>{pers.gradinvaliditate}</th>
            <th>{pers.coasigurat ? 'DA' : 'NU'}</th>
            <th>{pers.intretinut ? 'DA' : 'NU'}</th>
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
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addPersoanaIntretinere}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>Nume</Form.Label>
                    <Form.Control disabled type="text" value={this.state.nume} />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Preume</Form.Label>
                    <Form.Control disabled type="text" value={this.state.prenume} />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>CNP</Form.Label>
                    <Form.Control disabled type="text" value={this.state.cnp} />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Data nasterii</Form.Label>
                    <Form.Control disabled type="date" value={this.state.datanasterii} />
                  </Form.Group>
                  <Row>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Grad</Form.Label>
                        <Form.Control disabled as="select" value={this.state.grad} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group>
                        <Form.Label>Grad Invaliditate</Form.Label>
                        <Form.Control disabled as="select" value={this.state.gradinvaliditate} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="ml-3">
                        <Form.Check
                          disabled
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
                          disabled
                          custom
                          type="checkbox"
                          id="intretinutCheck"
                          label="Intretinut"
                          checked={this.state.intretinut}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Form>
          </Modal.Body>
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

export default PersoaneIntretinereView;
