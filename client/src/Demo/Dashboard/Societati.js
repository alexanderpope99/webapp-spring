import React from 'react';
import { Row, Col, Card, Button, Modal, Form } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';
import axios from 'axios';

import { getSocSel, setSocSel } from '../Resources/socsel';
import { judete, sectoare } from '../Resources/judete';
import { server } from '../Resources/server-address';
import { setAngajatSel } from '../Resources/angajatsel';
import { download } from '../Resources/download';
import months from '../Resources/months';
import authHeader from '../../services/auth-header';
import { Edit, PlusCircle } from 'react-feather';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import authService from '../../services/auth.service';

class Societati extends React.Component {
  constructor() {
    super();
    this.unselectAll = this.unselectAll.bind(this);
    this.select = this.select.bind(this);
    this.editSocietate = this.editSocietate.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.statSalarii = this.statSalarii.bind(this);
    this.dec112 = this.dec112.bind(this);
    this.mta = this.mta.bind(this);

    this.state = {
      show: false,
      modalMessage: '',
      isEdit: false,
      show_confirm: false,
      socsel: getSocSel(),

      today: new Date(),

      societati: {},
      id: null,
      nume: '',
      idcaen: '',
      cif: '',
      capsoc: '',
      regcom: '',
      idadresa: null,
      adresa: '',
      localitate: '',
      judet: '',
      capitala: 'Județ',
      email: '',
      telefon: '',
      fax: '',
    };
  }
  clearFields() {
    this.setState({
      id: null,
      nume: '',
      idcaen: '',
      cif: '',
      capsoc: '',
      regcom: '',
      idadresa: null,
      adresa: '',
      localitate: '',
      judet: '',
      capitala: 'Județ',
      email: '',
      telefon: '',
      fax: '',
    });
  }

