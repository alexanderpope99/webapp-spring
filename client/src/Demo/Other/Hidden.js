import React from 'react';
import Aux from '../../hoc/_Aux';
import { Button, Toast } from 'react-bootstrap';
import { server } from '../Resources/server-address';

import axios from 'axios';
import authHeader from '../../services/auth-header';

class Hidden extends React.Component {
	constructor() {
		super();

		this.fixContracte = this.fixContracte.bind(this);
		this.initRoles = this.initRoles.bind(this);
		this.solveConcedii = this.solveConcedii.bind(this);

		this.state = {
			toastMessage: '',
			showToast: false,
		}
	}

	async fixContracte() {
		const ok = await axios.get(`${server.address}/contract/fix-missing-values`, {headers: authHeader()})
			.then(res => res.data)
			.catch(err => console.error(err));
		if(ok) {
			this.setState({
				toastMessage: 'Contractele au fost completate',
				showToast: true,
			})
		} else {
			this.setState({
				toastMessage: 'O valoare care nu poate fi predefinita lipseste. Aceasta poate fi  ',
				showToast: true,
			})
		}
	}

	async solveConcedii() {
		const ok = await axios.get(`${server.address}/co/fix--concedii`, {headers: authHeader()})
			.then(res => res.data)
			.catch(err => console.error(err));
		if(ok) {
			this.setState({
				toastMessage: 'Concediile au fost rezolvate',
				showToast: true,
			})
		} else {
			this.setState({
				toastMessage: 'A apărut o eroare',
				showToast: true,
			})
		}
	}

	async initRoles() {
		const ok = axios
			.get(`${server.address}/init/roles`, {headers: authHeader()})
			.then(res => res.status === 200)
			.catch(err => console.error(err));
		if(ok) 
			this.setState({
				toastMessage: 'Rolurile au fost initializate',
				showToast: true,
			})
	}

  render() {
    return (
      <Aux>
				<Toast
					onClose={() => this.setState({ showToast: false })}
					show={this.state.showToast}
					delay={5000}
					autohide
					className="position-fixed"
					style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
				>
					<Toast.Header className="pr-2">
						<strong className="mr-auto">Verificat</strong>
					</Toast.Header>
					<Toast.Body>{this.state.toastMessage}</Toast.Body>
				</Toast>

        <Button onClick={this.fixContracte}>Introdu date prestabilite in contracte acolo unde lipsesc</Button>
        <Button onClick={this.initRoles}>Initializeaza rolurile</Button>
		<Button onClick={this.solveConcedii}>Repară concediile</Button>
      </Aux>
    );
  }
}

export default Hidden;
