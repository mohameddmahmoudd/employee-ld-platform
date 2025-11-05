import { Component } from '@angular/core';
import { NavBarComponent } from './nav-bar-component/nav-bar-component';
import { RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-home',
  imports: [NavBarComponent, RouterModule, RouterOutlet],
  template: `
    <app-nav-bar-component/>
    <router-outlet/>
  `
})
export class Home {}
