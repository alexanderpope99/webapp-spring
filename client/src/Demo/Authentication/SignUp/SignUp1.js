import React from 'react';
import { NavLink } from 'react-router-dom';
import { Modal, Form, Button } from 'react-bootstrap';

import './../../../assets/scss/style.scss';
import Aux from '../../../hoc/_Aux';
import AuthService from '../../../services/auth.service';
import { Multiselect } from 'multiselect-react-dropdown';

class SignUp1 extends React.Component {
  constructor() {
    super();
    this.handleRegister = this.handleRegister.bind(this);
    this.state = {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      message: '',
      show: false,
    };
  }
  async handleRegister(e) {
    e.preventDefault();

    if (this.state.confirmPassword !== this.state.password)
      this.setState({
        show: true,
        message: 'Parolele nu coincid',
      });
    else {
      AuthService.register(this.state.username, this.state.email, this.state.password).then(
        (response) => {
          this.setState({
            show: true,
            message: 'Utilizator adăugat cu succes',
          });
        },
        (error) => {
          const resMessage =
            (error.response && error.response.data && error.response.data.message) ||
            error.message ||
            error.toString();

          this.setState({
            show: true,
            message: this.state.username + ' ' + this.state.email + ' ' + this.state.password,
          });
        }
      );
    }
  }
  render() {
    return (
      <Aux>
        <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
          <Modal.Header closeButton>
            <Modal.Title>{this.state.message}</Modal.Title>
          </Modal.Header>
          <Modal.Footer>
            <Button variant="primary" onClick={() => this.setState({ show: false })}>
              OK
            </Button>
          </Modal.Footer>
        </Modal>
        <Form onSubmit={(e) => this.handleRegister(e)}>
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
                    <i className="feather icon-user-plus auth-icon" />
                  </div>
                  <h3 className="mb-4">Sign up</h3>
                  <div className="input-group mb-3">
                    <input
                      onChange={(e) =>
                        this.setState({
                          username: e.target.value,
                        })
                      }
                      value={this.state.username}
                      type="text"
                      className="form-control"
                      placeholder="Username"
                    />
                  </div>
                  <div className="input-group mb-3">
                    <input
                      onChange={(e) =>
                        this.setState({
                          email: e.target.value,
                        })
                      }
                      value={this.state.email}
                      type="email"
                      className="form-control"
                      placeholder="Email"
                    />
                  </div>
                  <div className="input-group mb-3">
                    <input
                      onChange={(e) =>
                        this.setState({
                          password: e.target.value,
                        })
                      }
                      value={this.state.password}
                      type="password"
                      className="form-control"
                      placeholder="password"
                    />
                  </div>
                  <div className="input-group mb-3">
                    <input
                      onChange={(e) =>
                        this.setState({
                          confirmPassword: e.target.value,
                        })
                      }
                      value={this.state.confirmPassword}
                      type="password"
                      className="form-control"
                      placeholder="password"
                    />
                  </div>
                  <div className="input-group mb-4">
                    <Multiselect options={['admin', 'dir', 'cont', 'ang']} isObject={false} />
                  </div>
                  {/* <div className="form-group text-left">
                  <div className="checkbox checkbox-fill d-inline">
                    <input type="checkbox" name="checkbox-fill-2" id="checkbox-fill-2" />
                    <label htmlFor="checkbox-fill-2" className="cr">
                      Send me the <a href={DEMO.BLANK_LINK}> Newsletter</a> weekly.
                    </label>
                  </div>
                </div> */}
                  <button type="submit" className="btn btn-primary shadow-2 mb-4">
                    Sign up
                  </button>
                  <p className="mb-0 text-muted">
                    Allready have an account? <NavLink to="/auth/signin-1">Login</NavLink>
                  </p>
                </div>
              </div>
            </div>
          </div>
        </Form>
      </Aux>
    );
  }
}

export default SignUp1;
