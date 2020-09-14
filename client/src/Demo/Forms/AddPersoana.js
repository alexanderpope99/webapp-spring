import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

import Persoana from '../UIElements/Forms/Persoana';
import SocietateContext from '../Context/SocietateContext';

class AddPersoanaTest extends React.Component {

  componentDidMount() {
    console.log(this.context.societate_selectata);
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Adaugă persoană - {this.context.selected_societate}</Card.Title>
              </Card.Header>
              <Card.Body>
                <Persoana />
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}
AddPersoanaTest.contextType = SocietateContext;

export default AddPersoanaTest;
