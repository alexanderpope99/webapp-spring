import React from 'react';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import { Modal, Form } from 'react-bootstrap';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class ParametriiSalarii extends React.Component {
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
      show: false,
    };
  }

  componentDidMount() {
    this.onRefresh();
  }

  // function to render in react
  async renderParametriiSalarii() {
    // console.log('render called');
    this.setState({
      parametriiSalariiComponent: (
        <tr key={this.state.parametriiSalarii.id}>
          <th>{this.state.parametriiSalarii.salariumin || '-'}</th>
          <th>{this.state.parametriiSalarii.salariuminstudiivechime || '-'}</th>
          <th>{this.state.parametriiSalarii.salariumediubrut || '-'}</th>
          <th>{this.state.parametriiSalarii.impozit || '-'}</th>
          <th>{this.state.parametriiSalarii.cas || '-'}</th>
          <th>{this.state.parametriiSalarii.cass || '-'}</th>
          <th>{this.state.parametriiSalarii.cam || '-'}</th>
          <th>{this.state.parametriiSalarii.valtichet || '-'}</th>
        </tr>
      ),
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
      .get(`${server.address}/parametriisalariu/date/${this.getDate()}`, { headers: authHeader() })
      .then((res) => res.data);

    console.log(parametriiSalarii);

    this.state.parametriiSalarii = parametriiSalarii;

    this.setState({
      salariumin: parametriiSalarii.salariumin,
      salariuminstudiivechime: parametriiSalarii.salariuminstudiivechime,
      salariumediubrut: parametriiSalarii.salariumediubrut,
      impozit: parametriiSalarii.impozit,
      cas: parametriiSalarii.cas,
      cass: parametriiSalarii.cass,
      cam: parametriiSalarii.cam,
      valtichet: parametriiSalarii.valtichet,
    });

    console.log('onRefresh called');
    this.renderParametriiSalarii();
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
          date: this.getDate(),
        },
        { headers: authHeader() }
      )
      .then((res) => res.data);
    this.onRefresh();
    this.setState({ show: false });
  }

  render() {
    return (
      <Aux>
        <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
          <Modal.Header closeButton>
            <Modal.Title>Parametrii Salarii</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="salariumin">
                <Form.Label>Sal. Minim (RON)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.salariumin}
                  onChange={(e) => {
                    this.setState({ salariumin: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariuminstudiivechime">
                <Form.Label>Sal. Minim - SS,V (RON)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.salariuminstudiivechime}
                  onChange={(e) => {
                    this.setState({ salariuminstudiivechime: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>Sal. Mediu Brut (RON)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.salariumediubrut}
                  onChange={(e) => {
                    this.setState({ salariumediubrut: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="impozit">
                <Form.Label>Impozit (%)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.impozit}
                  onChange={(e) => {
                    this.setState({ impozit: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cas">
                <Form.Label>CAS (%)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.cas}
                  onChange={(e) => {
                    this.setState({ cas: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="cass">
                <Form.Label>CASS (%)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.cass}
                  onChange={(e) => {
                    this.setState({ cass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="salariumediubrut">
                <Form.Label>CAM (%)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.cam}
                  onChange={(e) => {
                    this.setState({ cam: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="valtichet">
                <Form.Label>Val. tichet (RON)</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.valtichet}
                  onChange={(e) => {
                    this.setState({ valtichet: e.target.value });
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
                <Card.Title as="h5">Parametrii Salarii</Card.Title>
                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  ↺
                </Button>
                <Button
                  onClick={() => this.setState({ show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  Edit
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Sal. Minim (RON)</th>
                      <th>Sal. Minim - SS,V (RON)</th>
                      <th>Sal. Mediu Brut (RON)</th>
                      <th>Impozit (%)</th>
                      <th>CAS (%)</th>
                      <th>CASS (%)</th>
                      <th>CAM (%)</th>
                      <th>Val tichet (RON)</th>
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

export default ParametriiSalarii;
