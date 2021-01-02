import React from 'react';
import { Row, Col, Card, Table, Button, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { X, Check, RotateCw } from 'react-feather';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class CereriConcediuSuperiorTabel extends React.Component {
  constructor(props) {
    super();

    this.onRefresh = this.onRefresh.bind(this);
    this.approveCerereConcediu = this.approveCerereConcediu.bind(this);
    this.rejectCerereConcediu = this.rejectCerereConcediu.bind(this);

    this.state = {
      socsel: getSocSel(),
      cereriConcediu: [],
      cereriConcediuComponent: null,
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';

    this.onRefresh();
    window.scrollTo(0, 0);
  }

  async approveCerereConcediu(cer) {
    axios
      .put(`${server.address}/cerericoncediu/statusappr/${cer.id}`, {}, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  async rejectCerereConcediu(cer) {
    axios
      .put(`${server.address}/cerericoncediu/statusrej/${cer.id}`, {}, { headers: authHeader() })
      .then((response) => response.data)
      .then(this.onRefresh)
      .catch((err) => console.error(err));
  }

  // function to create react component with fetched data
  renderCereri() {
    this.setState({
      cereriConcediuComponent: this.state.cereriConcediu.map((cer, index) => {
        return (
          // TODO
          <tr key={cer.id}>
            <th>
              {cer.status === 'Propus' || cer.status === 'Propus (Modificat)' ? (
                <i className="fa fa-circle text-c-gray f-10 mr-2" />
              ) : cer.status === 'Aprobat' ? (
                <i className="fa fa-circle text-c-green f-10 mr-2" />
              ) : (
                <i className="fa fa-circle text-c-red f-10 mr-2" />
              )}
              {cer.status}
            </th>
            <th>{cer.nume || '-'}</th>
            <th>{cer.dela || '-'}</th>
            <th>{cer.panala}</th>
            <th>{cer.tip}</th>
            <th>{cer.motiv}</th>
            <th>
              {cer.status === 'Propus' || cer.status === 'Propus (Modificat)' ? (
                <Row>
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Acceptă
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.approveCerereConcediu(cer)}
                      variant="outline-success"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <Check size={20} />
                    </Button>
                  </OverlayTrigger>

                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Respinge
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.rejectCerereConcediu(cer)}
                      variant="outline-danger"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <X size={20} />
                    </Button>
                  </OverlayTrigger>
                </Row>
              ) : cer.status === 'Aprobat' ? (
                <Row>
                  <Button
                    disabled
                    onClick={() => this.approveCerereConcediu(cer)}
                    variant="outline-success"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <Check size={20} />
                  </Button>
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Respinge
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.rejectCerereConcediu(cer)}
                      variant="outline-danger"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <X size={20} />
                    </Button>
                  </OverlayTrigger>
                </Row>
              ) : (
                <Row>
                  <OverlayTrigger
                    placement="bottom"
                    delay={{ show: 250, hide: 250 }}
                    overlay={
                      <Tooltip id="update-button" style={{ opacity: '.4' }}>
                        Acceptă
                      </Tooltip>
                    }
                  >
                    <Button
                      onClick={() => this.approveCerereConcediu(cer)}
                      variant="outline-success"
                      className="m-1 p-1 rounded-circle border-0"
                    >
                      <Check size={20} />
                    </Button>
                  </OverlayTrigger>
                  <Button
                    disabled
                    onClick={() => this.rejectCerereConcediu(cer)}
                    variant="outline-danger"
                    className="m-1 p-1 rounded-circle border-0"
                  >
                    <X size={20} />
                  </Button>
                </Row>
              )}
            </th>
          </tr>
        );
      }),
    });
  }

  async onRefresh() {
    let cereriConcediu = await axios
      .get(`${server.address}/cerericoncediu/soc/${this.state.socsel.id}`, {
        headers: authHeader(),
      })
      .then((res) => res.data)
      .catch((err) => console.error(err));

    if (!cereriConcediu) return;

    else {
      this.setState(
        {
          cereriConcediu: cereriConcediu,
        },
        this.renderCereri
      );
    }
  }

  async handleClose() {
    this.setState({
      show: false,
      id: null,
      pentru: '',
      dela: '',
      panala: '',
      tip: '',
      motiv: '',
    });
  }

  handleCloseConfirm() {
    this.setState({
      modalMessage: '',
      showConfirm: false,
    });
  }

  render() {
    return (
      <Aux>
        {/* VIEW MODAL */}
        <Row>
          <Col>
            <Card>
              <Card.Header className="border-0">
                <Card.Title as="h5">Aprobare Cereri Concediu</Card.Title>

                <Button
                  variant="outline-info"
                  size="sm"
                  style={{ fontSize: '1.25rem', float: 'right' }}
                  onClick={this.onRefresh}
                >
                  <RotateCw size="25" />
                  {/* ↺ */}
                </Button>
              </Card.Header>
              <Card.Body>
                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>Status</th>
                      <th>Pentru</th>
                      <th>De la</th>
                      <th>Până la</th>
                      <th>Tip</th>
                      <th>Motiv</th>
                      <th>Acțiuni</th>
                    </tr>
                  </thead>
                  <tbody>{this.state.cereriConcediuComponent}</tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default CereriConcediuSuperiorTabel;
