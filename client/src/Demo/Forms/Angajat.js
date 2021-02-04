import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal, Breadcrumb } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
import ConcediiMedicale from '../Tables/ConcediiMedicale';
import { getAngajatSel } from '../Resources/angajatsel';
import PersoaneIntretinereTabel from '../Tables/PersoaneIntretinere';
import BazaCalcul from '../Tables/BazaCalcul';
import ZileCOAn from '../Tables/ZileCOAn';

/*
  ? how it works now:
	*	Angajat.js displays, and preselects, sessionStorage.selectedAngajat :: {numeintreg, idpersoana}
	* * * * *
  * fetch data when focusing tabs
  * * * * *
	* in EditPersoana -> selecting angajat calls setAngajatSel
	* * * * *
*/

class Angajat extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);
    this.scrollToTopSmooth = this.scrollToTopSmooth.bind(this);

    this.persoana = React.createRef();
    this.persoaneintretinere = React.createRef();
    this.contract = React.createRef();
    this.co = React.createRef();
    this.cm = React.createRef();
    this.bc = React.createRef();
    this.zc = React.createRef();

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
    this.setState({ angajatsel: getAngajatSel() });
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

  async onFocusZC() {
    await this.zc.current.updateAngajatSel();
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
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">Societăți</Breadcrumb.Item>
              <Breadcrumb.Item href="/tables/angajati">Angajați</Breadcrumb.Item>
              <Breadcrumb.Item active>Detalii angajat</Breadcrumb.Item>
            </Breadcrumb>
            <h5 className="m-0">
              {this.state.socsel.nume ? this.state.socsel.nume : ''} - Detalii angajat
              {this.state.angajatsel && this.state.key !== 'date-personale'
                ? ' - ' + this.state.angajatsel.numeintreg
                : this.state.key !== 'date-personale'
                ? ' *niciun angajat selectat'
                : ''}
						<Button variant="link" size="sm" className="float-right m-0" href="/forms/realizari-retineri">Realizări / Rețineri</Button>
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
                else if (key === 'zc') this.onFocusZC();
              }}
            >
              <Tab eventKey="date-personale" title="Date personale">
                <EditPersoana ref={this.persoana} scrollToTopSmooth={this.scrollToTopSmooth} />
              </Tab>

              <Tab eventKey="contract" title="Contract muncă">
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
			  <Tab eventKey="zc" title="Zile CO An">
                <ZileCOAn
                  ref={this.zc}
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
