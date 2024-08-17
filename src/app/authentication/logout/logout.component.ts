import { Component } from '@angular/core';
import {AuthenticateService} from "../authenticate.service";
import {TokenStorageService} from "../../services/token-storage/token-storage.service";
import {Router} from "@angular/router";
@Component({
  selector: 'app-logout',
  template: '<button type="button" class="btn btn-danger" (click)="logout()">Đăng xuất</button>',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  constructor( private authService: AuthenticateService,
               private tokenStorageService: TokenStorageService,
               private router: Router) {}

  logout() {
    this.authService.logout().subscribe(() => {
      // Xử lý sau khi đăng xuất thành công
      this.tokenStorageService.signOut();
      console.log('Logged out');
      // Chuyển hướng sang trang đăng nhập
      this.router.navigate(['/login']);
    });
  }
}
