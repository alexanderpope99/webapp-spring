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
  Table,
} from 'react-bootstrap';
import Add from '@material-ui/icons/Add';
import Aux from '../../hoc/_Aux';
import months from '../Resources/months';
import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
import { Typography } from '@material-ui/core';

class RealizariRetineri extends React.Component {
  constructor() {
    super();
    this.setCurrentYearMonth = this.setCurrentYearMonth.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);
    this.recalculeaza = this.recalculeaza.bind(this);
    this.veziOreSuplimentare = this.veziOreSuplimentare.bind(this);
    this.handleClose = this.handleClose.bind(this);

    this.state = {
      socsel: getSocSel(),

      show: false,
      modalMessage: '',

      an: '',
      luna: '',

      selected_angajat: '',
      lista_angajati: [], // object: {nume, id}
      idcontract: '',

      // realizari
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilecm: '',
      zileco: '',
      zileconeplatit: '',
      zilec: '',
      oresuplimentare: [], // user input
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      // retineri
      avansnet: 0,
      pensiefacultativa: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',
    };
  }
  clearForm() {
    this.setState({
      idcontract: '',

      // realizari
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilecm: '',
      zileco: '',
      zileconeplatit: '',
      zilec: '',
      oresuplimentare: 0, // user input
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      // retineri
      avansnet: 0,
      pensiefacultativa: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',
    });
  }

  componentDidMount() {
    this.setCurrentYearMonth(); // modifies state.an, state.luna
    this.setPersoane(); // date personale, also fills lista_angajati
  }

  numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  setCurrentYearMonth() {
    let today = new Date();
    let luna = months[today.getMonth()];
    let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  async setPersoane() {
    //* only people with contract <- done on backend
    const persoane = await fetch(`${server.address}/persoana/ids=${this.state.socsel.id}&c`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error(err));

    //* set lista_angajati
    let lista_angajati = [];
    for (let persoana of persoane) {
      lista_angajati.push({ nume: persoana.nume + ' ' + persoana.prenume, id: persoana.id });
    }
    this.setState({ lista_angajati: lista_angajati });
  }

  async fillForm() {
    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat.id;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    

    // get contract by idpersoana :: contract body needed for 4 fields
    const contract = await fetch(`${server.address}/contract/idp=${idpersoana}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => (res.ok ? res.json() : null))
      .catch((err) => console.error(err));
    console.log('contract:', contract);

    // TODO: get ore suplimentare by idcontract
    const oresuplimentare = await fetch(`${server.address}/oresuplimentare/idc=${contract.id}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(res => (res.ok ? res.json() : null))
      .catch(err => console.error(err));
    console.log(oresuplimentare);

    // get date realizariretineri
    const data = await fetch(
      `${server.address}/realizariretineri/idc=${contract.id}&mo=${luna}&y=${an}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }
    )
      .then((res) => (res.ok ? res.json() : null))
      .catch((err) => console.error(err));

    console.log('data:', data);

    this.setState({
      //* realizari
      functie: contract.functie,
      duratazilucru: contract.normalucru,
      normalucru: data.norma, // zile lucratoare in luna respectiva
      salariubrut: contract.salariutarifar,
      orelucrate: data.orelucrate,
      salariupezi: data.salariupezi,
      salariupeora: data.salariupeora,
      nrtichete: data.nrtichete,
      zilecm: data.zilecm,
      zileco: data.zileco,
      zileconeplatit: data.zileconeplatit,
      zilec: data.zilec,

      //* retineri
      // avansnet: 0,
      // pensiefacultativa: 0,
      // pensiealimentara: 0,
      // popriri: 0,
      // imprumuturi: 0,
      deducere: data.deducere,
      nrpersoaneintretinere: data.nrpersoaneintretinere,

      //* total
      totaldrepturi: data.totaldrepturi,
      cas: data.cas,
      cass: data.cass,
      valoaretichete: data.valoaretichete,
      impozit: data.impozit,
      restplata: data.restplata,
      cam: data.cam,

      //
      idcontract: contract.id,
      oresuplimentare: oresuplimentare,
    });
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  onSelect(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idangajat = e.target.options[selectedIndex].getAttribute('data-key');

    this.setState(
      {
        selected_angajat: {
          nume: e.target.value,
          id: idangajat,
        },
      },
      () => this.fillForm()
    );
  }

  async recalculeaza() {
    console.log('recalculez...');

    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat.id;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    let ttd = this.state.primabruta;
    let nrt = this.state.nrtichete;

    const data = await fetch(
      `${server.address}/realizariretineri/calc/idp=${idpersoana}&mo=${luna}&y=${an}&ttd=${ttd}&nrt=${nrt}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }
    )
      .then((res) => (res.ok ? res.json() : null))
      .catch((err) => console.error(err));

    console.log(data);
    // totaldrepturi
    this.setState({
      totaldrepturi: data.totaldrepturi,
      restplata: data.restplata,
      cas: data.cas,
      cass: data.cass,
      cam: data.cam,
      impozit: data.impozit,
      valoaretichete: data.valoaretichete,
    });
  }

  veziOreSuplimentare() {
    console.log('editez ore suplimentare');
    this.setState({
      show: true,
    });
  }

  onSubmit(e) {
    e.preventDefault();

    // POST/PUT ore suplimentare + prima bruta + nr tichete + avans + luna+an
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    const tabel_ore = this.state.oresuplimentare;

    const nume_persoane_opt = this.state.lista_angajati.map((angajat) => (
      <option key={angajat.id} data-key={angajat.id}>
        {angajat.nume}
      </option>
    ));

    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Ore suplimentare</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Table responsive hover>
              <thead>
                <tr>
                  <th>nr.</th>
                  <th>procent</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>{tabel_ore}</tbody>
            </Table>
          </Modal.Body>
        </Modal>

        <Card>
          {/* SELECT LUNA + AN */}
          <Card.Header>
            <Row>
              {/* LUNA */}
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.luna.nume}
                  onChange={(e) =>
                    this.setState(
                      {
                        luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
                      },
                      this.fillForm
                    )
                  }
                >
                  {luni}
                </FormControl>
              </Col>
              {/* AN */}
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.an}
                  onChange={(e) =>
                    this.setState({
                      an: e.target.value,
                    })
                  }
                >
                  {ani}
                </FormControl>
              </Col>
            </Row>
          </Card.Header>
          {/* SELECT ANGAJAT */}
          <Card.Header>
            <Card.Title as="h4">Angajat</Card.Title>
            <InputGroup className="mb-3">
              {/* NUMELE ANGAJATILOR CU CONTRACT */}
              <FormControl
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_angajat.nume}
                onChange={(e) => this.onSelect(e)}
              >
                <option>-</option>
                {/* lista_angajati mapped as <option> */}
                {nume_persoane_opt}
              </FormControl>
              <InputGroup.Append>
                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
                  overlay={
                    <Tooltip id="update-button" style={{ opacity: '.4' }}>
                      Adaugă angajat
                    </Tooltip>
                  }
                >
                  <Button href="/forms/angajat" variant="outline-info" className="pb-0">
                    <Add fontSize="small" className="m-0" />
                  </Button>
                </OverlayTrigger>
              </InputGroup.Append>
            </InputGroup>
          </Card.Header>

          <Card.Body>
            <Form onSubmit={this.onSubmit}>
              <Row>
                {/* REALIZARI = LEFT TOP */}
                <Col md={8} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Realizări
                  </Typography>
                  <Row>
                    <Col md={6}>
                      <Form.Group id="functie">
                        <Form.Label>Funcție</Form.Label>
                        <Form.Control type="text" disabled value={this.state.functie || '-'} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="duratailucru">
                        <Form.Label>Durată zi lucru</Form.Label>
                        <Form.Control
                          type="number"
                          disabled
                          value={this.state.duratazilucru || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="normazilucru">
                        <Form.Label>Normă lucru</Form.Label>
                        <Form.Control type="text" disabled value={this.state.normalucru || ''} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="salariubrut">
                        <Form.Label>Salariu brut</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.salariubrut
                              ? this.numberWithCommas(this.state.salariubrut)
                              : ''
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="orelucrate">
                        <Form.Label>Ore lucrate</Form.Label>
                        <Form.Control
                          type="number"
                          disabled
                          value={
                            this.state.orelucrate -
                            this.state.duratazilucru *
                              (Number(this.state.zilelibere) + Number(this.state.zileinvoire))
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="tichete">
                        <Form.Label>Nr. Tichete</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={
                            this.state.nrtichete - this.state.zilelibere - this.state.zileinvoire <
                            0
                              ? 0
                              : this.state.nrtichete -
                                this.state.zilelibere -
                                this.state.zileinvoire
                          }
                          onChange={(e) => this.setState({ nrtichete: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilecm">
                        <Form.Label>Zile concediu medical</Form.Label>
                        <InputGroup>
                          <Form.Control type="number" disabled min="0" value={this.state.zilecm} />
                          {this.state.zilecm ? (
                            <InputGroup.Append>
                              <InputGroup.Text style={{ fontSize: '0.75rem' }}>
                                Brut: {(this.state.zilecm * this.state.salariupezi).toFixed(0)} RON
                              </InputGroup.Text>
                            </InputGroup.Append>
                          ) : null}
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileco">
                        <Form.Label>Zile concediu odihna</Form.Label>
                        <InputGroup>
                          <Form.Control type="number" disabled min="0" value={this.state.zileco} />
                          {this.state.zileco ? (
                            <InputGroup.Append>
                              <InputGroup.Text style={{ fontSize: '0.75rem' }}>
                                Sumă brută:{' '}
                                {(this.state.zileconeplatit * this.state.salariupezi).toFixed(0)}{' '}
                                RON
                              </InputGroup.Text>
                            </InputGroup.Append>
                          ) : null}
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileconeplatit">
                        <Form.Label>Zile libere</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.zilelibere}
                          onChange={(e) => this.setState({ zilelibere: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileinvoire">
                        <Form.Label>Zile învoire</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.zileinvoire}
                          onChange={(e) => this.setState({ zileinvoire: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    {/* TODO */}
                    <Col md={6}>
                      <Form.Group id="oresuplimentare">
                        <Form.Label>Ore suplimentare</Form.Label>
                        <InputGroup>
                          <Form.Control type="text" disabled value={this.state.oresuplimentare} />
                          <InputGroup.Append>
                            <Button
                              variant={
                                this.state.selected_angajat ? 'outline-secondary' : 'outline-light'
                              }
                              disabled={!this.state.selected_angajat}
                              onClick={() => this.veziOreSuplimentare()}
                            >
                              Vezi
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="primabruta">
                        <Form.Label>Primă brută</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.primabruta}
                          onChange={(e) => this.setState({ primabruta: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>

                {/* RETINERI = RIGHT */}
                <Col md={4} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Rețineri
                  </Typography>
                  <Row>
                    <Col md={12}>
                      <Form.Group id="avansnet">
                        <Form.Label>Avans</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.avansnet}
                          onChange={(e) => this.setState({ avansnet: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiefacultativa">
                        <Form.Label>Pensie facultativă</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.pensiefacultativa}
                          onChange={(e) => this.setState({ pensiefacultativa: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiealimentara">
                        <Form.Label>Pensie alimentară</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.pensiealimentara}
                          onChange={(e) => this.setState({ pensiealimentara: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="popriri">
                        <Form.Label>Popriri</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.popriri}
                          onChange={(e) => this.setState({ popriri: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="imprumuturi">
                        <Form.Label>Împrumuturi</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.imprumuturi}
                          onChange={(e) => this.setState({ imprumuturi: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="deducere">
                        <Form.Label>Deducere personală</Form.Label>
                        <Form.Control type="number" disabled min="0" value={this.state.deducere} />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>

                {/* TOTAL = LEFT BOTTOM */}
                <Col md={12} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Total
                  </Typography>
                  <Row>
                    <Col md={6}>
                      <Form.Group id="totaltrepturi">
                        <Form.Label>Total drepturi</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.totaldrepturi
                              ? this.numberWithCommas(this.state.totaldrepturi)
                              : ''
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="restplata">
                        <Form.Label>Rest de plată</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.restplata
                              ? this.numberWithCommas(
                                  Number(this.state.restplata) - Number(this.state.avansnet)
                                )
                              : ''
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cas">
                        <Form.Label>CAS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.cas ? this.numberWithCommas(this.state.cas) : ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cass">
                        <Form.Label>CASS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.cass ? this.numberWithCommas(this.state.cass) : ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="valoaretichete">
                        <Form.Label>Valoare tichete</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.valoaretichete
                              ? this.numberWithCommas(this.state.valoaretichete)
                              : ''
                          }
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="impozit">
                        <Form.Label>Impozit</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.impozit ? this.numberWithCommas(this.state.impozit) : ''
                          }
                        />
                      </Form.Group>
                    </Col>

                    <Col md={4}>
                      <Form.Group id="cam">
                        <Form.Label>CAM</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.cam ? this.numberWithCommas(this.state.cam) : ''}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Button
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.selected_angajat}
                    onClick={this.recalculeaza}
                    className="mb-3 float-right"
                  >
                    Recalculează
                  </Button>
                </Col>

                {/* BUTTONS */}
                <Col md={12} className="m-0 ml-0 mt-3">
                  <Button
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.selected_angajat}
                    type="submit"
                  >
                    Salvează
                  </Button>
                </Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}

export default RealizariRetineri;
