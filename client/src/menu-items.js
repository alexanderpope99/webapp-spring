import AuthService from './services/auth.service';
const user = AuthService.getCurrentUser();

var items = [
  // DASHBOARD
  {
    id: 'navigation',
    title: 'Meniu principal',
    type: 'group',
    icon: 'icon-navigation',
    show: true,
    children: [
      {
        id: 'societati',
        title: 'Societăți',
        type: 'item',
        url: '/dashboard/societati',
        icon: 'feather icon-home',
      },
      {
        id: 'parametriisalarii',
        title: 'Parametrii Salarii',
        type: 'item',
        url: '/parametriisalarii',
        icon: 'feather icon-dollar-sign',
      },
      {
        id: 'sarbatori',
        title: 'Sărbători legale',
        type: 'item',
        url: '/sarbatori',
        icon: 'feather icon-calendar',
      },
    ],
  },

  // CERERI CONCEDIU
  {
    id: 'general-angajat',
    title: 'GENERAL',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_ANGAJAT'),
    children: [
      {
        id: 'cereri-concediu-angajat',
        title: 'Cereri Concediu',
        type: 'item',
        url: '/tables/cereri-concediu',
        icon: 'feather icon-briefcase',
      },
    ],
  },

  // CERERI CONCEDIU + APROBARE
  {
    id: 'general',
    title: 'GENERAL',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_CONTABIL') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'cereri-concediu',
        title: 'Cereri Concediu',
        type: 'item',
        url: '/tables/cereri-concediu',
        icon: 'feather icon-briefcase',
      },
      {
        id: 'cereriConcediu',
        title: 'Aprobare Cereri Concediu',
        type: 'item',
        url: '/tables/cereri-concediu-director',
        icon: 'feather icon-briefcase',
      },
    ],
  },

  // DETALII PERSONALE
  {
    id: 'administrare-angajat',
    title: 'ADMINISTRARE',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_ANGAJAT'),
    children: [
      {
        id: 'angajati-tabel',
        title: 'Date personale',
        type: 'item',
        url: '/forms/angajat',
        icon: 'feather icon-users',
      },
    ],
  },

  // ANGAJATI + RAPOARTE
  {
    id: 'administrare',
    title: 'ADMINISTRARE',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_CONTABIL') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'angajati-tabel',
        title: 'Angajați',
        type: 'item',
        url: '/tables/angajati',
        icon: 'feather icon-users',
      },
      {
        id: 'rapoarte',
        title: 'Rapoarte',
        type: 'item',
        url: '/rapoarte',
        icon: 'feather icon-file-text',
      },
    ],
  },

  // FACTURI
  {
    id: 'facturi',
    title: 'Facturi',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_CONTABIL') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'facturi',
        title: 'Facturi',
        type: 'item',
        url: '/facturi',
        icon: 'feather icon-file-text',
      },
      {
        id: 'facturi-aprobator',
        title: 'Aprobare Facturi',
        type: 'item',
        url: '/facturi-aprobator',
        icon: 'feather icon-file-text',
      },
      {
        id: 'facturi-operator',
        title: 'Operare Facturi',
        type: 'item',
        url: '/facturi-operator',
        icon: 'feather icon-file-text',
      },
    ],
  },

  // UTILIZATORI
  {
    id: 'configurare',
    title: 'CONFIGURARE',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'user-tabel',
        title: 'Utilizatori',
        type: 'item',
        url: '/tables/user-tabel',
        icon: 'feather icon-users',
      },
    ],
  },
];

items = items.filter(function (value, index, arr) {
  return value.show === true;
});

// eslint-disable-next-line import/no-anonymous-default-export
export default { items };
