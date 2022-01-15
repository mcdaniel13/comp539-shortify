import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  
  constructor(private readonly httpClient: HttpClient) { }

  updateProfile(payload: { userid: string; name: string | undefined; password: any; }) {
    const url = 'userId=' + payload.userid + '&password=' + payload.password + '&name=' + payload.name;
    return this.httpClient.put<any>('http://35.194.4.193:8080/user?'+url, payload);
  }
}
