import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { TokenStorageService } from '../../services/token-storage/token-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: any = {
    username: '',
    password: ''
  };
  registerForm: any = {
    email: '',
    username: '',
    password: ''
  };
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = this.tokenStorage.getToken();
    if (token) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getUser().roles;
      this.reloadPage();
    }
  }

  onSubmit(): void {
    this.authService.login(this.form).subscribe(
      (data) => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUser(data);

        this.isLoggedIn = true;
        this.isLoginFailed = false;
        this.roles = this.tokenStorage.getUser().roles;
        this.reloadPage();
      },
      (err) => {
        this.errorMessage = "Username or password is wrong!!!";
        this.isLoginFailed = true;
      }
    );
  }

  onRegister(): void {
    this.authService.register(this.registerForm).subscribe(
      (data) => {
        this.activeSignIn();
      },
      (err) => {
        this.errorMessage = err.error.message;
      }
    );
  }

  reloadPage(): void {
    this.router.navigate(['board']);
  }

  activeSignIn(): void {
    const wrapper = document.querySelector('.wrapper');
    if (wrapper) {
      wrapper.classList.add('activeSignIn');
      wrapper.classList.remove('activeSignUp');
    }
  }

  activeSignUp(): void {
    const wrapper = document.querySelector('.wrapper');
    if (wrapper) {
      wrapper.classList.add('activeSignUp');
      wrapper.classList.remove('activeSignIn');
    }
  }

  changeColor(color: string): void {
    const bgAnimate = document.getElementById('bg-animate');
    if (bgAnimate) {
      bgAnimate.style.background = color;
      bgAnimate.classList.add('active');

      setTimeout(() => {
        bgAnimate.classList.remove('active');
      }, 1200);
    }
  }
}
