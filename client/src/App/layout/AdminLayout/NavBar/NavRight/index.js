import React, { Component } from 'react';
import { Dropdown, Button } from 'react-bootstrap';

import Aux from '../../../../../hoc/_Aux';
import DEMO from '../../../../../store/constant';

import Avatar1 from '../../../../../assets/images/user/avatar-1.jpg';
import Avatar2 from '../../../../../assets/images/user/avatar-2.jpg';

import { server } from '../../../../../Demo/Resources/server-address';
import axios from 'axios';
import authHeader from '../../../../../../src/services/auth-header';

import authService from '../../../../../../src/services/auth.service';

import { Circle, X, Bell, Settings, Info } from 'react-feather';

class NavRight extends Component {
  constructor() {
    super();
    this.onRefresh = this.onRefresh.bind(this);
    this.logOut = this.logOut.bind(this);
    this.readNotification = this.readNotification.bind(this);
    this.readAllNotifications = this.readAllNotifications.bind(this);
    this.openInNewTab = this.openInNewTab.bind(this);

    this.state = {
      time: Date.now(),
      listOpen: false,
      user: authService.getCurrentUser(),
      notificari: [],
      cursEURRON: '',
    };
  }

  componentDidMount() {
    this.interval = setInterval(() => this.onRefresh(), 60000);
    this.onRefresh();
  }
  componentWillUnmount() {
    clearInterval(this.interval);
  }

  logOut() {
    authService.logout();
    window.location.href = '/auth/signin-1';
  }

  async onRefresh() {
    const notificari = await axios
      .get(`${server.address}/notificare/userid/${this.state.user.id}/unread`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (notificari)
      this.setState({
        notificari: notificari,
      });

    const curs = await axios
      .get(`${server.address}/webparse/cursbnr`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (curs)
      this.setState({
        cursEURRON: curs,
      });
  }

  async readNotification(e) {
    await axios
      .get(`${server.address}/notificare/${e.id}/read`, { headers: authHeader() })
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  async readAllNotifications() {
    this.state.notificari.forEach(async (noti) => {
      axios
        .get(`${server.address}/notificare/${noti.id}/read`, { headers: authHeader() })
        .then(this.onRefresh)
        .catch((err) => console.error(err));
    });
  }

  getAvatarIcon() {
    return this.state.user ? (this.state.user.gen ? Avatar2 : Avatar1) : Avatar1;
  }

  millisConverter(millis) {
    switch (true) {
      case millis < 60000:
        return 'acum ' + ((millis % 60000) / 1000).toFixed(0) + ' s';
      case millis < 3600000:
        return (
          'acum ' + Math.floor(millis / 60000) + 'm ' + ((millis % 60000) / 1000).toFixed(0) + ' s'
        );
      case millis < 86400000:
        var minutes = Math.floor((millis / (1000 * 60)) % 60);
        var hours = Math.floor((millis / (1000 * 60 * 60)) % 24);

        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        return 'acum ' + hours + ' h ' + minutes + ' m ';
      case millis < 604800000:
        return Math.floor(millis / 86400000) + ' days';
      default:
        const data = Date(millis).split(' ');
        return data[2] + ' ' + data[1] + ' ' + data[3];
    }
  }

  openInNewTab() {
    var win = window.open('https://www.cursbnr.ro/', '_blank');
    win.focus();
  }

  render() {
    const AvatarProp = this.getAvatarIcon();

    var notificari = [];
    if (this.state.notificari.length > 0)
      notificari = this.state.notificari.map((notificare, index) => (
        <li key={index} className="notification">
          <div className="media">
            <div className="media-body">
              <p>
                <strong onClick={() => (window.location.href = notificare.hyperlink)}>
                  {notificare.titlu}
                </strong>
                <span
                  onClick={() => (window.location.href = notificare.hyperlink)}
                  className="n-time text-muted"
                >
                  <i className="icon feather icon-clock m-r-10" />
                  {this.millisConverter(Date.now() - Date.parse(notificare.timp))}
                </span>
              </p>
              <p>{notificare.mesaj}</p>
            </div>
            <Button onClick={() => this.readNotification(notificare)}>
              <X />
            </Button>
          </div>
        </li>
      ));

    return (
      <Aux>
        <div className="navbar-nav" style={{ marginTop: '25px', marginLeft: '30px' }}>
          <h4>Societate</h4>
        </div>
        <ul className="navbar-nav ml-auto">
          <li>
            <Dropdown onClick={(e) => e.stopPropagation()}>
              <Dropdown.Toggle variant="link" id="dropdown-basic">
                {notificari.length > 0 ? (
                  <Circle style={{ width: '7.5', height: '7.5' }} fill="red" />
                ) : (
                  ''
                )}
                <Bell size={20} />
              </Dropdown.Toggle>
              <Dropdown.Menu className="notification">
                <div className="noti-head">
                  <h6 className="d-inline-block m-b-0">Notificări</h6>
                  {/* <div className="float-right">
                    <a href={DEMO.BLANK_LINK} className="m-r-10">
                      mark as read
                    </a>
                    <Button onClick={() => this.readAllNotifications()} variant="outline-grey">
                      <X size={15}/>
                    </Button>
                  </div> */}
                </div>
                <ul className="noti-body">{notificari}</ul>
                {/* <div className="noti-footer">
                  <a href={'/notificari'}>arată tot</a>
                </div> */}
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li>
            <Dropdown onClick={(e) => e.stopPropagation()}>
              <Dropdown.Toggle variant="link" id="dropdown-basic">
                <Info size={20} />
              </Dropdown.Toggle>
              <Dropdown.Menu className="notification">
                <div className="noti-head">
                  <h6 className="d-inline-block m-b-0">Informații</h6>
                </div>
                <ul className="noti-body">
                  <li className="notification">
                    <div className="media">
                      <div className="media-body">
                        <p>
                          <strong style={{ cursor: 'pointer' }} onClick={this.openInNewTab}>
                            Curs Valutar BNR EUR/RON
                          </strong>
                        </p>
                        <p>1 EUR = {this.state.cursEURRON} RON</p>
                      </div>
                    </div>
                  </li>
                </ul>
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li>
            <Dropdown onClick={(e) => e.stopPropagation()} className="drp-user">
              <Dropdown.Toggle variant={'link'} id="dropdown-basic">
                <Settings size={20} />
              </Dropdown.Toggle>
              <Dropdown.Menu alignRight className="profile-notification">
                <div className="pro-head">
                  <img src={AvatarProp} className="img-radius" alt="User Profile" />
                  <span>{this.state.user ? this.state.user.username : ''}</span>
                  <a
                    href={DEMO.BLANK_LINK}
                    className="dud-logout"
                    title="Logout"
                    onClick={this.logOut}
                  >
                    <i className="feather icon-log-out" />
                  </a>
                </div>
                <ul className="pro-body">
                  <li>
                    <a href="/edit/profile" className="dropdown-item">
                      <i className="feather icon-user" /> Profil
                    </a>
                  </li>
                  <li>
                    <a href="/edit/setari" className="dropdown-item">
                      <i className="feather icon-settings" /> Setări
                    </a>
                  </li>
                  {/* <li>
                    <a href={DEMO.BLANK_LINK} className="dropdown-item">
                      <i className="feather icon-mail" /> Mesaje
                    </a>
                  </li> */}
                </ul>
              </Dropdown.Menu>
            </Dropdown>
          </li>
        </ul>
      </Aux>
    );
  }
}

export default NavRight;
