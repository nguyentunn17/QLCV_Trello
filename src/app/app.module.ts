import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './authentication/404/page-not-found/page-not-found.component';
import { RegisterComponent } from './authentication/register/register.component';
import { AnimatedComponent } from './animated/animated.component';
import { LoginComponent } from './authentication/login/login.component';
import {HttpClientModule, withFetch} from "@angular/common/http";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {authInterceptorProviders} from "./authentication/helper/auth.interceptor";
import {provideHttpClient} from "@angular/common/http"
import { UserAddComponent } from './modules/user/user-add/user-add.component';
import {UserActivityComponent} from "./modules/user/user-activity/user-activity.component";
import {UserControlComponent} from "./modules/user/user-control/user-control.component";
import {TrelloComponent} from "./modules/trello/trello.component";
import { DragDropModule } from '@angular/cdk/drag-drop';
import { BoardAddComponent } from './modules/board/board-add/board-add.component';
import { BoardListComponent } from './modules/board/board-list/board-list.component';
import {CommonModule} from "@angular/common";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { BoardDetailComponent } from './modules/board/board-detail/board-detail.component';
import { LogoutComponent } from './authentication/logout/logout.component';
import { LoginLayoutComponent } from './modules/login-layout/login-layout.component';
import {HomeComponent} from "./modules/home/home.component";
import { MessageBoxComponent } from './modules/message-box/message-box.component';


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    RegisterComponent,
    AnimatedComponent,
    LoginComponent,
    UserAddComponent,
    UserActivityComponent,
    UserControlComponent,
    TrelloComponent,
    BoardAddComponent,
    BoardListComponent,
    BoardDetailComponent,
    LogoutComponent,
    LoginLayoutComponent,
    HomeComponent,
    MessageBoxComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterOutlet,
    FormsModule,
    DragDropModule,
    RouterOutlet,
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    BrowserAnimationsModule
  ],
  providers: [
    provideHttpClient(withFetch()),  // Enable fetch API here
    authInterceptorProviders,
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
