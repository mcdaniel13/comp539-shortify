import { TestBed } from '@angular/core/testing';

import { UrlmanagementService } from './urlmanagement.service';

describe('UrlmanagementService', () => {
  let service: UrlmanagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UrlmanagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
