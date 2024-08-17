import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChecklistItem } from '../../core/model/checklist-item/checklist-item';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';
import {Checklist} from "../../core/model/checklist/checklist";

@Injectable({
  providedIn: 'root'
})
export class ChecklistItemService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.CHECKLISTITEM}`;

  constructor(private httpClient: HttpClient) {}

  getChecklistItemByChecklistId(checklistId: string): Observable<ChecklistItem[]> {
    return this.httpClient.get<ChecklistItem[]>(`${this.baseURL}/${this.domain}/checklist/${checklistId}`);
  }

  addChecklistItem(checklistItem: ChecklistItem): Observable<ChecklistItem> {
    return this.httpClient.post<ChecklistItem>(`${this.baseURL}/${this.domain}/add`, checklistItem);
  }

  updateChecklistItem(checklistItem: ChecklistItem): Observable<void> {
    return this.httpClient.put<void>(`${this.baseURL}/${this.domain}/update/${checklistItem.id}`, checklistItem);
  }

}
