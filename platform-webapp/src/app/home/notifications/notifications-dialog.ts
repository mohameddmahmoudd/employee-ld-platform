import { Component, ViewEncapsulation } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { EmptyComponent } from "../../empty-component/empty-component";

@Component({
  selector: 'notifications-dialog',
  template: `
    <svg class="caret" width="20" height="10" viewBox="0 0 20 10" aria-hidden="true">
      <path  d="M0 10 L10 0 L20 10 Z" fill="var(--mat-sys-surface)"/>
    </svg>
    <h1 mat-dialog-title>Notifications</h1>
    <mat-dialog-content >  
    <app-empty-component/>
    </mat-dialog-content> 
    <mat-dialog-actions>
      <button matButton mat-dialog-close>Close</button>
    </mat-dialog-actions>
  `,
  styles: [`
    .caret {
      position: absolute;
      top: -8px;
      left: var(--mat-dialog-container-shape, var(--mat-sys-corner-extra-large, 4px));
    }
    @media (max-width: 600px) {
      .caret {
        visibility: hidden;
        /* left: unset;
        right: var(--mat-dialog-container-shape, var(--mat-sys-corner-extra-large, 4px)); */
      }
    }
  `],
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButtonModule,
    EmptyComponent
  ],
  // encapsulation: ViewEncapsulation.None,
})
export class NotificationsDialog { }