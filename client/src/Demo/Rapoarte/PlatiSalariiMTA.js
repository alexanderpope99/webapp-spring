import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { luni } from '../Resources/calendar';
import authService from '../../services/auth.service';
import authHeader from '../../services/auth-header';
import {download} from '../Resources/download';

class PlatiSalariiMTA extends React.Component {
	constructor() {
		super();

		if (!getSocSel()) window.location.href = '/dashboard/societati';

		this.creeazaMTA = this.creeazaMTA.bind(this);

		this.state = {
			socsel: getSocSel(),
			luna: '',
			an: '',
			d_rec: '',
			numeDeclarant: '',
			prenumeDeclarant: '',
			functieDeclarant: '',
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
			luna: { nume: luna, nr: today.getMonth() + 1 },
		});
	}

	async download(luna, an) {
		const token = this.state.user.accessToken;
		console.log('trying to download...');
		//let societateNume = this.state.socsel.nume;
		await fetch(`${server.address}/download/${this.state.user.id}/FisierMTA.xlsx`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/octet-stream',
				Authorization: `Bearer ${token}`,
			},
		})
			.then((res) => res.blob())
			.then((blob) => {
				var url = window.URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = url;
				a.download = `FisierMTA - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`;
				document.body.appendChild(a);
				a.click();
				a.remove(); //afterwards we remove the element again
				console.log('downloaded');
			})
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage: 'Nu am putut descărca MTA ' + err.response.data.message,
				})
			);
	}

	async creeazaMTA(e) {
		e.preventDefault();
		// make request to create stat for soc, luna, an
		let luna = this.state.luna;
		let an = this.state.an;

		const created = await axios.get(`${server.address}/mta/${this.state.socsel.id}&mo=${luna.nr}&y=${an}/${this.state.user.id}`, { headers: authHeader() })
			.then((res) => res.status === 200)
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage: 'Nu am putut crea MTA ' + err.response.data.message,
				})
			);

		if (created) download(`FisierMTA - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`, this.state.user.id);

		// const created = await fetch(
		//   `${server.address}/mta/${this.state.socsel.id}&mo=${luna.nr}&y=${an}/${this.state.user.id}`,
		//   {
		//     method: 'GET',
		//     headers: {
		//       'Content-Type': 'application/json',
		//       Authorization: `Bearer ${this.state.user.accessToken}`,
		//     },
		//   }
		// )
		//   .then((res) => res.ok)
		//   .catch((err) =>
		//     this.setState({
		//       showToast: true,
		//       toastMessage: 'Nu am putut crea MTA ' + err.response.data.message,
		//     })
		//   );

		// if (created) this.download(luna, an);
	}

	render() {
		const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

		return (
			<Card>
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
				<Card.Header>
					<Typography variant="h5">Plăți Salarii MTA</Typography>
				</Card.Header>
				<Card.Body>
					<Form onSubmit={this.creeazaMTA}>
						<Row>
							{/* LUNA */}
							<Col md={4}>
								<Form.Group>
									<Form.Control
										as="select"
										value={this.state.luna.nume}
										onChange={(e) =>
											this.setState({
												luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
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
						<Button onClick={this.creeazaMTA}>Generează MTA</Button>
					</div>
				</Card.Body>
			</Card>
		);
	}
}

export default PlatiSalariiMTA;
