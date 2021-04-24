import React from 'react';
import axios from 'axios';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import { Card, Form, Row, Col } from 'react-bootstrap';

import { server } from '../../Resources/server-address';
import authHeader from '../../../services/auth-header';
import { getSocSel } from '../../Resources/socsel';

export default class Concedii extends React.Component {
  constructor() {
    super();
    this.state = {
      socsel: getSocSel(),

      tip: 2,

      co: [],
      cm: [],
      sarbatori: [],
      zileNastere: [],
    };
  }

  componentDidMount() {
    this.getConcediiOdihna();
    this.getConcediiMedicale();
    this.getSarbatori();
    // this.getZileNastere();
  }

  async getConcediiOdihna() {
    var co = await axios
      .get(`${server.address}/co/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (co) {
      co = co.map((concediu) => {
        var endDate = new Date(concediu.panala);
        endDate.setDate(endDate.getDate() + 1);
        var tipConcediu = '';
        switch (concediu.tip) {
          case 'Concediu de odihnă':
            tipConcediu = 'C.O.';
            break;

          case 'Concediu fără plată':
            tipConcediu = 'C.F.P';
            break;

          case 'Concediu pentru situații speciale':
            tipConcediu = 'C. Sit. Spec.';
            break;

          case 'Concediu pentru studii':
            tipConcediu = 'C. Studii';
            break;

          default:
            tipConcediu = 'C.O.';
            break;
        }
        return {
          title: concediu.numeangajat + ' - ' + tipConcediu,
          start: concediu.dela,
          end: endDate.toISOString().substring(0, 10),
        };
      });
      this.setState({ co: co || [] });
    }
  }

  async getConcediiMedicale() {
    var cm = await axios
      .get(`${server.address}/cm/ids=${this.state.socsel.id}`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (cm) {
      cm = cm.map((concediu) => {
        var endDate = new Date(concediu.panala);
        endDate.setDate(endDate.getDate() + 1);
        return {
          title: concediu.numeangajat + ' - C.M.',
          start: concediu.dela,
          end: endDate.toISOString().substring(0, 10),
          color: '#a72c51',
        };
      });
      this.setState({ cm: cm || [] });
    }
  }

  async getSarbatori() {
    var sarbatori = await axios
      .get(`${server.address}/sarbatori/`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    if (sarbatori) {
      sarbatori = sarbatori.map((sarb) => {
        var endDate = new Date(sarb.panala);
        endDate.setDate(endDate.getDate() + 1);
        return {
          title: sarb.nume,
          start: sarb.dela,
          end: endDate.toISOString().substring(0, 10),
          display: 'background'
        };
      });
      this.setState({ sarbatori: sarbatori || [] });
    }
  }

  onChangeTip(e) {
    console.log(e.target.selectedIndex);

  }

  tipuriConcediu = ['Concedii Odihnă', 'Concedii Medicale', 'Toate'];

  render() {
    // tip: 2 = all, 1 = cm, 0 = co
    const concediiComponent =
      this.state.tip === 2
        ? [...this.state.co, ...this.state.cm]
        : this.state.tip
          ? this.state.cm
          : this.state.co;
      concediiComponent.push(...this.state.sarbatori);

    return (
      <Card>
        <Card.Header>
          <Card.Title as="h5">Concedii</Card.Title>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md="3">
              <Form.Group>
                <Form.Label>Tip Concediu</Form.Label>
                <Form.Control
                  as="select"
                  value={this.tipuriConcediu[this.state.tip]}
                  onChange={(e) => this.setState({ tip: e.target.selectedIndex })}
                >
                  <option>Concedii Odihnă</option>
                  <option>Concedii Medicale</option>
                  <option>Toate</option>
                </Form.Control>
              </Form.Group>
            </Col>
          </Row>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, listPlugin]}
            initialView="dayGridMonth"
            events={concediiComponent}
            headerToolbar={{
              left: 'prevYear,prev,next,nextYear,today',
              center: 'title',
              right: '',
            }}
            locale="ro"
            firstDay="1"
            fixedWeekCount={false}
            showNonCurrentDates={false}
          />
        </Card.Body>
      </Card >
    );
  }
}
