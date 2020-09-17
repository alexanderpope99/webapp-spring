import React from 'react';
import { Row, Col, Form, Button, Modal } from 'react-bootstrap';

import { judete, sectoare } from '../../Resources/judete';
import { getSocSel } from '../../Resources/socsel';
import { server } from '../../Resources/server-address';

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
    if (cnp === null || typeof cnp === 'undefined' || cnp === 'null') return '';

    if (cnp.length > 6) {
      const an = cnp.substring(1, 3);
      const luna = cnp.substring(3, 5);
      const zi = cnp.substring(5, 7);
      if (cnp[0] <= 2) return `19${an}-${luna}-${zi}`;
      else return `20${an}-${luna}-${zi}`;
    } else if (cnp.length === 0) return '';
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
    await fetch(`${server.address}/angajat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idpersoana: idpersoana, idsocietate: this.state.socsel.id }),
    }).catch((err) => console.error(err));
    console.log(idpersoana, this.state.socsel);
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.hasRequired()) return;

    var actidentitate = null,
      adresa = null;

    // POST only if any adrese fields is filled
    if (
      this.state.adresacompleta !== null ||
      this.state.localitate !== null ||
      this.state.judet !== null
    ) {
      const adresa_body = {
        adresa: this.state.adresacompleta,
        localitate: this.state.localitate,
        judet: this.state.judet,
        tara: null,
      };
      adresa = await fetch(`${server.address}/adresa`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(adresa_body),
      }).then((adresa) => adresa.json());
      console.log('idadresa:', adresa.id);
    }

    // POST only if any actitentitate field is filled
    if (this.state.serie !== null || this.state.numar !== null || this.state.cnp !== null) {
      const buletin_body = {
        cnp: this.state.cnp,
        tip: this.state.tipact,
        serie: this.state.serie,
        numar: this.state.numar,
        datanasterii: this.state.datanasterii,
        eliberatde: this.state.eliberatde,
        dataeliberarii: this.state.dataeliberarii,
        loculnasterii: this.state.loculnasterii,
      };

      actidentitate = await fetch(`${server.address}/actidentitate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(buletin_body),
      }).then((res) => res.json());

      console.log('idactidentitate:', actidentitate.id);
    }

    const persoana_body = {
      gen: this.state.gen,
      nume: this.state.nume,
      prenume: this.state.prenume,
      idactidentitate: actidentitate === null ? null : actidentitate.id,
      idadresa: adresa === null ? null : adresa.id,
      starecivila: this.state.starecivila,
      email: this.state.email,
      telefon: this.state.telefon,
      cnp: this.state.cnp,
    };
    console.log(persoana_body);

    const persoana = await fetch(`${server.address}/persoana`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(persoana_body),
    })
      .then((persoana) => persoana.json())
      .catch((err) => console.error('error:', err.message));

    if (typeof persoana.id === 'number') {
      this.clearFields();
      this.setState({
        show: true,
        modalMessage: 'Persoana adaugată cu succes.',
      });
      console.log('idpersoana:', persoana.id);

      await this.createAngajat(persoana.id);

      return persoana.id;
    } else return;
  }

  render() {
    const luni_nr = [];
    for (var i = 1; i < 13; ++i) luni_nr.push(<option key={i}>{i}</option>);

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
      <div>
        <Modal show={this.state.show} onHide={this.handleClose}>
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

        <Form onSubmit={this.onSubmit}>
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
            {/* <Col md={12} /> */}
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
            {/* <Col md={12} /> */}
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
            {/* <Col md={12} /> */}
            <Col md={6}>
              <Form.Group controlId="cnp">
                <Form.Label>CNP</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="CNP"
                  value={this.state.cnp}
                  onChange={this.onChangeCnp}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="datanasterii">
                <Form.Label>Data nașterii</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.datanasterii}
                  onChange={(e) => {
                    this.setState({ datanasterii: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={12} />

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
            {/* <Col md={12} /> */}
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
            {/* <Col md={12} /> */}
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
            {/* <Col md={12} /> */}
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
            {/* <Col md={12} /> */}
          </Row>

          {typeof this.props.asChild === 'undefined' ? (
            <Row>
              <Col md={12}>
                <Button variant="success" className="float-right m-0 pl-5 pr-5" type="submit">
                  Adaugă
                </Button>
              </Col>
            </Row>
          ) : null}
        </Form>
      </div>
    );
  }
}

export default Persoana;
