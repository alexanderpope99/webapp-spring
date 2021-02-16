import React from 'react';
import {
  Row,
  Col,
  Card,
  Form,
  Button,
  Modal,
  InputGroup,
  FormControl,
  OverlayTrigger,
  Tooltip,
  Table,
  Collapse,
  Toast,
  Dropdown,
  DropdownButton,
  Breadcrumb,
} from 'react-bootstrap';
import { Trash2, Info } from 'react-feather';

import Aux from '../../../../hoc/_Aux';
import Box from '@material-ui/core/Box';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import { luni } from '../../../Resources/calendar';
import { getSocSel } from '../../../Resources/socsel';
import { getAngajatSel, setAngajatSel } from '../../../Resources/angajatsel';
import { server } from '../../../Resources/server-address';
import { Typography } from '@material-ui/core';
import axios from 'axios';
import authHeader from '../../../../services/auth-header';
import authService from '../../../../services/auth.service';

class RealizariRetineri extends React.Component {
  constructor() {
    super();

    this.setCurrentYearMonth = this.setCurrentYearMonth.bind(this);
    this.numberWithCommas = this.numberWithCommas.bind(this);
    this.recalculeaza = this.recalculeaza.bind(this);
    this.veziOreSuplimentare = this.veziOreSuplimentare.bind(this);
    this.veziPensieFacultativa = this.veziPensieFacultativa.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.getOresuplimentare = this.getOresuplimentare.bind(this);
    this.addOrasuplimentara = this.addOrasuplimentara.bind(this);
    this.renderTabelore = this.renderTabelore.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.calcNrTichete = this.calcNrTichete.bind(this);
    this.getStatIndividual = this.getStatIndividual.bind(this);
    this.creeazaStateUltimele6Luni = this.creeazaStateUltimele6Luni.bind(this);
    this.recalcSocietate = this.recalcSocietate.bind(this);
    this.preiaCursCurent = this.preiaCursCurent.bind(this);

    this.state = {
      socsel: getSocSel(),
      // angajatsel: getAngajatSel(),
      detaliiAccordion: false,
      show: false,
      showPensie: false,
      modalMessage: '',
      showToast: false,
      toastMessage: '',
      toastTitle: '',
      toastColor: '',

      an: '',
      luna: '',

      luni: [],

      selected_angajat: getAngajatSel(),
      lista_angajati: [], // object: {nume, id}
      idcontract: '',
      idstat: '',

      // realizari
      an_inceput_contract: '',
      luna_inceput_contract: '',
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilec: '',
      zilecm: '',
      zilecmlucratoare: '',
      valcm: '',
      zileco: '',
      zilecolucratoare: '',
      zilecfp: '',
      zilecfplucratoare: '',
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: 100,
      totaloresuplimentare: 0,

      // retineri
      idretineri: null,
      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,
      totalpensiefacultativa: 0,
      impozitdedus: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',

      // detalii
      bazaimpozit: '',
      impozitscutit: '',
      salariurealizat: '',
      venitnet: '',
      zilelucrate: '',
      zileplatite: '',
    };
  }
  clearForm() {
    this.setState({
      idcontract: '',

      // realizari
      functie: '',
      duratazilucru: '',
      normalucru: '',
      salariubrut: '',
      orelucrate: '',
      nrtichete: '',
      zilec: '',
      zilecm: '',
      zilecmlucratoare: '',
      valcm: '',
      zileco: '',
      zilecolucratoare: '',
      zilecfp: '',
      zilecfplucratoare: '',
      zileinvoire: 0, // user input
      primabruta: 0, // user input
      zilelibere: 0, // user input
      salariupezi: 0,
      salariupeora: 0,

      oresuplimentare: [], // user input
      nrore: 0,
      procent: 100,
      totaloresuplimentare: 0,

      // retineri
      idretineri: null,
      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
      deducere: 0,
      nrpersoaneintretinere: 0,
      impozitdedus: 0,

      // total
      totaldrepturi: '',
      cas: '',
      cass: '',
      cam: '',
      valoaretichete: '',
      impozit: '',
      restplata: '',
      detalii: null,

      // detalii
      bazaimpozit: '',
      impozitscutit: '',
      salariurealizat: '',
      venitnet: '',
      zilelucrate: '',
      zileplatite: '',
    });
  }
  clearUserInput() {
    this.setState({
      oresuplimentare: [],
      zileinvoire: 0,
      primabruta: 0,
      zilelibere: 0,

      avansnet: 0,
      cursValutar: 0,
      pensiefacangajat: 0,
      pensiefacangajator: 0,
      pensiefacangajatretinuta: 0,
      pensiefacangajatordeductibila: 0,
      pensiefacexcedent: 0,
      pensiealimentara: 0,
      popriri: 0,
      imprumuturi: 0,
    });
  }

