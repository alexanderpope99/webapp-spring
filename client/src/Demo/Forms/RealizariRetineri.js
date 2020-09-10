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

    this.state = {
      show: false,
      modalMessage: '',

      an: '',
      luna: '',

      selected_persoana: '',
      angajati: [], // object: {idpersoana, idcontract, idsocietate}
      persoane: [], // date personale
      nume_persoane: [],
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
    // get only people with contract
    //* one query to get them all <- done on backend
    const persoane = await fetch('http://localhost:5000/persoana/c', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error(err));

    this.setState({ persoane: persoane }, () => {
      //set nume_persoane
      let nume_persoane = [];
      for (let persoana of persoane) {
        nume_persoane.push(persoana.nume + ' ' + persoana.prenume);
        console.log(nume_persoane);
      }
      this.setState({ nume_persoane: nume_persoane });
    });
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
      <option key="index">{nume}</option>
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
            <Card.Title as="h4">Angajați</Card.Title>
            <InputGroup className="mb-3">
              {/* NUMELE ANGAJATILOR CU CONTRACT */}
              <FormControl
                placeholder="Recipient's username"
                aria-label="Recipient's username"
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_persoana}
                onChange={(e) => {
                  this.setState({
                    selected_persoana: e.target.value,
                  });
                  // fill form with corresponding data
                }}
              >
                <option>-</option>
                {/* nume_persoane mapped to <option> */}
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
        </Card>
      </Aux>
    );
  }
}

export default RealizariRetineri;
