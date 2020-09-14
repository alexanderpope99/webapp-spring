import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { getSocSel, setSocSel } from '../Resources/socsel';
// import Persoana from '../UIElements/Forms/Persoana';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
import ConcediiMedicale from '../Tables/ConcediiMedicale';

/*
  ? how it works now:
  * fetch date contract when focusint tab 'contract'
  *
  * when focusing 'contract' check if person has contract:
  *   |> has contract: 1. method = 'PUT'
  *                    2. populate form with contract data
  *
  *   |>  no contract: 1. method = 'POST'
  *                    2. clearFields()
*/

class Angajat extends React.Component {
  constructor() {
    super();
    // this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);

    this.persoana = React.createRef();
    this.contract = React.createRef();
    this.co = React.createRef();
    this.cm = React.createRef();

    this.state = {
      socsel: getSocSel(),

      angajat: null,
      idpersoana: null,
      idcontract: null,
      idsocietate: getSocSel().id,

      show: false,
      modalMessage: '',

      key: 'date-personale', // selected pill
    };

    window.scrollTo(0, 0);
  }

  componentDidMount() {
    window.scrollTo(0, 0);
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  async getSelectedAngajatData() {
    // get id of selected angajat
    const idpersoana = this.persoana.current.getIdOfSelected();
    if (idpersoana === null || idpersoana === -1) {
      this.contract.current.clearFields();
      return;
    } else this.contract.current.setState({ buttonDisabled: false });

    // get angajat with selected id
    const angajat = await fetch(`http://localhost:5000/angajat/${idpersoana}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => res.json())
      .catch((err) => console.error(err));

    this.setState({
      angajat: angajat,
      idcontract: angajat.idcontract,
      idpersoana: angajat.idpersoana,
      idsocietate: angajat.idsocietate,
    });

    return angajat;
  }

  async onFocusContract() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;
    // declared just for typing convenience
    let idcontract = angajat.idcontract;
    let idpersoana = angajat.idpersoana;

    var contract =  null;
    // if angajat has contract
    if (idcontract !== null) {
      // fetch data from contract
      contract = await fetch(
        `http://localhost:5000/contract/${idcontract}`,
        {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        }
      )
        .then(res => res.json())
        .catch(err => console.error(err));
    }
    //* FILL FORM
    this.contract.current.fillForm(contract, idpersoana);
  }

  async onFocusCO() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;
    if(angajat.idcontract === null) {
      this.setState({
        show: true,
        modalMessage: "Pentru concedii, angajatul are nevoie de un contract de muncă.",
      }, () => this.setState({key: 'contract'}));
      return;
    }

    this.co.current.setAngajat(angajat);
  }

  async onFocusCM() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;

    // angajatul nu are contract, deci nu se pot adauga concedii
    if(angajat.idcontract === null) {
      this.setState({
        show: true,
        modalMessage: "Pentru concedii, angajatul are nevoie de un contract de muncă.",
        key: 'contract'
      });
      return;
    }

    this.cm.current.setAngajat(angajat);
  }

  render() {
    return (
      <Aux>
        <Modal show={this.state.show} onHide={this.handleClose}>
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
        <Row>
          <Col>
            <h5>
              Date angajat {this.state.socsel.nume ? "- " + this.state.socsel.nume : ""}
            </h5>

            <hr />
            <Tabs
              variant="pills"
              activeKey={this.state.key}
              onSelect={(key) => {
                this.setState({
                  key: key,
                });
                if (key === 'contract') this.onFocusContract();
                else if (key === 'co') this.onFocusCO();
                else if (key === 'cm') this.onFocusCM();
              }}
            >
              <Tab eventKey="date-personale" title="Date Personale">
                <EditPersoana ref={this.persoana} />
              </Tab>

              <Tab eventKey="contract" title="Contract de munca">
                <Contract ref={this.contract} idcontract={this.state.idcontract} idangajat={this.state.idangajat} />
              </Tab>

              <Tab eventKey="co" title="C.O.">
                <ConcediiOdihna ref={this.co} />
              </Tab>

              <Tab eventKey="cm" title="C.M.">
                <ConcediiMedicale ref={this.cm} />
              </Tab>

            </Tabs>
            <Button onClick={() => window.scrollTo(0, 0)} className="float-center">TO TOP</Button>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;