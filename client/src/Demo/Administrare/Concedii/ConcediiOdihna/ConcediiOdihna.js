import React from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import listPlugin from '@fullcalendar/list';
import { Card } from 'react-bootstrap';

export default class ConcediiOdihna extends React.Component {
  render() {
    return (
      <Card>
        <Card.Body>
          <FullCalendar
            plugins={[dayGridPlugin, timeGridPlugin, listPlugin]}
            initialView="dayGridMonth"
            events={[{ title: 'Sfanta Jigodie', start: '2021-03-11', end: '2021-03-15' }]}
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
