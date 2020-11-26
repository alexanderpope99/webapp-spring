import axios from 'axios';
import React from 'react';
import { Form, Button } from 'react-bootstrap';
import authHeader from '../../services/auth-header';
import { server } from './server-address';

class FileUpload extends React.Component {
  constructor() {
    super();
    this.handleUpload = this.handleUpload.bind(this);

    this.state = {
      file: null,
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
			.then(res => console.log(res))
      .catch((err) => console.error(err));
  }

  render() {
    return (
      <React.Fragment>
        <Form.Group>
          <Form.Control type="file" onChange={(e) => this.setState({ file: e.target.files[0] })} />
        </Form.Group>
        <Button onClick={this.handleUpload}>Upload</Button>
      </React.Fragment>
    );
  }
}

export default FileUpload;
