import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
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

class CMTabel extends React.Component {
  constructor(props) {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.addCM = this.addCM.bind(this);
    this.editCM = this.editCM.bind(this);
    this.updateCM = this.updateCM.bind(this);
    this.deleteCM = this.deleteCM.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.getBazaCalculCM = this.getBazaCalculCM.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);

    this.state = {
      angajat: props.angajat,

      an: '',
      luna: { nume: '-', nr: '-' },
      ani_cu_concediu: [],
      luni_cu_concediu: { '': [] },

      cm: [],
      cmComponent: null,

      // cm modal:
      show: false,
      isEdit: false,
      id: '',
      // cm modal fields
      today: '',
      dela: '',
      panala: '',
      nr_zile: 0,
      continuare: false,
      datainceput: '',
      serienrcertificat: '',
      dataeliberare: '',
      codurgenta: '',
      procent: 100,
      codboalainfcont: '',
      bazacalcul: '',
      bazacalculplafonata: '',
      zilebazacalcul: '',
      mediezilnica: '',
      zilefirma: '',
      indemnizatiefirma: '',
      zilefnuass: '',
      indemnizatiefnuass: '',
      locprescriere: '',
      nravizmedic: '',
      codboala: '',
      urgenta: false,
      conditii: '',
      idcontract: null,

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }

  clearCM() {
    this.setState({
      isEdit: false,
      // cm modal fields
      id: '',

      dela: '',
      panala: '',
      nr_zile: 0,
      continuare: false,
      datainceput: '',
      serienrcertificat: '',
      dataeliberare: '',
      codurgenta: '',
      procent: 100,
      codboalainfcont: '',
      bazacalcul: '',
      bazacalculplafonata: '',
      zilebazacalcul: '',
      mediezilnica: '',
      zilefirma: '',
      indemnizatiefirma: '',
      zilefnuass: '',
      indemnizatiefnuass: '',
      locprescriere: '',
      nravizmedic: '',
      codboala: '',
      urgenta: false,
      conditii: '',
      idcontract: null,
    });
  }

  setAngajat(angajat) {
    this.setState(
      {
        angajat: angajat,
      },
      () => this.fillTable()
    );
  }

  numberWithCommas(x) {
    if (!x) return 0;
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  componentDidMount() {
    this.setCurrentYear();
    this.fillTable();
  }

  setCurrentYear() {
    let today = new Date();

    this.setState({
      an: today.getFullYear(),
      today: today.toISOString().substring(0, 10),
    });
  }

  onChangeAn(an) {
    this.setState({ an: an, luna: { nume: '-', nr: '-' } }, this.renderCM);
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
        this.renderCM();
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
  }

  setNrZile() {
    const panala = this.state.panala;
    var nr_zile = 0;
    if (this.state.dela && this.state.panala) {
      let date1 = new Date(this.state.dela);
      let date2 = new Date(this.state.panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
    }
    this.setState({ panala: panala, nr_zile: nr_zile });
  }

  async fillTable() {
    if (!this.state.angajat) return;

    if (!this.state.angajat.idcontract) {
      this.setState({ cm: [] }, this.renderCM);
      return;
    }

    //? fetch must be with idcontract
    const cm = await axios
      .get(`${server.address}/cm/idc=${this.state.angajat.idcontract}`, { headers: authHeader() })
      // eslint-disable-next-line eqeqeq
      .then((cm) => (cm.status == 200 ? cm.data : null))
      .catch((err) => console.error('err', err));

    if (cm) {
      var ani_cu_concediu = new Set();
      var luni_cu_concediu = {};
      // add ani_cu_concediu
      for (let c of cm) {
        if (c.dela) {
          // an = c.dela.substring(0, 4);
          ani_cu_concediu.add(Number(c.dela.substring(0, 4)));
        }
        if (c.panala) {
          ani_cu_concediu.add(Number(c.panala.substring(0, 4)));
        }
      }
      // add ani in luni_cu_concediu
      for (let an of ani_cu_concediu) {
        luni_cu_concediu[an] = new Set();
      }
      // add luni in luni_cu_concediu
      let an;
      for (let c of cm) {
        if (c.dela) {
          an = c.dela.substring(0, 4);
          luni_cu_concediu[an].add(Number(c.dela.substring(5, 7)));
        }
      }
      // convert to array from set
      for (let an of ani_cu_concediu) {
        luni_cu_concediu[an] = [...luni_cu_concediu[an]];
      }

      this.setState(
        {
          cm: cm,
          ani_cu_concediu: [...ani_cu_concediu],
          luni_cu_concediu: luni_cu_concediu,
        },
        this.renderCM
      );
    } else {
      this.setState(
        {
          cm: [],
          ani_cu_concediu: [],
          luni_cu_concediu: { '': [] },
        },
        this.renderCM
      );
    }
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
        isEdit: false,
      });
  }

