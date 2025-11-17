import { Component, inject } from '@angular/core';
import { NgFor } from '@angular/common';
import { AuthService } from '../../../service/auth-service';

@Component({
  selector: 'app-guest-dashboard-component',
  standalone: true,
  imports: [NgFor],
  templateUrl: './guest-dashboard-component.html',
  styleUrl: './guest-dashboard-component.css',
})
export class GuestDashboardComponent {
  
  private readonly authService = inject(AuthService);
  readonly user = this.authService.user;

  readonly nextSteps = [
    {
      title: 'Verify your personal details',
      description: 'Double check that your legal name, email, and preferred title are correct so HR can activate your profile quickly.'
    },
    {
      title: 'Upload onboarding documents',
      description: 'Share any requested IDs or employment documents with HR to finalize your activation.'
    },
    {
      title: 'Schedule a welcome call',
      description: 'Pick a time with HR to understand your onboarding journey and the tools you will unlock after activation.'
    }
  ];

}
