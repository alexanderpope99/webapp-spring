import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../../../../Resources/server-address';
import { getSocSel } from '../../../../Resources/socsel';
import { download } from '../../../../Resources/download';
import { luni } from '../../../../Resources/calendar';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';
import authService from '../../../../../services/auth.service';
import { getAngajatSel } from '../../../../Resources/angajatsel';

class AdeverintaVenit extends React.Component {
	constructor() {
		super();

		if (!getSocSel()) window.location.href = '/dashboard/societati';

		this.creeazaFisier = this.creeazaFisier.bind(this);

		this.state = {
			socsel: getSocSel(),
			angajatsel: getAngajatSel(),
			lunaDela: '',
			lunaPanala: '',
			an: '',
			intocmitDe: '',
			user: authService.getCurrentUser(),
			showToast: false,
			toastMessage: '',
		};
	}

	componentDidMount() {
		if (!getSocSel()) window.location.href = '/dashboard/societati';

		this.setCurrentYearMonth();
	}

	setCurrentYearMonth() {
		let today = new Date();
		let luna = luni[today.getMonth()];
		let an = today.getFullYear();

		this.setState({
			an: an,
			lunaDela: { nume: luna, nr: today.getMonth() + 1 },
			lunaPanala: { nume: luna, nr: today.getMonth() + 1 },
		});
	}

	async creeazaFisier() {
		// make request to create stat for soc, luna, an
		let lunaDela = this.state.lunaDela;
		let lunaPanala = this.state.lunaPanala;
		let an = this.state.an;

		if(lunaDela.nr > lunaPanala.nr) return;

		// get current angajat
		const angajatsel = getAngajatSel();

		const created = await axios
		  .get(
		    `${server.address}/adeverinta-venit/${angajatsel.idpersoana}/m1=${lunaDela.nr}&m2=${lunaPanala.nr}&y=${an}/${this.state.user.id}`,
		    { headers: authHeader() }
		  )
		  .then((res) => res.data)
		  .catch((err) =>
		    this.setState({
		      showToast: true,
		      toastMessage: 'Nu am putut crea fisierul: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'),
		    })
		  );

		if (created)
		  download(
		    `Adeverinta de venit - ${angajatsel.numeintreg} - ${an}.xlsx`,
		    this.state.user.id
		  );
	}

	render() {
		const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

		return (
			<React.Fragment>
				<Toast
					onClose={() => this.setState({ showToast: false })}
					show={this.state.showToast}
					className="position-fixed"
					style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
				>
					<Toast.Header className="pr-2">
						<strong className="mr-auto">Eroare</strong>
					</Toast.Header>
					<Toast.Body>{this.state.toastMessage}</Toast.Body>
				</Toast>
				<Card className="border">
					<Card.Header>
						<Typography variant="h5">Adeverință de venit</Typography>
					</Card.Header>
					<Card.Body>
						<Form onSubmit={this.creeazaFisier}>
							<Row>
								{/* LUNA DELA */}
								<Col md={4}>
									<Form.Group>
										<Form.Label>Din luna</Form.Label>
										<Form.Control
											as="select"
											value={this.state.lunaDela.nume}
											onChange={(e) =>
												this.setState({
													lunaDela: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
												})
											}
										>
											{luniComponent}
										</Form.Control>
									</Form.Group>
								</Col>
								{/* LUNA DELA */}
								<Col md={4}>
									<Form.Group>
										<Form.Label>Până în luna</Form.Label>
										<Form.Control
											as="select"
											value={this.state.lunaPanala.nume}
											onChange={(e) =>
												this.setState({
													lunaPanala: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
												})
											}
										>
											{luniComponent}
										</Form.Control>
									</Form.Group>
								</Col>
								{/* AN */}
								<Col md={4}>
									<Form.Group>
										<Form.Label>An</Form.Label>
										<FormControl
											type="number"
											value={this.state.an}
											onChange={(e) =>
												this.setState({
													an: e.target.value,
												})
											}
										/>
									</Form.Group>
								</Col>
							</Row>
						</Form>
						<div className="mt-4">
							<Button onClick={this.creeazaFisier}>Adeverință de venit in Excel</Button>
						</div>
					</Card.Body>
				</Card>
			</React.Fragment>
		);
	}
}

export default AdeverintaVenit;
