import axios from 'axios';
import React from 'react';
import { Form, Button, Toast } from 'react-bootstrap';
import authHeader from '../../services/auth-header';
import { server } from './server-address';

class FileUpload extends React.Component {
  constructor() {
    super();
    this.handleUpload = this.handleUpload.bind(this);

    this.state = {
      file: null,
      showToast: false,
      toastMessage: '',
    };
  }

  async handleUpload() {
    const formData = new FormData();
    // Update the formData object
    formData.append('file', this.state.file);
    await axios
      .post(`${server.address}/upload`, formData, {
        headers: {
          ...authHeader(),
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((res) => console.log(res))
      .catch((err) =>
        this.setState({
          showToast: true,
          toastMessage: 'Nu am putut încărca fișierul ' + err.response.data.message,
        })
      );
  }

  render() {
    return (
      <React.Fragment>
        <Toast
          onClose={() => this.setState({ showToast: false })}
          show={this.state.showToast}
          delay={4000}
          autohide
          className="position-fixed"
          style={{ top: '10px', right: '5px', zIndex: '9999', background: 'red' }}
        >
          <Toast.Header className="pr-2">
            <strong className="mr-auto">Eroare</strong>
          </Toast.Header>
          <Toast.Body>{this.state.toastMessage}</Toast.Body>
        </Toast>
        <Form.Group>
          <Form.Control type="file" onChange={(e) => this.setState({ file: e.target.files[0] })} />
        </Form.Group>
        <Button onClick={this.handleUpload}>Upload</Button>
      </React.Fragment>
    );
  }
}

export default FileUpload;
