import React from 'react';
import {
  Row,
  Col,
  Card,
  Table,
  Button,
  Modal,
  Form,
  OverlayTrigger,
  Tooltip,
} from 'react-bootstrap';
import CloseIcon from '@material-ui/icons/Close';
import CheckIcon from '@material-ui/icons/Check';
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
    this.approveCerereConcediu = this.approveCerereConcediu.bind(this);
    this.rejectCerereConcediu = this.rejectCerereConcediu.bind(this);

    this.state = {
      socsel: getSocSel(),
      cereriConcediu: [],
      cereriConcediuComponent: null,

      // confirm modal

      // add/edit modal
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async approveCerereConcediu(cer) {
    this.setState({
      id: cer.id,
      pentru: cer.pentru,
      tip: cer.tip,
      motiv: cer.motiv,
      dela: cer.dela ? cer.dela.substring(0, 10) : '',
      panala: cer.panala ? cer.panala.substring(0, 10) : '',
    });
  }

  async rejectCerere(id) {
    axios
      .delete(`${server.address}/cerericoncediu/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  // function to create react component with fetched data
  renderCereri() {
    this.setState({
      cereriConcediuComponent: this.state.cereriConcediu.map((cer, index) => {
        return (
          // TODO
          <tr key={cer.id}>
            <th>{cer.dela || '-'}</th>
            <th>{cer.panala}</th>
            <th>{cer.tip}</th>
            <th>{cer.motiv}</th>
            <th>{cer.status}</th>
            <th>
              <Row>
                <OverlayTrigger
                  placement="bottom"
                  delay={{ show: 250, hide: 250 }}
                  overlay={
                    <Tooltip id="update-button" style={{ opacity: '.4' }}>
                      Acceptă
                    </Tooltip>
                  }
                >
                  <Button
                    onClick={() => this.approveCerereConcediu(cer)}
                    variant="outline-success"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <CheckIcon fontSize="medium" />
                  </Button>
                </OverlayTrigger>

                <PopupState variant="popover" popupId="demo-popup-popover">
                  {(popupState) => (
                    <div>
                      <OverlayTrigger
                        placement="bottom"
                        delay={{ show: 250, hide: 250 }}
                        overlay={
                          <Tooltip id="update-button" style={{ opacity: '.4' }}>
                            Respinge
                          </Tooltip>
                        }
                      >
                        <Button
                          variant="outline-danger"
                          className="m-1 p-1 rounded-circle border-0"
                          {...bindTrigger(popupState)}
                        >
                          <CloseIcon fontSize="medium" />
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
                            Sigur ștergeți cererea {cer.dela} {cer.panala}?
                          </Typography>
                          <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                          <br />
                          <Button
                            variant="outline-danger"
                            onClick={() => {
                              popupState.close();
                              this.rejectCerere(cer.id);
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
    const cereriConcediu = await axios
      .get(
        `${server.address}/cerericoncediu/supsoc/${JSON.parse(localStorage.getItem('user')).id}&${
          getSocSel().id
        }`,
        {
          headers: authHeader(),
        }
      )
      .then((res) => res.data)
      .catch((err) => console.error(err));
    console.log(
      `${server.address}/cerericoncediu/supsoc/${JSON.parse(localStorage.getItem('user')).id}&${
        getSocSel().id
      }`
    );
    if (cereriConcediu) {
      this.setState(
        {
          cereriConcediu: cereriConcediu,
        },
        this.renderCereri
      );
    }
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      pentru: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
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
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Cereri Concediu</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <Refresh className="m-0 p-0" />
                  {/* ↺ */}
                </Button>

                {/* <Button
                  onClick={() => this.setState({ isEdit: false, show: true })}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  <Add className="m-0 p-0" />
                </Button> */}
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>De la</th>
                      <th>Până la</th>
                      <th>Tip</th>
                      <th>Motiv</th>
                      <th>Status</th>
                      <th>Acțiuni</th>
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

export default PersoaneIntretinereTabel;
