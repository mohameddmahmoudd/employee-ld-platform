import { FormGroup } from "@angular/forms";
import { FieldComponent } from "./field-component/field-component";
import { SignupComponent } from "./signup-component/signup-component";
import { LoginComponent } from "./login-component/login-component";
import { HttpErrorResponse, HttpRequest, HttpResponse, HttpResponseBase, HttpStatusCode } from "@angular/common/http";
import { Observable } from "rxjs";
import { LoginRequestDto } from "./model/LoginRequestDto";
import { LoginResponseDto } from "./model/LoginResponseDto";

export function validateForm(form: FormGroup, fieldComponents: readonly FieldComponent[]) {
    form.markAllAsTouched();
    fieldComponents.forEach(field => {
        field.updateFieldErrorMessage();
    });
    let errorState = form.invalid;
    return errorState;
}

/* TODO: this assumes signup returns a loginDTO, right now signup returns nothing */
export function handleAuthResponse(request: Observable<LoginResponseDto>, component: LoginComponent | SignupComponent) {
    request.subscribe({
        next: (response: LoginResponseDto) => {
            /* TODO: make component error-setting a function later */
            component.errorState.set(false);
            component.errorMessage.set('');
            console.log(response);
            /* TODO: make authSuccess function and call it, it will be responsible for 
             setting any variables in authservice. Not our issue here */
            /* TODO: CREATE SETTER AND GETTER */
            component.authService.user = {
                ...response.user,
                roles: response.user.roles ?? [] // will it always be an empty array? do I need ??[] here?
            };
            component.authService.userToken = response.token;
            component.authService.isAuthenticated.set(true);

            component.authService.router.navigate(['/dashboard']);
        },
        error: (error: HttpErrorResponse) => {
            console.log(error);
            component.authService.isAuthenticated.set(false);
            component.updateErrorMessage(error);
        }
    })
}