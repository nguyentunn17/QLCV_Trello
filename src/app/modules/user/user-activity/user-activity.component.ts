import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
// import {HistoryService} from "../../../services/history/history.service";


@Component({
  selector: 'app-user-activity',
  templateUrl: './user-activity.component.html',
  styleUrls: ['./user-activity.component.css']
})
export class UserActivityComponent implements OnInit {

  histories: History[] = [];

  // constructor(private modalService: NgbActiveModal, private historyService: HistoryService) { }

  ngOnInit(): void {
    // const username = window.sessionStorage.getItem("username");
    // this.historyService.getHistoryByUser(username).subscribe(data => {
    //   this.histories = data;
    // })
  }

  // closeModal(){
  //   window.sessionStorage.removeItem("username");
  //   this.modalService.close();
  // }
}
