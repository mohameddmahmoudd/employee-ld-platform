import { Component, signal, viewChild } from '@angular/core';
import { FieldComponent } from "../field-component/field-component";
import { Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-signup-component', 
  imports: [
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    FieldComponent
  ],
  templateUrl: './signup-component.html',
  styleUrl: './signup-component.css',
})
export class SignupComponent {
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
