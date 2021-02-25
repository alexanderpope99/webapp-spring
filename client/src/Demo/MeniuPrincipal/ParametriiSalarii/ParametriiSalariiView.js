import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Toast } from 'react-bootstrap';

import Aux from '../../../hoc/_Aux';
import { server } from '../../Resources/server-address';
import axios from 'axios';
import authHeader from '../../../services/auth-header';

class ParametriiSalariiView extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.updateParametrii = this.updateParametrii.bind(this);

    this.state = {
      parametriiSalarii: [],
      parametriiSalariiComponent: null,
      salariumin: '',
      salariuminstudiivechime: '',
      salariumediubrut: '',
      impozit: '',
      cas: '',
      cass: '',
      cam: '',
      valtichet: '',
	  tva:'',
      show: false,
      date: '',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    this.onRefresh();
  }

  deleteParametrii(id) {
    axios
      .delete(`${server.address}/parametriisalariu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      })
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge taxele și impozitele: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'),
        })
      );
  }

  // function to render in react
  async renderParametriiSalarii() {
    // console.log('render called');
    this.setState({
      parametriiSalariiComponent: this.state.parametriiSalarii.map((par, index) => {
        // for (let key in soc) {
        //   if (!soc[key]) soc[key] = '-';
        // }
        // console.log(soc);
        return (
          <tr key={par.id}>
            <th>{par.date.substring(0, 10) || '-'}</th>
            <th>{par.salariumin || '-'}</th>
            <th>{par.salariuminstudiivechime || '-'}</th>
            <th>{par.salariumediubrut || '-'}</th>
            <th>{par.impozit || '-'}</th>
            <th>{par.cas || '-'}</th>
            <th>{par.cass || '-'}</th>
            <th>{par.cam || '-'}</th>
            <th>{par.valtichet || '-'}</th>
            <th>{par.tva || '-'}</th>
          </tr>
        );
      }),
    });
  }

  getDate() {
    var today = new Date();
    var dd = today.getDate();

    var mm = today.getMonth() + 1;
    var yyyy = today.getFullYear();
    if (dd < 10) {
      dd = '0' + dd;
    }

    if (mm < 10) {
      mm = '0' + mm;
    }
    return yyyy + '-' + mm + '-' + dd;
  }

  async onRefresh() {
    // e.preventDefault();

    let parametriiSalarii = await axios
      .get(`${server.address}/parametriisalariu/`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage:
            'Nu am putut prelua taxele și impozitele din baza de date: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'),
        })
      );

    // console.log(parametriiSalarii);

    this.state.parametriiSalarii = parametriiSalarii;

	if(parametriiSalarii.length!==0){
    this.setState({
      salariumin: parametriiSalarii[0].salariumin,
      salariuminstudiivechime: parametriiSalarii[0].salariuminstudiivechime,
      salariumediubrut: parametriiSalarii[0].salariumediubrut,
      impozit: parametriiSalarii[0].impozit,
      cas: parametriiSalarii[0].cas,
      cass: parametriiSalarii[0].cass,
      cam: parametriiSalarii[0].cam,
      valtichet: parametriiSalarii[0].valtichet,
      tva: parametriiSalarii[0].tva,
      date: parametriiSalarii[0].date,
    });

    this.renderParametriiSalarii();
	}
}

  async updateParametrii() {
    await axios
      .post(
        `${server.address}/parametriisalariu`,
        {
          salariumin: this.state.salariumin,
          salariuminstudiivechime: this.state.salariuminstudiivechime,
          salariumediubrut: this.state.salariumediubrut,
          impozit: this.state.impozit,
          cas: this.state.cas,
          cass: this.state.cass,
          cam: this.state.cam,
          valtichet: this.state.valtichet,
          tva: this.state.tva,
          date: this.state.date,
        },
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut adăuga taxe și impozite noi: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'),
        })
      );
    this.onRefresh();
    this.setState({ show: false });
  }

  render() {
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
        <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
          <Modal.Header closeButton>
            <Modal.Title>Taxe și Impozite</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="incepandcu">
                <Form.Label>Începând cu</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.date}
                  onChange={(e) => {
                    this.setState({ date: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumin">
                <Form.Label>Sal. Minim (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariumin}
                  onChange={(e) => {
                    this.setState({ salariumin: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariuminstudiivechime">
                <Form.Label>Sal. Minim - SS,V (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariuminstudiivechime}
                  onChange={(e) => {
                    this.setState({ salariuminstudiivechime: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>Sal. Mediu Brut (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.salariumediubrut}
                  onChange={(e) => {
                    this.setState({ salariumediubrut: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="impozit">
                <Form.Label>Impozit (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.impozit}
                  onChange={(e) => {
                    this.setState({ impozit: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cas">
                <Form.Label>CAS (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cas}
                  onChange={(e) => {
                    this.setState({ cas: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cass">
                <Form.Label>CASS (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cass}
                  onChange={(e) => {
                    this.setState({ cass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>CAM (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.cam}
                  onChange={(e) => {
                    this.setState({ cam: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="valtichet">
                <Form.Label>Val. tichet (RON)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.valtichet}
                  onChange={(e) => {
                    this.setState({ valtichet: e.target.value });
                  }}
                />
              </Form.Group>
			  <Form.Group id="tva">
                <Form.Label>TVA (%)</Form.Label>
                <Form.Control
                  type="number"
                  value={this.state.tva}
                  onChange={(e) => {
                    this.setState({ tva: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.updateParametrii}>
              Confirmă
            </Button>
          </Modal.Footer>
        </Modal>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Taxe și impozite</Card.Title>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Începând cu</th>
                      <th>Sal. Minim (RON)</th>
                      <th>Sal. Minim - SS,V (RON)</th>
                      <th>Sal. Mediu Brut (RON)</th>
                      <th>Impozit (%)</th>
                      <th>CAS (%)</th>
                      <th>CASS (%)</th>
                      <th>CAM (%)</th>
                      <th>Val tichet (RON)</th>
                      <th>TVA (%)</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.parametriiSalariiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default ParametriiSalariiView;
