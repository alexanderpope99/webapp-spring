import React from 'react';
import { Row, Col, Card, Table, Button, OverlayTrigger, Tooltip, Modal } from 'react-bootstrap';
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
import axios from 'axios';
import authHeader from '../../services/auth-header';

class PersoaneIntretinereTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);

    this.state = {
      socsel: getSocSel(),
      angajatsel: getAngajatSel(),
      persoane: [],
      persoaneComponent: null,
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
	}
	
	updateAngajatSel() {
		this.setState({angajatsel: getAngajatSel()});
	}

  editPersoanaIntretinere() {
    console.log('editing persoana');
  }

  deletePersoana(id, nume, prenume) {
    // id = id.replace('"', '');
    // console.log(id);

    axios
      .delete(`${server.address}/persoanaintretinere/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        // console.log(response);
        this.onRefresh();
      });
  }

  // function to create react component with fetched data
  renderPersoane() {
    this.setState({
      persoaneComponent: this.state.persoane.map((pers, index) => {
        for (let key in pers) {
          if (!pers[key]) pers[key] = '-';
        }
        return (
          // TODO
          <tr key={pers.id}>
            <th>{pers.nume}</th>
            <th>{pers.prenume}</th>
            <th className="d-inline-flex flex-row justify-content-around">
              <Button
                onClick={this.editPersoanaIntretinere}
                variant="outline-secondary"
                className="ml-2 p-1 rounded-circle border-0"
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
            </th>
          </tr>
        );
      }),
    });
  }

  async onRefresh() {
    if (this.state.angajatsel) {
      const persoane = await axios
        .get(`${server.address}/persoanaintretinere/ida=${this.state.angajatsel.idpersoana}`, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) => console.error(err));
      if (persoane) {
        this.setState({
          persoane: persoane,
        });

        this.renderPersoane();
      }
    }
  }

  render() {
    return (
      <Aux>
        {/* insert modal here */}

        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">
                  Persoane Întreținere
                  {this.state.angajatsel ? ' - ' + this.state.angajatsel.numeintreg : ' *niciun angajat selectat'}
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
                    <Refresh className="m-0 p-0" />
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
                    <Add className="m-0 p-0" />
                  </Button>
                </OverlayTrigger>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Nume</th>
                      <th>Prenume</th>
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

export default PersoaneIntretinereTabel;
