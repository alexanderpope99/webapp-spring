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
} from 'react-bootstrap';
import Add from '@material-ui/icons/Add';
import Aux from '../../hoc/_Aux';
import months from '../Resources/months';

class RealizariRetineri extends React.Component {
  constructor(props) {
    super(props);
    this.setCurrentYearMonth = this.setCurrentYearMonth.bind(this);
    this.getIdByNumeComplet = this.getIdByNumeComplet.bind(this);

    this.state = {
      show: false,
      modalMessage: '',

      an: '',
      luna: '',

      selected_nume: 'a',
      angajati: [], // object: {idpersoana, idcontract, idsocietate}
      persoane: [], // date personale
      nume_persoane: [],
      contract: [],
    };
  }

  componentDidMount() {
    this.setCurrentYearMonth(); // modifies state.an, state.luna
    this.setAngajatiWithContract(); // sets this.state.angajati
    this.setPersoane(); // date personale, also fills nume_persoane
  }

  setCurrentYearMonth() {
    let today = new Date();
    let luna = months[today.getMonth() - 1];
    let an = today.getFullYear();

    //  set them
    this.setState({
      an: an,
      luna: luna,
    });
  }

  async setAngajatiWithContract() {
    // fetch everyone with idcontract !== null
    const angajati = await fetch('http://localhost:5000/angajat/c', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error());
    this.setState({ angajati: angajati });
  }

  async setPersoane() {
    //* only people with contract
    //* one query to get them all <- done on backend
    const persoane = await fetch('http://localhost:5000/persoana/c', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error(err));

    //* set nume_persoane
    this.setState({ persoane: persoane }, () => {
      let nume_persoane = [];
      for (let persoana of persoane) {
        nume_persoane.push(persoana.nume + ' ' + persoana.prenume);
      }
      this.setState({ nume_persoane: nume_persoane });
    });
  }

  getIdByNumeComplet(nume_complet) {
    const persoana = this.state.persoane.find(
      (persoana) => persoana.nume + ' ' + persoana.prenume === nume_complet
    );
    if (typeof persoana === 'undefined') return null;
    return persoana.id;
  }

  async fillForm() {
    // get id by numecomplet
    const idpersoana = this.getIdByNumeComplet(this.state.selected_nume);
    // get date contrat
    const contract = await fetch(`http://localhost:5000/contract/idp=${idpersoana}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error(err));
    // console.log(contract);
    // form reacts to this.state.contract + other states
    this.setState({ contract: contract });
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    const nume_persoane_opt = this.state.nume_persoane.map((nume, index) => (
      <option key={index}>{nume}</option>
    ));

    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Date incomplete</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Card>
          <Card.Header>
            {/* LUNA + YEAR */}
            <Row>
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.luna}
                  onChange={(e) => this.setState({ luna: e.target.value })}
                >
                  {luni}
                </FormControl>
              </Col>
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.an}
                  onChange={(e) => this.setState({ an: e.target.value })}
                >
                  {ani};
                </FormControl>
              </Col>
            </Row>
          </Card.Header>

          <Card.Header>
            <Card.Title as="h4">Angajat</Card.Title>
            <InputGroup className="mb-3">
              {/* NUMELE ANGAJATILOR CU CONTRACT */}
              <FormControl
                placeholder="Recipient's username"
                aria-label="Recipient's username"
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_nume.nume_complet}
                onChange={(e) => {
                  this.setState(
                    {
                      selected_nume: e.target.value,
                    },
                    () => this.fillForm()
                  );
                  // fill form with corresponding data
                }}
              >
                <option>-</option>
                {/* nume_persoane mapped as <option> */}
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
            <Form>
              <Row>
                {/* LEFT */}
                <Col md={6} className="border pt-3">
                  <Row>
                    <Col md={12}>
                      <Form.Group id="totaltrepturi">
                        <Form.Label>Total drepturi</Form.Label>
                        <Form.Control type="number" disabled value={this.state.totaldrepturi} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="cas">
                        <Form.Label>Total drepturi</Form.Label>
                        <Form.Control type="number" disabled value={this.state.cas} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="cass">
                        <Form.Label></Form.Label>
                        <Form.Control type="number" disabled value={this.state.cass} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="valtichete">
                        <Form.Label>Valoare tichete</Form.Label>
                        <Form.Control type="number" disabled value={this.state.tichete} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="impozit">
                        <Form.Label>Impozit</Form.Label>
                        <Form.Control type="number" disabled value={this.state.impozit} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="restplata">
                        <Form.Label>Rest de plată</Form.Label>
                        <Form.Control type="number" disabled value={this.state.restplata} />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>

                {/* RIGHT */}
                <Col md={6} className="border pt-3">
                  <Row>
                    <Col md={6}>
                      <Form.Group id="functie">
                        <Form.Label>Funcție</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.contract.functie || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="duratailucru">
                        <Form.Label>Durată zi lucru</Form.Label>
                        <Form.Control
                          type="number"
                          disabled
                          value={this.state.contract.duratazilucru || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="normazilucru">
                        <Form.Label>Normă lucru</Form.Label>
                        <Form.Control
                          type="number"
                          disabled
                          value={this.state.contract.normalucru || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="salariubrut">
                        <Form.Label>Salariu brut</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.contract.salaritarifar || ''}
                        />
                      </Form.Group>
                    </Col>

                    {/* // TODO */}
                    <Col md={6}>
                      <Form.Group id="orelucrate">
                        <Form.Label>Ore lucrate</Form.Label>
                        <Form.Control type="number" disabled value={this.state.orelucrate || ''} />
                      </Form.Group>
                    </Col>

                    <Col md={6}>
                      <Form.Group id="tichete">
                        <Form.Label>Tichete</Form.Label>
                        <Form.Control
                          type="number"
                          value={this.state.tichete || ''}
                          onChange={(e) => this.setState({ tichete: e.target.value })}
                        />
                      </Form.Group>
                    </Col>

                    <Col md={6}>
                      <Form.Group id="zilecm">
                        <Form.Label>Zile concediu medical</Form.Label>
                        <Form.Control type="number" disabled value={this.state.zilecm || ''} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilectotal">
                        <Form.Label>Zile concediu total</Form.Label>
                        <Form.Control type="text" disabled value={this.state.zilectotal || ''} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="oresuplimentare">
                        <Form.Label>Ore suplimentare</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.state.oresuplimentare || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilelibere">
                        <Form.Label>Zile libere</Form.Label>
                        <Form.Control type="text" value={this.state.zilelibere || ''} onChange={(e) => this.setState({ zilelibere: e.target.value })}/>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileinvoire">
                        <Form.Label>Zile învoire</Form.Label>
                        <Form.Control type="text" value={this.state.zileinvoire || ''} onChange={(e) => this.setState({ zileinvoire: e.target.value })}/>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="primabruta">
                        <Form.Label>Primă brută</Form.Label>
                        <Form.Control
                          type="text"
                          value={this.state.primabruta || ''}
                          onChange={(e) => this.setState({ primabruta: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
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
