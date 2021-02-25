import React from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';
import { Edit3, RotateCw, Trash2 } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../../../../hoc/_Aux';
import { server } from '../../../../Resources/server-address';
import { getAngajatSel } from '../../../../Resources/angajatsel';
import { luni, formatDate } from '../../../../Resources/calendar';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';
import {
  cod_boala,
  cod_urgenta,
  cod_boala_infect,
  getProcente,
  getZileFirma,
  countWeekendDays,
} from '../../../../Resources/cm.js';

class CMTabel extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.addCM = this.addCM.bind(this);
    this.editCM = this.editCM.bind(this);
    this.updateCM = this.updateCM.bind(this);
    this.deleteCM = this.deleteCM.bind(this);
    this.onChangeAn = this.onChangeAn.bind(this);
    this.onChangeMonth = this.onChangeMonth.bind(this);
    this.onChangeCodboala = this.onChangeCodboala.bind(this);
    this.onChangeProcent = this.onChangeProcent.bind(this);
    this.getBazaCalculCM = this.getBazaCalculCM.bind(this);

    this.state = {
      angajat: getAngajatSel(),

      an: '',
      luna: { nume: '-', nr: '-' },
      ani_cu_concediu: [],
      luni_cu_concediu: { '': [] },
      sarbatori: [],

      cm: [],
      cmComponent: null,

      // cm modal:
      show: false,
      isEdit: false,
      id: '',
      validated: true,
      // cm modal fields
      today: '',
      dela: '',
      panala: '',
      nr_zile: 0,
      nr_zile_weekend: 0,
      continuare: false,
      datainceput: '',
      serie: '',
      nr: '',
      dataeliberare: '',
      codurgenta: '',
      codboala: '01-Boala obisnuita si accidente in afara muncii',
      procent: '75',
      procente: ['75'],
      codboalainfcont: '',
      bazacalcul: 0,
      bazacalculplafonata: 0,
      zilebazacalcul: 0,
      mediezilnica: 0,
      zilefirma: 0,
      zilefnuass: 0,
      zilefaambp: 0,
      codindemnizatie: '',
      indemnizatiefirma: 0,
      indemnizatiefnuass: 0,
      indemnizatiefaambp: 0,
      locprescriere: 'Medic de familie',
      nravizmedic: '',
      urgenta: false,
      conditii: '',
      cnpcopil: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',

      showToast: false,
      toastMessage: '',
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
      serie: '',
      nr: '',
      dataeliberare: '',
      codurgenta: '',
      codboala: '01-Boala obisnuita si accidente in afara muncii',
      procent: '75',
      procente: ['75'],
      codboalainfcont: '',
      bazacalcul: 0,
      bazacalculplafonata: 0,
      zilebazacalcul: 0,
      mediezilnica: 0,
      zilefirma: 0,
      zilefnuass: 0,
      zilefaambp: 0,
      codindemnizatie: '',
      indemnizatiefirma: 0,
      indemnizatiefnuass: 0,
      locprescriere: 'Medic de familie',
      nravizmedic: '',
      urgenta: false,
      conditii: '',
      cnpcopil: '',
    });
  }

  setCurrentYear() {
    let today = new Date();

    this.setState({
      an: today.getFullYear(),
      today: today.toISOString().substring(0, 10),
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
            toastMessage: 'Nu am putut prelua angajatul: ' + err.response.data.message,
          })
        );
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

  async getSarbatori() {
    const sarbatori = await axios
      .get(`${server.address}/sarbatori`, { headers: authHeader() })
      .then((res) => (res.status !== 200 ? null : res.data))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua sărbătorile: ' + err.response.data.message,
        })
      );
    if(sarbatori) this.setState({sarbatori: sarbatori});
  }

  componentDidMount() {
    this.setCurrentYear();
    this.updateAngajatSel();
    this.getSarbatori();
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
      this.setState({ dela: dela, panala: dela, validated: true }, this.setNrZile);
    else this.setState({ dela: dela, validated: true }, this.setNrZile);
  }
  onChangePanala(panala) {
    this.setState({ panala: panala, validated: true }, this.setNrZile);
  }

  setNrZile() {
    // Math.round(this.state.zilefirma * baza_calcul.mediezilnica)
    let panala = this.state.panala;
    let dela = this.state.dela;
    var nr_zile = 0,
      nr_zile_weekend = 0,
      zilefirma = 0,
      zilefnuass = 0,
      zilefaambp = 0,
      indemnizatiefirma = 0,
      indemnizatiefnuass = 0,
      indemnizatiefaambp = 0;
    if (dela && panala) {
      let date1 = new Date(dela);
      let date2 = new Date(panala);
      nr_zile = (date2.getTime() - date1.getTime()) / (1000 * 3600 * 24) + 1;
      nr_zile_weekend = countWeekendDays(date1, date2);
      [zilefirma, zilefnuass, zilefaambp] = getZileFirma(
        date1,
        date2,
        this.state.codboala.substring(0, 2),
        this.state.sarbatori,
      );
      if (this.state.mediezilnica) {
        indemnizatiefirma = Math.round(
          (zilefirma * this.state.mediezilnica * this.state.procent) / 100
        );
        indemnizatiefnuass = Math.round(
          (zilefnuass * this.state.mediezilnica * this.state.procent) / 100
        );
        indemnizatiefaambp = Math.round(
          (zilefaambp * this.state.mediezilnica * this.state.procent) / 100
        );
      }
    }

    this.setState({
      nr_zile: nr_zile,
      nr_zile_weekend: nr_zile_weekend,
      zilefirma: zilefirma,
      zilefnuass: zilefnuass,
      zilefaambp: zilefaambp,
      indemnizatiefirma: indemnizatiefirma,
      indemnizatiefnuass: indemnizatiefnuass,
      indemnizatiefaambp: indemnizatiefaambp,
    });
  }

  onChangeCodboala(cod) {
    let procente = getProcente(cod);
    this.setState(
      {
        codboala: cod,
        procente: procente,
        procent: procente[0],
      },
      this.setNrZile
    );
  }

  onChangeProcent(proc) {
    this.setState(
      {
        procent: proc,
      },
      this.setNrZile
    );
  }

  async fillTable() {
    if (!this.state.angajat) {
      this.setState({ cm: [] }, this.renderCM);
      return;
    }

    if (!this.state.angajat.idcontract) {
      this.setState({ cm: [] }, this.renderCM);
      return;
    }

    const cm = await axios
      .get(`${server.address}/cm/idc=${this.state.angajat.idcontract}`, { headers: authHeader() })
      // eslint-disable-next-line eqeqeq
      .then((res) => (res.status == 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelau concediile medicale: ' + err.response.data.message,
        })
      );
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
      for (let _an of ani_cu_concediu) {
        luni_cu_concediu[_an] = new Set();
      }
      // add luni in luni_cu_concediu
      let an;
      for (let c of cm) {
        if (c.dela) {
          an = Number(c.dela.substring(0, 4));
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

  correctNumber(numberStr) {
		if(!numberStr) return 0;
		if(typeof(numberStr) == 'number') return numberStr;
    return Number(numberStr.replace(/,/g, '.'));
  }

  async deleteCM(id) {
    await axios
      .delete(`${server.address}/cm/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge concediul medical: ' + err.response.data.message,
        })
      );
  }

  async addCM() {
    if (!this.state.angajat) return;
    if (!this.state.angajat.idcontract) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }

    const cm_body = {
      idcontract: this.state.angajat.idcontract,
      dela: this.state.dela,
      panala: this.state.panala,
      continuare: this.state.continuare || false,
      datainceput: this.state.datainceput,
      serie: this.state.serie,
      nr: this.state.nr,
      cnpcopil: this.state.cnpcopil,
      dataeliberare: this.state.dataeliberare,
      codurgenta: this.state.codurgenta,
      procent: this.state.procent,
      codboalainfcont: this.state.codboalainfcont,
      bazacalcul: this.correctNumber(this.state.bazacalcul) || 0,
      bazacalculplafonata: this.correctNumber(this.state.bazacalculplafonata) || 0,
      zilebazacalcul: this.state.zilebazacalcul || 0,
      mediezilnica: this.correctNumber(this.state.mediezilnica) || 0,
      zilefirma: this.state.zilefirma || 0,
      indemnizatiefirma: this.state.indemnizatiefirma || 0,
      zilefnuass: this.state.zilefnuass || 0,
      indemnizatiefnuass: this.state.indemnizatiefnuass || 0,
      zilefaambp: this.state.zilefaambp || 0,
      indemnizatiefaambp: this.state.indemnizatiefaambp || 0,
      locprescriere: this.state.locprescriere,
      nravizmedic: this.state.nravizmedic,
      codboala: this.state.codboala,
      urgenta: this.state.urgenta || false,
      conditii: this.state.conditii,
      codindemnizatie: this.state.codindemnizatie,
    };

    let ok = await axios
      .post(`${server.address}/cm`, cm_body, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga concediul: ' + err.response.data.message,
        })
      );

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Concediu medical adăugat  💾',
      });
      this.fillTable();
      this.clearCM();
    } else {
      this.setState({ validated: false });
    }
  }

  async updateCM() {
    const cm_body = {
      id: this.state.id,
      idcontract: this.state.angajat.idcontract,
      dela: this.state.dela,
      panala: this.state.panala,
      continuare: this.state.continuare || false,
      datainceput: this.state.datainceput,
      serie: this.state.serie,
      nr: this.state.nr,
      cnpcopil: this.state.cnpcopil,
      dataeliberare: this.state.dataeliberare,
      codurgenta: this.state.codurgenta,
      procent: this.state.procent,
      codboalainfcont: this.state.codboalainfcont,
      bazacalcul: this.correctNumber(this.state.bazacalcul) || 0,
      bazacalculplafonata: this.correctNumber(this.state.bazacalculplafonata) || 0,
      zilebazacalcul: this.state.zilebazacalcul || 0,
      mediezilnica: this.correctNumber(this.state.mediezilnica) || 0,
      zilefirma: this.state.zilefirma || 0,
      indemnizatiefirma: this.state.indemnizatiefirma || 0,
      zilefnuass: this.state.zilefnuass || 0,
      indemnizatiefnuass: this.state.indemnizatiefnuass || 0,
      zilefaambp: this.state.zilefaambp || 0,
      indemnizatiefaambp: this.state.indemnizatiefaambp || 0,
      locprescriere: this.state.locprescriere,
      nravizmedic: this.state.nravizmedic,
      codboala: this.state.codboala,
      urgenta: this.state.urgenta || false,
      conditii: this.state.conditii,
      codindemnizatie: this.state.codindemnizatie,
    };

    let ok = await axios
      .put(`${server.address}/cm/${this.state.id}`, cm_body, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut actualiza concediul: ' + err.response.data.message,
        })
      );

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
    } else {
      this.setState({ validated: false }, window.scrollTo(0, 0));
    }
  }

  async editCM(cm) {
    if (!this.state.angajat) return;
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        validated: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }

    for (let key in cm) if (cm[key] === '-') cm[key] = '';

    this.setState(
      {
        id: cm.id,
        dela: cm.dela.substring(0, 10),
        panala: cm.panala.substring(0, 10),
        continuare: cm.continuare || false,
        datainceput: cm.datainceput.substring(0, 10),
        serie: cm.serie,
        nr: cm.nr,
        codindemnizatie: cm.codindemnizatie,
        cnpcopil: cm.cnpcopil,
        dataeliberare: cm.dataeliberare.substring(0, 10),
        codurgenta: cm.codurgenta,
        procent: cm.procent,
        procente: getProcente(cm.codboala),
        codboalainfcont: cm.codboalainfcont,
        bazacalcul: cm.bazacalcul,
        bazacalculplafonata: cm.bazacalculplafonata,
        zilebazacalcul: cm.zilebazacalcul,
        mediezilnica: cm.mediezilnica,
        zilefirma: cm.zilefirma,
        indemnizatiefirma: cm.indemnizatiefirma,
        zilefnuass: cm.zilefnuass,
        indemnizatiefnuass: cm.indemnizatiefnuass,
        zilefaambp: cm.zilefaambp,
        indemnizatiefaambp: cm.indemnizatiefaambp,
        locprescriere: cm.locprescriere,
        nravizmedic: cm.nravizmedic,
        codboala: cm.codboala,
        urgenta: cm.urgenta || false,
        conditii: cm.conditii,

        isEdit: true,
        show: true,
      },
      this.setNrZile
    );
  }

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
        validated: true,
      });
    else
      this.setState({
        show: false,
        isEdit: false,
        validated: true,
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
                  <Edit3 size={20} />
                </Button>
                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
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
              <th>{formatDate(cm.dela)}</th>
              <th>{formatDate(cm.panala)}</th>
              <th>{cm.continuare === '-' ? 'Nu' : 'Da'}</th>
              <th>{formatDate(cm.datainceput)}</th>
              <th>{cm.serie}</th>
              <th>{cm.nr}</th>
              <th>{formatDate(cm.dataeliberare)}</th>
              <th>{cm.codboala}</th>
              <th>{cm.codurgenta}</th>
              <th>{cm.codboalainfcont}</th>
              <th>{cm.procent}%</th>
              <th>{cm.bazacalcul}</th>
              <th>{cm.bazacalculplafonata}</th>
              <th>{cm.zilebazacalcul}</th>
              <th>{cm.mediezilnica}</th>
              <th>{cm.codindemnizatie}</th>
              <th>{cm.zilefirma}</th>
              <th>{cm.indemnizatiefirma}</th>
              <th>{cm.zilefnuass}</th>
              <th>{cm.indemnizatiefnuass}</th>
              <th>{cm.zilefaambp}</th>
              <th>{cm.indemnizatiefaambp}</th>
              <th>{cm.nravizmedic}</th>
              <th>{cm.locprescriere}</th>
              <th>{cm.urgenta === '-' ? 'Nu' : 'Da'}</th>
              <th>{cm.conditii}</th>
              <th>{cm.cnpcopil}</th>
            </tr>
          );
        }
      }),
    });
  }

  // uses [this.state.an, this.state.luna]
  async getBazaCalculCM() {
    if (!this.state.angajat || !this.state.dela || !this.state.panala) {
      console.log('dela/panala neselectat');
      return;
    }

    let luna = this.state.dela.substring(5, 7);

    // get baza calcul + zile baza calcul + medie zilnica
    const baza_calcul = await axios
      .get(
        `${server.address}/bazacalcul/cm/${this.state.angajat.idpersoana}/mo=${luna}&y=${this.state.an}/${this.state.codboala.substring(0, 2)}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut prelua baza calcul, nr zile și media zilnică\n' +
            err.response.data.message,
        })
      );
    if (baza_calcul) {
      this.setState({
        bazacalcul: baza_calcul.bazacalcul,
        zilebazacalcul: baza_calcul.zilebazacalcul,
        mediezilnica: baza_calcul.mediezilnica.toFixed(4),
        indemnizatiefirma: Math.round(
          this.state.zilefirma *
            Number.parseFloat(baza_calcul.mediezilnica).toFixed(4) *
            (this.state.procent / 100)
        ),
        indemnizatiefnuass: Math.round(
          this.state.zilefnuass *
            Number.parseFloat(baza_calcul.mediezilnica).toFixed(4) *
            (this.state.procent / 100)
        ),
        indemnizatiefaambp: Math.round(
          this.state.zilefaambp *
            Number.parseFloat(baza_calcul.mediezilnica).toFixed(2) *
            (this.state.procent / 100)
        ),
      });
    }
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

    const yearsComponent = this.state.ani_cu_concediu
      .sort()
      .map((an, index) => <option key={index}>{an}</option>);

    const codBoalaComponent = cod_boala.map((cod, index) => <option key={index}>{cod}</option>);
    const codUrgentaComponent = cod_urgenta.map((cod, index) => <option key={index}>{cod}</option>);

    const codBoalaInfectComponent = cod_boala_infect.map((cod, index) => (
      <option key={index}>{cod}</option>
    ));
    const procentComponent = this.state.procente.map((p, index) => (
      <option key={index}>{p}</option>
    ));

    let angajatContract = this.state.angajat && this.state.angajat.idcontract;
    const concediuIsValid = this.state.dela && this.state.dela <= this.state.panala;

    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {/* C.M. MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Concediu medical</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Row>
                <Form.Group id="dela" as={Col} md="6">
                  <Form.Label>Începând cu (inclusiv)</Form.Label>
                  <Form.Control
                    required
                    type="date"
                    value={this.state.dela}
                    max={this.state.panala}
                    onChange={(e) => this.onChangeDela(e.target.value)}
                    className={this.state.validated ? 'form-control' : 'form-control is-invalid'}
                  />
                  <Form.Control.Feedback type="invalid">
                    Concediul se suprapune cu unul existent
                  </Form.Control.Feedback>
                </Form.Group>
                <Form.Group id="panala" as={Col} md="6">
                  <Form.Label>Până la (inclusiv)</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.panala}
                    min={this.state.dela}
                    onChange={(e) => this.onChangePanala(e.target.value)}
                  />
                </Form.Group>
                <Form.Group as={Col} md="12">
                  <Form.Label>
                    {this.state.nr_zile +
                      (this.state.nr_zile > 1
                        ? ` zile concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`
                        : ` zi concediu, din care ${this.state.nr_zile_weekend} in weekend (sărbători incluse)`)}
                  </Form.Label>
                </Form.Group>
                <Form.Group id="continuare" as={Col} md="2" className="mt-4">
                  <Form.Check
                    custom
                    type="switch"
                    id="continuareCheck"
                    label="Continuare"
                    checked={this.state.continuare}
                    onChange={(e) => {
                      this.setState({ continuare: e.target.checked });
                    }}
                  />
                </Form.Group>
                <Form.Group id="datainceput" as={Col} md="4">
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
                <Form.Group id="dataeliberare" as={Col} md="6">
                  <Form.Label>Dată eliberare</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.dataeliberare}
                    onChange={(e) => {
                      this.setState({ dataeliberare: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="seriecertificat" as={Col} md="6">
                  <Form.Label>Serie certificat</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.serie}
                    onChange={(e) => {
                      this.setState({ serie: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="nrcertificat" as={Col} md="6">
                  <Form.Label>Număr certificat</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nr}
                    onChange={(e) => {
                      this.setState({ nr: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="codboala" as={Col} md="6">
                  <Form.Label>Cod boală</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.codboala}
                    onChange={(e) => this.onChangeCodboala(e.target.value)}
                  >
                    {codBoalaComponent}
                  </Form.Control>
                </Form.Group>
                <Form.Group id="codurgenta" as={Col} md="6">
                  <Form.Label>Cod urgență</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.codurgenta}
                    onChange={(e) => this.setState({ codurgenta: e.target.value })}
                  >
                    {codUrgentaComponent}
                  </Form.Control>
                </Form.Group>
                <Form.Group id="codboalainfcont" as={Col} md="6">
                  <Form.Label>Cod boală infecțioasă/contagioasă</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.codboalainfcont}
                    onChange={(e) => {
                      this.setState({ codboalainfcont: e.target.value });
                    }}
                  >
                    {codBoalaInfectComponent}
                  </Form.Control>
                </Form.Group>
                <Form.Group id="procent" as={Col} md="6">
                  <Form.Label>Procent %</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.procent}
                    onChange={(e) => this.onChangeProcent(e.target.value)}
                  >
                    {procentComponent}
                  </Form.Control>
                </Form.Group>
                <Row className="border rounded pt-3 pb-3 m-3">
                  <Form.Group id="bazacalcul" as={Col} md="6">
                    <Form.Label>Bază calcul (RON)</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.bazacalcul}
                      onChange={(e) => {
                        this.setState({ bazacalcul: e.target.value });
                      }}
                    />
                  </Form.Group>
                  <Form.Group id="bazacalculplafonata" as={Col} md="6">
                    <Form.Label>Bază calcul plafonată (RON)</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.bazacalculplafonata}
                      onChange={(e) => {
                        this.setState({ bazacalculplafonata: e.target.value });
                      }}
                    />
                  </Form.Group>
                  <Form.Group id="zilebazacalcul" as={Col} md="6">
                    <Form.Label>Zile bază calcul</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.zilebazacalcul}
                      onChange={(e) => {
                        this.setState({ zilebazacalcul: e.target.value });
                      }}
                    />
                  </Form.Group>
                  <Form.Group id="mediezilnica" as={Col} md="6">
                    <Form.Label>Medie zilnică (RON)</Form.Label>
                    <Form.Control
                      type="number"
											step="0.0001"
                      value={this.state.mediezilnica}
                      onChange={(e) => {
                        this.setState({ mediezilnica: e.target.value });
                      }}
                    />
                  </Form.Group>
                  <Form.Group className="mb-0" as={Col} md="6">
                    <Button onClick={this.getBazaCalculCM}>Calculează automat</Button>
                  </Form.Group>
                </Row>
                <Form.Group id="zilefirma" as={Col} md="6">
                  <Form.Label>Zile suportate de firmă</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.zilefirma}
                    onChange={(e) => {
                      this.setState({ zilefirma: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefirma" as={Col} md="6">
                  <Form.Label>Indemnizație firmă</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.indemnizatiefirma}
                    onChange={(e) => {
                      this.setState({ indemnizatiefirma: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="zilefnuass" as={Col} md="6">
                  <Form.Label>Zile FNUASS</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.zilefnuass}
                    onChange={(e) => {
                      this.setState({ zilefnuass: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefnuass" as={Col} md="6">
                  <Form.Label>Indemnizație FNUASS</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.indemnizatiefnuass}
                    onChange={(e) => {
                      this.setState({ indemnizatiefnuass: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="zilefaambp" as={Col} md="6">
                  <Form.Label>Zile FAAMBP</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.zilefaambp}
                    onChange={(e) => {
                      this.setState({ zilefaambp: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="indemnizatiefaambp" as={Col} md="6">
                  <Form.Label>Indemnizație FAAMBP</Form.Label>
                  <Form.Control
                    type="number"
                    disabled
                    value={this.state.indemnizatiefaambp}
                    onChange={(e) => {
                      this.setState({ indemnizatiefaambp: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="codindemnizatie" as={Col} md="6">
                  <Form.Label>Cod indemnizație</Form.Label>
                  <Form.Control
                    type="text"
                    // disabled
                    value={this.state.codindemnizatie}
                    onChange={(e) => {
                      this.setState({ codindemnizatie: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="locprescriere" as={Col} md="6">
                  <Form.Label>Loc prescriere</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.locprescriere}
                    onChange={(e) => {
                      this.setState({ locprescriere: e.target.value });
                    }}
                  >
                    <option>Medic de familie</option>
                    <option>Spital</option>
                    <option>Ambulatoriu</option>
                    <option>CAS</option>
                  </Form.Control>
                </Form.Group>
                <Form.Group id="nravizmedic" as={Col} md="6">
                  <Form.Label>Nr. aviz medical</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.nravizmedic}
                    onChange={(e) => {
                      this.setState({ nravizmedic: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="urgenta" as={Col} md="6" className="mt-4">
                  <Form.Check
                    custom
                    type="switch"
                    id="urgentaCheck"
                    label="Urgență"
                    checked={this.state.urgenta}
                    onChange={(e) => {
                      this.setState({ urgenta: e.target.checked });
                    }}
                  />
                </Form.Group>
                <Form.Group id="conditii" as={Col} md="6">
                  <Form.Label>Condiții</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.conditii}
                    onChange={(e) => {
                      this.setState({ conditii: e.target.value });
                    }}
                  />
                </Form.Group>
                <Form.Group id="cnpcopil" as={Col} md="6">
                  <Form.Label>CNP Copil</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.cnpcopil}
                    onChange={(e) => {
                      this.setState({ cnpcopil: e.target.value });
                    }}
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.state.isEdit ? this.updateCM : this.addCM}
              disabled={!concediuIsValid}
            >
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
					<Link to="/forms/realizari-retineri">
              <Button variant="link">Către realizări/rețineri</Button>
            </Link>
            <Button variant="outline-info" onClick={this.handleClose}>
              Închide
            </Button>
          </Modal.Footer>
        </Modal>

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Concedii medicale</Card.Title>
                <Button
                  variant={angajatContract ? 'outline-primary' : 'outline-dark'}
                  className="float-right"
                  disabled={!angajatContract}
                  onClick={() =>
                    this.setState(
                      { show: true, dela: this.state.today, panala: this.state.today },
                      this.setNrZile
                    )
                  }
                >
                  Adaugă concediu
                </Button>

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
                      {/* <option>-</option> */}
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
                      <option>Toate</option>
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
                      <th>Serie certificat</th>
                      <th>Nr. certificat</th>
                      <th>Data eliberare</th>
                      <th>Cod boală</th>
                      <th>Cod urgență</th>
                      <th>Cod boală infecțioasă/contagioasă</th>
                      <th>Procent</th>
                      <th>Bază calcul</th>
                      <th>Bază calcul plafonată</th>
                      <th>Zile bază calul</th>
                      <th>Medie zilnică</th>
                      <th>Cod indemnizație</th>
                      <th>Zile firmă</th>
                      <th>Indemnizație firmă</th>
                      <th>Zile FNUASS</th>
                      <th>Indemnizație FNUASS</th>
                      <th>Zile FAAMBP</th>
                      <th>Indemnizație FAAMBP</th>
                      <th>Nr. aviz medic</th>
                      <th>Loc prescriere</th>
                      <th>Urgență</th>
                      <th>Condiții</th>
                      <th>CNP Copil</th>
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
