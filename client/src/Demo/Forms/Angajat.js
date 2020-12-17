import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
import ConcediiMedicale from '../Tables/ConcediiMedicale';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { getAngajatSel } from '../Resources/angajatsel';
import PersoaneIntretinereTabel from '../Tables/PersoaneIntretinere';
import BazaCalcul from '../Tables/BazaCalcul';
import RealizariRetineri from './RealizariRetineri';

/*
  ? how it works now:
	*	Angajat.js displays, and preselects, sessionStorage.selectedAngajat :: {numeintreg, idpersoana}
	* * * * *
  * fetch date contract when focusing tab 'contract'
  * * * * *
  * when focusing 'contract' check if person has contract:
  *   |> has contract: 1. method = 'PUT'
  *                    2. populate form with contract data
	* 											\ if persoana has adresa.judet != SECTOR -> preselect casa_sanatate
  *
  *   |>  no contract: 1. method = 'POST'
  *                    2. clearFields()
	* * * * *
	* in EditPersoana -> when selecting angajat, remember selectednume in sessionstorage
	* * * * *
*/

class Angajat extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    // this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);
    this.scrollToTopSmooth = this.scrollToTopSmooth.bind(this);

    this.persoana = React.createRef();
    this.persoaneintretinere = React.createRef();
    this.contract = React.createRef();
    this.co = React.createRef();
    this.cm = React.createRef();
    this.bc = React.createRef();

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

  scrollToTopSmooth() {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  async onFocusContract() {
		await this.contract.current.fillForm();
		this.setState({angajatsel: getAngajatSel()});
  }

  async onFocusCO() {
    await this.co.current.updateAngajatSel();
    this.setState({ angajatsel: getAngajatSel() });
  }

  async onFocusCM() {
    await this.cm.current.updateAngajatSel();
    this.setState({ angajatsel: getAngajatSel() });
  }

  async onFocusBC() {
    await this.bc.current.updateAngajatSel();
    this.setState({ angajatsel: getAngajatSel() });
  }

  async onFocusPI() {
    await this.persoaneintretinere.current.updateAngajatSel();
    this.setState({ angajatsel: getAngajatSel() });
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
            <h5 className="mb-3">
              {this.state.socsel.nume ? this.state.socsel.nume : ''} - Date angajat
              {this.state.angajatsel && this.state.key !== 'date-personale'
                ? ' - ' + this.state.angajatsel.numeintreg
                : this.state.key !== 'date-personale'
                ? ' *niciun angajat selectat'
                : ''}
								<Button variant="link" className="float-right bb-5" href="/forms/realizari-retineri">Realizări/Rețineri</Button>
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
                else if (key === 'pi') this.onFocusPI();
                else if (key === 'bc') this.onFocusBC();
              }}
            >
              <Tab eventKey="date-personale" title="Date Personale">
                <EditPersoana ref={this.persoana} scrollToTopSmooth={this.scrollToTopSmooth} />
              </Tab>

              <Tab eventKey="contract" title="Contract de munca">
                <Contract
                  ref={this.contract}
                  idcontract={this.state.idcontract}
                  idangajat={this.state.idangajat}
                  scrollToTopSmooth={this.scrollToTopSmooth}
                />
              </Tab>

              <Tab eventKey="co" title="C.O.">
                <ConcediiOdihna ref={this.co} scrollToTopSmooth={this.scrollToTopSmooth} />
              </Tab>

              <Tab eventKey="cm" title="C.M.">
                <ConcediiMedicale ref={this.cm} scrollToTopSmooth={this.scrollToTopSmooth} />
              </Tab>

              <Tab eventKey="bc" title="Bază calcul">
                <BazaCalcul ref={this.bc} scrollToTopSmooth={this.scrollToTopSmooth} />
              </Tab>

              <Tab eventKey="pi" title="Pers. într.">
                <PersoaneIntretinereTabel
                  ref={this.persoaneintretinere}
                  scrollToTopSmooth={this.scrollToTopSmooth}
                />
              </Tab>
            </Tabs>
            <Button
              onClick={() =>
                window.scrollTo({
                  top: 0,
                  left: 0,
                  behavior: 'smooth',
                })
              }
              className="float-center"
            >
              TO TOP
            </Button>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;
