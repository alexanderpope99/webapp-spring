import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Edit3, Plus, RotateCw, Trash2 } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel } from '../Resources/angajatsel';
import { luni } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class BazaCalcul extends React.Component {
  constructor() {
    super();

    this.fillTable = this.fillTable.bind(this);
    this.addBazaCalcul = this.addBazaCalcul.bind(this);
    this.updateBazaCalcul = this.updateBazaCalcul.bind(this);
    this.editBazaCalcul = this.editBazaCalcul.bind(this);
    this.deleteBazaCalcul = this.deleteBazaCalcul.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
    this.renderBazaCalcul = this.renderBazaCalcul.bind(this);
    this.openModalAdd = this.openModalAdd.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajat: getAngajatSel(),
      bazacalcul: [],
      bazacalculComponent: null,

      an_sel: '-',
      ani_cu_baza: [],
      luni_cu_baza: { '-': [] }, // {an: [...luni]}
      luni_fara_baza: { '-': [] }, // {an: [...luni]}

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      show: false,
      id: null,
      idangajat: null,
      an: '',
      luna: { nume: '-', nr: '-' },
      salariurealizat: '',
      zilelucrate: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';
    this.setState({ an_sel: new Date().getFullYear() });
    this.fillTable();
    window.scrollTo(0, 0);
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
      if (angajat)
        this.setState(
          { angajat: { ...angajat, numeintreg: getAngajatSel().numeintreg } },
          this.fillTable
        );
    } else {
      this.setState({ angajat: null }, this.fillTable);
    }
  }

  async addBazaCalcul() {
    const bazacalcul_body = {
      idangajat: this.state.angajat.idpersoana,
      an: this.state.an || null,
      luna: this.state.luna.nr || null,
      salariurealizat: this.state.salariurealizat || null,
      zilelucrate: this.state.zilelucrate || null,
    };

    let ok = await axios
      .post(`${server.address}/bazacalcul`, bazacalcul_body, { headers: authHeader() })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga bază calcul ' + err.response.data.message,
        })
      );

    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Bază calcul adaugată pentru ' + this.state.angajat.numeintreg,
        },
        this.fillTable
      );
    }
  }

  async updateBazaCalcul(idbazacalcul) {
    const bazacalcul_body = {
      idangajat: this.state.angajat.idpersoana,
      an: this.state.an || null,
      luna: this.state.luna.nr || null,
      salariurealizat: this.state.salariurealizat || null,
      zilelucrate: this.state.zilelucrate || null,
    };

    const ok = await axios
      .put(`${server.address}/bazacalcul/${idbazacalcul}`, bazacalcul_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza baza calcul ' + err.response.data.message,
        })
      );

    if (ok) {
      this.fillTable();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Bază calcul actualizată',
      });
    }
  }

  async editBazaCalcul(bc) {
    this.setState({
      isEdit: true,
      show: true,

      id: bc.id,
      idangajat: bc.idangajat,
      an: bc.an,
      luna: { nume: luni[bc.luna - 1], nr: bc.luna },
      salariurealizat: bc.salariurealizat,
      zilelucrate: bc.zilelucrate,
    });
  }

  async deleteBazaCalcul(idbazacalcul) {
    axios
      .delete(`${server.address}/bazacalcul/${idbazacalcul}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge baza calcul ' + err.response.data.message,
        })
      );
  }

  // function to create react component with fetched data
  renderBazaCalcul() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      bazacalculComponent: this.state.bazacalcul.map((bc, index) => {
        // eslint-disable-next-line eqeqeq
        if (bc.an == this.state.an_sel || this.state.an_sel === '-')
          return (
            <tr key={bc.id}>
              <th>{bc.an}</th>
              <th>{luni[bc.luna - 1]}</th>
              <th>{bc.zilelucrate}</th>
              <th>{bc.salariurealizat}</th>
              <th>
                <Row>
                  <Button
                    onClick={() => this.editBazaCalcul(bc)}
                    variant="outline-secondary"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <Edit3 size={20} />
                  </Button>

                  <PopupState variant="popover" popupId="demo-popup-popover">
                    {(popupState) => (
                      <div>
                        <Button
                          variant="outline-secondary"
                          className="m-1 p-1 rounded-circle border-0"
                          {...bindTrigger(popupState)}
                        >
                          <Trash2 size={20} />
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
                            <Typography>Confirmare ștergere</Typography>
                            <Typography variant="caption">
                              Datele nu mai pot fi recuperate
                            </Typography>
                            <br />
                            <Button
                              variant="outline-danger"
                              onClick={() => {
                                popupState.close();
                                this.deleteBazaCalcul(bc.id);
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
                </Row>
              </th>
            </tr>
          );
      }),
    });
  }

  async fillTable() {
    if (this.state.angajat) {
      const bazacalcul = await axios
        .get(`${server.address}/bazacalcul/ida=${this.state.angajat.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut prelua baza calcul ' + err.response.data.message,
          })
        );
      if (bazacalcul) {
        const luni_nr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        var ani_cu_baza = new Set();
        var luni_cu_baza = { '-': luni_nr };
        var luni_fara_baza = { '-': luni_nr };
        for (let b of bazacalcul) {
          ani_cu_baza.add(Number(b.an));
          luni_cu_baza[b.an] = [];
          luni_fara_baza[b.an] = [];
        }
        for (let b of bazacalcul) {
          luni_cu_baza[b.an].push(b.luna);
        }
        for (let an of ani_cu_baza) {
          luni_fara_baza[an] = luni_nr.filter((month) => luni_cu_baza[an].indexOf(month) < 0);
        }

        this.setState(
          {
            bazacalcul: bazacalcul,
            ani_cu_baza: [...ani_cu_baza],
            luni_cu_baza: luni_cu_baza,
            luni_fara_baza: luni_fara_baza,
          },
          this.renderBazaCalcul
        );
      }
    } else {
      this.setState({
        bazacalcul: [],
        bazacalculComponent: null,
        ani_cu_baza: [],
        luni_cu_baza: { '-': [] },
      });
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateBazaCalcul(this.state.id);
    else this.addBazaCalcul();
  }

  openModalAdd() {
    this.setState({
      isEdit: false,
      show: true,
      an: this.state.an_sel,
    });
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      nume: '',
      prenume: '',
      cnp: '',
      datanasterii: '',
      grad: '',
      gradinvaliditate: 'valid',
      intretinut: false,
      coasigurat: false,
      idangajat: null,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
      an: '-',
      luna: { nume: '-', nr: '-' },
      salariurealizat: '',
      zilelucrate: '',
    });
  }

  onChangeMonth(e) {
    const selectedIndex = e.target.options.selectedIndex;
    this.setState({
      luna: {
        nume: e.target.value,
        nr: Number(e.target.options[selectedIndex].getAttribute('data-key')),
      },
    });
  }

  render() {
    const yearsComponent = this.state.ani_cu_baza.map((an, index) => (
      <option key={index}>{an}</option>
    ));

    var luniFaraBazaComponent = null;
    if (this.state.luni_fara_baza && this.state.luni_fara_baza[this.state.an_sel])
      luniFaraBazaComponent = this.state.luni_fara_baza[this.state.an_sel].map((luna) => (
        <option key={luna} data-key={luna}>
          {luni[luna - 1]}
        </option>
      ));

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
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Bază calcul</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addBazaCalcul}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      disabled={this.state.an_sel !== '-'}
                      type="number"
                      value={this.state.an}
                      onChange={(e) => this.setState({ an: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Luna</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.luna.nume}
                      onChange={(e) => this.onChangeMonth(e)}
                    >
                      <option>-</option>
                      {this.state.isEdit
                        ? [
                            ...luniFaraBazaComponent,
                            <option key={this.state.luna.nr} data-key={this.state.luna.nr}>
                              {this.state.luna.nume}
                            </option>,
                          ]
                        : luniFaraBazaComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Zile Lucrate</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.zilelucrate}
                      onChange={(e) => this.setState({ zilelucrate: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Salariu realizat</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.salariurealizat}
                      onChange={(e) => this.setState({ salariurealizat: e.target.value })}
                    />
                  </Form.Group>
                </Col>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.onSubmit} type="submit">
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* confirm modal */}
        <Modal show={this.state.showConfirm} onHide={this.handleCloseConfirm}>
          <Modal.Header closeButton>
            <Modal.Title>Bază calcul</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleCloseConfirm}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Bază calcul</Card.Title>

                <Button
                  variant={this.state.angajat ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!this.state.angajat}
                  onClick={this.fillTable}
                >
                  <RotateCw className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  variant={this.state.angajat ? 'outline-primary' : 'outline-dark'}
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  disabled={!this.state.angajat}
                  onClick={this.openModalAdd}
                >
                  <Plus className="m-0 p-0" />
                </Button>
                <Row>
                  <Form.Group as={Col} sm="3" className="mt-3">
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.an_sel}
                      onChange={(e) =>
                        this.setState({ an_sel: e.target.value }, this.renderBazaCalcul)
                      }
                    >
                      <option>-</option>
                      {yearsComponent}
                    </Form.Control>
                  </Form.Group>
                </Row>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>An</th>
                      <th>Luna</th>
                      <th>Zile Lucrate</th>
                      <th>Salariu realizat</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.bazacalculComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default BazaCalcul;
