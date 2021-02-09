import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Breadcrumb, Toast } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

export default class EmitereFactura extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      showToast: false,
      toastMessage: '',

      factura: props.factura, // pentru a lua datele fin factura care este editata

			serie: 'BVFZ',
    };
  }

  componentDidUpdate(prevProps) {
    if (this.props.factura !== prevProps.factura) {
      this.setState({ factura: this.props.factura });
    } else return;
  }

  render() {
    return (
      <Aux>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        {this.state.factura ? this.state.factura.client : 'factura noua'}

        <Row>
          <Col md={3} className="border rounded pt-2">
            <Form.Group>
              <Form.Label>Centru de Profit</Form.Label>
              <Form.Control as="select" value={this.state.serie} onChange={(e) => this.setState({serie: e.target.value})}>
                <option>BVFZ</option>
                <option>NRM</option>
                <option>SMT</option>
                <option>ING</option>
                <option>UCS</option>
                <option>FFBN</option>
              </Form.Control>
            </Form.Group>
          </Col>
          <Col md={6} className="border rounded pt-3">
            <Form.Group as={Row}>
              <Form.Label column sm="4">
                Seria facturii:
              </Form.Label>
              <Col md={8}>
                <Form.Control type="text" disabled value={this.state.serie} />
              </Col>
            </Form.Group>
          </Col>
          <Col md={3} className="border rounded"></Col>
        </Row>
      </Aux>
    );
  }
}
