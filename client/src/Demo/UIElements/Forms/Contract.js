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

// import Aux from '../../../hoc/_Aux';
//import { isNumeric } from 'jquery';

class Contract extends React.Component {
  constructor(props) {
    super(props);
    this.handleClose = this.handleClose.bind(this);
    this.onChangeCentrucost = this.onChangeCentrucost.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.hasRequired = this.hasRequired.bind(this);

    this.state = {
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '', //text
      centruCost: '', //text
      echipa: '', //text,
      departament: '',
      deduceri: false,
      studiiSuperioare: false,
      functieBaza: false,
      normăLucru: 'Normă întreagă', //text
      monedăSalariu: 'RON', //text
      salariu: 0,
      modPlată: 'Transfer bancar', //text
      condițiiMuncă: 'Smechere', //text
      sindicat: false,
      cotizațieSindicat: '',
      pensiePrivată: false,
      cotizațiePensie: '',
      spor: 0,
      avans: 0,
      monedăAvans: 'RON', //text
      zileCOan: 0,
      casăSănătate: '', //text
      gradInvalid: 'valid', //text
      funcție: '', //text
      nivelStudii: '', //text
      cor: '',
      dataIncepere: new Date().toJSON().slice(0, 10),
      dataContract: new Date().toJSON().slice(0, 10),
      ultimaZiLucru: '',
      pensionar: false,

      show: false,
      modalMessage: '', //text
    };
  }

  clearFields() {
    this.setState({
      modelContract: 'Contract de munca', //text
      numărContract: '', //text
      marca: '', //text
      punctDeLucru: '', //text
      centruCost: '', //text
      echipa: '', //text,
      departament: '',
      deduceri: false,
      studiiSuperioare: false,
      functieBaza: false,
      normăLucru: 'Normă întreagă', //text
      monedăSalariu: 'RON', //text
      salariu: 0,
      modPlată: 'Transfer bancar', //text
      condițiiMuncă: 'Smechere', //text
      sindicat: false,
      cotizațieSindicat: '',
      pensiePrivată: false,
      cotizațiePensie: '',
      spor: 0,
      avans: 0,
      monedăAvans: 'RON', //text
      zileCOan: 0,
      casăSănătate: '', //text
      gradInvalid: 'valid', //text
      funcție: '', //text
      nivelStudii: '', //text
      cor: '',
      dataIncepere: new Date().toJSON().slice(0, 10),
      dataContract: new Date().toJSON().slice(0, 10),
      ultimaZiLucru: '',
      pensionar: false,

      show: false,
      modalMessage: '', //text
    });
  }

