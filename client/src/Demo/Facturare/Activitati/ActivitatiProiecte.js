import React from 'react';
import { Row, Col, Tabs, Tab, Breadcrumb } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import ActivitatiTabel from './ActivitatiTabel';
import ProiecteTabel from './ProiecteTabel';

import { getSocSel } from '../../Resources/socsel';

export default class Facturi extends React.Component {
  constructor() {
    super();

    this.onSelectPill = this.onSelectPill.bind(this);

    this.tabelActivitati = React.createRef();
    this.tabelProiecte = React.createRef();

    this.state = {
      socsel: getSocSel(),

      numarFactura: 0,
      key: 'tabel-activitati',
    };
  }
  onSelectPill(key) {
    if (key === 'tabel-activitati') {
      this.tabelActivitati.current.getActivitati();
    }
    if (key === 'tabel-proiecte') {
      this.tabelProiecte.current.init();
    }
    this.setState({
      key: key,
    });
  }

  scrollToTopSmooth() {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Breadcrumb style={{ fontSize: '12px' }}>
              <Breadcrumb.Item href="/dashboard/societati">
                {this.state.socsel.nume}
              </Breadcrumb.Item>
              <Breadcrumb.Item active>Facturi</Breadcrumb.Item>
            </Breadcrumb>
            <Tabs
              variant="pills"
              activeKey={this.state.key}
              onSelect={(key) => this.onSelectPill(key)}
            >
              <Tab eventKey="tabel-activitati" title="Activități">
                <ActivitatiTabel ref={this.tabelActivitati} />
              </Tab>
              <Tab eventKey="tabel-proiecte" title="Proiecte">
                <ProiecteTabel ref={this.tabelProiecte} />
              </Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}
