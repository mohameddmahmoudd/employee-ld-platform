import { Routes } from '@angular/router';
import { LoginComponent } from './login-signup-page/login-component/login-component';
import { SignupComponent } from './login-signup-page/signup-component/signup-component';

export const routes: Routes = [
    {path: "login", component: LoginComponent},
    {path: "signup", component: SignupComponent}
];
