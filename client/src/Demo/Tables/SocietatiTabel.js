import React from 'react';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import DeleteIcon from '@material-ui/icons/Delete';
import Popover from '@material-ui/core/Popover';
import PopupState, { bindTrigger, bindPopover } from 'material-ui-popup-state';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography/Typography';

import Aux from '../../hoc/_Aux';

class SocietatiTabel extends React.Component {
  constructor(props) {
    super(props);

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
    const response = fetch(`http://localhost:5000/societate/${id}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
    })
      .then((response) => response.json())
      .then(() => {
        console.log(response);
        // alert(`Deleted ${id}`);
        this.onRefresh();
      })
      .catch(console.log('could not connect to db'));
  }

  // function to render in react
  renderSocietati() {
    // console.log('render called');
    this.setState({
      societatiComponent: this.state.societati.map((soc, index) => {
        for (let key in soc) {
          if (soc[key] === 'null' || soc[key] === null) soc[key] = '-';
        }
        // console.log(soc);
        return (
          <tr key={soc.id}>
            <th>{soc.id}</th>
            <th>{soc.nume}</th>
            <th>{soc.email}</th>
            <th>{soc.idcaen}</th>
            <th>{soc.cif}</th>
            <th>{soc.regcom}</th>
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

    const societati = await fetch('http://localhost:5000/societate', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(societati),
    }).then((societati) => societati.json());

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
              <Card.Header>
                <Card.Title as="h5">Listă Societăți</Card.Title>
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
                      <th>#id</th>
                      <th>Nume</th>
                      <th>email</th>
                      <th>CAEN</th>
                      <th>CIF</th>
                      <th>Registrul Comertului</th>
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
