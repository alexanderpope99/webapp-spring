import React from 'react';
import Aux from '../../hoc/_Aux';
import { getSocSel } from '../Resources/socsel';
import AdeverintaVenit from './AdeverintaVenit';

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
        <AdeverintaVenit />
      </Aux>
    );
  }
}

export default Rapoarte;
