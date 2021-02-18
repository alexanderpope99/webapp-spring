import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Table, Button, Toast } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw, Plus,Printer } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';

import authHeader from '../../../services/auth-header';

class FacturiTabel extends React.Component {
  constructor(props) {
    super(props);

    this.getFacturi = this.getFacturi.bind(this);

    this.state = {
      showToast: false,
      toastMessage: '',

      socsel: getSocSel(),
      facturi: [],
      clienti: [],
    };
  }

  async getFacturi() {
    const facturi = await axios
      .get(`${server.address}/factura/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua facturile: ' + err.response.data.message,
        })
      );
    this.setState({ facturi: facturi });
  }

  componentDidMount() {
    if (!this.state.socsel) {
      window.location.href = '/dashboard/societati';
      return;
    }
    this.getFacturi();
  }

	componentDidUpdate(prevProps) {
    if (this.props.factura !== prevProps.factura) {
      this.getFacturi();
    } else return;
  }

  async delete(id) {
    await axios
      .delete(`${server.address}/factura/${id}`, { headers: authHeader() })
      .then(this.getFacturi)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut sterge factura: ' + err.response.data.message,
        })
      );
  }

  render() {
    const facturiComponent = this.state.facturi.map((item, index) => (
      <tr key={item.id}>
        <td>
          <div className="d-flex">
		  <Button
              onClick={() => this.props.edit(item)}
              variant="outline-secondary"
              className="m-1 p-1 rounded-circle border-0"
            >
              <Printer size={20} />
            </Button>
            <Button
              onClick={() => this.props.edit(item)}
              variant="outline-secondary"
              className="m-1 p-1 rounded-circle border-0"
            >
              <Edit3 size={20} />
            </Button>

            <PopupState variant="popover" popupId="demo-popup-popover">
              {(popupState) => (
                <div>
                  <Button
                    variant="outline-secondary"
                    className="m-1 p-1 rounded-circle border-0"
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
                      <Typography>Sigur ștergeți factura?</Typography>
                      <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                      <br />
                      <Button
                        variant="outline-danger"
                        onClick={() => {
                          popupState.close();
                          this.delete(item.id);
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
        </td>
        <td>{index + 1}</td>
        <td>{item.client.nume || '-'}</td>
        <td>{item.serie || ''}</td>
        <td>{item.titlu.length > 20 ? item.titlu.substring(0, 20) + '...' : item.titlu}</td>
        <td>{item.proiect ? item.proiect.nume : ''}</td>
        <td>{item.totalcutva}</td>
        <td>{item.dataexpedierii}</td>
        <td>{item.scadenta}</td>
        <td>{item.statut}</td>
      </tr>
    ));

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
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Facturi</Card.Title>
                <Button
                  variant="outline-primary"
                  size="sm"
                  className="float-right"
                  onClick={() => this.props.edit(null)}
                >
                  <Plus />
                </Button>

                <Button
                  variant="outline-primary"
                  size="sm"
                  className="float-right"
                  onClick={this.getFacturi}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th></th>
                      <th>Nr.</th>
                      <th>Client</th>
                      <th>Serie</th>
                      <th>Descriere</th>
                      <th>Proiect</th>
                      <th>Valoare</th>
                      <th>Data</th>
                      <th>Scadenta</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>{facturiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default FacturiTabel;
