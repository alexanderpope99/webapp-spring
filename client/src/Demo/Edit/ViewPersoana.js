import React from 'react';
import {
  Row,
  Col,
  Card,
  Form,
  Button,
  InputGroup,
  FormControl,
  OverlayTrigger,
  Tooltip,
} from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { FileText } from 'react-feather';
import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel, setAngajatSel } from '../Resources/angajatsel';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class ViewPersoana extends React.Component {
  constructor(props) {
    super();
    this.getTipJudet = this.getTipJudet.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.onChangeCnp = this.onChangeCnp.bind(this);
    this.fillForm = this.fillForm.bind(this);
    this.getDatanasteriiByCNP = this.getDatanasteriiByCNP.bind(this);

    let angajatSel = getAngajatSel();
    var idOfSelected = null;
    if (props.location) {
      let search = props.location.search; // returns the URL query String
      let params = new URLSearchParams(search);
      idOfSelected = params.get('id');
    }
    // exista un angajat selectat in sessionStorage
    else if (angajatSel) {
      idOfSelected = angajatSel.idpersoana;
    }
    // else: idOfSelected remains null

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      angajatsel: angajatSel,

      show: false,
      modalMessage: '',

      tipJudet: 'Județ',

      numeintreg: [], // array of objects
      numeComponent: null,

      id: idOfSelected,
      selectednume: '-',

      gen: 'Dl.',
      nume: '',
      prenume: '',

      idactidentitate: null,
      tipact: 'Carte de identitate',
      serie: '',
      numar: '',
      cnp: '',
      datanasterii: '',
      loculnasterii: '',
      eliberatde: '',
      dataeliberarii: '',
      starecivila: 'Necăsătorit',

      idadresa: null,
      localitate: '',
      judet: '',
      adresacompleta: '',

      telefon: '',
      email: '',
    };
  }

  clearFields() {
    this.setState({
      tipJudet: 'Județ',

      gen: 'Dl.',
      nume: '',
      prenume: '',

      idactidentitate: null,
      tipact: 'Carte de identitate',
      serie: '',
      numar: '',
      cnp: '',
      datanasterii: '',
      loculnasterii: '',
      eliberatde: '',
      dataeliberarii: '',
      starecivila: 'Necăsătorit',

      idadresa: null,
      localitate: '',
      judet: '',
      adresacompleta: '',

      telefon: '',
      email: '',
    });
  }

  async getAngajat() {
    return await axios
      .get(`${server.address}/angajat/socid=${this.state.socsel.id}/usrid=${this.state.user.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.log(err));
	}
	
	async fillForm() {
		this.clearFields();
		
		const angajat = await this.getAngajat();
		const persoana = angajat.persoana;

    setAngajatSel({
      idpersoana: persoana.id,
      numeintreg: persoana.nume + ' ' + persoana.prenume,
    });

    if (persoana.adresa) {
      this.setState(
        {
          idadresa: persoana.adresa.id,
          judet: persoana.adresa.judet || '',
          adresacompleta: persoana.adresa.adresa || '',
        },
        () => this.onChangeLocalitate(persoana.adresa.localitate)
      );
    }
    if (persoana.actidentitate) {
      this.setState({
        idactidentitate: persoana.actidentitate.id,
        tipact: persoana.actidentitate.tip || 'Carte de identitate',
        serie: persoana.actidentitate.serie || '',
        numar: persoana.actidentitate.numar || '',
        cnp: persoana.actidentitate.cnp || '',
        datanasterii: persoana.actidentitate.datanasterii
          ? persoana.actidentitate.datanasterii.substring(0, 10)
          : '',
        loculnasterii: persoana.actidentitate.loculnasterii || '',
        eliberatde: persoana.actidentitate.eliberatde || '',
        dataeliberarii: persoana.actidentitate.dataeliberarii || '',
        starecivila: persoana.actidentitate.starecivila || '',
      });
    }

    this.setState({
      id: persoana.id,
      email: persoana.email || '',
      gen: persoana.gen || '',
      nume: persoana.nume || '',
      prenume: persoana.prenume || '',
      telefon: persoana.telefon || '',
    });
  }

  componentDidMount() {
    this.fillForm();
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

  onChangeLocalitate(localitate) {
    if (localitate)
      this.setState({
        tipJudet: this.getTipJudet(localitate),
        localitate: localitate,
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
    const cnp = e.target.value;
    this.setState({
      cnp: cnp,
      datanasterii: this.getDatanasteriiByCNP(e.target.value),
    });
	}

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header style={{ border: 'none', paddingBottom: '0rem' }}>
                <Card.Title as="h4">Persoana</Card.Title>
                <InputGroup className="mb-3">
                  <FormControl
										disabled
                    type="text"
                    value={this.state.nume + ' ' + this.state.prenume}
                  />
                  <InputGroup.Append>
                    <OverlayTrigger
                      placement="bottom"
                      delay={{ show: 250, hide: 250 }}
                      overlay={
                        <Tooltip id="update-button" style={{ opacity: '.4' }}>
                          Către realizări/rețineri
                        </Tooltip>
                      }
                    >
                      <Button
                        href="/forms/realizari-retineri"
                        variant="outline-info"
                        className="pb-0"
                      >
                        <FileText size={20} className="m-0" />
                      </Button>
                    </OverlayTrigger>
                  </InputGroup.Append>
                </InputGroup>
              </Card.Header>
              <Card.Header>
                <Card.Title as="h5">Date personale</Card.Title>
              </Card.Header>

              <Card.Body>
                <Form>
                  <Row>
                    {/* dl/dna, nume, prenume */}
                    <Col md={12} className="border rounded pt-3">
                      <Row>
                        <Col md={3}>
                          <Form.Group id="gen">
                            <Form.Control disabled type="text" value={this.state.gen} />
                          </Form.Group>
                        </Col>
                        <Col md={9} />
                        <Col md={6}>
                          <Form.Group controlId="nume">
                            <Form.Label>Nume</Form.Label>
                            <Form.Control
                              disabled
                              required
                              type="text"
                              placeholder="eg. Popescu"
                              value={this.state.nume}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="prenume">
                            <Form.Label>Prenume</Form.Label>
                            <Form.Control
                              disabled
                              required
                              type="text"
                              placeholder="eg. Ion"
                              value={this.state.prenume}
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
                            <Form.Control disabled type="text" value={this.state.tipact} />
                          </Form.Group>
                        </Col>
                        <Col md={3}>
                          <Form.Group controlId="serie">
                            <Form.Label>Serie</Form.Label>
                            <Form.Control
                              disabled
                              type="text"
                              placeholder="Serie CI"
                              value={this.state.serie}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={3}>
                          <Form.Group controlId="numar">
                            <Form.Label>Număr</Form.Label>
                            <Form.Control
                              disabled
                              type="text"
                              placeholder="Număr"
                              value={this.state.numar}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="cnp">
                            <Form.Label>CNP</Form.Label>
                            <Form.Control
                              disabled
                              type="text"
                              placeholder="CNP"
                              value={this.state.cnp}
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
                              disabled
                              type="text"
                              placeholder="Eliberat de"
                              value={this.state.eliberatde}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="dataeliberarii">
                            <Form.Label>Data eliberării</Form.Label>
                            <Form.Control disabled type="date" value={this.state.dataeliberarii} />
                          </Form.Group>
                        </Col>
                        <Col md={12} />
                        <Col md={6}>
                          <Form.Group controlId="loculnasterii">
                            <Form.Label>Locul nașterii</Form.Label>
                            <Form.Control
                              disabled
                              type="text"
                              placeholder="Locul nașterii"
                              value={this.state.loculnasterii}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="starecivila">
                            <Form.Label>Stare civilă</Form.Label>
                            <Form.Control disabled type="text" value={this.state.starecivila} />
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
                              disabled
                              type="text"
                              placeholder="Localitate"
                              value={this.state.localitate}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="judet">
                            <Form.Label>{this.state.tipJudet}</Form.Label>
                            <Form.Control disabled type="text" value={this.state.judet} />
                          </Form.Group>
                        </Col>
                        <Col md={12}>
                          <Form.Group controlId="adresacompleta">
                            <Form.Label>Adresa Completă</Form.Label>
                            <Form.Control
                              disabled
                              type="text"
                              placeholder="eg. Str. nr., bl. sc. ap. etc."
                              value={this.state.adresacompleta}
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
                              disabled
                              type="text"
                              value={this.state.telefon}
                              placeholder="eg. 0712345678"
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="email">
                            <Form.Label>E-mail</Form.Label>
                            <Form.Control
                              disabled
                              type="email"
                              value={this.state.email}
                              placeholder="email@email.dom"
                            />
                          </Form.Group>
                        </Col>
                      </Row>
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

export default ViewPersoana;
