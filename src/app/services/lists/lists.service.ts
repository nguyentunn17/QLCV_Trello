import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lists } from '../../core/model/lists/lists';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';

@Injectable({
  providedIn: 'root'
})
export class ListService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.LISTS}`;

  constructor(private httpClient: HttpClient) {}

  addList(lists: Lists): Observable<Lists> {
    return this.httpClient.post<Lists>(`${this.baseURL}/${this.domain}/add`, lists);
  }
  getListsByBoardId(boardId: string): Observable<Lists[]> {
    return this.httpClient.get<Lists[]>(`${this.baseURL}/${this.domain}/board/${boardId}`);
  }

  updateList(lists: Lists): Observable<void> {
    return this.httpClient.put<void>(`${this.baseURL}/${this.domain}/update/${lists.id}`, lists);
  }

  deleteList(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }

  archiveList(listId: number, userId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.baseURL}/${this.domain}/${listId}/archive?userId=${userId}`, null);
  }

  restoreList(listId: number, userId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.baseURL}/${this.domain}/${listId}/restore?userId=${userId}`, null);
  }
}
