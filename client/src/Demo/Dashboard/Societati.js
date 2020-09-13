import React from 'react';
import { Row, Col, Card, Button } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';
import SocietateContext from '../Context/SocietateContext';

class Societati extends React.Component {
  /*
    TODO\
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
    this.select(this.context.societate_selectata);
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
    if(nume_soc)
      this.setState({
        [nume_soc]: '1',
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
            this.context.changeSelected(nume_soc);
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
Societati.contextType = SocietateContext;

export default Societati;
