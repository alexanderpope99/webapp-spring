import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { download } from '../Resources/download';
import { luni } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class Stat extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.recalcSocietate = this.recalcSocietate.bind(this);
    this.creeazaStatSalarii = this.creeazaStatSalarii.bind(this);
    // this.download = this.download.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      intocmitDe: '',
      user: authService.getCurrentUser(),

      showToast: false,
      toastMessage: '',

      tip: '',
      tipStat: '0',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.setCurrentYearMonth();
  }

  setCurrentYearMonth() {
    let today = new Date();
    let luna = luni[today.getMonth()];
    let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  async creeazaStatSalarii(e, format) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let i = this.state.intocmitDe || '-';

    console.log(this.state.tipStat);

    const created = await axios
      .get(
        `${server.address}/stat/${this.state.socsel.id}/mo=${luna.nr}&y=${an}&i=${i}/${this.state.user.id}/${this.state.tipStat}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea ștatul de salarii ' + err.response.data.message,
        })
      );

    if (created && this.state.tipStat === '0')
      download(
        `Stat Salarii - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );

    if (created && this.state.tipStat === '1')
      download(
        `Stat Salarii (doar impozit) - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );

    if (created && this.state.tipStat === '2')
      download(
        `Stat Salarii (fara impozit) - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );

    // if (created && format === 'PDF')
    //   download(
    //     `Stat Salarii - ${this.state.socsel.nume} - ${luna.nume} ${an}.pdf`,
    //     this.state.user.id
    //   );
  }

  async recalcSocietate() {
    let luna = this.state.luna;
    let an = this.state.an;

    const ok = await axios
      .put(
        `${server.address}/realizariretineri/recalc/societate/ids=${this.state.socsel.id}&mo=${luna.nr}&y=${an}`,
        {},
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut recalcula realizari/retineri ' + err.response.data.message,
        })
      );

    if (ok) {
      this.setState({
        showToast: true,
        toastColor: 'lightgreen',
        toastTitle: 'Recalculate',
        toastMessage: `Toate salariile din luna ${luna.nume} au fost recalculate pentru ${this.state.socsel.nume}`,
      });
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    }
  }

  render() {
    const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    return (
      <React.Fragment>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={5000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: this.state.toastColor }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">{this.state.toastTitle}</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        <Card className="border">
          <Card.Header>
            <Typography variant="h5">Ștat salarii</Typography>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={this.creeazaStatSalarii}>
              <Row>
                {/* LUNA */}
                <Col md={3}>
                  <Form.Group controlId="luna">
                    <Form.Control
                      as="select"
                      value={this.state.luna.nume}
                      onChange={(e) =>
                        this.setState({
                          luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
                        })
                      }
                    >
                      {luniComponent}
                    </Form.Control>
                  </Form.Group>
                </Col>
                {/* AN */}
                <Col md={3}>
                  <Form.Group controlId="an">
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
                <Col md={3}>
                  <Form.Group controlId="tipstat">
                    <FormControl
                      as="select"
                      value={this.state.tip}
                      onChange={(e) =>
                        this.setState({
                          tip: e.target.value,
                          tipStat: e.target.options[e.target.options.selectedIndex].getAttribute(
                            'data-key'
                          ),
                        })
                      }
                    >
                      <option data-key="0">Toți Angajații</option>
                      <option data-key="1">Angajații Cu Impozit</option>
                      <option data-key="2">Angajații Fără Impozit</option>
                    </FormControl>
                  </Form.Group>
                </Col>
								<Col md={3}>
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
              <Button onClick={(e) => this.creeazaStatSalarii(e, 'XLSX')}>
                Ștat salarii Excel
              </Button>
              <Button onClick={this.recalcSocietate}>Recalculează toate salariile</Button>
            </div>
          </Card.Body>
        </Card>
      </React.Fragment>
    );
  }
}

export default Stat;
