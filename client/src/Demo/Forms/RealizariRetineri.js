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
    };
  }

  componentDidMount() {
    this.setCurrentYearMonth();
    this.getAngajatiWithContract();
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

  async getAngajatiWithContract() {
    // fetch everyone with idcontract !== null
    const angajati = await fetch('http://localhost:5000/angajat/c', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => {
        if (res.ok) return res.json();
      })
      .catch((err) => console.error());
    console.log(angajati);
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
            <Card.Title as="h4">Persoana</Card.Title>
            <InputGroup className="mb-3">
              <FormControl
                placeholder="Recipient's username"
                aria-label="Recipient's username"
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_persoana}
                onChange={(e) =>
                  this.setState(
                    {
                      selected_persoana: e.target.value,
                      id: this.getIdByNumeintreg(e.target.value),
                    }
                    // this.fillForm
                  )
                }
              >
                <option>-</option>
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
