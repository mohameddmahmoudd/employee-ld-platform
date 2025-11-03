import { Component, inject, signal, viewChild, viewChildren } from '@angular/core';
import { FieldComponent } from "../field-component/field-component";
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../auth-service';
import { SignupRequestDto } from '../model/SignupRequestDto';
import { handleAuthResponse, validateForm } from '../util';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-signup-component',
  imports: [
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    FieldComponent,
    MatProgressSpinnerModule
  ],
  templateUrl: './signup-component.html',
  styleUrl: '../login-signup-page.css',
})
export class SignupComponent {
  authService = inject(AuthService);
  signupForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    password: new FormControl('', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]),
    firstname: new FormControl('', [Validators.required, Validators.maxLength(50)]),
    lastname: new FormControl('', [Validators.required, Validators.maxLength(50)]),
  });
  fieldComponents = viewChildren(FieldComponent);

  errorState = signal(false);
  errorMessage = signal('');
  isLoading = signal(false);

  private updateErrorMessage() {
    this.errorMessage.set("Please correct the errors in the form before submitting.");
  }

  private sendAuthRequest() {
    const dto: SignupRequestDto = {
      username: this.signupForm.controls["username"].value!,
      password: this.signupForm.controls["password"].value!,
      firstname: this.signupForm.controls["firstname"].value!,
      lastname: this.signupForm.controls["lastname"].value!
    }
    return this.authService.signup(dto);
  }

  tryToAuthenticate(event: Event) {
    this.isLoading.set(true);
    let newErrorState = validateForm(this.signupForm, this.fieldComponents());
    this.errorState.set(newErrorState);
    if (this.errorState()) {
      this.updateErrorMessage();
    } else {
      let request = this.sendAuthRequest();
      handleAuthResponse(request, this);
    }
    event.stopPropagation();
  }

}
