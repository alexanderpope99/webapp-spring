const getAngajatSel = () => {
	console.log('a');
  return JSON.parse(sessionStorage.getItem('angajatsel'));
}

// param must be object of type {id, nume}
const setAngajatSel = (angajat) => {
	if(angajat)
		sessionStorage.setItem('angajatsel', JSON.stringify(angajat));
	else sessionStorage.setItem('angajatsel', null);
}

export { getAngajatSel, setAngajatSel }