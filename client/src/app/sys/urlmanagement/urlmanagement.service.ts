import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UrlmanagementService {

  constructor(private readonly httpClient: HttpClient) { }

  getAllUrl(){
    return this.httpClient.get<any>('http://35.194.4.193:8080/urls');
  }

  getUrl(id:string){
    var url = "?requestUrl=" + id+"&userId=aaa&level=1&expireTime=7";
    return this.httpClient.get<any>('http://35.194.4.193:8080/url/short'+url);
  }
  update(payload:any){
    var url = "userId=" + payload.userid +"&password="+ payload.password+"&name="+payload.name;
    return this.httpClient.put<any>('http://35.194.4.193:8080/urls?'+ url, {} );
    // return ;
  }
  deleteUrl(id:string){
    return this.httpClient.delete<any>('http://35.194.4.193:8080/url/'+id);
  }
}
