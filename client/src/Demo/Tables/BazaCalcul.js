import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import Add from '@material-ui/icons/Add';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { getAngajatSel } from '../Resources/angajatsel';
import months from '../Resources/months';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class BazaCalcul extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.addBazaCalcul = this.addBazaCalcul.bind(this);
    this.updateBazaCalcul = this.updateBazaCalcul.bind(this);
    this.editBazaCalcul = this.editBazaCalcul.bind(this);
    this.deleteBazaCalcul = this.deleteBazaCalcul.bind(this);
    this.handleClose = this.handleClose.bind(this);
		this.handleCloseConfirm = this.handleCloseConfirm.bind(this);
		this.renderBazaCalcul = this.renderBazaCalcul.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajatsel: getAngajatSel(),
      bazacalcul: [],
      bazacalculComponent: null,

      isEdit: false,

      // confirm modal
      showConfirm: false,
      modalMessage: '',

      // add/edit modal
      show: false,
			id: null,
			idangajat: null,
			an: '',
			luna: '',
			salariurealizat: '',
			zilelucrate: ''
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  updateAngajatSel() {
    this.setState({ angajatsel: getAngajatSel() });
  }

  async addBazaCalcul() {
    const bazacalcul_body = {
			idangajat: this.state.angajatsel.idpersoana,
			an: this.state.an,
			luna: this.state.luna,
			salariurealizat: this.state.salariurealizat,
			zilelucrate: this.state.zilelucrate
    };

    let ok = await axios
      .post(`${server.address}/bazacalcul`, bazacalcul_body, { headers: authHeader() })
      .then((res) => res.status === 200)
			.catch((err) => console.error(err));
			
    if (ok) {
      await this.handleClose();
      this.setState(
        {
          showConfirm: true,
          modalMessage: 'Bază calcul adaugată pentru ' + this.state.angajatsel.numeintreg,
        },
        this.onRefresh
      );
    }
  }

  async updateBazaCalcul(idbazacalcul) {
    const bazacalcul_body = {
			idangajat: this.state.angajatsel.idpersoana,
			an: this.state.an,
			luna: this.state.luna,
			salariurealizat: this.state.salariurealizat,
			zilelucrate: this.state.zilelucrate
    };

    const ok = await axios
      .put(`${server.address}/bazacalcul/${idbazacalcul}`, bazacalcul_body, {
        headers: authHeader(),
      })
      .then((res) => res.status === 200)
      .catch((err) => console.error(err));

    if (ok) {
      this.onRefresh();
      await this.handleClose();
      this.setState({
        showConfirm: true,
        modalMessage: 'Bază calcul actualizată',
      });
    }
  }

  async editBazaCalcul(bc) {
    this.setState({
      isEdit: true,
      show: true,

      id: bc.id,
			idangajat: bc.idangajat,
			an: bc.an,
			luna: bc.luna,
			salariurealizat: bc.salariurealizat,
			zilelucrate: bc.zilelucrate
    });
  }

  async deleteBazaCalcul(idbazacalcul) {
    axios
      .delete(`${server.address}/bazacalcul/${idbazacalcul}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  // function to create react component with fetched data
  renderBazaCalcul() {
    this.setState({
      bazacalculComponent: this.state.bazacalcul.map((bc, index) => {
        return (
          <tr key={bc.id}>
						<th>{bc.an}</th>
						<th>{months[bc.luna-1]}</th>
						<th>{bc.zilelucrate}</th>
						<th>{bc.salariurealizat}</th>
            <th>
              <Row>
                <Button
                  onClick={() => this.editBazaCalcul(bc)}
                  variant="outline-secondary"
                  className="m-1 p-1 rounded-circle border-0"
                >
                  <Edit fontSize="small" />
                </Button>

                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-1 p-1 rounded-circle border-0"
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
                          <Typography>
                            Confirmare ștergere
                          </Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deleteBazaCalcul(bc.id);
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
              </Row>
            </th>
          </tr>
        );
      }),
    });
  }

  async onRefresh() {
    if (this.state.angajatsel) {
      const bazacalcul = await axios
        .get(`${server.address}/bazacalcul/ida=${this.state.angajatsel.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
				.catch((err) => console.error(err));
      if (bazacalcul) {
        this.setState(
          {
            bazacalcul: bazacalcul,
          },
          this.renderBazaCalcul
        );
      }
    } else {
      this.setState({
        bazacalcul: [],
        bazacalculComponent: null,
      });
    }
  }

  async onSubmit(e) {
    e.preventDefault();
    if (this.state.isEdit) this.updateBazaCalcul(this.state.id);
    else this.addBazaCalcul();
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      nume: '',
      prenume: '',
      cnp: '',
      datanasterii: '',
      grad: '',
      gradinvaliditate: 'valid',
      intretinut: false,
      coasigurat: false,
      idangajat: null,
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }

  render() {
    return (
      <Aux>
        {/* add/edit modal */}
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Bază calcul</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={this.addBazaCalcul}>
              <Row>
                <Col md={12}>
                  <Form.Group>
                    <Form.Label>An</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.an}
                      onChange={(e) => this.setState({ an: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Luna</Form.Label>
                    <Form.Control
                      type="text"
                      value={this.state.luna}
                      onChange={(e) => this.setState({ luna: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Zile Lucrate</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.zilelucrate}
                      onChange={(e) => this.setState({ zilelucrate: e.target.value })}
                    />
                  </Form.Group>
                  <Form.Group>
                    <Form.Label>Salariu realizat</Form.Label>
                    <Form.Control
                      type="number"
                      value={this.state.salariurealizat}
                      onChange={(e) => this.setState({ salariurealizat: e.target.value })}
                    />
                  </Form.Group>
                  </Col>
              </Row>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.onSubmit} type="submit">
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* confirm modal */}
        <Modal show={this.state.showConfirm} onHide={this.handleCloseConfirm}>
          <Modal.Header closeButton>
            <Modal.Title>Bază calcul</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.modalMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleCloseConfirm}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Bază calcul</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Add className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>An</th>
                      <th>Luna</th>
                      <th>Zile Lucrate</th>
                      <th>Salariu realizat</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.bazacalculComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default BazaCalcul;
