import React from 'react';
import { Row, Col, Tabs, Tab, Breadcrumb } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import FacturiTabel from './FacturiTabelNext';
import EmitereFactura from './EmitereFactura';

import { getSocSel } from '../../Resources/socsel';

export default class Facturi extends React.Component {
	constructor(props) {
		super(props);

		this.edit = this.edit.bind(this);
		this.scrollToTopSmooth = this.scrollToTopSmooth.bind(this);

		this.tabelFacturi = React.createRef();
		this.emiteFactura = React.createRef();

		this.state = {
			key: 'tabel-facturi',
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
	}

	async edit(factura) {
		await this.emiteFactura.current.fillForm(factura);
		this.setState({ key: 'emite-factura' });
	}

	async cresteUltimulNumar() {
		this.setState({ ultimulNumar: this.state.ultimulNumar + 1 })
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
