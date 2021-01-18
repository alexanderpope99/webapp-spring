import React from 'react';
import Aux from '../../hoc/_Aux';
import { Button, Toast } from 'react-bootstrap';
import { server } from '../Resources/server-address';

import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class DataIntegrityButtons extends React.Component {
	constructor() {
		super();

		this.fixContracte = this.fixContracte.bind(this);

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
		}
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
      </Aux>
    );
  }
}

export default DataIntegrityButtons;
