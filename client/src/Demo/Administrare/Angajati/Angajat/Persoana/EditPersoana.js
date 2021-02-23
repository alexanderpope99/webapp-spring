import React from 'react';
import { Link } from 'react-router-dom';
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
  Toast,
} from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { UserPlus, FileText } from 'react-feather';
import Aux from '../../../../../hoc/_Aux/index';
import { judete, sectoare } from '../../../../Resources/judete';
import { getSocSel } from '../../../../Resources/socsel';
import { getAngajatSel, setAngajatSel } from '../../../../Resources/angajatsel';
import { server } from '../../../../Resources/server-address';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';

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
    this.fillForm = this.fillForm.bind(this);
    this.getDatanasteriiByCNP = this.getDatanasteriiByCNP.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onChangeNume = this.onChangeNume.bind(this);

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
      angajatsel: angajatSel,

      show: false,
      showToast: false,
      toastMessage: '',
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

  clearFields(withSelect) {
    if (withSelect) {
      this.setState({
        selectednume: '-',
        id: -1,
      });
      setAngajatSel(null);
    }

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

  componentDidMount() {
    this.getNumeintreg();
    // daca este selectat un angajat
    if (this.state.id) this.fillForm();
  }

  async getNumeintreg() {
    const persoane = await axios
      .get(`${server.address}/persoana/ids=${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut prelua persoanele din baza de date: ' + err.response.data.message,
        })
      );

    this.setState({
      numeintreg: persoane.map((pers, index) => ({
        id: pers.id,
        nume: pers.nume + ' ' + pers.prenume,
      })),
    });
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
    // daca inca nu exista lista cu numele persoanelor
    if (this.state.numeintreg.length === 0) {
      await this.getNumeintreg();
    }

    const id = this.state.id;

    if (id === -1) {
      console.log('id was -1');
      this.setState({
        selectednume: '-',
      });
      this.clearFields(true);
      return;
    }

    const persoana = await axios
      .get(`${server.address}/persoana/${id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua persoana : ' + err.response.data.message,
        })
      );

    setAngajatSel({
      idpersoana: persoana.id,
      numeintreg: persoana.nume + ' ' + persoana.prenume,
    });

    if (persoana.adresa) {
      this.setState(
        {
          idadresa: persoana.adresa.id,
          localitate: persoana.adresa.localitate || '',
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
        starecivila: persoana.actidentitate.starecivila || 'Necăsătorit',
      });
    }

    this.setState({
      id: persoana.id,
      gen: persoana.gen || 'Dl.',
      nume: persoana.nume || '',
      prenume: persoana.prenume || '',
      email: persoana.email || '',
      telefon: persoana.telefon || '',

      selectednume: this.getNumeintregById(id),
    });
  }

  handleClose() {
    this.setState(
      {
        show: false,
      },
      this.props.scrollToTopSmooth
    );
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.hasRequired()) return -1;

    var adresa_body = {
      id: this.state.idadresa,
      adresa: this.state.adresacompleta || null,
      localitate: this.state.localitate || null,
      judet: this.state.judet || null,
      tara: null,
    };

    var buletin_body = {
      id: this.state.idactidentitate,
      cnp: this.state.cnp || null,
      tip: this.state.tipact || 'Carte de identitate',
      serie: this.state.serie || null,
      numar: this.state.numar || null,
      datanasterii: this.state.datanasterii || null,
      eliberatde: this.state.eliberatde || null,
      dataeliberarii: this.state.dataeliberarii || null,
      loculnasterii: this.state.loculnasterii || null,
    };
    console.log(buletin_body.id);

    let persoana_body = {
      gen: this.state.gen || null,
      nume: this.state.nume || null,
      prenume: this.state.prenume || null,
      actidentitate: buletin_body,
      adresa: adresa_body,
      starecivila: this.state.starecivila || null,
      email: this.state.email || null,
      telefon: this.state.telefon || null,
      cnp: this.state.cnp || null,
    };

    // update persoana
    await axios.put(`${server.address}/persoana/${this.state.id}`, persoana_body, {
      headers: authHeader(),
    });

    // refresh numele angajatilor
    this.getNumeintreg();
    this.fillForm();
    console.log('persoana actualizata');
    this.setState({
      show: true,
      modalMessage: 'Persoană actualizată  💾',
    });

    return this.state.id;
  }

  onChangeNume(e) {
    this.setState(
      {
        selectednume: e.target.value || '-',
        id: this.getIdByNumeintreg(e.target.value),
      },
      this.fillForm
    );
  }

  render() {
    const judeteObj = judete.map((judet, index) => {
      return <option key={index}>{judet}</option>;
    });

    const sectoareObj = sectoare.map((sector, index) => <option key={index}>{sector}</option>);

    const listaJudete = () => {
      if (this.state.tipJudet === 'Județ') return judeteObj;
      return sectoareObj;
    };

    const listaNumeintreg = this.state.numeintreg.map((nume, index) => (
      <option key={nume.id}>{nume.nume}</option>
    ));

    return (
      <Aux>
        {/* ERROR TOAST */}
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>

        {/* CONFIRM MODAL */}
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
                    onChange={this.onChangeNume}
                  >
                    <option>-</option>
                    {listaNumeintreg}
                  </FormControl>
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
                      <Link to="/forms/realizari-retineri">
                        <Button variant="outline-info" className="pb-2">
                          <FileText size={20} className="m-0" />
                        </Button>
                      </Link>
                    </OverlayTrigger>
                  </InputGroup.Append>
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
                      <Link to="/forms/add-persoana">
                        <Button variant="outline-info" className="pb-2">
                          <UserPlus size={20} className="m-0" />
                        </Button>
                      </Link>
                    </OverlayTrigger>
                  </InputGroup.Append>
                </InputGroup>
              </Card.Header>
              <Card.Header>
                <Card.Title as="h5">Modifică datele</Card.Title>
              </Card.Header>

              <Card.Body>
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
                                this.setState({
                                  gen: e.target.value,
                                });
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
                              value={this.state.nume || ''}
                              onChange={(e) => {
                                this.setState({
                                  nume: e.target.value,
                                });
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
                              value={this.state.prenume || ''}
                              onChange={(e) => {
                                this.setState({
                                  prenume: e.target.value,
                                });
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
                              value={this.state.tipact || 'Carte de identitate'}
                              onChange={(e) => {
                                this.setState({
                                  tipact: e.target.value,
                                });
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
                              value={this.state.serie || ''}
                              onChange={(e) => {
                                this.setState({
                                  serie: e.target.value,
                                });
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
                              value={this.state.numar || ''}
                              onChange={(e) => {
                                this.setState({
                                  numar: e.target.value,
                                });
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
                              value={this.state.cnp || ''}
                              onChange={(e) => this.onChangeCnp(e)}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="datanasterii">
                            <Form.Label>Data nașterii</Form.Label>
                            <Form.Control
                              type="date"
                              value={this.state.datanasterii || ''}
                              disabled
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group controlId="eliberatde">
                            <Form.Label>Eliberat de</Form.Label>
                            <Form.Control
                              type="text"
                              placeholder="Eliberat de"
                              value={this.state.eliberatde || ''}
                              onChange={(e) => {
                                this.setState({
                                  eliberatde: e.target.value,
                                });
                              }}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="dataeliberarii">
                            <Form.Label>Data eliberării</Form.Label>
                            <Form.Control
                              type="date"
                              value={this.state.dataeliberarii || ''}
                              onChange={(e) => {
                                this.setState({
                                  dataeliberarii: e.target.value,
                                });
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
                              value={this.state.loculnasterii || ''}
                              onChange={(e) => {
                                this.setState({
                                  loculnasterii: e.target.value,
                                });
                              }}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="starecivila">
                            <Form.Label>Stare civilă</Form.Label>
                            <Form.Control
                              as="select"
                              value={this.state.starecivila || 'Necăsătorit'}
                              onChange={(e) => {
                                this.setState({
                                  starecivila: e.target.value,
                                });
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
                              value={this.state.localitate || ''}
                              onChange={(e) => this.onChangeLocalitate(e.target.value)}
                            />
                          </Form.Group>
                        </Col>
                        <Col md={6}>
                          <Form.Group id="judet">
                            <Form.Label>{this.state.tipJudet}</Form.Label>
                            <Form.Control
                              as="select"
                              value={this.state.judet || '-'}
                              onChange={(e) => {
                                this.setState({
                                  judet: e.target.value,
                                });
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
                              value={this.state.adresacompleta || ''}
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
                              value={this.state.telefon || ''}
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
                              value={this.state.email || ''}
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

                  <Row>
                    <Col md={6}>
                      <Button
                        className="mt-2 ml-0"
                        variant={
                          this.state.selectednume === '-' ? 'outline-dark' : 'outline-primary'
                        }
                        onClick={(e) => this.onSubmit(e, this.state.id, this.state.idangajat)}
                        disabled={this.state.selectednume === '-'}
                      >
                        Actualizează datele
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

export default EditPersoana;
