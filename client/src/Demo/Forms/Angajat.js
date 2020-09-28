import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
// import Persoana from '../UIElements/Forms/Persoana';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
import ConcediiMedicale from '../Tables/ConcediiMedicale';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { getAngajatSel } from '../Resources/angajatsel';

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
	? adding:
	*	  |> in EditPersoana -> when selecting angajat, remember selectednume in sessionstorage
	*		|> Angajat.js displays, and preselects, sessionStorage.selectedAngajat :: {numeintreg, idpersoana}
*/

class Angajat extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    // this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);

    this.persoana = React.createRef();
    this.contract = React.createRef();
    this.co = React.createRef();
    this.cm = React.createRef();

    this.state = {
			socsel: getSocSel(),
			angajatsel: getAngajatSel(),			

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
		const angajatsel = getAngajatSel();
		const idpersoana = angajatsel ? angajatsel.idpersoana : -1;
    if (idpersoana === null || idpersoana === -1) {
			this.contract.current.clearFields();
			this.setState({angajatsel: null});
      return;
    } else this.contract.current.setState({ buttonDisabled: false });

    const angajat = await axios
      .get(`${server.address}/angajat/${idpersoana}`, { headers: authHeader() })
      .then((res) => res.data)
		.catch((err) => console.error(err));

    this.setState({
			angajatsel: angajatsel,
      angajat: angajat,
      idcontract: angajat.idcontract,
      idpersoana: angajat.idpersoana,
      idsocietate: angajat.idsocietate,
		});

    return angajat;
  }

  async onFocusContract() {
    const angajat = await this.getSelectedAngajatData();
    if (!angajat) return;
    // declared for typing convenience
    let idcontract = angajat.idcontract;
    let idpersoana = angajat.idpersoana;

    var contract = null;
    // if angajat has contract
    if (idcontract !== null) {
      // fetch data from contract
      contract = await axios
        .get(`${server.address}/contract/${idcontract}`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) => console.error(err));
		}
    //* FILL FORM
    this.contract.current.fillForm(contract, idpersoana);
  }

  async onFocusCO() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;
    if (angajat.idcontract === null) {
      this.setState(
        {
          show: true,
          modalMessage: 'Pentru concedii, angajatul are nevoie de un contract de muncă.',
        },
        () => this.setState({ key: 'contract' })
      );
      return;
		}

    this.co.current.setAngajat(angajat);
  }

  async onFocusCM() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;

    // angajatul nu are contract, deci nu se pot adauga concedii
    if (angajat.idcontract === null) {
      this.setState({
        show: true,
        modalMessage: 'Pentru concedii, angajatul are nevoie de un contract de muncă.',
        key: 'contract',
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
              {this.state.socsel.nume ? this.state.socsel.nume : ''} - Date angajat
              {this.state.angajatsel
                ? ' - ' + this.state.angajatsel.numeintreg
                : ''}
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
                <Contract
                  ref={this.contract}
                  idcontract={this.state.idcontract}
                  idangajat={this.state.idangajat}
                />
              </Tab>

              <Tab eventKey="co" title="C.O.">
                <ConcediiOdihna ref={this.co} />
              </Tab>

              <Tab eventKey="cm" title="C.M.">
                <ConcediiMedicale ref={this.cm} />
              </Tab>
            </Tabs>
            <Button onClick={() => window.scrollTo(0, 0)} className="float-center">
              TO TOP
            </Button>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;
