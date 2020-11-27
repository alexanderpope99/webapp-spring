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
            <Typography variant="h6">{this.state.socsel.nume}</Typography>
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
