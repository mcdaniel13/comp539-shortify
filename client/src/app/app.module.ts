import { NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule} from "@angular/common/http";
import { NgbPaginationModule, NgbAlertModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { RouterState, StoreRouterConnectingModule } from '@ngrx/router-store';
import { AuthenticationEffects  } from './store/effects/auth.effects';
import { reducers } from './store/app.states';
import { AppRoutingModule } from './app-routing.module';
import { DataTablesModule } from "angular-datatables";
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { HeaderComponent } from './header/header.component';
import { AuthComponent } from './auth/auth.component';
import { UrlmanagementComponent } from './sys/urlmanagement/urlmanagement.component';
import { UsermanagementComponent } from './sys/usermanagement/usermanagement.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import { ProfileComponent } from './profile/profile.component';
import { BulkaccessComponent } from './bulkaccess/bulkaccess.component';
@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    HeaderComponent,
    AuthComponent,
    UrlmanagementComponent,
    UsermanagementComponent,
    ProfileComponent,
    BulkaccessComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbPaginationModule,
    NgbAlertModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    DataTablesModule,
    MatSidenavModule,
    StoreModule.forRoot(reducers, {}),
    EffectsModule.forRoot([AuthenticationEffects]),
    StoreRouterConnectingModule.forRoot(),
    StoreRouterConnectingModule.forRoot({
      stateKey: 'router',
      routerState: RouterState.Minimal
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
