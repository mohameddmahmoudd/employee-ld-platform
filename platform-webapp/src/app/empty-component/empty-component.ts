import { ChangeDetectionStrategy, Component, inject, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatDialogConfig } from '@angular/material/dialog';
import { LoginComponent } from '../login-signup-page/login-component/login-component';

@Component({
  selector: 'app-empty-component',
  imports: [],
  templateUrl: './empty-component.html',
  styleUrls: ['./empty-component.css'],
})
export class EmptyComponent {
  readonly dialog = inject(MatDialog);

  openDialog(event: MouseEvent) {
    const buttonRect = (event.target as HTMLElement).getBoundingClientRect();

    const dialogConfig = new MatDialogConfig();
    dialogConfig.position = {
      top: `${buttonRect.bottom + 10}px`,
      left: `${buttonRect.left}px`,
    };

    // dialogConfig.hasBackdrop = false;
    // Optionally, set width and height
    dialogConfig.panelClass = 'speech-overlay'
    dialogConfig.width = '400px';
    dialogConfig.height = '300px';
    dialogConfig.disableClose = false;

    this.dialog.open(DialogElementsExampleDialog, dialogConfig);

  }
}
// var(--mat-sys-surface)
@Component({
  selector: 'dialog-elements-example-dialog',
  template: `
    <svg class="caret" width="20" height="10" viewBox="0 0 20 10" aria-hidden="true">
      <path  d="M0 10 L10 0 L20 10 Z" fill="var(--mat-sys-surface)"/>
    </svg>
    <h2 mat-dialog-title>Notifications</h2>
    <mat-dialog-content >  
      This dialog showcases the title, close, content and actions elements.</mat-dialog-content>
    <mat-dialog-actions>
      <button matButton mat-dialog-close>Close</button>
    </mat-dialog-actions>
  `,
  styleUrls: ['./empty-component.css'],
  imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose, MatButtonModule],
  // encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DialogElementsExampleDialog { }

