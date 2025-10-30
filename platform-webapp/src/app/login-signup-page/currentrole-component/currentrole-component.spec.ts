import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentroleComponent } from './currentrole-component';

describe('CurrentroleComponent', () => {
  let component: CurrentroleComponent;
  let fixture: ComponentFixture<CurrentroleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CurrentroleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentroleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
