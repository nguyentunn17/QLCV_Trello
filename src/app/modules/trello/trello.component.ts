import { Component, OnInit } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Lists } from "../../core/model/lists/lists";
import { ListService } from "../../services/lists/lists.service";
import { CardService } from "../../services/card/card.service";
import { Card } from "../../core/model/card/card";
import {User} from "../../core/model/user/user";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'app-trello',
  templateUrl: './trello.component.html',
  styleUrls: ['./trello.component.css']
})
export class TrelloComponent implements OnInit {
  lists: Lists[] = [];
  newListName: string = '';
  newCardTitle: string = '';
  selectedListId: string | null = null;
  boardId: string | null = null;
  boardName: string| null =null;

  users: User[] = [];
  selectedUsers: User[] = [];
  boardTitle: string = '';
  boardDescription: string = '';
  constructor(
    private route: ActivatedRoute,
    private listService: ListService,
    private cardService: CardService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.boardId = this.route.snapshot.paramMap.get('id');
    if (this.boardId) {
      this.loadLists(this.boardId);
      this.boardName = this.route.snapshot.paramMap.get('name');
    }

  }

  loadLists(boardId: string) {
    this.listService.getListsByBoardId(boardId).subscribe(
      (lists: Lists[]) => {
        this.lists = lists;

        // Sau khi lấy danh sách các list, ta cần lấy danh sách các thẻ của từng list
        this.lists.forEach(list => {
          this.cardService.getCardsByListId(list.id).subscribe(
            (cards: Card[]) => {
              list.cards = cards; // Gán danh sách thẻ cho list tương ứng
            },
            error => {
              console.error('Error loading cards for list', error);
            }
          );
        });
      },
      error => {
        console.error('Error loading lists', error);
      }
    );
  }

  drop(event: CdkDragDrop<Card[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }

  dropList(event: CdkDragDrop<Lists[]>) {
    moveItemInArray(this.lists, event.previousIndex, event.currentIndex);
  }

  addList() {
    if (this.newListName.trim() && this.boardId) {
      const newList: Lists = { id: '', boardId: this.boardId, name: this.newListName, position: this.lists.length, cards: [], status: true};
      this.listService.addList(newList).subscribe(list => {
        this.lists.push(list);
        this.newListName = '';
      });
    }
  }

  addCard() {
    if (this.newCardTitle.trim() && this.selectedListId) {
      console.log(this.selectedListId);
      const newCard: Card = {
        id: '', // API sẽ tự động sinh ID
        title: this.newCardTitle,
        listsId: this.selectedListId,
        position: 0,
        createdAt: new Date(),
        description: '',
        startDate: null,
        dueDate: null,
        status: true,
        completed: false
      };

      console.log(newCard); // Log đối tượng newCard để kiểm tra

      this.cardService.addCard(newCard).subscribe({
        next: (card: Card) => {
          const list = this.lists.find(l => l.id === this.selectedListId);
          if (list) {
            list.cards.push(card);
            this.newCardTitle = '';
            this.selectedListId = null;
          }
        },
        error: (error) => {
          console.error('Error adding card:', error);
        }
      });
    }
  }

  loadUsers(): void {
    this.userService.getUserList().subscribe({
      next: (data: User[]) => {
        this.users = data;
        this.users.forEach(user => user.visible = true);
      },
      error: (error: any) => {
        console.error('Error loading users:', error);
      }
    });
  }

  onSearch(event: any): void {
    const searchText = event.target.value.toLowerCase().trim();

    if (searchText === '') {
      this.loadUsers(); // Gọi lại loadUsers nếu không có kết quả tìm kiếm
    } else {
      this.userService.getSearch(searchText).subscribe({
        next: (data: User[]) => {
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
}
