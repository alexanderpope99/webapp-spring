import React from "react";
import {
  Row,
  Col,
  Card,
  Form,
  Button,
} from "react-bootstrap";

import Aux from "../../hoc/_Aux";

class AddCompany extends React.Component {
  constructor(props) {
    super(props);
    this.onChangeNume = this.onChangeNume.bind(this);
    this.onSubmit = this.onSubmit.bind(this);

    this.state = {
      nume: "",
    };
  }

  onChangeNume(e) {
    this.setState({
      nume: e.target.value,
    });
    console.log(this.state.nume);
  }

  onSubmit(e) {
    e.preventDefault();
    const da = {
      da: this.state.nume,
    };
    console.log(da);
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            <Card>
              <Card.Header>
                <Card.Title as="h5">Adaugă societate</Card.Title>
              </Card.Header>
              <Card.Body>
                <h5>Datele societății</h5>
                <hr />
                <Form onSubmit={this.onSubmit}>
                  <Row>
                    {/* <Form> */}
                    <Col md={6}>
                      <Form.Group controlId="nume">
                        <Form.Label>Denumire societate</Form.Label>
                        <Form.Control
                          type="text"
                          value={this.state.nume}
                          onChange={this.onChangeNume}
                          placeholder="Nume"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="punctDeLucru">
                        <Form.Label>Punct de lucru</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="eg. Strada nr. 1"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="codCaen">
                        <Form.Label>Cod CAEN</Form.Label>
                        <Form.Control type="number" placeholder="CAEN" />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="cif">
                        <Form.Label>CIF</Form.Label>
                        <Form.Control type="number" placeholder="CIF" />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="capSoc">
                        <Form.Label>Capital social</Form.Label>
                        <Form.Control
                          type="text"
                          placeholder="Capital social"
                        />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="an">
                        <Form.Label>An</Form.Label>
                        <Form.Control type="number" placeholder="an" />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="telefon">
                        <Form.Label>Telefon</Form.Label>
                        <Form.Control type="text" placeholder="Telefon" />
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group controlId="email">
                        <Form.Label>E-mail</Form.Label>
                        <Form.Control
                          type="email"
                          placeholder="email@email.dom"
                        />
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Button variant="outline-primary" type="submit">
                        Adaugă
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Card.Body>
            </Card>

            {/* DEMO PAGE
            <Card>
              <Card.Header>
                <Card.Title as="h5">Basic Component</Card.Title>
              </Card.Header>
              <Card.Body>
                <h5>Form controls</h5>
                <hr />
                <Row>
                  <Col md={6}>
                    <Form>
                      <Form.Group controlId="formBasicEmail">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control type="email" placeholder="Enter email" />
                      </Form.Group>

                      <Form.Group controlId="formBasicPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type="password" placeholder="Password" />
                      </Form.Group>
                      <Form.Group controlId="formBasicChecbox">
                        <Form.Check type="checkbox" label="Check me out" />
                      </Form.Group>
                      <Button variant="primary">Submit</Button>
                    </Form>
                  </Col>
                  <Col md={6}>
                    <Form.Group controlId="exampleForm.ControlInput1">
                      <Form.Label>Text</Form.Label>
                      <Form.Control type="email" placeholder="Text" />
                    </Form.Group>
                    <Form.Group controlId="exampleForm.ControlSelect1">
                      <Form.Label>Example select</Form.Label>
                      <Form.Control as="select">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                      </Form.Control>
                    </Form.Group>
                    <Form.Group controlId="exampleForm.ControlTextarea1">
                      <Form.Label>Example textarea</Form.Label>
                      <Form.Control as="textarea" rows="3" />
                    </Form.Group>
                  </Col>
                </Row>
                <h5 className="mt-5">Sizing</h5>
                <hr />
                <Row>
                  <Col md={6}>
                    <Form.Control
                      size="lg"
                      type="text"
                      placeholder="Large text"
                      className="mb-3"
                    />
                    <Form.Control
                      type="text"
                      placeholder="Normal text"
                      className="mb-3"
                    />
                    <Form.Control
                      size="sm"
                      type="text"
                      placeholder="Small text"
                      className="mb-3"
                    />
                  </Col>
                  <Col md={6}>
                    <Form.Control size="lg" as="select" className="mb-3">
                      <option>Large select</option>
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </Form.Control>
                    <Form.Control as="select" className="mb-3">
                      <option>Default select</option>
                      <option>1</option>
                      <option>2</option>
                      <option>3</option>
                      <option>4</option>
                      <option>5</option>
                    </Form.Control>
                  </Col>
                </Row>
                <h5 className="mt-5">Inline</h5>
                <hr />
                <Row>
                  <Col>
                    <Form inline>
                      <Form.Group className="mb-2">
                        <Form.Label srOnly>Email</Form.Label>
                        <Form.Control
                          plaintext
                          readOnly
                          defaultValue="email@example.com"
                        />
                      </Form.Group>
                      <Form.Group className="mb-2 mr-5">
                        <Form.Label srOnly>Password</Form.Label>
                        <Form.Control type="password" placeholder="Password" />
                      </Form.Group>
                      <Form.Group>
                        <Button className="mb-0">Confirm Identity</Button>
                      </Form.Group>
                    </Form>
                  </Col>
                </Row>
                <h3 className="mt-5">Checkboxes and Radios</h3>
                <Row>
                  <Col md={12}>
                    <h5 className="mt-5">Checkboxes</h5>
                    <hr />
                    <Form.Group>
                      <Form.Check
                        custom
                        type="checkbox"
                        id="checkbox1"
                        label="Check this custom checkbox"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <h5 className="mt-5">Radios</h5>
                    <hr />
                    <Form.Group>
                      <Form.Check
                        custom
                        type="radio"
                        label="Toggle this custom radio"
                        name="supportedRadios"
                        id="supportedRadio3"
                      />
                      <Form.Check
                        custom
                        type="radio"
                        label="Or toggle this other custom radio"
                        name="supportedRadios"
                        id="supportedRadio4"
                      />
                    </Form.Group>
                    <h5 className="mt-3">Inline</h5>
                    <hr />
                    <Form.Group>
                      <Form.Check
                        inline
                        custom
                        type="radio"
                        label="Toggle this custom radio"
                        name="supportedRadio"
                        id="supportedRadio21"
                      />
                      <Form.Check
                        inline
                        custom
                        type="radio"
                        label="Or toggle this other custom radio"
                        name="supportedRadio"
                        id="supportedRadio22"
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <h5 className="mt-5">Range</h5>
                    <hr />
                    <Form.Label htmlFor="customRange1">
                      Example range
                    </Form.Label>
                    <input
                      type="range"
                      className="custom-range"
                      defaultValue="22"
                      id="customRange1"
                    />
                    <Form.Label htmlFor="customRange2">
                      Example range
                    </Form.Label>
                    <input
                      type="range"
                      className="custom-range"
                      min="0"
                      defaultValue="3"
                      max="5"
                      id="customRange2"
                    />
                    <Form.Label htmlFor="customRange3">
                      Example range
                    </Form.Label>
                    <input
                      type="range"
                      className="custom-range"
                      min="0"
                      defaultValue="1.5"
                      max="5"
                      step="0.5"
                      id="customRange3"
                    />
                  </Col>
                </Row>
              </Card.Body>
            </Card> */}
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AddCompany;
