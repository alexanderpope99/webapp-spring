import React from 'react';
import { Row, Col, Card, Button } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

class Societati extends React.Component {
  /*
    TODO
    * selecteaza societate la fiecare adaugare/citire care are legatura cu societeatea
    * : adauga persoana | lista angajati | editeaza angajat | reailzari retineri
    * on click "Editeaza" -> redirect to Edit societate page (same as persoane)
    * on click "Sterge" -> prompt confirm, delete only on user confirmation
  */
  constructor() {
    super();
    this.unselectAll = this.unselectAll.bind(this);

    this.state = {};
  }

  async componentDidMount() {
    await this.getNumeSocietati();
  }

  async getNumeSocietati() {
    let societati = await fetch('http://localhost:5000/societate', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    }).then((response) => response.json());
    if (Array.isArray(societati)) {
      societati.forEach((societate) =>
        this.setState({
          [societate.nume]: { opacity: '.3', id: societate.id },
        })
      );
    }
    let selected = this.getSel();
    if (selected) this.select(selected.nume);
  }

  unselectAll() {
    for (let nume_soc in this.state) {
      this.setState({
        [nume_soc]: { opacity: '.3', id: this.state[nume_soc].id },
      });
    }
  }

  select(nume_soc) {
    this.unselectAll();
    if (nume_soc) {
      this.setState({
        [nume_soc]: { opacity: '1', id: this.state[nume_soc].id },
      });

      this.setSel(nume_soc);
      console.log(this.getSel());
    }
  }

  getSel() {
    let soc_sel = sessionStorage.getItem('socsel');
    // console.log(JSON.parse(soc_sel));
    return JSON.parse(soc_sel);
  }

  setSel(nume_soc) {
    let socsel = { id: this.state[nume_soc].id, nume: nume_soc };
    sessionStorage.setItem('socsel', JSON.stringify(socsel));
  }

  render() {
    const societatiComponent = Object.keys(this.state).map((nume_soc) => (
      <Col md={6} xl={4} key={nume_soc}>
        <Card
          style={{
            opacity: this.state[nume_soc].opacity,
            cursor: this.state[nume_soc].opacity === '1' ? '' : 'pointer',
          }}
          onClick={
            this.state[nume_soc].opacity === '.3'
              ? () => {
                  this.select(nume_soc);
                }
              : null
          }
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
