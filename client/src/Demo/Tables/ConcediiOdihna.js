import React from 'react';
import {
  Row,
  Col,
  Card,
  Table,
  Button,
  Modal,
  Form,
} from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
class COTabel extends React.Component {
  constructor(props) {
    super(props);

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.resetModals = this.resetModals.bind(this);
    this.addCO = this.addCO.bind(this);
    this.deleteCO = this.deleteCO.bind(this);

    this.state = {
      angajat: props.angajat,

      co: [],
      coComponent: null,

      // add modal:
      show: false,
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }

  setAngajat(angajat) {
    this.setState(
      {
        angajat: angajat,
      },
      () => this.fillTable()
    );
  }

  componentDidMount() {
    this.fillTable();
  }

  async fillTable() {
    if (typeof this.state.angajat === 'undefined') return;
    if (this.state.angajat.idcontract === null) {
      this.setState(
        {
          // show_confirm: true,
          // modalMessage: 'Angajatul nu are un contract.',
          co: [],
        },
        this.renderCO
      );

      return;
    }
    //? fetch must be with idcontract
    const co = await fetch(`http://localhost:5000/co/idc=${this.state.angajat.idcontract}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((co) => (co.status !== 200 ? null : co.json()))
      .catch((err) => console.error('err', err));

    if (co !== null) {
      this.setState(
        {
          co: co,
        },
        this.renderCO
      );
    } else {
      this.setState(
        {
          co: [],
        },
        this.renderCO
      );
    }
  }

  resetModals() {
    this.setState({
      // add modal:
      show: false,
      dela: '',
      panala: '',
      tip: 'Concediu de odihnă',

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
        dela: '',
        panala: '',
        tip: 'Concediu de odihnă',
      });
  }

  async deleteCO(id) {
    await fetch(`http://localhost:5000/co/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  async addCO() {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    const co_body = {
      dela: this.state.dela,
      panala: this.state.panala,
      tip: this.state.tip,
      idcontract: this.state.angajat.idcontract,
      // in DB also has sporuripermanente
    };

    let ok = await fetch('http://localhost:5000/co', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(co_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: this.state.tip + ' adăugat cu succes 💾',
      });
      this.fillTable();
    }
  }

  // function to create react component with fetched data
  renderCO() {
    this.setState({
      coComponent: this.state.co.map((co, index) => {
        for (let key in co) {
          if (co[key] === 'null' || co[key] === null) co[key] = '-';
        }
        return (
          <tr key={co.id}>
            <th>{co.dela.substring(0, 10)}</th>
            <th>{co.panala.substring(0, 10)}</th>
            <th>{co.tip}</th>
            <th className="d-inline-flex flex-row justify-content-around">
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
                        <Typography>Sigur ștergeți concediul?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteCO(co.id);
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
            </th>
          </tr>
        );
      }),
    });
  }

  render() {
    return (
      <Aux>
        {/* // ADD MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Concediu de odihnă</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
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
              <Form.Group id="tip">
                <Form.Label>Motiv</Form.Label>
                <Form.Control
                  required
                  as="select"
                  value={this.state.tip}
                  onChange={(e) => {
                    this.setState({ tip: e.target.value });
                  }}
                >
                  <option>Concediu de odihnă</option>
                  <option>Concediu fără plată</option>
                  <option>Concediu pentru situații speciale</option>
                  <option>Concediu pentru studii</option>
                </Form.Control>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="primary" onClick={this.addCO}>
              Adaugă
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
              <Card.Header>
                <Card.Title as="h5">Listă concedii de odihnă</Card.Title>
                  <Button
                    variant={
                      typeof this.state.angajat === 'undefined' ? 'outline-dark' : 'outline-primary'
                    }
                    disabled={typeof this.state.angajat === 'undefined'}
                    size="sm"
                    style={{ fontSize: '1.25rem', float: 'right' }}
                    onClick={this.fillTable}
                  >
                    <Refresh className="m-0 p-0" />
                    {/* ↺ */}
                  </Button>

                <Button
                  variant={
                      typeof this.state.angajat === 'undefined' ? 'outline-dark' : 'outline-primary'
                    }
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
                  disabled={typeof this.state.angajat === 'undefined'}
                >
                  Adaugă concediu
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Începând cu (inclusiv)</th>
                      <th>Până la (inclusiv)</th>
                      <th>Motiv</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.coComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default COTabel;
