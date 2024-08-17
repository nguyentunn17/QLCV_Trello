import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Lists } from "../../../core/model/lists/lists";
import { ListService } from "../../../services/lists/lists.service";
import { CardService } from "../../../services/card/card.service";
import { LabelService } from "../../../services/label/label.service";
import { Card } from "../../../core/model/card/card";
import { User } from "../../../core/model/user/user";
import { UserService } from "../../../services/user/user.service";
import {Label} from "../../../core/model/label/label";
import {CardLabelService} from "../../../services/card-label/card-label.service";
import {BoardService} from "../../../services/board/board.service";
import {MessageBoxService} from "../../../services/message-box/message-box.service";
import { switchMap, EMPTY} from "rxjs";
import {mergeMap, tap} from "rxjs/operators";
import {UserBoardService} from "../../../services/user-board/user-board.service";
import { Router } from '@angular/router';
import {TokenStorageService} from "../../../services/token-storage/token-storage.service";
import {Checklist} from "../../../core/model/checklist/checklist";
import {ChecklistItem} from "../../../core/model/checklist-item/checklist-item";
import {ChecklistService} from "../../../services/checklist/checklist.service";
import { ChangeDetectorRef } from '@angular/core';
import {ChecklistItemService} from "../../../services/checklist-item/checklist-item.service";

@Component({
  selector: 'app-board-detail',
  templateUrl: './board-detail.component.html',
  styleUrls: ['./board-detail.component.css']
})
export class BoardDetailComponent implements OnInit {

  boardId: string | null = null;
  boardName: string | null = null;
  isAdmin: boolean = false;
  isEditing: boolean = false;

  cardId: number | null = null;
  newCardTitle: string = '';


  lists: Lists[] = [];
  newListName: string = '';
  editingListId: string | null = null;
  originalListName: string = '';
  selectedListName: string | null = null;
  selectedListId: string | null = null;

  newChecklistName: string = 'Việc cần làm';
  checklists: Checklist[] = [];
  originalChecklist: Checklist[] = [];
  checklist: Checklist | null = null;


  originalChecklistItem: ChecklistItem[] = []; // Đổi từ originalChecklist
  editingChecklistItemId: number | null = null; // Đổi từ editingDescriptionId
  checklistItems: ChecklistItem[] = [];
  filteredChecklistItems: ChecklistItem[] = [];
  selectedChecklistId: string | null = null;

  showAddItemInput: { [checklistId: string]: boolean } = {}; // Trạng thái hiển thị input cho từng checklist
  newChecklistItemName: { [checklistId: string]: string } = {}; // Tên item mới cho từng checklist


  selectedLabels: number[] = [];
  originalLabel: Label[] = [];
  cardLabelsMap: { [cardId: number]: Label[] } = {};


  originalDescription: string = '';
  editingDescriptionId: string | null = null;
  originalChecklistName: string = '';


  labels: Label[] = [];
  color: string = '#FFFFFF';
  customColor: string = '#FFFFFF';
  selectedLabel: Label | null = null;
  labelName: string = '';
  colorOptions: string[] = [
    '#61BD4F', '#F2D600', '#FF9F1A', '#EB5A46', '#C377E0', '#0079BF', '#00C2E0', '#51E898', '#FF78CB', '#344563'
  ];

  usersInBoard: User[] = [];
  usersNotInBoard: User[] = [];

  showMembersSection: boolean = false;
  showMembersSection2: boolean = false;
  showLabelsSection: boolean = false;
  showAddLabelSection: boolean = false;
  showChecklistSection: boolean = false;
  showAttachment: boolean = false;
  showUpdateLabelSection: boolean = false;
  showDaySection: boolean = false;
  showDescriptionSection: boolean = false;
  showRestoreSection: boolean = false;


  card: Card[] = [];
  selectedCard: Card | null = null;
  selectedUsersInBoard: User[] = [];
  selectedUsersNotInBoard: User[] = [];
  originalUsersInBoard: User[] = [];
  originalUsersInBoardExceptAdmin: User[] = [];

  userToRemove: User | null = null;


  private actionToConfirm: Function | null = null;


