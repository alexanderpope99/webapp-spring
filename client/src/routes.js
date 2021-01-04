import React from 'react';
import authService from './services/auth.service';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;

const isAngajatSimplu = authService.isAngajatSimplu();

const DashboardDefault = React.lazy(() => import('./Demo/Dashboard/Default'));
const Societati = React.lazy(() => import('./Demo/Dashboard/Societati'));
const ParametriiSalarii = React.lazy(() => import('./Demo/Dashboard/ParametriiSalarii'));
const Setari = React.lazy(() => import('./Demo/Edit/Setari'));

const UIBasicButton = React.lazy(() => import('./Demo/UIElements/Basic/Button'));
const UIBasicBadges = React.lazy(() => import('./Demo/UIElements/Basic/Badges'));
const UIBasicBreadcrumbPagination = React.lazy(() =>
  import('./Demo/UIElements/Basic/BreadcrumbPagination')
);
const Notificari = React.lazy(() => import('./Demo/Other/Notificari'));

const UIBasicCollapse = React.lazy(() => import('./Demo/UIElements/Basic/Collapse'));
const UIBasicTabsPills = React.lazy(() => import('./Demo/UIElements/Basic/TabsPills'));
const UIBasicBasicTypography = React.lazy(() => import('./Demo/UIElements/Basic/Typography'));

// FORMS
const FormsElements = React.lazy(() => import('./Demo/Forms/FormsElements'));
const AddSocietate = React.lazy(() => import('./Demo/Forms/AddSocietate'));
const AddPersoana = React.lazy(() => import('./Demo/Forms/AddPersoana'));
const Angajat = React.lazy(() => import('./Demo/Forms/Angajat'));
const AngajatSimplu = React.lazy(() => import('./Demo/Forms/AngajatSimplu'));
const RealizariRetineri = React.lazy(() => import('./Demo/Forms/RealizariRetineri'));
const RealizariRetineriView = React.lazy(() => import('./Demo/Forms/RealizariRetineriView'));

// RAPOARTE
const Rapoarte = React.lazy(() => import('./Demo/Rapoarte/Rapoarte'));
const Stat = React.lazy(() => import('./Demo/Rapoarte/Stat'));
const Pontaj = React.lazy(() => import('./Demo/Rapoarte/Pontaj'));
const Dec112 = React.lazy(() => import('./Demo/Rapoarte/Dec112'));
const PlatiSalariiMTA = React.lazy(() => import('./Demo/Rapoarte/PlatiSalariiMTA'));

// TABLES
const BootstrapTable = React.lazy(() => import('./Demo/Tables/BootstrapTable'));
const SocietatiTabel = React.lazy(() => import('./Demo/Tables/SocietatiTabel'));
const PersoaneTabel = React.lazy(() => import('./Demo/Tables/PersoaneTabel'));
const ConcediiOdihna = React.lazy(() => import('./Demo/Tables/ConcediiOdihna'));
const CereriConcediu = React.lazy(() => import('./Demo/Tables/CereriConcediu'));
const CereriConcediuSuperior = React.lazy(() => import('./Demo/Tables/CereriConcediuSuperior'));
const UserTabel = React.lazy(() => import('./Demo/Tables/UserTabel'));
const SarbatoriTabel = React.lazy(() => import('./Demo/Tables/SarbatoriTabel'));
const FacturiTabel = React.lazy(() => import('./Demo/Tables/FacturiTabel'));
const FacturiAprobatorTabel = React.lazy(() => import('./Demo/Tables/FacturiAprobatorTabel'));
const FacturiOperatorTabel = React.lazy(() => import('./Demo/Tables/FacturiOperatorTabel'));
const AngajatiTabel = React.lazy(() => import('./Demo/Tables/AngajatiTabel'));

// EDIT
const EditPersoana = React.lazy(() => import('./Demo/Edit/EditPersoana'));
const Profile = React.lazy(() => import('./Demo/Edit/Profile'));

const Nvd3Chart = React.lazy(() => import('./Demo/Charts/Nvd3Chart/index'));

