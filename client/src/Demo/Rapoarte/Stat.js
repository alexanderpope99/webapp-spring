import React from 'react';
import { Row, Col, Card, Form, Button, FormControl } from 'react-bootstrap';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import months from '../Resources/months';

class Stat extends React.Component {
  constructor() {
    super();
    this.download = this.download.bind(this);
    this.creeazaStatSalarii = this.creeazaStatSalarii.bind(this);

    this.state = {
      socsel: getSocSel(),
      luna: '',
      an: '',
      intocmitDe: '',
    };
  }

  componentDidMount() {
    this.setCurrentYearMonth();
  }

  setCurrentYearMonth() {
    let today = new Date();
    let luna = months[today.getMonth()];
    let an = today.getFullYear();

    this.setState({
      an: an,
      luna: { nume: luna, nr: today.getMonth() + 1 },
    });
  }

  // luna is object of type { nume: string, nr: int }
  async download(luna, an) {
    console.log('trying to download...');
    let societateNume = this.state.socsel.nume;

    await fetch(
      `${server.address}/download/Stat Salarii - ${societateNume} - ${luna.nume} ${an}.xlsx`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/octet-stream' },
      }
    )
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = `Stat Salarii - ${societateNume} - ${luna.nume} ${an}.xlsx`;
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove(); //afterwards we remove the element again
      });
    console.log('downloaded');
  }

  async creeazaStatSalarii() {
    // make request to create stat for soc, luna, an
    let luna = this.state.luna;
    let an = this.state.an;
    let i = this.state.intocmitDe ? this.state.intocmitDe : '-';
    const created = await fetch(
      `${server.address}/stat/${this.state.socsel.id}/mo=${luna.nr}&y=${an}&i=${i}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }
    )
      .then((res) => res.ok)
      .catch((err) => console.error(err));

    if (created) this.download(luna, an);
  }

  render() {
    const luni = months.map((luna_nume, index) => <option key={index}>{luna_nume}</option>);

    const this_year = new Date().getFullYear();
    const ani = [this_year - 1, this_year, this_year + 1, this_year + 2].map((year) => (
      <option key={year}>{year}</option>
    ));

    return (
      <Card>
        <Card.Body>
          <Row>
            {/* LUNA */}
            <Col md={4}>
              <FormControl
                as="select"
                value={this.state.luna.nume}
                onChange={(e) =>
                  this.setState(
                    {
                      luna: { nume: e.target.value, nr: e.target.options.selectedIndex + 1 },
                    },
                    this.fillForm
                  )
                }
              >
                {luni}
              </FormControl>
            </Col>
            {/* AN */}
            <Col md={4}>
              <FormControl
                as="select"
                value={this.state.an}
                onChange={(e) =>
                  this.setState({
                    an: e.target.value,
                  })
                }
              >
                {ani}
              </FormControl>
            </Col>
            <Col md={4}>
              <FormControl
								typr="text"
								placeholder="Intocmid de"
                value={this.state.intocmitDe}
                onChange={(e) =>
                  this.setState({
                    intocmitDe: e.target.value,
                  })
                }
              />
            </Col>
          </Row>
          <div className="mt-3">
            <Button onClick={this.creeazaStatSalarii}>Stat salarii in Excel</Button>
          </div>
        </Card.Body>
      </Card>
    );
  }
}

export default Stat;