  onChangeCentrucost(selected) {
    if (typeof selected[0] !== 'undefined' || selected.length !== 0) {
      if (typeof selected[0] === 'object') this.setState({ centruCost: selected[0].label });
      else this.setState({ centruCost: selected[0] });
    }
    console.log(this.state.centruCost);
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  hasRequired() {
    if (this.state.numărContract === '') {
      this.setState({
        show: true,
        modalMessage: 'Contractul trebuie să aibă un număr.',
      });
      return false;
    }

    if (this.state.numărContract === '') {
      this.setState({
        show: true,
        modalMessage: 'Contractul trebuie să aibă o marcă.',
      });
      return false;
    }

    return true;
  }

  async onSubmit(e) {
    e.preventDefault();

    if (!this.hasRequired()) return;

    for (const key in this.state) {
      if (this.state[key] === '' || this.state[key] === "''" || this.state[key] === '-')
        this.state[key] = null;
      // console.log(this.state[key]);
    }
    //TODO - VALIDĂRI

    // console.log('centruCost:', this.state.centruCost);

    var idpunctlucru = null,
      idcentrucost = null,
      idechipa = null,
      iddepartament = null;

    // const centrucost_body = {
    //   // TODO
    //   idadresa: null,
    //   idsocietate: null,
    //   nume: this.state.centruCost,
    // };

    // idpunctlucru = await fetch(`http://localhost:5000/idpunctlucru/${this.state.punctDeLucru}`, {
    //   method: 'GET',
    //   headers: { 'Content-Type': 'application/json' },
    // }).then((idpunctlucru) => idpunctlucru.json());

    // idcentrucost = await fetch('http://localhost:5000/centrucost', {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: JSON.stringify(centrucost_body),
    // }).then((idcentrucost) => idcentrucost.json());

    // const departament_body = {
    //   idadresa: null,
    //   idsocietate: null,
    //   nume: this.state.departament,
    // }

    // iddepartament = await fetch('http://localhost:5000/departament', {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: JSON.stringify(departament_body),
    // }).then((iddepartament) => iddepartament.json());

    const contract_body = {
      tip: this.state.modelContract,
      nr: this.state.numărContract,
      marca: this.state.marca,
      data: this.state.dataContract,
      dataincepere: this.state.dataIncepere,
      idpunctlucru: idpunctlucru,
      idcentrucost: idcentrucost,
      idechipa: idechipa,
      iddepartament: iddepartament,
      functiedebaza: this.state.functieBaza,
      calculdeduceri: this.state.deduceri,
      studiisuperioare: this.state.studiiSuperioare,
      normalucru: this.state.normăLucru,
      salariutarifar: this.state.salariu,
      monedasalariu: this.state.monedăSalariu,
      modplata: this.state.modPlată,
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

    const idcontract = await fetch('http://localhost:5000/contract', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(contract_body),
    })
      .then((idcontract) => idcontract.json())
      .catch((err) => {
        console.log(err.message);
      });

    if (typeof this.props.asChild === 'undefined') {
      if (typeof idcontract === 'number') {
        // console.log('idcontract:', idcontract);
        this.clearFields();
        this.setState({
          show: true,
          modalMessage: 'Contract adăugat cu succes.',
        });
        console.log('idcontract:', idcontract);
        return idcontract;
      } else return;
    } else if (typeof idcontract === 'number') return idcontract;
    else return;
  }

  render() {
    return (
      <div>
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
        <Form onSubmit={this.onSubmit}>
          <Row>
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
                  value={new Date().toJSON().slice(0, 10)}
                  selected={this.state.dataContract}
                  onChange={(e) => {
                    this.setState({ dataContract: e.target.value });
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
                  options={['centru 1', 'centru smecherie']}
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
                  options={['echipa 1', 'echipa smechera']}
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
                  options={['optiunea 1', 'smecherie']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.departament}
                  onChange={(selected) => {
                    this.setState({ departament: selected });
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
                  value={this.state.functieBaza}
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
                  value={this.state.deduceri}
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
                  value={this.state.studiiSuperioare}
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
                  value={this.state.pensionar}
                  onChange={(e) => {
                    this.setState({ pensionar: e.target.checked });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={12} />

            <Col md={4}>
              <Form.Group id="normalucru">
                <Form.Label>Normă de lucru</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.normăLucru}
                  onChange={(e) => {
                    this.setState({ normăLucru: e.target.value });
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
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="salariu">
                <Form.Label>Salariu</Form.Label>
                <InputGroup>
                  <FormControl
                    type="number"
                    placeholder="Salariu"
                    aria-label="Salariu"
                    aria-describedby="basic-addon2"
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
              <Form.Group id="modplata">
                <Form.Label>Mod de plată</Form.Label>
                <Form.Control
                  as="select"
                  value={this.state.modPlată}
                  onChange={(e) => {
                    this.setState({ modPlată: e.target.value });
                  }}
                >
                  <option>Transfer bancar</option>
                  <option>Card</option>
                  <option>Cash</option>
                </Form.Control>
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
                  <option>Smechere</option>
                  <option>Nașpa</option>
                </Form.Control>
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
                  value={this.state.sindicat}
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
                  value={this.state.pensiePrivată}
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
            <Col md={2}>
              <Form.Group id="zilecoan">
                <Form.Label>Zile CO/an</Form.Label>
                <Form.Control
                  placeholder="0"
                  value={this.state.zileCOan}
                  onChange={(e) => {
                    this.setState({ zileCOan: e.target.value });
                  }}
                />
              </Form.Group>
            </Col>
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
                  <option>optiuni</option>
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
          {typeof this.props.asChild === 'undefined' ? (
            <Row>
              <Col md={6}>
                <Button variant="outline-primary" type="submit">
                  Adaugă
                </Button>
              </Col>
            </Row>
          ) : null}
        </Form>
      </div>
    );
  }
}

export default Contract;
