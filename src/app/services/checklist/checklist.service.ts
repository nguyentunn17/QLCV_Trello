import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Checklist } from '../../core/model/checklist/checklist';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';
import {Lists} from "../../core/model/lists/lists";

@Injectable({
  providedIn: 'root'
})
export class ChecklistService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.CHECKLIST}`;

  constructor(private httpClient: HttpClient) {}



  getChecklistDetail(id: number): Observable<Checklist> {
    return this.httpClient.get<Checklist>(`${this.baseURL}/${this.domain}/detail/${id}`);
  }

  addChecklist(checklist: Checklist): Observable<Checklist> {
    return this.httpClient.post<Checklist>(`${this.baseURL}/${this.domain}/add`, checklist);
  }

  getChecklistByCardId(cardId: string): Observable<Checklist[]> {
    return this.httpClient.get<Checklist[]>(`${this.baseURL}/${this.domain}/card/${cardId}`);
  }

  updateChecklist(checklist: Checklist): Observable<any> {
    return this.httpClient.put<any>(`${this.baseURL}/${this.domain}/update/${checklist.id}`, checklist);
  }

  deleteChecklist(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
