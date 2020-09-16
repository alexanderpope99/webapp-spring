import React from 'react';
import {
  Row,
  Col,
  Card,
  Form,
  Button,
  Modal,
  InputGroup,
  FormControl,
  OverlayTrigger,
  Tooltip,
} from 'react-bootstrap';
import Add from '@material-ui/icons/Add';
import Aux from '../../hoc/_Aux';
import { judete, sectoare } from '../Resources/judete';
import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';

class EditPersoana extends React.Component {
  constructor(props) {
    super();
    this.hasRequired = this.hasRequired.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.getTipJudet = this.getTipJudet.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.onChangeCnp = this.onChangeCnp.bind(this);
    this.getIdByNumeintreg = this.getIdByNumeintreg.bind(this);
    this.getNumeintregById = this.getNumeintregById.bind(this);
    // this.onChangeSelected = this.onChangeSelected.bind(this);
    this.fillForm = this.fillForm.bind(this);
    this.getDatanasteriiByCNP = this.getDatanasteriiByCNP.bind(this);
    this.handleClose = this.handleClose.bind(this);

    // console.log(props.location);
    var IdFromURL = null;
    if (typeof props.location !== 'undefined') {
      let search = props.location.search; // returns the URL query String
      let params = new URLSearchParams(search);
      IdFromURL = params.get('id');
    }
    // console.log(IdFromURL);

    this.state = {
      socsel: getSocSel(),

      show: false,
      modalMessage: '',

      tipJudet: 'Județ',

      numeintreg: [], // array of objects
      numeComponent: null,

      id: IdFromURL,
      selectednume: '-',

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

  clearFields(withSelect) {
    if (withSelect)
      this.setState({
        selectednume: '-',
        id: -1,
      });

    this.setState({
      tipJudet: 'Județ',

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

  componentDidMount() {
    this.getNumeintreg();
    // window.scrollTo(0, 0);
    // console.log(this.state);
  }

  async getNumeintreg() {
    const persoane = await fetch(`${server.address}/persoana/ids=${this.state.socsel.id}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    }).then((persoane) => persoane.json());

    this.setState(
      {
        numeintreg: persoane.map((pers, index) => ({
          id: pers.id,
          nume: pers.nume + ' ' + pers.prenume,
        })),
      },
      () => {
        this.setState(
          {
            selectednume: this.state.id ? '-' : this.getNumeintregById(this.state.id),
          },
          this.fillForm
        );
      }
    );
    // console.log('nume intregi:', this.state.numeintreg);
  }

  getIdByNumeintreg(value) {
    for (let nume of this.state.numeintreg) {
      if (nume.nume === value) {
        return nume.id;
      } else if (value === '-') return -1;
    }
    return -1;
  }

  getNumeintregById(id) {
    for (let nume of this.state.numeintreg) {
      // eslint-disable-next-line eqeqeq
      if (nume.id == id) return nume.nume;
    }

    return '-';
  }

  getTipJudet(tipJudet) {
    if (typeof tipJudet !== 'string') {
      return 'Județ';
    }
    if (
      tipJudet.toLowerCase() === 'bucuresti' ||
      tipJudet.toLowerCase() === 'bucurești' ||
      tipJudet.toLowerCase() === 'bucharest'
    )
      return 'Sector';
    else return 'Județ';
  }

  onChangeLocalitate(e) {
    this.setState({
      tipJudet: this.getTipJudet(e.target.value),
      localitate: e.target.value,
    });
  }

  getDatanasteriiByCNP(cnp) {
    // console.log('getDatanasteriiByCNP called |', cnp);
    if (cnp === null || typeof cnp === 'undefined' || cnp === 'null') return '';

    if (cnp.length > 6) {
      const an = cnp.substring(1, 3);
      const luna = cnp.substring(3, 5);
      const zi = cnp.substring(5, 7);
      if (cnp[0] <= 2)
        // this.setState({
        return `19${an}-${luna}-${zi}`;
      // });
      // this.setState({
      else return `20${an}-${luna}-${zi}`;
      // });
    } else if (cnp.length === 0)
      // this.setState({
      return '';
    // })
  }

  onChangeCnp(e) {
    const cnp = e.target.value;
    this.setState({
      cnp: cnp,
      datanasterii: this.getDatanasteriiByCNP(e.target.value),
    });
  }

  getIdOfSelected() {
    return this.state.id;
  }

  getSelectedNume() {
    return this.state.selectednume;
  }

  hasRequired() {
    if (this.state.nume === '') return false;

    if (this.state.prenume === '') return false;

    return true;
  }

  async fillForm() {
    this.clearFields();
    // console.log(this.state.numeintreg);
    const id = this.getIdByNumeintreg(this.state.selectednume);
    // console.log(id);
    if (id === -1) {
      this.setState({
        selectednume: '-',
      });
      this.clearFields();
      return;
    }

    const persoana = await fetch(`${server.address}/persoana/${id}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    }).then((persoana) => persoana.json());

    if (persoana.idadresa) {
      await fetch(`${server.address}/adresa/${persoana.idadresa}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      })
        .then((adresa) => adresa.json())
        .then((adresa) => {
          if (adresa)
            this.setState({
              idadresa: adresa.id,
              localitate: adresa.localitate || '',
              judet: adresa.judet || '',
              adresacompleta: adresa.adresa || '',
              tipJudet: this.getTipJudet(adresa.localitate),
            });
        });
    }

    if (persoana.idactidentitate) {
      await fetch(
        `${server.address}/actidentitate/${persoana.idactidentitate}`,
        {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        }
      )
        .then((actidentitate) => actidentitate.json())
        .then((actidentitate) => {
          if (actidentitate)
            this.setState({
              idactidentitate: actidentitate.id,
              tipact: actidentitate.tip || '',
              serie: actidentitate.serie || '',
              numar: actidentitate.numar || '',
              loculnasterii: actidentitate.loculnasterii || '',
              eliberatde: actidentitate.eliberatde || '',
              dataeliberarii: actidentitate.dataeliberarii || '',
            });
        });
    }

    this.setState({
      cnp: persoana.cnp || '',
      email: persoana.email || '',
      gen: persoana.gen || '',
      nume: persoana.nume || '',
      prenume: persoana.prenume || '',
      starecivila: persoana.starecivila || '',
      telefon: persoana.telefon || '',
      datanasterii: this.getDatanasteriiByCNP(persoana.cnp),
    });
  }

  handleClose() {
    this.setState({
      show: false,
    });
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.hasRequired()) return -1;

    for (let key in this.state)
      if (
        typeof this.state[key] === 'string' &&
        ['', '-'].indexOf(this.state[key]) !== -1
      )
        this.state[key] = null;

    var idadresa = this.state.idadresa,
      idactidentitate = this.state.idactidentitate;

    // persoana nu are adresa asociata
    if (this.state.idadresa === null) {
      // daca se completeaza -> adauga in DB | daca nu -> nimic
      if (
        this.state.adresacompleta !== null ||
        this.state.localitate !== null ||
        this.state.judet !== null
      ) {
        let adresa_body = {
          adresa: this.state.adresacompleta,
          localitate: this.state.localitate,
          judet: this.state.judet,
          tara: null,
        };
        idadresa = await fetch(`${server.address}/adresa`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(adresa_body),
        }).then((idadresa) => idadresa.json());
        idadresa = idadresa.id;
        console.log('added adresa, id =', idadresa);
      }
    } else {
      // persoana are adresa => se actualizeaza
      let adresa_body = {
        adresa: this.state.adresacompleta,
        localitate: this.state.localitate,
        judet: this.state.judet,
        tara: null,
      };
      await fetch(`${server.address}/adresa/${this.state.idadresa}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(adresa_body),
      });
    }

    // persoana nu are actidentitate
    if (this.state.idactidentitate === null) {
      // daca se completeaza actidentitate -> adauga in baza de date | daca nu -> nimic
      if (this.state.serie !== null || this.state.numar !== null || this.state.cnp !== null) {
        let buletin_body = {
          cnp: this.state.cnp,
          tip: this.state.tipact,
          serie: this.state.serie,
          numar: this.state.numar,
          datanasterii: this.state.datanasterii,
          eliberatde: this.state.eliberatde,
          dataeliberarii: this.state.dataeliberarii,
          loculnasterii: this.state.loculnasterii,
        };

        idactidentitate = await fetch(`${server.address}/actidentitate`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(buletin_body),
        })
          .then((idactidentitate) => idactidentitate.json())
          .catch((err) => console.error(err));
        idactidentitate = idactidentitate.id;
        console.log('idactidentitate:', idactidentitate);
      }
    } else {
      // are act identitate => se actualizeaza
      let buletin_body = {
        cnp: this.state.cnp,
        tip: this.state.tipact,
        serie: this.state.serie,
        numar: this.state.numar,
        datanasterii: this.state.datanasterii,
        eliberatde: this.state.eliberatde,
        dataeliberarii: this.state.dataeliberarii,
        loculnasterii: this.state.loculnasterii,
      };

      await fetch(
        `${server.address}/actidentitate/${this.state.idactidentitate}`,
        {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(buletin_body),
        }
      );
    }

    let persoana_body = {
      gen: this.state.gen,
      nume: this.state.nume,
      prenume: this.state.prenume,
      idactidentitate: idactidentitate,
      idadresa: idadresa,
      starecivila: this.state.starecivila,
      email: this.state.email,
      telefon: this.state.telefon,
      cnp: this.state.cnp,
    };
    // update persoana
    await fetch(`${server.address}/persoana/${this.state.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(persoana_body),
    });

    this.getNumeintreg();
    this.fillForm();
    console.log('persoana actualizata');
    this.setState({
      show: true,
      modalMessage: 'Persoană actualizată cu succes.',
    });

    return this.state.id;
  }

  render() {
    const luni_nr = [];
    for (var i = 1; i < 13; ++i) luni_nr.push(<option key={i}>{i}</option>);

    const judeteObj = judete.map((judet, index) => {
      return <option key={index}>{judet}</option>;
    });

    const sectoareObj = sectoare.map((sector, index) => (
      <option key={index}>{sector}</option>
    ));

    const listaJudete = () => {
      if (this.state.tipJudet === 'Județ') return judeteObj;
      return sectoareObj;
    };

    const listaNumeintreg = () =>
      this.state.numeintreg.map((nume, index) => (
        <option key={nume.id}>{nume.nume}</option>
      ));

    return (
      <Aux>
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

        <Row>
          <Col>
            <Card>
              <Card.Header style={{ border: 'none', paddingBottom: '0rem' }}>
                <Card.Title as="h4">Persoana</Card.Title>
                <InputGroup className="mb-3">
                  <FormControl
                    as="select"
                    value={this.state.selectednume}
                    onChange={(e) =>
                      this.setState(
                        {
                          selectednume: e.target.value || '-',
                          id: this.getIdByNumeintreg(e.target.value),
                        },
                        this.fillForm
                      )
                    }
                  >
                    <option>-</option>
                    {listaNumeintreg()}
                  </FormControl>
                  <InputGroup.Append>
                    <OverlayTrigger
                      placement="bottom"
                      delay={{ show: 250, hide: 250 }}
                      overlay={
                        <Tooltip id="update-button" style={{ opacity: '.4' }}>
                          Adaugă o persoană nouă
                        </Tooltip>
                      }
                    >
                      <Button
                        href="/forms/add-persoana"
                        variant="outline-info"
                        className="pb-0"
                      >
                        <Add fontSize="small" className="m-0" />
                      </Button>
                    </OverlayTrigger>
                  </InputGroup.Append>
                </InputGroup>
              </Card.Header>
              <Card.Header>
                <Card.Title as="h5">Modifică datele</Card.Title>
              </Card.Header>

              <Card.Body>
                <Form>
                  <Row>
                    <Col md={12}>
                      <Form.Group></Form.Group>
                    </Col>
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
                      <Form.Group id="nume">
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
                      <Form.Group id="prenume">
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
                      <Form.Group id="serie">
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
                      <Form.Group id="numar">
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
                      <Form.Group id="cnp">
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
                      <Form.Group>
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
                      <Form.Group id="loculnasterii">
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
                      <Form.Group id="localitate">
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
                        <Form.Label>{this.state.tipJudet}</Form.Label>
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
                      <Form.Group id="adresacompleta">
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
                      <Form.Group id="telefon">
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
                      <Form.Group id="email">
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
                        <Button
                          variant={
                            this.state.selectednume === '-'
                              ? 'outline-dark'
                              : 'outline-primary'
                          }
                          onClick={this.onSubmit}
                          disabled={
                            this.state.selectednume === '-' ? true : false
                          }
                        >
                          Actualizează datele personale
                        </Button>
                      </Col>
                    </Row>
                  ) : null}
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default EditPersoana;
