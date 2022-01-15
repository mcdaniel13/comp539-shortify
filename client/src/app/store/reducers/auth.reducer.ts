import { User } from '../models/users';
import { AuthenticationActions, AuthenticationActionTypes } from '../actions/auth.action';

export interface State {
  isAuthenticated: boolean;
  user: User | null;
  errorMessage: string | null;
}

//set the initial state with sessionStorage
export const initialState: State = {
  isAuthenticated: sessionStorage.getItem('token') !== null,
  user: {
    userid:sessionStorage.getItem('userid') || '',
    token: sessionStorage.getItem('token') || '',
    name: sessionStorage.getItem('name') || ''
  },
  errorMessage: null
};

export function reducer(state = initialState, action: AuthenticationActions): State {
  switch (action.type) {
    case AuthenticationActionTypes.LOGIN_SUCCESS: {
      return {
        ...state,
        isAuthenticated: true,
        user: {
          userid:action.payload.userid,
          token: action.payload.token,
          name: action.payload.name
        },
        errorMessage: null
      };
    }

    case AuthenticationActionTypes.LOGIN_FAILURE: {
      return {
        ...state,
        errorMessage: 'The password you’ve entered is incorrect'
      };
    }

    case AuthenticationActionTypes.LOGOUT: {
      return initialState;
    }

    case AuthenticationActionTypes.SIGNUP_SUCCESS: {
      return {
        ...state,
        isAuthenticated: true,
        user: {
          userid:action.payload.userid,
          token: action.payload.token,
          name: action.payload.name
        },
        errorMessage: null
      };
    }
    case AuthenticationActionTypes.SIGNUP_FAILURE: {
      return {
        ...state,
        errorMessage: 'That email is already in use.'
      };
    }


    default: {
      return state;
    }

  }
}