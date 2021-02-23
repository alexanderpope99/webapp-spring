import React from 'react';
import axios from 'axios';
import { Row, Col, Card, Form, Button, Modal, Collapse, Toast,Image } from 'react-bootstrap';
import Aux from '../../../hoc/_Aux';
import CentruCostTabel from './CentruCostTabel';
import ContBancarTabel from './ContBancarTabel';

import { judete, sectoare } from '../../Resources/judete';
import { server } from '../../Resources/server-address';
import { getSocSel, setSocSel } from '../../Resources/socsel';

import { downloadImagineSocietate } from '../../Resources/download';
import authHeader from '../../../services/auth-header';
import authService from '../../../services/auth.service';
import 'react-dropzone-uploader/dist/styles.css';
import Dropzone from 'react-dropzone-uploader';

const judeteOptions = judete.map((judet, index) => {
  return <option key={index}>{judet}</option>;
});

const sectoareOptions = sectoare.map((sector, index) => {
  return <option key={index}>{sector}</option>;
});

class Societate extends React.Component {
  constructor() {
    super();
    this.onSubmit = this.onSubmit.bind(this);
    this.onChangeLocalitate = this.onChangeLocalitate.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.clearFields = this.clearFields.bind(this);

    const urlParams = new URLSearchParams(window.location.search);

    this.state = {
      // confirmation modal
      show: false,
      modalMessage: '',
      isEdit: urlParams.has('isEdit'),
      socsel: getSocSel(),

      // accordion
      showCentreCost: false,
      showContBancar: false,

      // form data
      id: 0,
      nume: '',
      idcaen: '',
      cif: '',
      capsoc: '',
      regcom: '',
      idadresa: null,
      adresa: '',
      localitate: '',
      judet: '',
      tipJudet: 'Județ',
      email: '',
      telefon: '',
      fax: '',

      centreCost: [], // array containing CentruCost objects
      centruCost: null, // details for add/edit modal

      showToast: false,
      toastMessage: '',

      fisier: null,
      numefisier: '',
      idfisier: '',
	  existaImagine:false,
    };
  }

  clearFields() {
    this.setState({
      id: 0,
      nume: '',
      idcaen: '',
      cif: '',
      capsoc: '',
      regcom: '',
      idadresa: null,
      adresa: '',
      localitate: '',
      judet: '',
      tipJudet: 'Județ',
      email: '',
      telefon: '',
      fax: '',

      centreCost: [], // array containing CentruCost objects
      centruCost: null, // details for add/edit modal

      fisier: null,
      numefisier: null,
      idfisier: null,
    });
  }

  handleClose() {
    this.setState({ show: false }, () => {
      if (!this.state.isEdit) this.clearFields();
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    });
  }

  onChangeLocalitate(localitate) {
    if (!localitate) return;
    if (
      localitate.toLowerCase() === 'bucuresti' ||
      localitate.toLowerCase() === 'bucurești' ||
      localitate.toLowerCase() === 'bucharest'
    )
      this.setState({
        tipJudet: 'Sector',
        localitate: localitate,
      });
    else
      this.setState({
        tipJudet: 'Județ',
        localitate: localitate,
      });
  }