  async componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    await this.setCurrentYearMonth(); // modifies state.an, state.luna
    this.setPersoane(); // date personale, also fills lista_angajati
    this.fillForm();
  }

  numberWithCommas(x) {
    if (x) return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    else return 0;
  }

  async setCurrentYearMonth() {
    let today = new Date();
    let luna = luni[today.getMonth()];
    let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  async setPersoane() {
    //* only people with contract <- &c flag
    const persoane = await axios
      .get(`${server.address}/persoana/ids=${this.state.socsel.id}&c`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage:
            'Nu am putut prelua persoanele din baza de date: ' + err.response.data.message,
        })
      );
    if (!persoane) return;
    //* set lista_angajati
    let lista_angajati = [];
    for (let persoana of persoane) {
      lista_angajati.push({ nume: persoana.nume + ' ' + persoana.prenume, id: persoana.id });
    }
    this.setState({ lista_angajati: lista_angajati });
  }

  async getTotalPensie() {
    const totalpensiefacan = await axios
      .get(`${server.address}/retineri/${this.state.idcontract}/pensiefac/${this.state.an}`, {
        headers: authHeader(),
      })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) => console.error(err));

    this.setState({
      totalpensiefacultativa: totalpensiefacan,
    });
  }

  async fillForm() {
    this.clearUserInput();
    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;

    console.log(an, luna);

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat ? this.state.selected_angajat.idpersoana : null;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    // get contract by idpersoana :: contract needed for 4 fields
    const contract = await axios
      .get(`${server.address}/contract/idp=${idpersoana}`, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage:
            'Nu am putut prelua contractul pentru persoana: ' + err.response.data.message,
        })
      );
    console.log('contract:', contract);
    if (!contract) return;

    // if already calculated, gets existing data, if idstat does not exist for (idc, mo, y) => calc => saves to DB
    const data = await axios
      .post(
        `${server.address}/realizariretineri/save/idc=${contract.id}&mo=${luna}&y=${an}`,
        { body: null },
        {
          headers: authHeader(),
        }
      )
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage:
            'Nu am putut prelua sau calcula realizari retineri: ' + err.response.data.message,
        })
      );

    console.log('data:', data);
    if (!data) {
      this.setState({
        an_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(0, 4) : '',
        luna_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(5, 7) : '',
        functie: contract.functie || 0,
        duratazilucru: contract.normalucru || 0,
        salariubrut: contract.salariutarifar || 0,
        idcontract: contract.id || 0,
      });
      return;
    }

    // get ore suplimentare by idcontract, luna, an
    const oresuplimentare = await this.getOresuplimentare(contract.id, luna, an);
    let totaloresuplimentare = 0;
    if (oresuplimentare.length > 0) {
      for (let ora of oresuplimentare) totaloresuplimentare += ora.total;
    }

    const retineri = data.retineri;

    // set states with data
    this.setState(
      {
        //* realizari
        an_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(0, 4) : '',
        luna_inceput_contract: contract.dataincepere ? contract.dataincepere.substring(5, 7) : '',
        functie: contract.functie || 0,
        duratazilucru: contract.normalucru || 0,
        normalucru: data.norma || 0, // zile lucratoare in luna respectiva
        salariubrut: contract.salariutarifar || 0,
        orelucrate: data.orelucrate || 0,
        salariupezi: data.salariupezi || 0,
        salariupeora: data.salariupeora || 0,
        nrtichete: data.nrtichete || 0,
        zilecm: data.zilecm || 0,
        valcm: data.valcm || 0,
        zilecmlucratoare: data.zilecmlucratoare || 0,
        zileco: data.zileco || 0,
        zilecolucratoare: data.zilecolucratoare || 0,
        zilecfp: data.zilecfp || 0,
        zilecfplucratoare: data.zilecfplucratoare || 0,
        zilec: data.zilec || 0,

        //* retineri
        idretineri: retineri.id,
        avansnet: retineri.avansnet || 0,
        cursValutar: retineri.curseurron || 0,
        pensiefacangajat: retineri.pensiefacangajat || 0,
        pensiefacangajator: retineri.pensiefacangajator || 0,
        pensiefacangajatretinuta: retineri.pensiefacangajatretinuta || 0,
        pensiefacangajatordeductibila: retineri.pensiefacangajatordeductibila || 0,
        pensiefacexcedent: retineri.pensiefacexcedent || 0,
        pensiealimentara: retineri.pensiealimentara || 0,
        popriri: retineri.popriri || 0,
        imprumuturi: retineri.imprumuturi || 0,
        deducere: data.deducere || 0,
        nrpersoaneintretinere: data.nrpersoaneintretinere || 0,

        //* total
        totaldrepturi: data.totaldrepturi || 0,
        cas: data.cas || 0,
        cass: data.cass || 0,
        valoaretichete: data.valoaretichete || 0,
        impozit: data.impozit || 0,
        restplata: data.restplata || 0,
        cam: data.cam || 0,

        //* detalii
        bazaimpozit: data.bazaimpozit || 0,
        impozitscutit: data.impozitscutit || 0,
        salariurealizat: data.salariurealizat || 0,
        venitnet: data.venitnet || 0,
        zilelucrate: data.zilelucrate || 0,
        zileplatite: data.zileplatite || 0,

        //
        idstat: data.id || 0,
        idcontract: contract.id || 0,
        oresuplimentare: oresuplimentare || 0,
        totaloresuplimentare: totaloresuplimentare || 0,
        primabruta: data.primabruta || 0,
      },
      this.getTotalPensie
    );
  }

  onSelect(e) {
    const selectedIndex = e.target.options.selectedIndex;
    const idangajat = e.target.options[selectedIndex].getAttribute('data-key');
    if (selectedIndex === 0) setAngajatSel(null);
    else
      setAngajatSel({
        idpersoana: idangajat,
        numeintreg: e.target.value,
      });

    this.setState(
      {
        selected_angajat: getAngajatSel(),
      },
      this.fillForm
    );
  }

  // recalculeaza doar total
  async recalculeaza() {
    console.log('recalculez...');

    // get an, luna from select components
    let an = this.state.an;
    let luna = this.state.luna.nr;
    let luna_nume = this.state.luna.nume;

    // get idpersoana from select component
    const idpersoana = this.state.selected_angajat ? this.state.selected_angajat.idpersoana : null;
    if (!idpersoana) {
      this.clearForm();
      return;
    }

    //* 1. save retineri to DB
    if (this.state.idstat) {
      await axios
        .put(
          `${server.address}/retineri/${this.state.idretineri}`,
          {
            idstat: this.state.idstat,
            avansnet: this.state.avansnet || 0,
            curseurron: this.state.cursValutar || 0,
            pensiealimentara: this.state.pensiealimentara || 0,
            pensiefacangajat: this.state.pensiefacangajat || 0,
            pensiefacangajator: this.state.pensiefacangajator || 0,
            pensiefacangajatretinuta: this.state.pensiefacangajatretinuta || 0,
            pensiefacangajatordeductibila: this.state.pensiefacangajatordeductibila || 0,
            pensiefacexcedent: this.state.pensiefacexcedent || 0,
            popriri: this.state.popriri || 0,
            imprumuturi: this.state.imprumuturi || 0,
          },
          {
            headers: authHeader(),
          }
        )
        .then((res) => res.status === 200)
        .catch((err) => console.error(err));
    }

    const rrDetails = {
      idcontract: this.state.idcontract,
      luna: luna,
      an: an,
      primaBruta: this.state.primabruta || 0,
      nrTichete: this.state.nrtichete || 0,
      totalOreSuplimentare: this.state.totaloresuplimentare || 0,
    };

    //* 2. recalculare realizariRetineri
    console.log(this.state.idcontract);
    const data = await axios
      .put(`${server.address}/realizariretineri/update/calc`, rrDetails, { headers: authHeader() })
      .then((res) => (res.status === 200 ? res.data : null))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut recalcula realizari/retineri: ' + err.response.data.message,
        })
      );
    if (!data) return;

    this.setState(
      {
        totaldrepturi: data.totaldrepturi || 0,
        restplata: data.restplata || 0,
        cas: data.cas || 0,
        cass: data.cass || 0,
        cam: data.cam || 0,
        impozit: data.impozit || 0,
        valoaretichete: data.valoaretichete || 0,

        showToast: true,
        toastTitle: 'Recalculat',
        toastMessage: `Realizari/Retineri recalculate in ${luna_nume} ${an}`,
        toastColor: 'lightgreen',
      },
      this.fillForm
    );
  }

  async creeazaStateUltimele6Luni() {
    if (!this.state.selected_angajat.idpersoana) {
      this.clearForm();
      return;
    }

    let luna = this.state.luna;
    let an = this.state.an;

    const ok = await axios
      .put(
        `${server.address}/realizariretineri/recalc/ultimele6/idc=${this.state.idcontract}&mo=${luna.nr}&y=${an}`,
        {},
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut recalcula realizari/retineri: ' + err.response.data.message,
        })
      );

    if (ok) {
      this.setState({
        showToast: true,
        toastColor: 'lightgreen',
        toastTitle: 'Recalculat',
        toastMessage: `Statul de salarii recalculat pe ultimele 6 luni incepand cu ${luna.nume}. De asemenea, bazele de calcul au fost adaugate.`,
      });
    }
  }

  async recalcSocietate() {
    if (!this.state.selected_angajat.idpersoana) {
      this.clearForm();
      return;
    }

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
          toastMessage: 'Nu am putut recalcula realizari/retineri: ' + err.response.data.message,
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

  calcNrTichete() {
    if (!this.state.selected_angajat) return;

    this.setState({
      nrtichete: this.state.zilelucrate,
    });
  }

  async preiaCursCurent() {
    let curs = await axios
      .get(`${server.address}/webparse/cursbnr`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut prelua cursul curent: ' + err.response.data.message,
        })
      );

    this.setState({
      cursValutar: curs,
    });
  }

  async getOresuplimentare(idc, luna, an) {
    const oresuplimentare = await axios
      .get(`${server.address}/oresuplimentare/api/idc=${idc}&mo=${luna}&y=${an}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut prelua orele suplimentare: ' + err.response.data.message,
        })
      );

    return oresuplimentare;
  }

  veziOreSuplimentare() {
    console.log('editez ore suplimentare');

    this.setState({
      show: true,
    });
  }

  async veziPensieFacultativa() {
    this.setState({
      showPensie: true,
    });
  }

  async addOrasuplimentara(n, p, t) {
    const ore_body = {
      statsalariat: { id: this.state.idstat },
      nr: n || 0,
      procent: p || 0,
      total: t ? t.toFixed(0) : 0,
    };

    await axios
      .post(`${server.address}/oresuplimentare`, ore_body, {
        headers: authHeader(),
      })
      .then(this.renderTabelore)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut adăuga ore suplimentare: ' + err.response.data.message,
        })
      );
  }

  async deleteOra(id) {
    await axios
      .delete(`${server.address}/oresuplimentare/${id}`, { headers: authHeader() })
      .then(this.renderTabelore)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut șterge orele suplimentare: ' + err.response.data.message,
        })
      );
  }

  async renderTabelore() {
    let oreSuplimentare = await this.getOresuplimentare(
      this.state.idcontract,
      this.state.luna.nr,
      this.state.an
    );
    let totaloresuplimentare = 0;
    for (let ora of oreSuplimentare) totaloresuplimentare += ora.total;

    this.setState({
      oresuplimentare: oreSuplimentare,
      totaloresuplimentare: totaloresuplimentare,
    });
  }

  handleClose() {
    this.setState(
      {
        show: false,
        showPensie: false,
        cursValutar: 0,
        pensiefacangajat: 0,
        modalMessage: '',
        nrore: 0,
        procent: 100,
      },
      this.fillForm
    );
  }

  async downloadStatIndividual(numeintreg, luna, an) {
    const user = authService.getCurrentUser();
    console.log('trying to download...');
    await fetch(
      `${server.address}/download/${user.id}/Stat Salarii - ${numeintreg} - ${luna.nume} ${an}.xlsx`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/octet-stream',
          Authorization: `Bearer ${user.accessToken}`,
        },
      }
    )
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `Stat Salarii - ${numeintreg} - ${luna.nume} ${an}.xlsx`;
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
        console.log('downloaded');
        window.scrollTo({
          top: 0,
          left: 0,
          behavior: 'smooth',
        });
      })
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut descărca ștatul: ' + err.response.data.message,
        })
      );
  }

  async getStatIndividual() {
    const idangajat = this.state.selected_angajat.idpersoana;
    const luna = this.state.luna;
    const an = this.state.an;
    const user = authService.getCurrentUser();

    const ok = await axios
      .get(
        `${server.address}/stat/${this.state.socsel.id}/individual/ida=${idangajat}&mo=${luna.nr}&y=${an}/${user.id}`,
        { headers: authHeader() }
      )
      .then((res) => res.status === 200)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastTitle: 'Eroare',
          toastColor: 'white',
          toastMessage: 'Nu am putut prelua ștatul individual: ' + err.response.data.message,
        })
      );

    if (ok) {
      let numeintreg = this.state.selected_angajat.numeintreg;
      this.downloadStatIndividual(numeintreg, luna, an);
    }
  }

  onSubmit(e) {
    e.preventDefault();

    this.recalculeaza();
  }

  convertCurrency(from, curs) {
    if (curs && from) return Number(from / curs).toFixed(4);
    else return 0;
  }

  onChangeAn(e) {
    const an = e.target.value;
    // eslint-disable-next-line eqeqeq
    if (an == this.state.an_inceput_contract) {
      const luni_contract = luni.slice(Number(this.state.luna_inceput_contract) - 1);
      this.setState(
        {
          an: an,
          luna: {
            nume: luni_contract[0],
            nr: 13 - luni_contract.length,
          },
        },
        this.fillForm
      );
    } else this.setState({ an: an }, this.fillForm);
  }

  render() {
    const this_year = new Date().getFullYear();

    var ani = [];
    for (let i = this.state.an_inceput_contract; i <= this_year + 1; ++i) {
      ani.push(<option key={i}>{i}</option>);
    }

    var luniComponent = luni.map((luna_nume, index) => (
      <option key={index} data-key={index + 1}>
        {luna_nume}
      </option>
    ));
    // eslint-disable-next-line eqeqeq
    if (this.state.an == this.state.an_inceput_contract) {
      luniComponent = luniComponent.slice(Number(this.state.luna_inceput_contract) - 1);
    }

    const tabel_ore = this.state.oresuplimentare.map((ora, index) => {
      for (let key in ora) if (!ora[key]) ora[key] = '-';

      return (
        <tr key={ora.id}>
          <th>{ora.nr} ore</th>
          <th>{ora.procent}%</th>
          <th>{ora.total} RON</th>
          <th className="d-inline-flex flex-row justify-content-around">
            <PopupState variant="popover" popupId="demo-popup-popover">
              {(popupState) => (
                <div>
                  <Button
                    variant="outline-secondary"
                    className="m-0 p-1 rounded-circle border-0"
                    {...bindTrigger(popupState)}
                  >
                    <Trash2 fontSize="small" />
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
                      <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                      <br />
                      <Button
                        variant="outline-danger"
                        onClick={() => {
                          popupState.close();
                          this.deleteOra(ora.id);
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
    });

    const nume_persoane_opt = this.state.lista_angajati.map((angajat) => (
      <option key={angajat.id} data-key={angajat.id}>
        {angajat.nume}
      </option>
    ));

    return (
      <Aux>
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

        {/* ORE SUPLIMENTARE */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Ore suplimentare</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Nr. ore</Form.Label>
                  <Form.Control
                    type="number"
                    min="0"
                    value={this.state.nrore}
                    onChange={(e) => this.setState({ nrore: e.target.value })}
                  />
                </Form.Group>
              </Col>
              <Col md={3}>
                <Form.Group>
                  <Form.Label>Procent</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.procent}
                    onChange={(e) => this.setState({ procent: e.target.value })}
                  >
                    <option>100</option>
                    <option>150</option>
                    <option>175</option>
                    <option>200</option>
                  </Form.Control>
                </Form.Group>
              </Col>
              <Col md={4}>
                <Form.Group>
                  <Form.Label>Suma</Form.Label>
                  <Form.Control
                    disabled
                    type="number"
                    value={(
                      Number(this.state.nrore) *
                      (Number(this.state.procent) / 100) *
                      Number(this.state.salariupeora)
                    ).toFixed(0)}
                  />
                </Form.Group>
              </Col>
              <Col md={1}>
                <Form.Label> </Form.Label>
                <Button
                  className="display-flex m-0"
                  onClick={async () =>
                    await this.addOrasuplimentara(
                      this.state.nrore,
                      this.state.procent,
                      Number(this.state.nrore) *
                        (Number(this.state.procent) / 100) *
                        Number(this.state.salariupeora)
                    )
                  }
                >
                  +
                </Button>
              </Col>
            </Row>
            <Table responsive hover>
              <thead>
                <tr>
                  <th>nr.</th>
                  <th>procent</th>
                  <th>suma</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>{tabel_ore}</tbody>
            </Table>
          </Modal.Body>
        </Modal>

        {/* PENSIE FACULTATIVA */}
        <Modal show={this.state.showPensie} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Pensie facultativă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Col md={12}>
                <Form.Group id="curseurron">
                  <Form.Label>
                    Curs BNR EUR/RON Final {this.state.luna.nume} {this.state.an}
                  </Form.Label>
                  <InputGroup>
                    <Form.Control
                      type="number"
                      min="0"
                      step="0.0001"
                      onChange={(e) => {
                        this.setState({ cursValutar: Number(e.target.value).toFixed(4) });
                      }}
                      value={this.state.cursValutar}
                    />
                    <InputGroup.Append>
                      <Button
                        onClick={this.preiaCursCurent}
                        size="sm"
                        className="p-0 pl-2 pr-2"
                        variant="outline-info"
                      >
                        Preia curs
                        <br />
                        curent
                      </Button>
                    </InputGroup.Append>
                  </InputGroup>
                </Form.Group>
              </Col>
              <Col md={12}>
                <Form.Group id="pensiefacangajat">
                  <Form.Label>Pensie facultativă angajat</Form.Label>
                  <InputGroup className="mb-3">
                    <Form.Control
                      type="number"
                      min="0"
                      onChange={(e) => {
                        this.setState({ pensiefacangajat: e.target.value });
                      }}
                      value={this.state.pensiefacangajat}
                    />
                    <InputGroup.Append>
                      <InputGroup.Text id="basic-addon2">
                        {this.convertCurrency(this.state.pensiefacangajat, this.state.cursValutar)}{' '}
                        EUR
                      </InputGroup.Text>
                    </InputGroup.Append>
                  </InputGroup>
                </Form.Group>
              </Col>
              <Col md={12}>
                <Form.Group id="totalpensiefacan">
                  <Form.Label>Total Pensie Facultativă an curent</Form.Label>
                  <InputGroup className="mb-3">
                    <Form.Control
                      disabled
                      type="number"
                      min="0"
                      value={this.state.totalpensiefacultativa}
                    />
                    <InputGroup.Append>
                      <InputGroup.Text id="basic-addon2">EUR</InputGroup.Text>
                    </InputGroup.Append>
                  </InputGroup>
                </Form.Group>

                {/* <Form.Group id="impozitdeduspensie">
                  <Form.Label>Impozit Dedus</Form.Label>
                  <Form.Control disabled type="number" min="0" value={this.state.impozitdedus} />
                </Form.Group> */}

                <Form.Label> </Form.Label>
                <Button
                  className="mb-3 float-right"
                  onClick={() => {
                    this.setState({ showPensie: false });
                  }}
                >
                  Adaugă
                </Button>
                {/* <Button className="mb-3 float-right" onClick={() => {}}>
                  Calculează
                </Button> */}
              </Col>
            </Row>
          </Modal.Body>
        </Modal>

        <Breadcrumb style={{ fontSize: '12px' }}>
          <Breadcrumb.Item href="/dashboard/societati">{this.state.socsel.nume}</Breadcrumb.Item>
          <Breadcrumb.Item href="/tables/angajati">Angajați</Breadcrumb.Item>
          <Breadcrumb.Item active>Realizări & Rețineri</Breadcrumb.Item>
        </Breadcrumb>

        <Card>
          {/* SELECT LUNA + AN */}
          <Card.Header>
            <Row>
              {/* LUNA */}
              <Col md={6}>
                <FormControl
                  as="select"
                  value={this.state.luna.nume}
                  onChange={(e) => {
                    let selectedIndex = e.target.options.selectedIndex;
                    this.setState(
                      {
                        luna: {
                          nume: e.target.value,
                          nr: Number(e.target.options[selectedIndex].getAttribute('data-key')),
                        },
                      },
                      this.fillForm
                    );
                  }}
                >
                  {luniComponent}
                </FormControl>
              </Col>
              {/* AN */}
              <Col md={6}>
                <FormControl as="select" value={this.state.an} onChange={(e) => this.onChangeAn(e)}>
                  {ani}
                </FormControl>
              </Col>
            </Row>
          </Card.Header>
          {/* SELECT ANGAJAT */}
          <Card.Header>
            <Card.Title as="h4">Angajat</Card.Title>
            <InputGroup className="mb-3">
              {/* NUMELE ANGAJATILOR CU CONTRACT */}
              <FormControl
                aria-describedby="basic-addon2"
                as="select"
                value={this.state.selected_angajat ? this.state.selected_angajat.numeintreg : ''}
                onChange={(e) => this.onSelect(e)}
              >
                <option> - </option>
                {/* lista_angajati mapped as <option> */}
                {nume_persoane_opt}
              </FormControl>
              <InputGroup.Append>
                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
                  overlay={
                    <Tooltip id="update-button" style={{ opacity: '.4' }}>
                      Către date personale
                    </Tooltip>
                  }
                >
                  <Button href="/forms/angajat" variant="outline-info" className="pb-0">
                    <Info size={20} className="m-0" />
                  </Button>
                </OverlayTrigger>
              </InputGroup.Append>
            </InputGroup>
          </Card.Header>

          <Card.Body>
            <Form onSubmit={this.onSubmit}>
              <Row>
                {/* REALIZARI = LEFT TOP */}
                <Col md={8} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Realizări
                  </Typography>
                  <Row>
                    <Col md={6}>
                      <Form.Group id="functie">
                        <Form.Label>Funcție</Form.Label>
                        <Form.Control type="text" disabled value={this.state.functie || '-'} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="duratailucru">
                        <Form.Label>Durată zi lucru</Form.Label>
                        <Form.Control
                          type="number"
                          disabled
                          value={this.state.duratazilucru || ''}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="normazilucru">
                        <Form.Label>Normă lucru</Form.Label>
                        <Form.Control type="text" disabled value={this.state.normalucru || ''} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="salariubrut">
                        <Form.Label>Salariu brut</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.salariubrut)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="orelucrate">
                        <Form.Label>Ore lucrate</Form.Label>
                        <Form.Control type="number" disabled value={this.state.orelucrate} />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="tichete">
                        <Form.Label>Nr. Tichete</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="number"
                            min="0"
                            value={this.state.nrtichete || 0}
                            onChange={(e) => this.setState({ nrtichete: e.target.value })}
                          />
                          <InputGroup.Append>
                            <Button
                              onClick={this.calcNrTichete}
                              size="sm"
                              className="p-0 pl-2 pr-2"
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
                              }
                              disabled={this.state.selected_angajat ? false : true}
                            >
                              Calculează
                              <br />
                              automat
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilecm">
                        <Form.Label>Zile concediu medical lucrătoare</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="number"
                            disabled
                            min="0"
                            value={this.state.zilecmlucratoare}
                          />
                          {this.state.zilecmlucratoare ? (
                            <InputGroup.Append>
                              <InputGroup.Text style={{ fontSize: '0.75rem' }}>
                                Brut: {this.state.valcm.toFixed(0)} RON
                              </InputGroup.Text>
                            </InputGroup.Append>
                          ) : null}
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilecolucratoare">
                        <Form.Label>Zile concediu odihna lucrătoare</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="number"
                            disabled
                            min="0"
                            value={this.state.zilecolucratoare}
                          />
                          {this.state.zilecolucratoare ? (
                            <InputGroup.Append>
                              <InputGroup.Text style={{ fontSize: '0.75rem' }}>
                                {(this.state.zilecolucratoare * this.state.salariupezi).toFixed(0)}{' '}
                                RON
                              </InputGroup.Text>
                            </InputGroup.Append>
                          ) : null}
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zilecfp">
                        <Form.Label>Zile libere</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.zilelibere}
                          onChange={(e) => this.setState({ zilelibere: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="zileinvoire">
                        <Form.Label>Zile învoire</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.zileinvoire}
                          onChange={(e) => this.setState({ zileinvoire: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    {/* TODO */}
                    <Col md={6}>
                      <Form.Group id="oresuplimentare">
                        <Form.Label>Valoare Ore suplimentare</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="text"
                            disabled
                            value={this.numberWithCommas(this.state.totaloresuplimentare)}
                          />
                          <InputGroup.Append>
                            <Button
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
                              }
                              disabled={!this.state.selected_angajat}
                              onClick={() => this.veziOreSuplimentare()}
                            >
                              Vezi
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="primabruta">
                        <Form.Label>Primă brută</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.primabruta}
                          onChange={(e) => this.setState({ primabruta: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>

                {/* RETINERI = RIGHT */}
                <Col md={4} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Rețineri
                  </Typography>
                  <Row>
                    <Col md={12}>
                      <Form.Group id="avansnet">
                        <Form.Label>Avans</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.avansnet}
                          onChange={(e) => this.setState({ avansnet: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiefacultativa">
                        <Form.Label>Pensie facultativă</Form.Label>
                        <InputGroup>
                          <Form.Control
                            type="number"
                            min="0"
                            disabled
                            value={this.state.pensiefacangajat}
                            onChange={(e) =>
                              this.setState({ pensiefacangajat: e.target.value || 0 })
                            }
                          />
                          <InputGroup.Append>
                            <Button
                              variant={
                                this.state.selected_angajat ? 'outline-info' : 'outline-dark'
                              }
                              disabled={!this.state.selected_angajat}
                              onClick={() => this.veziPensieFacultativa()}
                            >
                              Vezi
                            </Button>
                          </InputGroup.Append>
                        </InputGroup>
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="pensiealimentara">
                        <Form.Label>Pensie alimentară</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.pensiealimentara}
                          onChange={(e) => this.setState({ pensiealimentara: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="popriri">
                        <Form.Label>Popriri</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.popriri}
                          onChange={(e) => this.setState({ popriri: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="imprumuturi">
                        <Form.Label>Împrumuturi</Form.Label>
                        <Form.Control
                          type="number"
                          min="0"
                          value={this.state.imprumuturi}
                          onChange={(e) => this.setState({ imprumuturi: e.target.value })}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={12}>
                      <Form.Group id="deducere">
                        <Form.Label>Deducere personală</Form.Label>
                        <Form.Control type="number" disabled min="0" value={this.state.deducere} />
                      </Form.Group>
                    </Col>
                  </Row>
                </Col>

                {/* TOTAL = LEFT BOTTOM */}
                <Col md={12} className="border rounded pt-3">
                  <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
                    Total
                  </Typography>
                  <Row>
                    <Col md={6}>
                      <Form.Group id="totaltrepturi">
                        <Form.Label>Total drepturi</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.totaldrepturi)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group id="restplata">
                        <Form.Label>Rest de plată brut</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.restplata)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cas">
                        <Form.Label>CAS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cas)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="cass">
                        <Form.Label>CASS</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cass)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="valoaretichete">
                        <Form.Label>Valoare tichete</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.valoaretichete)}
                        />
                      </Form.Group>
                    </Col>
                    <Col md={4}>
                      <Form.Group id="impozit">
                        <Form.Label>Impozit</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.impozit)}
                        />
                      </Form.Group>
                    </Col>

                    <Col md={4}>
                      <Form.Group id="cam">
                        <Form.Label>CAM</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(this.state.cam)}
                        />
                      </Form.Group>
                    </Col>

                    <Col md={4}>
                      <Form.Group id="cheltuieliangajator">
                        <Form.Label>Cheltuieli angajator</Form.Label>
                        <Form.Control
                          type="text"
                          disabled
                          value={this.numberWithCommas(
                            Number(this.state.cam) +
                              Number(this.state.totaldrepturi) +
                              Number(this.state.valoaretichete)
                          )}
                        />
                      </Form.Group>
                    </Col>
                  </Row>
                  <DropdownButton
                    title="Recalculează"
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.selected_angajat}
                    className="mb-3 float-right"
                    onClick={(e) => e.stopPropagation()}
                  >
                    <Dropdown.Item eventKey="1" onClick={this.onSubmit}>
                      Luna selectată
                    </Dropdown.Item>
                    <Dropdown.Item eventKey="2" onClick={this.creeazaStateUltimele6Luni}>
                      Ultimele 6 luni
                    </Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item eventKey="3" onClick={this.recalcSocietate}>
                      Toți angajații
                    </Dropdown.Item>
                  </DropdownButton>
                  <Button
                    variant={this.state.selected_angajat ? 'primary' : 'outline-dark'}
                    disabled={!this.state.selected_angajat}
                    onClick={this.getStatIndividual}
                    className="float-right mb-3"
                  >
                    Stat salariat individual
                  </Button>
                </Col>

                {/* DETALII */}
                <Col md={12}>
                  <Card className="mt-2">
                    <Card.Header
                      style={{ cursor: 'pointer' }}
                      onClick={() =>
                        this.setState({
                          detaliiAccordion: !this.state.detaliiAccordion,
                        })
                      }
                    >
                      <Card.Title
                        as="h5"
                        aria-controls="accordion1"
                        aria-expanded={this.state.detaliiAccordion}
                      >
                        Detalii
                      </Card.Title>
                    </Card.Header>
                    <Collapse in={this.state.detaliiAccordion}>
                      <div id="accordion1">
                        <Card.Body>
                          <Row>
                            <Form.Group id="bazaimpozit" as={Col} md="6">
                              <Form.Label>Bază impozit</Form.Label>
                              <Form.Control type="number" value={this.state.bazaimpozit} disabled />
                            </Form.Group>
                            <Form.Group id="impozitscutit" as={Col} md="6">
                              <Form.Label>Impozit scutit</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.impozitscutit}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="nrpersoaneintretinere" as={Col} md="6">
                              <Form.Label>Număr persoane întreținere</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.nrpersoaneintretinere}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="salariupeora" as={Col} md="6">
                              <Form.Label>Salariu pe oră</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.salariupeora}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="salariupezi" as={Col} md="6">
                              <Form.Label>Salariu pe zi</Form.Label>
                              <Form.Control type="number" value={this.state.salariupezi} disabled />
                            </Form.Group>
                            <Form.Group id="salariurealizat" as={Col} md="6">
                              <Form.Label>Salariu realizat</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.salariurealizat}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="venitnet" as={Col} md="6">
                              <Form.Label>Venit net</Form.Label>
                              <Form.Control type="number" value={this.state.venitnet} disabled />
                            </Form.Group>
                            <Form.Group id="zilec" as={Col} md="6">
                              <Form.Label>Zile concediu (total)</Form.Label>
                              <Form.Control type="number" value={this.state.zilec} disabled />
                            </Form.Group>
                            <Form.Group id="zilecmlucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu medical</Form.Label>
                              <Form.Control type="number" value={this.state.zilecm} disabled />
                            </Form.Group>
                            <Form.Group id="zilecolucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu odihna</Form.Label>
                              <Form.Control type="number" value={this.state.zileco} disabled />
                            </Form.Group>
                            <Form.Group id="zilecfp" as={Col} md="6">
                              <Form.Label>Zile concediu fară plată</Form.Label>
                              <Form.Control type="number" value={this.state.zilecfp} disabled />
                            </Form.Group>
                            <Form.Group id="zilecfplucratoare" as={Col} md="6">
                              <Form.Label>Zile concediu fara plată lucrătoare</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.zilecfplucratoare}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="zilelucrate" as={Col} md="6">
                              <Form.Label>Zile lucrate</Form.Label>
                              <Form.Control type="number" value={this.state.zilelucrate} disabled />
                            </Form.Group>
                            <Form.Group id="zileplatite" as={Col} md="6">
                              <Form.Label>Zile plătite</Form.Label>
                              <Form.Control type="number" value={this.state.zileplatite} disabled />
                            </Form.Group>
                            <Form.Group id="restplatanet" as={Col} md="6">
                              <Form.Label>Rest plata net</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.restplata - this.state.valoaretichete}
                                disabled
                              />
                            </Form.Group>
                            <Form.Group id="zileconeefectuat" as={Col} md="6">
                              <Form.Label>Rest plata net</Form.Label>
                              <Form.Control
                                type="number"
                                value={this.state.restplata - this.state.valoaretichete}
                                disabled
                              />
                            </Form.Group>
                          </Row>
                        </Card.Body>
                      </div>
                    </Collapse>
                  </Card>
                </Col>

                <Col></Col>
              </Row>
            </Form>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}

export default RealizariRetineri;
