import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageBoxService {
  private messageSubject = new Subject<string>();
  private isVisibleSubject = new Subject<boolean>();

  message$ = this.messageSubject.asObservable();
  isVisible$ = this.isVisibleSubject.asObservable();

  showMessage(message: string) {
    this.messageSubject.next(message);
    this.isVisibleSubject.next(true);
  }

  hideMessage() {
    this.isVisibleSubject.next(false);
  }
}
