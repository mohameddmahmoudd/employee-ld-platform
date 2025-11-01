import { Component, signal, viewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { FieldComponent } from "../field-component/field-component";
import { Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-login-component',
  imports: [
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    FieldComponent
  ],
  templateUrl: './login-component.html',
  styleUrl: '../login-signup-page.css',
})
export class LoginComponent {
  Validators = Validators;
  
  username = viewChild.required<FieldComponent>('username');
  password = viewChild.required<FieldComponent>('password');
  allFields = [this.username, this.password];

  errorState = signal(false);
  tryToAuthenticate(event: MouseEvent) {
    let errorStateLoop = false;
    this.allFields.forEach(field => {
      let isInError = field().updateFieldErrorMessage();
      errorStateLoop ||= isInError;
      console.log(field);
    });
    this.errorState.set(errorStateLoop);
    if (errorStateLoop) return;

    console.log(this.username().field.value);
    console.log(this.password().field.value);
    // placeholder
    event.stopPropagation();
  }
}
