import React from 'react';
import { Row, Col, Card, Table, Button, Modal, Form, Breadcrumb, Toast } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';

export default class EmitereFactura extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      showToast: false,
      toastMessage: '',

      factura: props.factura, // datele din factura care este editata

      clienti: [],

      // datele din formular
      serie: 'BVFZ',
      numar: props.numarFactura,
      today: new Date().toISOString().substring(0, 10),
      nrAvizInsotire: '-',
      client: { id: 0, nume: '-' },
      titlu: 'Cf. Contract vanzare-cumparare',
    };
  }

  componentDidUpdate(prevProps) {
    if (this.props.factura !== prevProps.factura) {
      // this.setState({ factura: this.props.factura });
      this.fillForm(this.props.factura);
    } else return;
  }

  fillForm(factura) {
    // const factura = this.props.factura;
    if (!factura) {
      this.setState({
        factura: null,

        serie: 'BVFZ',
        numar: this.state.props ? this.state.props.numarFactura : 0,
        today: new Date().toISOString().substring(0, 10),
        nrAvizInsotire: '-',
        client: { id: 0, nume: '-' },
        titlu: 'Cf. Contract vanzare-cumparare',
      });
    } else {
      this.setState({
        factura: factura,

        serie: factura.serie,
        numar: factura.numar,
        nrAvizInsotire: factura.nravizinsotire,
        client: factura.client
          ? { id: factura.client.id, nume: factura.client.nume }
          : { id: 0, nume: '-' },
        titlu: factura.titlu,
      });
    }
  }

  onChangeClient(e) {
    if (e.target.value === '-') {
      this.setState({ client: { id: 0, nume: '-' } });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const idClient = e.target.options[selectedIndex].getAttribute('data-key');

    this.setState({ client: { id: Number(idClient), nume: e.target.value } });
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

        <Card>
          <Card.Header className="border-0">
            <Card.Title as="h5">Emitere factură fiscală</Card.Title>
          </Card.Header>
          <Card.Body>
            {/* TOP ROW */}
            <Row>
              <Col md={3} className="border rounded pt-2">
                <Form.Group>
                  <Form.Label>Centru de Profit</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.serie}
                    onChange={(e) => this.setState({ serie: e.target.value })}
                  >
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
                <Form.Group as={Row}>
                  <Form.Label column sm="4">
                    Numărul facturii:
                  </Form.Label>
                  <Col md={8}>
                    <Form.Control type="text" disabled value={this.state.numar} />
                  </Col>
                </Form.Group>
                <Form.Group as={Row}>
                  <Form.Label column sm="4">
                    Data:
                  </Form.Label>
                  <Col md={8}>
                    <Form.Control type="date" disabled value={this.state.today} />
                  </Col>
                </Form.Group>
                <Form.Group as={Row}>
                  <Form.Label column sm="4">
                    Nr. aviz insotire
                  </Form.Label>
                  <Col md={8}>
                    <Form.Control
                      as="select"
                      value={this.state.nrAvizInsotire}
                      onChange={(e) => this.setState({ nrAvizInsotire: e.target.value })}
                    >
                      <option>-</option>
                      <option>AUCS / 0097</option>
                      <option>AUCS / 0098</option>
                    </Form.Control>
                  </Col>
                </Form.Group>
              </Col>
              <Col md={3} className="border rounded">
                <Form.Group>
                  <Form.Label>Client</Form.Label>
                  <Form.Control
                    as="select"
                    value={this.state.client.nume}
                    onChange={(e) => this.onChangeClient(e)}
                  >
                    <option data-key="2">-</option>
                  </Form.Control>
                </Form.Group>
              </Col>
            </Row>

            {/* TITLUL */}
            <Row className="border rounded mt-2 pt-3">
              <Col sm={12}>
                <Form.Group as={Col} md="12" controlId="titlu-factura">
                  <Form.Label>Titlul facturii (actul în baza căruia se emite factura)</Form.Label>
                  <Form.Control
                    type="text"
                    value={this.state.titlu}
                    onChange={(e) => this.setState({ titlu: e.target.value })}
                  />
                </Form.Group>
              </Col>
            </Row>

						{/* PRODUSE / SERVICII */}
						<Row className="border rounded mt-2 pt-3 pb-2">
							<Col md={12}>
								<Button>Adauga produs/serviciu</Button>
							</Col>
						</Row>

						{/* FOOTER */}
						<Row className="border rounded mt-2 pt-3">
							<Col md={8}>
								<Button>Adauga produs/serviciu</Button>
							</Col>
						</Row>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}
