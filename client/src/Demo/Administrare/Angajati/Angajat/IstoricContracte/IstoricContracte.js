
import React from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import { getAngajatSel } from '../../../../Resources/angajatsel';
import { getSocSel } from '../../../../Resources/socsel';
import axios from 'axios';
import { server } from '../../../../Resources/server-address';
import authHeader from '../../../../../services/auth-header';
import { Typeahead } from 'react-bootstrap-typeahead';
import {
	Card,
	Toast,
	OverlayTrigger,
	Button,
	Tooltip,
	Modal,
	Form,
	Row,
	Col,
	FormControl,
} from 'react-bootstrap';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import { Trash2,Eye } from 'react-feather';

export default class IstoricContracte extends React.Component {

	constructor(props) {
		super(props);

		this.handleClose = this.handleClose.bind(this);
		this.showContract = this.showContract.bind(this);

		this.state = {
			socsel: getSocSel(),
			angajatsel: getAngajatSel(),
			contracte: [],

			showToast: false,
			toastMessage: '',
			showModal: false,

			contract: {
				tip: '',
				nr: '',
				marca: '',
				data: '',
				dataincepere: '',
				functiedebaza: '',
				calculdeduceri: '',
				studiisuperioare: '',
				normalucru: '',
				salariutarifar: '',
				monedasalariu: '',
				conditiimunca: '',
				pensieprivata: '',
				cotizatiepensieprivata: '',
				avans: '',
				monedaavans: '',
				zilecoan: '',
				ultimazilucru: '',
				casasanatate: '',
				gradinvaliditate: '',
				functie: '',
				nivelstudii: '',
				cor: '',
				sindicat: '',
				cotizatiesindicat: '',
				spor: '',
				pensionar: '',
			}
		}
	}

	componentDidMount() {
		this.getIstoricContracte();
	}

	async getIstoricContracte() {
		const contracte = await axios
			.get(`${server.address}/istoric-contract/ida=${this.state.angajatsel.idpersoana}`, { headers: authHeader() })
			.then(res => res.data)
			.catch(err =>
				this.setState({
					showToast: true,
					toastMessage: 'Nu am putut prelua contractele: ' + (err.response
						? err.response.data.message
						: 'Nu s-a putut stabili conexiunea la server'),
				})
			);

		if (contracte) {
			this.setState({
				contracte: [...contracte],
			});
		} else this.setState({ contracte: [] });
	}

	async deleteContract(id) {
		const ok = await axios
			.delete(`${server.address}/istoric-contract/${id}`, { headers: authHeader() })
			.then(res => res.status === 200)
			.catch(err =>
				this.setState({
					showToast: true,
					toastMessage: 'Nu am putut șterge contractul: ' + (err.response
						? err.response.data.message
						: 'Nu s-a putut stabili conexiunea la server'),
				}));

		if (ok) this.getIstoricContracte();
	}

	async showContract(shownContract) {
		this.setState({
			showModal:true,
			contract: {...shownContract},
		})
	}

	handleClose() {
		this.setState({
		  showModal: false,
		});
	}

	buttons = (cell, row, rowIndex) => (
		<Row>
			<Button
                    onClick={() => this.showContract(row.contract)}
                    variant="outline-secondary"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <Eye size={20} />
             </Button>
			<PopupState variant="popover" popupId="demo-popup-popover">
				{(popupState) => (
					<div>
						<OverlayTrigger
							placement="bottom"
							overlay={
								<Tooltip id="delete-button" style={{ opacity: '.4' }}>
									Șterge
								</Tooltip>
							}
						>
							<Button
								variant="outline-secondary"
								className="m-0 p-1 rounded-circle border-0"
								{...bindTrigger(popupState)}
							>
								<Trash2 size={20} />
							</Button>
						</OverlayTrigger>
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
								<Typography>
									Sigur ștergeți contractul?
								</Typography>
								<Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
								<br />
								<Button
									variant="outline-danger"
									onClick={() => {
										popupState.close();
										this.deleteContract(row.id);
									}}
									className="mt-2 "
								>
									Da
								</Button>
								<Button variant="outline-persondary" onClick={popupState.close} className="mt-2">
									Nu
								</Button>
							</Box>
						</Popover>
					</div>
				)}
			</PopupState>
		</Row>
	);

	columns = [
		{
			dataField: 'any',
			text: '#',
			formatter: (cell, row, rowIndex) => rowIndex + 1,
			headerStyle: { width: '50px' },
		},
		{
			dataField: 'contract.salariutarifar',
			text: 'Salariu (RON)',
			sort: true,
		},
		{
			dataField: 'contract.functie',
			text: 'Funcție',
			sort: true,
		},
		{
			dataField: 'contract.normalucru',
			text: 'Norma lucru (ore)',
			sort: true,
		},
		{
			dataField: 'dataModificarii',
			text: 'Până în',
			sort: true,
		},
		{
			dataField: '',
			text: 'Acțiuni',
			formatter: this.buttons,
		},
	];

