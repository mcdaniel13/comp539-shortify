import { TestBed } from '@angular/core/testing';

import { BulkaccessService } from './bulkaccess.service';

describe('BulkaccessService', () => {
  let service: BulkaccessService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BulkaccessService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
