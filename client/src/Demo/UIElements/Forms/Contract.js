import React from 'react';
import {
  Row,
  Col,
  Form,
  Button,
  Modal,
  FormControl,
  InputGroup,
  DropdownButton,
  Dropdown,
} from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';
import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { case_de_sanatate, judete } from '../../Resources/judete';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

class Contract extends React.Component {
  constructor() {
    super();
    this.handleClose = this.handleClose.bind(this);
    this.onChangeCentrucost = this.onChangeCentrucost.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.hasRequired = this.hasRequired.bind(this);
    this.fillForm = this.fillForm.bind(this);

    this.state = {
      socsel: getSocSel(),
      id: 0,
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '', //text
      centruCost: '', //text
      echipa: '', //text,
      departament: '',
      deduceri: true,
      studiiSuperioare: false,
      functieBaza: true,
      normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
      salariu: '',
      monedăSalariu: 'RON', //text
      iban: '',
      numeBanca: '',
      condițiiMuncă: 'Normale', //text
      sindicat: false,
      cotizațieSindicat: '',
      pensiePrivată: false,
      cotizațiePensie: '',
      spor: 0,
      avans: 0,
      monedăAvans: 'RON', //text
      zileCOan: 21,
      casăSănătate: '', //text
      gradInvalid: 'valid', //text
      funcție: '', //text
      nivelStudii: '', //text
      cor: '',
      dataIncepere: '',
      dataContract: '',
      ultimaZiLucru: '',
      pensionar: false,

      show: false,
      modalMessage: '', //text
      buttonDisabled: true,
    };
  }

  clearFields() {
    this.setState({
      id: 0,
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '', //text
      centruCost: '', //text
      echipa: '', //text,
      departament: '',
      deduceri: true,
      studiiSuperioare: false,
      functieBaza: true,
      normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
      monedăSalariu: 'RON', //text
      salariu: '',
      iban: '',
      numeBanca: '',
      condițiiMuncă: 'Normale', //text
      sindicat: false,
      cotizațieSindicat: '',
      pensiePrivată: false,
      cotizațiePensie: '',
      spor: 0,
      avans: 0,
      monedăAvans: 'RON', //text
      zileCOan: 21,
      casăSănătate: '', //text
      gradInvalid: 'valid', //text
      funcție: '', //text
      nivelStudii: '', //text
      cor: '',
      dataIncepere: '',
      dataContract: '',
      ultimaZiLucru: '',
      pensionar: false,

      show: false,
      modalMessage: '', //text
    });
  }

  getNumeNorma(nrOre) {
    switch (nrOre) {
      case 8:
        return 'Normă întreagă';
      case 7:
        return 'Normă parțială 7/8';
      case 6:
        return 'Normă parțială 6/8';
      case 5:
        return 'Normă parțială 5/8';
      case 4:
        return 'Normă parțială 4/8';
      case 3:
        return 'Normă parțială 3/8';
      case 2:
        return 'Normă parțială 2/8';
      case 1:
        return 'Normă parțială 1/8';
      default:
        break;
    }
  }

