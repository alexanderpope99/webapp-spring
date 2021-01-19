import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { formatDate } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class SarbatoriTabelView extends React.Component {
  constructor() {
    super();

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.deleteSarbatoare = this.deleteSarbatoare.bind(this);
    this.setCurrentYear = this.setCurrentYear.bind(this);

    this.state = {
      isEdit: false,

      sarbatori: [],
      sarbatoriComponent: null,

      an: '',

      // edit modal:
      show: false,
      id: '',
      dela: '',
      panala: '',
      nume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    this.setCurrentYear();
    this.fillTable();
  }

  setCurrentYear() {
    let today = new Date();
    let an = today.getFullYear();

    this.setState({ an: an });
  }

  getAniCuSarbatori(sarbatori) {
    var ani_cu_sarbatori = new Set();
    sarbatori.forEach((sarbatoare) => {
      ani_cu_sarbatori.add(sarbatoare.dela.substring(0, 4));
      // console.log(sarbatoare);
    });
    return [...ani_cu_sarbatori];
  }

  async fillTable() {
    //? fetch must be with idcontract
    const sarbatori = await axios
      .get(`${server.address}/sarbatori`, { headers: authHeader() })
      .then((res) => (res.status !== 200 ? null : res.data))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua sărbătorile\n' + err.response.data.message,
        })
      );

    if (sarbatori) {
      this.setState(
        {
          sarbatori: sarbatori,
        },
        this.renderSarbatori
      );
    } else {
      this.setState(
        {
          sarbatori: [],
        },
        this.renderSarbatori
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      show: false,
      dela: '',
      panala: '',
      nume: '',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    });
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
        // reset data
        id: '',
        dela: '',
        panala: '',
        nume: '',
      });
  }

  async deleteSarbatoare(id) {
    await axios
      .delete(`${server.address}/sarbatori/${id}`, { headers: authHeader() })
      .then(this.fillTable)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge sărbătoarea\n' + err.response.data.message,
        })
      );
  }

  async onSubmit(e) {
    e.preventDefault();

    const sarbatoare_body = {
      dela: this.state.dela || null,
      panala: this.state.panala || null,
      nume: this.state.nume || null,
      // in DB also has sporuripermanente
    };
    let ok = false;
    if (this.state.isEdit) {
      ok = await axios
        .put(`${server.address}/sarbatori/${this.state.id}`, sarbatoare_body, {
          headers: authHeader(),
        })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut actualiza sărbătoarea\n' + err.response.data.message,
          })
        );
    } else {
      ok = await axios
        .post(`${server.address}/sarbatori`, sarbatoare_body, { headers: authHeader() })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut adăuga sărbătoarea\n' + err.response.data.message,
          })
        );
    }

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal
      this.setState({
        show_confirm: true,
        modalMessage:
          'Sărbătoare ' +
          this.state.nume +
          (this.state.isEdit ? ' actualizată' : ' adăugată') +
          ' 💾',
      });

      this.fillTable();
    }
  }

  editSarbatoare(sarbatoare) {
    this.setState({
      isEdit: true,
      show: true,
      id: sarbatoare.id,
      dela: sarbatoare.dela,
      panala: sarbatoare.panala,
      nume: sarbatoare.nume,
    });
  }

  onChangeAn(an) {
    this.setState({ an: an }, this.renderSarbatori);
  }
  // function to create react component with fetched data
  renderSarbatori() {
    this.setState({
      // eslint-disable-next-line array-callback-return
      sarbatoriComponent: this.state.sarbatori.map((sarbatoare, index) => {
        for (let key in sarbatoare) if (!sarbatoare[key]) sarbatoare[key] = '-';
        if (sarbatoare.dela.includes(this.state.an)) {
          return (
            <tr key={sarbatoare.id}>
              <th>{formatDate(sarbatoare.dela.substring(0, 10))}</th>
              <th>{formatDate(sarbatoare.panala.substring(0, 10))}</th>
              <th>{sarbatoare.nume}</th>
            </tr>
          );
        }
      }),
    });
  }

  render() {
    const aniCuSarbatori = this.getAniCuSarbatori(this.state.sarbatori).map((an) => (
      <option key={an}>{an}</option>
    ));

    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {/* ADD/EDIT MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Date sărbătoare legală</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.onSubmit}>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  value={this.state.dela}
                  onChange={(e) => {
                    this.setState({ dela: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="panala">
                <Form.Label>Până la (inclusiv)</Form.Label>
                <Form.Control
                  required
                  type="date"
                  value={this.state.panala}
                  onChange={(e) => {
                    this.setState({ panala: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="nume">
                <Form.Label>Nume</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.nume}
                  onChange={(e) => {
                    this.setState({ nume: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.onSubmit}>
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

        {/* PAGE CONTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Sărbători</Card.Title>
              </Card.Header>
              <Card.Body>
                <Form.Group as={Col} sm="3">
                  <Form.Control
                    as="select"
                    value={this.state.an}
                    onChange={(e) => this.onChangeAn(e.target.value)}
                  >
                    {aniCuSarbatori}
                  </Form.Control>
                </Form.Group>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Începând cu (inclusiv) ↓</th>
                      <th>Până la (inclusiv)</th>
                      <th>Nume</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.sarbatoriComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default SarbatoriTabelView;
