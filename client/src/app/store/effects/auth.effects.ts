import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import { map, switchMap, catchError, tap } from 'rxjs/operators';
import {AuthenticationActionTypes, Login, LoginSuccess, LoginFailure, Logout, Signup, SignupSuccess, SignupFailure
}  from '../actions/auth.action';
import { AuthService } from '../../auth/auth.service';

@Injectable()
export class AuthenticationEffects {

  constructor(
    private actions: Actions,
    private authService: AuthService,
    private router: Router,
  ) { }

  @Effect()
  Login: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.LOGIN),
    map((action: Login) => action.payload),
    switchMap(payload => {
      return this.authService.login(payload.userid, payload.password)
        .pipe(
          map((users) => {
            const userJSON = JSON.parse(JSON.stringify(users));
            if (userJSON.success) {
              return new LoginSuccess({ token: userJSON.data.token, name: userJSON.data.name, userid:userJSON.data.userId});
            }
            return new LoginFailure({ error: 'Invalid username or password' });
          }),
          catchError((error) => {
            return of(new LoginFailure({ error: error }));
          }));
    }));


  @Effect({ dispatch: false })
  LoginSuccess: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.LOGIN_SUCCESS),
    tap((user: any) => {
      sessionStorage.setItem('token', user.payload.token);
      sessionStorage.setItem('name', user.payload.name);
      sessionStorage.setItem('userid', user.payload.userid);
      this.router.navigateByUrl('/');
    })
  );

  @Effect({ dispatch: false })
  LoginFailure: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.LOGIN_FAILURE)
  );

  @Effect({ dispatch: false })
  public Logout: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.LOGOUT),
    tap((user) => {
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('name');
      sessionStorage.removeItem('userid');
      // window.location.reload();
    })
  );

  @Effect()
  Signup: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.SIGNUP),
    map((action: Signup) => action.payload),
    switchMap(payload => {
      return this.authService.signup(payload.name, payload.userid, payload.password)
        .pipe(
          map((user) => {
            const userJSON = JSON.parse(JSON.stringify(user));
            console.log(userJSON)
            if (userJSON.success) {
              return new SignupSuccess({token: userJSON.data.password, userid: userJSON.data.userId, name:userJSON.data.name});
            }
            return of(new SignupFailure({ error: "Sign up error" }));
          }),
          catchError((error) => {
            return of(new SignupFailure({ error: error }));
          }));
    })
  );

  @Effect({ dispatch: false })
  SignupSuccess: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.SIGNUP_SUCCESS),
    tap((user:any) => {
      console.log(user)
      sessionStorage.setItem('token', user.payload.token);
      sessionStorage.setItem('name', user.payload.name);
      sessionStorage.setItem('userid', user.payload.userid);
      this.router.navigateByUrl('/');
    })
  );

  @Effect({ dispatch: false })
  SignupFailure: Observable<any> = this.actions.pipe(
    ofType(AuthenticationActionTypes.SIGNUP_FAILURE)
  );
}