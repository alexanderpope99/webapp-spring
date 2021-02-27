import React from 'react';
import {
	Row,
	Col,
	Form,
	Button,
	Modal,
	FormControl,
	InputGroup,
	DropdownButton,
	Dropdown,
	Toast,
	Table,
} from 'react-bootstrap';
import { Trash2 } from 'react-feather';
import Typography from '@material-ui/core/Typography/Typography';
import Box from '@material-ui/core/Box';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import { server } from '../../../../Resources/server-address';
import { getSocSel } from '../../../../Resources/socsel';
import { case_de_sanatate, judete } from '../../../../Resources/judete';
import axios from 'axios';
import authHeader from '../../../../../services/auth-header';
import { getAngajatSel } from '../../../../Resources/angajatsel';

const case_de_sanatate_component = case_de_sanatate.map((casa, index) => (
	<option key={index}>{casa}</option>
));

class Contract extends React.Component {
	constructor() {
		super();
		this.handleClose = this.handleClose.bind(this);
		this.onSubmit = this.onSubmit.bind(this);
		this.hasRequired = this.hasRequired.bind(this);
		this.fillForm = this.fillForm.bind(this);
		this.getSuspendari = this.getSuspendari.bind(this);

		this.state = {
			socsel: getSocSel(),
			angajatsel: getAngajatSel(),

			id: 0,
			modelContract: 'Contract de munca', //text
			numărContract: '', //text
			marca: '', //text
			punctDeLucru: '', //text
			echipa: '', //text,
			departament: '',
			calculdeduceri: true,
			studiiSuperioare: false,
			functieBaza: true,
			normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
			salariu: '',
			monedăSalariu: 'RON', //text
			idcontbancar: null,
			iban: '',
			numebanca: '',
			condițiiMuncă: 'Normale', //text
			sindicat: false,
			cotizațieSindicat: '',
			pensiePrivată: false,
			cotizațiePensie: '',
			spor: 0,
			avans: 0,
			monedăAvans: 'RON', //text
			zileCOan: 21,
			casăSănătate: '', //text
			gradInvalid: 'valid', //text
			funcție: '', //text
			nivelStudii: '', //text
			cor: '',
			dataIncepere: '',
			dataContract: '',
			ultimazilucru: '',
			pensionar: false,

			show: false,
			showSuspendare: false,
			modalMessage: '', //text

			// superior
			angajat: null,
			superior: null,
			superiori: [],

			// centrucost
			centruCost: null,
			centreCost: [],

			showToast: false,
			toastMessage: '',

			suspendari: [],
		};
	}

	clearFields() {
		this.setState({
			id: 0,
			modelContract: 'Contract de munca', //text
			numărContract: '', //text
			marca: '', //text
			punctDeLucru: '',
			echipa: '', //text,
			departament: '',
			calculdeduceri: true,
			studiiSuperioare: false,
			functieBaza: true,
			normăLucru: { nrOre: 8, nume: 'Normă întreagă' }, //text
			monedăSalariu: 'RON', //text
			salariu: '',
			idcontbancar: null,
			iban: '',
			numebanca: '',
			condițiiMuncă: 'Normale', //text
			sindicat: false,
			cotizațieSindicat: '',
			pensiePrivată: false,
			cotizațiePensie: '',
			spor: 0,
			avans: 0,
			monedăAvans: 'RON', //text
			zileCOan: 21,
			casăSănătate: '', //text
			gradInvalid: 'valid', //text
			funcție: '', //text
			nivelStudii: '', //text
			cor: '',
			dataIncepere: '',
			dataContract: '',
			ultimazilucru: '',
			pensionar: false,

			show: false,
			showSuspendare: false,
			modalMessage: '', //text

			angajat: null,
			superior: null,
			centruCost: null,

			suspendari: [],
		});
	}

	handleClose() {
		this.setState(
			{
				show: false,
				modalMessage: '',
				showSuspendare: false,
			},
			this.props.scrollToTopSmooth
		);
	}

