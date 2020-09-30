import React from 'react';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;

const DashboardDefault = React.lazy(() => import('./Demo/Dashboard/Default'));
const Societati = React.lazy(() => import('./Demo/Dashboard/Societati'));
const ParametriiSalarii = React.lazy(() => import('./Demo/Dashboard/ParametriiSalarii'));

const UIBasicButton = React.lazy(() => import('./Demo/UIElements/Basic/Button'));
const UIBasicBadges = React.lazy(() => import('./Demo/UIElements/Basic/Badges'));
const UIBasicBreadcrumbPagination = React.lazy(() =>
  import('./Demo/UIElements/Basic/BreadcrumbPagination')
);

const UIBasicCollapse = React.lazy(() => import('./Demo/UIElements/Basic/Collapse'));
const UIBasicTabsPills = React.lazy(() => import('./Demo/UIElements/Basic/TabsPills'));
const UIBasicBasicTypography = React.lazy(() => import('./Demo/UIElements/Basic/Typography'));

// FORMS
const FormsElements = React.lazy(() => import('./Demo/Forms/FormsElements'));
const AddSocietate = React.lazy(() => import('./Demo/Forms/AddSocietate'));
const AddPersoana = React.lazy(() => import('./Demo/Forms/AddPersoana'));
const Angajat = React.lazy(() => import('./Demo/Forms/Angajat'));
const RealizariRetineri = React.lazy(() => import('./Demo/Forms/RealizariRetineri'));

// DOWNLOADS
const Stat = React.lazy(() => import('./Demo/Rapoarte/Stat'));

// TABLES
const BootstrapTable = React.lazy(() => import('./Demo/Tables/BootstrapTable'));
const AngajatiTabel = React.lazy(() => import('./Demo/Tables/AngajatiTabel'));
const SocietatiTabel = React.lazy(() => import('./Demo/Tables/SocietatiTabel'));
const PersoaneTabel = React.lazy(() => import('./Demo/Tables/PersoaneTabel'));
const ConcediiOdihna = React.lazy(() => import('./Demo/Tables/ConcediiOdihna'));
const CereriConcediu = React.lazy(() => import('./Demo/Tables/CereriConcediu'));
const CereriConcediuSuperior = React.lazy(() => import('./Demo/Tables/CereriConcediuSuperior'));

// EDIT
const EditPersoana = React.lazy(() => import('./Demo/Edit/EditPersoana'));

const Nvd3Chart = React.lazy(() => import('./Demo/Charts/Nvd3Chart/index'));

const GoogleMap = React.lazy(() => import('./Demo/Maps/GoogleMap/index'));

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

  // FORMS
  { path: '/forms/form-basic', exact: true, name: 'Forms Elements', component: FormsElements },
  { path: '/forms/add-societate', exact: true, name: 'Adauga Societate', component: AddSocietate },
  { path: '/forms/add-persoana', exact: true, name: 'Adauga Persoana', component: AddPersoana },
  { path: '/forms/angajat', exact: true, name: 'Angajat', component: Angajat },
  {
    path: '/forms/realizari-retineri',
    exact: true,
    name: 'Realizari / Retineri',
    component: RealizariRetineri,
  },

  // DOWNLOADS
  { path: '/state-salarii', exact: true, name: 'Stat salarii', component: Stat },

  // TABLES
  { path: '/tables/bootstrap', exact: true, name: 'Bootstrap Table', component: BootstrapTable },
  { path: '/tables/angajati-tabel', exact: true, name: 'Tabel Angajati', component: AngajatiTabel },
  {
    path: '/tables/societati-tabel',
    exact: true,
    name: 'Tabel Societati',
    component: SocietatiTabel,
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

  // EDIT
  { path: '/edit/edit-persoana', exact: true, name: 'Editeaza Persoana', component: EditPersoana },

  { path: '/charts/nvd3', exact: true, name: 'Nvd3 Chart', component: Nvd3Chart },
  { path: '/maps/google-map', exact: true, name: 'Google Map', component: GoogleMap },
  { path: '/sample-page', exact: true, name: 'Sample Page', component: OtherSamplePage },
  { path: '/docs', exact: true, name: 'Documentation', component: OtherDocs },

  // TEST
  // { path: '/test/angajat', exact: true, name: 'Angajat Test', component: AngajatTest },
  // { path: '/test/add-persoana', exact: true, name: 'Add Persoana Test', component: AddPersoanaTest },
];

export default routes;
