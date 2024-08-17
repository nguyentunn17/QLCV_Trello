import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Label } from '../../core/model/label/label';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';
import {Card} from "../../core/model/card/card";

@Injectable({
  providedIn: 'root'
})
export class LabelService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.LABEL}`;

  constructor(private httpClient: HttpClient) {}

  getLabelByBoardId(boardId :any): Observable<Label[]>{
    const params = `${this.baseURL}/${this.domain}/get-by-board?boardId=${boardId}`;
    return this.httpClient.get<Label[]>(params);
  }

  addLabel(label: Label): Observable<Label> {
    return this.httpClient.post<Label>(`${this.baseURL}/${this.domain}/add`, label);
  }

  updateLabel(label: Label): Observable<Label> {
    return this.httpClient.put<Label>(`${this.baseURL}/${this.domain}/update/${label.id}`, label);
  }

  getLabelDetail(id: number): Observable<Label> {
    return this.httpClient.get<Label>(`${this.baseURL}/${this.domain}/detail/${id}`);
  }

  deleteLabel(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
