import React from 'react';
import { Row, Col, Card, Form, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { judete, sectoare } from '../Resources/judete';

class AddSocietate extends React.Component {
  constructor(props) {
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.handleClose = this.handleClose.bind(this);

    this.state = {
      nume: '', // text
      idcaen: '',
      cif: '', // text
      capsoc: '',
      regcom: '', // text
      adresa: '', // text
      localitate: '', // text
      judet: '', // text
      capitala: 'Județ',
      email: '', // text
      telefon: '', // text
      show: false,
      modalMessage: '', // text,
    };
  }

  clearFields() {
    this.setState({
      nume: '', // text
      idcaen: '',
      cif: '', // text
      capsoc: '',
      regcom: '', // text
      adresa: '', // text
      localitate: '', // text
      judet: '', // text
      capitala: 'Județ',
      email: '', // text
      telefon: '', // text
      show: false,
      modalMessage: '', //text
    });
  }

  onChangeLocalitate(e) {
    if (
      e.target.value === 'Bucuresti' ||
      e.target.value === 'București' ||
      e.target.value === 'BUCURESTI' ||
      e.target.value === 'BUCUREȘTI'
    )
      this.setState({
        capitala: 'Sector',
        localitate: e.target.value,
      });
    else
      this.setState({
        capitala: 'Județ',
        localitate: e.target.value,
      });
  }

  handleClose(e) {
    this.setState({
      show: false,
    });
  }

  async onSubmit(e) {
    try {
      e.preventDefault();
    } catch (err) {
      console.log(err);
    }

    for (const key in this.state) {
      if (this.state[key] === '' || this.state[key] === "''") this.state[key] = null;
    }

    var caen_id = null;
    if (this.state.idcaen !== null) {
      caen_id = await fetch(`http://localhost:5000/caen/${this.state.idcaen}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }).then((res) => (res.status === 200 ? res.json() : -1));

      if (caen_id === -1) {
        caen_id = null;
      } else {
        caen_id = caen_id.id;
        console.log('caen_id:', caen_id);
      }
    }

    var adr_id = null;
    // if aresa hass all fields null, don't add adresa or idadresa
    if (this.state.adresa !== null || this.state.localitate !== null) {
      // FIRST ADD ADRESA TO DATABASE
      const adresa_body = {
        adresa: this.state.adresa,
        localitate: this.state.localitate,
        judet: this.state.judet,
        tara: null,
      };
      // console.log(JSON.stringify(adresa_body));
      adr_id = await fetch('http://localhost:5000/adresa', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(adresa_body),
      }).then((res) => res.json());
      console.log('idadresa:', adr_id);
    }
    // build societate JSON for POST with adr_id as idadresa
    const societate_body = {
      nume: this.state.nume,
      idcaen: caen_id,
      cif: this.state.cif,
      capsoc: this.state.capsoc,
      regcom: this.state.regcom,
      idadresa: adr_id,
      email: this.state.email,
      telefon: this.state.telefon,
    };
    // ADD SOCIETATE TO DATABASE
    await fetch('http://localhost:5000/societate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(societate_body),
    })
      .then((societate_response) => societate_response.json())
      .then(() => {
        //alert("Societate adaugata cu succes!");
        this.setState({
          show: true,
          message: 'Societate adăugată cu succes!',
        });
      })
      .then(this.clearFields());
  }

  render() {
    const judeteObj = judete.map((judet, index) => {
      return <option key={index}>{judet}</option>;
    });

    const sectoareObj = sectoare.map((sector, index) => {
      return <option key={index}>{sector}</option>;
    });

    const list = () => {
      if (this.state.capitala === 'Județ') return judeteObj;
      return sectoareObj;
    };

    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Date incomplete</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.message}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Adaugă societate</Card.Title>
              </Card.Header>
              <Card.Body>
                <h5>Datele societății</h5>
                <hr />
                <Form onSubmit={this.onSubmit}>
                  <Row>
                    {/* <Form> */}
                    <Col md={6}>
                      <Form.Group controlId="nume">
                        <Form.Label>Denumire societate</Form.Label>
                        <Form.Control
                          required
                          type="text"
                          placeholder="Nume"
                          value={this.state.nume}
                          onChange={(e) =>
                            this.setState({
                              nume: e.target.value,
                            })
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="adresa">
                        <Form.Label>Punct de lucru</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="eg. Strada nr. 1"
                          value={this.state.adresa}
                          onChange={(e) =>
                            this.setState({
                              adresa: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="localitate">
                        <Form.Label>Localitate</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Localitate"
                          value={this.state.localitate}
                          onChange={this.onChangeLocalitate}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="judet">
                        <Form.Label>{this.state.capitala}</Form.Label>
                        <Form.Control
                          as="select"
                          value={this.state.judet}
                          onChange={(e) => this.setState({
                            judet: e.target.value,
                          })}
                        >
                          <option>-</option>
                          {list()}
                        </Form.Control>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="codCaen">
                        <Form.Label>Cod CAEN</Form.Label>
                        <Form.Control
                          type="number"
                          placeholder="CAEN"
                          value={this.state.idcaen}
                          onChange={(e) =>
                            this.setState({
                              idcaen: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="cif">
                        <Form.Label>CIF</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="CIF"
                          value={this.state.cif}
                          onChange={(e) =>
                            this.setState({
                              cif: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="capSoc">
                        <Form.Label>Capital social</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Capital social"
                          value={this.state.capsoc}
                          onChange={(e) =>
                            this.setState({
                              capsoc: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="regcom">
                        <Form.Label>Registrul comerțului</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="regcom"
                          value={this.state.regcom}
                          onChange={(e) =>
                            this.setState({
                              regcom: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="telefon">
                        <Form.Label>Telefon</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Telefon"
                          value={this.state.telefon}
                          onChange={(e) =>
                            this.setState({
                              telefon: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="email">
                        <Form.Label>E-mail</Form.Label>
                        <Form.Control
                          type="email"
                          placeholder="email@email.dom"
                          value={this.state.email}
                          onChange={(e) =>
                            this.setState({
                              email: e.target.value,
                            })}
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Button variant="outline-primary" type="submit">
                        Adaugă
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AddSocietate;
