import React from 'react';
import authService from './services/auth.service';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;

const isAngajatSimplu = authService.isAngajatSimplu();
const isDirectorContabil = authService.isDirectorOrContabil();

// MENIU PRINCIPAL
const Societati = React.lazy(() => import('./Demo/MeniuPrincipal/Societati/Societati'));
const Societate = React.lazy(() => import('./Demo/MeniuPrincipal/Societati/Societate'));
const SocietatiView = React.lazy(() => import('./Demo/MeniuPrincipal/Societati/SocietatiView'));
const ParametriiSalarii = React.lazy(() => import('./Demo/MeniuPrincipal/ParametriiSalarii/ParametriiSalarii'));
const ParametriiSalariiView = React.lazy(() => import('./Demo/MeniuPrincipal/ParametriiSalarii/ParametriiSalariiView'));
const SarbatoriTabel = React.lazy(() => import('./Demo/MeniuPrincipal/SarbatoriLegale/SarbatoriTabel'));
const SarbatoriTabelView = React.lazy(() => import('./Demo/MeniuPrincipal/SarbatoriLegale/SarbatoriTabelView'));

// CERERI CONCEDIU
const CereriConcediu = React.lazy(() => import('./Demo/CereriConcediu/CereriConcediu'));
const CereriConcediuSuperior = React.lazy(() => import('./Demo/CereriConcediu/CereriConcediuSuperior'));

// ADMINISTRARE
// 		ANGAJATI
const AngajatiTabel = React.lazy(() => import('./Demo/Administrare/Angajati/AngajatiTabelOld'));
const AngajatiTabelView = React.lazy(() => import('./Demo/Administrare/Angajati/AngajatiTabelView'));
// 			DETALII ANGAJAT
const Angajat = React.lazy(() => import('./Demo/Administrare/Angajati/Angajat/Angajat'));
const AngajatSimplu = React.lazy(() => import('./Demo/Administrare/Angajati/Angajat/AngajatSimplu'));
const AddPersoana = React.lazy(() => import('./Demo/Administrare/Angajati/Angajat/Persoana/AddPersoana'));
const RealizariRetineri = React.lazy(() => import('./Demo/Administrare/Angajati/RealizariRetineri/RealizariRetineri'));
const RealizariRetineriView = React.lazy(() => import('./Demo/Administrare/Angajati/RealizariRetineri/RealizariRetineriView'));
// 		RAPOARTE
const Rapoarte = React.lazy(() => import('./Demo/Rapoarte/Rapoarte'));
// 	CONCEDII
const ConcediiOdihna = React.lazy(() => import('./Demo/Administrare/Concedii/ConcediiOdihna/ConcediiOdihna'));

// FACTURARE
const ClientiTabel = React.lazy(() => import('./Demo/Facturare/Clienti/ClientiTabel'));
const Facturi = React.lazy(() => import('./Demo/Facturare/Facturi/Facturi'));
const FacturiAprobatorTabel = React.lazy(() => import('./Demo/Facturare/AprobareFacturi/FacturiAprobatorTabel'));
const FacturiOperatorTabel = React.lazy(() => import('./Demo/Facturare/OperareFacturi/FacturiOperatorTabel'));
const ActivitatiProiecte = React.lazy(() => import('./Demo/Facturare/Activitati/ActivitatiProiecte'));
const Caiete = React.lazy(() => import('./Demo/Facturare/Caiete/CaieteTabel'));
// const ActivitatiTabel = React.lazy(() => import('./Demo/Facturare/Activitati/ActivitatiTabel'));
// const ProiecteTabel = React.lazy(() => import('./Demo/Facturare/Activitati/ProiecteTabel'));

// CONFIGURARE
const UserTabel = React.lazy(() => import('./Demo/Configurare/Utilizatori/UserTabel'));
const Profile = React.lazy(() => import('./Demo/Configurare/Profil/Profile'));

// OTHER
const Notificari = React.lazy(() => import('./Demo/Other/Notificari'));
const Hidden = React.lazy(() => import('./Demo/Other/Hidden'));

const routes = [
  // CARDS
  {
    path: '/dashboard/societati',
    exact: true,
    name: 'Societati',
    component: isAngajatSimplu ? SocietatiView : Societati,
  },
  {
    path: '/taxeimpozite',
    exact: true,
    name: 'Taxe Impozite',
    component: isAngajatSimplu ? ParametriiSalariiView : ParametriiSalarii,
  },

  {
    path: '/notificari',
    exact: true,
    name: 'Notificari',
    component: Notificari,
  },

  // FORMS
  {
    path: '/forms/add-persoana',
    exact: true,
    name: 'Adauga Persoana',
    component: isAngajatSimplu ? null : AddPersoana,
  },
  {
    path: '/forms/add-societate',
    exact: true,
    name: 'Adauga Societate',
    component: isAngajatSimplu ? null : Societate,
  },
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

	// CALENDAR
	{
		path: '/calendar',
		exact: true,
		name: 'Calendar',
		component: isAngajatSimplu ? null : ConcediiOdihna,
	},

  // RAPOARTE
  {
    path: '/rapoarte',
    exact: true,
    name: 'Rapoarte',
    component: isDirectorContabil ? Rapoarte : null,
  },

  {
    path: '/tables/user-tabel',
    exact: true,
    name: 'Tabel Useri',
    component: isAngajatSimplu ? null : UserTabel,
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
    path: '/clienti',
    exact: true,
    name: 'Tabel Clienți',
    component: ClientiTabel,
  },
  {
    path: '/facturi',
    exact: true,
    name: 'Tabel Facturi',
    component: Facturi,
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
		path: '/activitati-proiecte',
		exact: true,
		name: 'Activitati, Proiecte',
		component: ActivitatiProiecte,
	},
	{
		path: '/caiete',
		exact: true,
		name: 'Caiete',
		component: Caiete,
	},
	// {
	// 	path: '/activitati',
	// 	exact: true,
	// 	name: 'Tabel Activitati',
	// 	component: ActivitatiTabel,
	// },
	// {
	// 	path: '/proiecte',
	// 	exact: true,
	// 	name: 'Tabel Proiecte',
	// 	component: ProiecteTabel,
	// },
  {
    path: '/tables/angajati',
    exact: true,
    name: 'Tabel Angajați',
    component: isAngajatSimplu ? AngajatiTabelView : AngajatiTabel,
  },
  // EDIT
  { path: '/edit/profile', exact: true, name: 'Profil', component: Profile },

  // TEST
  {
    path: '/hidden',
    exact: true,
    name: 'Data buttons',
    component: isAngajatSimplu ? null : Hidden,
  },
];

export default routes;
