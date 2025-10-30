import { Component } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { FieldComponent } from "../field-component/field-component";
import { Form, FormControl, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';

@Component({
  selector: 'app-login-component',
  imports: [
    MatInputModule,
    MatButtonModule,
    RouterLink,
    FieldComponent
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  Validators = Validators;
  tryToAuthenticate(event: MouseEvent) {
    // placeholder
    event.stopPropagation();
  }
}
