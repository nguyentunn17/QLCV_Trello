import { Injectable } from '@angular/core';
import {Constant} from "../../core/config/constant";
import {Domain} from "../../core/domain/domain";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Attachment} from "../../core/model/attachment/attachment";

@Injectable({
  providedIn: 'root'
})
export class AttachmentService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain =  `${Domain.ATTACHMENT}`;

  constructor(private httpClient: HttpClient) { }

  getAll(): Observable<Attachment[]>{
    return this.httpClient.get<Attachment[]>(`${this.baseURL}/${this.domain}/get-all`);
  }
  getAttachmentById(id: number): Observable<Attachment>{
    return this.httpClient.get<Attachment>(`${this.baseURL}/${this.domain}/deatail/${id}`);
  }
  updateAttachment(id: number, attachment: FormData): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}/${this.domain}/update/${id}`,attachment)
  }
  addAttachment(attachment: Attachment): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}/${this.domain}/add`,attachment);
}
  deleteAttachment(id: number): Observable<Object>{
    return this.httpClient.get(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
