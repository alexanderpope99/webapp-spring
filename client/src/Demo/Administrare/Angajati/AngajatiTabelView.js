import React from 'react';
import axios from 'axios';
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
} from 'react-bootstrap';
import { RotateCw } from 'react-feather';
import Aux from '../../../hoc/_Aux/index';

import { getSocSel } from '../../Resources/socsel';
import { server } from '../../Resources/server-address';

import authHeader from '../../../services/auth-header';
import authService from '../../../services/auth.service';

class AngajatiTabelView extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.deleteAngajat = this.deleteAngajat.bind(this);

    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
      angajati: [],
      angajatiComponent: null,
      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

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
          toastMessage: 'Nu am putut șterge angajatul: ' + err.response.data.message,
        })
      );
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
            <th>{index + 1}</th>
            <th>{ang.persoana.nume}</th>
            <th>{ang.persoana.prenume}</th>
            <th>{ang.contract.functie || '-'}</th>
          </tr>
        );
      }),
    });
  }

  async onRefresh() {
    const angajati = await axios
      .get(`${server.address}/angajat/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua angajații: ' + err.response.data.message,
        })
      );

    this.setState({
      angajati: angajati,
    });

    this.renderAngajati();
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
              <Breadcrumb.Item linkAs={Link} linkProps={{ to: '/dashboard/societati' }}>{this.state.socsel.nume}</Breadcrumb.Item>
              <Breadcrumb.Item active>Angajați</Breadcrumb.Item>
            </Breadcrumb>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Angajați</Card.Title>
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
                    <RotateCw size="25" />
                    {/* ↺ */}
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

export default AngajatiTabelView;
