import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CardLabel } from '../../core/model/card-label/card-label';
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';
import {Label} from "../../core/model/label/label";

@Injectable({
  providedIn: 'root'
})
export class CardLabelService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.CARDLABEL}`;

  constructor(private httpClient: HttpClient) {}

  addCardLabel(cardId: number, labelIds: number[]): Observable<void> {
    const reqestPayLoad = { labelIds};
    return this.httpClient.post<void>(`${this.baseURL}/${this.domain}/add-color?cardId=${cardId}`, reqestPayLoad);
  }
  getLabelByCardId(cardId :string): Observable<Label[]>{
    const params = `${this.baseURL}/${this.domain}/${cardId}/get-by-card`;
    return this.httpClient.get<Label[]>(params);
  }
  removeLabelFromCard(cardId: string, labelId: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/${cardId}/remove-label/${labelId}`);
  }

  removeLabelFromLabelId( labelId: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/remove-label/${labelId}`);
  }


}

