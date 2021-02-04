import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Eye, RotateCw } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getAngajatSel } from '../Resources/angajatsel';
import { luni, formatDate } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { countWeekendDays } from '../Resources/cm';

class ConcediiOdihnaView extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.clearCO = this.clearCO.bind(this);
    this.viewCO = this.viewCO.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.onChangePanala = this.onChangePanala.bind(this);
    this.setNrZile = this.setNrZile.bind(this);

    this.state = {
      angajat: getAngajatSel(),

      id: 0,
      today: '',
      an: '',
      luna: { nume: '-', nr: '-' },

      ultimul_an: '',
      ani_cu_concediu: [],
      luni_cu_concediu: { '': [] }, // map months to years -> {an_cu_concediu: [...luni_cu_concediu]}
      nr_zile: 0,
      nr_zile_weekend: 0,

      co: [],
      coComponent: null,

      // add modal:
      show: false,
      isEdit: false,
      // detalii co
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',

      // succes modal:
      show_confirm: false,
      modalTitle: '',
      modalMessage: '',

      showToast: false,
      toastMessage: '',
    };
  }
  clearCO() {
    this.setState({
      // add modal:
      show: false,
      isEdit: false,
      id: 0,
      dela: '',
      panala: '',
      nr_zile: 0,
      nr_zile_weekend: 0,
      tip: 'Concediu de odihnă',
    });
  }

  async updateAngajatSel() {
    let angajatSel = getAngajatSel();

    if (angajatSel) {
      let angajat = await axios
        .get(`${server.address}/angajat/${angajatSel.idpersoana}`, { headers: authHeader() })
        .then((res) => (res.status === 200 ? res.data : null))
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua angajatul ' + err.response.data.message,
          })
        );
      // angajat = {idpersoana, idsocietate, idcontract, idsuperior}
      if (angajat) {
        this.setState(
          { angajat: { ...angajat, numeintreg: getAngajatSel().numeintreg } },
          this.fillTable
        );
      }
    } else {
      this.setState({ angajat: null }, this.fillTable);
    }
  }

  componentDidMount() {
    this.setCurrentYear();
    this.updateAngajatSel();
  }

  setCurrentYear() {
    let today = new Date();

    this.setState({
      an: today.getFullYear(),
      today: today.toISOString().substring(0, 10),
    });
  }

  // function to create react table rows with fetched data
  renderCO() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      coComponent: this.state.co.map((co, index) => {
        if (
          co.dela
            ? co.dela.includes(this.state.an) &&
              (this.state.luna.nume !== '-'
                ? Number(co.dela.substring(5, 7)) === this.state.luna.nr
                : true)
            : true
        ) {
          for (let key in co) {
            if (!co[key]) co[key] = '-';
          }

          return (
            <tr key={co.id}>
              <th>{formatDate(co.dela)}</th>
              <th>{formatDate(co.panala)}</th>
              <th>{co.tip}</th>
              <th className="d-inline-flex flex-row justify-content-around">
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  onClick={() => this.viewCO(co)}
                >
                  <Eye size={20} />
                </Button>
              </th>
            </tr>
          );
        }
      }),
    });
  }

  onChangeAn(an) {
    this.setState({ an: an, luna: { nume: '-', nr: '-' } }, this.renderCO);
  }

  onChangeMonth(e) {
    const selectedIndex = e.target.options.selectedIndex;
    this.setState(
      {
        luna: {
          nume: e.target.value,
          nr: Number(e.target.options[selectedIndex].getAttribute('data-key')),
        },
      },
      () => {
        this.renderCO();
        console.log(this.state.luna);
      }
    );
  }

  onChangeDela(dela) {
    if (!this.state.dela || dela > this.state.panala)
      this.setState({ dela: dela, panala: dela }, this.setNrZile);
    else this.setState({ dela: dela }, this.setNrZile);
  }
  onChangePanala(panala) {
    this.setState({ panala: panala }, this.setNrZile);
    // calculate number of days between dates
  }

  setNrZile() {
    var nr_zile = 0,
      nr_zile_weekend = 0;
    if (this.state.dela && this.state.panala) {
      let date1 = new Date(this.state.dela);
      let date2 = new Date(this.state.panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
      nr_zile_weekend = countWeekendDays(date1, date2);
    }
    this.setState({ nr_zile: nr_zile, nr_zile_weekend: nr_zile_weekend });
  }

  async fillTable() {
    if (!this.state.angajat) {
      this.setState({ co: [] }, this.renderCO);
      return;
    }
    if (!this.state.angajat.idcontract) {
      this.setState({ co: [] }, this.renderCO);
      return;
    }

    const concedii = await axios
      .get(`${server.address}/co/idc=${this.state.angajat.idcontract}`, { headers: authHeader() })
      // eslint-disable-next-line eqeqeq
      .then((res) => (res.status == 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua concediile de odihnă ' + err.response.data.message,
        })
      );

    if (concedii) {
      var ani_cu_concediu = new Set();
      var luni_cu_concediu = {};
      var an;
      // add ani_cu_concediu
      for (let c of concedii) {
        if (c.dela) {
          // an = c.dela.substring(0, 4);
          ani_cu_concediu.add(Number(c.dela.substring(0, 4)));
        }
        if (c.panala) {
          ani_cu_concediu.add(Number(c.panala.substring(0, 4)));
        }
      }
      // add ani in luni_cu_concediu
      for (let _an of ani_cu_concediu) {
        luni_cu_concediu[_an] = new Set();
      }
      // add luni in luni_cu_concediu
      for (let c of concedii) {
        if (c.dela) {
          an = c.dela.substring(0, 4);
          luni_cu_concediu[an].add(Number(c.dela.substring(5, 7)));
        }
      }
      // convert to array from set
      for (let _an of ani_cu_concediu) {
        luni_cu_concediu[_an] = [...luni_cu_concediu[_an]];
      }

      let thisYear = new Date().getFullYear();
      ani_cu_concediu.add(thisYear);

      this.setState(
        {
          co: concedii,
          ani_cu_concediu: [...ani_cu_concediu],
          luni_cu_concediu: luni_cu_concediu,
        },
        this.renderCO
      );
    } else {
      this.setState(
        {
          co: [],
          ani_cu_concediu: [],
          luni_cu_concediu: { '': [] },
        },
        this.renderCO
      );
    }
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState(
        {
          show_confirm: false,
          modalTitle: '',
          modalMessage: '',
        },
        this.props.scrollToTopSmooth
      );
    else
      this.setState({
        show: false,
        isEdit: false,
        // reset data
        dela: '',
        panala: '',
        tip: 'Concediu de odihnă',
      });
  }

  viewCO(co) {
    if (!this.state.angajat.idcontract) {
      this.setState({
        show_confirm: true,
        modalTitle: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }

    for (let key in co) if (co[key] === '-') co[key] = '';

    this.setState(
      {
        id: co.id,
        dela: co.dela.substring(0, 10),
        panala: co.panala.substring(0, 10),
        tip: co.tip,

        isEdit: true,
        show: true,
      },
      this.setNrZile
    );
  }

  render() {
    var monthsComponent = [];
    if (this.state.luni_cu_concediu[this.state.an]) {
      monthsComponent = this.state.luni_cu_concediu[this.state.an].map((luna, index) => (
        <option key={index} data-key={Number(luna)}>
          {luni[luna - 1]}
        </option>
      ));
    }

    const yearsComponent = this.state.ani_cu_concediu.map((an, index) => (
      <option key={index}>{an}</option>
    ));

    let angajatContract = this.state.angajat && this.state.angajat.idcontract;

    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {/* // CO MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Concediu de odihnă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
                  disabled
                  required
                  type="date"
                  value={this.state.dela}
                  max={this.state.panala === this.state.dela ? null : this.state.panala}
                />
              </Form.Group>

              <Form.Group id="panala">
                <Form.Label>Până la (inclusiv)</Form.Label>
                <Form.Control
                  disabled
                  required
                  type="date"
                  min={this.state.dela}
                  value={this.state.panala}
                />
              </Form.Group>

              <Form.Group id="tip">
                <Form.Label>Tip</Form.Label>
                <Form.Control disabled required type="text" value={this.state.tip} />
              </Form.Group>

              <Form.Group>
                <Form.Label>
                  {this.state.nr_zile +
                    (this.state.nr_zile > 1
                      ? ` zile concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`
                      : ` zi concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`)}
                </Form.Label>
              </Form.Group>
            </Form>
          </Modal.Body>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Concedii de odihnă</Card.Title>
                <Button
                  variant={angajatContract ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!angajatContract}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>
                <Row>
                  <Form.Group as={Col} sm="auto" className="mt-3">
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.an}
                      onChange={(e) => this.onChangeAn(e.target.value)}
                    >
                      <option>-</option>
                      {yearsComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group as={Col} sm="auto" className="mt-3">
                    <Form.Label>Luna</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.luna.nume}
                      onChange={(e) => this.onChangeMonth(e)}
                    >
                      <option>-</option>
                      {monthsComponent}
                    </Form.Control>
                  </Form.Group>
                </Row>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Începând cu (inclusiv)</th>
                      <th>Până la (inclusiv)</th>
                      <th>Tip</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.coComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default ConcediiOdihnaView;
