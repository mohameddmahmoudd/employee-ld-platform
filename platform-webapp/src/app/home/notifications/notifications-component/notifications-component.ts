import { Component, input } from '@angular/core';

@Component({
  selector: 'app-notifications-component',
  imports: [],
  templateUrl: './notifications-component.html',
  styleUrl: './notifications-component.css',
})
export class NotificationsComponent {
  pageIndex = input.required<number>();
  pageSize = input.required<number>();
}
