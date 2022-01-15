import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UrlmanagementComponent } from './urlmanagement.component';

describe('UrlmanagementComponent', () => {
  let component: UrlmanagementComponent;
  let fixture: ComponentFixture<UrlmanagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UrlmanagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UrlmanagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
