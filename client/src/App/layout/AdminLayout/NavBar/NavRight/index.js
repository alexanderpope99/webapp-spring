import React, { Component } from 'react';
import { Dropdown, Button } from 'react-bootstrap';

import Aux from '../../../../../hoc/_Aux';
import DEMO from '../../../../../store/constant';

import Avatar1 from '../../../../../assets/images/user/avatar-1.jpg';
import Avatar2 from '../../../../../assets/images/user/avatar-2.jpg';
import Avatar3 from '../../../../../assets/images/user/avatar-3.jpg';

import { server } from '../../../../../Demo/Resources/server-address';
import { getSocSel } from '../../../../../Demo/Resources/socsel';
import axios from 'axios';
import authHeader from '../../../../../../src/services/auth-header';

import authService from '../../../../../../src/services/auth.service';

import { Circle, X } from 'react-feather';

class NavRight extends Component {
  constructor() {
    super();
    this.onRefresh = this.onRefresh.bind(this);
    this.logOut = this.logOut.bind(this);
    this.readNotification = this.readNotification.bind(this);
    this.readAllNotifications = this.readAllNotifications.bind(this);
    this.state = {
      time: Date.now(),
      listOpen: false,
      user: authService.getCurrentUser(),
      notificariComponent: [],
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
        notificariComponent: notificari,
      });
  }

  async readNotification(e) {
    await axios
      .get(`${server.address}/notificare/${e.id}/read`, { headers: authHeader() })
      .then((res) => res.data)
      .catch((err) => console.error(err));
    this.onRefresh();
  }

  async readAllNotifications() {
    await this.state.notificariComponent.forEach(async (noti) => {
      console.log(`${server.address}/notificare/${noti.id}/read`);

      axios
        .get(`${server.address}/notificare/${noti.id}/read`, { headers: authHeader() })
        .then((res) => res.data)
        .catch((err) => console.error(err));
      this.onRefresh();
    });
  }

  getAvatarIcon() {
    return this.state.user ? (this.state.user.gen ? Avatar2 : Avatar1) : Avatar1;
  }

  render() {
    const AvatarProp = this.getAvatarIcon();
    function millisConverter(millis) {
      switch (true) {
        case millis < 60000:
          return 'acum ' + ((millis % 60000) / 1000).toFixed(0) + ' s';
        case millis < 3600000:
          return (
            'acum ' +
            Math.floor(millis / 60000) +
            'm ' +
            ((millis % 60000) / 1000).toFixed(0) +
            ' s'
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

    var notificari = [];
    if (this.state.notificariComponent.length > 0)
      notificari = this.state.notificariComponent.map((notificare, index) => (
        <li className="notification">
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
                  {millisConverter(Date.now() - Date.parse(notificare.timp))}
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
        <ul className="navbar-nav ml-auto">
          <li>
            <Dropdown alignRight={!this.props.rtlLayout}>
              <Dropdown.Toggle variant={'link'} id="dropdown-basic">
                {notificari.length > 0 ? (
                  <Circle style={{ width: '7.5', height: '7.5' }} fill="red" />
                ) : (
                  ''
                )}
                <i className="icon feather icon-bell" />
              </Dropdown.Toggle>
              <Dropdown.Menu alignRight className="notification">
                <div className="noti-head">
                  <h6 className="d-inline-block m-b-0">Notificări</h6>
                  <div className="float-right">
                    {/* <a href={DEMO.BLANK_LINK} className="m-r-10">
                      mark as read
                    </a> */}
                    {/* <Button onClick={() => this.readAllNotifications()} variant="outline-grey">
                      <X size={15}/>
                    </Button> */}
                  </div>
                </div>
                <ul className="noti-body">{notificari}</ul>
                <div className="noti-footer">
                  <a href={'/notificari'}>arată tot</a>
                </div>
              </Dropdown.Menu>
            </Dropdown>
          </li>
          <li>
            <Dropdown alignRight={!this.props.rtlLayout} className="drp-user">
              <Dropdown.Toggle variant={'link'} id="dropdown-basic">
                <i className="icon feather icon-settings" />
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
