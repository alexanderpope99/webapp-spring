import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import { Trash2 } from 'react-feather';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';

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
      date: '',
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
      .catch((err) => console.error(err));
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
            <th>
              <div className="d-inline-flex">
                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
                        {...bindTrigger(popupState)}
                      >
                        <Trash2 fontSize="small" />
                      </Button>
                      <Popover
                        {...bindPopover(popupState)}
                        anchorOrigin={{
                          vertical: 'bottom',
                          horizontal: 'center',
                        }}
                        transformOrigin={{
                          vertical: 'top',
                          horizontal: 'center',
                        }}
                      >
                        <Box p={2}>
                          <Typography>Sigur ștergeți parametrii respectivi?</Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteParametrii(par.id);
                            }}
                            className="mt-2 "
                          >
                            Da
                          </Button>
                          <Button
                            variant="outline-persondary"
                            onClick={popupState.close}
                            className="mt-2"
                          >
                            Nu
                          </Button>
                        </Box>
                      </Popover>
                    </div>
                  )}
                </PopupState>
              </div>
            </th>
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
      .then((res) => res.data);

    // console.log(parametriiSalarii);

    this.state.parametriiSalarii = parametriiSalarii;

    this.setState({
      salariumin: parametriiSalarii[0].salariumin,
      salariuminstudiivechime: parametriiSalarii[0].salariuminstudiivechime,
      salariumediubrut: parametriiSalarii[0].salariumediubrut,
      impozit: parametriiSalarii[0].impozit,
      cas: parametriiSalarii[0].cas,
      cass: parametriiSalarii[0].cass,
      cam: parametriiSalarii[0].cam,
      valtichet: parametriiSalarii[0].valtichet,
      date: parametriiSalarii[0].date,
    });

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
          date: this.state.date,
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
                  onClick={() => this.setState({ date: this.getDate(), show: true })}
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
                      <th>Începând cu</th>
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
