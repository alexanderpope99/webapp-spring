import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Table, Button, Toast } from 'react-bootstrap';
import { Trash2, Edit3 } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { downloadFactura } from '../../Resources/download';

import authHeader from '../../../services/auth-header';

class FacturiTabel extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      showToast: false,
      toastMessage: '',

			socsel: getSocSel(),
      facturi: [],
      clienti: [],
    };
  }

  render() {
    const facturiComponent = this.state.facturi.map((item, index) => (
      <tr key={item.id}>
        <th>
          <div className="d-flex">
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
        </th>
        <th>{index + 1}</th>
        <th>{item.client || '-'}</th>
        <th>{item.serie || ''}</th>
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
                <Card.Title as="h5">Facturi</Card.Title>
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
