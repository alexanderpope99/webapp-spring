import React from 'react';
import Aux from '../../hoc/_Aux';
import { Card } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';

import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class Notificari extends React.Component {
  constructor() {
    super();
    this.state = {
      socsel: getSocSel(),
      user: authService.getCurrentUser(),
    };
  }

  async onRefresh() {
    // eslint-disable-next-line no-unused-vars
    const notificari = await axios
      .get(`${server.address}/notificare/userid/${this.state.user.id}`, { withCredentials: true })
      .then((res) => res.data)
      .catch((err) => console.error(err));
  }

  render() {
    return (
      <Aux>
        <Card className="border">
          <Card.Header>
            <Typography variant="h6">{this.state.socsel.nume}</Typography>
          </Card.Header>
        </Card>
      </Aux>
    );
  }
}

export default Notificari;
