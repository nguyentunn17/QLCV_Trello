import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CardAssignment } from '../../core/model/card-assignment/card-assignment';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';

@Injectable({
  providedIn: 'root'
})
export class CardAssignmentService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.CARDASSIGNMENT}`;

  constructor(private httpClient: HttpClient) {}

  // getAllCardAssignments(): Observable<CardAssignment[]> {
  //   return this.httpClient.get<CardAssignment[]>(`${this.baseURL}/${this.domain}/get-all`);
  // }
  //
  // getCardAssignmentDetail(id: number): Observable<CardAssignment> {
  //   return this.httpClient.get<CardAssignment>(`${this.baseURL}/${this.domain}/detail/${id}`);
  // }
  //
  // addCardAssignment(cardAssignment: CardAssignment): Observable<CardAssignment> {
  //   return this.httpClient.post<CardAssignment>(`${this.baseURL}/${this.domain}/add`, cardAssignment);
  // }
  //
  // updateCardAssignment(id: number, cardAssignment: CardAssignment): Observable<CardAssignment> {
  //   return this.httpClient.put<CardAssignment>(`${this.baseURL}/${this.domain}/update/${id}`, cardAssignment);
  // }
  //
  // deleteCardAssignment(id: number): Observable<void> {
  //   return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  // }
}
