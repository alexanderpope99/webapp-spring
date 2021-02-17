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
    this.tableProiecte = React.createRef();

    this.state = {
      socsel: getSocSel(),

      numarFactura: 0,
    };
  }
  onSelectPill(key) {
    if (key === 'tabel-activitati') {
      this.tabelActivitati.current.getActivitati();
    }
    if (key === 'tabel-proiecte') {
      this.tableProiecte.current.init();
    }
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
              <Tab eventKey="tabel-facturi" title="Activități">
                <ActivitatiTabel ref={this.tabelFacturi} />
              </Tab>
              <Tab eventKey="emite-factura" title="Proiecte">
                <ProiecteTabel ref={this.emiteFactura} />
              </Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}
