import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./authentication/login/login.component";
import {RegisterComponent} from "./authentication/register/register.component";
import {UserControlComponent} from "./modules/user/user-control/user-control.component";
import {BoardListComponent} from "./modules/board/board-list/board-list.component";
import {BoardAddComponent} from "./modules/board/board-add/board-add.component";
import {TrelloComponent} from "./modules/trello/trello.component";
import {BoardDetailComponent} from "./modules/board/board-detail/board-detail.component";
import {LogoutComponent} from "./authentication/logout/logout.component";
import {HomeComponent} from "./modules/home/home.component";
import {LoginLayoutComponent} from "./modules/login-layout/login-layout.component";
import {AuthenticateService} from "./authentication/authenticate.service";

const routes: Routes = [
  // { path: '', redirectTo: '/login', pathMatch: 'full' },
  //
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthenticateService],
    children: [
      { path: 'user', component: UserControlComponent},
      { path: 'board', component: BoardListComponent},
      { path: 'board/board-add', component: BoardAddComponent},
      { path: 'trello', component: TrelloComponent},
      { path: 'board/:id/:name', component: BoardDetailComponent},
      { path: 'logout',component: LogoutComponent}
    ]
  },
  {
    path: '',
    component: LoginLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
    ]
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