const OtherSamplePage = React.lazy(() => import('./Demo/Other/SamplePage'));
const OtherDocs = React.lazy(() => import('./Demo/Other/Docs'));

const routes = [
  // LOGIN?

  // CARDS
  { path: '/dashboard/societati', exact: true, name: 'Societati', component: Societati },
  {
    path: '/parametriisalarii',
    exact: true,
    name: 'Parametrii Salarii',
    component: ParametriiSalarii,
  },
  {
    path: '/edit/setari',
    exact: true,
    name: 'Setari',
    component: Setari,
  },
  { path: '/dashboard/default', exact: true, name: 'Default', component: DashboardDefault },
  { path: '/basic/button', exact: true, name: 'Basic Button', component: UIBasicButton },
  { path: '/basic/badges', exact: true, name: 'Basic Badges', component: UIBasicBadges },
  {
    path: '/basic/breadcrumb-paging',
    exact: true,
    name: 'Basic Breadcrumb Pagination',
    component: UIBasicBreadcrumbPagination,
  },
  { path: '/basic/collapse', exact: true, name: 'Basic Collapse', component: UIBasicCollapse },
  {
    path: '/basic/tabs-pills',
    exact: true,
    name: 'Basic Tabs & Pills',
    component: UIBasicTabsPills,
  },
  {
    path: '/basic/typography',
    exact: true,
    name: 'Basic Typography',
    component: UIBasicBasicTypography,
  },
  {
    path: '/notificari',
    exact: true,
    name: 'Notificari',
    component: Notificari,
  },

  // FORMS
  { path: '/forms/form-basic', exact: true, name: 'Forms Elements', component: FormsElements },
  { path: '/forms/add-societate', exact: true, name: 'Adauga Societate', component: AddSocietate },
  { path: '/forms/add-persoana', exact: true, name: 'Adauga Persoana', component: AddPersoana },
	{ path: '/forms/angajat', exact: true, name: 'Angajat', component: isAngajatSimplu ? AngajatSimplu : Angajat },

  {
    path: '/forms/realizari-retineri',
    exact: true,
    name: 'Realizari / Retineri',
    component: isAngajatSimplu ? RealizariRetineriView : RealizariRetineri,
  },

  // RAPOARTE
  { path: '/rapoarte', exact: true, name: 'Rapoarte', component: Rapoarte },
  { path: '/state-salarii', exact: true, name: 'Stat salarii', component: Stat },
  { path: '/pontaj', exact: true, name: 'Foaie pontaj', component: Pontaj },
  { path: '/dec112', exact: true, name: 'Declarația 112', component: Dec112 },
  { path: '/mta', exact: true, name: 'Plăți Salarii Mta', component: PlatiSalariiMTA },

  // TABLES
  { path: '/tables/bootstrap', exact: true, name: 'Bootstrap Table', component: BootstrapTable },
  {
    path: '/tables/societati-tabel',
    exact: true,
    name: 'Tabel Societati',
    component: SocietatiTabel,
  },
  {
    path: '/tables/user-tabel',
    exact: true,
    name: 'Tabel Useri',
    component: UserTabel,
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
    component: CereriConcediuSuperior,
  },
  {
    path: '/sarbatori',
    exact: true,
    name: 'Sărbători',
    component: SarbatoriTabel,
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
    component: AngajatiTabel,
  },
  // EDIT
  { path: '/edit/edit-persoana', exact: true, name: 'Editeaza Persoana', component: EditPersoana },
  { path: '/edit/profile', exact: true, name: 'Profil', component: Profile },

  { path: '/charts/nvd3', exact: true, name: 'Nvd3 Chart', component: Nvd3Chart },
  { path: '/sample-page', exact: true, name: 'Sample Page', component: OtherSamplePage },
  { path: '/docs', exact: true, name: 'Documentation', component: OtherDocs },

  // TEST
  // { path: '/test/angajat', exact: true, name: 'Angajat Test', component: AngajatTest },
  // { path: '/test/add-persoana', exact: true, name: 'Add Persoana Test', component: AddPersoanaTest },
];

export default routes;
