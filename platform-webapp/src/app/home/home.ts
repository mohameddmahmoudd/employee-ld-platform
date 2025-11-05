import { Component, inject } from '@angular/core';
import { AuthService } from '../auth-service';
import { GuestDashboardComponent } from './dashboard/guest-dashboard-component/guest-dashboard-component';
import { EmployeeDashboardComponent } from './dashboard/employee-dashboard-component/employee-dashboard-component';
import { NgComponentOutlet } from '@angular/common';
import { NavBarComponent } from './nav-bar-component/nav-bar-component';
import { RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-home',
  imports: [NgComponentOutlet, NavBarComponent, RouterModule, RouterOutlet],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  authService = inject(AuthService);
  getDashboardComponent(){
    return (this.authService.user?.roles.has("EMPLOYEE"))? EmployeeDashboardComponent : GuestDashboardComponent;
  }
}
