import React, { Component, Suspense } from 'react';
import { Switch, Route } from 'react-router-dom';
import Loadable from 'react-loadable';

import '../../node_modules/font-awesome/scss/font-awesome.scss';

import Loader from './layout/Loader';
import Aux from '../hoc/_Aux';
import ScrollToTop from './layout/ScrollToTop';
import routes from '../route';
import SocietateContext from '../Demo/Context/SocietateContext';

const AdminLayout = Loadable({
  loader: () => import('./layout/AdminLayout'),
  loading: Loader,
});

class App extends Component {
  constructor() {
    super();
    this.state = {
      societate_selectata: {nume: '', id: 0},
      changeSelected: (newSelect) => {
        this.setState(
          { societate_selectata: newSelect },
        );
      },
    };
  }

  render() {
    const menu = routes.map((route, index) => {
      return route.component ? (
        <Route
          key={index}
          path={route.path}
          exact={route.exact}
          name={route.name}
          render={(props) => <route.component {...props} />}
        />
      ) : null;
    });

    return (
      <Aux>
        <SocietateContext.Provider value={this.state}>
          <ScrollToTop>
            <Suspense fallback={<Loader />}>
              <Switch>
                {menu}
                <Route path="/" component={AdminLayout} />
              </Switch>
            </Suspense>
          </ScrollToTop>
        </SocietateContext.Provider>
      </Aux>
    );
  }
}

export default App;
