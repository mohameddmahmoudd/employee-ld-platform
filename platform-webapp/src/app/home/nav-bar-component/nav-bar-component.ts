import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-nav-bar-component',
  imports: [MatTabsModule, RouterModule],
  templateUrl: './nav-bar-component.html',
  styleUrl: './nav-bar-component.css',
})
export class NavBarComponent {
  // links = ["first", "second"];
   links = [
    { label: 'Home', path: '' },
    { label: 'Profile', path: 'profile' },
    { label: 'Learnings', path: 'learnings' },
    { label: 'Wikis', path: 'wikis' },
    { label: 'Blogs', path: 'blogs' },
    { label: 'Career Package', path: 'career-package' },
    { label: 'Employee Management', path: 'employee-management' },
  ];

  constructor(public router: Router) {}
  // activeLink = this.links[0];
}
