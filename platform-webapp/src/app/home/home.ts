import { Component } from '@angular/core';
import { NavBarComponent } from './nav-bar-component/nav-bar-component';
import { RouterModule, RouterOutlet } from "@angular/router";

@Component({
  selector: 'app-home',
  imports: [NavBarComponent, RouterModule, RouterOutlet],
  template: `
    <app-nav-bar-component/>
    <div class="content">
      <router-outlet/>
    </div>
  `,
  styles: [`
    :host {
      display: flex;
      flex-direction: column;
      height: 100vh;
      height: 100dvh;
      width: 100vw;
    }

    .content {
      flex: 1;
      min-height: 0;
      min-width: 0;
      position: relative;
    }
  `]
})
export class Home { }
