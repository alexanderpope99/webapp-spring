import React from 'react';
import { Row, Col, Form, FormControl } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { case_de_sanatate, judete } from '../../Resources/judete';
import axios from 'axios';
import authHeader from '../../../services/auth-header';
import { getAngajatSel } from '../../Resources/angajatsel';

const case_de_sanatate_component = case_de_sanatate.map((casa, index) => (
  <option key={index}>{casa}</option>
));

class ContractView extends React.Component {
  constructor() {
    super();
    this.handleClose = this.handleClose.bind(this);
    this.hasRequired = this.hasRequired.bind(this);
    this.fillForm = this.fillForm.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajatsel: getAngajatSel(),

      id: 0,
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '', //text
      echipa: '', //text,
      departament: '',
      calculdeduceri: true,
      studiiSuperioare: false,
      functieBaza: true,
      normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
      salariu: '',
      monedăSalariu: 'RON', //text
      idcontbancar: null,
      iban: '',
      numebanca: '',
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

      // superior
      angajat: null,
      superior: null,
      superiori: [],

      // centrucost
      centruCost: null,
      centreCost: [],
    };
  }

  clearFields() {
    this.setState({
      id: 0,
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '',
      echipa: '', //text,
      departament: '',
      calculdeduceri: true,
      studiiSuperioare: false,
      functieBaza: true,
      normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
      monedăSalariu: 'RON', //text
      salariu: '',
      idcontbancar: null,
      iban: '',
      numebanca: '',
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

      angajat: null,
      superior: null,
      centruCost: null,
    });
  }

  handleClose() {
    this.setState(
      {
        show: false,
        modalMessage: '',
      },
      this.props.scrollToTopSmooth
    );
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

  async getSuperiori() {
    if (!this.state.angajatsel) return;

    const superiori = await axios
      .get(`${server.address}/angajat/superiori-posibili/${this.state.angajatsel.idpersoana}`, {
        headers: authHeader(),
      })
      .then((res) =>
        res.data
          .map((angajat) => ({
            id: angajat.persoana.id,
            numeintreg: angajat.persoana.nume + ' ' + angajat.persoana.prenume,
          }))
          .filter((angajat) => angajat.id !== this.state.angajatsel.idpersoana)
      )
      .catch((err) => console.error(err));

    if (superiori) this.setState({ superiori: superiori });
  }

  async getCentreCost() {
    const centreCost = await axios
      .get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

    if (centreCost) this.setState({ centreCost: centreCost });
  }

  async fillForm() {
    this.getSuperiori();
    this.getCentreCost();

    const angajatsel = getAngajatSel();
    if (!angajatsel) return;

    const angajat = await axios
      .get(`${server.address}/angajat/expand/${angajatsel.idpersoana}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (angajat.contract) {
      this.clearFields();
      let contract = angajat.contract;
      let superior = angajat.superior
        ? {
            id: angajat.superior.persoana.id,
            numeintreg: angajat.superior.persoana.nume + ' ' + angajat.superior.persoana.prenume,
          }
        : null;
      let centruCost = contract.centrucost
        ? { id: contract.centrucost.id, nume: contract.centrucost.nume }
        : null;

      this.setState(
        {
          angajat: angajat,
          superior: superior,
          angajatsel: getAngajatSel(),

          id: contract.id,
          modelContract: contract.tip || '', //text
          numărContract: contract.nr || '', //text
          marca: contract.marca || '', //text
          dataContract: contract.data ? contract.data.substring(0, 10) : '',
          dataIncepere: contract.dataincepere ? contract.dataincepere.substring(0, 10) : '',
          punctDeLucru: contract.idpunctdelucru || '',
          centruCost: centruCost,
          echipa: contract.idechipa || '',
          departament: contract.iddepartament || '',
          functieBaza: contract.functiedebaza || false,
          calculdeduceri: contract.calculdeduceri || false,
          studiiSuperioare: contract.studiisuperioare || false,
          normăLucru: { nrOre: contract.normalucru, nume: this.getNumeNorma(contract.normalucru) },
          salariu: contract.salariutarifar || '',
          condițiiMuncă: contract.conditiimunca || '',
          idcontbancar: contract.contbancar ? contract.contbancar.id : null,
          iban: contract.contbancar ? contract.contbancar.iban : '',
          numebanca: contract.contbancar ? contract.contbancar.numebanca : '',
          sindicat: contract.sindicat || false,
          cotizațieSindicat: contract.cotizatiesindicat || '',
          pensiePrivată: contract.pensieprivata || false,
          cotizațiePensie: contract.cotizatiepensieprivata || '',
          avans: contract.avans || 0,
          monedăAvans: contract.monedaavans || 'RON',
          zileCOan: contract.zilecoan || 0,
          ultimaZiLucru: contract.ultimaZiLucru ? contract.ultimazilucru.substring(0, 10) : '',
          casăSănătate: contract.casasanatate || '-',
          gradInvalid: contract.gradinvaliditate || '', //text
          funcție: contract.functie || '', //text
          nivelStudii: contract.nivelstudii || '', //text
          cor: contract.cor || '',
          pensionar: contract.pensionar || false,
          spor: contract.spor || '',
        },
        () => console.log('idangajat:', angajat.idpersoana, '\tidcontract:', contract.id)
      );
    } else {
      // nu are contract
      this.clearFields();

      // if has adresa -> preselect casa_sanatate
      var adresa = angajat.persoana.adresa;
      var cs = '-';
      if (adresa) {
        if (adresa.judet) {
          if (adresa.judet.substring(0, 2) === 'SE') cs = case_de_sanatate[0];
          else cs = case_de_sanatate[judete.indexOf(adresa.judet)];
        }

        this.setState({ angajat: angajat, casăSănătate: cs }, () =>
          console.log('idangajat:', angajat.idpersoana, '\tidcontract:', null)
        );
      }
    }
  }

  componentDidMount() {
    // await get centre cost here
    // ... //

    this.fillForm();
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

    if (!this.state.dataContract || !this.state.dataIncepere) {
      this.setState({
        show: true,
        modalMessage: 'Contractul trebuie să aibă o dată și o dată de începere a activității.',
      });
      return false;
    }

    return true;
  }

  getCentruCostById(centruCost) {
    if (centruCost) return this.state.centreCost.find((cc) => cc.id === centruCost.id);
    else return null;
	}
	
  onChangeSuperior(e) {
    if (e.target.value === '-') {
      this.setState({ superior: null });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const idsuperior = e.target.options[selectedIndex].getAttribute('data-key');
    this.setState({ superior: { id: Number(idsuperior), numeintreg: e.target.value } });
  }

  onChangeCentruCost(e) {
    if (e.target.value === '-') {
      this.setState({ centruCost: null });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const idcentrucost = e.target.options[selectedIndex].getAttribute('data-key');
    this.setState({ centruCost: { id: Number(idcentrucost), nume: e.target.value } });
  }

  render() {
    const superioriComponent = this.state.superiori.map((superior) => (
      <option key={superior.id} data-key={superior.id}>
        {superior.numeintreg}
      </option>
    ));

    const centreCostComponent = this.state.centreCost.map((centruCost) => (
      <option key={centruCost.id} data-key={centruCost.id}>
        {centruCost.nume}
      </option>
    ));

    return (
      <React.Fragment>
        <Form onSubmit={(e) => e.preventDefault()}>
          <Row>
            <Col md={12}>
              <Form.Group controlId="functia">
                <Form.Label>Funcție</Form.Label>
                <Form.Control
                  disabled
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
                  disabled
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
                  disabled
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
                  disabled
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
                  disabled
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
                  disabled
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
                <Form.Label>
                  <Form.Check
                    disabled
                    custom
                    type="switch"
                    id="functieDeBazaCheck"
                    label="Funcție de bază"
                    checked={this.state.functieBaza}
                    onChange={(e) => {
                      this.setState({ functieBaza: e.target.checked });
                    }}
                    size="sm"
                  />
                </Form.Label>
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="calculdeduceri">
                <Form.Check
                  disabled
                  custom
                  type="switch"
                  id="deduceriCheck"
                  label="Calcul deduceri"
                  checked={this.state.calculdeduceri}
                  onChange={(e) => {
                    this.setState({ calculdeduceri: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="studiisuperioare">
                <Form.Check
                  disabled
                  custom
                  type="switch"
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
                  disabled
                  custom
                  type="switch"
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
                  disabled
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
                  disabled
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
                <FormControl
                  disabled
                  type="number"
                  required
                  placeholder="Salariu"
                  aria-label="Salariu"
                  aria-describedby="basic-addon2"
                  value={this.state.salariu}
                  onChange={(e) => this.setState({ salariu: e.target.value })}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="conditiidemunca">
                <Form.Label>Condiții de muncă</Form.Label>
                <Form.Control
                  disabled
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
            <Col md={12} className="border rounded pt-3">
              <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                Cont bancar
              </Typography>
              <Row>
                <Col md={6}>
                  <Form.Group id="iban">
                    <Form.Label>IBAN</Form.Label>
                    <Form.Control
                      disabled
                      type="text"
                      value={this.state.iban}
                      style={{ fontFamily: 'Consolas, Courier New' }}
                      onChange={(e) => {
                        this.setState({ iban: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="numebanca">
                    <Form.Label>Nume bancă</Form.Label>
                    <Form.Control
                      disabled
                      type="text"
                      value={this.state.numebanca}
                      onChange={(e) => {
                        this.setState({ numebanca: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Col>
            <Col md={6}>
              <Form.Group controlId="punctdelucru">
                <Form.Label>Punct de lucru</Form.Label>
                <Form.Control
                  disabled
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
                <Form.Control
                  disabled
                  as="select"
                  value={this.state.centruCost ? this.state.centruCost.nume : '-'}
                  onChange={(e) => this.onChangeCentruCost(e)}
                >
                  <option>-</option>
                  {centreCostComponent}
                </Form.Control>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="echipa">
                <Form.Label>Echipa</Form.Label>
                <Typeahead
                  disabled
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
                  disabled
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
                  disabled
                  custom
                  type="switch"
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
                    disabled
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
                  disabled
                  custom
                  type="switch"
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
                    disabled
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
                  disabled
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
                <FormControl
                  disabled
                  type="number"
                  placeholder="Avans"
                  aria-label="Avans"
                  aria-describedby="basic-addon2"
                  value={this.state.value}
                  onChange={(e) => this.setState({ avans: e.target.value })}
                />
              </Form.Group>
            </Col>

            <Col md={12} />

            <Col md={10} />

            <Col md={6}>
              <Form.Group id="ultimazilucru">
                <Form.Label>Ultima zi de lucru</Form.Label>
                <Form.Control
                  disabled
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
                  disabled
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
                  disabled
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
                  disabled
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
                  disabled
                  type="text"
                  placeholder="cod COR"
                  value={this.state.cor}
                  onChange={(e) => {
                    this.setState({ cor: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="superior">
                <Form.Label>Superior</Form.Label>
                <Form.Control
                  disabled
                  as="select"
                  value={this.state.superior ? this.state.superior.numeintreg : '-'}
                  onChange={(e) => this.onChangeSuperior(e)}
                >
                  <option>-</option>
                  {superioriComponent}
                </Form.Control>
              </Form.Group>
            </Col>
          </Row>
        </Form>
      </React.Fragment>
    );
  }
}

export default ContractView;