  constructor(
    private route: ActivatedRoute,
    private listService: ListService,
    private cardService: CardService,
    private checklistService: ChecklistService,
    private checklistItemService: ChecklistItemService,
    private userService: UserService,
    private labelService: LabelService,
    private cardLabelService: CardLabelService,
    private boardService: BoardService,
    private messageBoxService: MessageBoxService,
    private userBoardService: UserBoardService,
    private router: Router,
    private tokenStorage: TokenStorageService,
    private changeDetectorRef: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {

    this.boardId = this.route.snapshot.paramMap.get('id');
    if (this.boardId) {
      this.loadLists(this.boardId);
      this.boardName = this.route.snapshot.paramMap.get('name');
      // this.loadAllUsers();
      this.loadUserInBoard();
      this.loadUserNotInBoard();
      this.loadUserInBoardExceptAdmin();
      this.loadLabelByBoard(this.boardId);
      this.loadLabelInCard2();
      this.updateCardDisplay();
      this.selectedLabel = {id: null, boardId: '', color: '#FFFFFF', name: ''};
      // Lấy quyền người đó trong bảng(ADMIN hay MEMBER)
      this.userBoardService.getUserRole(Number(this.boardId)).subscribe(role => {
        this.isAdmin = role === 'ADMIN';
      });
    } else {
      console.error('boardId is null');
    }

    if (this.selectedCard) {
      this.originalDescription = this.selectedCard.description;
    }
  }

  // Chỉnh sửa boardName
  onUpdateBoardName(newName: string): void {
    if (this.boardId) {
      this.boardService.updateBoard(Number(this.boardId), newName).subscribe(
        response => {
          this.boardName = newName;
          this.isEditing = false;
        },
        error => {
          console.error('Error updating board name:', error);
        }
      );
    }
  }


  // Chỉnh sửa hay không
  toggleEdit(): void {
    this.isEditing = !this.isEditing;
  }

  // Sự kiện click khỏi input
  onKeyUpEnter(event: Event): void {
    const newName = (event.target as HTMLInputElement).value;
    this.onUpdateBoardName(newName);
  }

  // Sự kiện click khỏi input
  onBlur(event: Event): void {
    const newName = (event.target as HTMLInputElement).value;
    this.onUpdateBoardName(newName);
  }

  //Hiện thông báo đóng bảng
  closeBoard(): void {
    this.actionToConfirm = this.performCloseBoard.bind(this);
    this.messageBoxService.showMessage('Bạn có thể tìm và mở lại các bảng đã đóng ở cuối trang các bảng của bạn.');
  }

  // Đóng bảng
  performCloseBoard(): void {
    this.boardService.closeBoard(Number(this.boardId)).subscribe(
      response => {
        alert('Bảng đã được đóng!');
        this.router.navigate(['/board']); // Điều hướng đến trang board
      },
      error => {
        console.error('Error:', error); // Logging lỗi
        alert('Có lỗi xảy ra khi đóng bảng.');
      }
    );
  }

  // Hiện thông báo rời khỏi bảng
  leaveBoard(): void {
    this.actionToConfirm = this.performLeaveBoard.bind(this);
    this.messageBoxService.showMessage('Bạn sẽ bị loại bỏ khỏi toàn bộ thẻ trong bảng này.');
  }

  // Rời khỏi bảng
  performLeaveBoard(): void {
    this.boardService.leaveBoard(Number(this.boardId)).subscribe(
      response => {
        this.router.navigate(['/board']);
      },
      error => {
        console.error('Error:', error); // Xử lý lỗi
      }
    );
  }

  // Hiện thị tất cả user trong board trừ ADMIN của board để kick thành viên ra khỏi board
  loadUserInBoardExceptAdmin() {
    if (this.boardId) {
      const currentUsername = this.tokenStorage.getUsername();
      this.userService.getUserByBoard(this.boardId).subscribe({
        next: (data: User[]) => {
          this.originalUsersInBoardExceptAdmin = data.filter(user => user.username !== currentUsername);
          this.originalUsersInBoardExceptAdmin.forEach(user => user.visible = true);
        },
        error: (error: any) => {
          console.error('Error loading users:', error);
        }
      })
    }
  }

  // Hiện thị mess kick thành viên ra khỏi bảng
  removeUserFromBoard(user: User): void {
    this.userToRemove = user;
    this.actionToConfirm = this.performRemoveUserFromBoard.bind(this, user);
    this.messageBoxService.showMessage('Người này sẽ bị xoá khỏi tất cả các thẻ trong bảng này.');
  }

  // kick thành viên ra khỏi bảng
  performRemoveUserFromBoard(user: User): void {
    if (user) {
      this.boardService.removeUserFromBoard(Number(this.boardId), user.id).subscribe(
        response => {
          this.loadUserInBoardExceptAdmin();
        },
        error => {
          console.error('Error:', error); // Xử lý lỗi
        }
      );
    }
  }

  loadLists(boardId: string) {
    this.listService.getListsByBoardId(boardId).subscribe(
      (lists: Lists[]) => {
        this.lists = lists;
        // Đảm bảo rằng danh sách cards được khởi tạo trước khi gán dữ liệu
        this.lists.forEach(list => {
          if (list && list.id) {
            this.loadCards(list.id); // Gọi phương thức loadCards cho từng list
            this.updateCard();
          } else {
            console.error('List or list ID is null/undefined');
          }
        });
      },
      error => {
        console.error('Error loading lists', error);
      }
    );
  }
  //Lưu trữ card
  toggleRestoreCard(cardId: number): void {
    this.cardService.restoreCard(cardId).subscribe(
      () => {
        if (this.selectedCard && this.selectedCard.listsId) {
          this.loadCards(this.selectedCard.listsId);
          this.showRestoreSection = !this.showRestoreSection;
        } else {
          console.error('List ID is undefined, skipping loadCards');
        }
      },
      (error) => {
        console.error('Error restoring card:', error);
      }
    );
  }

// Bỏ lưu tữ card
  archiveCard(cardId: number){
    this.cardService.archiveCard(cardId).subscribe(
      () => {
        // Khi yêu cầu thành công, cập nhật giao diện
        if (this.selectedCard && this.selectedCard.listsId) {
          this.loadCards(this.selectedCard.listsId);
          this.showRestoreSection = !this.showRestoreSection;
        } else {
          console.error('List ID is undefined, skipping loadCards');
        }

      },
      (error) => {
        // Xử lý lỗi nếu cần
        console.error('Error restoring card:', error);
      }
    );

  }
  // Hiện thị label ở thẻ
  loadLabelInCard2() {
    if (this.selectedCard) {
      this.cardLabelService.getLabelByCardId(this.selectedCard.id).subscribe(
        (labels: Label[]) => {
          // Lưu trữ nhãn theo thẻ
          this.cardLabelsMap[this.selectedCard?.id] = labels;

          // Kiểm tra dữ liệu đã lưu
          console.log('Updated cardLabelsMap:', this.cardLabelsMap);

          // Tự động check các checkbox của nhãn đã chọn
          this.selectedLabels = labels.map(label => label.id);

          // Gọi phương thức để cập nhật giao diện
          this.updateCardDisplay();
        },
        error => {
          console.error('Error loading labels:', error);
        }
      );
    }
  }

  updateCardDisplay() {
    // Thực hiện cập nhật giao diện thẻ ở đây
    console.log('Updating card display');
    // Có thể cần phải gọi một số hàm để cập nhật giao diện hoặc thông báo cho Angular biết về sự thay đổi
    this.changeDetectorRef.detectChanges();
  }

  loadCards(listId: string) {
    this.cardService.getCardsByListId(listId).subscribe(
      (cards: Card[]) => {
        const list = this.lists.find(l => l.id === listId);
        if (list) {
          // Đảm bảo rằng cards không bị undefined trước khi gán
          list.cards = cards || [];
          // Gọi để lấy nhãn cho từng thẻ
          list.cards.forEach(card => {
            this.cardLabelService.getLabelByCardId(card.id).subscribe(
              (labels: Label[]) => {
                this.cardLabelsMap[card.id] = labels;
              },
              error => {
                console.error('Error loading labels for card:', error);
              }
            );
          });
        } else {
          console.error('List not found for ID:', listId);
        }
      },
      error => {
        console.error('Error loading cards for list', error);
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
      const newList: Lists = {
        id: '',
        boardId: this.boardId,
        name: this.newListName,
        position: this.lists.length + 1,
        cards: [],
        status: true
      };
      this.listService.addList(newList).subscribe(list => {
        this.lists.push(list);
        this.newListName = '';
      });
    }
  }


  updateList(list: Lists) {
    this.listService.updateList(list).subscribe({
      next: () => {
      },
      error: (error) => {
        console.error('Error updating list:', error);
      }
    });
  }

  editListName(listId: string) {
    const list = this.lists.find(l => l.id === listId);
    if (list) {
      this.originalListName = list.name;
      this.editingListId = listId;
    }
  }

  saveListName(list: Lists) {
    if (list.name.trim() && this.boardId) {
      list.boardId = this.boardId;
      this.updateList(list);
    } else {
      list.name = this.originalListName;
    }
    this.editingListId = null;
  }


  // Mở modal để cập nhật thẻ
  openCardModal(card: Card, list: Lists) {
    this.selectedCard = {...card};
    this.selectedCard.startDate = this.formatDateForInput(this.selectedCard.startDate, true); // Format as date without time
    this.selectedCard.dueDate = this.formatDateForInput(this.selectedCard.dueDate, false); // Format as datetime
    this.selectedListName = list.name;
    this.cardId = card.id;
    this.loadMembers();
    this.loadLabelInCard();
    this.loadLabelInCard2();
    this.loadChecklist();
    this.showRestoreSection = false; // Reset trạng thái khi chọn card mới
  }

  // Xử lý thay đổi ngày bắt đầu
  onStartDateChange() {
    if (this.selectedCard) {
      const startDate = new Date(this.selectedCard.startDate);
      const dueDate = new Date(this.selectedCard.dueDate);
      if (dueDate < startDate) {
        this.selectedCard.dueDate = this.formatDateForInput(new Date(startDate.getTime() + 24 * 60 * 60 * 1000).toISOString(), false); // +1 day
      }
    }
  }

  // Xử lý thay đổi ngày hết hạn
  onDueDateChange() {
    if (this.selectedCard) {
      const startDate = new Date(this.selectedCard.startDate);
      const dueDate = new Date(this.selectedCard.dueDate);
      if (startDate > dueDate) {
        this.selectedCard.startDate = this.formatDateForInput(new Date(dueDate.getTime() - 24 * 60 * 60 * 1000).toISOString(), true); // -1 day
      }
    }
  }

  addCard() {
    if (this.newCardTitle.trim() && this.selectedListId) {
      const list = this.lists.find(l => l.id === this.selectedListId);
      if (list) {
        if (!list.cards) {
          list.cards = [];
        }

        const newCard: Card = {
          id: '',
          title: this.newCardTitle,
          listsId: this.selectedListId,
          position: list.cards.length + 1,
          createdAt: new Date(),
          description: '',
          startDate: null,
          dueDate: null,
          completed: false,
          status: true
        };

        this.cardService.addCard(newCard).subscribe({
          next: (card: Card) => {
            list.cards.push(card);
            this.changeDetectorRef.detectChanges();
            this.newCardTitle = '';
            this.selectedListId = null;
          },
          error: (error) => {
            console.error('Error adding card:', error);
          }
        });
      } else {
        console.error('Selected list not found for ID:', this.selectedListId);
      }
    }
  }


  trackById(index: number, item: Lists): string {
    return item.id;
  }

  //Cập nhật thẻ
  updateCard() {
    if (this.selectedCard) {
      this.cardService.updateCard(this.selectedCard).subscribe({
        next: (updatedCard: Card) => {
          const list = this.lists.find(l => l.id === this.selectedCard!.listsId);
          if (list) {
            const cardIndex = list.cards.findIndex(c => c.id === updatedCard.id);
            if (cardIndex !== -1) {
              list.cards[cardIndex] = updatedCard;
            }
          }
          this.showDaySection = false;
          this.showDescriptionSection = false;
        },
        error: (error) => {
          console.error('Error updating card:', error);
        }
      });
    }
  }

  // Xoá thẻ
  deleteCard(cardId: number): void {
    this.actionToConfirm = this.performDeleteCard.bind(this, cardId);
    this.messageBoxService.showMessage('Tất cả hoạt động trên thẻ sẽ bị xoá khỏi bảng tin hoạt động và bạn sẽ không thể mở lại thẻ nữa. Sẽ không có cách nào để hoàn tác. Bạn có chắc không?');
  }

  // Xóa thẻ
  performDeleteCard(cardId: number) {
      this.cardService.deleteCard(cardId).subscribe({
        next: () => {
          // Cập nhật giao diện sau khi xóa thẻ
          this.lists.forEach(list => {
            list.cards = list.cards.filter(card => card.id !== cardId);
          });
        },
        error: (error) => {
          console.error('Error deleting card:', error);
        }
      });
  }

  loadChecklist() {
    if (this.selectedCard) {
      // Reset danh sách checklist items mỗi khi load checklist mới
      this.checklistItems = [];

      this.checklistService.getChecklistByCardId(this.selectedCard.id).subscribe(
        (checklists: Checklist[]) => {
          this.originalChecklist = checklists;

          // Nếu có ít nhất một checklist, thì chọn cái đầu tiên
          if (checklists.length > 0) {
            this.selectedChecklistId = checklists[0].id;
          }

          // Sử dụng vòng lặp để tải tất cả các checklist items liên quan
          for (const checklist of checklists) {
            this.loadChecklistItems(checklist.id);
          }
        },
        error => {
          console.error('Error loading checklists:', error);
        }
      );
    }
  }


  addChecklist() {
    if (this.selectedCard && this.newChecklistName.trim()) {
      const newChecklist: Checklist = {
        id: '',
        name: this.newChecklistName,
        cardId: this.selectedCard.id
      };

      this.checklistService.addChecklist(newChecklist).subscribe(checklist => {
        this.checklists.push(checklist);
        this.showChecklistSection = false;
        this.loadChecklist();
        this.newChecklistName = 'Việc phải làm';

        // Đợi 1 chút rồi bôi đen văn bản trong ô input
        setTimeout(() => {
          const inputElement = document.getElementById('newChecklistName') as HTMLInputElement;
          if (inputElement) {
            inputElement.focus();
            inputElement.select(); // Bôi đen văn bản trong ô input
          }
        }, 0);
      });
    }
  }

  selectText(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    setTimeout(() => inputElement.select(), 0); // Bôi đen văn bản khi focus vào input
  }


  editChecklistName(checklistId: string) {
    const checklist = this.originalChecklist.find(cl => cl.id === checklistId);
    if (checklist) {
      this.originalChecklistName = checklist.name;  // Lưu tên gốc vào originalChecklistName
      this.editingDescriptionId = checklistId;
    }
  }

  saveChecklist(checklist: Checklist) {
    if (checklist.name.trim() && this.selectedCard && this.selectedCard.id) {
      checklist.cardId = this.selectedCard.id;
      this.updateChecklist(checklist);
    } else {
      checklist.name = this.originalChecklistName;  // Gán lại tên gốc nếu không hợp lệ
    }
    this.editingDescriptionId = null;
  }

  updateChecklist(checkList: Checklist) {
    this.checklistService.updateChecklist(checkList).subscribe({})
  }

  loadChecklistItems(checklistId: string): void {
    this.checklistItemService.getChecklistItemByChecklistId(checklistId).subscribe((items: ChecklistItem[]) => {
      // Thêm các item mới vào mảng mà không xóa những item đã có
      this.checklistItems = [...this.checklistItems, ...items];
      // Cập nhật filteredChecklistItems nếu cần thiết
      this.filteredChecklistItems = this.filterChecklistItems(checklistId);
    });
  }

  filterChecklistItems(checklistId: string): ChecklistItem[] {
    return this.checklistItems.filter(i => i.checklistId === checklistId);
  }


  addChecklistItem(selectedChecklistId: string): void {
    if (this.newChecklistItemName[selectedChecklistId].trim() && selectedChecklistId) {
      const newChecklistItem: ChecklistItem = {
        id: '',
        name: this.newChecklistItemName[selectedChecklistId],
        isCompleted: false,
        position: this.filteredChecklistItems.length + 1,
        checklistId: selectedChecklistId
      };
      this.checklistItemService.addChecklistItem(newChecklistItem).subscribe((item: ChecklistItem) => {
        this.checklistItems.push(item);
        this.filteredChecklistItems = this.filterChecklistItems(selectedChecklistId);
        this.newChecklistItemName[selectedChecklistId] = ''; // Reset input field
        this.showAddItemInput[selectedChecklistId] = false; // Ẩn ô input sau khi thêm item
      });
    }
  }


  toggleChecklistItemCompletion(item: ChecklistItem): void {
    item.isCompleted = !item.isCompleted;
    this.checklistItemService.updateChecklistItem(item).subscribe(() => {
      this.updateChecklistProgress();
    });
  }

  updateChecklistProgress(): void {
    if (this.selectedChecklistId) {
      const completedItems = this.checklistItems.filter(i => i.checklistId === this.selectedChecklistId && i.isCompleted).length;
      const totalItems = this.checklistItems.filter(i => i.checklistId === this.selectedChecklistId).length;
      const progress = totalItems > 0 ? (completedItems / totalItems) * 100 : 0;
    }
  }

  editChecklistItemName(checklistItemId: number): void {
    this.editingChecklistItemId = checklistItemId;
  }

  saveChecklistItem(checklistItem: ChecklistItem): void {
    this.checklistItemService.updateChecklistItem(checklistItem).subscribe(() => {
      this.editingChecklistItemId = null;
    });
  }


  // Hàm xóa checklist
  deleteChecklist(selectedChecklistId: string): void {
    this.actionToConfirm = this.performDeleteChecklist.bind(this, selectedChecklistId);
    this.messageBoxService.showMessage('Danh sách công việc sẽ bị xoá vĩnh viễn và không bao giờ lấy lại được.');
  }

// Hàm thực hiện xóa checklist
  performDeleteChecklist(selectedChecklistId: string): void {
    if (selectedChecklistId) {
      this.checklistService.deleteChecklist(Number(selectedChecklistId)).subscribe({
        next: () => {
          // Cập nhật danh sách checklist sau khi xóa
          this.checklists = this.checklists.filter(checklist => checklist.id !== selectedChecklistId);
          this.loadChecklist();
        },
        error: (error) => {
          console.error('Error deleting checklist:', error);
        }
      });
    }
  }

  calculateChecklistProgress(checklistId: string): number {
    const items = this.checklistItems.filter(i => i.checklistId === checklistId);
    const completedItems = items.filter(i => i.isCompleted).length;
    const totalItems = items.length;
    const progress = totalItems > 0 ? (completedItems / totalItems) * 100 : 0;
    return Math.round(progress); // Làm tròn phần trăm
  }

  cancelAddItem(checklistId: string): void {
    this.showAddItemInput[checklistId] = false; // Ẩn ô input thêm item
    this.newChecklistItemName[checklistId] = ''; // Xóa tên item mới
  }

  // đóng model
  closeModal() {
    this.selectedCard = null;
    this.selectedListName = null;
  }

  // đóng card
  closeCard() {
    this.selectedListId = null;
  }

  // hiện thị thành viên trong card
  loadMembers() {
    if (this.selectedCard) {
      this.cardService.getMembersInCard(this.selectedCard.id).subscribe(
        (users: User[]) => {
          this.originalUsersInBoard = users;
        },
        error => {
          console.error('Error loading members:', error);
        }
      );
    }
  }

  // Xoá thành viên khỏi card
  removeMember(user: User): void {
    if (this.selectedCard) {
      this.cardService.removeMemberFromCard(this.selectedCard.id, user.id).subscribe({
        next: () => {
          // Cập nhật danh sách thành viên sau khi xóa
          this.originalUsersInBoard = this.originalUsersInBoard.filter(u => u.id !== user.id);
        },
        error: (error) => {
          console.error('Error removing member:', error);
        }
      });
    }
  }

  // hiện thị thành viên trong bảng để add vào card
  loadUserInBoard() {
    if (this.boardId) {
      this.userService.getUserByBoard(this.boardId).subscribe({
        next: (data: User[]) => {
          this.usersInBoard = data.map(user => ({...user, visible: true}));
        },
        error: (error: any) => {
          console.error('Error loading users:', error);
        }
      })
    }
  }

  // hiện thị thành viên không trong bảng để add vào bảng
  loadUserNotInBoard() {
    if (this.boardId) {
      this.userService.getUserNotAtBoard(this.boardId).subscribe({
        next: (data: User[]) => {
          this.usersNotInBoard = data.map(user => ({...user, visible: true}));
        },
        error: (error: any) => {
          console.error('Error loading users:', error);
        }
      })
    }
  }

  // tìm kiếm thành viên trong bảng
  onSearchInBoard(event: any): void {
    const searchText = event.target.value.toLowerCase().trim();
    if (!searchText) {
      this.loadUserInBoard();
    } else {
      this.userService.getSearch(searchText).subscribe({
        next: (data: User[]) => {
          // Lọc ra chỉ những người dùng trong bảng
          this.usersInBoard = data
            .filter(user => this.usersInBoard.some(u => u.id === user.id))
            .map(user => ({...user, visible: true}));
        },
        error: (error: any) => {
          console.error('Error searching users:', error);
        }
      });
    }
  }

  // tìm kiếm thành viên không trong bảng
  onSearchNotInBoard(event: any): void {
    const searchText = event.target.value.toLowerCase().trim();
    if (!searchText) {
      this.loadUserNotInBoard();
    } else {
      this.userService.getSearch(searchText).subscribe({
        next: (data: User[]) => {
          // Lọc ra chỉ những người dùng không trong bảng
          this.usersNotInBoard = data
            .filter(user => this.usersNotInBoard.some(u => u.id === user.id))
            .map(user => ({...user, visible: true}));
        },
        error: (error: any) => {
          console.error('Error searching users:', error);
        }
      });
    }
  }

  // chọn thành viên trong bảng
  onSelectChangeInBoard(user: User): void {
    const index = this.selectedUsersInBoard.indexOf(user);
    if (index === -1) {
      this.selectedUsersInBoard.push(user);
    } else {
      this.selectedUsersInBoard.splice(index, 1);
    }
  }

  // chọn thành viên không trong bảng
  onSelectChangeNotInBoard(user: User): void {
    const index = this.selectedUsersNotInBoard.indexOf(user);
    if (index === -1) {
      this.selectedUsersNotInBoard.push(user);
    } else {
      this.selectedUsersNotInBoard.splice(index, 1);
    }
  }

// chọn thành viên
  isSelectedInBoard(user: User): boolean {
    return this.selectedUsersInBoard.some(selectedUser => selectedUser.id === user.id);
  }

  // đã chọn thành viên
  isSelectedNotInBoard(user: User): boolean {
    return this.selectedUsersNotInBoard.some(selectedUser => selectedUser.id === user.id);
  }

  // xoá thành viên
  removeSelectedUserInBoard(user: User): void {
    const index = this.selectedUsersInBoard.indexOf(user);
    if (index !== -1) {
      this.selectedUsersInBoard.splice(index, 1);
    }
  }

  // xoá thành viên
  removeSelectedUserNotInBoard(user: User): void {
    const index = this.selectedUsersNotInBoard.indexOf(user);
    if (index !== -1) {
      this.selectedUsersNotInBoard.splice(index, 1);
    }
  }

  // thêm thành viên vào bảng
  addMemberInBoard() {
    if (this.selectedUsersNotInBoard.length > 0 && this.boardId) {
      const userIds = this.selectedUsersNotInBoard.map(user => user.id);
      this.boardService.addMembersToBoard(Number(this.boardId), userIds)
        .subscribe({
          next: (response) => {
            // Thực hiện các hành động cần thiết sau khi thêm thành viên thành công
            this.loadMembers();
            this.selectedUsersNotInBoard.length = 0;
            this.showMembersSection2 = false;
            this.loadUserNotInBoard();
            this.loadUserInBoard();
            this.loadUserInBoardExceptAdmin();
          },
          error: (error) => {
            console.error('Error adding members:', error);
          }
        });
      this.toggleMembersSection();
    }
  }

  // thêm tthanh viên vào card
  addMemberToCard(): void {
    if (this.selectedUsersInBoard.length > 0 && this.selectedCard) {
      const userIds = this.selectedUsersInBoard.map(user => user.id);
      this.cardService.addMemberToCard(this.selectedCard.id, userIds)
        .subscribe({
          next: (response) => {
            // Thực hiện các hành động cần thiết sau khi thêm thành viên thành công
            this.loadMembers();
            this.selectedUsersInBoard.length = 0;
            this.showMembersSection = false;
            this.loadUserInBoard();
          },
          error: (error) => {
            console.error('Error adding members:', error);
          }
        });
      this.toggleMembersSection();
    }
  }

// khi click vào checkbox label
  toggleLabelSelection(label: any): void {
    const index = this.selectedLabels.indexOf(label.id);
    if (index === -1) {
      // Thêm nhãn vào thẻ khi checkbox được checked
      this.selectedLabels.push(label.id);
      // @ts-ignore
      this.cardLabelService.addCardLabel(this.selectedCard.id, [label.id]).subscribe(
        () => {
          this.loadLabelInCard(); // Load lại danh sách nhãn đã chọn
          this.loadLabelInCard2();
        },
        error => {
          console.error('Error adding label to card:', error);
        }
      );
    } else {
      // Xóa nhãn khỏi thẻ khi checkbox được unchecked
      this.selectedLabels.splice(index, 1);
      // @ts-ignore
      this.cardLabelService.removeLabelFromCard(this.selectedCard.id, label.id).subscribe(
        () => {
          this.loadLabelInCard(); // Load lại danh sách nhãn đã chọn
          this.loadLabelInCard2();
        },
        error => {
          console.error('Error removing label from card:', error);
        }
      );
    }
  }

  // hiện thị label đã thêm vào card
  loadLabelInCard() {
    if (this.selectedCard) {
      this.cardLabelService.getLabelByCardId(this.selectedCard.id).subscribe(
        (labels: Label[]) => {
          this.originalLabel = labels;
          // Tự động check các checkbox của nhãn đã chọn
          this.selectedLabels = labels.map(label => label.id);
        },
        error => {
          console.error('Error loading labels:', error);
        }
      );
    }
  }


  openUpdateLabelModal(label: Label): void {
    // Mở modal cập nhật nhãn và tải chi tiết nhãn
    this.labelService.getLabelDetail(label.id!).subscribe(labelDetail => {
      this.selectedLabel = labelDetail;
      this.showUpdateLabelSection = true;
    });
  }

  // Xoá label khỏi card
  removeLabel(label: Label) {
    if (this.selectedCard) {
      this.cardLabelService.removeLabelFromCard(this.selectedCard.id, label.id).subscribe({
        next: () => {
          this.originalLabel = this.originalLabel.filter(l => l.id !== label.id);
        },
        error: (error) => {
          console.error('Error removing label:', error);
        }
      })
    }
  }

  // hiện thị label trong board
  loadLabelByBoard(boardId: string) {
    this.labelService.getLabelByBoardId(boardId).subscribe(
      (labels: Label[]) => {
        this.labels = labels;
      },
      error => {
        console.error('Error loading labels', error);

      }
    );
  }

  // chọn màu
  selectColor(selectedColor: string) {
    this.color = selectedColor;
  }

  // thêm label mới(add label vào card_label)
  addLabel(): void {
    if (this.labelName.trim() && this.color.trim()) {
      const newLabel: Label = {
        id: null,
        boardId: this.boardId,
        color: this.color,
        name: this.labelName
      };

      this.labelService.addLabel(newLabel).pipe(
        tap((createdLabel: Label) => {
          if (!createdLabel.id) {
            throw new Error('ID của label vừa tạo là null hoặc undefined');
          }
        }),
        mergeMap((createdLabel: Label) => {
          if (this.selectedCard && createdLabel.id) {
            return this.cardLabelService.addCardLabel(this.selectedCard.id, [createdLabel.id]);

          } else {
            return EMPTY; // Trả về một observable rỗng nếu không có card hoặc ID của label không hợp lệ
          }
        })
      ).subscribe({
        next: () => {
          this.loadLabelInCard();
          this.loadLabelInCard2();
          this.showAddLabelSection = false;
          if (this.boardId) {
            this.loadLabelByBoard(this.boardId);
          }
        },
        error: (error) => {
          console.error('Lỗi khi thêm label hoặc gán label cho thẻ:', error);
        }
      });
    } else {
      console.warn('Tên label hoặc màu sắc còn trống.');
    }
  }


  selectColorUp(color: string): void {
    if (this.selectedLabel) {
      this.selectedLabel.color = color;
    }
  }

  updateLabel(): void {
    if (this.selectedLabel) {
      this.labelService.updateLabel(this.selectedLabel).subscribe({
        next: (updatedLabel: Label) => {
          this.selectedLabel = updatedLabel; // Cập nhật selectedLabel với giá trị từ server

          this.loadLabelInCard();
          this.loadLabelInCard2();
          if (this.boardId) {
            this.loadLabelByBoard(this.boardId);
          }
          this.showUpdateLabelSection = false;

        },
        error: (error) => {
          console.error('Error updating label:', error);
        }
      });
    } else {
      console.error('Selected label is null');
    }
  }


  deleteLabel(): void {
    this.actionToConfirm = this.performDeleteLabel.bind(this);
    this.messageBoxService.showMessage('Việc này sẽ xóa nhãn này khỏi tất cả các thẻ. Không có hoàn tác.');
    this.showUpdateLabelSection = false;
  }

  performDeleteLabel(): void {
    if (this.selectedLabel) {
      this.cardLabelService.removeLabelFromLabelId(this.selectedLabel.id).pipe(
        switchMap(() => {
          // Remove the label from the originalLabel array
          // @ts-ignore
          this.originalLabel = this.originalLabel.filter(l => l.id !== this.selectedLabel.id);
          // If the label was successfully removed from the card, delete the label itself
          if (this.selectedLabel) {
            return this.labelService.deleteLabel(this.selectedLabel.id);
          } else {
            throw new Error('Selected label is null');
          }
        })
      ).subscribe({
        next: () => {
          if (this.boardId) {
            this.loadLabelByBoard(this.boardId);
          }
        },
        error: (error) => {
          console.error('Error deleting label:', error);
        }
      });
    } else {
      console.error('Selected card or label is null');
    }
  }



  onConfirm() {
    if (this.actionToConfirm) {
      this.actionToConfirm();
    }
  }

  onCancel() {
    console.log('Cancelled');
  }

  getContrastingColor(color: string): string {
    // Chuyển đổi mã màu thành RGB
    const rgb = parseInt(color.slice(1), 16);
    const r = (rgb >> 16) & 0xff;
    const g = (rgb >> 8) & 0xff;
    const b = (rgb >> 0) & 0xff;

    // Tính độ sáng theo công thức YIQ
    const yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000;

    // Trả về màu trắng hoặc đen dựa trên độ sáng
    return (yiq >= 128) ? 'black' : 'white';
  }

  // Nếu dueDate là string, cần convert nó thành Date object
  isOverdue(): boolean {
    if (this.selectedCard && this.selectedCard.dueDate) {
      const currentDate = new Date();
      return new Date(this.selectedCard.dueDate) < currentDate && !this.selectedCard.completed;
    }
    return false;
  }

  // Phương thức kiểm tra trạng thái hoàn tất
  get isCompleted(): boolean {
    // @ts-ignore
    return this.selectedCard.completed;

  }

// Điều kiện ngày bắt đầu phải nhỏ hơn ngày kết thúc
  private formatDateForInput(date: string | null, isDateOnly: boolean): string | null {
    if (!date) {
      return null;
    }
    const d = new Date(date);
    const pad = (n: number) => n.toString().padStart(2, '0');
    if (isDateOnly) {
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
    } else {
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
    }
  }


// Function to discard changes and revert textarea to the original value
  discardChanges(): void {
    // Reset textarea content to the original value
    if (this.selectedCard) {
      this.selectedCard.description = this.originalDescription;
    }
  }


  toggleMembersSection(): void {
    this.showMembersSection = !this.showMembersSection;

  }

  toggleMembersSection2(): void {
    this.showMembersSection2 = !this.showMembersSection2;

  }

  toggleLabelSection(): void {
    this.showLabelsSection = !this.showLabelsSection;
  }

  toggleAddLabelSection(): void {
    this.showAddLabelSection = !this.showAddLabelSection;
  }

  toggleChecklistSection(): void {
    this.showChecklistSection = !this.showChecklistSection;
  }

  toggleAttachmentSection(): void {
    this.showAttachment = !this.showAttachment;
  }

  toggleDaySection(): void {
    this.showDaySection = !this.showDaySection;
  }

  toggleDescriptionSection(): void {
    this.showDescriptionSection = !this.showDescriptionSection;
  }

  toggleAddItemInput(selectedChecklistId: string): void {
    this.showAddItemInput[selectedChecklistId] = !this.showAddItemInput[selectedChecklistId];
    if (!this.showAddItemInput[selectedChecklistId]) {
      this.newChecklistItemName[selectedChecklistId] = ''; // Xóa tên item khi ẩn input
    }
  }

  toggleUpdateLabelSection(label?: Label): void {
    this.showUpdateLabelSection = !this.showUpdateLabelSection;
    if (label) {
      this.selectedLabel = label;
      this.labelName = label.name;
      this.color = label.color;
    }
  }

  // RestoreCard

}
