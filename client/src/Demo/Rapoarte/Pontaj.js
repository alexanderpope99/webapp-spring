import React from 'react';
import { Row, Col, Card, Form, Button, FormControl, Toast } from 'react-bootstrap';
import Typography from '@material-ui/core/Typography/Typography';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import { luni } from '../Resources/calendar';
import { download } from '../Resources/download';
import authService from '../../services/auth.service';

class Pontaj extends React.Component {
  constructor() {
    super();

    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.creeazaFoaiePontaj = this.creeazaFoaiePontaj.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      user: authService.getCurrentUser(),

      showToast: false,
      toastMessage: '',
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

  async creeazaFoaiePontaj(e) {
    e.preventDefault();
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    const created = await fetch(
      `${server.address}/pontaj/${this.state.socsel.id}/mo=${luna.nr}&y=${an}/${this.state.user.id}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${this.state.user.accessToken}`,
        },
      }
    )
      .then((res) => res.ok)
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut crea foaie pontaj\n' + err.response.data.message,
        })
      );

    if (created)
      download(
        `Foaie Pontaj - ${this.state.socsel.nume} - ${luna.nume} ${an}.xlsx`,
        this.state.user.id
      );
  }

  render() {
    const luniComponent = luni.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    return (
      <Card>
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
        <Card.Header>
          <Typography variant="h5"> Foaie de pontaj</Typography>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={this.creeazaFoaiePontaj}>
            <Row>
              {/* LUNA */}
              <Col md={4}>
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
            <Button onClick={this.creeazaFoaiePontaj}>Foaie pontaj in Excel</Button>
          </div>
        </Card.Body>
      </Card>
    );
  }
}

export default Pontaj;