  async fillForm(contract, idangajat) {
    if (!contract) {
      this.clearFields();
      // get adresa
      const adresa = await axios
        .get(`${server.address}/adresa/idp=${idangajat}`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) => console.error(err));
      console.log(adresa);
      // get casa_de_sanatate
      var cs = '-';
      if (adresa.judet) {
        if (adresa.judet.substring(0, 2) === 'SE') cs = case_de_sanatate[0];
        else cs = case_de_sanatate[judete.indexOf(adresa.judet)];
      }

      // use casa_de_sanatate[judet_index]
      this.setState(
        {
          idangajat: idangajat,
          casăSănătate: cs,
        },
        () => console.log('idangajat:', idangajat, '\tidcontract:', null)
      );
    } else {
      this.setState(
        {
          idangajat: idangajat,
          id: contract.id,
          modelContract: contract.tip, //text
          numărContract: contract.nr || '', //text
          marca: contract.marca || '', //text
          dataContract: contract.data ? contract.data.substring(0, 10) : '',
          dataIncepere: contract.dataincepere ? contract.dataincepere.substring(0, 10) : '',
          punctDeLucru: contract.idpunctdelucru || '',
          centruCost: contract.idcentrucost || '',
          echipa: contract.idechipa || '',
          departament: contract.iddepartament || '',
          functieBaza: contract.functiedebaza || false,
          deduceri: contract.calculdeduceri || false,
          studiiSuperioare: contract.studiisuperioare || false,
          normăLucru: { nrOre: contract.normalucru, nume: this.getNumeNorma(contract.normalucru) },
          salariu: contract.salariutarifar,
          condițiiMuncă: contract.conditiimunca,
          iban: contract.contbancar ? contract.contbancar.iban : '',
          numeBanca: contract.contbancar ? contract.contbancar.numebanca : '',
          sindicat: contract.sindicat || false,
          cotizațieSindicat: contract.cotizatiesindicat || '',
          pensiePrivată: contract.pensieprivata || false,
          cotizațiePensie: contract.cotizatiepensieprivata || '',
          avans: contract.avans || 0,
          monedăAvans: contract.monedaavans,
          zileCOan: contract.zilecoan || 0,
          ultimaZiLucru: contract.ultimaZiLucru ? contract.ultimazilucru.substring(0, 10) : '',
          casăSănătate: contract.casasanatate || '-',
          gradInvalid: contract.gradinvaliditate || '', //text
          funcție: contract.functie || '', //text
          nivelStudii: contract.nivelstudii || '', //text
          cor: contract.cor || '',
          pensionar: contract.pensionar,
          spor: contract.spor || '',
        },
        () => console.log('idangajat:', idangajat, '\tidcontract:', contract.id)
      );
    }
  }

  onChangeCentrucost(selected) {
    if (typeof selected[0] !== 'undefined' || selected.length !== 0) {
      if (typeof selected[0] === 'object') this.setState({ centruCost: selected[0].label });
      else this.setState({ centruCost: selected[0] });
    }
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  hasRequired() {
    if (!this.state.numărContract) {
      this.setState({
        show: true,
        modalMessage: 'Contractul trebuie să aibă un număr.',
      });
      return false;
    }

    if (!this.state.salariu) {
      this.setState({
        show: true,
        modalMessage: 'Contractul trebuie să aibă un salariu.',
      });
      return false;
    }

    return true;
  }

  async onSubmit(e, idcontract, idangajat) {
    e.preventDefault();
    // console.log(idcontract, idangajat);

    if (!this.hasRequired()) return;

    var method = 'PUT';
    // if person is missing contract
    if (!idcontract) {
      method = 'POST';
      idcontract = '';
    }

    const contract_body = {
      tip: this.state.modelContract,
      nr: this.state.numărContract,
      marca: this.state.marca,
      data: this.state.dataContract,
      dataincepere: this.state.dataIncepere,
      idpunctlucru: null, //punctlucru.id,  // null or int
      idcentrucost: null, //centrucost.id,
      idechipa: null, //echipa.id,
      iddepartament: null, //departament.id,
      functiedebaza: this.state.functieBaza,
      calculdeduceri: this.state.deduceri,
      studiisuperioare: this.state.studiiSuperioare,
      normalucru: this.state.normăLucru.nrOre,
      salariutarifar: this.state.salariu,
      contbancar: { iban: this.state.iban, numebanca: this.state.numeBanca },
      conditiimunca: this.state.condițiiMuncă,
      sindicat: this.state.sindicat,
      cotizatiesindicat: this.state.cotizațieSindicat,
      pensieprivata: this.state.pensiePrivată,
      cotizatiepensieprivata: this.state.cotizațiePensie,
      avans: this.state.avans,
      monedaavans: this.state.monedăAvans,
      zilecoan: this.state.zileCOan,
      ultimazilucru: this.state.ultimaZiLucru === '' ? null : this.state.ultimaZiLucru,
      casasanatate: this.state.casăSănătate,
      gradinvaliditate: this.state.gradInvalid,
      functie: this.state.funcție,
      nivelstudii: this.state.nivelStudii,
      cor: this.state.cor,
      pensionar: this.state.pensionar,
      spor: this.state.spor,
    };
    let contract;
    if (method === 'PUT')
      contract = await axios
        .put(`${server.address}/contract/${idcontract}`, contract_body, {
          headers: authHeader(),
        })
        .then((res) => (res.status === 200 ? res.data : null))
        .catch((err) => {
          console.error(err.message);
        });
    else if (method === 'POST')
      contract = await axios
        .post(`${server.address}/contract/${idcontract}`, contract_body, {
          headers: authHeader(),
        })
        .then((res) => (res.status === 200 ? res.data : null))
        .catch((err) => {
          console.error(err.message);
        });

    // if recieved response from server
    if (contract) {
      this.setState({
        show: true,
        modalMessage:
          method === 'POST' ? 'Contract adăugat cu succes 📄' : 'Contract actualizat 💾',
        id: contract.id,
      });

      if (method === 'POST') {
        // update angajat with idangajat from functon props
        await axios
          .put(
            `${server.address}/angajat/${idangajat}`,
            {
              idcontract: contract.id,
              idpersoana: idangajat,
              idsocietate: this.state.socsel.id,
            },
            {
              headers: authHeader(),
            }
          )
          .catch((err) => console.error(err));
        method = 'PUT';
      }
      console.log('idcontract:', contract.id);
    } else {
      this.setState({
        show: true,
        modalMessage: 'A aparut o eroare ⛔',
      });
    }
  }

  render() {
    const case_de_sanatate_component = case_de_sanatate.map((casa, index) => (
      <option key={index}>{casa}</option>
    ));

    return (
      <React.Fragment>
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

        <Form onSubmit={(e) => e.preventDefault()}>
          <Row>
            <Col md={12}>
              <Form.Group controlId="functia">
                <Form.Label>Funcție</Form.Label>
                <Form.Control
                  placeholder="functia"
                  value={this.state.funcție}
                  onChange={(e) => {
                    this.setState({ funcție: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="tip">
                <Form.Label>Model Contract</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.modelContract}
                  onChange={(e) => {
                    this.setState({ modelContract: e.target.value });
                  }}
                >
                  <option>Contract de munca</option>
                  <option>Contract de administrator</option>
                  <option>Convenție civilă</option>
                  <option>Drepturi de autor</option>
                  <option>Figuranți / Zilieri</option>
                </Form.Control>
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="nrcontract">
                <Form.Label>Număr contract</Form.Label>
                <Form.Control
                  required
                  type="text"
                  placeholder="Număr contract"
                  value={this.state.numărContract}
                  onChange={(e) => {
                    this.setState({ numărContract: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="marca">
                <Form.Label>Marca</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Marca"
                  value={this.state.marca}
                  onChange={(e) => {
                    this.setState({ marca: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group controlId="dataincepere">
                <Form.Label>Data începere activitate</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.dataIncepere}
                  onChange={(e) => {
                    this.setState({ dataIncepere: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="datacontract">
                <Form.Label>Data contract</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.dataContract}
                  selected={this.state.dataContract}
                  onChange={(e) => {
                    this.setState({ dataContract: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group id="functiedabaza">
                <Form.Check
                  custom
                  type="checkbox"
                  id="functieDeBazaCheck"
                  label="Funcție de bază"
                  checked={this.state.functieBaza}
                  onChange={(e) => {
                    this.setState({ functieBaza: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="calculdeduceri">
                <Form.Check
                  custom
                  type="checkbox"
                  id="deduceriCheck"
                  label="Calcul deduceri"
                  checked={this.state.deduceri}
                  onChange={(e) => {
                    this.setState({ deduceri: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="studiisuperioare">
                <Form.Check
                  custom
                  type="checkbox"
                  id="studiiSuperioareCheck"
                  label="Studii superioare"
                  checked={this.state.studiiSuperioare}
                  onChange={(e) => {
                    this.setState({
                      studiiSuperioare: e.target.checked,
                    });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group id="pensionar">
                <Form.Check
                  custom
                  type="checkbox"
                  id="pensionarCheck"
                  label="Pensionar"
                  checked={this.state.pensionar}
                  onChange={(e) => {
                    this.setState({ pensionar: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="normalucru">
                <Form.Label>Normă de lucru</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.normăLucru.nume}
                  onChange={(e) => {
                    this.setState(
                      {
                        normăLucru: {
                          nrOre: 8 - e.target.options.selectedIndex,
                          nume: e.target.value,
                        },
                      },
                      () => console.log(this.state.normăLucru)
                    );
                  }}
                >
                  <option>Normă întreagă</option>
                  <option>Normă parțială 7/8</option>
                  <option>Normă parțială 6/8</option>
                  <option>Normă parțială 5/8</option>
                  <option>Normă parțială 4/8</option>
                  <option>Normă parțială 3/8</option>
                  <option>Normă parțială 2/8</option>
                  <option>Normă parțială 1/8</option>
                </Form.Control>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="zilecoan">
                <Form.Label>Zile CO/an</Form.Label>
                <Form.Control
                  placeholder="0"
                  type="number"
                  value={this.state.zileCOan}
                  onChange={(e) => {
                    this.setState({ zileCOan: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="salariu">
                <Form.Label>Salariu</Form.Label>
                <InputGroup>
                  <FormControl
                    type="number"
                    required
                    placeholder="Salariu"
                    aria-label="Salariu"
                    aria-describedby="basic-addon2"
                    value={this.state.salariu}
                    onChange={(e) => this.setState({ salariu: e.target.value })}
                  />

                  <DropdownButton
                    as={InputGroup.Append}
                    title={this.state.monedăSalariu}
                    id="monedasalariu"
                  >
                    <Dropdown.Item
                      onClick={() =>
                        this.setState({
                          monedăSalariu: 'RON',
                        })
                      }
                    >
                      RON
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() =>
                        this.setState({
                          monedăSalariu: 'EUR',
                        })
                      }
                    >
                      EUR
                    </Dropdown.Item>
                  </DropdownButton>
                </InputGroup>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="conditiidemunca">
                <Form.Label>Condiții de muncă</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.condițiiMuncă}
                  onChange={(e) => {
                    this.setState({ condițiiMuncă: e.target.value });
                  }}
                >
                  <option>Normale</option>
                  <option>Deosebite</option>
                </Form.Control>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="iban">
                <Form.Label>IBAN</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.iban}
                  onChange={(e) => {
                    this.setState({ iban: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="numebanca">
                <Form.Label>Nume banca</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.numeBanca}
                  onChange={(e) => {
                    this.setState({ numeBanca: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group controlId="punctdelucru">
                <Form.Label>Punct de lucru</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.punctDeLucru}
                  onChange={(e) => {
                    this.setState({ punctDeLucru: e.target.value });
                  }}
                >
                  <option>-</option>
                  {/* TODO fetch names from db and list here */}
                  {/* TODO 'add punct de lucru' button */}
                </Form.Control>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="centrucost">
                <Form.Label>Centre de cost</Form.Label>
                <Typeahead
                  id="optiune-centrucost"
                  options={['centru test']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.centruCost}
                  onChange={this.onChangeCentrucost}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="echipa">
                <Form.Label>Echipa</Form.Label>
                <Typeahead
                  id="optiune-echipa"
                  options={['echipa test']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.echipa}
                  onChange={(selected) => {
                    this.setState({ echipa: selected });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="departament">
                <Form.Label>Departament</Form.Label>
                <Typeahead
                  id="optiune-departament"
                  options={['departament test']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.departament}
                  onChange={(selected) => {
                    this.setState({ departament: selected });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={12} />
            <Col md={1}>
              <Form.Group id="sindicat" style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}>
                <Form.Check
                  custom
                  type="checkbox"
                  id="sindicatCheck"
                  label="Sindicat"
                  checked={this.state.sindicat}
                  onChange={(e) => {
                    this.setState({ sindicat: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            {this.state.sindicat ? (
              <Col md={3}>
                <Form.Group id="cotizatiesindicat">
                  <Form.Label>Cotizație sindicat</Form.Label>
                  <Form.Control
                    placeholder="0"
                    value={this.state.cotizațieSindicat}
                    onChange={(e) => {
                      this.setState({ cotizațieSindicat: e.target.value });
                    }}
                  />
                </Form.Group>
              </Col>
            ) : null}
            <Col md={2} />
            <Col md={1.5}>
              <Form.Group
                id="pensieprivata"
                style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}
              >
                <Form.Check
                  custom
                  type="checkbox"
                  id="pensiePrivataCheck"
                  label="Pensie privată"
                  checked={this.state.pensiePrivată}
                  onChange={(e) => {
                    this.setState({ pensiePrivată: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            {this.state.pensiePrivată ? (
              <Col md={3}>
                <Form.Group id="cotizatiepensieprivata">
                  <Form.Label>Cotizație pensie privată</Form.Label>
                  <Form.Control
                    placeholder="0"
                    value={this.state.cotizațiePensie}
                    onChange={(e) => {
                      this.setState({
                        cotizațiePensie: e.target.value,
                      });
                    }}
                  />
                </Form.Group>
              </Col>
            ) : null}
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="sporuri">
                <Form.Label>Sporuri permanente</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.spor}
                  onChange={(e) => {
                    this.setState({ spor: e.target.value });
                  }}
                >
                  <option>-</option>
                  <option>Spor sărbători legale</option>
                  <option>Spor ore de noapte</option>
                  <option>Spor de weekend</option>
                </Form.Control>
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="avans">
                <Form.Label>Avans</Form.Label>
                <InputGroup>
                  <FormControl
                    type="number"
                    placeholder="Avans"
                    aria-label="Avans"
                    aria-describedby="basic-addon2"
                    value={this.state.value}
                    onChange={(e) => this.setState({ avans: e.target.value })}
                  />

                  <DropdownButton
                    as={InputGroup.Append}
                    title={this.state.monedăAvans}
                    id="monedaavans"
                  >
                    <Dropdown.Item
                      onClick={() =>
                        this.setState({
                          monedăAvans: 'RON',
                        })
                      }
                    >
                      RON
                    </Dropdown.Item>
                    <Dropdown.Item
                      onClick={() =>
                        this.setState({
                          monedăAvans: 'EUR',
                        })
                      }
                    >
                      EUR
                    </Dropdown.Item>
                  </DropdownButton>
                </InputGroup>
              </Form.Group>
            </Col>

            <Col md={12} />

            <Col md={10} />

            <Col md={6}>
              <Form.Group id="ultimazilucru">
                <Form.Label>Ultima zi de lucru</Form.Label>
                <Form.Control
                  type="date"
                  placeholder="data"
                  value={this.state.ultimaZiLucru}
                  onChange={(e) => {
                    this.setState({ ultimaZiLucru: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="casasanatate">
                <Form.Label>Casa de sănătate</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.casăSănătate}
                  onChange={(e) => {
                    this.setState({ casăSănătate: e.target.value });
                  }}
                >
                  <option>-</option>
                  {case_de_sanatate_component}
                </Form.Control>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="gradinvaliditate">
                <Form.Label>Grad invaliditate</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.gradInvalid}
                  onChange={(e) => {
                    this.setState({ gradInvalid: e.target.value });
                  }}
                >
                  <option>valid</option>
                  <option>invalid</option>
                </Form.Control>
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="nivelstudii">
                <Form.Label>Nivel studii</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.nivelStudii}
                  onChange={(e) => {
                    this.setState({ nivelStudii: e.target.value });
                  }}
                >
                  <option>-</option>
                  <option>Gimnaziale</option>
                  <option>Medii</option>
                  <option>Superioare</option>
                </Form.Control>
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="cor">
                <Form.Label>Cod COR</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="cod COR"
                  value={this.state.cor}
                  onChange={(e) => {
                    this.setState({ cor: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
          </Row>

          <Row>
            <Col md={6}>
              <Button
                variant={this.state.buttonDisabled ? 'outline-dark' : 'outline-primary'}
                onClick={(e) => this.onSubmit(e, this.state.id, this.state.idangajat)}
                disabled={this.state.buttonDisabled}
              >
                {this.state.id ? 'Actualizează contract' : 'Adaugă contract'}
              </Button>
            </Col>
          </Row>
        </Form>
      </React.Fragment>
    );
  }
}

export default Contract;
