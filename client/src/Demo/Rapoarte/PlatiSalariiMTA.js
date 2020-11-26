import React from 'react';
import { Row, Col, Card, Form, Button, FormControl } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import months from '../Resources/months';

class PlatiSalariiMTA extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.download = this.download.bind(this);
    this.creeazaMTA = this.creeazaMTA.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      d_rec: '',
      numeDeclarant: '',
      prenumeDeclarant: '',
      functieDeclarant: '',
      user: JSON.parse(localStorage.getItem('user')),
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

  async download(luna, an) {
    const token = this.state.user.accessToken;
    console.log('trying to download...');
    //let societateNume = this.state.socsel.nume;
    await fetch(`${server.address}/download/${this.state.user.id}/FisierMTA.xlsx`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/octet-stream',
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `FisierMTA - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`;
        document.body.appendChild(a);
        a.click();
        a.remove(); //afterwards we remove the element again
        console.log('downloaded');
      });
  }

  async creeazaMTA(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;

    const created = await fetch(
      `${server.address}/mta/${this.state.socsel.id}&mo=${luna.nr}&y=${an}/${this.state.user.id}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${this.state.user.accessToken}`,
        },
      }
    )
      .then((res) => res.ok)
      .catch((err) => console.error(err));

    if (created) this.download(luna, an);
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    return (
      <Card>
        <Card.Header>
          <Typography variant="h5">Plăți Salarii MTA</Typography>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={this.creeazaMTA}>
            <Row>
              {/* LUNA */}
              <Col md={4}>
                <Form.Group>
                  <Form.Control
                    as="select"
                    value={this.state.luna.nume}
                    onChange={(e) =>
                      this.setState({
                        luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
                      })
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
                    type="number"
                    value={this.state.an}
                    onChange={(e) =>
                      this.setState({
                        an: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Col>
            </Row>
          </Form>
          <div className="mt-4">
            <Button onClick={this.creeazaMTA}>Generează MTA</Button>
          </div>
        </Card.Body>
      </Card>
    );
  }
}

export default PlatiSalariiMTA;
