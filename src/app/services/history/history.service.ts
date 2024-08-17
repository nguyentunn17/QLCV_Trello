import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { History } from '../../core/model/history/history';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.HISTORY}`;

  constructor(private httpClient: HttpClient) {}

  getAllHistories(): Observable<History[]> {
    return this.httpClient.get<History[]>(`${this.baseURL}/${this.domain}/get-all`);
  }

  getHistoryDetail(id: number): Observable<History> {
    return this.httpClient.get<History>(`${this.baseURL}/${this.domain}/detail/${id}`);
  }

  addHistory(history: History): Observable<History> {
    return this.httpClient.post<History>(`${this.baseURL}/${this.domain}/add`, history);
  }

  updateHistory(id: number, history: History): Observable<History> {
    return this.httpClient.put<History>(`${this.baseURL}/${this.domain}/update/${id}`, history);
  }

  deleteHistory(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
