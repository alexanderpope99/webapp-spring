import React from 'react';
import { Link } from 'react-router-dom';
import { Row, Col, Tabs, Tab, Breadcrumb } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import FacturiTabel from './FacturiTabelNext';
import EmitereFactura from './EmitereFactura';

import { getSocSel } from '../../Resources/socsel';

export default class Facturi extends React.Component {
  constructor(props) {
    super(props);

    this.edit = this.edit.bind(this);
    this.onSelectPill = this.onSelectPill.bind(this);

    this.tabelFacturi = React.createRef();
    this.emiteFactura = React.createRef();

    if(!getSocSel()) {
      window.location.href = '/dashboard/societati';
      return;
    }

    this.state = {
      key: window.location.search.substring(1),
      socsel: getSocSel(),

      numarFactura: 0,
    };
  }

  onSelectPill(key) {
    if (key === 'tabel-facturi') {
      this.tabelFacturi.current.getFacturi();
      this.setState({ key: key, factura: null });
    }
    if (key === 'emite-factura') {
      this.edit(null);
    }
    window.history.pushState(key, 'Tabel facturi', '/facturi?' + key);
  }

  async edit(factura) {
    await this.emiteFactura.current.fillForm(factura);
    this.setState({ key: 'emite-factura' });
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
              <Breadcrumb.Item linkAs={Link} linkProps={{ to: '/dashboard/societati' }}>
                {this.state.socsel.nume}
              </Breadcrumb.Item>
              <Breadcrumb.Item active>Facturi</Breadcrumb.Item>
            </Breadcrumb>
            <Tabs
              variant="pills"
              activeKey={this.state.key}
              onSelect={(key) => this.onSelectPill(key)}
            >
              <Tab eventKey="tabel-facturi" title="Tabel facturi">
                <FacturiTabel
                  ref={this.tabelFacturi}
                  edit={this.edit}
                  scrollToTopSmooth={this.scrollToTopSmooth}
                />
              </Tab>
              <Tab eventKey="emite-factura" title="Emitere factură">
                <EmitereFactura
                  ref={this.emiteFactura}
                  factura={this.state.factura}
                  numarFactura={this.state.numarFactura}
                  scrollToTopSmooth={this.scrollToTopSmooth}
                />
              </Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}
