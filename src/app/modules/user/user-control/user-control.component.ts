import { Component, OnInit } from '@angular/core';
import {HttpParams} from "@angular/common/http";
import {UserService} from "../../../services/user/user.service";
import {User} from "../../../core/model/user/user";
// import {NavAddComponent} from "../../navigator/nav-add/nav-add.component";
import {NgbModalRef} from "@ng-bootstrap/ng-bootstrap/modal/modal-ref";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {UserAddComponent} from "../user-add/user-add.component";
import {UserActivityComponent} from "../user-activity/user-activity.component";

@Component({
  selector: 'app-user-control',
  templateUrl: './user-control.component.html',
  styleUrls: ['./user-control.component.css']
})
export class UserControlComponent implements OnInit {

  users: User[] = [];
  totalPages: any;
  pageSizes = [10, 20, 30];

  searchField = {
    pageIndex: 1,
    pageSize: 3,
    totalElements: 0,
    keyword: ''
  }

  modalRef?: NgbModalRef;

  constructor(private userService: UserService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.getBySearch();
  }

  getBySearch(){
    const params = new HttpParams()
      .set('pageNo', this.searchField.pageIndex)
      .set('pageSize', this.searchField.pageSize)
      .set('keyword', this.searchField.keyword);
    this.userService.getSearchList(params).subscribe(data => {
      this.users = data.content;
      this.searchField.totalElements = data.totalElements;
      this.totalPages = data.totalPages;
    });
  }

  search(){
    this.searchField.pageIndex = 1;
    this.getBySearch();
  }

  pageChanged(event: any){
    this.searchField.pageIndex = event;
    this.getBySearch();
  }

  changePageSize(psize: any) {
    this.searchField.pageSize = psize.target.value;
    this.searchField.pageIndex = 1;
    this.getBySearch();
  }

  addNew(){
    this.modalRef = this.modalService.open(UserAddComponent, {
      centered: true,
      backdrop: false,
      animation: true
    });
  }

  updateUser(e: any){
    this.modalRef = this.modalService.open(UserAddComponent, {
      centered: true,
      backdrop: false,
      animation: true
    });
    // this.modalRef.result.then(item => {
    //   if(item){
    //     this.getAllNavGroup();
    //   }
    // })
    window.sessionStorage.setItem("UID", e)
  }

  activeDetails(e: any){
    this.modalRef = this.modalService.open(UserActivityComponent, {
      centered: true,
      backdrop: false,
      animation: true
    });
    window.sessionStorage.setItem("username", e)
  }

}
