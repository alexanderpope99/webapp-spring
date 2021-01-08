import React from 'react';
import { NavLink, Redirect } from 'react-router-dom';
import { Modal, Form, Button, Row, Col } from 'react-bootstrap';

import './../../../assets/scss/style.scss';
import Aux from '../../../hoc/_Aux';
import AuthService from '../../../services/auth.service';

class SignUp1 extends React.Component {
  constructor() {
    super();
    if (sessionStorage.getItem('user') !== null) window.location.href = '/dashboard/societati';
    this.handleRegister = this.handleRegister.bind(this);
    this.state = {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      gen: true,
      message: '',
      show: false,
      options: [
        { name: 'Srigar', id: 1 },
        { name: 'Sam', id: 2 },
        { name: 'Cristi', id: 3 },
      ],
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
      AuthService.register(
        this.state.username,
        this.state.email,
        this.state.password,
        this.state.gen
      )
        .then((response) => {
          if (response.status === 200)
            this.setState({
              show: true,
              message: 'Utilizator adăugat cu succes',
            });
        })
        .catch(() => {
          this.setState({
            show: true,
            message: 'A apărut o eroare ❌',
          });
        });
    }
  }
  render() {
    if (sessionStorage.getItem('user')) return <Redirect to="/auth/signin-1" />;
    else
      return (
        <Aux>
					{/* CONFIRM MODAL */}
          <Modal show={this.state.show} onHide={() => this.setState({ show: false })}>
            <Modal.Header closeButton>
              <Modal.Title>{this.state.message}</Modal.Title>
            </Modal.Header>
            <Modal.Footer>
              <Button variant="primary" onClick={() => this.setState({ show: false }, ()=>window.location.href="/")}>
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
                      <Row>
                          <Col md={7}>
                            <Form.Check
                              custom
                              type="radio"
                              label="Dl."
                              checked={this.state.gen}
                              onChange={() => this.setState({ gen: !this.state.gen })}
                              name="gen"
                              id="dl"
                            />
                          </Col>
                          <Col md={3}>
                            <Form.Check
                              custom
                              type="radio"
                              label="Dna."
                              checked={!this.state.gen}
                              onChange={() => this.setState({ gen: !this.state.gen })}
                              name="gen"
                              id="dna"
                            />
                          </Col>
                      </Row>
                    </div>
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
