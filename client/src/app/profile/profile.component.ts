import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { selectAuthenticationState } from '../store/app.states';
import { User } from '../store/models/users';
import { ProfileService } from './profile.service';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User = new User();
  getState!: Observable<any>;
  Username: string | undefined;
  isUpdateSubmitted: boolean = false;
  userid!: string;
  token!: string;
  active = 'top';
  profileForm: FormGroup;
  
  constructor(
    private store: Store, 
    private router: Router,
    private profileService:ProfileService
    ) {
    this.getState = this.store.select(selectAuthenticationState);
    this.profileForm = new FormGroup({
      userid: new FormControl({value:'',disabled: true},[Validators.required]),
      name: new FormControl('', [Validators.required]),
      pwd: new FormControl('', [Validators.required]),
      repeatpwd: new FormControl('', [Validators.required])
    });
  }

  ngOnInit(): void {
    this.getState.subscribe(state => {
      if (state) {
        this.user = state.user;
        this.Username = this.user.name;
        this.userid = this.user.userid;
        this.token = this.user.token;
      }
      this.profileForm.patchValue({
        userid:this.userid,
        name:this.Username
      })
    });
  }

  onUpdateSubmit() {
    this.isUpdateSubmitted = true;
    if (this.profileForm.valid) {
      const payload = {
        userid: this.userid,
        name: this.Username,
        password: this.profileForm.value.pwd
      };
      this.profileService.updateProfile(payload).subscribe(res => {
        if(res.success = true){
          alert("update successful")
        }else{
          alert("update failed")
        }
        this.profileForm.reset();
        this.profileForm.patchValue({
          userid:this.userid,
          name:this.Username
        })
      }) 
    }
  }

}
