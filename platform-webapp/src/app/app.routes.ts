import { Routes } from '@angular/router';
import { LoginComponent } from './login-signup-page/login-component/login-component';
import { SignupComponent } from './login-signup-page/signup-component/signup-component';
import { Home } from './home/home';
import { dashboardGuard } from './guards/dashboard-guard';

// TODO: lazy load
export const routes: Routes = [
    { path: "login", component: LoginComponent },
    { path: "signup", component: SignupComponent },
    { path: "dashboard", component: Home, canActivate: [dashboardGuard] },
    { path: "**", redirectTo: "dashboard" }
];
