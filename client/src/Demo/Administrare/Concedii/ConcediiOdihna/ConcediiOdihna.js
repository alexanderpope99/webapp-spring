import React from 'react';
import axios from 'axios';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import { Card } from 'react-bootstrap';

import { server } from '../../../Resources/server-address';
import authHeader from '../../../../services/auth-header';

export default class ConcediiOdihna extends React.Component {
  constructor() {
    super();
    this.state = {
      conciediiOdihna: [],
    };
  }

  componentDidMount() {
    this.getConcediiOdihna();
  }

  async getConcediiOdihna() {
    var co = await axios
      .get(`${server.address}/co`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    co = co.map((concediu) => ({
      title: concediu.numeangajat,
      start: concediu.dela,
      end: concediu.panala,
    }));
    this.setState({ co: co || [] });
  }

  render() {
    return (
      <Card>
        <Card.Body>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, listPlugin]}
            initialView="dayGridMonth"
            events={this.state.co || []}
            headerToolbar={{
              left: 'prev,next today',
              center: 'title',
              right: 'dayGridMonth,timeGridWeek,listWeek',
            }}
          />
        </Card.Body>
      </Card>
    );
  }
}
