import { Component, inject } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../../../service/auth-service';

@Component({
  selector: 'app-employee-dashboard-component',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './employee-dashboard-component.html',
  styleUrl: './employee-dashboard-component.css',
})
export class EmployeeDashboardComponent {
  private readonly authService = inject(AuthService);
  readonly user = this.authService.user;

  readonly quickStats = [
    { label: 'Completed Learnings', value: 8, meta: '+2 this month' },
    { label: 'Open Approvals', value: 3, meta: '2 pending manager review' },
    { label: 'Unread Notifications', value: 7, meta: 'Stay up to date' },
    { label: 'Current Projects', value: 4, meta: '1 ending soon' }
  ];

  readonly actionItems = [
    {
      title: 'Finalize onboarding checklist',
      description: 'Upload your signed contracts and confirm your mentor selection.',
      cta: 'Go to checklist'
    },
    {
      title: 'Schedule career conversation',
      description: 'Sync with your manager to discuss goals for this quarter.',
      cta: 'Book 30 min slot'
    },
    {
      title: 'Explore learning paths',
      description: 'Recommended tracks curated for your current title.',
      cta: 'View learning hub'
    }
  ];

  readonly upcomingEvents = [
    {
      dayLabel: 'Mon',
      day: '18',
      title: 'Manager stand-up',
      description: 'Project Falcon sync'
    },
    {
      dayLabel: 'Thu',
      day: '21',
      title: 'Career package review',
      description: 'HR virtual session'
    }
  ];

}
