import React from 'react';
import {
  Row,
  Col,
  Card,
} from 'react-bootstrap';

import Contract from '../UIElements/Forms/Contract';
import Aux from '../../hoc/_Aux';
//import { isNumeric } from 'jquery';

class AddContract extends React.Component {

  componentDidMount() {
    window.scrollTo(0, 0);
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Date contract</Card.Title>
              </Card.Header>
              <Card.Body>
               <Contract/>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AddContract;
