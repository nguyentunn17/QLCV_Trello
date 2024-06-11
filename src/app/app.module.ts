import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './authentication/404/page-not-found/page-not-found.component';
import { RegisterComponent } from './authentication/register/register.component';
import { AnimatedComponent } from './animated/animated.component';
import { LoginComponent } from './authentication/login/login.component';
import {HttpClientModule, withFetch} from "@angular/common/http";
import {RouterOutlet} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {authInterceptorProviders} from "./authentication/helper/auth.interceptor";
import {provideHttpClient} from "@angular/common/http"


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    RegisterComponent,
    AnimatedComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterOutlet,
    FormsModule

  ],
  providers: [
    provideHttpClient(withFetch()),  // Enable fetch API here
    authInterceptorProviders,
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
