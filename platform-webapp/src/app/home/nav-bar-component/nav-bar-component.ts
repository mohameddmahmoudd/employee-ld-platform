import { MediaMatcher } from '@angular/cdk/layout';
import { Component, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatRippleModule } from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Router, RouterModule, RouterOutlet } from "@angular/router";
import { NotificationsDialog } from '../notifications/notifications-dialog/notifications-dialog';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-nav-bar-component',
  imports: [
    MatTabsModule,
    RouterModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatRippleModule,
    MatTooltipModule
  ],
  templateUrl: './nav-bar-component.html',
  styleUrl: './nav-bar-component.css',
})
export class NavBarComponent {
  protected isMobile = signal(true);
  private _mobileQuery!: MediaQueryList;
  private _mobileQueryListener!: () => void;

  authService = inject(AuthService);

  links = [
    { label: 'Home', path: '' },
    { label: 'Blogs', path: 'blogs' }
  ];


  // I stole this from angular material
  private prepareMobileView() {
    const media = inject(MediaMatcher);
    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => {
      this.isMobile.set(this._mobileQuery.matches);
      this.dialog.closeAll();
    }
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  private prepareLinksList() {
    if (this.authService.checkRole(this.authService.user, "EMPLOYEE")) {
      this.links.push(...[
        { label: 'Wikis', path: 'wikis' },
        { label: 'Learnings', path: 'learnings' },
        { label: 'Career Package', path: 'career-package' },
      ])
    }

    if (this.authService.checkRole(this.authService.user, "MANAGER")) {
      this.links.push(...[
        { label: 'Approvals', path: 'approvals' },
      ])
    }
// TODO: this should be ADMIN but it's none for testing
    if (/*true || */ this.authService.checkRole(this.authService.user, "ADMIN")) {
      this.links.push(...[
        { label: 'Admin Panel', path: 'admin-panel' },
      ])
    }
  }

  constructor(public router: Router) {
    this.prepareMobileView();
    this.prepareLinksList();

    console.log(this.authService.user?.roles);
  }

  ngOnDestroy(): void {
    this._mobileQuery.removeEventListener('change', this._mobileQueryListener);
  }

  readonly dialog = inject(MatDialog);
  // TODO: add number badge to notifications bell with the number of unread notifs. or at least a badge that shows that there are unread notifications
  openDialog(event: MouseEvent) {
    const buttonRect = (event.currentTarget as HTMLElement).getBoundingClientRect();
    if (this.isMobile()) {
      this.dialog.open(NotificationsDialog, NotificationsDialog.getMobileViewConfig(buttonRect));
    } else {
      this.dialog.open(NotificationsDialog, NotificationsDialog.getDesktopViewConfig(buttonRect));
    }
  }
}
