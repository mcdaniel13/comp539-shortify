import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BulkaccessService {

  constructor(private readonly httpClient: HttpClient) { }

  getBulkUrls(payload: any, username:string) {
    let requestUrl = '';
    let url ='';
    for(var i = 0; i < payload.length; i++) {
      var a = "requestUrl=" + payload[i] + "&";
       url+= a;
    }
    url += 'userId='+ username +'&level=1&expireTime=7';
    return this.httpClient.get<any>('http://35.194.4.193:8080/url/shortBulk?'+url);
  }
}