  onChangeLocalitate(e) {
    if (
      e.toLowerCase() === 'bucuresti' ||
      e.toLowerCase() === 'bucurești' ||
      e.toLowerCase() === 'bucharest'
    )
      this.setState({
        capitala: 'Sector',
        localitate: e,
      });
    else
      this.setState({
        capitala: 'Județ',
        localitate: e,
      });
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        isEdit: false,
      });
    setSocSel(null);
    window.location.reload();
  }

  async componentDidMount() {
    await this.getSocietati();
    let today = new Date();
    this.setState({
      today: today,
      luna: today.getMonth(),
      an: today.getFullYear(),
      user: authService.getCurrentUser(),
    });
  }

  async getSocietati() {
    this.clearFields();
    const user = authService.getCurrentUser();
    let uri = `${server.address}/societate/user/${user.id}`;
    if (user.roles.includes('ROLE_DIRECTOR')) {
      uri = `${server.address}/societate/`;
    }
    const societati_res = await axios
      .get(uri, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.log(err));

    if (Array.isArray(societati_res)) {
			var societati = this.state.societati;
      // var date_societati = this.state.date_societati;
      societati_res.forEach((societate) => {
        societati[societate.nume] = { opacity: '.3', ...societate };
      });
    }
    let selected = getSocSel();
    if (selected) this.select(selected.nume);
  }

  unselectAll() {
    var societati = this.state.societati;
    for (let nume_soc in societati) {
      societati[nume_soc].opacity = '.3';
    }
  }

  select(nume_soc) {
    // unselect all
    var societati = this.state.societati;
    for (let _nume_soc in societati) {
      societati[_nume_soc].opacity = '.3';
    }
    setAngajatSel(null);

    // select nume_soc
    if (nume_soc) {
      societati[nume_soc].opacity = '1';

      setSocSel({ id: societati[nume_soc].id, nume: nume_soc });
      console.log(getSocSel());

      this.setState({ societati: societati, socsel: getSocSel() });
    }
  }

  async deleteSocietate(id) {
    // id = id.replace('"', '');
    // console.log(id);
    const response = axios
      .delete(`${server.address}/societate/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        console.log(response);
        // alert(`Deleted ${id}`);
        setSocSel(null);
        this.setState({ show: false });
        window.location.reload();
      })
      .catch((err) => console.error(err));
  }

  editSocietate(societate) {
    console.log(societate);
    this.setState(
      {
        show: true,
        isEdit: true,

        id: societate.id,
        nume: societate.nume,
        idcaen: societate.idcaen,
        cif: societate.cif,
        capsoc: societate.capsoc,
        regcom: societate.regcom,
        idadresa: societate.adresa ? societate.adresa.id : null,
        adresa: societate.adresa ? societate.adresa.adresa : '',
        localitate: societate.adresa ? societate.adresa.localitate : '',
        judet: societate.adresa ? societate.adresa.judet : '',
        capitala: 'Județ',
        email: societate.email,
        telefon: societate.telefon,
        fax: societate.fax,
      },
      () => (societate.adresa ? this.onChangeLocalitate(societate.adresa.localitate) : null)
    );
  }

  async statSalarii() {
    let luna = this.state.luna;
    let an = this.state.an;
    let user = this.state.user;
    let socsel = this.state.socsel;

    const created = await axios
      .get(`${server.address}/stat/${socsel.id}/mo=${luna + 1}&y=${an}&i=-/${user.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (created) download(`Stat Salarii - ${socsel.nume} - ${months[luna]} ${an}.xlsx`, user.id);
  }

  async dec112() {
    let luna = this.state.luna;
    let an = this.state.an;
    let user = this.state.user;
    let socsel = this.state.socsel;

    const created = await axios
      .get(
        `${server.address}/dec112/${this.state.socsel.id}/mo=${
          luna + 1
        }&y=${an}&drec=0&numeDec=-}&prenumeDec=-&functieDec=-/${this.state.user.id}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (created) download(`Declaratia 112 - ${socsel.nume} - ${months[luna]} ${an}.pdf`, user.id);
  }

  async mta() {
    let luna = this.state.luna;
    let an = this.state.an;
    let user = this.state.user;
    let socsel = this.state.socsel;

    const created = await axios
      .get(`${server.address}/mta/${socsel.id}&mo=${luna + 1}&y=${an}/${user.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (created) download(`FisierMTA - ${socsel.nume} - ${months[luna]} ${an}.xlsx`, user.id);
  }

  async onSubmit(e) {
    try {
      e.preventDefault();
    } catch (err) {
      console.log(err);
    }

    let adresa_body = {
      id: this.state.idadresa,
      adresa: this.state.adresa || null,
      localitate: this.state.localitate || null,
      judet: this.state.judet || null,
    };

    // build societate JSON for POST with adr_id as idadresa
    const societate_body = {
      nume: this.state.nume || null,
      idcaen: Number(this.state.idcaen) || null,
      cif: this.state.cif || null,
      capsoc: this.state.capsoc || null,
      regcom: this.state.regcom || null,
      adresa: adresa_body,
      email: this.state.email || null,
      telefon: this.state.telefon || null,
      fax: this.state.fax || null,
    };

    // UPDATE SOCIETATE
    await axios
      .put(`${server.address}/societate/${this.state.id}`, societate_body, {
        headers: authHeader(),
      })
      .then(() => {
        this.setState(
          {
            show: false,
            show_confirm: true,
            modalMessage: 'Date actualizate!',
          },
          this.getSocietati
        );
      })
      .catch((err) => console.error(err));
  }

  render() {
    const societatiComponent = Object.keys(this.state.societati).map((key) => (
      <Col md={6} xl={4} key={key}>
        <Card
          style={{
            maxBlockSize: 150,
            opacity: this.state.societati[key].opacity,
            cursor: this.state.societati[key].opacity === '1' ? '' : 'pointer',
          }}
          onClick={
            this.state.societati[key].opacity === '.3'
              ? () => {
                  this.select(key);
                }
              : null
          }
        >
          <Card.Body>
            <h3
              style={{
                fontSize:
                  key.length > 25
                    ? (30 - Math.floor(key.length / 25) * 10).toString() + 'px'
                    : '30px',
              }}
              className="d-flex justify-content-around"
            >
              {key}
            </h3>
            <div
              className="mt-4"
              visibility={this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible'}
            >
              <Edit
                className="d-flex justify-content-around float float-right"
                visibility={this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible'}
                style={{ cursor: 'pointer' }}
                onClick={() => this.editSocietate(this.state.societati[key])}
              />

              <Button
                size="sm"
                onClick={this.statSalarii}
                style={{
                  visibility: this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible',
                }}
              >
                Stat salarii
              </Button>
              <Button
                size="sm"
                onClick={this.dec112}
                style={{
                  visibility: this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible',
                }}
              >
                Dec.112
              </Button>
              <Button
                size="sm"
                onClick={this.mta}
                style={{
                  visibility: this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible',
                }}
              >
                MTA
              </Button>
            </div>
          </Card.Body>
        </Card>
      </Col>
    ));

    const addSocietateComponent = (
      <Col md={6} xl={4}>
        <Card
          style={{
            opacity: 0.4,
            cursor: 'pointer',
          }}
          onClick={() => (window.location.href = '/forms/add-societate')}
        >
          <Card.Body className="mt-2 d-flex justify-content-center align-items-center">
            <PlusCircle style={{ width: '80px', height: '80px' }} />
          </Card.Body>
        </Card>
      </Col>
    );

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
        {/* EDIT SOCIETATE MODAL HERE */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Date societate</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Row>
                <Form.Group id="nume" as={Col} md="6">
                  <Form.Label>Nume societate</Form.Label>
                  <Form.Control
                    required
                    type="text"
                    placeholder="Nume"
                    value={this.state.nume || ''}
                    onChange={(e) =>
                      this.setState({
                        nume: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="adresa" as={Col} md="6">
                  <Form.Label>Adresă</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="eg. Strada nr. 1"
                    value={this.state.adresa || ''}
                    onChange={(e) =>
                      this.setState({
                        adresa: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="localitate" as={Col} md="6">
                  <Form.Label>Localitate</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Localitate"
                    value={this.state.localitate || ''}
                    onChange={(e) => this.onChangeLocalitate(e.target.value)}
                  />
                </Form.Group>
                <Form.Group controlId="judet" as={Col} md="6">
                  <Form.Label>{this.state.capitala}</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.judet || ''}
                    onChange={(e) =>
                      this.setState({
                        judet: e.target.value,
                      })
                    }
                  >
                    <option>-</option>
                    {list()}
                  </Form.Control>
                </Form.Group>
                <Form.Group controlId="codCaen" as={Col} md="6">
                  <Form.Label>Cod CAEN</Form.Label>
                  <Form.Control
                    type="number"
                    placeholder="CAEN"
                    value={this.state.idcaen || ''}
                    onChange={(e) =>
                      this.setState({
                        idcaen: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="cif" as={Col} md="6">
                  <Form.Label>CIF</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="CIF"
                    value={this.state.cif || ''}
                    onChange={(e) =>
                      this.setState({
                        cif: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="capSoc" as={Col} md="6">
                  <Form.Label>Capital social</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Capital social"
                    value={this.state.capsoc || ''}
                    onChange={(e) =>
                      this.setState({
                        capsoc: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="regcom" as={Col} md="6">
                  <Form.Label>Registrul comerțului</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="regcom"
                    value={this.state.regcom || ''}
                    onChange={(e) =>
                      this.setState({
                        regcom: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="telefon" as={Col} md="6">
                  <Form.Label>Telefon</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Telefon"
                    value={this.state.telefon || ''}
                    onChange={(e) =>
                      this.setState({
                        telefon: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="email" as={Col} md="6">
                  <Form.Label>E-mail</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="email@email.dom"
                    value={this.state.email || ''}
                    onChange={(e) =>
                      this.setState({
                        email: e.target.value,
                      })
                    }
                  />
                </Form.Group>
                <Form.Group controlId="fax" as={Col} md="6">
                  <Form.Label>Fax</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="fax"
                    value={this.state.fax || ''}
                    onChange={(e) =>
                      this.setState({
                        fax: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <PopupState variant="popover" popupId="demo-popup-popover">
              {(popupState) => (
                <div>
                  <Button variant="outline-danger" {...bindTrigger(popupState)}>
                    Șterge Societatea
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
                      <Typography>Sigur ștergeți societatea {this.state.nume}?</Typography>
                      <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                      <br />
                      <Button
                        variant="outline-danger"
                        onClick={() => {
                          popupState.close();
                          this.deleteSocietate(this.state.id);
                        }}
                        className="mt-2"
                      >
                        Da
                      </Button>
                      <Button
                        variant="outline-secondary"
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
            <Button variant="primary" onClick={this.onSubmit}>
              Actualizează
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CONFIRM MODAL */}
        <Modal show={this.state.show_confirm} onHide={() => this.handleClose(true)}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          {societatiComponent}
          {addSocietateComponent}
        </Row>
      </Aux>
    );
  }
}

export default Societati;
