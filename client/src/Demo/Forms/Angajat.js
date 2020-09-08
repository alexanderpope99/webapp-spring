import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
// import Persoana from '../UIElements/Forms/Persoana';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
// import AddContract from './AddContract';

/*
  TODO
  * fetch date contract when focusint tab 'contract'
  *
  * when focusing pill 'contract' check if person has contract:
  *   |> has contract: 1. method = 'PUT'
  *                    2. populate form with contract data
  *
  *   |>  no contract: 1. method = 'POST'
  *                    2. clearFields()
*/

class Angajat extends React.Component {
  constructor(props) {
    super(props);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);

    this.persoana = React.createRef();
    this.contract = React.createRef();
    this.co       = React.createRef();

    this.state = {
      idpersoana: null,
      idcontract: null,

      show: false,
      modalMessage: '',

      key: 'date-personale',

      method: 'POST',
    };
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

  async onFocusContract() {
    // get id of selected angajat
    const idpersoana = this.persoana.current.getIdOfSelected();
    if (idpersoana === null || idpersoana === -1) {
      this.contract.current.clearFields();
      return;
    }
    else this.contract.current.setState({buttonDisabled: false})

    this.setState({ idpersoana: idpersoana });

    // get angajat with selected id
    const angajat = await fetch(`http://localhost:5000/angajat/${idpersoana}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((res) => res.json())
      .catch((err) => console.error(err));

    const idcontract = angajat.idcontract;
    this.setState({ idcontract: idcontract });

    // if angajat has contract
    if (idcontract !== null) {
      // change onSubmit method to 'PUT'
      this.setState({ method: 'PUT' });

      // fetch data from contract
      const contract = await fetch(
        `http://localhost:5000/contract/${idcontract === null ? '' : idcontract}`,
        {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        }
      )
        .then((res) => res.json())
        .catch((err) => console.error(err));

      //* FILL FORM
      this.contract.current.fillForm(contract);
    } else {
      // selected angajat is missing contract
      // method will be post
      this.setState({ method: 'POST' });
      // fields will be empty
      this.contract.current.clearFields();
      console.log(`${idpersoana} missing contract`);
    }
  }

  onSubmit = async (e) => {
    // field checking if's
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

    // handle contract
    const idpersoana = this.state.idpersoana;
    var idcontract = this.state.idcontract; // will change is person is missing contract

    if (idcontract === null) {
      // does not have idcontract
      let contract = await this.contract.current.onSubmit(e, 'POST', ''); // post/put contract
      idcontract = contract.id;
      this.setState({ idcontract: contract.id });
    } else {
      // has idcontract
      await this.contract.current.onSubmit(e, 'PUT', idcontract); // update existring Contract
    }

    console.log('idpersoana:', idpersoana);
    console.log('idcontract:', idcontract);
    if (typeof idpersoana === 'number' && typeof idcontract === 'number') {
      let angajat_body = {
        idpersoana: idpersoana,
        idcontract: idcontract,
        co: null,
        cm: null,
      };
      // update angajat in database
      const angajat = await fetch(`http://localhost:5000/angajat/${idpersoana}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(angajat_body),
      })
        .then((res) => res.json())
        .catch((err) => console.log(err.message));

      console.log('idangajat:', angajat.idpersoana);

      if (typeof angajat.idpersoana === 'number') {
        this.setState({
          key: 'date-personale',
          show: true,
          modalMessage:
            this.state.method === 'POST'
              ? 'Angajat adaugat cu succes ✔'
              : 'Datele angajatului actualizate 📝',
        });
        this.persoana.current.clearFields(true);
        this.contract.current.clearFields();
        this.setState({
          idpersoana: null,
          idcontract: null,
        });
        window.scrollTo(0, 0);
      }
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
                if (key === 'contract') this.onFocusContract();
                else if(key === 'co') this.co.current.setAngajat(this.state.angajat)
              }}
            >
              <Tab eventKey="date-personale" title="Date Personale">
                <Row className="m-0 p-0">
                  <Col md={12} className="m-0 p-0">
                    <EditPersoana ref={this.persoana} />
                    <Button
                      className="float-right"
                      variant="outline-primary"
                      onClick={() => {
                        this.setState({ key: 'contract' });
                        window.scrollTo(0, 0);
                        this.onFocusContract();
                      }}
                    >
                      Următor
                    </Button>
                  </Col>
                </Row>
              </Tab>
              
              <Tab eventKey="contract" title="Contract de munca">
                <Contract ref={this.contract} asChild idcontract={this.state.idcontract}/>
                <Row className="m-0 p-0">
                  <Col md={12} className="m-0 p-0 ">
                    <Button
                      className="float-right"
                      variant="outline-primary"
                      onClick={() => {
                        this.setState({ key: 'co' });
                        window.scrollTo(0, 0);
                        this.onFocusContract();
                      }}
                    >
                      Următor
                    </Button>
                  </Col>
                </Row>
              </Tab>
              
              <Tab eventKey="co" title="C.O.">
                <Row className="m-0 p-0">
                  <Col md={12} className="m-0 p-0">
                    <ConcediiOdihna ref={this.co} />
                    <Button variant="success" className="float-right m-0" onClick={this.onSubmit}>
                      Salvează modificările
                    </Button>
                  </Col>
                </Row>
              </Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;
