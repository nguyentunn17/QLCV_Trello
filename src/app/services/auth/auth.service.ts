import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../../core/model/user/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseURL = "http://localhost:9090/auth";

  constructor(private httpClient: HttpClient) { }

  login(user: User): Observable<any> {
    return this.httpClient.post<any>(`${this.baseURL}/login`, user);
  }

  register(user: User): Observable<Object> {
    return this.httpClient.post(`${this.baseURL}/register`, user);
  }
}
