
import React from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import { getAngajatSel } from '../../../../Resources/angajatsel';
import { getSocSel } from '../../../../Resources/socsel';
import axios from 'axios';
import { server } from '../../../../Resources/server-address';
import authHeader from '../../../../../services/auth-header';
import {
	Card,
	Toast,
	OverlayTrigger,
	Button,
	Tooltip,
} from 'react-bootstrap';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';
import { Trash2 } from 'react-feather';

export default class IstoricContracte extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			socsel: getSocSel(),
			angajatsel: getAngajatSel(),
			contracte: [],

			showToast: false,
			toastMessage: '',
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

	buttons = (cell, row, rowIndex) => (
		<div className="d-inline-flex">
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
		</div>
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