import React, { Component, Suspense } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import Loadable from 'react-loadable';

import '../../node_modules/font-awesome/scss/font-awesome.scss';

import Loader from './layout/Loader';
import Aux from '../hoc/_Aux';
import ScrollToTop from './layout/ScrollToTop';
import routes from '../route';
import authService from '../services/auth.service';

import Societati from '../Demo/Dashboard/Societati';

const AdminLayout = Loadable({
  loader: () => import('./layout/AdminLayout'),
  loading: Loader,
});

class App extends Component {

  constructor() {
    super();

    this.state = {
      user: null,
    }
  }

  componentDidMount() {
    this.init();
  }

  async init() {
    const user = await authService.getCurrentUser();
    if(user)
      this.setState({ user: user });
    // else window.location.href = "/auth/signin-1"
  }

  render() {
    // singin-1 and singup-1 pages
    const login = routes.map((route, index) => {
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
        <ScrollToTop>
          <Suspense fallback={<Loader />}>
            <Switch>
              {login}
              {/* if user is missing, redirect */}
              {/* {this.state.user ? <Redirect to="/dashboard/societati" /> : <Redirect to="/auth/signin-1" />} */}
              <Route path="/" component={AdminLayout} />
              {/* <Societati /> */}
            </Switch>
          </Suspense>
        </ScrollToTop>
      </Aux>
    );
  }
}

export default App;
