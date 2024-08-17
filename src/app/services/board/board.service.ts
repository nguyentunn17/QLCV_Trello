import { Injectable } from '@angular/core';
import {Constant} from "../../core/config/constant";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Domain} from "../../core/domain/domain";
import { Observable } from 'rxjs';
import {Board} from "../../core/model/board/board";

@Injectable({
  providedIn: 'root'
})
export class BoardService {
  private baseURL = `${Constant.BASE_URL}`;
  private  domain = `${Domain.BOARD}`;

  constructor(private httpClient: HttpClient) { }
  getListBoard(): Observable<Board[]> {
      return this.httpClient.get<Board[]>(`${this.baseURL}/${this.domain}/get-all`);
  }

  getBoardById(id: number): Observable<Board>{
    return this.httpClient.get<Board>(`${this.baseURL}/${this.domain}/${id}`);
  }
  addMembersToBoard(boardId: number, userIds: number[]): Observable<string> {
    return this.httpClient.post<string>(`${this.baseURL}/${this.domain}/${boardId}/members`, {
      userIds
    });
  }
  updateBoard(boardId: number, boardName: string):Observable<Object>{
    let params = new HttpParams().set('newName', boardName);
    return this.httpClient.put(`${this.baseURL}/${this.domain}/update/${boardId}`, null, { params });
  }

  addBoardWithMembers(board: Board, userIds: number[]): Observable<Board> {
    const requestPayload = {
      board: board,
      userIds: userIds
    };
    return this.httpClient.post<Board>(`${this.baseURL}/${this.domain}/add-all`, requestPayload);
  }

  searchBoard(param: HttpParams): Observable<any>{
    return this.httpClient.get(`${this.baseURL}/${this.domain}/search`, {params: param});
  }
  locBoard(param: HttpParams): Observable<any>{
    return this.httpClient.get(`${this.baseURL}/${this.domain}/loc`, {params: param});
  }

  closeBoard(boardId: number): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.baseURL}/${this.domain}/close/${boardId}`, {});
  }

  restoreBoard(boardId: number): Observable<{ message: string }> {
    return this.httpClient.post<{ message: string }>(`${this.baseURL}/${this.domain}/restore/${boardId}`, {})
  }

  leaveBoard(boardId: number): Observable<{ message: string }> {
    return this.httpClient.delete<{ message: string }>(`${this.baseURL}/${this.domain}/leave/${boardId}`, {})
  }

  removeUserFromBoard(boardId: number, userId: number): Observable<{ message: string }> {
    return this.httpClient.delete<{ message: string }>(`${this.baseURL}/${this.domain}/remove/${boardId}/${userId}`, {})
  }

}
