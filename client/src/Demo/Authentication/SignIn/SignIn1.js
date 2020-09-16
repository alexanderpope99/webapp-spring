import React from 'react';
import { NavLink } from 'react-router-dom';
import { Button, Modal } from 'react-bootstrap';

import './../../../assets/scss/style.scss';
import Aux from '../../../hoc/_Aux';
import Breadcrumb from '../../../App/layout/AdminLayout/Breadcrumb';
import { server } from '../../Resources/server-address';

class SignUp1 extends React.Component {
  constructor() {
    super();
    this.handleClick = this.handleClick.bind(this);
    this.state = {
      username: '',
      password: '',
      show: false,
    };
  }
  async handleClick() {
    await fetch(
      `${server.address}/user/usr=${this.state.username}/pass=${this.state.password}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
      }
    )
      .then((res) => {
        if (res.ok) {
          sessionStorage.setItem('logged', true);
          sessionStorage.setItem('username', this.state.username);
          window.location.href = '/dashboard/societati';
        } else {
          this.setState({ show: true });
        }
      })
      .catch((err) => {
        console.error(err);
      });
  }
  render() {
    return (
      <Aux>
        <Modal
          show={this.state.show}
          onHide={() => this.setState({ show: false })}
        >
          <Modal.Header closeButton>
            <Modal.Title>Utilizator invalid</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button
              variant="primary"
              onClick={() => this.setState({ show: false })}
            >
              OK
            </Button>
          </Modal.Footer>
        </Modal>
        <Breadcrumb />
        <div className="auth-wrapper">
          <div className="auth-content">
            <div className="auth-bg">
              <span className="r" />
              <span className="r s" />
              <span className="r s" />
              <span className="r" />
            </div>
            <div className="card">
              <div className="card-body text-center">
                <div className="mb-4">
                  <i className="feather icon-unlock auth-icon" />
                </div>
                <h3 className="mb-4">Login</h3>
                <div className="input-group mb-3">
                  <input
                    onChange={(e) =>
                      this.setState({
                        username: e.target.value,
                      })
                    }
                    type="username"
                    className="form-control"
                    placeholder="Username"
                  />
                </div>
                <div className="input-group mb-4">
                  <input
                    onChange={(e) =>
                      this.setState({
                        password: e.target.value,
                      })
                    }
                    type="password"
                    className="form-control"
                    placeholder="password"
                  />
                </div>
                <div className="form-group text-left">
                  <div className="checkbox checkbox-fill d-inline">
                    <input
                      type="checkbox"
                      name="checkbox-fill-1"
                      id="checkbox-fill-a1"
                    />
                    <label htmlFor="checkbox-fill-a1" className="cr">
                      {' '}
                      Save credentials
                    </label>
                  </div>
                </div>
                <button
                  onClick={this.handleClick}
                  className="btn btn-primary shadow-2 mb-4"
                >
                  Login
                </button>
                <p className="mb-2 text-muted">
                  Forgot password?{' '}
                  <NavLink to="/auth/reset-password-1">Reset</NavLink>
                </p>
                <p className="mb-0 text-muted">
                  Don’t have an account?{' '}
                  <NavLink to="/auth/signup-1">Signup</NavLink>
                </p>
              </div>
            </div>
          </div>
        </div>
      </Aux>
    );
  }
}

export default SignUp1;
