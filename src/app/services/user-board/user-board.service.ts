import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { UserBoard } from '../../core/model/user-board/user-board';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';

@Injectable({
  providedIn: 'root'
})
export class UserBoardService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.USERBOARD}`;

  constructor(private httpClient: HttpClient) {}

  getUserRole(boardId: number): Observable<string> {
    return this.httpClient.get<{ role: string }>(`${this.baseURL}/${this.domain}/role`, {
      params: { boardId: boardId.toString() }
    }).pipe(map(response => response.role));
  }

  getAllUserBoards(): Observable<UserBoard[]> {
    return this.httpClient.get<UserBoard[]>(`${this.baseURL}/${this.domain}/get-all`);
  }

  getUserBoardDetail(id: number): Observable<UserBoard> {
    return this.httpClient.get<UserBoard>(`${this.baseURL}/${this.domain}/detail/${id}`);
  }

  addUserBoard(userBoard: UserBoard): Observable<UserBoard> {
    return this.httpClient.post<UserBoard>(`${this.baseURL}/${this.domain}/add`, userBoard);
  }

  updateUserBoard(id: number, userBoard: UserBoard): Observable<UserBoard> {
    return this.httpClient.put<UserBoard>(`${this.baseURL}/${this.domain}/update/${id}`, userBoard);
  }

  deleteUserBoard(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