  async deleteCM(id) {
    await axios
      .delete(`${server.address}/cm/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async addCM() {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    let { angajat, cm, cmComponent, show, show_confirm, modalMessage, ...cm_body } = this.state;
    cm_body.idcontract = this.state.angajat.idcontract;

    let ok = await axios
      .post(`${server.address}/cm`, cm_body, { headers: authHeader() })
      .then((res) => {
        console.log(res.data);
        return res.status === 200;
      })
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Concediu medical adăugat cu succes 💾',
      });
      this.fillTable();
      this.clearCM();
    }
  }

  async updateCM() {
    let {
      angajat,
      cm,
      cmComponent,
      show,
      show_confirm,
      modalMessage,
      id,
      isEdit,
      ...cm_body
    } = this.state;
    cm_body.idcontract = this.state.angajat.idcontract;

    let ok = await axios
      .put(`${server.address}/cm/${this.state.id}`, cm_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Concediu medical actualizat ✔',
      });
      this.fillTable();
      this.clearCM();
    }
  }

  async editCM(cm) {
		if (!this.state.angajat) return;
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }

    for (let key in cm) if (cm[key] === '-') cm[key] = '';

    this.setState({
      id: cm.id,
      dela: cm.dela.substring(0, 10),
      panala: cm.panala.substring(0, 10),
      continuare: cm.continuare,
      datainceput: cm.datainceput.substring(0, 10),
      serienrcertificat: cm.serienrcertificat,
      dataeliberare: cm.dataeliberare.substring(0, 10),
      codurgenta: cm.codurgenta,
      procent: cm.procent,
      codboalainfcont: cm.codboalainfcont,
      bazacalcul: cm.bazacalcul,
      bazacalculplafonata: cm.bazacalculplafonata,
      zilebazacalcul: cm.zilebazacalcul,
      mediezilnica: cm.mediezilnica,
      zilefirma: cm.zilefirma,
      indemnizatiefirma: cm.indemnizatiefirma,
      zilefnuass: cm.zilefnuass,
      indemnizatiefnuass: cm.indemnizatiefnuass,
      locprescriere: cm.locprescriere,
      nravizmedic: cm.nravizmedic,
      codboala: cm.codboala,
      urgenta: cm.urgenta,
      conditii: cm.conditii,
      idcontract: cm.idcontract,

      isEdit: true,
      show: true,
    });
  }

  // function to create react component with fetched data
  renderCM() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      cmComponent: this.state.cm.map((cm, index) => {
        if (
          cm.dela
            ? cm.dela.includes(this.state.an) &&
              (this.state.luna.nume !== '-'
                ? // eslint-disable-next-line eqeqeq
                  cm.dela.substring(5, 7) == this.state.luna.nr
                : true)
            : true
        ) {
          for (let key in cm) {
            if (!cm[key]) cm[key] = '-';
          }
          return (
            <tr key={cm.id}>
              <th className="d-inline-flex flex-row justify-content-around">
                <Button
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                  onClick={() => this.editCM(cm)}
                >
                  <Edit fontSize="small" />
                </Button>
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
                              this.deleteCM(cm.id);
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
              <th>{cm.dela.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{cm.panala.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{cm.continuare ? 'Da' : 'Nu'}</th>
              <th>{cm.datainceput.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{cm.serienrcertificat}</th>
              <th>{cm.dataeliberare.substring(0, 10).split('-').reverse().join('.')}</th>
              <th>{cm.codurgenta}</th>
              <th>{cm.codboalainfcont}</th>
              <th>{cm.procent}%</th>
              <th>{cm.bazacalcul}</th>
              <th>{cm.bazacalculplafonata}</th>
              <th>{cm.zilebazacalcul}</th>
              <th>{cm.mediezilnica}</th>
              <th>{cm.zilefirma}</th>
              <th>{cm.indemnizatiefirma}</th>
              <th>{cm.zilefnuass}</th>
              <th>{cm.indemnizatiefnuass}</th>
              <th>{cm.locprescriere}</th>
              <th>{cm.nravizmedic}</th>
              <th>{cm.codboala}</th>
              <th>{cm.urgenta ? 'Da' : 'Nu'}</th>
              <th>{cm.conditii}</th>
            </tr>
          );
        }
      }),
    });
  }

  // uses [this.state.an, this.state.luna]
  async getBazaCalculCM() {
    if (typeof this.state.angajat === 'undefined' || !this.state.dela || !this.state.panala) {
      console.log('dela/panala neselectat');
      return;
    }
    // 2020-10

    let luna = this.state.dela.substring(5, 7);

    // get baza calcul + zile baza calcul + medie zilnica
    const baza_calcul = await axios
      .get(
        `${server.address}/bazacalcul/cm/${this.state.angajat.idpersoana}/mo=${luna}&y=${this.state.an}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (baza_calcul) {
      this.setState({
        bazacalcul: baza_calcul.bazacalcul,
        zilebazacalcul: baza_calcul.zilebazacalcul,
        mediezilnica: baza_calcul.mediezilnica,
      });
    }
  }

  render() {
    var monthsComponent = [];
    if (this.state.luni_cu_concediu[this.state.an]) {
      monthsComponent = this.state.luni_cu_concediu[this.state.an].map((luna, index) => (
        <option key={index} data-key={Number(luna)}>
          {months[luna - 1]}
        </option>
      ));
    }

    const yearsComponent = this.state.ani_cu_concediu.map((an, index) => (
      <option key={index}>{an}</option>
    ));

    return (
      <Aux>
        {/* C.M. MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Concediu medical</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
                  type="date"
									value={this.state.dela}
									max={this.state.panala}
                  onChange={(e) => this.onChangeDela(e.target.value)}
                />
              </Form.Group>
              <Form.Group id="panala">
                <Form.Label>Până la (inclusiv)</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.panala}
                  min={this.state.dela}
                  onChange={(e) => this.onChangePanala(e.target.value)}
                />
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
              <Row>
                <Col md={6}>
                  <Form.Group id="continuare">
                    <Form.Check
                      custom
                      type="checkbox"
                      id="continuareCheck"
                      label="Continuare"
                      checked={this.state.continuare}
                      value={this.state.continuare}
                      onChange={(e) => {
                        this.setState({ continuare: e.target.checked });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="panala">
                    <Form.Label>Dată început</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.datainceput}
                      disabled={!this.state.continuare}
                      onChange={(e) => {
                        this.setState({ datainceput: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
              </Row>
              <Form.Group id="serienrcertificat">
                <Form.Label>Serie și număr certificat</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.serienrcertificat}
                  onChange={(e) => {
                    this.setState({ serienrcertificat: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="dataeliberare">
                <Form.Label>Dată eliberare</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.dataeliberare}
                  onChange={(e) => {
                    this.setState({ dataeliberare: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codurgenta">
                <Form.Label>Cod urgență</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codurgenta}
                  onChange={(e) => {
                    this.setState({ codurgenta: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codboalainfcont">
                <Form.Label>Cod boală infecțioasă/contagioasă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codboalainfcont}
                  onChange={(e) => {
                    this.setState({ codboalainfcont: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="procent">
                <Form.Label>Procent %</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.procent}
                  onChange={(e) => {
                    this.setState({ procent: e.target.value });
                  }}
                />
              </Form.Group>
              <div className="border rounded p-3 pb-0 mb-1">
                <Form.Group id="bazacalcul">
                  <Form.Label>Bază calcul (RON)</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.numberWithCommas(this.state.bazacalcul)}
                    onChange={(e) => {
                      this.setState({ bazacalcul: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="bazacalculplafonata">
                  <Form.Label>Bază calcul plafonată (RON)</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.bazacalculplafonata}
                    onChange={(e) => {
                      this.setState({ bazacalculplafonata: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="zilebazacalcul">
                  <Form.Label>Zile bază calcul</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.zilebazacalcul}
                    onChange={(e) => {
                      this.setState({ zilebazacalcul: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="mediezilnica">
                  <Form.Label>Medie zilnică (RON)</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.mediezilnica}
                    onChange={(e) => {
                      this.setState({ mediezilnica: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group className="mb-0">
                  <Button onClick={this.getBazaCalculCM}>Calculează automat</Button>
                </Form.Group>
              </div>
              <Form.Group id="zilefirma">
                <Form.Label>Zile suportate de firmă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.zilefirma}
                  onChange={(e) => {
                    this.setState({ zilefirma: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="indemnizatiefirma">
                <Form.Label>Indemnizație firmă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.indemnizatiefirma}
                  onChange={(e) => {
                    this.setState({ indemnizatiefirma: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="zilefnuass">
                <Form.Label>Zile FNUASS</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.zilefnuass}
                  onChange={(e) => {
                    this.setState({ zilefnuass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="indemnizatiefnuass">
                <Form.Label>Indemnizație FNUASS</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.indemnizatiefnuass}
                  onChange={(e) => {
                    this.setState({ indemnizatiefnuass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="locprescriere">
                <Form.Label>Loc prescriere</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.locprescriere}
                  onChange={(e) => {
                    this.setState({ locprescriere: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="nravizmedic">
                <Form.Label>Nr. aviz medical</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.nravizmedic}
                  onChange={(e) => {
                    this.setState({ nravizmedic: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codboala">
                <Form.Label>Cod boală</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codboala}
                  onChange={(e) => {
                    this.setState({ codboala: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="urgenta">
                <Form.Check
                  custom
                  type="checkbox"
                  id="urgentaCheck"
                  label="Urgență"
                  checked={this.state.urgenta}
                  value={this.state.urgenta}
                  onChange={(e) => {
                    this.setState({ urgenta: e.target.checked });
                  }}
                />
              </Form.Group>
              <Form.Group id="conditii">
                <Form.Label>Condiții</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.conditii}
                  onChange={(e) => {
                    this.setState({ conditii: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.state.isEdit ? this.updateCM : this.addCM}>
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
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

        {/* PAGE CMNTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Concedii medicale</Card.Title>
                <Button
                  variant={
                    typeof this.state.angajat === 'undefined' ? 'outline-dark' : 'outline-primary'
                  }
                  className="float-right"
                  onClick={() =>
                    this.setState(
                      { show: true, dela: this.state.today, panala: this.state.today },
                      this.setNrZile
                    )
                  }
                  disabled={!this.state.angajat}
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
                      <th></th>
                      <th>Începând cu (inclusiv)</th>
                      <th>Până la (inclusiv)</th>
                      <th>Continuare</th>
                      <th>Data început</th>
                      <th>Serie si nr. certificat</th>
                      <th>Data eliberare</th>
                      <th>Cod urgență</th>
                      <th>Cod boală infecțioasă</th>
                      <th>Procent</th>
                      <th>Bază calcul</th>
                      <th>Bază calcul plafonată</th>
                      <th>Zile bază calul</th>
                      <th>Medie zilnică</th>
                      <th>Zile firmă</th>
                      <th>Indemnizație firmă</th>
                      <th>Zile FNUASS</th>
                      <th>Indemnizație FNUASS</th>
                      <th>Nr. aviz medic</th>
                      <th>Loc prescriere</th>
                      <th>Cod boală</th>
                      <th>Cod boală infecțioasă/contagioasă</th>
                      <th>Urgență</th>
                      <th>Condiții</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.cmComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default CMTabel;
