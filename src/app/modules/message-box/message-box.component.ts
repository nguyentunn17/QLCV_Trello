import { Component, EventEmitter, Output } from '@angular/core';

import { Subscription } from 'rxjs';
import {MessageBoxService} from "../../services/message-box/message-box.service";

@Component({
  selector: 'app-message-box',
  templateUrl: './message-box.component.html',
  styleUrls: ['./message-box.component.css']
})
export class MessageBoxComponent {
  message: string = '';
  isVisible: boolean = false;
  private subscription: Subscription;

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  constructor(private messageBoxService: MessageBoxService) {
    this.subscription = this.messageBoxService.message$.subscribe(
      message => this.message = message
    );
    this.subscription.add(
      this.messageBoxService.isVisible$.subscribe(
        isVisible => this.isVisible = isVisible
      )
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  onConfirm() {
    this.confirm.emit();
    this.messageBoxService.hideMessage();
  }

  onCancel() {
    this.cancel.emit();
    this.messageBoxService.hideMessage();
  }
}
