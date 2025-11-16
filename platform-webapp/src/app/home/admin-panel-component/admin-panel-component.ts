import { Component, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCard, MatCardModule } from "@angular/material/card";
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { UserService } from '../../service/user-service';
import { UserDto } from '../../model/UserDto';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { delay, Observable } from 'rxjs';
import { MatRippleModule } from '@angular/material/core';
import { UserUpdateInfoDTO } from './UserUpdateInfoDto';
import { AuthService } from '../../service/auth-service';

// TODO: guard for admin-panel component bc it doesn't exist yet
@Component({
  selector: 'app-admin-panel-component',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatSnackBarModule,
    MatRippleModule
  ],
  templateUrl: './admin-panel-component.html',
  styleUrl: './admin-panel-component.css',
})
export class AdminPanelComponent {
  private _snackBar = inject(MatSnackBar);
  userService = inject(UserService);
  authService = inject(AuthService);

  searchCtrl = new FormControl('');
  defaultRoles: { name: string, isSelected: boolean }[] = [];

  currentUser = signal<UserDto | null>(null);
  currentUserManager = signal<UserDto | null>(null);

  userFound = signal<boolean | null>(null);
  isLoading = signal(false);
  isLoadingSave = signal(false);
  saveErrorMessage: string = '';
  errorSaving = signal(false);

  ngOnInit() {
    // TODO: remove this delay, this is just for illustration lol
    let rolesRequest = this.userService.fetchDefaultRoles().pipe(delay(10000)).subscribe({
      next: (response: string[]) => {
        this.defaultRoles = response.map(r => ({ name: r, isSelected: false }));
        console.log(response);

        let currentUser = this.currentUser ? this.currentUser() : null;
        if (currentUser) {
          this.setSearchedForUserRoles(currentUser);
        }
      }, error: (error: HttpErrorResponse) => {
        // TODO: not sure what to do here. make spinner spin forever?
      }
    })
  }


  resetPanel() {
    this.isLoading.set(false);
    this.isLoadingSave.set(false);
    this.saveErrorMessage = '';
    this.errorSaving.set(false);

    this.userFound.set(null);
    this.currentUser.set(null);
    this.currentUserManager.set(null);
  }

  private setSearchedForUser(data: UserDto) {
    this.currentUser.set(data);
  }
  private setSearchedForUserRoles(data: UserDto) {
    this.defaultRoles = this.defaultRoles.map(r => ({
      ...r,
      isSelected: (data.roles ?? []).includes(r.name)
    }));
  }
  private setSearchedForUserManager(data: UserDto) {
    let managerId = data.managerId;
    if (managerId) {
      this.isLoading.set(true);
      let managerRequest = this.userService.getUser(managerId);
      managerRequest.subscribe({
        next: (response: UserDto) => {
          this.isLoading.set(false);
          this.currentUserManager.set(response);
        },
        error: (error: HttpErrorResponse) => {
          console.log(error)
          this.isLoading.set(false);
        }
      })
    }
  }

  onSearch() {
    let searchValue = this.searchCtrl.value?.trim();
    if (!searchValue || searchValue === '') return;
    this.resetPanel();
    this.searchCtrl.reset();
    this.isLoading.set(true);

    let request = this.userService.getUserByUsername(searchValue);
    request.subscribe({
      next: (response: UserDto) => {
        this.userFound.set(true);
        this.isLoading.set(false);
        this.setSearchedForUser(response);
        this.setSearchedForUserRoles(response);
        this.setSearchedForUserManager(response);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error)
        this.userFound.set(false);
        this.isLoading.set(false);
      }
    })
  }


  private subscribtionToSaveRequestsHandler(req: Observable<any>, caller: "roles" | "manager" | "title", currentUser: UserDto, changes: any) {
    req.subscribe({
      next: (response: any) => {
        this._snackBar.open("Success!", undefined, { duration: 2000 });
        this.saveErrorMessage = "";
        this.errorSaving.set(false);
        this.isLoadingSave.set(false);
        if (currentUser.id === this.authService.user?.id) {
          switch (caller) {
            case 'roles':
              this.authService.user.roles = changes;
              break;
            case 'manager':
              this.authService.user.managerId = changes;
              break;
            case 'title':
              this.authService.user.title = changes;
              break;
          }
        }

      },
      error: (error: HttpErrorResponse) => {
        console.log(error)
        this.saveErrorMessage = "Some error occured. Try again later.";
        this.errorSaving.set(true);
        this.isLoadingSave.set(false);
      }
    })
  }
  saveRoles(event: Event) {
    this.isLoadingSave.set(true);
    let currentUser = this.currentUser()!; // exclamaion point because you WILL NOT access this unless you have a valid current user

    const selected = this.defaultRoles.filter(d => d.isSelected).map(d => d.name);
    let updateUserRolesRequest = this.userService.updateUserRoles(currentUser.id, selected);
    this.subscribtionToSaveRequestsHandler(updateUserRolesRequest, "roles", currentUser, selected);
  }

  private handleUpdateManagerByIdRequest(currentUser: UserDto, managerId: number | null) {
    let updateManagerRequest = this.userService.updateUserManager(currentUser.id, managerId);
    this.subscribtionToSaveRequestsHandler(updateManagerRequest, "manager", currentUser, managerId);
  }

  private handleUpdateManagerByUsernameRequest(currentUser: UserDto, managerUserName: string) {
    let findManagerId = this.userService.getUserByUsername(managerUserName);
    findManagerId.subscribe({
      next: (response: UserDto) => {
        console.log(response);
        let managerId = response.id;
        this.handleUpdateManagerByIdRequest(currentUser, managerId);
      },
      error: (error: HttpErrorResponse) => {
        console.log(error)
        this.saveErrorMessage = "No user with this username found.";
        this.errorSaving.set(true);
        this.isLoadingSave.set(false);
      }
    })
  }
  saveManager(event: Event) {
    this.isLoadingSave.set(true);
    const currentUser = this.currentUser()!; // exclamaion point because you WILL NOT access this unless you have a valid current user
    // await new Promise(r => setTimeout(r, 5000));
    let managerUserName = (event.target as HTMLElement).innerText.trim();
    // TODO: on signup, validate that there are no spaces in the username
    if (managerUserName === '') {
      this.handleUpdateManagerByIdRequest(currentUser, null);
    } else {
      this.handleUpdateManagerByUsernameRequest(currentUser, managerUserName);
    }
  }


  saveTitle(event: Event) {
    this.isLoadingSave.set(true);
    const currentUser = this.currentUser()!;
    const userTitle = (event.target as HTMLElement).innerText.trim();
    const newData: UserUpdateInfoDTO = {
      title: userTitle,
      username: currentUser.username,
      fullName: currentUser.fullName
    }
    let request = this.userService.updateUserInfo(currentUser.id, newData);
    this.subscribtionToSaveRequestsHandler(request, "title", currentUser, userTitle);
  }
}