import AuthService from './services/auth.service';
const user = AuthService.getCurrentUser();

var items = [
  {
    id: 'navigation',
    title: 'Navigation',
    type: 'group',
    icon: 'icon-navigation',
    show:
      user.roles.includes('ROLE_ANGAJAT') ||
      user.roles.includes('ROLE_ADMIN') ||
      user.roles.includes('ROLE_DIRECTOR'),
    children: [
      // {
      //   id: 'dashboard',
      //   title: 'Dashboard',
      //   type: 'item',
      //   url: '/dashboard/default',
      //   icon: 'feather icon-home',
      // },
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
    ],
  },
  {
    id: 'cereri',
    title: 'Cereri',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'cereriConcediu',
        title: 'Cereri Concediu Director',
        type: 'item',
        url: '/tables/cereri-concediu-director',
        icon: 'feather icon-dollar-sign',
      },
    ],
  },
  {
    id: 'cereri',
    title: 'Cereri',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_ANGAJAT'),
    children: [
      {
        id: 'cereriConcediu',
        title: 'Cereri Concediu',
        type: 'item',
        url: '/tables/cereri-concediu',
        icon: 'feather icon-dollar-sign',
      },
    ],
  },
  {
    id: 'general',
    title: 'GENERAL',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_USER') || user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'angajat',
        title: 'Angajați',
        type: 'item',
        url: '/forms/angajat',
        icon: 'feather icon-users',
      },
      {
        id: 'realizari-retineri',
        title: 'Realizări / Rețineri',
        type: 'item',
        url: '/forms/realizari-retineri',
        icon: 'feather icon-clipboard',
      },
      // {
      //   id: 'add-persoana',
      //   title: 'Adauga Persoana',
      //   type: 'item',
      //   url: '/forms/add-persoana',
      //   icon: 'feather icon-file-text',
      // },
      {
        id: 'persoane-tabel',
        title: 'Tabel Persoane',
        type: 'item',
        url: '/tables/persoane-tabel',
        icon: 'feather icon-credit-card',
      },
      // {
      //   id: 'edit-persoana',
      //   title: 'Editeaza Persoana',
      //   type: 'item',
      //   url: '/edit/edit-persoana',
      //   icon: 'feather icon-file-text',
      // },
      // {
      //   id: 'angajati-tabel',
      //   title: 'Tabel Angajati',
      //   type: 'item',
      //   url: '/tables/angajati-tabel',
      //   icon: 'feather icon-file-text',
      // },
      // {
      //   id: 'add-societate',
      //   title: 'Adauga Societate',
      //   type: 'item',
      //   url: '/forms/add-societate',
      //   icon: 'feather icon-file-text',
      // },
      {
        id: 'societati-tabel',
        title: 'Tabel Socieăți',
        type: 'item',
        url: '/tables/societati-tabel',
        icon: 'feather icon-credit-card',
      },
      {
        id: 'cereri-concediu',
        title: 'Cereri Concediu',
        type: 'item',
        url: '/tables/cereri-concediu',
        icon: 'feather icon-briefcase',
      },
    ],
  },
  {
    id: 'rapoarte',
    title: 'Rapoarte',
    type: 'group',
    icon: 'icon-group',
    show: user.roles.includes('ROLE_USER') || user.roles.includes('ROLE_ADMIN') || user.roles.includes('ROLE_DIRECTOR'),
    children: [
      {
        id: 'stat',
        title: 'Ștate salarii',
        type: 'item',
        url: '/state-salarii',
        icon: 'feather icon-file-text',
			},
			{
        id: 'pontaj',
        title: 'Foi pontaj',
        type: 'item',
        url: '/pontaj',
        icon: 'feather icon-grid',
      },
    ],
  },
  // {
  //   id: 'configuration',
  //   title: 'Configuration',
  //   type: 'group',
  //   icon: 'icon-group',
  //   show: user.roles.includes('ROLE_ADMIN'),
  //   children: [
  //     {
  //       id: 'user-tabel',
  //       title: 'Tabel Useri',
  //       type: 'item',
  //       url: '/tables/user-tabel',
  //       icon: 'feather icon-users',
  //     },
  //     {
  //       id: 'role-tabel',
  //       title: 'Tabel Role',
  //       type: 'item',
  //       url: '/tables/role-tabel',
  //       icon: 'feather icon-file-text',
  //     },
  //     {
  //       id: 'permission-tabel',
  //       title: 'Tabel Permission',
  //       type: 'item',
  //       url: '/tables/permission-tabel',
  //       icon: 'feather icon-file-text',
  //     },
  //     {
  //       id: 'role-to-permission-tabel',
  //       title: 'Tabel Role-to-Permission',
  //       type: 'item',
  //       url: '/tables/role-to-permission-tabel',
  //       icon: 'feather icon-file-text',
  //     },
  //     {
  //       id: 'user-to-role-tabel',
  //       title: 'Tabel User-to-Role',
  //       type: 'item',
  //       url: '/tables/user-to-role-tabel',
  //       icon: 'feather icon-file-text',
  //     },
  //   ],
  // },
  // {
  //   id: 'ui-forms',
  //   title: 'Forms & Tables',
  //   type: 'group',
  //   icon: 'icon-group',
  //   show: user.roles.includes('ROLE_ADMIN'),
  //   children: [
  //     {
  //       id: 'form-basic',
  //       title: 'Form Elements',
  //       type: 'item',
  //       url: '/forms/form-basic',
  //       icon: 'feather icon-file-text',
  //     },
  //     {
  //       id: 'bootstrap',
  //       title: 'Table',
  //       type: 'item',
  //       icon: 'feather icon-server',
  //       url: '/tables/bootstrap',
  //     },
  //   ],
  // },
  // {
  //   id: 'chart-maps',
  //   title: 'Chart & Maps',
  //   type: 'group',
  //   icon: 'icon-charts',
  //   show: user.roles.includes('ROLE_ADMIN'),
  //   children: [
  //     {
  //       id: 'charts',
  //       title: 'Charts',
  //       type: 'item',
  //       icon: 'feather icon-pie-chart',
  //       url: '/charts/nvd3',
  //     },
  //     {
  //       id: 'maps',
  //       title: 'Map',
  //       type: 'item',
  //       icon: 'feather icon-map',
  //       url: '/maps/google-map',
  //     },
  //   ],
  // },
  // {
  //   id: 'pages',
  //   title: 'Pages',
  //   type: 'group',
  //   icon: 'icon-pages',
  //   show: user.roles.includes('ROLE_ADMIN'),
  //   children: [
  //     {
  //       id: 'auth',
  //       title: 'Authentication',
  //       type: 'collapse',
  //       icon: 'feather icon-lock',
  //       badge: {
  //         title: 'New',
  //         type: 'label-danger',
  //       },
  //       children: [
  //         {
  //           id: 'signup-1',
  //           title: 'Sign up',
  //           type: 'item',
  //           url: '/auth/signup-1',
  //           target: true,
  //           breadcrumbs: false,
  //         },
  //         {
  //           id: 'signin-1',
  //           title: 'Sign in',
  //           type: 'item',
  //           url: '/auth/signin-1',
  //           target: true,
  //           breadcrumbs: false,
  //         },
  //       ],
  //     },

  //     {
  //       id: 'sample-page',
  //       title: 'Sample Page',
  //       type: 'item',
  //       url: '/sample-page',
  //       classes: 'nav-item',
  //       icon: 'feather icon-sidebar',
  //     },
  //     {
  //       id: 'docs',
  //       title: 'Documentation',
  //       type: 'item',
  //       url: '/docs',
  //       classes: 'nav-item',
  //       icon: 'feather icon-help-circle',
  //     },
  //     {
  //       id: 'menu-level',
  //       title: 'Menu Levels',
  //       type: 'collapse',
  //       icon: 'feather icon-menu',
  //       children: [
  //         {
  //           id: 'menu-level-1.1',
  //           title: 'Menu Level 1.1',
  //           type: 'item',
  //           url: '#!',
  //         },
  //         {
  //           id: 'menu-level-1.2',
  //           title: 'Menu Level 2.2',
  //           type: 'collapse',
  //           children: [
  //             {
  //               id: 'menu-level-2.1',
  //               title: 'Menu Level 2.1',
  //               type: 'item',
  //               url: '#',
  //             },
  //             {
  //               id: 'menu-level-2.2',
  //               title: 'Menu Level 2.2',
  //               type: 'collapse',
  //               children: [
  //                 {
  //                   id: 'menu-level-3.1',
  //                   title: 'Menu Level 3.1',
  //                   type: 'item',
  //                   url: '#',
  //                 },
  //                 {
  //                   id: 'menu-level-3.2',
  //                   title: 'Menu Level 3.2',
  //                   type: 'item',
  //                   url: '#',
  //                 },
  //               ],
  //             },
  //           ],
  //         },
  //       ],
  //     },
  //     {
  //       id: 'disabled-menu',
  //       title: 'Disabled Menu',
  //       type: 'item',
  //       url: '#',
  //       classes: 'nav-item disabled',
  //       icon: 'feather icon-power',
  //     },
  //     /*{
  //                 id: 'buy-now',
  //                 title: 'Buy Now',
  //                 type: 'item',
  //                 icon: 'feather icon-user',
  //                 classes: 'nav-item',
  //                 url: 'https://codedthemes.com',
  //                 target: true,
  //                 external: true,
  //                 badge: {
  //                     title: 'v1.0',
  //                     type: 'label-primary'
  //                 }
  //             }*/
  //   ],
  // },
];

items = items.filter(function (value, index, arr) {
  return value.show === true;
});

export default { items };
