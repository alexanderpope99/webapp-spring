import React from 'react';
import { Row, Col, Card, Table, Button, OverlayTrigger, Tooltip, Toast } from 'react-bootstrap';
import { Trash2, Edit3, Plus, RotateCw } from 'react-feather';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { setAngajatSel } from '../Resources/angajatsel';

class PersoaneTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);

    this.state = {
      socsel: getSocSel(),
      persoane: [],
      persoaneComponent: null,

      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  deletePersoana(id) {
    setAngajatSel(null);

    axios
      .delete(`${server.address}/angajat/${id}`, { withCredentials: true })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      })
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut șterge persoana\n' + err.response.data.message,
        })
      );
  }

  // function to create react component with fetched data
  renderPersoane() {
    this.setState({
      persoaneComponent: this.state.persoane.map((pers, index) => {
        for (let key in pers) {
          if (!pers[key]) pers[key] = '-';
        }
        return (
          <tr key={pers.id}>
            <th>{pers.nume}</th>
            <th>{pers.prenume}</th>
            <th>{pers.email}</th>
            <th>{pers.telefon}</th>
            {/* <th className="d-inline-flex"> */}
            <th>
              <div className="d-inline-flex">
                <Button
                  href={`/edit/edit-persoana?id=${pers.id}`}
                  variant="outline-secondary"
                  className="ml-2 p-1 rounded-circle border-0"
                >
                  <Edit3 size={20} />
                </Button>

                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <Button
                        variant="outline-secondary"
                        className="m-0 p-1 rounded-circle border-0"
                        {...bindTrigger(popupState)}
                      >
                        <Trash2 size={20} />
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
                            Sigur ștergeți persoana {pers.nume} {pers.prenume}?
                          </Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.deletePersoana(pers.id, pers.nume, pers.prenume);
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
    const persoane = await axios
      .get(`${server.address}/persoana/ids=${this.state.socsel.id}`, { withCredentials: true })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua persoanele\n' + err.response.data.message,
        })
      );

    this.setState({
      persoane: persoane,
    });

    this.renderPersoane();
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
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">
                  Persoane Înregistrate
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
                      <th>Nume</th>
                      <th>Prenume</th>
                      <th>email</th>
                      <th>telefon</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.persoaneComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default PersoaneTabel;
