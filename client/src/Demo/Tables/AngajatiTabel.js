import React from 'react';
import { Row, Col, Card, Button, Table, OverlayTrigger, Tooltip } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';
import axios from 'axios';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';

import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import { getSocSel, setSocSel } from '../Resources/socsel';
import { setAngajatSel } from '../Resources/angajatsel';
import { server } from '../Resources/server-address';
import authHeader from '../../services/auth-header';
import { RotateCw, Plus, Edit3, Trash2, Clipboard } from 'react-feather';

class AngajatiTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajati: [],
      angajatiComponent: null,
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  deletePersoana(id) {
    axios
      .delete(`${server.address}/angajat/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      });
  }

  // function to create react component with fetched data
  renderAngajati() {
    this.setState({
      angajatiComponent: this.state.angajati.map((ang, index) => {
        for (let key in ang) {
          if (!ang[key]) ang[key] = '-';
        }
        return (
          <tr key={ang.persoana.id}>
            <th>{++index}</th>
            <th>{ang.persoana.nume}</th>
            <th>{ang.persoana.prenume}</th>
            <th>{ang.contract.functie}</th>
            {/* <th className="d-inline-flex"> */}
            <th>
              <div className="d-inline-flex">
                <OverlayTrigger
                  placement="bottom"
                  overlay={
                    <Tooltip id="realizari-retineri" style={{ opacity: '.4' }}>
                      Realizări / Rețineri
                    </Tooltip>
                  }
                >
                  <Button
                    onClick={() => {
                      setAngajatSel({
                        idpersoana: ang.persoana.id,
                        numeintreg: ang.persoana.nume + ' ' + ang.persoana.prenume,
                      });
                      window.location.href = `/forms/realizari-retineri`;
                    }}
                    variant="outline-secondary"
                    className="ml-2 p-1 rounded-circle border-0"
                  >
                    <Clipboard size={20} />
                  </Button>
                </OverlayTrigger>
                <OverlayTrigger
                  placement="bottom"
                  overlay={
                    <Tooltip id="edit-button" style={{ opacity: '.4' }}>
                      Editează
                    </Tooltip>
                  }
                >
                  <Button
                    onClick={() => {
                      setAngajatSel({
                        idpersoana: ang.persoana.id,
                        numeintreg: ang.persoana.nume + ' ' + ang.persoana.prenume,
                      });
                      window.location.href = `/forms/angajat`;
                    }}
                    variant="outline-secondary"
                    className="ml-2 p-1 rounded-circle border-0"
                  >
                    <Edit3 size={20} />
                  </Button>
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
                              this.deleteAngajat(ang.id, ang.nume, ang.prenume);
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
    const angajati = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}&c`, { headers: authHeader() })
      .then((res) => res.data);

    this.setState({
      angajati: angajati,
    });

    this.renderAngajati();
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">
                  Angajați
                  {this.state.socsel ? ' - ' + this.state.socsel.nume : ''}
                </Card.Title>
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
                    onClick={this.onRefresh}
                  >
                    <RotateCw className="m-0 p-0" />
                    {/* ↺ */}
                  </Button>
                </OverlayTrigger>

                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
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
                    <Plus className="m-0 p-0" />
                  </Button>
                </OverlayTrigger>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>Funcție</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.angajatiComponent}</tbody>
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
