// board-add.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user/user.service';
import { User } from "../../../core/model/user/user";
import { Board } from "../../../core/model/board/board";
import { UserBoard } from "../../../core/model/user-board/user-board";
import { UserBoardService } from "../../../services/user-board/user-board.service";
import { BoardService } from "../../../services/board/board.service";
import { Router } from "@angular/router";
import {map, mergeMap, tap} from 'rxjs/operators';
import { of } from "rxjs";
import {TokenStorageService} from "../../../services/token-storage/token-storage.service";

@Component({
  selector: 'app-board-add',
  templateUrl: './board-add.component.html',
  styleUrls: ['./board-add.component.css']
})
export class BoardAddComponent implements OnInit {
  users: User[] = [];
  selectedUsers: User[] = [];
  boardTitle: string = '';
  boardDescription: string = '';

  constructor(
    private userService: UserService,
    private boardService: BoardService,
    private userBoardService: UserBoardService,
    private router: Router,
    private tokenStorage: TokenStorageService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    const currentUsername = this.tokenStorage.getUsername();

    this.userService.getUserList().subscribe({
      next: (data: User[]) => {
        this.users = data.filter(user => user.username !== currentUsername);
        this.users.forEach(user => user.visible = true);
      },
      error: (error: any) => {
        console.error('Error loading users:', error);
      }
    });
  }

  onSearch(event: any): void {
    const searchText = event.target.value.trim();

    if (searchText === '') {
      this.loadUsers();
    } else {
      this.userService.getSearch(searchText).subscribe({
        next: (data: User[]) => {
          console.log('Search results:', data);
          this.users = data;
          this.users.forEach(user => user.visible = true);
        },
        error: (error: any) => {
          console.error('Error searching users:', error);
        }
      });
    }
  }

  onSelectChange(user: User): void {
    const index = this.selectedUsers.indexOf(user);
    if (index === -1) {
      this.selectedUsers.push(user);
    } else {
      this.selectedUsers.splice(index, 1);
    }
  }

  isSelected(user: User): boolean {
    return this.selectedUsers.some(selectedUser => selectedUser.id === user.id);
  }

  removeSelectedUser(user: User): void {
    const index = this.selectedUsers.indexOf(user);
    if (index !== -1) {
      this.selectedUsers.splice(index, 1);
    }
  }

  onCreateBoard(): void {
    const board = new Board(
      this.boardTitle,
      this.boardDescription,
      new Date(),
      1
    );
    if (this.selectedUsers.length > 0) {
      const userIds = this.selectedUsers.map(user => user.id);
      console.log('Adding members with userIds:', userIds);
      this.boardService.addBoardWithMembers(board, userIds).subscribe(
        (createdBoard) => {
          console.log('Board created successfully', createdBoard);
          this.router.navigate(['/board', createdBoard.id, this.formatBoardTitle(createdBoard.name)]);
        },
        (error) => {
          console.error('Error creating board', error);
          // Xử lý lỗi nếu cần
        }
      );
    } else {
      console.log('No users selected');
      // Xử lý trường hợp không có người dùng nào được chọn
    }
  }

  private formatBoardTitle(title: string): string {
    // Replace spaces with dashes and handle any other necessary formatting
    return title.toLowerCase().replace(/\s+/g, '-');
  }

}
