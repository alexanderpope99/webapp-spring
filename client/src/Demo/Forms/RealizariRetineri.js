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
import { getSocSel } from '../Resources/socsel';

class RealizariRetineri extends React.Component {
  constructor() {
    super();
    this.setCurrentYearMonth = this.setCurrentYearMonth.bind(this);

    this.state = {
      socsel: getSocSel(),

      show: false,
      modalMessage: '',

      an: '',
      luna: '',

      selected_angajat: '',
      lista_angajati: [], // object: {nume, id}
      contract: [], // required for 4 fields

      totaldrepturi: '',
      cas: '',
      cass: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',

      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilecm: '',
      zilec: '',
      oresuplimentare: '',
      zilelibere: '',
      zileinvoire: '',
      primabruta: '',
    };
  }

  componentDidMount() {
    this.setCurrentYearMonth(); // modifies state.an, state.luna
    this.setPersoane(); // date personale, also fills lista_angajati
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
    const persoane = await fetch(`http://localhost:5000/persoana/ids=${this.state.socsel.id}&c`, {
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
    let an = this.state.an;
    let luna = this.state.luna.nr;
    // get id by numecomplet
    const idpersoana = this.state.selected_angajat.id;
    if (!idpersoana) {
      this.clearForm();
      return;
    }
    // get date contrat
    const data = await fetch(
      `http://localhost:5000/realizariretineri/idp=${idpersoana}&mo=${luna}&y=${an}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }
    )
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error(err));
    console.log(data);
    this.setState({ nrtichete: data.nrtichete });
  }

  clearForm() {
    this.setState({});
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

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    const nume_persoane_opt = this.state.lista_angajati.map((angajat) => (
      <option key={angajat.id} data-key={angajat.id}>
        {angajat.nume}
      </option>
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
            {/* LUNA + AN */}
            <Row>
              {/* LUNA */}
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.luna.nume}
                  onChange={(e) =>
                    this.setState({
                      luna: { nume: e.target.value, nr: e.target.options.selectedIndex },
                    })
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
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_angajat}
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
            <Form>
              <Row>
                {/* LEFT */}
                <Col md={6} className="border rounded pt-3">
                  <Row>
                    <Col md={12}>
                      <Form.Group id="totaltrepturi">
                        <Form.Label>Total drepturi</Form.Label>
                        <Form.Control type="number" disabled value={this.state.totaldrepturi} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="cas">
                        <Form.Label>CAS</Form.Label>
                        <Form.Control type="number" disabled value={this.state.cas} />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="cass">
                        <Form.Label>CASS</Form.Label>
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
                <Col md={6} className="border rounded pt-3">
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
                          type="text"
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
                        <Form.Label>Nr. Tichete</Form.Label>
                        <Form.Control
                          type="number"
                          value={this.state.nrtichete || ''}
                          onChange={(e) => this.setState({ nrtichete: e.target.value })}
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
                        <Form.Control
                          type="text"
                          value={this.state.zilelibere || ''}
                          onChange={(e) => this.setState({ zilelibere: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileinvoire">
                        <Form.Label>Zile învoire</Form.Label>
                        <Form.Control
                          type="text"
                          value={this.state.zileinvoire || ''}
                          onChange={(e) => this.setState({ zileinvoire: e.target.value })}
                        />
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
