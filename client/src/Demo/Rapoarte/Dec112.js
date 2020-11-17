import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Modal } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import months from '../Resources/months';

import axios from 'axios';
import authHeader from '../../services/auth-header';

class Dec112 extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.downloadXML = this.downloadXML.bind(this);
    this.downloadPDF = this.downloadPDF.bind(this);
    this.creeazaDec112XML = this.creeazaDec112XML.bind(this);
    this.creeazaDec112PDF = this.creeazaDec112PDF.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      d_rec: '',
      numeDeclarant: '',
      prenumeDeclarant: '',
      functieDeclarant: '',
      user: JSON.parse(localStorage.getItem('user')),
      show: false,
      errorMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.setCurrentYearMonth();
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

  // luna is object of type { nume: string, nr: int }
  async downloadXML(luna, an) {
    const token = this.state.user.accessToken;
    console.log('trying to download...');
    let societateNume = this.state.socsel.nume;
    await fetch(
      `${server.address}/download/${this.state.user.id}/Declaratia 112 - ${societateNume} - ${luna.nume} ${an}.xml`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/octet-stream',
          Authorization: `Bearer ${token}`,
        },
      }
    )
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `Declaratia 112 - ${societateNume} - ${luna.nume} ${an}.xml`;
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        console.log('downloaded');
      });
  }

  async downloadPDF(luna, an) {
    const token = this.state.user.accessToken;
    console.log('trying to download...');
    let societateNume = this.state.socsel.nume;
    await fetch(
      `${server.address}/download/${this.state.user.id}/Declaratia 112 - ${societateNume} - ${luna.nume} ${an}.pdf`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/octet-stream',
          Authorization: `Bearer ${token}`,
        },
      }
    )
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `Declaratia 112 - ${societateNume} - ${luna.nume} ${an}.pdf`;
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        console.log('downloaded');
      });
  }

  async creeazaDec112XML(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let d_rec = this.state.d_rec ? 1 : 0;
    let numeDec = this.state.numeDeclarant;
    let prenumeDec = this.state.prenumeDeclarant;
    let functieDec = this.state.functieDeclarant;

    const created = await axios
      .get(
        `${server.address}/dec112/${this.state.socsel.id}/mo=${luna.nr}&y=${an}&drec=${d_rec}&numeDec=${numeDec}&prenumeDec=${prenumeDec}&functieDec=${functieDec}/${this.state.user.id}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.status === 200)
      .catch((err) => this.setState({ show: true, errorMessage: err.response.data.message }));

    if (created) this.downloadXML(luna, an);
  }

  async creeazaDec112PDF(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let d_rec = this.state.d_rec ? 1 : 0;
    let numeDec = this.state.numeDeclarant;
    let prenumeDec = this.state.prenumeDeclarant;
    let functieDec = this.state.functieDeclarant;

    const created = await axios
      .get(
        `${server.address}/dec112/${this.state.socsel.id}/mo=${luna.nr}&y=${an}&drec=${d_rec}&numeDec=${numeDec}&prenumeDec=${prenumeDec}&functieDec=${functieDec}/${this.state.user.id}`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.status === 200)
      .catch((err) => this.setState({ show: true, errorMessage: err.response.data.message }));

    if (created) this.downloadPDF(luna, an);
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    return (
      <Card>
        <Card.Header>
          <Typography variant="h5">{this.state.socsel.nume} - Declarația 112</Typography>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={this.creeazaDec112}>
            <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
              <Modal.Header closeButton>
                <Modal.Title>Eroare</Modal.Title>
              </Modal.Header>
              <Modal.Body>{this.state.errorMessage}</Modal.Body>
              <Modal.Footer>
                <Button variant="primary" onClick={() => this.setState({ show: false })}>
                  OK
                </Button>
              </Modal.Footer>
            </Modal>
            <Row>
              {/* LUNA */}
              <Col md={4}>
                <Form.Group>
                  <Form.Control
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
                  </Form.Control>
                </Form.Group>
              </Col>
              {/* AN */}
              <Col md={4}>
                <Form.Group>
                  <FormControl
                    as="select"
                    value={this.state.an}
                    onChange={(e) =>
                      this.setState({
                        an: e.target.value,
                      })
                    }
                  >
                    {ani}
                  </FormControl>
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={4}>
                <Form.Group controlId="numeDeclarant">
                  <Form.Control
                    type="text"
                    placeholder="Nume Declarant"
                    value={this.state.numeDeclarant}
                    onChange={(e) =>
                      this.setState({
                        numeDeclarant: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="prenumeDeclarant">
                  <Form.Control
                    type="text"
                    placeholder="Prenume Declarant"
                    value={this.state.prenumeDeclarant}
                    onChange={(e) =>
                      this.setState({
                        prenumeDeclarant: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group controlId="functieDeclarant">
                  <Form.Control
                    type="text"
                    placeholder="Functie Declarant"
                    value={this.state.functieDeclarant}
                    onChange={(e) =>
                      this.setState({
                        functieDeclarant: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Col>
            </Row>
            <Row>
              <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
                <Form.Group controlId="d_rec">
                  <Form.Check
                    type="checkbox"
                    label="Decl. Rectificativă"
                    onChange={(e) =>
                      this.setState({
                        d_rec: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Col>
            </Row>
          </Form>
          <div className="mt-4">
            <Button onClick={this.creeazaDec112XML}>Generează XML</Button>
            <Button onClick={this.creeazaDec112PDF}>Generează PDF</Button>
          </div>
        </Card.Body>
      </Card>
    );
  }
}

export default Dec112;
