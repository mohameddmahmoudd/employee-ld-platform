import { Component, inject } from '@angular/core';
import { NgIf } from '@angular/common';
import { GuestDashboardComponent } from './guest-dashboard-component/guest-dashboard-component';
import { EmployeeDashboardComponent } from './employee-dashboard-component/employee-dashboard-component';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-dashboard-component',
  standalone: true,
  imports: [NgIf, GuestDashboardComponent, EmployeeDashboardComponent],
  template: `
    <div class="dashboard-wrapper">
      <app-guest-dashboard-component
        *ngIf="shouldShowGuestDashboard(); else employeeDashboard"
      />
      <ng-template #employeeDashboard>
        <app-employee-dashboard-component />
      </ng-template>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      height: 100%;
      box-sizing: border-box;
      background: var(--dashboard-surface, #f6f7fb);
      padding: clamp(1rem, 3vw, 2rem);
    }

    .dashboard-wrapper {
      height: 100%;
      min-height: 100%;
      width: 100%;
    }
  `]
})

export class DashboardComponent 
{
    private readonly authService = inject(AuthService);

    shouldShowGuestDashboard() {
        const user = this.authService.user;
        if (!user) {
            return true;
        }

        const hasGuestRole = this.authService.checkRole(user, "GUEST");
        
        if (!hasGuestRole) {
            return false;
        }

        const roles = user.roles ?? [];
        const hasAnotherRole = roles.some(role => role !== "GUEST");
        return !hasAnotherRole;
    }

}
