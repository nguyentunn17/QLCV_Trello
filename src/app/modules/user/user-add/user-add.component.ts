import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../services/user/user.service";
import {User} from "../../../core/model/user/user";
import {AuthService} from "../../../services/auth/auth.service";

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit {

  id: any
  user: User = new User();
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = "";

  constructor(private modalService: NgbActiveModal, private userService: UserService,
              private authService: AuthService) { }

  ngOnInit(): void {
    this.id = window.sessionStorage.getItem("UID");
    if(this.id){
      this.userService.getUserById(this.id).subscribe(data => {
        this.user = data;
      })
    }
  }

  onSubmit(){
    this.authService.register(this.user).subscribe(data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        this.errorMessage = "Đăng ký thành công!!"
      },
      err =>{
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      })
  }

  closeModal(){
    window.sessionStorage.removeItem("UID");
    this.modalService.close();
  }

  resetPassword(id: any){
    const option = confirm("Thao tác này sẽ đưa mật khẩu về giá trị mặc đinh. Bạn có muốn tiếp tục?")
    if (option){
      this.userService.resetPassword(id).subscribe(data => {
        this.modalService.close(data);
      })
    }
  }

}
