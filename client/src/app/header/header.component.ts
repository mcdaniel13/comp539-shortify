import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { User } from '../store/models/users';
import { selectAuthenticationState } from '../store/app.states';
import { Logout } from '../store/actions/auth.action';
import { Clipboard } from '@angular/cdk/clipboard';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  user: User = new User();
  getState!: Observable<any>;
  errorMessage: string = '';
  name: string | undefined;
  userid!: string;
  token!: string;
  isAuth:boolean = true;
  showFiller = false;
  history:any = [];

  constructor(
    private store:Store,
    private router: Router,
    private clipboard: Clipboard
  ){ 
    this.getState = this.store.select(selectAuthenticationState);
  }

  public isMenuCollapsed = true;

  directPage(pageName:string){
    this.router.navigate([`${pageName}`]);
  };

  btnClick(url:string){
    window.open(url,'_blank');
  };

  copyText(textToCopy: string) {
    this.clipboard.copy(textToCopy);
  }

  clearhistory(){
    localStorage.removeItem('history');
    this.history = JSON.parse(localStorage.getItem("history")|| '[]');
  }
  
  gethistory(){
    this.history = JSON.parse(localStorage.getItem("history")|| '[]');
  }

  ngOnInit(): void {
    this.getState.subscribe((state) => {
      if (state) {
        this.user = state.user;
        this.name = this.user.name;
        this.userid = this.user.userid;
        this.token = this.user.token;
      }
    });
  }

  logout(){
    const payload ={
      userid : this.userid,
      token : this.token
    }
    this.store.dispatch(new Logout(payload));
  }
}
