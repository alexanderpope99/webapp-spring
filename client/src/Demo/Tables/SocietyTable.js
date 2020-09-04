import React from 'react';
// import ReactDOM from 'react-dom';
import MaterialTable from 'material-table';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core';

export const MatTable = function () {
  // const [selectedRow, setSelectedRow] = React.useState(null);
  const theme = createMuiTheme({
    palette: {
      primary: {
        main: '#4caf50',
      },
      secondary: {
        main: '#ff9100',
      },
    },
  });
  const [columns, setColumns] = React.useState([
    { title: 'Name', field: 'name' },
    {
      title: 'Surname',
      field: 'surname',
      initialEditValue: 'initial edit value',
    },
    { title: 'Birth Year', field: 'birthYear', type: 'numeric' },
    {
      title: 'Birth Place',
      field: 'birthCity',
      lookup: { 34: 'İstanbul', 63: 'Şanliurfa' },
    },
  ]);

  const [data, setData] = React.useState([
    { name: 'Mehmet', surname: 'Baran', birthYear: 1987, birthCity: 63 },
    { name: 'Zerya Betül', surname: 'Baran', birthYear: 2017, birthCity: 34 },
  ]);

  return (
    <MuiThemeProvider theme={theme}>
      <MaterialTable
        title="Basic Filtering Preview"
        columns={columns}
        data={data}
        editable={{
          onRowAdd: (newData) =>
            new Promise((resolve, reject) => {
              console.log('Sugi pl');
            }),
          onRowUpdate: (newData, oldData) =>
            new Promise((resolve, reject) => {
              setTimeout(() => {
                const dataUpdate = [...data];
                const index = oldData.tableData.id;
                dataUpdate[index] = newData;
                setData([...dataUpdate]);

                resolve();
              }, 1000);
            }),
          onRowDelete: (oldData) =>
            new Promise((resolve, reject) => {
              setTimeout(() => {
                const dataDelete = [...data];
                const index = oldData.tableData.id;
                dataDelete.splice(index, 1);
                setData([...dataDelete]);

                resolve();
              }, 1000);
            }),
        }}
        options={{
          filtering: true,
          selection: true,
        }}
      />
    </MuiThemeProvider>
  );
};
