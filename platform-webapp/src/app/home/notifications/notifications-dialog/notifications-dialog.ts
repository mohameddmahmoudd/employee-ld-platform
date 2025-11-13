import { Component, ViewEncapsulation } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { EmptyComponent } from "../../../empty-component/empty-component";
import { MatDialogConfig } from "@angular/material/dialog";
import { NotificationsComponent } from "../notifications-component/notifications-component";
import { MatPaginatorModule, PageEvent } from "@angular/material/paginator";

@Component({
  selector: 'notifications-dialog',
  templateUrl: './notifications-dialog.html',
  styleUrl: './notifications-dialog.css',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButtonModule,
    NotificationsComponent,
    MatPaginatorModule,
  ],
  // encapsulation: ViewEncapsulation.None,
})
export class NotificationsDialog {
  pageIndex : number = 1;
  pageSize: number = 5;
  onPage(e: PageEvent){
    this.pageIndex = e.pageIndex + 1;
    this.pageSize = e.pageSize;
  }
  
  static getMobileViewConfig(buttonRect: DOMRect): MatDialogConfig {
    return {
      width: '100vw',
      height: '90dvh',
      position: {
        top: `${buttonRect.bottom + 10}px`,
      },
    }
  }

  static getDesktopViewConfig(buttonRect: DOMRect): MatDialogConfig {
    return {
      height: `90dvh`,
      position: {
        top: `${buttonRect.bottom + 10}px`,
        left: `${buttonRect.left - 28 - 10 + buttonRect.width / 2}px`, //28 is caret position, 10 is half caret width
      },
      // backdropClass: "backdrop" 
      // TODO: decide whether we want outline to notifs and no backdrop or we want backdrop?
    };
  }

}