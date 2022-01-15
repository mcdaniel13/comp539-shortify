import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginResponse } from '../store/models/users';
// import { SignupResponse } from '../store/models/users';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private readonly httpClient: HttpClient) { }

  login(userid: string, password: string): Observable<LoginResponse[]> {
    const payload ={
      userid: userid,
      password:password
    }
    const url = 'userId=' + userid + '&password=' + password;
    return this.httpClient.post<LoginResponse[]>('http://35.194.4.193:8080/signin?'+url, payload);
  }

  signup(name: string, userid:string, password: string){
    const payload ={
      userid: userid,
      name: name,
      password:password
    }
    const url = 'userId=' + userid +'&name=' + name + '&password=' + password;
    return this.httpClient.post<any>('http://35.194.4.193:8080/signup?'+url, payload);
  }
}