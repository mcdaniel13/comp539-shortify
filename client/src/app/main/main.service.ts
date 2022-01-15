import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class MainService {
  constructor(private readonly httpClient: HttpClient) {}

  requestShortUrl(longUrl: string, userid: string, token: string) {
    const url =
      'requestUrl=' + longUrl + '&userId=' + userid + '&access_token=' + token;
    return this.httpClient.get<any>(
      'http://35.194.4.193:8080/url/short?' + url
    );
  }
}
