import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

import Persoana from '../UIElements/Forms/Persoana';

class AddPersoanaTest extends React.Component {
  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Adaugă persoană</Card.Title>
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

export default AddPersoanaTest;
