import React from 'react';
import {
  Row,
  Col,
  Card,
  Table,
  Button,
  OverlayTrigger,
  Tooltip,
  Modal,
  Form,
} from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Edit from '@material-ui/icons/Edit';
import Refresh from '@material-ui/icons/Refresh';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
class CMTabel extends React.Component {
  constructor(props) {
    super(props);

    this.handleClose = this.handleClose.bind(this);
    this.fillTable = this.fillTable.bind(this);
    this.addCM = this.addCM.bind(this);
    this.deleteCM = this.deleteCM.bind(this);

    this.state = {
      angajat: props.angajat,

      cm: [],
      cmComponent: null,

      // cm details:
      show: false,
      dela: '',
      panala: '',
      continuare: false,
      datainceput: false,
      serienrcertificat: '',
      dataeliberare: '',
      codurgenta: '',
      procent: '',
      codboalainfcont: '',
      bazacalcul: '',
      bazacalculplafonata: '',
      zilebazacalcul: '',
      mediezilnica: '',
      zilefirma: '',
      indemnizatiefirma: '',
      zilefnuass: '',
      indemnizatiefnuass: '',
      locprescriere: '',
      nravizmedic: '',
      codboala: '',
      urgenta: false,
      conditii: '',
      idcontract: null,

      // succes modal:
      show_confirm: false,
      modalMessage: '',
    };
  }
  
  clearCM() {
    this.setState({
      tip: 'Concediu medical',
      dela: '',
      panala: '',
      continuare: false,
      datainceput: false,
      serienrcertificat: '',
      dataeliberare: '',
      codurgenta: '',
      procent: '',
      codboalainfcont: '',
      bazacalcul: '',
      bazacalculplafonata: '',
      zilebazacalcul: '',
      mediezilnica: '',
      zilefirma: '',
      indemnizatiefirma: '',
      zilefnuass: '',
      indemnizatiefnuass: '',
      locprescriere: '',
      nravizmedic: '',
      codboala: '',
      urgenta: false,
      conditii: '',
      idcontract: null,
    })
  }

  // TODO
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

  // TODO
  async fillTable() {
    if (typeof this.state.angajat === 'undefined') return;
    if (this.state.angajat.idcontract === null) {
      this.setState({ cm: [] }, this.renderCM);

      return;
    }
    //? fetch must be with idcontract
    const cm = await fetch(`http://localhost:5000/cm/idc=${this.state.angajat.idcontract}`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    })
      .then((cm) => (cm.status !== 200 ? null : cm.json()))
      .catch((err) => console.error('err', err));

    if (cm !== null) {
      this.setState(
        {
          cm: cm,
        },
        this.renderCM
      );
    } else {
      this.setState(
        {
          cm: [],
        },
        this.renderCM
      );
    }
  }

  // TODO
  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
      });
  }

  //* Works
  async deleteCM(id) {
    await fetch(`http://localhost:5000/cm/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then(this.fillTable)
      .catch((err) => console.error(err));
  }

  // TODO
  async addCM() {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    let { angajat, cm, cmComponent, show, show_confirm, modalMessage, ...cm_body } = this.state;

    console.log(cm_body);
    return;

    let ok = await fetch('http://localhost:5000/cm', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cm_body),
    })
      .then((res) => res.ok)
      .catch((err) => console.error('err:', err));

    if (ok) {
      // close add modal
      this.handleClose();
      // open confirm modal <- closes on OK button
      this.setState({
        show_confirm: true,
        modalMessage: 'Concediu medical adăugat cu succes 💾',
      });
      this.fillTable();
      this.clearCM();
    }
  }

  // function to create react component with fetched data
  renderCM() {
    this.setState({
      cmComponent: this.state.cm.map((cm, index) => {
        for (let key in cm) {
          if (cm[key] === 'null' || cm[key] === null) cm[key] = '-';
        }
        return (
          <tr key={cm.id}>
            <th>{cm.dela === null ? '' : cm.dela.substring(0, 10)}</th>
            <th>{cm.panala === null ? '' : cm.panala.substring(0, 10)}</th>
            <th>{cm.continuare ? 'Da' : 'Nu'}</th>
            <th>{cm.datainceput === null ? '' : cm.datainceput.substring(0, 10)}</th>
            <th>{cm.serienrcertificat === null ? '' : cm.serienrcertificat}</th>
            <th>{cm.dataeliberare === null ? '' : cm.dataeliberare.substring(0, 10)}</th>
            <th>{cm.codurgenta === null ? '' : cm.codurgenta}</th>
            <th>{cm.procent === null ? '' : cm.procent}</th>
            <th>{cm.codboalainfcont === null ? '' : cm.codboalainfcont}</th>
            <th>{cm.bazacalcul === null ? '' : cm.bazacalcul}</th>
            <th>{cm.bazacalculplafonata === null ? '' : cm.bazacalculplafonata}</th>
            <th>{cm.zilebazacalcul === null ? '' : cm.zilebazacalcul}</th>
            <th>{cm.mediezilnica === null ? '' : cm.mediezilnica}</th>
            <th>{cm.zilefirma === null ? '' : cm.zilefirma}</th>
            <th>{cm.indemnizatiefirma === null ? '' : cm.indemnizatiefirma}</th>
            <th>{cm.zilefnuass === null ? '' : cm.zilefnuass}</th>
            <th>{cm.indemnizatiefnuass === null ? '' : cm.indemnizatiefnuass}</th>
            <th>{cm.locprescriere === null ? '' : cm.locprescriere}</th>
            <th>{cm.nravizmedic === null ? '' : cm.nravizmedic}</th>
            <th>{cm.codboala === null ? '' : cm.codboala}</th>
            <th>{cm.urgenta === null ? '' : cm.urgenta}</th>
            <th>{cm.conditii === null ? '' : cm.conditii}</th>

            <th className="d-inline-flex flex-row justify-content-around">
              //! ADD EDIT BUTTON HERE
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
                            this.deleteCM(cm.id);
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
            <Button variant="primary" onClick={this.addCM}>
              Adaugă
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CMNFIRM Modal */}
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

        {/* PAGE CMNTENTS */}
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Listă concedii de odihnă</Card.Title>
                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
                  overlay={
                    <Tooltip id="refresh-button" style={{ opacity: '.4' }}>
                      Refresh
                    </Tooltip>
                  }
                >
                  <Button
                    variant="outline-info"
                    size="sm"
                    style={{ fontSize: '1.25rem', float: 'right' }}
                    onClick={this.fillTable}
                  >
                    <Refresh className="m-0 p-0" />
                    {/* ↺ */}
                  </Button>
                </OverlayTrigger>

                <Button
                  variant="outline-info"
                  className="float-right"
                  onClick={() => this.setState({ show: true })}
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
                      <th>Continuare</th>
                      <th>Data început</th>
                      <th>Serie si nr. certificat</th>
                      <th>Data eliberare</th>
                      <th>Cod urgență</th>
                      <th>Procent</th>
                      <th>Bază calcul</th>
                      <th>Bază calcul fără plată</th>
                      <th>Zile bază calul</th>
                      <th>Medie zilnică</th>
                      <th>Zile firmă</th>
                      <th>Indemnizație firmă</th>
                      <th>Zile FNUASS</th>
                      <th>Indemnizație FNUASS</th>
                      <th>Nr. aviz medic</th>
                      <th>Loc prescriere</th>
                      <th>Cod boală</th>
                      <th>Cod boală infecțioasă/contagioasă</th>
                      <th>Urgență</th>
                      <th>Condiții</th>
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

export default CMTabel;
