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
  
}