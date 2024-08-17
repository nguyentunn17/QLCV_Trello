import { Component, ViewChild, OnInit } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';
import {Board} from "../../../core/model/board/board";
import {BoardService} from "../../../services/board/board.service";


interface Category {
  value: string;
  viewValue: string;
}

interface Sorter {
  value: string;
  viewValue: string;
}

interface Filter {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-board-list',
  templateUrl: './board-list.component.html',
  styleUrls: ['./board-list.component.css']
})
export class BoardListComponent implements OnInit {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isExpanded = false;
  showSubmenu: boolean = false;
  isShowing = false;
  isMobile = true;


  boards: Board[] = []; // Mảng chứa danh sách boards từ service

  constructor(private observer: BreakpointObserver, private boardService: BoardService) {}

  ngOnInit(): void {
    this.loadBoards();
    this.observer.observe(['(max-width: 800px)']).subscribe((screenSize) => {
      this.isMobile = screenSize.matches;
    });
  }

  toggleMenu(): void {
    if (this.isMobile) {
      this.sidenav.toggle();
      this.isExpanded = true;
    } else {
      this.sidenav.open();
      this.isExpanded = !this.isExpanded;
    }
  }

  loadBoards(): void {
    this.boardService.getListBoard().subscribe(
      (boards: Board[]) => {
        this.boards = boards;
      },
      error => {
        console.error('Error loading boards', error);
        // Xử lý lỗi nếu cần thiết
      }
    );
  }
  viewBoard(boardId: number): void {
    this.boardService.getBoardById(boardId).subscribe(
      (board: Board) => {
        // Xử lý khi lấy thành công board chi tiết
        // Bạn có thể điều hướng đến trang chi tiết của board hoặc cập nhật giao diện
        console.log(board);
      },
      error => {
        console.error('Error loading board details', error);
      }
    );
  }
}
