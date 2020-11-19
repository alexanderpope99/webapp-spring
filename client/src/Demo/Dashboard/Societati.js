import React from 'react';
import { Row, Col, Card, Button, Modal, Form } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

import { getSocSel, setSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';
import { setAngajatSel } from '../Resources/angajatsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import { Edit } from 'react-feather';

class Societati extends React.Component {
  constructor() {
    super();
    this.unselectAll = this.unselectAll.bind(this);

    this.state = {
		societati: {},
		nume: '',
	};
  }

  async componentDidMount() {
    await this.getNumeSocietati();
  }

  async getNumeSocietati() {
    const user = JSON.parse(localStorage.getItem('user'));
    let uri = `${server.address}/societate/user/${user.id}`;
    if (user.roles.includes('ROLE_DIRECTOR')) {
      uri = `${server.address}/societate/`;
    }
    const societati_res = await axios
      .get(uri, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.log(err));

    if (Array.isArray(societati_res)) {
      societati_res.forEach((societate) => {
		console.log(societate);
		this.setState({
			societati: {...this.state.societati, [societate.nume]: { opacity: '.3', id: societate.id }},
		  });
	  });
    }
	console.log(this.state.societati);
    let selected = getSocSel();
    // console.log(selected);
	if (selected) this.select(selected.nume);
	// for(let key in this.state.societati) {
	// 	console.log(key);
	// }
  }

  unselectAll() {
	var societati = this.state.societati;
    for (let nume_soc in societati) {
      societati[nume_soc] = {opacity: '.3', id: societati[nume_soc].id}
    }
  }

  select(nume_soc) {
	this.unselectAll();
	setAngajatSel(null);
	console.log(nume_soc);
    if (nume_soc) {
      let id = this.state.societati[nume_soc].id;
      this.setState({
        societati: {...this.state.societati, [nume_soc]: { opacity: '1', id: id }},
      });

      setSocSel({ id: id, nume: nume_soc });
      console.log(getSocSel());
    }
  }

  render() {
    const societatiComponent = Object.keys(this.state.societati).map((key) => (
      <Col md={6} xl={4} key={key}>
        <Card
          style={{
            opacity: this.state.societati[key].opacity,
            cursor: this.state.societati[key].opacity === '1' ? '' : 'pointer',
          }}
          onClick={
            this.state.societati[key].opacity === '.3'
              ? () => {
                  this.select(key);
                }
              : null
          }
        >
          <Card.Body>
            <h3 className="d-flex justify-content-around">{key}</h3>
            <Edit
              className="d-flex justify-content-around float float-right"
              visibility={this.state.societati[key].opacity === '.3' ? 'hidden' : 'visible'}
            />
          </Card.Body>
        </Card>
      </Col>
    ));

    return (
      <Aux>
        {/* EDIT SOCIETATE MODAL HERE */}
        <Modal show={this.state.show} onHide={() => this.handleClose(false)} size="lg">
          <Modal.Header closeButton>
            <Modal.Title>Date societate</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Row>
                <Form.Group id="nume">
                  <Form.Control
                    required
                    type="text"
                    placeholder="Nume"
                    value={this.state.nume}
                    onChange={(e) =>
                      this.setState({
                        nume: e.target.value,
                      })
                    }
                  />
                </Form.Group>
              </Row>
            </Form>
          </Modal.Body>
        </Modal>
        <Button href="/forms/add-societate">Adaugă societate</Button>
        <Row>{societatiComponent}</Row>
      </Aux>
    );
  }
}

export default Societati;
