import React from 'react';
import { Row, Col, Form, Button, Modal, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';

import { judete, sectoare } from '../../Resources/judete';
import { getSocSel } from '../../Resources/socsel';
import { server } from '../../Resources/server-address';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

class Persoana extends React.Component {
  constructor() {
    super();
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.onChangeCnp = this.onChangeCnp.bind(this);
    this.hasRequired = this.hasRequired.bind(this);
    this.getDatanasteriiByCNP = this.getDatanasteriiByCNP.bind(this);
    this.createAngajat = this.createAngajat.bind(this);

    // PostgreSQL uses the  yyyy-mm-dd
    this.state = {
      socsel: getSocSel(),

      show: false,
      modalMessage: '',
      capitala: 'Județ',

      gen: 'Dl.',
      nume: '',
      prenume: '',

      idactidentitate: null, // separate fetch
      tipact: 'Carte de identitate',
      serie: '',
      numar: '',
      cnp: '',
      datanasterii: '',
      loculnasterii: '',
      eliberatde: '',
      dataeliberarii: '',

      starecivila: 'Necăsătorit',

      idadresa: null, // separate fetch
      localitate: '',
      judet: '',
      adresacompleta: '',

      telefon: '',
      email: '',

      showToast: false,
      toastMessage: '',
    };
  }

  clearFields() {
    this.setState({
      socsel: getSocSel(),

      show: false,
      modalMessage: '',
      capitala: 'Județ',

      gen: 'Dl.',
      nume: '',
      prenume: '',

      idactidentitate: null, // separate fetch
      tipact: 'Carte de identitate',
      serie: '',
      numar: '',
      cnp: '',
      datanasterii: '',
      loculnasterii: '',
      eliberatde: '',
      dataeliberarii: '',

      starecivila: 'Necăsătorit',

      idadresa: null, // separate fetch
      localitate: '',
      judet: '',
      adresacompleta: '',

      telefon: '',
      email: '',
    });
  }

  onChangeLocalitate(e) {
    if (
      e.target.value.toLowerCase() === 'bucuresti' ||
      e.target.value.toLowerCase() === 'bucurești' ||
      e.target.value.toLowerCase() === 'bucharest'
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

  getDatanasteriiByCNP(cnp) {
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
    this.setState({
      cnp: e.target.value,
      datanasterii: this.getDatanasteriiByCNP(e.target.value),
    });
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  hasRequired() {
    if (this.state.nume === '') {
      this.setState({
        show: true,
        modalMessage: 'Persoana trebuie să aibă un nume.',
      });
      this.handleClose();
      return false;
    }

    if (this.state.prenume === '') {
      this.setState({
        show: true,
        modalMessage: 'Persoana trebuie să aibă un prenume.',
      });
      // this.handleClose();
      return false;
    }

    return true;
  }

  async createAngajat(idpersoana) {
    await axios
      .post(
        `${server.address}/angajat`,
        { idpersoana: idpersoana, idsocietate: this.state.socsel.id },
        { withCredentials: true }
      )
      .catch((err) => console.error(err));
    console.log(idpersoana, this.state.socsel);
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.hasRequired()) return;

    let adresa_body = {
      adresa: this.state.adresacompleta || null,
      localitate: this.state.localitate || null,
      judet: this.state.judet || null,
      tara: null,
    };

    let buletin_body = {
      cnp: this.state.cnp || null,
      tip: this.state.tipact || null,
      serie: this.state.serie || null,
      numar: this.state.numar || null,
      datanasterii: this.state.datanasterii || null,
      eliberatde: this.state.eliberatde || null,
      dataeliberarii: this.state.dataeliberarii || null,
      loculnasterii: this.state.loculnasterii || null,
    };

    const angajat_body = {
      persoana: {
        gen: this.state.gen || null,
        nume: this.state.nume || null,
        prenume: this.state.prenume || null,
        actidentitate: buletin_body,
        adresa: adresa_body,
        starecivila: this.state.starecivila || null,
        email: this.state.email || null,
        telefon: this.state.telefon || null,
        cnp: this.state.cnp || null,
      },
    };

    const persoana = await axios
      .post(`${server.address}/angajat/ids=${this.state.socsel.id}/0`, angajat_body, {
        withCredentials: true,
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga angajatul\n' + err.response.data.message,
        })
      );

    if (persoana) {
      this.clearFields();
      this.setState({
        show: true,
        modalMessage: 'Persoană adaugată  💾',
      });

      console.log('idpersoana:', persoana.id);
    } else return;
  }

  render() {
    const judeteObj = judete.map((judet, index) => {
      return <option key={index}>{judet}</option>;
    });

    const sectoareObj = sectoare.map((sector, index) => {
      return <option key={index}>{sector}</option>;
    });

    const listaJudete = () => {
      if (this.state.capitala === 'Județ') return judeteObj;
      return sectoareObj;
    };

    return (
      <React.Fragment>
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
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" href="/tables/angajati">
              Către angajatți
            </Button>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Form onSubmit={this.onSubmit}>
          <Row>
            {/* dl/dna, nume, prenume */}
            <Col md={12} className="border rounded pt-3">
              <Row>
                <Col md={3}>
                  <Form.Group id="gen">
                    <Form.Control
                      as="select"
                      value={this.state.gen}
                      onChange={(e) => {
                        this.setState({ gen: e.target.value });
                      }}
                    >
                      <option>Dl.</option>
                      <option>Dna.</option>
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={9} />
                <Col md={6}>
                  <Form.Group controlId="nume">
                    <Form.Label>Nume</Form.Label>
                    <Form.Control
                      required
                      type="text"
                      placeholder="eg. Popescu"
                      value={this.state.nume}
                      onChange={(e) => {
                        this.setState({ nume: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="prenume">
                    <Form.Label>Prenume</Form.Label>
                    <Form.Control
                      required
                      type="text"
                      placeholder="eg. Ion"
                      value={this.state.prenume}
                      onChange={(e) => {
                        this.setState({ prenume: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Col>

            {/* act identitate */}
            <Col md={12} className="border rounded pt-3">
              <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                Act identitate
              </Typography>
              <Row>
                <Col md={6}>
                  <Form.Group id="tipact">
                    <Form.Label>Tip act</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.tipact}
                      onChange={(e) => {
                        this.setState({ tipact: e.target.value });
                      }}
                    >
                      <option>Carte de identitate</option>
                      <option>Pașaport</option>
                      <option>Buletin de identitate</option>
                      <option>Carte de rezidență</option>
                      <option>Alt tip</option>
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={3}>
                  <Form.Group controlId="serie">
                    <Form.Label>Serie</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Serie CI"
                      value={this.state.serie}
                      onChange={(e) => {
                        this.setState({ serie: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={3}>
                  <Form.Group controlId="numar">
                    <Form.Label>Număr</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Număr"
                      value={this.state.numar}
                      onChange={(e) => {
                        this.setState({ numar: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="cnp">
                    <Form.Label>CNP</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="CNP"
                      value={this.state.cnp}
                      onChange={(e) => this.onChangeCnp(e)}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="datanasterii">
                    <Form.Label>Data nașterii</Form.Label>
                    <Form.Control type="date" value={this.state.datanasterii} disabled />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="eliberatde">
                    <Form.Label>Eliberat de</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Eliberat de"
                      value={this.state.eliberatde}
                      onChange={(e) => {
                        this.setState({ eliberatde: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="dataeliberarii">
                    <Form.Label>Data eliberării</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.dataeliberarii}
                      onChange={(e) => {
                        this.setState({ dataeliberarii: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={12} />
                <Col md={6}>
                  <Form.Group controlId="loculnasterii">
                    <Form.Label>Locul nașterii</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Locul nașterii"
                      value={this.state.loculnasterii}
                      onChange={(e) => {
                        this.setState({ loculnasterii: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="starecivila">
                    <Form.Label>Stare civilă</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.starecivila}
                      onChange={(e) => {
                        this.setState({ starecivila: e.target.value });
                      }}
                    >
                      <option>Necăsătorit</option>
                      <option>Căsătorit</option>
                    </Form.Control>
                  </Form.Group>
                </Col>
              </Row>
            </Col>
            {/* adresa */}
            <Col md={12} className="border rounded pt-3">
              <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                Adresă
              </Typography>
              <Row>
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
                  <Form.Group id="judet">
                    <Form.Label>{this.state.capitala}</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.judet}
                      onChange={(e) => {
                        this.setState({ judet: e.target.value });
                      }}
                    >
                      <option>-</option>
                      {listaJudete()}
                    </Form.Control>
                  </Form.Group>
                </Col>
                <Col md={12}>
                  <Form.Group controlId="adresacompleta">
                    <Form.Label>Adresa Completă</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="eg. Str. nr., bl. sc. ap. etc."
                      value={this.state.adresacompleta}
                      onChange={(e) =>
                        this.setState({
                          adresacompleta: e.target.value,
                        })
                      }
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Col>
            {/* nr. tel, email */}
            <Col md={12} className="border rounded pt-3">
              <Row>
                <Col md={6}>
                  <Form.Group controlId="telefon">
                    <Form.Label>Număr telefon</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.telefon}
                      onChange={(e) =>
                        this.setState({
                          telefon: e.target.value,
                        })
                      }
                      placeholder="eg. 0712345678"
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group controlId="email">
                    <Form.Label>E-mail</Form.Label>
                    <Form.Control
                      type="email"
                      value={this.state.email}
                      onChange={(e) =>
                        this.setState({
                          email: e.target.value,
                        })
                      }
                      placeholder="email@email.dom"
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Col>
          </Row>

          {!this.props.asChild ? (
            <Row>
              <Col md={12}>
                <Button variant="success" className="float-right m-0 pl-5 pr-5 mt-2" type="submit">
                  Adaugă
                </Button>
              </Col>
            </Row>
          ) : null}
        </Form>
      </React.Fragment>
    );
  }
}

export default Persoana;
