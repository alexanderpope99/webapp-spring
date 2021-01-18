const luni = [
  'Ianuarie',
  'Februarie',
  'Martie',
  'Aprilie',
  'Mai',
  'Iunie',
  'Iulie',
  'August',
  'Septembrie',
  'Octombrie',
  'Noiembrie',
  'Decembrie',
];

const zileSaptamana = [
	'Duminică',
	'Luni',
	'Marți',
	'Miercuri',
	'Joi',
	'Vineri',
	'Sâmbătă',
];

function formatDate(date) {
	if(!date || date === '-') return '-';
	
	let data = new Date(date.substring(0, 10));
	let ziuaSaptamanii = zileSaptamana[data.getDay()];

	let luna = date.substring(5, 7);
	let ziua = date.substring(8, 10).match('[1-9][0-9]*');

	luna = luni[Number(luna) - 1];

	return `${ziua} ${luna}, ${ziuaSaptamanii}`;
}

export {luni, zileSaptamana, formatDate};
