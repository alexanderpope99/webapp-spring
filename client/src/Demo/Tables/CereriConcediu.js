import React from 'react';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import { Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';

class CereriConcediu extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.updateCereri = this.updateCereri.bind(this);

    this.state = {
      cereriConcediu: [],
      cereriConcediuComponent: null,
      pentru: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
      show: false,
    };
  }

  componentDidMount() {
    this.onRefresh();
  }

  deleteCereri(id) {
    axios
      .delete(`${server.address}/cereriConcediu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      })
      .catch((err) => console.error(err));
  }

  // function to render in react
  async renderCereriConcediu() {
    // console.log('render called');
    this.setState({
      cereriConcediuComponent: this.state.cereriConcediu.map((cer, index) => {
        // for (let key in soc) {
        //   if (!soc[key]) soc[key] = '-';
        // }
        // console.log(soc);
        return (
          <tr key={cer.id}>
            <th>{cer.pentru || '-'}</th>
            <th>{cer.dela.substring(0, 10) || '-'}</th>
            <th>{cer.panala.substring(0, 10) || '-'}</th>
            <th>{cer.tip || '-'}</th>
            <th>{cer.motiv || '-'}</th>
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
                        <DeleteIcon fontSize="small" />
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
                          <Typography>Sigur ștergeți cererea respectivă?</Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteCereri(cer.id);
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

  async onRefresh() {
    // e.preventDefault();

    let cereriConcediu = await axios
      .get(`${server.address}/cerericoncediu/`, { headers: authHeader() })
      .then((res) => res.data);

    console.log(cereriConcediu);

    this.state.cereriConcediu = cereriConcediu;

    this.setState({
      pentru: cereriConcediu[0].pentru,
      dela: cereriConcediu[0].dela,
      panala: cereriConcediu[0].panala,
      tip: cereriConcediu[0].tip,
      motiv: cereriConcediu[0].motiv,
    });

    console.log('onRefresh called');
    this.renderCereriConcediu();
  }

  async updateCereri() {
    await axios
      .post(
        `${server.address}/cerericoncediu`,
        {
          pentru: this.state.pentru,
          dela: this.state.dela,
          panala: this.state.panala,
          tip: this.state.tip,
          motiv: this.state.motiv,
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
            <Modal.Title>Cereri Concediu</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="incepandcu">
                <Form.Label>Începând cu</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.dela}
                  onChange={(e) => {
                    this.setState({ dela: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="panala">
                <Form.Label>Până la</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.panala}
                  onChange={(e) => {
                    this.setState({ panala: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="tip">
                <Form.Label>Tip</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.tip}
                  onChange={(e) => {
                    this.setState({ tip: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="motiv">
                <Form.Label>Motiv</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.motiv}
                  onChange={(e) => {
                    this.setState({ motiv: e.target.value });
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
                <Card.Title as="h5">Cereri Concediu</Card.Title>
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
                      <th>Până la</th>
                      <th>Tip</th>
                      <th>Motiv</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.cereriConcediuComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default CereriConcediu;
