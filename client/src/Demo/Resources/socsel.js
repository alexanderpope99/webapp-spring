const getSocSel = () => {
  return JSON.parse(sessionStorage.getItem('socsel'));
}

// param must be object of type {id, nume}
const setSocSel = (soc) => {
  sessionStorage.setItem('socsel', JSON.stringify(soc));
}

export { getSocSel, setSocSel }