import React from 'react';
import axios from 'axios';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import { Card, Row, Col, Form } from 'react-bootstrap';

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
    };
  }

  componentDidMount() {
    this.getConcediiOdihna();
    this.getConcediiMedicale();
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
        return {
          title: concediu.numeangajat + ' - ' + concediu.tip,
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
          title: concediu.numeangajat + ' - Concediu Medical',
          start: concediu.dela,
          end: endDate.toISOString().substring(0, 10),
					color: '#a72c51',
        };
      });
      this.setState({ cm: cm || [] });
    }
  }
	
	tipuriConcedii = ['Concedie de odihnă', 'Concedii medicale'];

  render() {

		const concediiComponent = this.state.tip === 2 ? [...this.state.co, ...this.state.cm] : this.state.tip ? this.state.cm : this.state.co;

    return (
      <Card>
        <Card.Header>
          <Card.Title as="h5">Concedii</Card.Title>

          <Row>
            <Form.Group as={Col} sm="6" className="mt-3">
              <Form.Control
                as="select"
                value={this.tipuriConcedii[this.state.tip]}
                onChange={(e) => this.setState({ tip: e.target.options.selectedIndex })}
								defaultValue="Toate"
              >
                <option>Concedii odihna</option>
                <option>Concedii medicale</option>
                <option>Toate</option>
              </Form.Control>
            </Form.Group>
          </Row>
        </Card.Header>
        <Card.Body>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, listPlugin]}
            initialView="dayGridMonth"
            // 0 is default, will go to ConcediiOdihna, 1 is the other value -> goes to ConcediiMedicale
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
      </Card>
    );
  }
}
