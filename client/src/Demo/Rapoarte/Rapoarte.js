import React from 'react';
import { Link } from 'react-router-dom';
import Aux from '../../hoc/_Aux';
import { Breadcrumb } from 'react-bootstrap';
import { getSocSel } from '../Resources/socsel';
import PlatiSalariiMTA from './PlatiSalariiMTA.js';
import Dec112 from './Dec112.js';
import Pontaj from './Pontaj.js';
import Stat from './Stat.js';
import NotaContabila from './NotaContabila.js';
import Tichete from './Tichete.js';
import ListaAngajati from './ListaAngajati.js';
import Centralizator from './Centralizator.js';

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
        <Breadcrumb style={{ fontSize: '12px' }}>
          <Breadcrumb.Item linkAs={Link} linkProps={{ to: '/dashboard/societati' }}>{this.state.socsel.nume}</Breadcrumb.Item>
          <Breadcrumb.Item active>Rapoarte</Breadcrumb.Item>
        </Breadcrumb>
        <Stat />
        <Tichete />
        <ListaAngajati />
        <Centralizator />
        <NotaContabila />
        <Pontaj />
        <Dec112 />
        <PlatiSalariiMTA />
      </Aux>
    );
  }
}

export default Rapoarte;
