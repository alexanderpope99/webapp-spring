import React from 'react';
import { Row, Col, Card, Button } from 'react-bootstrap';
import Aux from '../../hoc/_Aux';

import { getSocSel, setSocSel } from '../Resources/socsel';
import { server } from '../Resources/server-address';

class Societati extends React.Component {
  /*
    TODO
    * selecteaza societate la fiecare adaugare/citire care are legatura cu societeatea
    * : adauga persoana | lista angajati | editeaza angajat | reailzari retineri
    * on click "Editeaza" -> redirect to Edit societate page (same as persoane)
    * on click "Sterge" -> prompt confirm, delete only on user confirmation
  */
  constructor() {
    super();
    this.unselectAll = this.unselectAll.bind(this);
    this.downloadButton = this.downloadButton.bind(this);

    this.state = {};
  }

  async componentDidMount() {
    await this.getNumeSocietati();
  }

  async getNumeSocietati() {
    let societati = await fetch(`${server.address}/societate`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    }).then((response) => response.json());
    if (Array.isArray(societati)) {
      societati.forEach((societate) =>
        this.setState({
          [societate.nume]: { opacity: '.3', id: societate.id },
        })
      );
    }
    let selected = getSocSel();
    // console.log(selected);
    if (selected) this.select(selected.nume);
  }

  unselectAll() {
    for (let nume_soc in this.state) {
      this.setState({
        [nume_soc]: { opacity: '.3', id: this.state[nume_soc].id },
      });
    }
  }

  select(nume_soc) {
    this.unselectAll();
    if (nume_soc) {
      let id = this.state[nume_soc].id;
      this.setState({
        [nume_soc]: { opacity: '1', id: id },
      });

      setSocSel({ id: id, nume: nume_soc });
      console.log(getSocSel());
    }
  }

  async downloadButton() {
    console.log('trying to download...');
    await fetch(`${server.address}/download/Stat Salarii - Ingenio Software S.A. - Septembrie 2020.xlsx`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/octet-stream' },
    })
      .then((res) => res.blob())
      .then((blob) => {
        var url = window.URL.createObjectURL(blob);
        var a = document.createElement('a');
        a.href = url;
        a.download = "Stat Salarii - Ingenio Software S.A. - Septembrie 2020.xlsx";
        document.body.appendChild(a); // we need to append the element to the dom -> otherwise it will not work in firefox
        a.click();
        a.remove();  //afterwards we remove the element again
      });
  }

  render() {
    const societatiComponent = Object.keys(this.state).map((nume_soc) => (
      <Col md={6} xl={4} key={nume_soc}>
        <Card
          style={{
            opacity: this.state[nume_soc].opacity,
            cursor: this.state[nume_soc].opacity === '1' ? '' : 'pointer',
          }}
          onClick={
            this.state[nume_soc].opacity === '.3'
              ? () => {
                  this.select(nume_soc);
                }
              : null
          }
        >
          <Card.Body>
            <h3 className="d-flex justify-content-around pb-5">{nume_soc}</h3>
            <div className="d-flex flex-inline justify-content-end">
              <Button size="sm" className="m-1 p-1">
                Editează
              </Button>
              <Button size="sm" className="m-1 p-1">
                Șterge
              </Button>
            </div>
          </Card.Body>
        </Card>
      </Col>
    ));

    return (
      <Aux>
        <Button onClick={this.downloadButton}>download</Button>
        <Row>{societatiComponent}</Row>
      </Aux>
    );
  }
}

export default Societati;
