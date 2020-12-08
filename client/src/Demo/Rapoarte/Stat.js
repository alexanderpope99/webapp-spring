import React from 'react';
import { Row, Col, Card, Form, Button, FormControl } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { download } from '../Resources/download';
import months from '../Resources/months';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class Stat extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.creeazaStatSalarii = this.creeazaStatSalarii.bind(this);
    // this.download = this.download.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      intocmitDe: '',
      user: authService.getCurrentUser(),
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

  async creeazaStatSalarii(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let i = this.state.intocmitDe || '-';

    const created = await axios
      .get(
        `${server.address}/stat/${this.state.socsel.id}/mo=${luna.nr}&y=${an}&i=${i}/${this.state.user.id}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (created)
      download(
        `Stat Salarii - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    return (
      <React.Fragment>
        <Card className="border">
          <Card.Header>
            <Typography variant="h5">Ștat salarii</Typography>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={this.creeazaStatSalarii}>
              <Row>
                {/* LUNA */}
                <Col md={4}>
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
                </Col>
                {/* AN */}
                <Col md={4}>
                  <FormControl
                    type="number"
                    value={this.state.an}
                    onChange={(e) =>
                      this.setState({
                        an: e.target.value,
                      })
                    }
                  />
                </Col>
                <Col md={4}>
                  <Form.Group controlId="intocmitde">
                    <Form.Control
                      type="text"
                      placeholder="Intocmit de"
                      value={this.state.intocmitDe}
                      onChange={(e) =>
                        this.setState({
                          intocmitDe: e.target.value,
                        })
                      }
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Form>
            <div className="mt-2">
              <Button onClick={this.creeazaStatSalarii}>Stat salarii in Excel</Button>
            </div>
          </Card.Body>
        </Card>
      </React.Fragment>
    );
  }
}

export default Stat;
