/* eslint-disable eqeqeq */
import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Button, Toast, Form } from 'react-bootstrap';
import { Trash2, Edit3, RotateCw, Plus,Download } from 'react-feather';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Popover from '@material-ui/core/Popover';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import Aux from '../../../hoc/_Aux';
import { download } from '../../Resources/download';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';
import { luni } from '../../Resources/calendar';

import authHeader from '../../../services/auth-header';
import authService from '../../../services/auth.service';
import BootstrapTable from 'react-bootstrap-table-next';

class FacturiTabel extends React.Component {
  constructor(props) {
    super(props);

    this.getFacturi = this.getFacturi.bind(this);
    this.renderFacturi = this.renderFacturi.bind(this);
    this.creeazaFactura = this.creeazaFactura.bind(this);


    let today = new Date();
    this.state = {
      showToast: false,
      toastMessage: '',

      socsel: getSocSel(),
      facturi: [],
      today: today,

      // filtre
      clientiNume: [],
      clientFilter: '',
      proiecteNume: [],
      proiectFilter: '',
      luna: '',
      aniFacturi: [],
      an: '',
      ultimulAn: today.getFullYear(),

	  user: authService.getCurrentUser(),
    };
  }

  componentDidMount() {
    if (!this.state.socsel) {
      window.location.href = '/dashboard/societati';
      return;
    }
    this.init();
  }

  componentDidUpdate(prevProps) {
    if (this.props.factura !== prevProps.factura) {
      this.init();
    } else return;
  }

  init() {
    this.getFacturi();
    // this.getClienti();
  }

  async creeazaFactura(e) {
	  console.log(e);
    const created = await fetch(
      `${server.address}/factura/createfile/ids=${this.state.socsel.id}/${e.id}/${this.state.user.id}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${this.state.user.accessToken}`,
        },
      }
    )
      .then((res) => res.ok)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea factură: ' + err.response.data.message,
        })
      );

    if (created)
      download(
        `Factură - ${this.state.socsel.nume} - ${e.client.nume} - ${e.dataexpedierii} ${e.oraexpedierii}.xlsx`,
        this.state.user.id
      );
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
    if (facturi) {
      var clientiNume = new Set();
      var proiecteNume = new Set();
      var aniFacturi = new Set();
      aniFacturi.add(new Date().getFullYear());
      for (let factura of facturi) {
        clientiNume.add(factura.client.nume);
        if (factura.proiect) proiecteNume.add(factura.proiect.nume);
        let anFactura = new Date(factura.dataexpedierii).getFullYear();
        aniFacturi.add(anFactura);
      }

      this.setState({
        facturi: facturi || [],
        clientiNume: [...clientiNume],
        proiecteNume: [...proiecteNume],
        aniFacturi: [...aniFacturi],
      });
    }
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

  filterFacturi() {
    const clientNume = this.state.clientFilter;
    const proiectNume = this.state.proiectFilter;
    const luna = this.state.luna;
    const an = this.state.an;
    var facturi = [...this.state.facturi];
    if (clientNume) {
      facturi = facturi.filter((factura) => factura.client.nume === clientNume);
      // console.log('facturi filtrate dupa client ' + clientNume + ':', facturi);
    }
    if (proiectNume) {
      facturi = facturi.filter((factura) =>
        factura.proiect ? factura.proiect.nume === proiectNume : false
      );
      // console.log('facturi filtrate dupa proiect ' + proiectNume + ':', facturi);
    }
    if (an) {
      facturi = facturi.filter((factura) => {
        let anFactura = new Date(factura.dataexpedierii).getFullYear();
        return anFactura == an;
      });
      // console.log('facturi filtrate dupa an ' + an + ':', facturi);
    }
    if (luna) {
      facturi = facturi.filter((factura) => {
        let lunaFactura = new Date(factura.dataexpedierii).getMonth() + 1;
        return lunaFactura == luna;
      });
      console.log('facturi filtrate dupa luna ' + luna + ':', facturi);
    }
    // console.log(facturi);
    return facturi;
  }

  buttons = (cell, row, rowIndex, formatExtraData) => (
    <div className="d-inline-flex">
	<Button
    onClick={() => this.creeazaFactura(row)}
    variant="outline-secondary"
    className="m-1 p-1 rounded-circle border-0"
    >
        <Download size={20} />
    </Button>
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
        dataField: 'status',
        text: 'Status',
        sort: true,
      },
    ];

    const clientiComponent = this.state.clientiNume.map((client, index) => (
      <option key={index}>{client}</option>
    ));

    const proiecteComponent = this.state.proiecteNume.map((proiect, index) => (
      <option key={index}>{proiect}</option>
    ));

    const luniComponent = luni.map((luna) => <option key={luna}>{luna}</option>);

    var aniComponent = this.state.aniFacturi.map((an) => <option key={an}>{an}</option>);

    const facturiComponent = this.filterFacturi();

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
                <Card.Title as="h5">Facturi pentru {this.state.socsel.nume}</Card.Title>
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

                {/* SORT FILTERS */}
                <Row className="mt-5">
                  <Form.Group as={Col} sm="auto">
                    <Form.Label>Clientul</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.clientFilter}
                      onChange={(e) =>
                        this.setState({
                          clientFilter: e.target.value === 'Toți clienții' ? '' : e.target.value,
                        })
                      }
                    >
                      <option>Toți clienții</option>
                      {clientiComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group as={Col} sm="auto">
                    <Form.Label>Anul</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.an}
                      onChange={(e) =>
                        this.setState({ an: e.target.value === '-' ? '' : e.target.value })
                      }
                    >
                      <option>-</option>
                      {aniComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group as={Col} sm="auto">
                    <Form.Label>Luna</Form.Label>
                    <Form.Control
                      as="select"
                      value={luni[this.state.luna - 1]}
                      onChange={(e) => this.setState({ luna: e.target.selectedIndex })}
                    >
                      <option>-</option>
                      {luniComponent}
                    </Form.Control>
                  </Form.Group>
                  <Form.Group as={Col} sm="auto">
                    <Form.Label>Proiect</Form.Label>
                    <Form.Control
                      as="select"
                      value={this.state.proiectFilter}
                      onChange={(e) => this.setState({ proiectFilter: e.target.value === '-' ? '' : e.target.value })}
                    >
                      <option>-</option>
                      {proiecteComponent}
                    </Form.Control>
                  </Form.Group>
                </Row>
              </Card.Header>
              <Card.Body>
                <BootstrapTable
                  bootstrap4
                  overflow
                  keyField="id"
                  data={facturiComponent}
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