	getNumeNorma(nrOre) {
		switch (nrOre) {
			case 8:
				return 'Normă întreagă';
			case 7:
				return 'Normă parțială 7/8';
			case 6:
				return 'Normă parțială 6/8';
			case 5:
				return 'Normă parțială 5/8';
			case 4:
				return 'Normă parțială 4/8';
			case 3:
				return 'Normă parțială 3/8';
			case 2:
				return 'Normă parțială 2/8';
			case 1:
				return 'Normă parțială 1/8';
			default:
				break;
		}
	}

	async getSuperiori() {
		if (!this.state.angajatsel) return;

		const superiori = await axios
			.get(`${server.address}/angajat/superiori-posibili/${this.state.angajatsel.idpersoana}`, {
				headers: authHeader(),
			})
			.then((res) =>
				res.data
					.map((angajat) => ({
						id: angajat.persoana.id,
						numeintreg: angajat.persoana.nume + ' ' + angajat.persoana.prenume,
					}))
					.filter((angajat) => angajat.id !== this.state.angajatsel.idpersoana)
			)
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage:
						'Nu am putut prelua angajații superiori posibili: ' +
						(err.response
							? err.response.data.message
							: 'Nu s-a putut stabili conexiunea la server'),
				})
			);

		if (superiori) this.setState({ superiori: superiori });
	}

	async getCentreCost() {
		const centreCost = await axios
			.get(`${server.address}/centrucost/ids=${this.state.socsel.id}`, {
				headers: authHeader(),
			})
			.then((res) => (res.status === 200 ? res.data : null))
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage:
						'Nu am putut prelua centrele de cost: ' +
						(err.response
							? err.response.data.message
							: 'Nu s-a putut stabili conexiunea la server'),
				})
			);

		if (centreCost) this.setState({ centreCost: centreCost });
	}

	async getSuspendari() {
		if (!this.state.id) return;
		const suspendari = await axios
			.get(`${server.address}/suspendare/idc=${this.state.id}`, {
				headers: authHeader(),
			})
			.then((res) => res.data)
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage:
						'Nu am putut prelua suspendările: ' +
						(err.response
							? err.response.data.message
							: 'Nu s-a putut stabili conexiunea la server'),
				})
			);
		if (suspendari)
			this.setState({
				suspendari: suspendari,
			})
	}

	async fillForm() {
		this.getSuperiori();
		this.getCentreCost();
		this.getSuspendari();

		const angajatsel = getAngajatSel();
		if (!angajatsel) return;

		const angajat = await axios
			.get(`${server.address}/angajat/expand/${angajatsel.idpersoana}`, {
				headers: authHeader(),
			})
			.then((res) => res.data)
			.catch((err) =>
				this.setState({
					showToast: true,
					toastMessage:
						'Nu am putut prelua angajații: ' +
						(err.response
							? err.response.data.message
							: 'Nu s-a putut stabili conexiunea la server'),
				})
			);

		if (angajat.contract) {
			this.clearFields();
			let contract = angajat.contract;
			let superior = angajat.superior
				? {
					id: angajat.superior.persoana.id,
					numeintreg: angajat.superior.persoana.nume + ' ' + angajat.superior.persoana.prenume,
				}
				: null;
			let centruCost = contract.centrucost
				? { id: contract.centrucost.id, nume: contract.centrucost.nume }
				: null;

			this.setState(
				{
					angajat: angajat,
					superior: superior,
					angajatsel: getAngajatSel(),

					id: contract.id,
					modelContract: contract.tip || 'Contract de muncă', //text
					numărContract: contract.nr || '', //text
					marca: contract.marca || '', //text
					dataContract: contract.data ? contract.data.substring(0, 10) : '',
					dataIncepere: contract.dataincepere ? contract.dataincepere.substring(0, 10) : '',
					punctDeLucru: contract.idpunctdelucru || '',
					centruCost: centruCost,
					echipa: contract.idechipa || '',
					departament: contract.iddepartament || '',
					functieBaza: contract.functiedebaza || false,
					calculdeduceri: contract.calculdeduceri || false,
					studiiSuperioare: contract.studiisuperioare || false,
					normăLucru: { nrOre: contract.normalucru, nume: this.getNumeNorma(contract.normalucru) },
					salariu: contract.salariutarifar || '',
					condițiiMuncă: contract.conditiimunca || '',
					idcontbancar: contract.contbancar ? contract.contbancar.id : null,
					iban: contract.contbancar ? contract.contbancar.iban : '',
					numebanca: contract.contbancar ? contract.contbancar.numebanca : '',
					sindicat: contract.sindicat || false,
					cotizațieSindicat: contract.cotizatiesindicat || '',
					pensiePrivată: contract.pensieprivata || false,
					cotizațiePensie: contract.cotizatiepensieprivata || '',
					avans: contract.avans || 0,
					monedăAvans: contract.monedaavans || 'RON',
					zileCOan: contract.zilecoan || 0,
					ultimazilucru: contract.ultimazilucru ? contract.ultimazilucru.substring(0, 10) : '',
					casăSănătate: contract.casasanatate || '-',
					gradInvalid: contract.gradinvaliditate || '', //text
					funcție: contract.functie || '', //text
					nivelStudii: contract.nivelstudii || '', //text
					cor: contract.cor || '',
					pensionar: contract.pensionar || false,
					spor: contract.spor || '',
				},
				() =>
					console.log(
						'idangajat:',
						angajat.idpersoana,
						'\tidcontract:',
						contract.id,
					)
			);
		} else {
			// nu are contract
			this.clearFields();

			// if has adresa -> preselect casa_sanatate
			var adresa = angajat.persoana.adresa;
			var cs = '-';
			if (adresa) {
				if (adresa.judet) {
					if (adresa.judet.substring(0, 2) === 'SE') cs = case_de_sanatate[0];
					else cs = case_de_sanatate[judete.indexOf(adresa.judet)];
				}

				this.setState({ angajat: angajat, casăSănătate: cs }, () =>
					console.log('idangajat:', angajat.idpersoana, '\tidcontract:', null)
				);
			}
		}
	}

	componentDidMount() {
		this.fillForm();
	}

	hasRequired() {
		if (!this.state.numărContract) {
			this.setState({
				show: true,
				modalMessage: 'Contractul trebuie să aibă un număr.',
			});
			return false;
		}

		if (!this.state.salariu) {
			this.setState({
				show: true,
				modalMessage: 'Contractul trebuie să aibă un salariu.',
			});
			return false;
		}

		if (!this.state.dataContract || !this.state.dataIncepere) {
			this.setState({
				show: true,
				modalMessage: 'Contractul trebuie să aibă o dată și o dată de începere a activității.',
			});
			return false;
		}

		return true;
	}

	getCentruCostById(centruCost) {
		if (centruCost) return this.state.centreCost.find((cc) => cc.id === centruCost.id);
		else return null;
	}

	async onSubmit(e) {
		e.preventDefault();

		if (!this.hasRequired()) return;

		let contbancar_body;
		if (this.state.idcontbancar)
			contbancar_body = {
				id: this.state.idcontbancar,
				iban: this.state.iban,
				numebanca: this.state.numebanca,
			};
		else
			contbancar_body = {
				iban: this.state.iban,
				numebanca: this.state.numebanca,
			};

		let centrucost_body = this.getCentruCostById(this.state.centruCost);

		const contract_body = {
			tip: this.state.modelContract || null,
			nr: this.state.numărContract || null,
			marca: this.state.marca || null,
			data: this.state.dataContract || null,
			dataincepere: this.state.dataIncepere || null,
			centrucost: centrucost_body, //centrucost.id || null,
			idechipa: null, //echipa.id || null,
			iddepartament: null, //departament.id || null,
			functiedebaza: this.state.functieBaza,
			calculdeduceri: this.state.calculdeduceri,
			studiisuperioare: this.state.studiiSuperioare,
			normalucru: this.state.normăLucru.nrOre || null,
			salariutarifar: this.state.salariu || null,
			monedasalariu: this.state.monedăSalariu || 'RON',
			contbancar: contbancar_body,
			conditiimunca: this.state.condițiiMuncă || null,
			sindicat: this.state.sindicat,
			cotizatiesindicat: this.state.cotizațieSindicat || null,
			pensieprivata: this.state.pensiePrivată,
			cotizatiepensieprivata: this.state.cotizațiePensie || null,
			avans: this.state.avans || null,
			monedaavans: this.state.monedăAvans || 'RON',
			zilecoan: this.state.zileCOan || 21,
			ultimazilucru: this.state.ultimazilucru === '' ? null : this.state.ultimazilucru || null,
			casasanatate: this.state.casăSănătate || null,
			gradinvaliditate: this.state.gradInvalid || null,
			functie: this.state.funcție || null,
			nivelstudii: this.state.nivelStudii || null,
			cor: this.state.cor || null,
			pensionar: this.state.pensionar,
			spor: this.state.spor || null,
		};

		let contract = null;
		if (this.state.angajat.contract) {
			contract = await axios
				.put(`${server.address}/contract/${this.state.id}`, contract_body, {
					headers: authHeader(),
				})
				.then((res) => (res.status === 200 ? res.data : null))
				.catch((err) =>
					this.setState({
						showToast: true,
						toastMessage:
							'Nu am putut actualiza contractul: ' +
							(err.response
								? err.response.data.message
								: 'Nu s-a putut stabili conexiunea la server'),
					})
				);
		} else {
			contract = await axios
				.post(`${server.address}/contract/${this.state.angajat.idpersoana}`, contract_body, {
					headers: authHeader(),
				})
				.then((res) => (res.status === 200 ? res.data : null))
				.catch((err) =>
					this.setState({
						showToast: true,
						toastMessage:
							'Nu am putut adăuga contractul: ' +
							(err.response
								? err.response.data.message
								: 'Nu s-a putut stabili conexiunea la server'),
					})
				);
		}

		if (contract) {
			// update superior -> angajatul are superior, nu tine de contract
			if (this.state.superior) {
				await axios
					.put(
						`${server.address}/angajat/superior/${this.state.angajat.idpersoana}&${this.state.superior.id}`,
						{},
						{ headers: authHeader() }
					)
					.then((res) => res.status === 200)
					.catch((err) =>
						this.setState({
							showToast: true,
							toastMessage:
								'Nu am putut actualiza superiorul: ' +
								(err.response
									? err.response.data.message
									: 'Nu s-a putut stabili conexiunea la server'),
						})
					);
			}

			this.setState({
				show: true,
				modalMessage: this.state.id ? 'Contract actualizat 💾' : 'Contract adăugat  📄',
				id: contract.id,
			});
		}
	}

	onChangeSuperior(e) {
		if (e.target.value === '-') {
			this.setState({ superior: null });
			return;
		}
		const selectedIndex = e.target.options.selectedIndex;
		const idsuperior = e.target.options[selectedIndex].getAttribute('data-key');
		this.setState({ superior: { id: Number(idsuperior), numeintreg: e.target.value } });
	}

	onChangeCentruCost(e) {
		if (e.target.value === '-') {
			this.setState({ centruCost: null });
			return;
		}
		const selectedIndex = e.target.options.selectedIndex;
		const idcentrucost = e.target.options[selectedIndex].getAttribute('data-key');
		this.setState({ centruCost: { id: Number(idcentrucost), nume: e.target.value } });
	}

	render() {
		const superioriComponent = this.state.superiori.map((superior) => (
			<option key={superior.id} data-key={superior.id}>
				{superior.numeintreg}
			</option>
		));

		const centreCostComponent = this.state.centreCost.map((centruCost) => (
			<option key={centruCost.id} data-key={centruCost.id}>
				{centruCost.nume}
			</option>
		));

		const tabel_ore = this.state.suspendari.map((sus, index) => {
			for (let key in sus) if (!sus[key]) sus[key] = '-';

			return (
				<tr key={sus.id}>
					<th>{sus.dela}</th>
					<th>{sus.panala}</th>
					<th className="d-inline-flex flex-row justify-content-around">
						<PopupState variant="popover" popupId="demo-popup-popover">
							{(popupState) => (
								<div>
									<Button
										variant="outline-secondary"
										className="m-0 p-1 rounded-circle border-0"
										{...bindTrigger(popupState)}
									>
										<Trash2 fontSize="small" />
									</Button>
									<Popover
										{...bindPopover(popupState)}
										anchorOrigin={{
											vertical: 'bottom',
											horizontal: 'center',
										}}
										transformOrigin={{
											vertical: 'top',
											horizontal: 'center',
										}}
									>
										<Box p={2}>
											<Typography>Confirmare ștergere</Typography>
											<Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
											<br />
											<Button
												variant="outline-danger"
												onClick={() => {
													popupState.close();
													console.log('Am șters șmecherie');
												}}
												className="mt-2 "
											>
												Da
                      </Button>
											<Button
												variant="outline-persondary"
												onClick={popupState.close}
												className="mt-2"
											>
												Nu
                      </Button>
										</Box>
									</Popover>
								</div>
							)}
						</PopupState>
					</th>
				</tr>
			);
		});

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
					<Toast.Body>
						{this.state.toastMessage}
						{/* <Button variant="light">Repara scriind valori predefinite</Button> */}
					</Toast.Body>
				</Toast>

				<Modal show={this.state.show} onHide={this.handleClose}>
					<Modal.Header closeButton>
						<Modal.Title>Mesaj</Modal.Title>
					</Modal.Header>
					<Modal.Body>{this.state.modalMessage}</Modal.Body>
					<Modal.Footer>
						<Button variant="primary" onClick={this.handleClose}>
							OK
            </Button>
					</Modal.Footer>
				</Modal>

				<Modal size="lg" show={this.state.showSuspendare} onHide={this.handleClose}>
					<Modal.Header closeButton>
						<Modal.Title>Suspendări Contract</Modal.Title>
					</Modal.Header>
					<Modal.Body>
						Salut
            <Table responsive hover>
							<thead>
								<tr>
									<th>De la</th>
									<th>Până la</th>
								</tr>
							</thead>
							<tbody>{tabel_ore}</tbody>
						</Table>
					</Modal.Body>
				</Modal>

				<Form onSubmit={(e) => this.onSubmit(e)}>
					<Row>
						<Col md={12}>
							<Form.Group controlId="functia">
								<Form.Label>Funcție</Form.Label>
								<Form.Control
									placeholder="functia"
									value={this.state.funcție || ''}
									onChange={(e) => {
										this.setState({ funcție: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group controlId="tip">
								<Form.Label>Model Contract</Form.Label>
								<InputGroup>
									<Form.Control
										as="select"
										value={this.state.modelContract}
										onChange={(e) => {
											this.setState({ modelContract: e.target.value });
										}}
									>
										<option>Contract de muncă</option>
										<option>Contract de administrator</option>
										<option>Convenție civilă</option>
										<option>Drepturi de autor</option>
										<option>Figuranți / Zilieri</option>
									</Form.Control>
									<InputGroup.Append>
										<Button
											variant="outline-info"
											disabled={!this.state.angajatsel || !this.state.id}
											onClick={() => this.setState({ showSuspendare: true })}
										>
											Suspendări
                    </Button>
									</InputGroup.Append>
								</InputGroup>
							</Form.Group>
						</Col>
						{/* <Col md={12} /> */}
						<Col md={3}>
							<Form.Group controlId="nrcontract">
								<Form.Label>Număr contract</Form.Label>
								<Form.Control
									required
									type="text"
									placeholder="Număr contract"
									value={this.state.numărContract || ''}
									onChange={(e) => {
										this.setState({ numărContract: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>
						{/* <Col md={12} /> */}
						<Col md={3}>
							<Form.Group controlId="marca">
								<Form.Label>Marca</Form.Label>
								<Form.Control
									type="text"
									placeholder="Marca"
									value={this.state.marca || ''}
									onChange={(e) => {
										this.setState({ marca: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group controlId="dataincepere">
								<Form.Label>Data începere activitate</Form.Label>
								<Form.Control
									type="date"
									value={this.state.dataIncepere || ''}
									onChange={(e) => {
										this.setState({ dataIncepere: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group controlId="datacontract">
								<Form.Label>Data contract</Form.Label>
								<Form.Control
									type="date"
									value={this.state.dataContract || ''}
									selected={this.state.dataContract}
									onChange={(e) => {
										this.setState({ dataContract: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
							<Form.Group id="functiedabaza">
								<Form.Label>
									<Form.Check
										custom
										type="switch"
										id="functieDeBazaCheck"
										label="Funcție de bază"
										checked={this.state.functieBaza || false}
										onChange={(e) => {
											this.setState({ functieBaza: e.target.checked });
										}}
										size="sm"
									/>
								</Form.Label>
							</Form.Group>
						</Col>
						<Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
							<Form.Group controlId="calculdeduceri">
								<Form.Check
									custom
									type="switch"
									id="deduceriCheck"
									label="Platește impozit"
									checked={this.state.calculdeduceri || false}
									onChange={(e) => {
										this.setState({ calculdeduceri: e.target.checked });
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
							<Form.Group controlId="studiisuperioare">
								<Form.Check
									custom
									type="switch"
									id="studiiSuperioareCheck"
									label="Studii superioare"
									checked={this.state.studiiSuperioare || false}
									onChange={(e) => {
										this.setState({
											studiiSuperioare: e.target.checked,
										});
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
							<Form.Group id="pensionar">
								<Form.Check
									custom
									type="switch"
									id="pensionarCheck"
									label="Pensionar"
									checked={this.state.pensionar || false}
									onChange={(e) => {
										this.setState({ pensionar: e.target.checked });
									}}
								/>
							</Form.Group>
						</Col>
						<Col md={12} />

						<Col md={6}>
							<Form.Group id="normalucru">
								<Form.Label>Normă de lucru</Form.Label>
								<Form.Control
									as="select"
									value={this.state.normăLucru.nume || 'Normă întreagă'}
									onChange={(e) => {
										this.setState(
											{
												normăLucru: {
													nrOre: 8 - e.target.options.selectedIndex,
													nume: e.target.value,
												},
											},
											() => console.log(this.state.normăLucru)
										);
									}}
								>
									<option>Normă întreagă</option>
									<option>Normă parțială 7/8</option>
									<option>Normă parțială 6/8</option>
									<option>Normă parțială 5/8</option>
									<option>Normă parțială 4/8</option>
									<option>Normă parțială 3/8</option>
									<option>Normă parțială 2/8</option>
									<option>Normă parțială 1/8</option>
								</Form.Control>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group id="zilecoan">
								<Form.Label>Zile CO/an</Form.Label>
								<Form.Control
									placeholder="0"
									type="number"
									value={this.state.zileCOan || 0}
									onChange={(e) => {
										this.setState({ zileCOan: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="salariu">
								<Form.Label>Salariu</Form.Label>
								<InputGroup>
									<FormControl
										type="number"
										required
										placeholder="Salariu"
										aria-label="Salariu"
										aria-describedby="basic-addon2"
										value={this.state.salariu || 0}
										onChange={(e) => this.setState({ salariu: e.target.value })}
									/>

									<DropdownButton
										as={InputGroup.Append}
										title={this.state.monedăSalariu || 'RON'}
										id="monedasalariu"
									>
										<Dropdown.Item
											onClick={() =>
												this.setState({
													monedăSalariu: 'RON',
												})
											}
										>
											RON
                    </Dropdown.Item>
										<Dropdown.Item
											onClick={() =>
												this.setState({
													monedăSalariu: 'EUR',
												})
											}
										>
											EUR
                    </Dropdown.Item>
									</DropdownButton>
								</InputGroup>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group id="conditiidemunca">
								<Form.Label>Condiții de muncă</Form.Label>
								<Form.Control
									as="select"
									value={this.state.condițiiMuncă || 'Normale'}
									onChange={(e) => {
										this.setState({ condițiiMuncă: e.target.value });
									}}
								>
									<option>Normale</option>
									<option>Deosebite</option>
								</Form.Control>
							</Form.Group>
						</Col>
						<Col md={12} className="border rounded pt-3">
							<Typography variant="body1" className="border-bottom mb-3" gutterBottom>
								Cont bancar
              </Typography>
							<Row>
								<Col md={6}>
									<Form.Group id="iban">
										<Form.Label>IBAN</Form.Label>
										<Form.Control
											type="text"
											value={this.state.iban || ''}
											style={{ fontFamily: 'Consolas, Courier New' }}
											onChange={(e) => {
												this.setState({ iban: e.target.value });
											}}
										/>
									</Form.Group>
								</Col>
								<Col md={6}>
									<Form.Group controlId="numebanca">
										<Form.Label>Nume bancă</Form.Label>
										<Form.Control
											type="text"
											value={this.state.numebanca || ''}
											onChange={(e) => {
												this.setState({ numebanca: e.target.value });
											}}
										/>
									</Form.Group>
								</Col>
							</Row>
						</Col>
						<Col md={6}>
							<Form.Group controlId="punctdelucru">
								<Form.Label>Punct de lucru</Form.Label>
								<Form.Control
									as="select"
									value={this.state.punctDeLucru || '-'}
									onChange={(e) => {
										this.setState({ punctDeLucru: e.target.value });
									}}
								>
									<option>-</option>
									{/* TODO fetch names from db and list here */}
									{/* TODO 'add punct de lucru' button */}
								</Form.Control>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group controlId="centrucost">
								<Form.Label>Centre de cost</Form.Label>
								<Form.Control
									as="select"
									value={this.state.centruCost ? this.state.centruCost.nume : '-'}
									onChange={(e) => this.onChangeCentruCost(e)}
								>
									<option>-</option>
									{centreCostComponent}
								</Form.Control>
							</Form.Group>
						</Col>
						{/* <Col md={6}>
              <Form.Group controlId="echipa">
                <Form.Label>Echipa</Form.Label>
                <Typeahead
                  id="optiune-echipa"
                  options={['echipa test']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.echipa || ''}
                  onChange={(selected) => {
                    this.setState({ echipa: selected });
                  }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="departament">
                <Form.Label>Departament</Form.Label>
                <Typeahead
                  id="optiune-departament"
                  options={['departament test']}
                  allowNew
                  newSelectionPrefix="Adaugă"
                  value={this.state.departament}
                  onChange={(selected) => {
                    this.setState({ departament: selected });
                  }}
                />
              </Form.Group>
            </Col> */}

						<Col md={12} />
						<Col md={1}>
							<Form.Group id="sindicat" style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}>
								<Form.Check
									custom
									type="switch"
									id="sindicatCheck"
									label="Sindicat"
									checked={this.state.sindicat}
									onChange={(e) => {
										this.setState({ sindicat: e.target.checked });
									}}
								/>
							</Form.Group>
						</Col>
						{this.state.sindicat ? (
							<Col md={3}>
								<Form.Group id="cotizatiesindicat">
									<Form.Label>Cotizație sindicat</Form.Label>
									<Form.Control
										placeholder="0"
										value={this.state.cotizațieSindicat}
										onChange={(e) => {
											this.setState({ cotizațieSindicat: e.target.value });
										}}
									/>
								</Form.Group>
							</Col>
						) : null}
						<Col md={2} />
						<Col md={1.5}>
							<Form.Group
								id="pensieprivata"
								style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}
							>
								<Form.Check
									custom
									type="switch"
									id="pensiePrivataCheck"
									label="Pensie privată"
									checked={this.state.pensiePrivată}
									onChange={(e) => {
										this.setState({ pensiePrivată: e.target.checked });
									}}
								/>
							</Form.Group>
						</Col>
						{this.state.pensiePrivată ? (
							<Col md={3}>
								<Form.Group id="cotizatiepensieprivata">
									<Form.Label>Cotizație pensie privată</Form.Label>
									<Form.Control
										placeholder="0"
										value={this.state.cotizațiePensie}
										onChange={(e) => {
											this.setState({
												cotizațiePensie: e.target.value,
											});
										}}
									/>
								</Form.Group>
							</Col>
						) : null}
						<Col md={12} />

						<Col md={6}>
							<Form.Group id="sporuri">
								<Form.Label>Sporuri permanente</Form.Label>
								<Form.Control
									as="select"
									value={this.state.spor}
									onChange={(e) => {
										this.setState({ spor: e.target.value });
									}}
								>
									<option>-</option>
									<option>Spor sărbători legale</option>
									<option>Spor ore de noapte</option>
									<option>Spor de weekend</option>
								</Form.Control>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="avans">
								<Form.Label>Avans</Form.Label>
								<InputGroup>
									<FormControl
										type="number"
										placeholder="Avans"
										aria-label="Avans"
										aria-describedby="basic-addon2"
										value={this.state.value}
										onChange={(e) => this.setState({ avans: e.target.value })}
									/>

									<DropdownButton
										as={InputGroup.Append}
										title={this.state.monedăAvans}
										id="monedaavans"
									>
										<Dropdown.Item
											onClick={() =>
												this.setState({
													monedăAvans: 'RON',
												})
											}
										>
											RON
                    </Dropdown.Item>
										<Dropdown.Item
											onClick={() =>
												this.setState({
													monedăAvans: 'EUR',
												})
											}
										>
											EUR
                    </Dropdown.Item>
									</DropdownButton>
								</InputGroup>
							</Form.Group>
						</Col>

						<Col md={12} />

						<Col md={10} />

						<Col md={6}>
							<Form.Group id="ultimazilucru">
								<Form.Label>Data încetării</Form.Label>
								<Form.Control
									type="date"
									placeholder="data"
									value={this.state.ultimazilucru}
									onChange={(e) => {
										this.setState({ ultimazilucru: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="casasanatate">
								<Form.Label>Casa de sănătate</Form.Label>
								<Form.Control
									as="select"
									value={this.state.casăSănătate}
									onChange={(e) => {
										this.setState({ casăSănătate: e.target.value });
									}}
								>
									<option>-</option>
									{case_de_sanatate_component}
								</Form.Control>
							</Form.Group>
						</Col>
						<Col md={6}>
							<Form.Group id="gradinvaliditate">
								<Form.Label>Grad invaliditate</Form.Label>
								<Form.Control
									as="select"
									value={this.state.gradInvalid}
									onChange={(e) => {
										this.setState({ gradInvalid: e.target.value });
									}}
								>
									<option>valid</option>
									<option>invalid</option>
								</Form.Control>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="nivelstudii">
								<Form.Label>Nivel studii</Form.Label>
								<Form.Control
									as="select"
									value={this.state.nivelStudii}
									onChange={(e) => {
										this.setState({ nivelStudii: e.target.value });
									}}
								>
									<option>-</option>
									<option>Gimnaziale</option>
									<option>Medii</option>
									<option>Superioare</option>
								</Form.Control>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="cor">
								<Form.Label>Cod COR</Form.Label>
								<Form.Control
									type="text"
									placeholder="cod COR"
									value={this.state.cor}
									onChange={(e) => {
										this.setState({ cor: e.target.value });
									}}
								/>
							</Form.Group>
						</Col>

						<Col md={6}>
							<Form.Group id="superior">
								<Form.Label>Superior</Form.Label>
								<Form.Control
									as="select"
									value={this.state.superior ? this.state.superior.numeintreg : '-'}
									onChange={(e) => this.onChangeSuperior(e)}
								>
									<option>-</option>
									{superioriComponent}
								</Form.Control>
							</Form.Group>
						</Col>
					</Row>

					<Row>
						<Col md={6}>
							<Button
								variant={!this.state.angajatsel ? 'outline-dark' : 'outline-primary'}
								onClick={(e) => this.onSubmit(e)}
								disabled={!this.state.angajatsel}
							>
								{this.state.id ? 'Actualizează contract' : 'Adaugă contract'}
							</Button>
						</Col>
					</Row>
				</Form>
			</React.Fragment>
		);
	}
}

export default Contract;
