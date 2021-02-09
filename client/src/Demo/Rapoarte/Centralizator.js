import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { download } from '../Resources/download';
import { luni } from '../Resources/calendar';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class Centralizator extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.creeazaCentralizator = this.creeazaCentralizator.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      intocmitDe: '',
      user: authService.getCurrentUser(),
      showToast: false,
	  toastMessage: '',
	  pentru:'Vârstă',
	  tip:'Rezumat',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.setCurrentYearMonth();
  }

  setCurrentYearMonth() {
    let today = new Date();
    let luna = luni[today.getMonth()];
    let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  async creeazaCentralizator() {
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
	let an = this.state.an;
	let pentru="";
	let tip="";
	if(this.state.pentru==="Vârstă" )pentru="1";
	if(this.state.pentru==="Sex")pentru="2";
	if(this.state.pentru==="Vechime")pentru="3";
	if(this.state.tip==="Rezumat")tip="1";
	if(this.state.tip==="Complet")tip="2";

	console.log(`${server.address}/societate/raport/centralizator/${pentru}/${tip}/${this.state.socsel.id}/mo=${luna.nr}&y=${an}/${this.state.user.id}`)

    const created = await axios
      .get(
        `${server.address}/societate/raport/centralizator/${pentru}/${tip}/${this.state.socsel.id}/mo=${luna.nr}&y=${an}/${this.state.user.id}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea centralizator '+this.state.pentru.toLowerCase() +" "+this.state.tip.toLowerCase() + err.response.data.message,
        })
	  );
	  console.log(`Centralizator ${this.state.pentru} ${this.state.tip} - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`)

    if (created)
      download(`Centralizator ${this.state.pentru} ${this.state.tip} - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`, this.state.user.id);
  }

  render() {
    const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    return (
      <React.Fragment>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'white' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        <Card className="border">
          <Card.Header>
            <Typography variant="h5">Centralizator</Typography>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={this.creeazaCentralizator}>
              <Row>
                {/* LUNA */}
                <Col md={3}>
                  <Form.Control
                    as="select"
                    value={this.state.luna.nume}
                    onChange={(e) =>
                      this.setState({
                        luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
                      })
                    }
                  >
                    {luniComponent}
                  </Form.Control>
                </Col>
                {/* AN */}
                <Col md={3}>
                  <FormControl
                    type="number"
                    value={this.state.an}
                    onChange={(e) =>
                      this.setState({
                        an: e.target.value,
                      })
                    }
                  />
                </Col>
				<Col md={3}>
                <Form.Group>
                  <Form.Control
                    as="select"
                    value={this.state.pentru}
                    onChange={(e) => this.setState({
						pentru:e.target.value
					})}
                  >
                    <option>Vârstă</option>
					<option>Sex</option>
					<option>Vechime</option>
                  </Form.Control>
                </Form.Group>
					</Col>
					<Col md={3}>
				<Form.Group>
                  <Form.Control
                    as="select"
                    value={this.state.tip}
                    onChange={(e) => this.setState({
						tip:e.target.value
					})}
                  >
                    <option>Rezumat</option>
                    <option>Complet</option>
                  </Form.Control>
                </Form.Group>
				</Col>
              </Row>
            </Form>
            <div className="mt-4">
              <Button onClick={this.creeazaCentralizator}>Centralizator în Excel</Button>
            </div>
          </Card.Body>
        </Card>
      </React.Fragment>
    );
  }
}

export default Centralizator;
