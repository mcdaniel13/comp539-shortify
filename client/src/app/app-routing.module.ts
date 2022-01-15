import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main/main.component';
import { AuthComponent } from './auth/auth.component';
import { UsermanagementComponent } from './sys/usermanagement/usermanagement.component';
import { UrlmanagementComponent } from './sys/urlmanagement/urlmanagement.component';
import { AuthGaurdService } from './auth/auth-gaurd.service';
import { ProfileComponent } from './profile/profile.component';
import { BulkaccessComponent } from './bulkaccess/bulkaccess.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: AuthComponent },
  {
    path: 'userManage',
    component: UsermanagementComponent,
    canActivate: [AuthGaurdService]
  },
  {
    path: 'urlManage',
    component: UrlmanagementComponent,
    canActivate: [AuthGaurdService]
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGaurdService]
  },
  {
    path: 'bulkaccess',
    component: BulkaccessComponent,
    canActivate: [AuthGaurdService]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
