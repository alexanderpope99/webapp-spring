import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import months from '../Resources/months';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class COTabel extends React.Component {
  constructor(props) {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addCO = this.addCO.bind(this);
    this.deleteCO = this.deleteCO.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.onChangePanala = this.onChangePanala.bind(this);
    this.setNrZile = this.setNrZile.bind(this);

    this.state = {
      angajat: props.angajat,

      an: '',
      luna: { nume: '-', nr: '-' },

      ultimul_an: '',
      ani_cu_concediu: [],
      nr_zile: 0,

      co: [],
      coComponent: null,

      // add modal:
      show: false,
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }

  setAngajat(angajat) {
    this.setState(
      {
        angajat: angajat,
      },
      () => this.fillTable()
    );
  }

  componentDidMount() {
    this.setCurrentYear();
    this.fillTable();
  }

  setCurrentYear() {
    let today = new Date();
    let an = today.getFullYear();

    this.setState({
      an: an,
    });
  }

  onChangeAn(an) {
    this.setState({ an: an }, this.renderCO);
  }

  onChangeMonth(e) {
    this.setState({ luna: { nume: e.target.value, nr: e.target.options.selectedIndex } }, () => {
      this.renderCO();
      console.log(this.state.luna);
    });
  }

  onChangePanala(panala) {
    this.setState({ panala: panala }, this.setNrZile);
    // calculate number of days between dates
  }

  setNrZile() {
    const panala = this.state.panala;
    var nr_zile = 0;
    if (this.state.dela && this.state.panala) {
      let date1 = new Date(this.state.dela);
      let date2 = new Date(this.state.panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
      console.log(date1);
      console.log(date2);
      console.log(nr_zile);
    }
    this.setState({ panala: panala, nr_zile: nr_zile });
  }

  async fillTable() {
    if (typeof this.state.angajat === 'undefined') return;
    if (this.state.angajat.idcontract === null) {
      this.setState({ co: [] }, this.renderCO);

      return;
    }

    //? fetch must be with idcontract
    const co = await axios
      .get(`${server.address}/co/idc=${this.state.angajat.idcontract}`, { headers: authHeader() })
      // eslint-disable-next-line eqeqeq
      .then((co) => (co.status == 200 ? co.data : null))
      .catch((err) => console.error('err', err));

    if (co) {
      const ani_cu_concediu = new Set();
      for (let c of co) {
        if (c.dela) ani_cu_concediu.add(c.dela.substring(0, 4));
        if (c.panala) ani_cu_concediu.add(c.panala.substring(0, 4));
      }

      this.setState(
        {
          co: co,
          ani_cu_concediu: [...ani_cu_concediu],
        },
        this.renderCO
      );
    } else {
      this.setState(
        {
          co: [],
          ani_cu_concediu: [],
        },
        this.renderCO
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      show: false,
      dela: '',
      panala: '',
      nr_zile: 0,
      tip: 'Concediu de odihnă',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    });
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        // reset data
        dela: '',
        panala: '',
        tip: 'Concediu de odihnă',
      });
  }

  async deleteCO(id) {
    await axios
      .delete(`${server.address}/co/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  // TODO: adds, but modal doesnt change
  async addCO() {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    const co_body = {
      dela: this.state.dela,
      panala: this.state.panala,
      tip: this.state.tip,
      idcontract: this.state.angajat.idcontract,
      // in DB also has sporuripermanente
    };

    let ok = await axios
      .post(`${server.address}/co`, co_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) => console.error('err:', err));

    console.log(ok);
    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: this.state.tip + ' adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  // function to create react table rows with fetched data
  renderCO() {
    this.setState({
      coComponent: this.state.co.map((co, index) => {
        if (
          co.dela
            ? co.dela.includes(this.state.an) &&
              (this.state.luna.nume !== '-'
                ? // eslint-disable-next-line eqeqeq
                  co.dela.substring(5, 7) == this.state.luna.nr // "==" compares str to number
                : true)
            : true
        ) {
          for (let key in co) {
            if (!co[key]) co[key] = '-';
          }

          return (
            <tr key={co.id}>
              <th>{co.dela.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{co.panala.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{co.tip}</th>
              <th className="d-inline-flex flex-row justify-content-around">
                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
                        {...bindTrigger(popupState)}
                      >
                        <DeleteIcon fontSize="small" />
                      </Button>
                      <Popover
                        {...bindPopover(popupState)}
                        anchorOrigin={{
                          vertical: 'bottom',
                          horizontal: 'center',
                        }}
                        transformOrigin={{
                          vertical: 'top',
                          horizontal: 'center',
                        }}
                      >
                        <Box p={2}>
                          <Typography>Sigur ștergeți concediul?</Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteCO(co.id);
                            }}
                            className="mt-2 "
                          >
                            Da
                          </Button>
                          <Button
                            variant="outline-persondary"
                            onClick={popupState.close}
                            className="mt-2"
                          >
                            Nu
                          </Button>
                        </Box>
                      </Popover>
                    </div>
                  )}
                </PopupState>
              </th>
            </tr>
          );
        }
      }),
    });
  }

  render() {
    const monthsComponent = months.map((month, index) => <option key={index}>{month}</option>);

    const yearsComponent = this.state.ani_cu_concediu.map((an, index) => (
      <option key={index}>{an}</option>
    ));

    return (
      <Aux>
        {/* // ADD MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Concediu de odihnă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  value={this.state.dela}
                  onChange={(e) => {
                    this.setState({ dela: e.target.value, panala: e.target.value }, this.setNrZile);
                  }}
                />
              </Form.Group>

              <Form.Group id="panala">
                <Form.Label>Până la (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  min={this.state.dela}
                  value={this.state.panala}
                  onChange={(e) => this.onChangePanala(e.target.value)}
                />
              </Form.Group>

              <Form.Group id="tip">
                <Form.Label>Tip</Form.Label>
                <Form.Control
                  required
                  as="select"
                  value={this.state.tip}
                  onChange={(e) => {
                    this.setState({ tip: e.target.value });
                  }}
                >
                  <option>Concediu de odihnă</option>
                  <option>Concediu fără plată</option>
                  <option>Concediu pentru situații speciale</option>
                  <option>Concediu pentru studii</option>
                </Form.Control>
              </Form.Group>

              <Form.Group>
                <Form.Label>
                  {this.state.nr_zile === 0
                    ? ''
                    : this.state.nr_zile +
                      (this.state.nr_zile > 1
                        ? ' zile concediu (include weekend-uri și sărbători)'
                        : ' zi concediu (include weekend și sărbători)')}
                </Form.Label>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.addCO}>
              Adaugă
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CONFIRM Modal */}
        <Modal show={this.state.show_confirm} onHide={() => this.handleClose(true)}>
          <Modal.Header closeButton>
            <Modal.Title>Mesaj</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Concedii de odihnă</Card.Title>
                <Button
                  variant={
                    typeof this.state.angajat === 'undefined' ? 'outline-dark' : 'outline-primary'
                  }
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
                  disabled={typeof this.state.angajat === 'undefined'}
                >
                  Adaugă concediu
                </Button>

                <Button
                  variant={
                    typeof this.state.angajat === 'undefined' ? 'outline-dark' : 'outline-primary'
                  }
                  disabled={typeof this.state.angajat === 'undefined'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.fillTable}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>
                <Row>
                  <Form.Group as={Col} sm="3" className="mt-3">
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
                  <Form.Group as={Col} sm="3" className="mt-3">
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

export default COTabel;
