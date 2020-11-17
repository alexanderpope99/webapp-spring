const cod_boala = [
  '01-Boala obisnuita si accidente in afara muncii',
  '02-Accident in timpul deplasarii la/de la locul de munca',
  '03-Accident de munca',
  '04-Boala profesionala',
  '05-Boala infectocontagioasa din grupa A',
  '06-Urgenta medico-chirurgicala',
  '07-Carantina',
  '08-Sarcina si lehuzie (maternitate)',
  '09-INgrijire copil bolnav in varsta de pana la 7 ani sau copil cu handicap, pentru afectiuni intercurente, pana la implinirea varstei de 18 ani 85%',
  '10-Reducerea duratei cu ¼ a duratei normale a timpului de lucru',
  '11-Trecerea temporara in alt loc de munca',
  '12-Tuberculoza',
  '13-Boala cardiovasculara',
  '14-Cancer, HIV, SIDA',
  '15-Risc maternal',
];

function getProcent(cod) {
  cod = cod.substring(0, 2);
  switch (cod) {
    case '01':
      return ['75'];
    case '02':
      return ['80', '100'];
    case '03':
      return ['80', '100'];
    case '04':
      return ['80', '100'];
    case '05':
      return ['100'];
    case '06':
      return ['80', '100'];
    case '07':
      return ['75'];
    case '08':
      return ['85'];
    case '09':
      return ['85'];
    case '10':
      return ['Reducerea duratei cu 1/4 a duratei normale a timpului de lucru'];
    case '11':
      return ['Trecerea temporara in alt loc de munca'];
    case '12':
      return ['100'];
    case '13':
      return ['75'];
    case '14':
      return ['100'];
    case '15':
      return ['75'];

    default:
      return ['100'];
	}
}

export { cod_boala, getProcent };
