import React from 'react';
import axios from 'axios';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import { Card } from 'react-bootstrap';

import { server } from '../../../Resources/server-address';
import authHeader from '../../../../services/auth-header';

import { getSocSel } from '../../../Resources/socsel';

export default class ConcediiOdihna extends React.Component {
  constructor() {
    super();
    this.state = {
	socsel: getSocSel(),
      events: [],
    };
  }

  componentDidMount() {
    this.getEvents();
  }

  async getEvents() {
    var co = await axios
      .get(`${server.address}/co`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    co = co.map((concediu) => ({
      title: "Concediu "+concediu.numeangajat,
      start: concediu.dela,
      end: concediu.panala,
    }));
	var zn = await axios
	.get(`${server.address}/persoana/ids=${this.state.socsel.id}&c`, { headers: authHeader() })
	.then((res) => res.data)
	.catch((err) => console.error(err));
  	zn = zn.map((persoana) => ({
	title: "Ziua lui "+persoana.nume+" "+persoana.prenume,
	color:"purple",
	date: (new Date().getFullYear())+"-"+persoana.actidentitate.datanasterii.substring(5,10),
  }));
    this.setState({ events: co.concat(zn) || [] });
  }


  render() {
    return (
      <Card>
        <Card.Body>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, listPlugin]}
            initialView="dayGridMonth"
            events={this.state.events || []}
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
