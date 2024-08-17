import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { TokenStorageService } from '../services/token-storage/token-storage.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthenticateService implements CanActivate {
  constructor(
    private tokenStorageService: TokenStorageService,
    private router: Router,
    private http: HttpClient
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isLogIn = !!this.tokenStorageService.getToken();

    if (isLogIn) {
      // const user = this.tokenStorageService.getUser();
      // this.username = user.username;
      // this.roles = user.roles;
      //
      // if(this.roles.includes("ROLE_USER")){
      //   this.router.navigate(['/login']);
      // }
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }

  logout() {
    return this.http.post('http://localhost:9090/logout', {});
  }
}