	render() {
		return (
			<>
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

				<Modal show={this.state.showModal} onHide={this.handleClose} size="xl">
				<Modal.Header closeButton>
					<Modal.Title>Date Contract</Modal.Title>
				</Modal.Header>
				<Modal.Body>
				<Form onSubmit={(e) => e.preventDefault()}>
          <Row>
            <Col md={12}>
              <Form.Group controlId="functia">
                <Form.Label>Funcție</Form.Label>
                <Form.Control disabled placeholder="functia" value={this.state.contract.functie} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="tip">
                <Form.Label>Model Contract</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.tip} />
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="nrcontract">
                <Form.Label>Număr contract</Form.Label>
                <Form.Control
                  disabled
                  type="text"
                  placeholder="Număr contract"
                  value={this.state.contract.nr}
                />
              </Form.Group>
            </Col>
            {/* <Col md={12} /> */}
            <Col md={3}>
              <Form.Group controlId="marca">
                <Form.Label>Marca</Form.Label>
                <Form.Control disabled type="text" placeholder="Marca" value={this.state.contract.marca} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group controlId="dataincepere">
                <Form.Label>Data începere activitate</Form.Label>
                <Form.Control disabled type="date" value={this.state.contract.dataincepere} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group controlId="datacontract">
                <Form.Label>Data contract</Form.Label>
                <Form.Control
                  disabled
                  type="date"
                  value={this.state.contract.data}
                  selected={this.state.contract.data}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group id="functiedabaza">
                <Form.Label>
                  <Form.Check
                    disabled
                    custom
                    type="switch"
                    id="functieDeBazaCheck"
                    label="Funcție de bază"
                    checked={this.state.contract.functiedebaza}
                    size="sm"
                  />
                </Form.Label>
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="calculdeduceri">
                <Form.Check
                  disabled
                  custom
                  type="switch"
                  id="deduceriCheck"
                  label="Calcul deduceri"
                  checked={this.state.contract.calculdeduceri}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group controlId="studiisuperioare">
                <Form.Check
                  disabled
                  custom
                  type="switch"
                  id="studiiSuperioareCheck"
                  label="Studii superioare"
                  checked={this.state.contract.studiisuperioare}
                />
              </Form.Group>
            </Col>
            <Col md={3} style={{ paddingBottom: '1rem', paddingTop: '1rem' }}>
              <Form.Group id="pensionar">
                <Form.Check
                  disabled
                  custom
                  type="switch"
                  id="pensionarCheck"
                  label="Pensionar"
                  checked={this.state.contract.pensionar}
                />
              </Form.Group>
            </Col>
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="normalucru">
                <Form.Label>Normă de lucru</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.normalucru} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="zilecoan">
                <Form.Label>Zile CO/an</Form.Label>
                <Form.Control disabled placeholder="0" type="number" value={this.state.contract.zilecoan} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="salariu">
                <Form.Label>Salariu</Form.Label>
                <FormControl
                  disabled
                  type="number"
                  required
                  placeholder="Salariu"
                  value={this.state.contract.salariutarifar}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="conditiidemunca">
                <Form.Label>Condiții de muncă</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.conditiimunca} />
              </Form.Group>
            </Col>

            <Col md={12} />
            <Col md={1}>
              <Form.Group id="sindicat" style={{ paddingTop: '2.5rem', paddingBottom: '0.5rem' }}>
                <Form.Check disabled custom type="switch" id="sindicatCheck" label="Sindicat" />
              </Form.Group>
            </Col>
            {this.state.contract.sindicat ? (
              <Col md={3}>
                <Form.Group id="cotizatiesindicat">
                  <Form.Label>Cotizație sindicat</Form.Label>
                  <Form.Control disabled placeholder="0" value={this.state.contract.cotizatiesindicat} />
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
                  disabled
                  custom
                  type="switch"
                  id="pensiePrivataCheck"
                  label="Pensie privată"
                  checked={this.state.contract.pensieprivata}
                />
              </Form.Group>
            </Col>
            {this.state.contract.pensiveprivata ? (
              <Col md={3}>
                <Form.Group id="cotizatiepensieprivata">
                  <Form.Label>Cotizație pensie privată</Form.Label>
                  <Form.Control disabled placeholder="0" value={this.state.contract.cotizatiepensieprivata} />
                </Form.Group>
              </Col>
            ) : null}
            <Col md={12} />

            <Col md={6}>
              <Form.Group id="sporuri">
                <Form.Label>Sporuri permanente</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.spor} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="avans">
                <Form.Label>Avans</Form.Label>
                <FormControl
                  disabled
                  type="number"
                  placeholder="Avans"
                  aria-label="Avans"
                  aria-describedby="basic-addon2"
                  value={this.state.contract.avans}
                />
              </Form.Group>
            </Col>

            <Col md={12} />

            <Col md={10} />

            <Col md={6}>
              <Form.Group id="ultimazilucru">
                <Form.Label>Ultima zi de lucru</Form.Label>
                <Form.Control
                  disabled
                  type="date"
                  placeholder="data"
                  value={this.state.contract.ultimazilucru}
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="casasanatate">
                <Form.Label>Casa de sănătate</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.casasanatate} />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group id="gradinvaliditate">
                <Form.Label>Grad invaliditate</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.gradinvaliditate} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="nivelstudii">
                <Form.Label>Nivel studii</Form.Label>
                <Form.Control disabled type="text" value={this.state.contract.nivelstudii} />
              </Form.Group>
            </Col>

            <Col md={6}>
              <Form.Group id="cor">
                <Form.Label>Cod COR</Form.Label>
                <Form.Control disabled type="text" placeholder="cod COR" value={this.state.contract.cor} />
              </Form.Group>
            </Col>
          </Row>
        </Form>
				</Modal.Body>
				</Modal>
		  

				<Card>
					<Card.Header className="border-0">
						<Card.Title as="h5">Istoric Contracte</Card.Title>
					</Card.Header>

					<Card.Body>
						<BootstrapTable
							bootstrap4
							keyField="id"
							data={this.state.contracte}
							columns={this.columns}
							wrapperClasses="table-responsive"
							hover
							bordered={false}
						/>
					</Card.Body>
				</Card>
			</>
		)
	}
}