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
  Collapse,
  Breadcrumb,
} from 'react-bootstrap';
import { Info } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { luni } from '../Resources/calendar';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel, setAngajatSel } from '../Resources/angajatsel';
import { server } from '../Resources/server-address';
import { Typography } from '@material-ui/core';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class RealizariRetineriView extends React.Component {
  constructor() {
    super();

    this.setYearsAndMonths = this.setYearsAndMonths.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);
    this.veziOreSuplimentare = this.veziOreSuplimentare.bind(this);
    this.veziPensieFacultativa = this.veziPensieFacultativa.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.getOresuplimentare = this.getOresuplimentare.bind(this);
    this.renderTabelore = this.renderTabelore.bind(this);
    this.getStatIndividual = this.getStatIndividual.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      detaliiAccordion: false,
      show: false,
      showPensie: false,
      modalMessage: '',
      showToast: false,
      toastMessage: '',

      an: '',
      luna: '',
      lunaan: [],
      luni: [],

      selected_angajat: getAngajatSel(),
      lista_angajati: [], // object: {nume, id}
      contract: null,
      idstat: '',

      // realizari
      an_inceput_contract: '',
      luna_inceput_contract: '',
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilec: '',
      zilecm: '',
      zilecmlucratoare: '',
      valcm: '',
      zileco: '',
      zilecolucratoare: '',
      zilecfp: '',
      zilecfplucratoare: '',
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: 100,
      totaloresuplimentare: 0,

      // retineri
      idretineri: 0,
      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,
      totalpensiefacultativa: 0,
      impozitdedus: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',

      // detalii
      bazaimpozit: '',
      impozitscutit: '',
      salariurealizat: '',
      venitnet: '',
      zilelucrate: '',
      zileplatite: '',
    };
  }
  clearForm() {
    this.setState({
      idstat: 0,
      // realizari
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilec: '',
      zilecm: '',
      zilecmlucratoare: '',
      valcm: '',
      zileco: '',
      zilecolucratoare: '',
      zilecfp: '',
      zilecfplucratoare: '',
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: 100,
      totaloresuplimentare: 0,

      // retineri
      idretineri: 0,
      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,
      impozitdedus: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',
      detalii: null,

      // detalii
      bazaimpozit: '',
      impozitscutit: '',
      salariurealizat: '',
      venitnet: '',
      zilelucrate: '',
      zileplatite: '',
    });
  }
  clearUserInput() {
    this.setState({
      oresuplimentare: [],
      zileinvoire: 0,
      primabruta: 0,
      zilelibere: 0,

      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
    });
  }

  async componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    await this.setYearsAndMonths(); // modifies state.an, state.luna
    await this.setPersoane(); // date personale, also fills lista_angajati
    this.fillForm();
  }

  async setYearsAndMonths() {
    const rr = await axios
      .get(
        `${server.address}/realizariretineri/luni-ani/ids=${this.state.socsel.id}&idu=${this.state.user.id}`,
        { headers: authHeader() }
      )
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

    this.setState({
      lunaan: rr,
      an: rr[rr.length - 1].an,
      luna: {
        nume: luni[rr[rr.length - 1].luna[rr[rr.length - 1].luna.length - 1]],
        nr: rr[rr.length - 1].luna[rr[rr.length - 1].luna.length - 1],
      },
      luni: rr[rr.length - 1].luna,
    });
  }

  async setPersoane() {
    // va fi un array de 1 element
    const angajati = await axios
      .get(
        `${server.address}/angajat/socid=${this.state.socsel.id}/usrid=${this.state.user.id}&c`,
        { headers: authHeader() }
      )
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));
    if (!angajati) return;

    let lista_angajati = [];
    for (let angajat of angajati) {
      lista_angajati.push({
        nume: angajat.persoana.nume + ' ' + angajat.persoana.prenume,
        id: angajat.persoana.id,
      });
    }

    this.setState({ lista_angajati: lista_angajati, contract: angajati[0].contract });
  }

  async fillForm() {
    this.clearUserInput();
    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat ? this.state.selected_angajat.idpersoana : null;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    const contract = this.state.contract;
    console.log();

    // if already calculated, gets existing data, if idstat does not exist for (idc, mo, y) => calc => saves to DB
    const data = await axios
      .get(`${server.address}/realizariretineri/get/idc=${contract.id}&mo=${luna}&y=${an}`, {
        headers: authHeader(),
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));
    if (!data) {
      this.clearForm();
      return;
    }

    // get ore suplimentare by contract.id, luna, an
    const oresuplimentare = await this.getOresuplimentare(contract.id, luna, an);
    let totaloresuplimentare = 0;
    if (oresuplimentare.length > 0) {
      for (let ora of oresuplimentare) totaloresuplimentare += ora.total;
    }

    const retineri = data.retineri;

    // set states with data
    this.setState(
      {
        //* realizari
        an_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(0, 4) : '',
        luna_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(5, 7) : '',
        functie: contract.functie || 0,
        duratazilucru: contract.normalucru || 0,
        normalucru: data.norma || 0, // zile lucratoare in luna respectiva
        salariubrut: contract.salariutarifar || 0,
        orelucrate: data.orelucrate || 0,
        salariupezi: data.salariupezi || 0,
        salariupeora: data.salariupeora || 0,
        nrtichete: data.nrtichete || 0,
        zilecm: data.zilecm || 0,
        valcm: data.valcm || 0,
        zilecmlucratoare: data.zilecmlucratoare || 0,
        zileco: data.zileco || 0,
        zilecolucratoare: data.zilecolucratoare || 0,
        zilecfp: data.zilecfp || 0,
        zilecfplucratoare: data.zilecfplucratoare || 0,
        zilec: data.zilec || 0,

        //* retineri
        idretineri: retineri.id || 0,
        avansnet: retineri.avansnet || 0,
        cursValutar: retineri.curseurron || 0,
        pensiefacangajat: retineri.pensiefacangajat || 0,
        pensiefacangajator: retineri.pensiefacangajator || 0,
        pensiefacangajatretinuta: retineri.pensiefacangajatretinuta || 0,
        pensiefacangajatordeductibila: retineri.pensiefacangajatordeductibila || 0,
        pensiefacexcedent: retineri.pensiefacexcedent || 0,
        pensiealimentara: retineri.pensiealimentara || 0,
        popriri: retineri.popriri || 0,
        imprumuturi: retineri.imprumuturi || 0,
        deducere: data.deducere || 0,
        nrpersoaneintretinere: data.nrpersoaneintretinere || 0,

        //* total
        totaldrepturi: data.totaldrepturi || 0,
        cas: data.cas || 0,
        cass: data.cass || 0,
        valoaretichete: data.valoaretichete || 0,
        impozit: data.impozit || 0,
        restplata: data.restplata || 0,
        cam: data.cam || 0,

        //* detalii
        bazaimpozit: data.bazaimpozit || 0,
        impozitscutit: data.impozitscutit || 0,
        salariurealizat: data.salariurealizat || 0,
        venitnet: data.venitnet || 0,
        zilelucrate: data.zilelucrate || 0,
        zileplatite: data.zileplatite || 0,

        //
        idstat: data.id || 0,
        oresuplimentare: oresuplimentare || 0,
        totaloresuplimentare: totaloresuplimentare || 0,
        primabruta: data.primabruta || 0,
      },
      this.getTotalPensie
    );
  }

  async getTotalPensie() {
    const totalpensiefacan = await axios
      .get(`${server.address}/retineri/${this.state.contract.id}/pensiefac/${this.state.an}`, {
        headers: authHeader(),
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

    this.setState({
      totalpensiefacultativa: totalpensiefacan,
    });
  }

  numberWithCommas(x) {
    if (x) return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    else return 0;
  }

  onSelect(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idangajat = e.target.options[selectedIndex].getAttribute('data-key');
    if (selectedIndex === 0) setAngajatSel(null);
    else
      setAngajatSel({
        idpersoana: idangajat,
        numeintreg: e.target.value,
      });

    this.setState(
      {
        selected_angajat: getAngajatSel(),
      },
      this.fillForm
    );
  }

  async getOresuplimentare(idc, luna, an) {
    const oresuplimentare = await axios
      .get(`${server.address}/oresuplimentare/api/idc=${idc}&mo=${luna}&y=${an}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    return oresuplimentare;
  }

  veziOreSuplimentare() {
    console.log('editez ore suplimentare');

    this.setState({
      show: true,
    });
  }

  async veziPensieFacultativa() {
    this.setState({
      showPensie: true,
    });
  }

  async renderTabelore() {
    let oreSuplimentare = await this.getOresuplimentare(
      this.state.contract.id,
      this.state.luna.nr,
      this.state.an
    );
    let totaloresuplimentare = 0;
    for (let ora of oreSuplimentare) totaloresuplimentare += ora.total;

    this.setState({
      oresuplimentare: oreSuplimentare,
      totaloresuplimentare: totaloresuplimentare,
    });
  }

  handleClose() {
    this.setState(
      {
        show: false,
        showPensie: false,
        cursValutar: 0,
        pensiefacangajat: 0,
        modalMessage: '',
        nrore: 0,
        procent: 100,
      },
      this.fillForm
    );
  }

  async downloadStatIndividual(numeintreg, luna, an) {
    const user = authService.getCurrentUser();
    console.log('trying to download...');
    await fetch(
      `${server.address}/download/${user.id}/Stat Salarii - ${numeintreg} - ${luna.nume} ${an}.xlsx`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/octet-stream',
          Authorization: `Bearer ${user.accessToken}`,
        },
      }
    )
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `Stat Salarii - ${numeintreg} - ${luna.nume} ${an}.xlsx`;
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        console.log('downloaded');
        window.scrollTo({
          top: 0,
          left: 0,
          behavior: 'smooth',
        });
      });
  }

  async getStatIndividual() {
    const idangajat = this.state.selected_angajat.idpersoana;
    const luna = this.state.luna;
    const an = this.state.an;
    const user = authService.getCurrentUser();

    // check if realizariretineri exist in (luna, an)
    if (!this.state.idstat) return;

    const ok = await axios
      .get(
        `${server.address}/stat/${this.state.socsel.id}/individual/ida=${idangajat}&mo=${luna.nr}&y=${an}/${user.id}/get`,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) {
      let numeintreg = this.state.selected_angajat.numeintreg;
      this.downloadStatIndividual(numeintreg, luna, an);
    }
  }

  onChangeAn(e) {
    var luniArray = [];

    if (e.target.value) {
      this.state.lunaan.forEach((value) => {
        // eslint-disable-next-line
        if (e.target.value == value.an) {
          luniArray = value.luna;
        }
      });
      console.log(luniArray);
      this.setState(
        {
          luni: luniArray,
          an: e.target.value,
          luna: {
            nume: luni[luniArray[0] - 1],
            nr: luniArray[0],
          },
        },
        this.fillForm
      );
    }
  }

  onSubmit(e) {
    e.preventDefault();

    this.recalculeaza();
  }

  convertCurrency(from, curs) {
    if (curs && from) return Number(from / curs).toFixed(4);
    else return 0;
  }

  render() {
    // console.log(this.state.lunaan);
    // this.state.lunaan.forEach((value, index) => {
    //   ani.push(<option key={value.an}>{value.an}</option>);
    // });
    var ani = [];
    if (this.state.lunaan) {
      ani = this.state.lunaan.map((value) => <option key={value.an}>{value.an}</option>);
    }

    var luniComponent = this.state.luni.map((value) => (
      <option key={value} data-key={value}>
        {luni[value - 1]}
      </option>
    ));

    // if(this.state.an){var luniComponent = this.state.an.map((luna_nume, index) => (
    //    <option key={index} data-key={index + 1}>
    //      {luna_nume}
    //    </option>
    //  ));
    // // eslint-disable-next-line eqeqeq
    // if (this.state.an == this.state.an_inceput_contract) {
    //   luniComponent = luniComponent.slice(Number(this.state.luna_inceput_contract) - 1);
    // }

    const tabel_ore = this.state.oresuplimentare.map((ora, index) => {
      for (let key in ora) if (!ora[key]) ora[key] = '-';

      return (
        <tr key={ora.id}>
          <th>{ora.nr} ore</th>
          <th>{ora.procent}%</th>
          <th>{ora.total} RON</th>
        </tr>
      );
    });

    const nume_persoane_opt = this.state.lista_angajati.map((angajat) => (
      <option key={angajat.id} data-key={angajat.id}>
        {angajat.nume}
      </option>
    ));

    return (
      <Aux>
        {/* ORE SUPLIMENTARE */}
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
                  <th>suma</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>{tabel_ore}</tbody>
            </Table>
          </Modal.Body>
        </Modal>

        {/* PENSIE FACULTATIVA */}
        <Modal show={this.state.showPensie} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Pensie facultativă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Col md={12}>
                <Form.Group id="curseurron">
                  <Form.Label>
                    Curs BNR EUR/RON Final {this.state.luna.nume} {this.state.an}
                  </Form.Label>
                  <Form.Control
                    disabled
                    type="number"
                    min="0"
                    step="0.0001"
                    value={this.state.cursValutar}
                  />
                </Form.Group>
              </Col>
              <Col md={12}>
                <Form.Group id="pensiefacangajat">
                  <Form.Label>Pensie facultativă angajat</Form.Label>
                  <InputGroup className="mb-3">
                    <Form.Control
                      disabled
                      type="number"
                      min="0"
                      value={this.state.pensiefacangajat}
                    />
                    <InputGroup.Append>
                      <InputGroup.Text id="basic-addon2">
                        {this.convertCurrency(this.state.pensiefacangajat, this.state.cursValutar)}{' '}
                        EUR
                      </InputGroup.Text>
                    </InputGroup.Append>
                  </InputGroup>
                </Form.Group>
              </Col>
              <Col md={12}>
                <Form.Group id="totalpensiefacan">
                  <Form.Label>Total Pensie Facultativă an curent</Form.Label>
                  <InputGroup className="mb-3">
                    <Form.Control
                      disabled
                      type="number"
                      min="0"
                      value={this.state.totalpensiefacultativa}
                    />
                    <InputGroup.Append>
                      <InputGroup.Text id="basic-addon2">EUR</InputGroup.Text>
                    </InputGroup.Append>
                  </InputGroup>
                </Form.Group>
              </Col>
            </Row>
          </Modal.Body>
        </Modal>

        <Breadcrumb style={{ fontSize: '12px' }}>
          <Breadcrumb.Item href="/dashboard/societati">Societăți</Breadcrumb.Item>
          <Breadcrumb.Item href="/tables/angajati">Angajați</Breadcrumb.Item>
          <Breadcrumb.Item active>Realizări & Rețineri</Breadcrumb.Item>
        </Breadcrumb>

        <Card>
          {/* SELECT LUNA + AN */}
          <Card.Header>
            <Row>
              {/* LUNA */}
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.luna.nume}
                  onChange={(e) => {
                    let selectedIndex = e.target.options.selectedIndex;
                    this.setState(
                      {
                        luna: {
                          nume: e.target.value,
                          nr: Number(e.target.options[selectedIndex].getAttribute('data-key')),
                        },
                      },
                      this.fillForm
                    );
                  }}
                >
                  {luniComponent}
                </FormControl>
              </Col>
              {/* AN */}
              <Col md={6}>
                <FormControl as="select" value={this.state.an} onChange={(e) => this.onChangeAn(e)}>
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
                disabled
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_angajat ? this.state.selected_angajat.numeintreg : ''}
              >
                {/* lista_angajati mapped as <option> */}
                {nume_persoane_opt}
              </FormControl>
              <InputGroup.Append>
                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
                  overlay={
                    <Tooltip id="update-button" style={{ opacity: '.4' }}>
                      Către date personale
                    </Tooltip>
                  }
                >
                  <Button href="/forms/angajat" variant="outline-info" className="pb-0">
                    <Info size={20} className="m-0" />
                  </Button>
                </OverlayTrigger>
              </InputGroup.Append>
            </InputGroup>
          </Card.Header>

          <Card.Body>
            <Form>
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
                          disabled
                          type="number"
                          value={this.state.duratazilucru || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="normazilucru">
                        <Form.Label>Normă lucru</Form.Label>
                        <Form.Control disabled type="text" value={this.state.normalucru || ''} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="salariubrut">
                        <Form.Label>Salariu brut</Form.Label>
                        <Form.Control
                          disabled
                          type="text"
                          value={this.numberWithCommas(this.state.salariubrut)}
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
                        <Form.Control disabled type="number" min="0" value={this.state.nrtichete} />
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
                                Brut: {this.state.valcm.toFixed(0)} RON
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
                                {(
                                  (this.state.zilecolucratoare - this.state.zilecfplucratoare) *
                                  this.state.salariupezi
                                ).toFixed(0)}{' '}
                                RON
                              </InputGroup.Text>
                            </InputGroup.Append>
                          ) : null}
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilecfp">
                        <Form.Label>Zile libere</Form.Label>
                        <Form.Control
                          disabled
                          type="number"
                          min="0"
                          value={this.state.zilelibere}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileinvoire">
                        <Form.Label>Zile învoire</Form.Label>
                        <Form.Control
                          disabled
                          type="number"
                          min="0"
                          value={this.state.zileinvoire}
                        />
                      </Form.Group>
                    </Col>
                    {/* TODO */}
                    <Col md={6}>
                      <Form.Group id="oresuplimentare">
                        <Form.Label>Valoare Ore suplimentare</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="text"
                            disabled
                            value={this.numberWithCommas(this.state.totaloresuplimentare)}
                          />
                          <InputGroup.Append>
                            <Button
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
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
                          disabled
                          type="number"
                          min="0"
                          value={this.state.primabruta}
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
                        <Form.Control disabled type="number" min="0" value={this.state.avansnet} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiefacultativa">
                        <Form.Label>Pensie facultativă</Form.Label>
                        <InputGroup>
                          <Form.Control
                            disabled
                            type="number"
                            min="0"
                            value={this.state.pensiefacangajat}
                          />
                          <InputGroup.Append>
                            <Button
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
                              }
                              disabled={!this.state.selected_angajat}
                              onClick={() => this.veziPensieFacultativa()}
                            >
                              Vezi
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiealimentara">
                        <Form.Label>Pensie alimentară</Form.Label>
                        <Form.Control
                          disabled
                          type="number"
                          min="0"
                          value={this.state.pensiealimentara}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="popriri">
                        <Form.Label>Popriri</Form.Label>
                        <Form.Control disabled type="number" min="0" value={this.state.popriri} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="imprumuturi">
                        <Form.Label>Împrumuturi</Form.Label>
                        <Form.Control
                          disabled
                          type="number"
                          min="0"
                          value={this.state.imprumuturi}
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
                          value={this.numberWithCommas(this.state.totaldrepturi)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="restplata">
                        <Form.Label>Rest de plată</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.restplata)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cas">
                        <Form.Label>CAS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cas)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cass">
                        <Form.Label>CASS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cass)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="valoaretichete">
                        <Form.Label>Valoare tichete</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.valoaretichete)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="impozit">
                        <Form.Label>Impozit</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.impozit)}
                        />
                      </Form.Group>
                    </Col>

                    <Col md={4}>
                      <Form.Group id="cam">
                        <Form.Label>CAM</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cam)}
                        />
                      </Form.Group>
                    </Col>

                    <Col md={4}>
                      <Form.Group id="cheltuieliangajator">
                        <Form.Label>Cheltuieli angajator</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(
                            Number(this.state.cam) + Number(this.state.totaldrepturi)
                          )}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Button
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.idstat}
                    onClick={this.getStatIndividual}
                    className="float-right mb-3"
                  >
                    Stat salariat individual
                  </Button>
                </Col>

                {/* DETALII */}
                <Col md={12}>
                  <Card className="mt-2">
                    <Card.Header
                      style={{ cursor: 'pointer' }}
                      onClick={() =>
                        this.setState({
                          detaliiAccordion: !this.state.detaliiAccordion,
                        })
                      }
                    >
                      <Card.Title
                        as="h5"
                        aria-controls="accordion1"
                        aria-expanded={this.state.detaliiAccordion}
                      >
                        Detalii
                      </Card.Title>
                    </Card.Header>
                    <Collapse in={this.state.detaliiAccordion}>
                      <div id="accordion1">
                        <Card.Body>
                          <Row>
                            <Form.Group id="bazaimpozit" as={Col} md="6">
                              <Form.Label>Bază impozit</Form.Label>
                              <Form.Control type="number" value={this.state.bazaimpozit} disabled />
                            </Form.Group>
                            <Form.Group id="impozitscutit" as={Col} md="6">
                              <Form.Label>Impozit scutit</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.impozitscutit}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="nrpersoaneintretinere" as={Col} md="6">
                              <Form.Label>Număr persoane întreținere</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.nrpersoaneintretinere}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="salariupeora" as={Col} md="6">
                              <Form.Label>Salariu pe oră</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.salariupeora}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="salariupezi" as={Col} md="6">
                              <Form.Label>Salariu pe zi</Form.Label>
                              <Form.Control type="number" value={this.state.salariupezi} disabled />
                            </Form.Group>
                            <Form.Group id="salariurealizat" as={Col} md="6">
                              <Form.Label>Salariu realizat</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.salariurealizat}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="venitnet" as={Col} md="6">
                              <Form.Label>Venit net</Form.Label>
                              <Form.Control type="number" value={this.state.venitnet} disabled />
                            </Form.Group>
                            <Form.Group id="zilec" as={Col} md="6">
                              <Form.Label>Zile concediu (total)</Form.Label>
                              <Form.Control type="number" value={this.state.zilec} disabled />
                            </Form.Group>
                            <Form.Group id="zilecmlucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu medical lucrătoare</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.zilecmlucratoare}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="zilecolucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu odihna lucrătoare</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.zilecolucratoare}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="zilecfp" as={Col} md="6">
                              <Form.Label>Zile concediu fară plată</Form.Label>
                              <Form.Control type="number" value={this.state.zilecfp} disabled />
                            </Form.Group>
                            <Form.Group id="zilecfplucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu fara plată lucrătoare</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.zilecfplucratoare}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="zilelucrate" as={Col} md="6">
                              <Form.Label>Zile lucrate</Form.Label>
                              <Form.Control type="number" value={this.state.zilelucrate} disabled />
                            </Form.Group>
                            <Form.Group id="zileplatite" as={Col} md="6">
                              <Form.Label>Zile plătite</Form.Label>
                              <Form.Control type="number" value={this.state.zileplatite} disabled />
                            </Form.Group>
                          </Row>
                        </Card.Body>
                      </div>
                    </Collapse>
                  </Card>
                </Col>

                <Col></Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}

export default RealizariRetineriView;
