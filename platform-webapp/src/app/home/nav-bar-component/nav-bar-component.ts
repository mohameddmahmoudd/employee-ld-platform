import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { Router, RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-nav-bar-component',
  imports: [MatTabsModule, RouterOutlet, RouterModule],
  templateUrl: './nav-bar-component.html',
  styleUrl: './nav-bar-component.css',
})
export class NavBarComponent {
  // links = ["first", "second"];
   links = [
    { label: 'Home', path: '' },
    { label: 'Profile', path: 'profile' },
    { label: 'Learnings', path: 'login' },
    { label: 'Wikis', path: 'login' },
    { label: 'Blogs', path: 'login' },
    { label: 'Career Package', path: 'login' },
    { label: 'Employee Management', path: 'login' },
  ];

  constructor(public router: Router) {}
  // activeLink = this.links[0];
}