  async getSocietateDetails() {
    const societate = await axios
      .get(`${server.address}/societate/${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));

	if(societate.idimagine)
	{
		const imagine = await axios
		.get(`${server.address}/fisier/${societate.idimagine}`, { headers: authHeader() })
		.then((res) => res.data)
		.catch((err) => console.error(err));

		const download=await fetch(`${server.address}/fisier/download/${societate.idimagine}`, {
			method: 'GET',
			headers: {
			  'Content-Type': 'application/octet-stream',
			  ...authHeader(),
			},
		  })
			.then((res) => (res.ok ? res.blob() : null))
				.catch((err) => console.error(err));
		this.setState(
			{
				fisier: download,
				numefisier:imagine.name,
				idfisier:societate.idimagine,
				existaImagine:true
			});
	}

    // console.log(societate.adresa.id);
    if (societate.adresa)
      this.setState(
        {
          id: societate.id,
          nume: societate.nume || '',
          idcaen: societate.idcaen || '',
          cif: societate.cif || '',
          capsoc: societate.capsoc || '',
          regcom: societate.regcom || '',
          // adresa
          idadresa: societate.adresa.id,
          adresa: societate.adresa.adresa || '',
          judet: societate.adresa.judet || '',
          //
          email: societate.email || '',
          telefon: societate.telefon || '',
          fax: societate.fax || '',
        },
        () => this.onChangeLocalitate(societate.adresa.localitate)
      );
    else {
      this.setState({
        id: societate.id,
        nume: societate.nume || '',
        idcaen: societate.idcaen || '',
        cif: societate.cif || '',
        capsoc: societate.capsoc || '',
        regcom: societate.regcom || '',
        email: societate.email || '',
        telefon: societate.telefon || '',
        fax: societate.fax || '',
      });
    }
  }

  componentDidMount() {
    if (this.state.isEdit) this.getSocietateDetails();
  }

  async onSubmit(e) {
    try {
      e.preventDefault();
    } catch (err) {
      console.log(err);
    }

    const formData = new FormData();
    if (this.state.numefisier) formData.append('file', this.state.fisier);

    if (this.state.fisier) {
		if(this.state.existaImagine){
		// put
		await axios
        .put(`${server.address}/fisier/${this.state.idfisier}`, formData, {
			headers: authHeader(),
        })
        .then((res) => res.status === 200)
		.catch((err) =>
		this.setState({ showToast: true, toastMessage: err.response.data.message })
		);
	}
	else{
		//post
		const file=await axios
        .post(`${server.address}/fisier/upload`, formData, {
          headers: authHeader(),
        })
        .then((res) => res.data)
        .catch((err) =>
		this.setState({ showToast: true, toastMessage: err.response.data.message })
        );
		this.setState({idfisier:file.fileId});
	}
    }

    let adresa_body = {
      id: this.state.idadresa,
      adresa: this.state.adresa || null,
      localitate: this.state.localitate || null,
      judet: this.state.judet || null,
      tara: null,
    };

    // build societate for POST
    const societate_body = {
      nume: this.state.nume || null,
      idcaen: Number(this.state.idcaen) || null,
      cif: this.state.cif || null,
      capsoc: this.state.capsoc || null,
      regcom: this.state.regcom || null,
      adresa: adresa_body,
      email: this.state.email || null,
      telefon: this.state.telefon || null,
      fax: this.state.fax || null,
      idimagine: this.state.idfisier || 0,

      centreCost: null,
    };

    const user = authService.getCurrentUser();
	var uri=this.state.idfisier ? `/imageid=${this.state.idfisier}` : '';
    var ok;
    if (this.state.isEdit) {
      // put
      ok = await axios
        .put(`${server.address}/societate/${this.state.id}${uri}`, societate_body, {
          headers: authHeader(),
        })
        .then((res) => res.status === 200)
        .catch((err) => console.error(err));
    } else {
      //post
      ok = await axios
        .post(`${server.address}/societate/${user.id}${uri}`, societate_body, {
          headers: authHeader(),
        })
        .then((res) => res.status === 200)
        .catch((err) =>
          this.setState({ showToast: true, toastMessage: err.response.data.message })
        );
    }
    if (ok) {
      this.setState({
        show: true,
        modalMessage: this.state.isEdit ? 'Societate actualizată' : 'Societate adăugată !',
      });

      if (!this.state.isEdit) this.clearFields();
    }
  }

  render() {
    const judeteComponent = () => {
      if (this.state.tipJudet === 'Județ') return judeteOptions;
      return sectoareOptions;
    };

    const handleChangeStatus = ({ file }, status) => {
      if (status === 'done') {
        console.log(status, file);


        this.setState({ fisier: file, numefisier: file.name });
      }
    };

    return (
      <Aux>
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
        <Modal show={this.state.show} onHide={this.handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.modalMessage}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" href="/dashboard/societati" onClick={() => setSocSel(null)}>
              Către societați
            </Button>
            <Button variant="primary" onClick={this.handleClose}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>

        {/* PAGE CONTENT */}
        <Row>
          <Col>
            <Card>
              <Card.Body>
                <h5>Datele societății</h5>
                <hr />
                <Form onSubmit={this.onSubmit}>
                  <Row>
                    {/* <Form> */}
                    <Form.Group controlId="nume" as={Col} md="12">
                      <Form.Label>Denumire societate</Form.Label>
                      <Form.Control
                        required
                        type="text"
                        placeholder="Nume"
                        value={this.state.nume}
                        onChange={(e) => this.setState({ nume: e.target.value })}
                      />
                    </Form.Group>

                    <Form.Group controlId="localitate" as={Col} md="6">
                      <Form.Label>Localitate</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Localitate"
                        value={this.state.localitate}
                        onChange={(e) => this.onChangeLocalitate(e.target.value)}
                      />
                    </Form.Group>
                    <Form.Group controlId="judet" as={Col} md="6">
                      <Form.Label>{this.state.tipJudet}</Form.Label>
                      <Form.Control
                        as="select"
                        value={this.state.judet}
                        onChange={(e) =>
                          this.setState({
                            judet: e.target.value,
                          })
                        }
                      >
                        <option>-</option>
                        {judeteComponent()}
                      </Form.Control>
                    </Form.Group>
                    <Form.Group controlId="adresa" as={Col} md="6">
                      <Form.Label>Adresă</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="eg. Strada nr. 1"
                        value={this.state.adresa}
                        onChange={(e) =>
                          this.setState({
                            adresa: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="codCaen" as={Col} md="6">
                      <Form.Label>Cod CAEN</Form.Label>
                      <Form.Control
                        type="number"
                        placeholder="CAEN"
                        value={this.state.idcaen}
                        onChange={(e) =>
                          this.setState({
                            idcaen: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="cif" as={Col} md="6">
                      <Form.Label>CIF</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="CIF"
                        value={this.state.cif}
                        style={{ fontFamily: 'Consolas, Courier New' }}
                        onChange={(e) =>
                          this.setState({
                            cif: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="capSoc" as={Col} md="6">
                      <Form.Label>Capital social</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Capital social"
                        value={this.state.capsoc}
                        onChange={(e) =>
                          this.setState({
                            capsoc: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="regcom" as={Col} md="6">
                      <Form.Label>Registrul comerțului</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="regcom"
                        value={this.state.regcom}
                        style={{ fontFamily: 'Consolas, Courier New' }}
                        onChange={(e) =>
                          this.setState({
                            regcom: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="telefon" as={Col} md="6">
                      <Form.Label>Telefon</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="Telefon"
                        value={this.state.telefon}
                        onChange={(e) =>
                          this.setState({
                            telefon: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="email" as={Col} md="6">
                      <Form.Label>E-mail</Form.Label>
                      <Form.Control
                        type="email"
                        placeholder="email@email.dom"
                        value={this.state.email}
                        onChange={(e) =>
                          this.setState({
                            email: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group controlId="fax" as={Col} md="6">
                      <Form.Label>Fax</Form.Label>
                      <Form.Control
                        type="text"
                        placeholder="fax"
                        value={this.state.fax}
                        onChange={(e) =>
                          this.setState({
                            fax: e.target.value,
                          })
                        }
                      />
                    </Form.Group>
                    <Form.Group as={Col} md="12">
                      {this.state.numefisier ? (
                        <div>
							<Image className="border p-3" fluid style={{height:200,weight:200}} src={window.URL.createObjectURL(this.state.fisier)}/>
                          <Button
                            variant="dark"
                            onClick={() => downloadImagineSocietate(this.state.numefisier, this.state.idfisier)}
                          >
                            {this.state.numefisier}
                          </Button>
                          <Button
                            variant="link"
                            onClick={() =>
                              this.setState({ idfisier:null,fisier: null, numefisier: null,existaImagine:false })
                            }
                          >
                            Șterge
                      	</Button>
                        </div>
                      ) : (
                          <Dropzone
                            inputContent="Imagine / Logo"
                            onChangeStatus={handleChangeStatus}
                            maxFiles={1}
                          />
                        )}
                    </Form.Group>
                  </Row>

                  {this.state.isEdit ? (
                    <React.Fragment>
                      {/* CONT BANCAR */}
                      <Col md={12}>
                        <Card className="mt-2">
                          <Card.Header
                            style={{ cursor: 'pointer' }}
                            onClick={() =>
                              this.setState({
                                showContBancar: !this.state.showContBancar,
                              })
                            }
                          >
                            <Card.Title
                              as="h5"
                              aria-controls="accordion1"
                              aria-expanded={this.state.showContBancar}
                            >
                              Conturi bancare
                            </Card.Title>
                          </Card.Header>

                          <Collapse in={this.state.showContBancar}>
                            <div id="accordion1">
                              <Card.Body>
                                <ContBancarTabel />
                              </Card.Body>
                            </div>
                          </Collapse>
                        </Card>
                      </Col>

                      {/* CENTRE COST */}
                      <Col md={12}>
                        <Card className="mt-2">
                          <Card.Header
                            style={{ cursor: 'pointer' }}
                            onClick={() =>
                              this.setState({
                                showCentreCost: !this.state.showCentreCost,
                              })
                            }
                          >
                            <Card.Title
                              as="h5"
                              aria-controls="accordion1"
                              aria-expanded={this.state.showCentreCost}
                            >
                              Centre cost
                            </Card.Title>
                          </Card.Header>

                          <Collapse in={this.state.showCentreCost}>
                            <div id="accordion1">
                              <Card.Body>
                                <CentruCostTabel
                                  adresaSocietate={{
                                    id: this.state.idadresa,
                                    adresa: this.state.adresa,
                                    localitate: this.state.localitate,
                                    tipJudet: this.state.tipJudet,
                                    judet: this.state.judet,
                                  }}
                                />
                              </Card.Body>
                            </div>
                          </Collapse>
                        </Card>
                      </Col>
                    </React.Fragment>
                  ) : null}
                  <Row>
                    <Col md={6}>
                      <Button variant="outline-primary" type="submit">
                        {this.state.isEdit ? 'Actualizează' : 'Adaugă'}
                      </Button>
                    </Col>
                  </Row>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default Societate;
