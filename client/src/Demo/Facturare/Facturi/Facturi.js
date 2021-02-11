import React from 'react';
import { Row, Col, Tabs, Tab, Breadcrumb } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import { getSocSel } from '../../Resources/socsel';
import FacturiTabel from './FacturiTabel';
import EmitereFactura from './EmitereFactura';

export default class Facturi extends React.Component {
  constructor(props) {
    super(props);

		this.edit = this.edit.bind(this);

    this.state = {
      key: 'emite-factura',
      socsel: getSocSel(),

			numarFactura: 0,
    };
  }

	onSelectPill(key) {
		this.setState({key: key, factura: null,});
	}

	edit(factura) {
    this.setState({
			key: 'emite-factura',
			factura: factura,
			numarFactura: factura.numar,
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
              <Tab eventKey="tabel-facturi" title="Tabel facturi">
                <FacturiTabel edit={this.edit}/>
              </Tab>
              <Tab eventKey="emite-factura" title="Emitere factură">
								<EmitereFactura factura={this.state.factura} numarFactura={this.state.numarFactura}/>
							</Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}
