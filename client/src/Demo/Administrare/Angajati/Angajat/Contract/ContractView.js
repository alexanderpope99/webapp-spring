import React from 'react';
import { Row, Col, Form, FormControl, Toast } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../../../../Resources/server-address';
import { getSocSel } from '../../../../Resources/socsel';
import { case_de_sanatate, judete } from '../../../../Resources/judete';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';
import { getAngajatSel } from '../../../../Resources/angajatsel';

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

      showToast: false,
      toastMessage: '',
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
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut prelua angajații superiori posibili ' + err.response.data.message,
        })
      );

    if (superiori) this.setState({ superiori: superiori });
  }

  async getCentreCost() {
    const centreCost = await axios
      .get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua centrele de cost ' + err.response.data.message,
        })
      );

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
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua angajații ' + err.response.data.message,
        })
      );

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
    return (
      <React.Fragment>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        <Form onSubmit={(e) => e.preventDefault()}>
          <Row>
            <Col md={12}>
              <Form.Group controlId="functia">
                <Form.Label>Funcție</Form.Label>
                <Form.Control disabled placeholder="functia" value={this.state.funcție} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="tip">
                <Form.Label>Model Contract</Form.Label>
                <Form.Control disabled type="text" value={this.state.modelContract} />
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="nrcontract">
                <Form.Label>Număr contract</Form.Label>
                <Form.Control
                  disabled
                  type="text"
                  placeholder="Număr contract"
                  value={this.state.numărContract}
                />
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="marca">
                <Form.Label>Marca</Form.Label>
                <Form.Control disabled type="text" placeholder="Marca" value={this.state.marca} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group controlId="dataincepere">
                <Form.Label>Data începere activitate</Form.Label>
                <Form.Control disabled type="date" value={this.state.dataIncepere} />
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
                />
              </Form.Group>
            </Col>
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="normalucru">
                <Form.Label>Normă de lucru</Form.Label>
                <Form.Control disabled type="text" value={this.state.normăLucru.nume} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="zilecoan">
                <Form.Label>Zile CO/an</Form.Label>
                <Form.Control disabled placeholder="0" type="number" value={this.state.zileCOan} />
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
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="conditiidemunca">
                <Form.Label>Condiții de muncă</Form.Label>
                <Form.Control disabled type="text" value={this.state.condițiiMuncă} />
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
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="numebanca">
                    <Form.Label>Nume bancă</Form.Label>
                    <Form.Control disabled type="text" value={this.state.numebanca} />
                  </Form.Group>
                </Col>
              </Row>
            </Col>
            <Col md={6}>
              <Form.Group controlId="punctdelucru">
                <Form.Label>Punct de lucru</Form.Label>
                <Form.Control disabled type="text" value={this.state.punctDeLucru || '-'} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="centrucost">
                <Form.Label>Centre de cost</Form.Label>
                <Form.Control
                  disabled
                  type="text"
                  value={this.state.centruCost ? this.state.centruCost.nume : '-'}
                />
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
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="departament">
                <Form.Label>Departament</Form.Label>
                <Form.Control disabled type="text" value={this.state.departament || '-'} />
              </Form.Group>
            </Col>

            <Col md={12} />
            <Col md={1}>
              <Form.Group id="sindicat" style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}>
                <Form.Check disabled custom type="switch" id="sindicatCheck" label="Sindicat" />
              </Form.Group>
            </Col>
            {this.state.sindicat ? (
              <Col md={3}>
                <Form.Group id="cotizatiesindicat">
                  <Form.Label>Cotizație sindicat</Form.Label>
                  <Form.Control disabled placeholder="0" value={this.state.cotizațieSindicat} />
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
                />
              </Form.Group>
            </Col>
            {this.state.pensiePrivată ? (
              <Col md={3}>
                <Form.Group id="cotizatiepensieprivata">
                  <Form.Label>Cotizație pensie privată</Form.Label>
                  <Form.Control disabled placeholder="0" value={this.state.cotizațiePensie} />
                </Form.Group>
              </Col>
            ) : null}
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="sporuri">
                <Form.Label>Sporuri permanente</Form.Label>
                <Form.Control disabled type="text" value={this.state.spor} />
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
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="casasanatate">
                <Form.Label>Casa de sănătate</Form.Label>
                <Form.Control disabled type="text" value={this.state.casăSănătate} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="gradinvaliditate">
                <Form.Label>Grad invaliditate</Form.Label>
                <Form.Control disabled type="text" value={this.state.gradInvalid} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="nivelstudii">
                <Form.Label>Nivel studii</Form.Label>
                <Form.Control disabled type="text" value={this.state.nivelStudii} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="cor">
                <Form.Label>Cod COR</Form.Label>
                <Form.Control disabled type="text" placeholder="cod COR" value={this.state.cor} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="superior">
                <Form.Label>Superior</Form.Label>
                <Form.Control
                  disabled
                  type="text"
                  value={this.state.superior ? this.state.superior.numeintreg : '-'}
                />
              </Form.Group>
            </Col>
          </Row>
        </Form>
      </React.Fragment>
    );
  }
}

export default ContractView;
