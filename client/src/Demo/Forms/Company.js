import React from "react";
import { BrowserRouter as Router, Link } from "react-router-dom";
import {
  Row,
  Col,
  Card,
  Form,
  Button,
  InputGroup,
  FormControl,
  DropdownButton,
  Dropdown,
  Table,
} from "react-bootstrap";

import Aux from "../../hoc/_Aux";

import AddCompany from "./AddCompany";

class Company extends React.Component {
  render() {
    return (
      <div>
        <Card>
          <Card.Header></Card.Header>
          <Card.Body>
            <Button
              href="/forms/addcompany"
              variant="primary"
              style={{ width: 150, marginRight: 50 }}
            >
              Adaugă
            </Button>
            <Button
              href="/forms/editcompany"
              variant="primary"
              style={{ width: 150, marginRight: 50 }}
            >
              Modifică
            </Button>
            <Button
              href="/forms/removecompany"
              variant="primary"
              style={{ width: 150, marginRight: 50 }}
            >
              Șterge
            </Button>
            <Button
              href="/forms/importcompany"
              variant="primary"
              style={{ width: 150, marginRight: 50 }}
            >
              Import
            </Button>
            <Button
              href="/forms/exportcompany"
              variant="primary"
              style={{ width: 150, marginRight: 50 }}
            >
              Export
            </Button>
            <Table responsive hover>
              <thead>
                <tr>
                  <th>#</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Username</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th scope="row">1</th>
                  <td>Mark</td>
                  <td>Otto</td>
                  <td>@mdo</td>
                </tr>
                <tr>
                  <th scope="row">2</th>
                  <td>Jacob</td>
                  <td>Thornton</td>
                  <td>@fat</td>
                </tr>
                <tr>
                  <th scope="row">3</th>
                  <td>Larry</td>
                  <td>the Bird</td>
                  <td>@twitter</td>
                </tr>
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      </div>
    );
  }
}

export default Company;
