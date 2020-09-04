import React from 'react';
import { Row, Col, Card, Button } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

class Societati extends React.Component {
  /*
    TODO
    * on click "Editeaza" -> redirect to Edit societate page (same as persoane)
    * on click "Sterge" -> prompt confirm popover, delete only on user confirm
  */
  constructor(props) {
    super(props);
    this.unselectAll = this.unselectAll.bind(this);

    this.state = {};
  }

  async componentDidMount() {
    await this.getNumeSocietati();
    let selected = await fetch('http://localhost:5000/selected', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    }).then((response) => response.json());
    this.select(selected);
  }

  async getNumeSocietati() {
    let societati = await fetch('http://localhost:5000/societate', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    }).then((response) => response.json());
    if (Array.isArray(societati)) {
      societati.forEach((item) =>
        this.setState({
          [item.nume]: '.3',
        })
      );
    }
  }

  unselectAll() {
    for (let key in this.state) {
      this.setState({
        [key]: '.3',
      })
    }
      
  }

  select(nume_soc) {
    this.unselectAll();
    this.setState({
      [nume_soc]: '1',
    });
  }

  async getNumeSocietate() {
    await fetch('http://localhost:5000/selected', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      // body: JSON.stringify(persoane),
    }).then((response) => response.json());
  }

  async saveNumeSocietate(nume_soc) {
    await fetch('http://localhost:5000/selected', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        nume: nume_soc,
      }),
    });
  }

  render() {
    const societatiComponent = Object.keys(this.state).map((nume_soc) => (
      <Col md={6} xl={4} key={nume_soc}>
        <Card
          style={{
            opacity: this.state[nume_soc],
            cursor: this.state[nume_soc] === '1' ? '' : 'pointer',
          }}
          onClick={() => {
            this.select(nume_soc);
            this.saveNumeSocietate(nume_soc);
          }}
        >
          <Card.Body>
            <h3 className="d-flex justify-content-around pb-5">{nume_soc}</h3>
            <div className="d-flex flex-inline justify-content-end">
              <Button size="sm" className="m-1 p-1">
                Editează
              </Button>
              <Button size="sm" className="m-1 p-1">
                Șterge
              </Button>
            </div>
          </Card.Body>
        </Card>
      </Col>
    ));

    return (
      <Aux>
        <Row>{societatiComponent}</Row>
      </Aux>
    );
  }
}

export default Societati;
