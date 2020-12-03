import authHeader from '../../services/auth-header';
import { server } from './server-address';

async function download(fileName, userid) {
  // const token = this.state.user.accessToken;
  console.log('trying to download...');

  const blob = await fetch(`${server.address}/download/${userid}/${fileName}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/octet-stream',
      ...authHeader(),
    },
  })
    .then((res) => (res.ok ? res.blob() : null))
		.catch((err) => console.error(err));
		
  if (blob) {
    var url = window.URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a); 
    a.click();
    a.remove();
    console.log('downloaded');
  }
}

async function downloadFactura(fileName, idfactura) {
  // const token = this.state.user.accessToken;
  console.log('trying to download...');

  const blob = await fetch(`${server.address}/factura/file/${idfactura}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/octet-stream',
      ...authHeader(),
    },
  })
    .then((res) => (res.ok ? res.blob() : null))
		.catch((err) => console.error(err));
		
  if (blob) {
    var url = window.URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    document.body.appendChild(a); 
    a.click();
    a.remove();
    console.log('downloaded');
  }
}

export { download, downloadFactura };
