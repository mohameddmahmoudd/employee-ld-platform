import { Component, inject } from '@angular/core';
import { AuthService } from '../auth-service';
import { GuestDashboardComponent } from './guest-dashboard-component/guest-dashboard-component';
import { EmployeeDashboardComponent } from './employee-dashboard-component/employee-dashboard-component';
import { NgComponentOutlet } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [NgComponentOutlet],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  authService = inject(AuthService);
  getDashboardComponent(){
    return (this.authService.user?.roles.has({role: "EMPLOYEE"}))? EmployeeDashboardComponent : GuestDashboardComponent;
  }
  getNavBarComponent(){
    return (this.authService.user?.roles.has({role: "EMPLOYEE"}))? EmployeeDashboardComponent : GuestDashboardComponent;
  }
}
