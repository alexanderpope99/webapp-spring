import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { download } from '../Resources/download';
import axios from 'axios';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

class RaportConcedii extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.creeazaRaportConcedii = this.creeazaRaportConcedii.bind(this);

    this.state = {
      socsel: getSocSel(),
      an: '',
      intocmitDe: '',
      user: authService.getCurrentUser(),
      showToast: false,
      toastMessage: '',
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.setCurrentYear();
  }

  setCurrentYear() {
    let today = new Date();
    let an = today.getFullYear();

    this.setState({
      an: an,
    });
  }

  async creeazaRaportConcedii() {
    // make request to create stat for soc, an
    let an = this.state.an;

    console.log(
      `${server.address}/societate/raport/concedii/${this.state.socsel.id}/y=${an}/${this.state.user.id}`
    );

    const created = await axios
      .get(
        `${server.address}/societate/raport/concedii/${this.state.socsel.id}/y=${an}/${this.state.user.id}`,
        { headers: authHeader() }
      )
      .then((res) => res.data)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea raport concedii: ' + (err.response
              ? err.response.data.message
              : 'Nu s-a putut stabili conexiunea la server'),
        })
      );

    if (created)
      download(`Raport Concedii - ${this.state.socsel.nume} - ${an}.xlsx`, this.state.user.id);
  }

  render() {

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
            <Typography variant="h5">Raport Concedii</Typography>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={this.creeazaRaportConcedii}>
              <Row>
                {/* AN */}
                <Col md={4}>
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
              </Row>
            </Form>
            <div className="mt-4">
              <Button onClick={this.creeazaRaportConcedii}>Raport Concedii în Excel</Button>
            </div>
          </Card.Body>
        </Card>
      </React.Fragment>
    );
  }
}

export default RaportConcedii;
