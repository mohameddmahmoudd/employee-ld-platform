import { Routes } from '@angular/router';
import { LoginComponent } from './login-signup-page/login-component/login-component';
import { SignupComponent } from './login-signup-page/signup-component/signup-component';
import { Home } from './home/home';
import { dashboardGuard } from './guards/dashboard-guard';
import { EmptyComponent } from './empty-component/empty-component';

// TODO: lazy load
export const routes: Routes = [
    { path: "login", component: LoginComponent },
    { path: "signup", component: SignupComponent },
    {
        path: "", component: Home,
        /*canActivate: [dashboardGuard],*/
        children: [
            { path: 'profile', component: EmptyComponent },
            { path: 'learnings', component: EmptyComponent }, //guard this
            { path: 'wikis', component: EmptyComponent }, //guard this
            { path: 'blogs', component: EmptyComponent },
            { path: 'career-package', component: EmptyComponent }, //guard this
            { path: 'employee-management', component: EmptyComponent }, //guard this
            { path: 'approvals', component: EmptyComponent }, //guard this
        ]
    },
    { path: "**", redirectTo: "" },

];
