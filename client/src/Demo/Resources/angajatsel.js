const getAngajatSel = () => {
  return JSON.parse(localStorage.getItem('angajatsel'));
}

// param must be object of type {id, nume}
const setAngajatSel = (angajat) => {
	if(angajat)
		localStorage.setItem('angajatsel', JSON.stringify(angajat));
	else localStorage.setItem('angajatsel', null);
}

export { getAngajatSel, setAngajatSel }