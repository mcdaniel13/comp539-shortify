import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkaccessComponent } from './bulkaccess.component';

describe('BulkaccessComponent', () => {
  let component: BulkaccessComponent;
  let fixture: ComponentFixture<BulkaccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BulkaccessComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkaccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
