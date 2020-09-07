import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
// import Persoana from '../UIElements/Forms/Persoana';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
// import AddContract from './AddContract';

/*
  TODO
  * fetch date contract when selecting anagajat
  * insert prop to see when selected id changes
  * inside EditAngajat set prop when changing selection
  * when focusing pill 'contract' fetch if person has contract:
  *   ├─has contract: method = 'PUT, 'button text = "Actualizează"
  *   └─ no contract: method 'POST' => 1. create contract -> get idcontract,
  *                                    2. create angajat with idpersoana + idcontract
  * 
  * change add persoana to create persoana and create angajat with idpersoana
  *
*/

class Angajat extends React.Component {
  constructor(props) {
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);

    this.persoana = React.createRef();
    this.contract = React.createRef();

    this.state = {
      show: false,
      modalMessage: '',

      key: 'date-personale',
    };
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
  }

  onSubmit = async (e) => {
    // field checking functions
    if (!this.persoana.current.hasRequired()) {
      this.setState({
        key: 'date-personale',
        show: true,
        modalMessage: 'Persoana trebuie să aibă Nume și Prenume.',
      });
      window.scrollTo(0, 0);
      return;
    }

    if (!this.contract.current.hasRequired()) {
      this.setState({
        key: 'contract',
      });
      return;
    }

    const idpersoana = await this.persoana.current.getIdOfSelected();
    console.log(idpersoana);
    const idcontract = await this.contract.current.onSubmit(e);
    console.log('idpersoana:', idpersoana);
    console.log('idcontract:', idcontract);
    if (typeof idpersoana === 'number' && typeof idcontract === 'number') {
      // console.log('idpersoana:', idpersoana);
      // console.log('idcontract:', idcontract);
      let angajat_body = {
        idpersoana: idpersoana,
        idcontract: idcontract,
        co: null,
        cm: null,
      };
      // create angajat in database
      const idangajat = await fetch('http://localhost:5000/angajat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(angajat_body),
      }).then((res) => res.json());

      console.log('idangajat:', idangajat);

      if (typeof idangajat === 'number') {
        this.setState({
          key: 'date-personale',
          show: true,
          modalMessage: 'Angajat adaugat cu succes.🚀',
        });
        this.persoana.current.clearFields(true);
        this.contract.current.clearFields();
        window.scrollTo(0, 0);
      }
      // .catch((err) => console.log(err.message));
    }
  };

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
            <h5>Date angajat</h5>
            <hr />
            <Tabs
              variant="pills"
              activeKey={this.state.key}
              onSelect={(key) => {
                this.setState({
                  key: key,
                });
              }}
            >
              <Tab eventKey="date-personale" title="Date Personale">
                <Row className="m-0 p-0">
                  <Col md={12} className="m-0 p-0">
                    <EditPersoana ref={this.persoana} asChild />
                    <Button
                      className="float-right"
                      variant="outline-primary"
                      onClick={() => {
                        this.setState({ key: 'contract' });
                        window.scrollTo(0, 0);
                      }}
                    >
                      Următor
                    </Button>
                  </Col>
                </Row>
              </Tab>
              <Tab eventKey="contract" title="Contract de munca">
                <Contract ref={this.contract} asChild />
                <Button variant="outline-primary" onClick={this.onSubmit}>
                  Adaugă
                </Button>
              </Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;
