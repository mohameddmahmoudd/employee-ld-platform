import { Component } from '@angular/core';
import { FieldComponent } from "../field-component/field-component";
import { Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-signup-component', 
  imports: [
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
  tryToAuthenticate(event: MouseEvent) {
    // placeholder
    event.stopPropagation();
  }

}
