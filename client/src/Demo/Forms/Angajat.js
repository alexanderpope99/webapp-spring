import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
// import Persoana from '../UIElements/Forms/Persoana';
import EditPersoana from '../Edit/EditPersoana';
import Contract from '../UIElements/Forms/Contract';
import ConcediiOdihna from '../Tables/ConcediiOdihna';
// import AddContract from './AddContract';

/*
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
    // this.onSubmit = this.onSubmit.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.componentDidMount = this.componentDidMount.bind(this);

    this.persoana = React.createRef();
    this.contract = React.createRef();
    this.co = React.createRef();

    this.state = {
      angajat: null,
      idpersoana: null,
      idcontract: null,

      show: false,
      modalMessage: '',

      key: 'date-personale',

    };

    // window.scrollTo(0, 0);
  }

  componentDidMount() {
    window.scrollTo(0, 0);
  }

  componentDidUpdate() {
    window.scrollTo(0, 0);
  }

  handleClose() {
    this.setState({
      show: false,
      modalMessage: '',
    });
    window.scrollTo(0, 0);
  }

  async getSelectedAngajatData() {
    // get id of selected angajat
    const idpersoana = this.persoana.current.getIdOfSelected();
    if (idpersoana === null || idpersoana === -1) {
      this.contract.current.clearFields();
      return;
    } else this.contract.current.setState({ buttonDisabled: false });

    // this.setState({ idpersoana: idpersoana });

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
    this.contract.current.fillForm(contract, idpersoana); // here it gets the idcontract and idangajat
  }

  async onFocusCO() {
    // can also work with state.angajat
    const angajat = await this.getSelectedAngajatData();
    if (typeof angajat === 'undefined') return;
    if(angajat.idcontract === null) {
      this.setState({
        show: true,
        modalMessage: "Pentru concedii, angajatul are nevoie de un contract de muncă.",
        key: 'contract'
      })
    }

    this.co.current.setAngajat(angajat);
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
              Date angajat
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
            </Tabs>
            <Button onClick={() => window.scrollTo(0, 0)}>TO TOP</Button>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Angajat;

// onSubmit = async (e) => {
//   // field checking if's
//   if (!this.persoana.current.hasRequired()) {
//     this.setState({
//       key: 'date-personale',
//       show: true,
//       modalMessage: 'Persoana trebuie să aibă Nume și Prenume.',
//     });
//     window.scrollTo(0, 0);
//     return;
//   }
//   if (!this.contract.current.hasRequired()) {
//     this.setState({
//       key: 'contract',
//     });
//     return;
//   }

//   // handle contract
//   const idpersoana = this.state.idpersoana;
//   var idcontract = this.state.idcontract; // will change is person is missing contract

//   if (idcontract === null) {
//     // does not have idcontract
//     let contract = await this.contract.current.onSubmit(e, 'POST', ''); // post/put contract
//     idcontract = contract.id;
//     this.setState({ idcontract: contract.id });
//   } else {
//     // has idcontract
//     await this.contract.current.onSubmit(e, 'PUT', idcontract); // update existring Contract
//   }

//   console.log('idpersoana:', idpersoana);
//   console.log('idcontract:', idcontract);
//   if (typeof idpersoana === 'number' && typeof idcontract === 'number') {
//     let angajat_body = {
//       idpersoana: idpersoana,
//       idcontract: idcontract,
//       co: null,
//       cm: null,
//     };
//     // update angajat in database
//     const angajat = await fetch(`http://localhost:5000/angajat/${idpersoana}`, {
//       method: 'PUT',
//       headers: { 'Content-Type': 'application/json' },
//       body: JSON.stringify(angajat_body),
//     })
//       .then((res) => res.json())
//       .catch((err) => console.log(err.message));

//     console.log('idangajat:', angajat.idpersoana);

//     if (typeof angajat.idpersoana === 'number') {
//       this.setState({
//         key: 'date-personale',
//         show: true,
//         modalMessage:
//           this.state.method === 'POST'
//             ? 'Angajat adaugat cu succes ✔'
//             : 'Datele angajatului actualizate 📝',
//       });
//       this.persoana.current.clearFields(true);
//       this.contract.current.clearFields();
//       this.setState({
//         idpersoana: null,
//         idcontract: null,
//       });
//       window.scrollTo(0, 0);
//     }
//   }
// };
