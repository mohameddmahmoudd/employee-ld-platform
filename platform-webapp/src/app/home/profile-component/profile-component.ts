import { Component, inject } from '@angular/core';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-profile-component',
  imports: [],
  templateUrl: './profile-component.html',
  styleUrl: './profile-component.css',
})
export class ProfileComponent {
  authService = inject(AuthService);
  user = this.authService.user;

}
