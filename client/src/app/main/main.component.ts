import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { User } from '../store/models/users';
import { selectAuthenticationState } from '../store/app.states';
import { MainService } from './main.service';
// import {ShortifyService} from "../../service/helloworld.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})

export class MainComponent implements OnInit{
  ShortenUrl: any = '';
  getState!: Observable<any>;
  user: User = new User();
  errorMessage: string = '';
  name: string | undefined;
  userid!: string;
  token: string = "";
  history = [];
  
  constructor(
    private store:Store,
    private mainService:MainService
    // private api: ShortifyService,
  ){
    this.getState = this.store.select(selectAuthenticationState);
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

  generateShortUrl(longUrl: string) {
    this.mainService.requestShortUrl(longUrl, this.userid,this.token).subscribe(response => {
       this.ShortenUrl = 'http://35.194.4.193/' + response.data.shortUrl;
       var a = JSON.parse(localStorage.getItem("history")|| '[]');;
         a.push({
           longUrl : longUrl,
           shortUrl : this.ShortenUrl,
           date : new Date().toLocaleString("en-US")
         })
      localStorage.setItem('history', JSON.stringify(a));  
     });
  }
}
