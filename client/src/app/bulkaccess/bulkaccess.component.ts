import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectAuthenticationState } from '../store/app.states';
import { User } from '../store/models/users';
import { BulkaccessService } from './bulkaccess.service';
@Component({
  selector: 'app-bulkaccess',
  templateUrl: './bulkaccess.component.html',
  styleUrls: ['./bulkaccess.component.css']
})
export class BulkaccessComponent implements OnInit {
  user: User = new User();
  getState!: Observable<any>;
  Username!: string;
  userid!: string;
  token: string = "";
  urlArray: any;

  constructor(
    private store:Store,
    private router: Router,
    private bulkaccessService:BulkaccessService
  ){
    this.getState = this.store.select(selectAuthenticationState);
  }

  ngOnInit(): void {
    this.getState.subscribe(state => {
      if (state) {
        this.user = state.user;
        this.Username = this.user.name;
        this.userid = this.user.userid;
        this.token = this.user.token;
      }
    });
  }

  generate(bulkurls:string){
    var bulks = bulkurls.split('\n');
    this.bulkaccessService.getBulkUrls(bulks,this.Username).subscribe(res => {
      this.urlArray = res.list;
    })
  }

}
