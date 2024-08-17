import { Injectable } from '@angular/core';
import {Constant} from "../../core/config/constant";
import {Domain} from "../../core/domain/domain";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Card} from "../../core/model/card/card";
import {User} from "../../core/model/user/user";

@Injectable({
  providedIn: 'root'
})
export class CardService {
  private baseURL = `${Constant.BASE_URL}`;
  private  domain = `${Domain.CARD}`;
  constructor(private httpClient: HttpClient) { }

  addCard(card: Card): Observable<Card> {
    return this.httpClient.post<Card>(`${this.baseURL}/${this.domain}/add`, card);
  }

  updateCard( card: Card): Observable<Card> {
    return this.httpClient.put<Card>(`${this.baseURL}/${this.domain}/update/${card.id}`, card);
  }

  deleteCard(cardId: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${cardId}`);
  }

  addMemberToCard(cardId: number, userIds: number[]): Observable<string> {
    const requestPayload = { userIds: userIds };
    return this.httpClient.post<string>(`${this.baseURL}/${this.domain}/add-member?cardId=${cardId}`, requestPayload);
  }

  removeMemberFromCard(cardId: string, userId: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/${cardId}/remove-member/${userId}`);
  }

  getMembersInCard(cardId: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`${this.baseURL}/${this.domain}/${cardId}/members`);
  }

  archiveCard(cardId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.baseURL}/${this.domain}/${cardId}/archive`, {});
  }

  restoreCard(cardId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.baseURL}/${this.domain}/${cardId}/restore`, {});
  }
  getCardsByListId(listId: any): Observable<Card[]> {
    const params = `${this.baseURL}/${this.domain}/get-by-list?listId=${listId}`; // Đảm bảo rằng endpoint trùng khớp với API bên phía server
    return this.httpClient.get<Card[]>(params);
  }
}
