import React from 'react';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { setSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class SocietatiTabel extends React.Component {
  constructor() {
    super();

    this.onRefresh = this.onRefresh.bind(this);

    this.state = {
      societati: [],
      societatiComponent: null,
    };
  }

  componentDidMount() {
    this.onRefresh();
  }

  deleteSocietate(id) {
    // id = id.replace('"', '');
    // console.log(id);
    const response = axios
      .delete(`${server.address}/societate/${id}`, { headers: authHeader() })
      .then((response) => response.data)
      .then(() => {
        console.log(response);
        // alert(`Deleted ${id}`);
				this.onRefresh();
				setSocSel(null);
      })
      .catch(err => console.error(err));
  }

  // function to render in react
  async renderSocietati() {
    // console.log('render called');
    this.setState({
      societatiComponent: this.state.societati.map((soc, index) => {
        return (
          <tr key={soc.id}>
            <th>{soc.nume || '-'}</th>
            <th>{soc.email || '-'}</th>
						<th>{soc.telefon || '-'}</th>
            <th>{soc.idcaen || '-'}</th>
            <th>{soc.cif || '-'}</th>
            <th>{soc.regcom || '-'}</th>
            <th>{soc.nrangajati || 0}</th>
            <th>
              <PopupState variant="popover" popupId="demo-popup-popover">
                {(popupState) => (
                  <div>
                    <Button variant="contained" className="m-0 p-0" {...bindTrigger(popupState)}>
                      <DeleteIcon fontSize="small" />
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
                        <Typography>Sigur ștergeți societatea {soc.nume}?</Typography>
                        <Typography variant="caption">Datele nu mai pot fi recuperate</Typography>
                        <br />
                        <Button
                          variant="outline-danger"
                          onClick={() => {
                            popupState.close();
                            this.deleteSocietate(soc.id);
                          }}
                          className="mt-2"
                        >
                          Da
                        </Button>
                        <Button
                          variant="outline-secondary"
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
      }),
    });
  }
  async onRefresh() {
    // e.preventDefault();
		const user = JSON.parse(localStorage.getItem('user'));
		let uri = `${server.address}/societate/user/${user.id}`;
		if(user.roles.includes('ROLE_DIRECTOR'))
			uri = `${server.address}/societate/`;

    let societati = await axios
      .get(uri, {
        headers: authHeader(),
      })
      .then((societati) => societati.data);

    societati = await Promise.all(
      societati.map(async (societate) => {
        let nrAngajati = await axios
          .get(`${server.address}/angajat/ids=${societate.id}/count`, { headers: authHeader() })
          .then((res) => res.data);

        return { ...societate, nrangajati: nrAngajati };
      })
    );
    console.log(societati);

    this.state.societati = societati;

    console.log('onRefresh called');
    this.renderSocietati();
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Societăți</Card.Title>
                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  ↺
                </Button>
                <Button
                  onClick={() => (window.location.href = '/forms/add-societate')}
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                >
                  +
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Nume</th>
                      <th>email</th>
                      <th>telefon</th>
                      <th>CAEN</th>
                      <th>CIF</th>
                      <th>Reg. Com.</th>
                      <th>Nr. Angajați</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>{this.state.societatiComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default SocietatiTabel;
