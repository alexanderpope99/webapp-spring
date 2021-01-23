import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { luni } from '../Resources/calendar';
import authService from '../../services/auth.service';
import authHeader from '../../services/auth-header';
import { download } from '../Resources/download';

class PlatiSalariiMTA extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.creeazaMTA = this.creeazaMTA.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      d_rec: '',
      numeDeclarant: '',
      prenumeDeclarant: '',
      functieDeclarant: '',
      user: authService.getCurrentUser(),

      conturiBancare: [],
      idContBancar: null,
      numeBanca: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.setCurrentYearMonth();
    this.getConturi();
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

  async getConturi() {
    const conturi = await axios
      .get(`${server.address}/contbancar/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua conturile societății ' + err.response.data.message,
        })
      );
    if (conturi) this.setState({ conturiBancare: conturi, idContBancar: conturi[0].id, numeBanca: conturi[0].numebanca });
  }

  async creeazaMTA(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let socselId = this.state.socsel.id;
    let userId = this.state.user.id;
    let idContBancar = this.state.idContBancar;
    if (!idContBancar) {
      this.setState({
        showToast: true,
        toastMessage: 'Selectați un cont plătitor',
      })
    }

    const created = await axios
      .get(
        `${server.address}/mta/${socselId}&mo=${luna.nr}&y=${an}/${userId}/${idContBancar}`,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea MTA ' + err.response.data.message,
        })
      );

    if (created)
      download(
        `FisierMTA - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );
  }

  onChangeCont(e) {
    if (e.target.value === '-') {
      this.setState({ idContBancar: 0, numeBanca: '-' });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const idcont = e.target.options[selectedIndex].getAttribute('data-key');

    this.setState({ idContBancar: Number(idcont), numeBanca: e.target.value });
  }

  render() {
    const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    // const conturiComponent = this.state.conturiBancare.map((cont) => (
    //   <option key={cont.id} data-key={cont.id}>
    //     {cont.numebanca}
    //   </option>
    // ));

    return (
      <Card>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>

        <Card.Header>
          <Typography variant="h5">Plăți Salarii MTA</Typography>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={this.creeazaMTA}>
            <Row>
              {/* LUNA */}
              <Col md={4}>
                <Form.Group>
                  <Form.Label>Luna</Form.Label>
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
              <Col md={4}>
                <Form.Group>
                  <Form.Label>An</Form.Label>
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
              {/* CONT PLATITOR */}
              {/* <Col md={4}>
                <Form.Label>Contul din care se face plata</Form.Label>
                <Form.Group>
                  <Form.Control
                    as="select"
                    value={this.state.numeBanca}
                    onChange={(e) => this.onChangeCont(e)}
                  >
                    {conturiComponent}
                  </Form.Control>
                </Form.Group>
              </Col> */}

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
