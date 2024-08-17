import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../core/model/comment/comment'; // Đảm bảo đường dẫn đúng đến model Comment
import { Constant } from '../../core/config/constant';
import { Domain } from '../../core/domain/domain';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private baseURL = `${Constant.BASE_URL}`;
  private domain = `${Domain.COMMENT}`;

  constructor(private httpClient: HttpClient) {}

  getAllComments(): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.baseURL}/${this.domain}/get-all`);
  }

  getCommentDetail(id: number): Observable<Comment> {
    return this.httpClient.get<Comment>(`${this.baseURL}/${this.domain}/detail/${id}`);
  }

  addComment(comment: Comment): Observable<Comment> {
    return this.httpClient.post<Comment>(`${this.baseURL}/${this.domain}/add`, comment);
  }

  updateComment(id: number, comment: Comment): Observable<Comment> {
    return this.httpClient.put<Comment>(`${this.baseURL}/${this.domain}/update/${id}`, comment);
  }

  deleteComment(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${this.domain}/delete/${id}`);
  }
}
