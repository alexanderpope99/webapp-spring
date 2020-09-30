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
import Box from '@material-ui/core/Box';
import Popover from '@material-ui/core/Popover';
import DeleteIcon from '@material-ui/icons/Delete';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import months from '../Resources/months';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel, setAngajatSel } from '../Resources/angajatsel';
import { server } from '../Resources/server-address';
import { Typography } from '@material-ui/core';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class RealizariRetineri extends React.Component {
  constructor() {
    super();

    this.setCurrentYearMonth = this.setCurrentYearMonth.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);
    this.recalculeaza = this.recalculeaza.bind(this);
    this.reseteazaCalculul = this.reseteazaCalculul.bind(this);
    this.veziOreSuplimentare = this.veziOreSuplimentare.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.getOresuplimentare = this.getOresuplimentare.bind(this);
    this.addOrasuplimentara = this.addOrasuplimentara.bind(this);
    this.renderTabelore = this.renderTabelore.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
		this.calcNrTichete = this.calcNrTichete.bind(this);
		this.getStatIndividual = this.getStatIndividual.bind(this);

    this.state = {
      socsel: getSocSel(),
      // angajatsel: getAngajatSel(),
      show: false,
      modalMessage: '',

      an: '',
      luna: '',

      selected_angajat: getAngajatSel(),
      lista_angajati: [], // object: {nume, id}
      idcontract: '',
      idstat: '',

      // realizari
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilecm: '',
      valcm: '',
      zileco: '',
      zileconeplatit: '',
      zilec: '',
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: '',
      totaloresuplimentare: 0,

      // retineri
      idretineri: 0,
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
      valcm: '',
      zileco: '',
      zileconeplatit: '',
      zilec: '',
      zileinvoire: 0, // user input
      primabruta: 0,
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: '',
      totaloresuplimentare: 0,

      // retineri
      idretineri: 0,
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
  clearUserInput() {
    this.setState({
      oresuplimentare: [],
      zileinvoire: 0,
      primabruta: 0,
      zilelibere: 0,

      avansnet: 0,
      pensiefacultativa: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
    });
  }

  async componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

		await this.setCurrentYearMonth(); // modifies state.an, state.luna
    this.setPersoane(); // date personale, also fills lista_angajati
	  this.fillForm();
  }

  numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  async setCurrentYearMonth() {
    let today = new Date();
    let luna = months[today.getMonth()];
		let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  async setPersoane() {
    //* only people with contract <- &c flag
    const persoane = await axios
      .get(`${server.address}/persoana/ids=${this.state.socsel.id}&c`, { headers: authHeader() })
      .then((res) => res.status === 200 ? res.data : null)
			.catch((err) => console.error(err));
		if(!persoane) return;
    //* set lista_angajati
    let lista_angajati = [];
    for (let persoana of persoane) {
      lista_angajati.push({ nume: persoana.nume + ' ' + persoana.prenume, id: persoana.id });
    }
    this.setState({ lista_angajati: lista_angajati });
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

    // get contract by idpersoana :: contract body needed for 4 fields

    const contract = await axios
      .get(`${server.address}/contract/idp=${idpersoana}`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));
		console.log('contract:', contract);
		if(!contract) return;

    // if already calculated, gets existing data, if idstat does not exist for idc, mo, y => calc => saves to DB
    const data = await axios
      .post(
        `${server.address}/realizariretineri/save/idc=${contract.id}&mo=${luna}&y=${an}`,
        {body: null},
        {
          headers: authHeader(),
        }
      )
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

		console.log('data:', data);
		if(!data) return;

    // get ore suplimentare by idcontract, luna, an
    const oresuplimentare = await this.getOresuplimentare(contract.id, luna, an);
    let totaloresuplimentare = 0;
    if (oresuplimentare.length > 0) {
      for (let ora of oresuplimentare) totaloresuplimentare += ora.total;
		}
    const retineri = await axios
      .get(`${server.address}/retineri/ids=${data.id}`, { headers: authHeader() })
      .then((res) => res.data)
			.catch((err) => console.error(err));


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
      valcm: data.valcm,
      zilecmlucratoare: data.zilecmlucratoare,
      zileco: data.zileco,
      zilecolucratoare: data.zilecolucratoare,
      zileconeplatit: data.zileconeplatit,
      zileconeplatitlucratoare: data.zileconeplatitlucratoare,
      zilec: data.zilec,

      //* retineri
      idretineri: retineri.id,
      avansnet: retineri.avansnet,
      pensiefacultativa: retineri.pensiefacultativa,
      pensiealimentara: retineri.pensiealimentara,
      popriri: retineri.popriri,
      imprumuturi: retineri.imprumuturi,
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
      idstat: data.id,
      idcontract: contract.id,
      oresuplimentare: oresuplimentare,
      totaloresuplimentare: totaloresuplimentare,
      primabruta: data.primabruta,
    });
  }

  onSelect(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idangajat = e.target.options[selectedIndex].getAttribute('data-key');
    setAngajatSel({
      numeintreg: e.target.value,
      idpersoana: idangajat,
    });

    this.setState(
      {
        selected_angajat: getAngajatSel(),
      },
      () => this.fillForm()
    );
  }

  // recalculeaza doar total
  async recalculeaza() {
    console.log('recalculez...');

    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat ? this.state.selected_angajat.idpersoana : null;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    // save retineri to DB
    await axios
      .put(
        `${server.address}/retineri/${this.state.idretineri}`,
        {
          idstat: this.state.idstat,
          avansnet: this.state.avansnet,
          pensiealimentara: this.state.pensiealimentara,
          pensiefacultativa: this.state.pensiefacultativa,
          popriri: this.state.popriri,
          imprumuturi: this.state.imprumuturi,
        },
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));

    let pb = this.state.primabruta;
    let nrt = this.state.nrtichete;
    let tos = this.state.totaloresuplimentare;

    const data = await axios
      .put(
        `${server.address}/realizariretineri/update/calc/idc=${this.state.idcontract}&mo=${luna}&y=${an}&pb=${pb}&nrt=${nrt}&tos=${tos}`,
        {},
        { headers: authHeader() }
      )
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

		console.log(data);
		if(!data) return;
    // total
    this.setState(
      {
        totaldrepturi: data.totaldrepturi,
        restplata: data.restplata,
        cas: data.cas,
        cass: data.cass,
        cam: data.cam,
        impozit: data.impozit,
        valoaretichete: data.valoaretichete,
      },
      this.fillForm
    );

    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  async reseteazaCalculul() {
    console.log('recalculez TOT...');

    let an = this.state.an;
    let luna = this.state.luna.nr;

    const idpersoana = this.state.selected_angajat.idpersoana;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    await axios.put(
      `${server.address}/realizariretineri/update/reset/idc=${this.state.idcontract}&mo=${luna}&y=${an}`,
			{},
			{ headers: authHeader() }
    )
      .then((res) => (res.status === 200))
      .catch((err) => console.error(err));

    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });

    this.fillForm();
  }

  async calcNrTichete() {
    if (!this.state.selected_angajat) return;

    let idc = this.state.idcontract;
    let luna = this.state.luna.nr;
    let an = this.state.an;
    let nrTichete = await axios
      .get(`${server.address}/tichete/nr/idc=${idc}&mo=${luna}&y=${an}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    console.log(nrTichete);
    this.setState({
      nrtichete: nrTichete,
    });
  }

  async getOresuplimentare(idc, luna, an) {
    const oresuplimentare = await axios
      .get(`${server.address}/oresuplimentare/api/idc=${idc}&mo=${luna}&y=${an}`, {
        headers: authHeader(),
      })
			.then((res) => res.data)
			.catch(err => console.error(err));

    return oresuplimentare;
  }

  veziOreSuplimentare() {
    console.log('editez ore suplimentare');

    this.setState({
      show: true,
    });
  }

  async addOrasuplimentara(n, p, t) {
    const ore_body = {
      idstatsalariat: this.state.idstat,
      nr: n,
      procent: p,
      total: t.toFixed(0),
    };

    await axios
      .post(`${server.address}/oresuplimentare`, ore_body, {
        headers: authHeader(),
      })
      .then(this.renderTabelore)
      .catch((err) => console.error(err));
  }

  async deleteOra(id) {
    await axios
      .delete(`${server.address}/oresuplimentare/${id}`, { headers: authHeader() })
      .then(this.renderTabelore)
      .catch((err) => console.error(err));
  }

  async renderTabelore() {
    let oreSuplimentare = await this.getOresuplimentare(
      this.state.idcontract,
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
    this.setState({
      show: false,
      modalMessage: '',
      nrore: 0,
      procent: '',
    });
	}
	
	async getStatIndividual() {
		const idangajat = this.state.selected_angajat.idpersoana;
		const luna = this.state.luna;
		const an = this.state.an;
		const user = JSON.parse(localStorage.getItem('user'));

		const ok = await axios.get(
			`${server.address}/stat/${this.state.socsel.id}/individual/ida=${idangajat}&mo=${luna.nr}&y=${an}/${user.id}`,
			{ headers: authHeader() }
		)
			.then(res => res.status === 200)
			.catch(err => console.error(err));
		
		if(ok) {
			let numeintreg = this.state.selected_angajat.numeintreg;
			this.downloadStatIndividual(numeintreg, luna, an);
		}
	}

	async downloadStatIndividual(numeintreg, luna, an) {
    const user = JSON.parse(localStorage.getItem('user'));
		console.log('trying to download...');
    await fetch(
      `${server.address}/download/${user.id}/Stat Salarii - ${numeintreg} - ${luna.nume} ${an}.xlsx`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/octet-stream',
          'Authorization': `Bearer ${user.accessToken}`,
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
			});
	}

  onSubmit(e) {
    e.preventDefault();

    this.recalculeaza();
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    const tabel_ore = this.state.oresuplimentare.map((ora, index) => {
      for (let key in ora) if (!ora[key]) ora[key] = '-';

      return (
        <tr key={ora.id}>
          <th>{ora.nr} ore</th>
          <th>{ora.procent}%</th>
          <th>{ora.total} RON</th>
          <th className="d-inline-flex flex-row justify-content-around">
            <PopupState variant="popover" popupId="demo-popup-popover">
              {(popupState) => (
                <div>
                  <Button
                    variant="outline-secondary"
                    className="m-0 p-1 rounded-circle border-0"
                    {...bindTrigger(popupState)}
                  >
                    <DeleteIcon fontSize="small" />
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
                      <Typography>Confirmare ștergere</Typography>
                      <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                      <br />
                      <Button
                        variant="outline-danger"
                        onClick={() => {
                          popupState.close();
                          this.deleteOra(ora.id);
                        }}
                        className="mt-2 "
                      >
                        Da
                      </Button>
                      <Button
                        variant="outline-persondary"
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
          </th>
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
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Ore suplimentare</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Nr. ore</Form.Label>
                  <Form.Control
                    size="sm"
                    type="number"
                    min="0"
                    value={this.state.nrore}
                    onChange={(e) => this.setState({ nrore: e.target.value })}
                  />
                </Form.Group>
              </Col>
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Procent</Form.Label>
                  <Form.Control
                    size="sm"
                    type="number"
                    min="0"
                    value={this.state.procent}
                    onChange={(e) => this.setState({ procent: e.target.value })}
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group>
                  <Form.Label>Suma</Form.Label>
                  <Form.Control
                    size="sm"
                    type="number"
                    value={(
                      Number(this.state.nrore) *
                      (Number(this.state.procent) / 100) *
                      Number(this.state.salariupeora)
                    ).toFixed(0)}
                    disabled
                  />
                </Form.Group>
              </Col>
              <Col md={1}>
                <Form.Label> </Form.Label>
                <Button
                  size="sm"
                  className="display-flex m-0"
                  onClick={async () =>
                    await this.addOrasuplimentara(
                      this.state.nrore,
                      this.state.procent,
                      Number(this.state.nrore) *
                        (Number(this.state.procent) / 100) *
                        Number(this.state.salariupeora)
                    )
                  }
                >
                  +
                </Button>
              </Col>
            </Row>
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
                    this.setState(
                      {
                        an: e.target.value,
                      },
                      this.fillForm
                    )
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
                value={this.state.selected_angajat ? this.state.selected_angajat.numeintreg : ''}
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
                        <InputGroup>
                          <Form.Control
                            type="number"
                            min="0"
                            value={this.state.nrtichete}
                            onChange={(e) => this.setState({ nrtichete: e.target.value })}
                          />
                          <InputGroup.Append>
                            <Button
                              onClick={this.calcNrTichete}
                              size="sm"
                              className="p-0 pl-2 pr-2"
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
                              }
                              disabled={this.state.selected_angajat ? false : true}
                            >
                              Caclulează
                              <br />
                              automat
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
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
                                Sumă brută: {this.state.valcm.toFixed(0)} RON
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
                          <Form.Control
                            type="text"
                            disabled
                            value={this.state.totaloresuplimentare + ' RON'}
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
                          value={this.state.impozit ? this.numberWithCommas(this.state.impozit) : 0}
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

                    <Col md={4}>
                      <Form.Group id="cheltuieliangajator">
                        <Form.Label>Cheltuieli angajator</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={
                            this.state.cam
                              ? this.numberWithCommas(
                                  Number(this.state.cam) + Number(this.state.totaldrepturi)
                                )
                              : ''
                          }
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <Button
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.selected_angajat}
                    type="submit"
                    className="mb-3 float-right"
                  >
                    Recalculează
                  </Button>
                </Col>

								<Button
                  variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                  disabled={!this.state.selected_angajat}
                  onClick={this.getStatIndividual}
                  className="mb-3 mt-3"
                >
                  Stat salariat individual
                </Button>

                <Button
                  variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                  disabled={!this.state.selected_angajat}
                  onClick={this.reseteazaCalculul}
                  className="mb-3 mt-3"
                >
                  Resetează Calculul
                </Button>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}

export default RealizariRetineri;
