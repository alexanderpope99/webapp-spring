import React from 'react';
import { Row, Col } from 'react-bootstrap';
import MaterialTable from 'material-table';

import Aux from '../../hoc/_Aux';
import { server } from '../Resources/server-address';
import { getSocSel } from '../Resources/socsel';
import axios from 'axios';
import authHeader from '../../services/auth-header';

class AngatjatiTabel extends React.Component {
  constructor() {
    super();
    this.state = {
      columns: [
        { title: '#id', field: 'id' },
        { title: 'Nume', field: 'nume' },
        { title: 'Prenume', field: 'prenume' },
        { title: 'e-mail', field: 'email' },
        { title: 'Nr. Telefon', field: 'telefon' },
      ],
      data: [], // for display only
      persoane: this.getData(),
    };
  }

  componentDidMount() {
    if (!getSocSel()) window.location.href = '/dashboard/societati';
  }

  async deletePersoana(state) {
    await axios.delete(`${server.address}/persoana/${state.id}`, { headers: authHeader() });
  }

  async getData() {
    const persoane = await axios
      .get(`${server.address}/persoana`, { headers: authHeader() })
      .then((persoane) => persoane.data);

    //for loop to keep only the display-able data in state.data
    for (let pers in persoane) {
      let { gen, idactidentitate, idadresa, starecivila, cnp, idsocietate, ...newpers } = persoane[
        pers
      ];
      persoane[pers] = newpers;
    }

    this.setState({
      data: persoane,
    });
    // return persoane;
  }

  render() {
    return (
      <Aux>
        <Row>
          <Col>
            {/* <Card>
              <Card.Header>
                <Card.Title as="h5">Listă angajați</Card.Title>
              </Card.Header>
              <Card.Body> */}
            <MaterialTable
              title="Angajați"
              columns={this.state.columns}
              data={this.state.data}
              options={{
                actionsColumnIndex: -1,
              }}
              actions={[
                {
                  icon: 'add',
                  tooltip: 'Adaugă Persoană',
                  isFreeAction: true,
                  onClick: (e) => (window.location.href = '/forms/add-persoana'),
                },
                {
                  icon: 'refresh',
                  tooltip: 'Refresh',
                  isFreeAction: true,
                  onClick: () => this.getData(),
                },
                {
                  icon: 'edit',
                  tooltip: 'Editează',
                  onClick: (e, rowData) =>
                    (window.location.href = `/edit/edit-persoana?id=${rowData.id}`),
                },
              ]}
              localization={{
                toolbar: {
                  nRowsSelected: '{0} rănduri selectate',
                },
                header: {
                  actions: 'Acțiuni',
                },
                body: {
                  deleteTooltip: 'Șterge',
                  emptyDataSourceMessage: 'Nimic de afișat',
                  filterRow: {
                    filterTooltip: 'Filtrează',
                  },
                  editRow: {
                    deleteText: 'Sigur ștergeti această persoană?',
                    cancelTooltip: 'Anulează',
                    saveTooltip: 'Salvează',
                  },
                },
              }}
              editable={{
                // onRowUpdate: (newData, oldData) =>
                //   new Promise((resolve) => {
                //     resolve();
                //     if (oldData) {
                //       this.setState((prevState) => {
                //         const data = [...prevState.data];
                //         data[data.indexOf(oldData)] = newData;
                //         console.log(data);
                //         // this.getData();
                //         return { ...prevState, data };
                //       });
                //     }
                //   }),
                onRowDelete: (oldData) =>
                  new Promise((resolve) => {
                    resolve();
                    this.setState((prevState) => {
                      // extract data from state
                      const data = [...prevState.data];
                      // delete oldData <- clicked row
                      data.splice(data.indexOf(oldData), 1);
                      this.deletePersoana(oldData);

                      return { ...prevState, data };
                    });
                  }),
              }}
            />
            {/* </Card.Body>
            </Card> */}
          </Col>
        </Row>
      </Aux>
    );
  }
}

export default AngatjatiTabel;
