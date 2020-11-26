import React from 'react';
import Aux from '../../hoc/_Aux';
import { Card } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { getSocSel } from '../Resources/socsel';
import PlatiSalariiMTA from './PlatiSalariiMTA.js';
import Dec112 from './Dec112.js';
import Pontaj from './Pontaj.js';
import Stat from './Stat.js';
import NotaContabila from './NotaContabila.js';

/*
  ? how it works now:
	*	Angajat.js displays, and preselects, sessionStorage.selectedAngajat :: {numeintreg, idpersoana}
	* * * * *
  * fetch date contract when focusing tab 'contract'
  * * * * *
  * when focusing 'contract' check if person has contract:
  *   |> has contract: 1. method = 'PUT'
  *                    2. populate form with contract data
	* 											\ if persoana has adresa.judet != SECTOR -> preselect casa_sanatate
  *
  *   |>  no contract: 1. method = 'POST'
  *                    2. clearFields()
	* * * * *
	* in EditPersoana -> when selecting angajat, remember selectednume in sessionstorage
	* * * * *
*/

class Rapoarte extends React.Component {
  constructor() {
    super();
    if (!getSocSel()) window.location.href = '/dashboard/societati';
    this.state = {
      socsel: getSocSel(),
    };
  }

  render() {
    return (
      <Aux>
        <Card className="border">
          <Card.Header>
            <Typography variant="h6">Societatea - {this.state.socsel.nume}</Typography>
          </Card.Header>
        </Card>
        <Stat />
        <NotaContabila />
        <Pontaj />
        <Dec112 />
        <PlatiSalariiMTA />
      </Aux>
    );
  }
}

export default Rapoarte;
