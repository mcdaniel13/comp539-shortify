import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsermanagementService {
  constructor(private readonly httpClient: HttpClient) { }

  getAllUser(){
    return this.httpClient.get<any>('http://35.194.4.193:8080/users');
  }

  getUser(id:string){
    return this.httpClient.get<any>('http://35.194.4.193:8080/user/'+id);
  }
  update(payload:any){
    var url = "userId=" + payload.userid +"&password"+ payload.password+"&name"+payload.name;
    return this.httpClient.put<any>('http://35.194.4.193:8080/user?'+ url, {} );
    // return ;
  }
  deleteUser(id:string){
    return this.httpClient.delete<any>('http://35.194.4.193:8080/user/'+id);
  }

}
