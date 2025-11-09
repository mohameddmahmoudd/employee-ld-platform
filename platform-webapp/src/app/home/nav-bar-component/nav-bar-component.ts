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
import { NotificationsDialog } from '../notifications/notifications-dialog';

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

  // links = ["first", "second"];

  protected readonly isMobile = signal(true);
  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  links = [
    { label: 'Home', path: '' },
    { label: 'Learnings', path: 'learnings' },
    { label: 'Wikis', path: 'wikis' },
    { label: 'Blogs', path: 'blogs' },
    { label: 'Career Package', path: 'career-package' },
    { label: 'Approvals', path: 'approvals' },
    { label: 'Employee Management', path: 'employee-management' },
  ];

  // I stole this from angular material
  constructor(public router: Router) {
    const media = inject(MediaMatcher);
    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => {
      this.isMobile.set(this._mobileQuery.matches);
      this.dialog.closeAll();
    }
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);
  }

  ngOnDestroy(): void {
    this._mobileQuery.removeEventListener('change', this._mobileQueryListener);
  }

  readonly dialog = inject(MatDialog);

  openDialog(event: MouseEvent) {
    const buttonRect = (event.currentTarget as HTMLElement).getBoundingClientRect();

    if (this.isMobile()) {
      // width: `100vw`,
      this.dialog.open(NotificationsDialog, {
        width: `100vw`,
        position: {
          top: `${buttonRect.bottom + 10}px`,
        },
      });
    } else {
      this.dialog.open(NotificationsDialog, {
        position: {
          top: `${buttonRect.bottom + 10}px`,
          left: `${buttonRect.left - 28 - 10 + buttonRect.width / 2}px`, //28 is caret position, 10 is half caret width
        },
        // backdropClass: "backdrop" 
        // TODO: decide whether we want outline to notifs and no backdrop or we want backdrop?

      });
    }

  }
  // activeLink = this.links[0];
}
