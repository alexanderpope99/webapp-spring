import React from 'react';
import { Button } from 'react-bootstrap';

class Stat extends React.Component {
	render() {
		return (
			<React.Fragment>
				<Button onClicl={() => console.log('aaa')}>cicke me</Button>
			</React.Fragment>
		)
	}
}

export default Stat;