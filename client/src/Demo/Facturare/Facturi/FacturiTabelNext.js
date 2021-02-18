import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Button, Toast } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw, Plus } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';

import authHeader from '../../../services/auth-header';
import BootstrapTable from 'react-bootstrap-table-next';

class FacturiTabel extends React.Component {
  constructor(props) {
    super(props);

    this.getFacturi = this.getFacturi.bind(this);
    this.renderFacturi = this.renderFacturi.bind(this);

    this.state = {
      showToast: false,
      toastMessage: '',

      socsel: getSocSel(),
      facturi: [],
      clienti: [],
    };
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
    this.setState({
      facturi: facturi || [],
    });
  }

  async renderFacturi(noTimeout) {
    if (noTimeout === 'no-timeout') {
      this.getFacturi();
    } else {
      this.setState({ facturi: [] });
      setTimeout(this.getFacturi, 100);
    }
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

  buttons = (cell, row, rowIndex, formatExtraData) => (
    <div className="d-inline-flex">
      <Button
        onClick={() => this.props.edit(row)}
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
                    this.delete(row.id);
                  }}
                  className="mt-2 "
                >
                  Da
                </Button>
                <Button variant="outline-persondary" onClick={popupState.close} className="mt-2">
                  Nu
                </Button>
              </Box>
            </Popover>
          </div>
        )}
      </PopupState>
    </div>
  );

  render() {
    const columns = [
      {
        dataField: '',
        text: 'Acțiuni',
        formatter: this.buttons,
      },
      {
        dataField: 'numar',
        text: 'Număr',
        sort: true,
      },
      {
        dataField: 'client.nume',
        text: 'Client',
        sort: true,
      },
      {
        dataField: 'serie',
        text: 'Serie',
        sort: true,
      },
      {
        dataField: 'titlu',
        text: 'Descriere',
        sort: true,
        style: { overflow: 'hide' },
      },
      {
        dataField: 'proiect.nume',
        text: 'Proiect',
        sort: true,
      },
      {
        dataField: 'totalcutva',
        text: 'Valoare',
        formatter: (cell) => cell.toFixed(2),
        sort: true,
      },
      {
        dataField: 'dataexpedierii',
        text: 'Data',
        sort: true,
      },
      {
        dataField: 'scadenta',
        text: 'Scadență',
        sort: true,
      },
      {
        dataField: 'statut',
        text: 'Statut',
        sort: true,
      },
    ];

    return (
      <Aux>
        {/* ERROR TOAST */}
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

        {/* TABLE BODY */}
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
                  onClick={this.renderFacturi}
                >
                  <RotateCw className="m-0 p-0" />
                </Button>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={this.state.facturi}
                  columns={columns}
                  wrapperClasses="table-responsive"
                  hover
                  bordered={false}
                />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default FacturiTabel;
