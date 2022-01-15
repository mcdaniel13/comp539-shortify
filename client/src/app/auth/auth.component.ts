import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { User } from '../store/models/users';
import { Login, LoginSuccess, Signup } from '../store/actions/auth.action';
import { selectAuthenticationState } from '../store/app.states';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})

export class AuthComponent implements OnInit {
  user: User = new User();
  getState!: Observable<any>;
  errorMessage: string = '';
  isLoginSubmitted: boolean = false;
  isLoading: boolean = false;
  isRegisterSubmitted: boolean = false;
  loginForm: FormGroup;
  registerForm: FormGroup;
  closeResult = '';

  constructor(
    private store:Store,
    private router: Router,
    private modalService: NgbModal
  ){
    this.getState = this.store.select(selectAuthenticationState);
    this.loginForm = new FormGroup({
      userid: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
    });
    this.registerForm = new FormGroup({
      userid: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      repassword: new FormControl('', [Validators.required])
    });
  }

  ngOnInit(): void {
    this.getState.subscribe((state) => {
      if (state && state.errorMessage) {
        this.errorMessage = state.errorMessage;
        this.isLoading = false;
      }
    });
  }

  directPage(pageName:string){
    this.router.navigate([`${pageName}`]);
  }

  onLoginSubmit() {
    this.isLoginSubmitted = true;
    this.isLoading = true;
    if (this.loginForm.valid) {
      const payload = {
        userid: this.loginForm.value.userid,
        password: this.loginForm.value.password
      };
      this.store.dispatch(new Login(payload));
    }else{
      this.isLoading = false;
    }
    
  }

  onRegistSubmit(){
    this.isRegisterSubmitted = true;
    if (this.registerForm.valid) {
      const payload = {
        userid: this.registerForm.value.userid,
        name: this.registerForm.value.name,
        password: this.registerForm.value.password
      };
      this.store.dispatch(new Signup(payload));
      this.modalService.dismissAll();
    }
  }

  open(content: any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
}
