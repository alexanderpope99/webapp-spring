import React from 'react';
import {
  Row,
  Col,
  Card,
  Button,
  Table,
  OverlayTrigger,
  Tooltip,
  Breadcrumb,
} from 'react-bootstrap';
import Aux from '../../hoc/_Aux';
import axios from 'axios';

import { getSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

import { RotateCw } from 'react-feather';

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
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">Societăți</Breadcrumb.Item>
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
