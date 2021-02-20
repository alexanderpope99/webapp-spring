import React from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { Redirect } from 'react-router-dom';

import './../../../assets/scss/style.scss';
import Aux from '../../../hoc/_Aux';
import AuthService from '../../../services/auth.service';

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
  async handleClick(e) {
    e.preventDefault();

    AuthService.login(this.state.username, this.state.password).then(
      () => {
        this.props.history.push('/profile');
        window.location.href = '/dashboard/societati';
      },
      (error) => {
        this.setState({ show: true });
      }
    );
  }
  render() {
      return (
        <Aux>
          <Form onSubmit={(e) => this.handleClick(e)}>
            <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
              <Modal.Header closeButton>
                <Modal.Title>Utilizator invalid</Modal.Title>
              </Modal.Header>
              <Modal.Footer>
                <Button variant="primary" onClick={() => this.setState({ show: false })}>
                  OK
                </Button>
              </Modal.Footer>
            </Modal>
            
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
                        value={this.state.username}
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
                        value={this.state.password}
                        type="password"
                        className="form-control"
                        placeholder="password"
                      />
                    </div>
                    {/* <div className="form-group text-left">
                      <div className="checkbox checkbox-fill d-inline">
                        <input
                          onChange={() => {
                            localStorage.setItem('savedCheckbox', !this.state.checked);
                            this.setState({
                              checked: !this.state.checked,
                            });
                          }}
                          checked={this.state.checked}
                          type="checkbox"
                          name="checkbox-fill-1"
                          id="checkbox-fill-a1"
                        />
                        <label htmlFor="checkbox-fill-a1" className="cr">
                          {' '}
                          Save credentials
                        </label>
                      </div>
                    </div> */}
                    <button type="submit" className="btn btn-primary shadow-2 mb-4">
                      Login
                    </button>
                    {/* <p className="mb-2 text-muted">
                    Forgot password? <NavLink to="/auth/reset-password-1">Reset</NavLink>
                  </p>
                  <p className="mb-0 text-muted">
                    Don’t have an account? <NavLink to="/auth/signup-1">Signup</NavLink>
                  </p> */}
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
