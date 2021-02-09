import React from 'react';
import { Row, Col, Tabs, Tab, Button, Modal, Breadcrumb } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import FacturiTabel from '../Tables/FacturiTabel';
import EmitereFactura from '../Forms/EmitereFactura';

export default class Facturi extends React.Component {
  constructor(props) {
    super(props);

		this.edit = this.edit.bind(this);

    this.state = {
      key: 'tabel-facturi',
      socsel: getSocSel(),
    };
  }

	onSelectPill(key) {
		this.setState({key: key, factura: null,});
	}

	edit(factura) {
    this.setState({
			key: 'emite-factura',
			factura: factura,
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
              <Tab eventKey="emite-factura" title="Emite factură">
								<EmitereFactura factura={this.state.factura}/>
							</Tab>
            </Tabs>
          </Col>
        </Row>
      </Aux>
    );
  }
}
