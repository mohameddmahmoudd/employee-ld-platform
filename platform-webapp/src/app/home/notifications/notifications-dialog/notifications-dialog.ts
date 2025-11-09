import { Component, ViewEncapsulation } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogTitle, MatDialogContent, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { EmptyComponent } from "../../../empty-component/empty-component";
import { MatDialogConfig } from "@angular/material/dialog";
import { NotificationsComponent } from "../notifications-component/notifications-component";

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
    NotificationsComponent
  ],
  // encapsulation: ViewEncapsulation.None,
})
export class NotificationsDialog {

  // the pages bit is chatgpt generated bc it's dumb
  page = 1;                    // default page = 1
  totalPages?: number;         // optional, set if you know total

  prev() { if (this.page > 1) this.page--; }
  next() { if (!this.totalPages || this.page < this.totalPages) this.page++; }
  // goTo(n?: number) {
  //   if (!n) return;
  //   this.page = Math.max(1, this.totalPages ? Math.min(n, this.totalPages) : n);
  // }

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