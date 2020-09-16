import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form } from 'react-bootstrap';
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
    this.editCM = this.editCM.bind(this);
    this.updateCM = this.updateCM.bind(this);
    this.deleteCM = this.deleteCM.bind(this);

    this.state = {
      angajat: props.angajat,

      cm: [],
      cmComponent: null,

      // cm modal:
      show: false,
      isEdit: false,
      id: '',
      // cm modal fields
      dela: '',
      panala: '',
      continuare: false,
      datainceput: '',
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
      isEdit: false,
      // cm modal fields
      id: '',

      dela: '',
      panala: '',
      continuare: false,
      datainceput: '',
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
    });
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
      this.setState({ cm: [] }, this.renderCM);

      return;
    }
    //? fetch must be with idcontract
    const cm = await fetch(
      `http://192.168.2.159/cm/idc=${this.state.angajat.idcontract}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        // body: JSON.stringify(persoane),
      }
    )
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

  handleClose(confirmWindow) {
    if (confirmWindow)
      this.setState({
        show_confirm: false,
        modalMessage: '',
      });
    else
      this.setState({
        show: false,
        isEdit: false,
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

  async addCM() {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    let {
      angajat,
      cm,
      cmComponent,
      show,
      show_confirm,
      modalMessage,
      ...cm_body
    } = this.state;
    cm_body.idcontract = this.state.angajat.idcontract;

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

  async updateCM() {
    let {
      angajat,
      cm,
      cmComponent,
      show,
      show_confirm,
      modalMessage,
      id,
      isEdit,
      ...cm_body
    } = this.state;
    cm_body.idcontract = this.state.angajat.idcontract;

    let ok = await fetch(`http://localhost:5000/cm/${this.state.id}`, {
      method: 'PUT',
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
        modalMessage: 'Concediu medical actualizat ✔',
      });
      this.fillTable();
      this.clearCM();
    }
  }

  async editCM(cm) {
    if (this.state.angajat.idcontract === null) {
      this.setState({
        show_confirm: true,
        modalMessage: 'Angajatul are nevoide de un contract de muncă',
      });
      return;
    }
    if (typeof this.state.angajat === 'undefined') return;

    for (let key in cm) if (cm[key] === '-') cm[key] = '';

    this.setState({
      id: cm.id,
      dela: cm.dela.substring(0, 10),
      panala: cm.panala.substring(0, 10),
      continuare: cm.continuare,
      datainceput: cm.datainceput.substring(0, 10),
      serienrcertificat: cm.serienrcertificat,
      dataeliberare: cm.dataeliberare.substring(0, 10),
      codurgenta: cm.codurgenta,
      procent: cm.procent,
      codboalainfcont: cm.codboalainfcont,
      bazacalcul: cm.bazacalcul,
      bazacalculplafonata: cm.bazacalculplafonata,
      zilebazacalcul: cm.zilebazacalcul,
      mediezilnica: cm.mediezilnica,
      zilefirma: cm.zilefirma,
      indemnizatiefirma: cm.indemnizatiefirma,
      zilefnuass: cm.zilefnuass,
      indemnizatiefnuass: cm.indemnizatiefnuass,
      locprescriere: cm.locprescriere,
      nravizmedic: cm.nravizmedic,
      codboala: cm.codboala,
      urgenta: cm.urgenta,
      conditii: cm.conditii,
      idcontract: cm.idcontract,

      isEdit: true,
      show: true,
    });
  }

  // function to create react component with fetched data
  renderCM() {
    this.setState({
      cmComponent: this.state.cm.map((cm, index) => {
        for (let key in cm) {
          if (cm[key] === '' || cm[key] === null) cm[key] = '-';
        }
        return (
          <tr key={cm.id}>
            <th className="d-inline-flex flex-row justify-content-around">
              <Button
                variant="outline-secondary"
                className="ml-2 p-1 rounded-circle border-0"
                onClick={() => this.editCM(cm)}
              >
                <Edit fontSize="small" />
              </Button>
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
                        <Typography variant="caption">
                          Datele nu mai pot fi recuperate
                        </Typography>
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
            <th>{cm.dela.substring(0, 10)}</th>
            <th>{cm.panala.substring(0, 10)}</th>
            <th>{cm.continuare ? 'Da' : 'Nu'}</th>
            <th>{cm.datainceput.substring(0, 10)}</th>
            <th>{cm.serienrcertificat}</th>
            <th>{cm.dataeliberare.substring(0, 10)}</th>
            <th>{cm.codurgenta}</th>
            <th>{cm.procent}</th>
            <th>{cm.codboalainfcont}</th>
            <th>{cm.bazacalcul}</th>
            <th>{cm.bazacalculplafonata}</th>
            <th>{cm.zilebazacalcul}</th>
            <th>{cm.mediezilnica}</th>
            <th>{cm.zilefirma}</th>
            <th>{cm.indemnizatiefirma}</th>
            <th>{cm.zilefnuass}</th>
            <th>{cm.indemnizatiefnuass}</th>
            <th>{cm.locprescriere}</th>
            <th>{cm.nravizmedic}</th>
            <th>{cm.codboala}</th>
            <th>{cm.urgenta ? 'Da' : 'Nu'}</th>
            <th>{cm.conditii}</th>
          </tr>
        );
      }),
    });
  }

  render() {
    return (
      <Aux>
        {/* // C.M. MODAL */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Concediu medical</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group id="dela">
                <Form.Label>Începând cu (inclusiv)</Form.Label>
                <Form.Control
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
                  type="date"
                  value={this.state.panala}
                  onChange={(e) => {
                    this.setState({ panala: e.target.value });
                  }}
                />
              </Form.Group>
              <Row>
                <Col md={6}>
                  <Form.Group id="continuare">
                    <Form.Check
                      custom
                      type="checkbox"
                      id="continuareCheck"
                      label="Continuare"
                      checked={this.state.continuare}
                      value={this.state.continuare}
                      onChange={(e) => {
                        this.setState({ continuare: e.target.checked });
                      }}
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group id="panala">
                    <Form.Label>Dată început</Form.Label>
                    <Form.Control
                      type="date"
                      value={this.state.datainceput}
                      onChange={(e) => {
                        this.setState({ datainceput: e.target.value });
                      }}
                    />
                  </Form.Group>
                </Col>
              </Row>
              <Form.Group id="serienrcertificat">
                <Form.Label>Serie și număr certificat</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.serienrcertificat}
                  onChange={(e) => {
                    this.setState({ serienrcertificat: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="dataeliberare">
                <Form.Label>Dată eliberare</Form.Label>
                <Form.Control
                  type="date"
                  value={this.state.dataeliberare}
                  onChange={(e) => {
                    this.setState({ dataeliberare: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codurgenta">
                <Form.Label>Cod urgență</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codurgenta}
                  onChange={(e) => {
                    this.setState({ codurgenta: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="procent">
                <Form.Label>Procent</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.procent}
                  onChange={(e) => {
                    this.setState({ procent: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codboalainfcont">
                <Form.Label>Cod boală infecțioasă/contagioasă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codboalainfcont}
                  onChange={(e) => {
                    this.setState({ codboalainfcont: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="bazacalcul">
                <Form.Label>Bază calcul</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.bazacalcul}
                  onChange={(e) => {
                    this.setState({ bazacalcul: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="bazacalculplafonata">
                <Form.Label>Bază calcul plafonată</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.bazacalculplafonata}
                  onChange={(e) => {
                    this.setState({ bazacalculplafonata: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="zilebazacalcul">
                <Form.Label>Zile bază calcul</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.zilebazacalcul}
                  onChange={(e) => {
                    this.setState({ zilebazacalcul: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="mediezilnica">
                <Form.Label>Medie zilnică</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.mediezilnica}
                  onChange={(e) => {
                    this.setState({ mediezilnica: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="zilefirma">
                <Form.Label>Zile suportate de firmă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.zilefirma}
                  onChange={(e) => {
                    this.setState({ zilefirma: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="indemnizatiefirma">
                <Form.Label>Indemnizație firmă</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.indemnizatiefirma}
                  onChange={(e) => {
                    this.setState({ indemnizatiefirma: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="zilefnuass">
                <Form.Label>Zile FNUASS</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.zilefnuass}
                  onChange={(e) => {
                    this.setState({ zilefnuass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="indemnizatiefnuass">
                <Form.Label>Indemnizație FNUASS</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.indemnizatiefnuass}
                  onChange={(e) => {
                    this.setState({ indemnizatiefnuass: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="locprescriere">
                <Form.Label>Loc prescriere</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.locprescriere}
                  onChange={(e) => {
                    this.setState({ locprescriere: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="nravizmedic">
                <Form.Label>Nr. aviz medical</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.nravizmedic}
                  onChange={(e) => {
                    this.setState({ nravizmedic: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="codboala">
                <Form.Label>Cod boală</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.codboala}
                  onChange={(e) => {
                    this.setState({ codboala: e.target.value });
                  }}
                />
              </Form.Group>
              <Form.Group id="urgenta">
                <Form.Check
                  custom
                  type="checkbox"
                  id="urgentaCheck"
                  label="Urgență"
                  checked={this.state.urgenta}
                  value={this.state.urgenta}
                  onChange={(e) => {
                    this.setState({ urgenta: e.target.checked });
                  }}
                />
              </Form.Group>
              <Form.Group id="conditii">
                <Form.Label>Condiții</Form.Label>
                <Form.Control
                  type="text"
                  value={this.state.conditii}
                  onChange={(e) => {
                    this.setState({ conditii: e.target.value });
                  }}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={this.state.isEdit ? this.updateCM : this.addCM}
            >
              {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
            </Button>
          </Modal.Footer>
        </Modal>

        {/* CMNFIRM Modal */}
        <Modal
          show={this.state.show_confirm}
          onHide={() => this.handleClose(true)}
        >
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
                <Button
                  variant={
                    typeof this.state.angajat === 'undefined'
                      ? 'outline-dark'
                      : 'outline-primary'
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
                    typeof this.state.angajat === 'undefined'
                      ? 'outline-dark'
                      : 'outline-primary'
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
                      <th></th>
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
                    </tr>
                  </thead>
                  <tbody>{this.state.cmComponent}</tbody>
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
