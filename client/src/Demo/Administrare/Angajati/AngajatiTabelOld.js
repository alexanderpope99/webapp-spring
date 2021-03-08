import React from 'react';
import { Link } from 'react-router-dom';
import {
  Row,
  Col,
  Card,
  Button,
  Table,
  OverlayTrigger,
  Tooltip,
  Breadcrumb,
  Toast,
  Form,
} from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import axios from 'axios';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';

import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { getSocSel } from '../../Resources/socsel';
import { setAngajatSel } from '../../Resources/angajatsel';
import { server } from '../../Resources/server-address';
import authHeader from '../../../services/auth-header';
import authService from '../../../services/auth.service';

import { RotateCw, UserPlus, Trash2, Info, FileText } from 'react-feather';

class AngajatiTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.deleteAngajat = this.deleteAngajat.bind(this);

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      angajati: [],
      angajatiComponent: null,
      normaFiltru: '-',

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel() || authService.isAngajatSimplu())
      window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  deleteAngajat(id) {
    axios
      .delete(`${server.address}/angajat/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      })
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge angajatul PDF: ' + (err.response
            ? err.response.data.message
            : 'Nu s-a putut stabili conexiunea la server'),
        })
      );
  }

  // function to create react component with fetched data
  renderAngajati() {
    this.setState({
    });
  }

  async onRefresh() {
    const angajati = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua lista de angajați: ' + (err.response
            ? err.response.data.message
            : 'Nu s-a putut stabili conexiunea la server'),
        })
      );

    this.setState({
      angajati: angajati || [],
    });
  }

  // filterState() {
  //   var angajati = [...this.state.angajati];
  //   if(this.state.normaFiltru !== '-') {
  //     angajati = angajati.filter(ang => ang.contract.normalucru == this.state.normaFiltru);
  //   }

  //   return angajati;
  // }

  render() {

    const angajatiComponent = this.state.angajati
      // eslint-disable-next-line eqeqeq
      .filter(ang => this.state.normaFiltru === '-' ? true : ang.contract.normalucru == this.state.normaFiltru)
      .map((ang, index) => {
        for (let key in ang) {
          if (!ang[key]) ang[key] = '-';
        }
        return (
          <tr key={ang.persoana.id}>
            <td>{index + 1}</td>
            <td>{ang.persoana.nume}</td>
            <td>{ang.persoana.prenume}</td>
            <td>{ang.contract.functie || '-'}</td>
            <td>
              {ang.contract.salariutarifar
                ? ang.contract.salariutarifar + ' ' + ang.contract.monedasalariu
                : 'lipsă contract'}
            </td>
            <td>{ang.contract.normalucru || '-'}</td>
            <td className="d-inline-flex">
              {/* REALIZARI/RETINERI BUTTON */}
              <OverlayTrigger
                placement="bottom"
                overlay={
                  <Tooltip id="realizari-retineri" style={{ opacity: '.4' }}>
                    Realizări / Rețineri
                </Tooltip>
                }
              >
                <Link to="/forms/realizari-retineri">
                  <Button
                    disabled={!ang.contract.id}
                    onClick={() => {
                      setAngajatSel({
                        idpersoana: ang.persoana.id,
                        numeintreg: ang.persoana.nume + ' ' + ang.persoana.prenume,
                      });
                    }}
                    variant="outline-secondary"
                    className="ml-2 p-1 rounded-circle border-0"
                  >
                    <FileText size={20} />
                  </Button></Link>
              </OverlayTrigger>

              {/* DATE PERSONALE BUTTON */}
              <OverlayTrigger
                placement="bottom"
                overlay={
                  <Tooltip id="edit-button" style={{ opacity: '.4' }}>
                    Date personale, contract, concedii, etc.
                </Tooltip>
                }
              >
                <Link to="/forms/angajat">
                  <Button
                    onClick={() => {
                      setAngajatSel({
                        idpersoana: ang.persoana.id,
                        numeintreg: ang.persoana.nume + ' ' + ang.persoana.prenume,
                      });
                    }}
                    variant="outline-secondary"
                    className="ml-2 p-1 rounded-circle border-0"
                  >
                    <Info size={20} />
                  </Button></Link>
              </OverlayTrigger>

              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <OverlayTrigger
                      placement="bottom"
                      overlay={
                        <Tooltip id="delete-button" style={{ opacity: '.4' }}>
                          Șterge
                      </Tooltip>
                      }
                    >
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
                        {...bindTrigger(popupState)}
                      >
                        <Trash2 size={20} />
                      </Button>
                    </OverlayTrigger>
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
                          Sigur ștergeți angajatul {ang.nume} {ang.prenume}?
                      </Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteAngajat(ang.persoana.id);
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
            </td>
          </tr>
        );
      });

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


        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item linkAs={Link} linkProps={{ to: '/dashboard/societati' }}>{this.state.socsel ? this.state.socsel.nume : ''}</Breadcrumb.Item>
              <Breadcrumb.Item active>Angajați</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Angajați</Card.Title>

                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250 }}
                  overlay={
                    <Tooltip id="add-button" style={{ opacity: '.4' }}>
                      Adaugă o persoană nouă
                    </Tooltip>
                  }
                >
                  <Button
                    href="/forms/add-persoana"
                    variant="outline-info"
                    size="sm"
                    style={{ fontSize: '1.25rem', float: 'right' }}
                  >
                    <UserPlus className="m-0 p-0" />
                  </Button>
                </OverlayTrigger>

                <Row>
                  <Form.Group as={Col} sm="auto" className="mt-3">
                    <Form.Label>Normă lucru (ore)</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.normaFiltru}
                      onChange={e => this.setState({ normaFiltru: e.target.value })}
                      default="8"
                    >
                      <option>-</option>
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                      <option>6</option>
                      <option>7</option>
                      <option>8</option>
                    </Form.Control>
                  </Form.Group>
                </Row>

              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>Funcție</th>
                      <th>Salariu de baza</th>
                      <th>Normă lucru (ore)</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{angajatiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AngajatiTabel;
