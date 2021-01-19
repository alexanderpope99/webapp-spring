import React from 'react';
import authService from './services/auth.service';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;

const isAngajatSimplu = authService.isAngajatSimplu();
const isAdmin = authService.isAdmin();
const isDirectorContabil = authService.isDirectorOrContabil();

const Societati = React.lazy(() => import('./Demo/Dashboard/Societati'));
const SocietatiView = React.lazy(() => import('./Demo/Dashboard/SocietatiView'));
const ParametriiSalarii = React.lazy(() => import('./Demo/Dashboard/ParametriiSalarii'));
const ParametriiSalariiView = React.lazy(() => import('./Demo/Dashboard/ParametriiSalariiView'));

const Notificari = React.lazy(() => import('./Demo/Other/Notificari'));
const Hidden = React.lazy(() => import('./Demo/Other/Hidden'));

// FORMS
const AddPersoana = React.lazy(() => import('./Demo/Forms/AddPersoana'));
const AddSocietate = React.lazy(() => import('./Demo/Forms/AddSocietate'));
const Angajat = React.lazy(() => import('./Demo/Forms/Angajat'));
const AngajatSimplu = React.lazy(() => import('./Demo/Forms/AngajatSimplu'));
const RealizariRetineri = React.lazy(() => import('./Demo/Forms/RealizariRetineri'));
const RealizariRetineriView = React.lazy(() => import('./Demo/Forms/RealizariRetineriView'));

// RAPOARTE
const Rapoarte = React.lazy(() => import('./Demo/Rapoarte/Rapoarte'));

// TABLES
const AngajatiTabel = React.lazy(() => import('./Demo/Tables/AngajatiTabel'));
const AngajatiTabelView = React.lazy(() => import('./Demo/Tables/AngajatiTabelView'));
const ConcediiOdihna = React.lazy(() => import('./Demo/Tables/ConcediiOdihna'));
const PersoaneTabel = React.lazy(() => import('./Demo/Tables/PersoaneTabel'));
const CereriConcediu = React.lazy(() => import('./Demo/Tables/CereriConcediu'));
const CereriConcediuSuperior = React.lazy(() => import('./Demo/Tables/CereriConcediuSuperior'));
const UserTabel = React.lazy(() => import('./Demo/Tables/UserTabel'));
const SarbatoriTabel = React.lazy(() => import('./Demo/Tables/SarbatoriTabel'));
const SarbatoriTabelView = React.lazy(() => import('./Demo/Tables/SarbatoriTabelView'));
const FacturiTabel = React.lazy(() => import('./Demo/Tables/FacturiTabel'));
const FacturiAprobatorTabel = React.lazy(() => import('./Demo/Tables/FacturiAprobatorTabel'));
const FacturiOperatorTabel = React.lazy(() => import('./Demo/Tables/FacturiOperatorTabel'));


// EDIT
const EditPersoana = React.lazy(() => import('./Demo/Edit/EditPersoana'));
const Profile = React.lazy(() => import('./Demo/Edit/Profile'));


const routes = [
	// LOGIN?

	// CARDS
	{
		path: '/dashboard/societati',
		exact: true,
		name: 'Societati',
		component: isAngajatSimplu ? SocietatiView : Societati,
	},
	{
		path: '/parametriisalarii',
		exact: true,
		name: 'Parametrii Salarii',
		component: isAngajatSimplu ? ParametriiSalariiView : ParametriiSalarii,
	},

	{
		path: '/notificari',
		exact: true,
		name: 'Notificari',
		component: Notificari,
	},

	// FORMS
	{ path: '/forms/add-persoana', exact: true, name: 'Adauga Persoana', component: isAngajatSimplu ? null : AddPersoana },
	{ path: '/forms/add-societate', exact: true, name: 'Adauga Societate', component: isAngajatSimplu ? null : AddSocietate },
	{
		path: '/forms/angajat',
		exact: true,
		name: 'Angajat',
		component: isAngajatSimplu ? AngajatSimplu : Angajat,
	},

	{
		path: '/forms/realizari-retineri',
		exact: true,
		name: 'Realizari / Retineri',
		component: isAngajatSimplu ? RealizariRetineriView : RealizariRetineri,
	},

	// RAPOARTE
	{ path: '/rapoarte', exact: true, name: 'Rapoarte', component: isDirectorContabil ? Rapoarte : null },

	{
		path: '/tables/user-tabel',
		exact: true,
		name: 'Tabel Useri',
		component: isAdmin ? UserTabel : null,
	},
	{ path: '/tables/persoane-tabel', exact: true, name: 'Tabel Persoane', component: PersoaneTabel },
	{
		path: '/tables/concedii-odihna',
		exact: true,
		name: 'Concedii Odihna',
		component: ConcediiOdihna,
	},
	{
		path: '/tables/cereri-concediu',
		exact: true,
		name: 'Cereri Concediu',
		component: CereriConcediu,
	},
	{
		path: '/tables/cereri-concediu-director',
		exact: true,
		name: 'Cereri Concediu',
		component: isDirectorContabil ? CereriConcediuSuperior : null,
	},
	{
		path: '/sarbatori',
		exact: true,
		name: 'Sărbători',
		component: isAngajatSimplu ? SarbatoriTabelView : SarbatoriTabel,
	},
	{
		path: '/facturi',
		exact: true,
		name: 'Tabel Facturi',
		component: FacturiTabel,
	},
	{
		path: '/facturi-aprobator',
		exact: true,
		name: 'Tabel Facturi Aprobator',
		component: FacturiAprobatorTabel,
	},
	{
		path: '/facturi-operator',
		exact: true,
		name: 'Tabel Facturi Operator',
		component: FacturiOperatorTabel,
	},
	{
		path: '/tables/angajati',
		exact: true,
		name: 'Tabel Angajați',
		component: isAngajatSimplu ? AngajatiTabelView : AngajatiTabel,
	},
	// EDIT
	{ path: '/edit/edit-persoana', exact: true, name: 'Editeaza Persoana', component: EditPersoana },
	{ path: '/edit/profile', exact: true, name: 'Profil', component: Profile },

	// TEST
	{ path: '/hidden', exact: true, name: 'Data buttons', component: isAngajatSimplu ? null : Hidden },
];

export default routes;
