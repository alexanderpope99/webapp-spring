import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Button, Form, Toast, Modal } from 'react-bootstrap';
import { Typography } from '@material-ui/core';
import Aux from '../../../hoc/_Aux';

import authHeader from '../../../services/auth-header';

import { server } from '../../Resources/server-address';
import { getSocSel } from '../../Resources/socsel';

export default class EmitereFactura extends React.Component {
  constructor(props) {
    super(props);

    this.adaugaProdus = this.adaugaProdus.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      showToast: false,
      toastMessage: '',

      factura: props.factura, // factura care este editata

      clienti: [],
      activitati: [],

      today: new Date().toISOString().substring(0, 10),

      // datele facturii
      id: null,
      serie: 'BVFZ',
      numar: props.numarFactura,
      nrAvizInsotire: '-',
      client: { nume: '' },
      titlu: 'Cf. Contract vanzare-cumparare',
      produse: [],
      dataExpedierii: new Date().toISOString().substring(0, 10),
      oraExpedierii: '09:00',
      totalFaraTva: 0,
      totalTva: 0,
      totalCuTva: 0,
    };
  }

  async getClienti() {
    const clienti = await axios
      .get(`${server.address}/client/ids=${getSocSel().id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut prelua clienții: ' + err.response.data.message,
        })
      );
    this.setState({ clienti: clienti ? clienti : [] });
  }

  componentDidMount() {
    this.getClienti();
  }

  componentDidUpdate(prevProps) {
    if (this.props.factura !== prevProps.factura) {
      this.fillForm(this.props.factura);
    } else return;
  }

  fillForm(factura) {
    if (!factura) {
      this.setState({
        factura: null,

        id: null,
        serie: 'BVFZ',
        numar: this.props.numarFactura,
        today: new Date().toISOString().substring(0, 10),
        nrAvizInsotire: '-',
        client: { nume: '' },
        titlu: 'Cf. Contract vanzare-cumparare',
        produse: [],
        dataExpedierii: new Date().toISOString().substring(0, 10),
        oraExpedierii: '09:00',
        totalFaraTva: 0,
        totalTva: 0,
        totalCuTva: 0,
      });
    } else {
      this.setState({
        factura: factura,

        id: factura.id,
        serie: factura.serie,
        numar: factura.numar,
        nrAvizInsotire: factura.nravizinsotire,
        client: factura.client
          ? { id: factura.client.id, nume: factura.client.nume }
          : { id: 0, nume: '-' },
        titlu: factura.titlu,
        produse: factura.produse,
        dataExpedierii: factura.dataexpedierii,
        oraExpedierii: factura.oraexpedierii,
        totalFaraTva: factura.totalfaratva,
        totalTva: factura.tva,
        totalCuTva: factura.totalcutva,
      });
    }
  }

  onChangeClient(e) {
    if (e.target.value === '-') {
      this.setState({ client: { nume: '' } });
      return;
    }
    const selectedIndex = e.target.options.selectedIndex;
    const idClient = e.target.options[selectedIndex].getAttribute('data-key');
		// eslint-disable-next-line eqeqeq
		const client = this.state.clienti.find(c => c.id == idClient);
		console.log(client);
    this.setState({ client: client ? client : {nume: ''}});
  }

  adaugaProdus() {
    let produse = this.state.produse;
    produse.push({
      denumire: '',
      um: '',
      cantitatea: 0,
      pretUnitar: 0,
      valoareFaraTva: 0,
      tva: 0,
    });
    this.setState({ produse: produse });
  }
  stergeProdus(index) {
    let produse = this.state.produse;
    produse.splice(index, 1);
    this.setState({ produse: produse });
  }
  changeProdusAttribute(produs, attr, value, index) {
    let produse = this.state.produse;
    produs = { ...produs, [attr]: value };
    produse[index] = produs;
    this.setState({ produse: produse });
  }

  getTotal() {
    const produse = this.state.produse;
    var totalFaraTva = 0,
      totalTva = 0,
      totalCuTva = 0;
    if (produse && produse.length > 0) {
      totalFaraTva = produse.reduce(
        (acc, produs) => acc + produs.cantitatea * produs.pretUnitar,
        0
      );
      totalTva = totalFaraTva * 0.19;
      totalCuTva = totalFaraTva + totalTva;
    }
		totalFaraTva = totalFaraTva.toFixed(2);
		totalTva = totalTva.toFixed(2);
		totalCuTva = totalCuTva.toFixed(2);
    return { totalFaraTva, totalTva, totalCuTva };
  }

  async onSubmit() {
    const factura = this.state.factura;
    const nrAvizInsotire = this.state.nrAvizInsotire === '-' ? '' : this.state.nrAvizInsotire;
    const client = this.state.clienti.find((c) => c.id === this.state.client.id);

		if(!client) {
			this.setState({
				toastMessage: 'Factura are nevoie de un client',
				showToast: true,
			});
			return;
		}

    const newFactura = {
      serie: this.state.serie,
      numar: this.state.numar,
      nravizinsotire: nrAvizInsotire,
      client: client,
      titlu: this.state.titlu,
      produse: this.state.produse,
      dataexpedierii: this.state.dataExpedierii,
      oraexpedierii: this.state.oraExpedierii,
      totalfaratva: this.state.totalFaraTva,
      tva: this.state.totalTva,
      // totalcutva: this.state.totalCuTva,
    };

    var ok = false;
    // se adauga o factura noua
    if (!factura) {
      console.log('POST:', newFactura);
      ok = await axios
        .post(`${server.address}/factura`, newFactura, { headers: authHeader() })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut adăuga factura: ' + err.response.data.message,
          })
        );
    } else {
      newFactura['id'] = factura.id;
      console.log('PUT:', newFactura);
      ok = await axios
        .put(`${server.address}/factura/${factura.id}`, newFactura, { headers: authHeader() })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({
            showToast: true,
            toastMessage: 'Nu am putut modifica factura: ' + err.response.data.message,
          })
        );
    }

    if (ok) {
      this.setState({
        showToast: false,
        toastMessage: '',
        showModal: true,
        modalMessage: factura ? 'Factură modificată' : 'Factură adăugată',
      });
    }
  }

	handleClose() {
		this.setState({
			showModal: false,
			modalMessage: '',
		})
	}

  render() {
    const produseComponent = this.state.produse.map((produs, index) => (
      <Row className="border rounded p-0 pt-2 mt-2 mb-2" key={index}>
        <Col md={12}>
          <Typography variant="body1" className="border-bottom mb-3" gutterBottom>
            #{index + 1}
          </Typography>
        </Col>
        <Form.Group as={Col} sm="12">
          <Form.Label>Denumirea produselor sau serviciilor</Form.Label>
          <Form.Control
            as="textarea"
            value={produs.denumire}
            onChange={(e) => this.changeProdusAttribute(produs, 'denumire', e.target.value, index)}
          />
        </Form.Group>
        <Form.Group as={Col} lg="4">
          <Form.Label>UM</Form.Label>
          <Form.Control
            type="text"
            value={produs.um}
            onChange={(e) => this.changeProdusAttribute(produs, 'um', e.target.value, index)}
          />
        </Form.Group>
        <Form.Group as={Col} lg="4">
          <Form.Label>Cantitatea</Form.Label>
          <Form.Control
            type="number"
            value={produs.cantitatea}
            onChange={(e) =>
              this.changeProdusAttribute(produs, 'cantitatea', Number(e.target.value), index)
            }
          />
        </Form.Group>
        <Form.Group as={Col} lg="4">
          <Form.Label>Pret unitar</Form.Label>
          <Form.Control
            type="number"
            step="0.01"
            value={produs.pretUnitar}
            onChange={(e) =>
              this.changeProdusAttribute(produs, 'pretUnitar', Number(e.target.value), index)
            }
          />
        </Form.Group>
        <Form.Group as={Col} sm="6">
          <Form.Label>Valoare (fara TVA)</Form.Label>
          <Form.Control disabled type="number" value={produs.pretUnitar * produs.cantitatea} />
        </Form.Group>
        <Form.Group as={Col} sm="6">
          <Form.Label>Valoare TVA</Form.Label>
          <Form.Control
            disabled
            type="text"
            step="0.01"
            value={(produs.pretUnitar * produs.cantitatea * 0.19).toFixed(2)}
          />
        </Form.Group>
        <Col md={12}>
          <Button
            className="p-1 mb-3 float-right"
            variant="link"
            onClick={() => this.stergeProdus(index)}
          >
            Șterge produs
          </Button>
        </Col>
      </Row>
    ));

		const clientiComponent = this.state.clienti.map((client) => (
			<option key={client.id} data-key={client.id}>{client.nume}</option>
		))

    const { totalFaraTva, totalTva, totalCuTva } = this.getTotal();

    return (
      <Aux>
        {/* ERROR TOAST */}
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>

        {/* CONFIRM MODAL */}
        <Modal show={this.state.showModal} onHide={this.hanldeClose}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

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
                    <option>-</option>
										{clientiComponent}
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
                <Button onClick={this.adaugaProdus} variant="info" className="pt-1 pb-1">
                  Adauga produs/serviciu
                </Button>
                <Col md={12}>{produseComponent}</Col>
              </Col>
            </Row>

            {/* FOOTER */}
            <Row className="mt-2">
              <Col md={6} className="border rounded pt-3">
                <Form.Group>
                  <Form.Label>Data si ora expedierii</Form.Label>
                  <Form.Control
                    type="date"
                    value={this.state.dataExpedierii}
                    onChange={(e) => this.setState({ dataExpedierii: e.target.value })}
                  />
                  <Form.Control
                    className="mt-2"
                    type="time"
                    value={this.state.oraExpedierii}
                    onChange={(e) => this.setState({ oraExpedierii: e.target.value })}
                  />
                </Form.Group>
              </Col>
              <Col md={6} className="border rounded pt-3">
                <Form.Group>
                  <Form.Label>Total (fara TVA)</Form.Label>
                  <Form.Control type="number" value={totalFaraTva} disabled />
                </Form.Group>
                <Form.Group>
                  <Form.Label>TVA</Form.Label>
                  <Form.Control type="number" value={totalTva} disabled />
                </Form.Group>
                <Form.Group>
                  <Form.Label>Total (cu TVA)</Form.Label>
                  <Form.Control type="number" value={totalCuTva} disabled />
                </Form.Group>
              </Col>
              <Button onClick={this.onSubmit} className="mt-3">
                {this.state.factura ? 'Modifică' : 'Emite factura'}
              </Button>
            </Row>
          </Card.Body>
        </Card>
      </Aux>
    );
  }
}
