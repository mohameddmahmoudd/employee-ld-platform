import { Component, signal, input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Form, FormControl, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { RouterLink } from '@angular/router';
import { merge, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-field-component',
  imports: [
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    RouterLink    
  ],
  templateUrl: './field-component.html',
  styleUrl: './field-component.css',
})
export class FieldComponent {
  validatorsList = input.required<ValidatorFn[]>();

  name = input.required<string>();
  fieldErrorMessage = signal('');
  field!: FormControl;

  hideInput = signal(false);

  private destroy$ = new Subject<void>();
  ngOnInit() {
    this.field = new FormControl('', this.validatorsList());
    merge(this.field.statusChanges, this.field.valueChanges)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.updateFieldErrorMessage());
  }
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  clickOnHidePassword(event: MouseEvent) {
    this.hideInput.set(!this.hideInput());
    event.stopPropagation();
  }

  updateFieldErrorMessage() {
    console.trace("boo");
    this.field.markAsTouched();
    if (this.field.errors) {
      let errors = this.field.errors;
      if (errors['required'])
        this.fieldErrorMessage.set('Field can\'t be empty.');
      else if (errors['minlength'])
        this.fieldErrorMessage.set('Field value is too short.');
      else if (errors['maxlength'])
        this.fieldErrorMessage.set('Field value is too long.');
      return true;
    } else {
      this.fieldErrorMessage.set('');
      return false;
    }
  }
}
