import { Component, inject, signal, viewChild, viewChildren } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { FieldComponent } from "../field-component/field-component";
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../auth-service';
import { LoginRequestDto } from '../model/LoginRequestDto';
import { handleAuthResponse, validateForm } from '../util';
import { MatProgressSpinnerModule, MatSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-login-component',
  imports: [
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    FieldComponent,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login-component.html',
  styleUrl: '../login-signup-page.css',
})
export class LoginComponent {
  authService = inject(AuthService);
  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  });
  fieldComponents = viewChildren(FieldComponent);
  errorState = signal(false);
  errorMessage = signal('');
  isLoading = signal(false);

  private updateErrorMessage() {
    this.errorMessage.set("Please correct the errors in the form before submitting.");
  }

  private sendAuthRequest() {
    const dto: LoginRequestDto = {
      username: this.loginForm.controls["username"].value!,
      password: this.loginForm.controls["password"].value!
    }
    return this.authService.login(dto);
  }

  tryToAuthenticate(event: Event) {
    this.isLoading.set(true);
    let newErrorState = validateForm(this.loginForm, this.fieldComponents());
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
