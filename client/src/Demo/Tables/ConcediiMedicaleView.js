import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Eye, RotateCw } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getAngajatSel } from '../Resources/angajatsel';
import { luni, formatDate } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import {
  getProcente,
  getZileFirma,
  countWeekendDays,
} from '../Resources/cm.js';

class ConcediiMedicaleView extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.viewCM = this.viewCM.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.onChangeCodboala = this.onChangeCodboala.bind(this);
    this.onChangeProcent = this.onChangeProcent.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);

    this.state = {
      angajat: getAngajatSel(),

      an: '',
      luna: { nume: '-', nr: '-' },
      ani_cu_concediu: [],
      luni_cu_concediu: { '': [] },

      cm: [],
      cmComponent: null,

      // cm modal:
      show: false,
      isEdit: false,
      id: '',
      // cm modal fields
      today: '',
      dela: '',
      panala: '',
      nr_zile: 0,
      nr_zile_weekend: 0,
      continuare: false,
      datainceput: '',
      serie: '',
      nr: '',
      dataeliberare: '',
      codurgenta: '',
      codboala: '01-Boala obisnuita si accidente in afara muncii',
      procent: '75',
      procente: ['75'],
      codboalainfcont: '',
      bazacalcul: 0,
      bazacalculplafonata: 0,
      zilebazacalcul: 0,
      mediezilnica: 0,
      zilefirma: 0,
      zilefnuass: 0,
      zilefaambp: 0,
      codindemnizatie: '',
      indemnizatiefirma: 0,
      indemnizatiefnuass: 0,
      indemnizatiefaambp: 0,
      locprescriere: 'Medic de familie',
      nravizmedic: '',
      urgenta: false,
      conditii: '',
      cnpcopil: '',
      idcontract: null,

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }

  clearCM() {
    this.setState({
      isEdit: false,
      // cm modal fields
      id: '',

      dela: '',
      panala: '',
      nr_zile: 0,
      continuare: false,
      datainceput: '',
      serie: '',
      nr: '',
      dataeliberare: '',
      codurgenta: '',
      codboala: '01-Boala obisnuita si accidente in afara muncii',
      procent: '75',
      procente: ['75'],
      codboalainfcont: '',
      bazacalcul: 0,
      bazacalculplafonata: 0,
      zilebazacalcul: 0,
      mediezilnica: 0,
      zilefirma: 0,
      zilefnuass: 0,
      zilefaambp: 0,
      codindemnizatie: '',
      indemnizatiefirma: 0,
      indemnizatiefnuass: 0,
      locprescriere: 'Medic de familie',
      nravizmedic: '',
      urgenta: false,
      conditii: '',
      cnpcopil: '',
      idcontract: null,
    });
  }

  numberWithCommas(x) {
    if (!x) return 0;
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  setCurrentYear() {
    let today = new Date();

    this.setState({
      an: today.getFullYear(),
      today: today.toISOString().substring(0, 10),
    });
  }

  async updateAngajatSel() {
    let angajatSel = getAngajatSel();

    if (angajatSel) {
      let angajat = await axios
        .get(`${server.address}/angajat/${angajatSel.idpersoana}`, { headers: authHeader() })
        .then((res) => (res.status === 200 ? res.data : null))
        .catch((err) => console.error(err));
      if (angajat) {
        this.setState(
          { angajat: { ...angajat, numeintreg: getAngajatSel().numeintreg } },
          this.fillTable
        );
      }
    } else {
      this.setState({ angajat: null }, this.fillTable);
    }
  }

  componentDidMount() {
    this.setCurrentYear();
    this.updateAngajatSel();
  }

  onChangeAn(an) {
    this.setState({ an: an, luna: { nume: '-', nr: '-' } }, this.renderCM);
  }

  onChangeMonth(e) {
    const selectedIndex = e.target.options.selectedIndex;
    this.setState(
      {
        luna: {
          nume: e.target.value,
          nr: Number(e.target.options[selectedIndex].getAttribute('data-key')),
        },
      },
      () => {
        this.renderCM();
      }
    );
  }

  onChangeDela(dela) {
    if (!this.state.dela || dela > this.state.panala)
      this.setState({ dela: dela, panala: dela }, this.setNrZile);
    else this.setState({ dela: dela }, this.setNrZile);
  }
  onChangePanala(panala) {
    this.setState({ panala: panala }, this.setNrZile);
  }

  setNrZile() {
    // Math.round(this.state.zilefirma * baza_calcul.mediezilnica)
    let panala = this.state.panala;
    let dela = this.state.dela;
    var nr_zile = 0,
      nr_zile_weekend = 0,
      zilefirma = 0,
      zilefnuass = 0,
      zilefaambp = 0,
      indemnizatiefirma = 0,
      indemnizatiefnuass = 0,
      indemnizatiefaambp = 0;
    if (dela && panala) {
      let date1 = new Date(dela);
      let date2 = new Date(panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
      nr_zile_weekend = countWeekendDays(date1, date2);
      [zilefirma, zilefnuass, zilefaambp] = getZileFirma(
        date1,
        date2,
        this.state.codboala.substring(0, 2)
      );
      if (this.state.mediezilnica) {
        indemnizatiefirma = Math.round(
          (zilefirma * Math.round(this.state.mediezilnica) * this.state.procent) / 100
        );
        indemnizatiefnuass = Math.round(
          (zilefnuass * Math.round(this.state.mediezilnica) * this.state.procent) / 100
        );
        indemnizatiefaambp = Math.round(
          (zilefaambp * Math.round(this.state.mediezilnica) * this.state.procent) / 100
        );
      }
    }

    this.setState({
      nr_zile: nr_zile,
      nr_zile_weekend: nr_zile_weekend,
      zilefirma: zilefirma,
      zilefnuass: zilefnuass,
      zilefaambp: zilefaambp,
      indemnizatiefirma: indemnizatiefirma,
      indemnizatiefnuass: indemnizatiefnuass,
      indemnizatiefaambp: indemnizatiefaambp,
    });
  }

  onChangeCodboala(cod) {
    let procente = getProcente(cod);
    this.setState(
      {
        codboala: cod,
        procente: procente,
        procent: procente[0],
      },
      this.setNrZile
    );
  }

  onChangeProcent(proc) {
    this.setState(
      {
        procent: proc,
      },
      this.setNrZile
    );
  }

  async fillTable() {
    if (!this.state.angajat) {
      this.setState({ cm: [] }, this.renderCM);
      return;
    }

    if (!this.state.angajat.idcontract) {
      this.setState({ cm: [] }, this.renderCM);
      return;
    }

    const cm = await axios
      .get(`${server.address}/cm/idc=${this.state.angajat.idcontract}`, { headers: authHeader() })
      // eslint-disable-next-line eqeqeq
      .then((res) => (res.status == 200 ? res.data : null))
      .catch((err) => console.error('err', err));
    if (cm) {
      var ani_cu_concediu = new Set();
      var luni_cu_concediu = {};
      // add ani_cu_concediu
      for (let c of cm) {
        if (c.dela) {
          // an = c.dela.substring(0, 4);
          ani_cu_concediu.add(Number(c.dela.substring(0, 4)));
        }
        if (c.panala) {
          ani_cu_concediu.add(Number(c.panala.substring(0, 4)));
        }
      }
      // add ani in luni_cu_concediu
      for (let _an of ani_cu_concediu) {
        luni_cu_concediu[_an] = new Set();
      }
      // add luni in luni_cu_concediu
      let an;
      for (let c of cm) {
        if (c.dela) {
          an = c.dela.substring(0, 4);
          luni_cu_concediu[an].add(Number(c.dela.substring(5, 7)));
        }
      }
      // convert to array from set
      for (let _an of ani_cu_concediu) {
        luni_cu_concediu[_an] = [...luni_cu_concediu[_an]];
      }
			
			let thisYear = new Date().getFullYear();
			ani_cu_concediu.add(thisYear);
			
      this.setState(
        {
          cm: cm,
          ani_cu_concediu: [...ani_cu_concediu],
          luni_cu_concediu: luni_cu_concediu,
        },
        this.renderCM
      );
    } else {
      this.setState(
        {
          cm: [],
          ani_cu_concediu: [],
          luni_cu_concediu: { '': [] },
        },
        this.renderCM
      );
    }
  }

  async deleteCM(id) {
    await axios
      .delete(`${server.address}/cm/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async viewCM(cm) {
    if (!this.state.angajat) return;
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }

    for (let key in cm) if (cm[key] === '-') cm[key] = '';

    this.setState(
      {
        id: cm.id,
        dela: cm.dela.substring(0, 10),
        panala: cm.panala.substring(0, 10),
        continuare: cm.continuare || false,
        datainceput: cm.datainceput.substring(0, 10),
        serie: cm.serie,
        nr: cm.nr,
        codindemnizatie: cm.codindemnizatie,
        cnpcopil: cm.cnpcopil,
        dataeliberare: cm.dataeliberare.substring(0, 10),
        codurgenta: cm.codurgenta,
        procent: cm.procent,
        procente: getProcente(cm.codboala),
        codboalainfcont: cm.codboalainfcont,
        bazacalcul: cm.bazacalcul,
        bazacalculplafonata: cm.bazacalculplafonata,
        zilebazacalcul: cm.zilebazacalcul,
        mediezilnica: cm.mediezilnica,
        zilefirma: cm.zilefirma,
        indemnizatiefirma: cm.indemnizatiefirma,
        zilefnuass: cm.zilefnuass,
        indemnizatiefnuass: cm.indemnizatiefnuass,
        zilefaambp: cm.zilefaambp,
        indemnizatiefaambp: cm.indemnizatiefaambp,
        locprescriere: cm.locprescriere,
        nravizmedic: cm.nravizmedic,
        codboala: cm.codboala,
        urgenta: cm.urgenta || false,
        conditii: cm.conditii,
        idcontract: cm.idcontract,

        isEdit: true,
        show: true,
      },
      this.setNrZile
    );
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
  }
  // function to create react component with fetched data
  renderCM() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      cmComponent: this.state.cm.map((cm, index) => {
        if (
          cm.dela
            ? cm.dela.includes(this.state.an) &&
              (this.state.luna.nume !== '-'
                ? // eslint-disable-next-line eqeqeq
                  cm.dela.substring(5, 7) == this.state.luna.nr
                : true)
            : true
        ) {
          for (let key in cm) {
            if (!cm[key]) cm[key] = '-';
          }
          return (
            <tr key={cm.id}>
              <th className="d-inline-flex flex-row justify-content-around">
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  onClick={() => this.viewCM(cm)}
                >
                  <Eye size={20} />
                </Button>
              </th>
              <th>{formatDate(cm.dela)}</th>
              <th>{formatDate(cm.panala)}</th>
              <th>{cm.continuare === '-' ? 'Nu' : 'Da'}</th>
              <th>{formatDate(cm.datainceput)}</th>
              <th>{cm.serie}</th>
              <th>{cm.nr}</th>
              <th>{formatDate(cm.dataeliberare)}</th>
              <th>{cm.codboala}</th>
              <th>{cm.codurgenta}</th>
              <th>{cm.codboalainfcont}</th>
              <th>{cm.procent}%</th>
              <th>{cm.bazacalcul}</th>
              <th>{cm.bazacalculplafonata}</th>
              <th>{cm.zilebazacalcul}</th>
              <th>{cm.mediezilnica}</th>
              <th>{cm.codindemnizatie}</th>
              <th>{cm.zilefirma}</th>
              <th>{cm.indemnizatiefirma}</th>
              <th>{cm.zilefnuass}</th>
              <th>{cm.indemnizatiefnuass}</th>
              <th>{cm.zilefaambp}</th>
              <th>{cm.indemnizatiefaambp}</th>
              <th>{cm.nravizmedic}</th>
              <th>{cm.locprescriere}</th>
              <th>{cm.urgenta === '-' ? 'Nu' : 'Da'}</th>
              <th>{cm.conditii}</th>
              <th>{cm.cnpcopil}</th>
            </tr>
          );
        }
      }),
    });
  }

  render() {
    var monthsComponent = [];
    if (this.state.luni_cu_concediu[this.state.an]) {
      monthsComponent = this.state.luni_cu_concediu[this.state.an].map((luna, index) => (
        <option key={index} data-key={Number(luna)}>
          {luni[luna - 1]}
        </option>
      ));
    }

    const yearsComponent = this.state.ani_cu_concediu.map((an, index) => (
      <option key={index}>{an}</option>
    ));

    let exists = this.state.angajat && this.state.angajat.idcontract;

    return (
      <Aux>
        {/* VIEW MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Concediu medical</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Row>
                <Form.Group id="dela" as={Col} md="6">
                  <Form.Label>Începând cu (inclusiv)</Form.Label>
                  <Form.Control
										disabled
                    type="date"
                    value={this.state.dela}
                    max={this.state.panala}
                  />
                </Form.Group>
                <Form.Group id="panala" as={Col} md="6">
                  <Form.Label>Până la (inclusiv)</Form.Label>
                  <Form.Control
										disabled
                    type="date"
                    value={this.state.panala}
                    min={this.state.dela}
                  />
                </Form.Group>
                <Form.Group as={Col} md="12">
                  <Form.Label>
                    {this.state.nr_zile +
                      (this.state.nr_zile > 1
                        ? ` zile concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`
                        : ` zi concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`)}
                  </Form.Label>
                </Form.Group>
                <Form.Group id="continuare" as={Col} md="2" className="mt-4">
                  <Form.Check
										disabled
                    custom
                    type="switch"
                    id="continuareCheck"
                    label="Continuare"
                    checked={this.state.continuare}
                  />
                </Form.Group>
                <Form.Group id="datainceput" as={Col} md="4">
                  <Form.Label>Dată început</Form.Label>
                  <Form.Control
										disabled
                    type="date"
                    value={this.state.datainceput}
                  />
                </Form.Group>
                <Form.Group id="dataeliberare" as={Col} md="6">
                  <Form.Label>Dată eliberare</Form.Label>
                  <Form.Control
										disabled
                    type="date"
                    value={this.state.dataeliberare}
                  />
                </Form.Group>
                <Form.Group id="seriecertificat" as={Col} md="6">
                  <Form.Label>Serie certificat</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.serie}
                  />
                </Form.Group>
                <Form.Group id="nrcertificat" as={Col} md="6">
                  <Form.Label>Număr certificat</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.nr}
                  />
                </Form.Group>
                <Form.Group id="codboala" as={Col} md="6">
                  <Form.Label>Cod boală</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.codboala}
                  />
                </Form.Group>
                <Form.Group id="codurgenta" as={Col} md="6">
                  <Form.Label>Cod urgență</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.codurgenta}
                  />
                </Form.Group>
                <Form.Group id="codboalainfcont" as={Col} md="6">
                  <Form.Label>Cod boală infecțioasă/contagioasă</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.codboalainfcont}
                  />
                </Form.Group>
                <Form.Group id="procent" as={Col} md="6">
                  <Form.Label>Procent %</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.procent}
                  />
                </Form.Group>
                <Row className="border rounded pt-3 pb-3 m-3">
                  <Form.Group id="bazacalcul" as={Col} md="6">
                    <Form.Label>Bază calcul (RON)</Form.Label>
                    <Form.Control
										disabled
										type="text"
										value={this.numberWithCommas(this.state.bazacalcul)}
                    />
                  </Form.Group>
                  <Form.Group id="bazacalculplafonata" as={Col} md="6">
                    <Form.Label>Bază calcul plafonată (RON)</Form.Label>
                    <Form.Control
											disabled
											type="text"
                      value={this.state.bazacalculplafonata}
                    />
                  </Form.Group>
                  <Form.Group id="zilebazacalcul" as={Col} md="6">
                    <Form.Label>Zile bază calcul</Form.Label>
                    <Form.Control
										disabled
										type="text"
                      value={this.state.zilebazacalcul}
                    />
                  </Form.Group>
                  <Form.Group id="mediezilnica" as={Col} md="6">
                    <Form.Label>Medie zilnică (RON)</Form.Label>
                    <Form.Control
										disabled
										type="text"
										value={this.state.mediezilnica}
                    />
                  </Form.Group>
                </Row>
                <Form.Group id="zilefirma" as={Col} md="6">
                  <Form.Label>Zile suportate de firmă</Form.Label>
                  <Form.Control
										disabled
                    type="number"
                    value={this.state.zilefirma}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefirma" as={Col} md="6">
                  <Form.Label>Indemnizație firmă</Form.Label>
                  <Form.Control
										disabled
                    type="number"
                    value={this.state.indemnizatiefirma}
                  />
                </Form.Group>
                <Form.Group id="zilefnuass" as={Col} md="6">
                  <Form.Label>Zile FNUASS</Form.Label>
                  <Form.Control
										disabled
                    type="number"
                    value={this.state.zilefnuass}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefnuass" as={Col} md="6">
                  <Form.Label>Indemnizație FNUASS</Form.Label>
                  <Form.Control
										disabled
                    type="number"
                    value={this.state.indemnizatiefnuass}
                  />
                </Form.Group>
                <Form.Group id="zilefaambp" as={Col} md="6">
                  <Form.Label>Zile FAAMBP</Form.Label>
                  <Form.Control
                    disabled
                    type="number"
                    value={this.state.zilefaambp}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefaambp" as={Col} md="6">
                  <Form.Label>Indemnizație FAAMBP</Form.Label>
                  <Form.Control
										disabled
                    type="number"
                    value={this.state.indemnizatiefaambp}
                  />
                </Form.Group>
                <Form.Group id="codindemnizatie" as={Col} md="6">
                  <Form.Label>Cod indemnizație</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.codindemnizatie}
                  />
                </Form.Group>
                <Form.Group id="locprescriere" as={Col} md="6">
                  <Form.Label>Loc prescriere</Form.Label>
                  <Form.Control
										disabled
                    number="text"
                    value={this.state.locprescriere}
                  />
                </Form.Group>
                <Form.Group id="nravizmedic" as={Col} md="6">
                  <Form.Label>Nr. aviz medical</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.nravizmedic}
                  />
                </Form.Group>
                <Form.Group id="urgenta" as={Col} md="6" className="mt-4">
                  <Form.Check
										disabled
                    custom
                    type="switch"
                    id="urgentaCheck"
                    label="Urgență"
                    checked={this.state.urgenta}
                  />
                </Form.Group>
                <Form.Group id="conditii" as={Col} md="6">
                  <Form.Label>Condiții</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.conditii}
                  />
                </Form.Group>
                <Form.Group id="cnpcopil" as={Col} md="6">
                  <Form.Label>CNP Copil</Form.Label>
                  <Form.Control
										disabled
                    type="text"
                    value={this.state.cnpcopil}
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Concedii medicale</Card.Title>
                <Button
                  variant={exists ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!exists}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Row>
                  <Form.Group as={Col} sm="auto" className="mt-3">
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.an}
                      onChange={(e) => this.onChangeAn(e.target.value)}
                    >
                      <option>-</option>
                      {yearsComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group as={Col} sm="auto" className="mt-3">
                    <Form.Label>Luna</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.luna.nume}
                      onChange={(e) => this.onChangeMonth(e)}
                    >
                      <option>-</option>
                      {monthsComponent}
                    </Form.Control>
                  </Form.Group>
                </Row>
              </Card.Header>

              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Începând cu (inclusiv)</th>
                      <th>Până la (inclusiv)</th>
                      <th>Continuare</th>
                      <th>Data început</th>
                      <th>Serie certificat</th>
                      <th>Nr. certificat</th>
                      <th>Data eliberare</th>
                      <th>Cod boală</th>
                      <th>Cod urgență</th>
                      <th>Cod boală infecțioasă/contagioasă</th>
                      <th>Procent</th>
                      <th>Bază calcul</th>
                      <th>Bază calcul plafonată</th>
                      <th>Zile bază calul</th>
                      <th>Medie zilnică</th>
                      <th>Cod indemnizație</th>
                      <th>Zile firmă</th>
                      <th>Indemnizație firmă</th>
                      <th>Zile FNUASS</th>
                      <th>Indemnizație FNUASS</th>
                      <th>Zile FAAMBP</th>
                      <th>Indemnizație FAAMBP</th>
                      <th>Nr. aviz medic</th>
                      <th>Loc prescriere</th>
                      <th>Urgență</th>
                      <th>Condiții</th>
                      <th>CNP Copil</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.cmComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default ConcediiMedicaleView;
