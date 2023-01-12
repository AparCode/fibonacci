import { TestBed } from '@angular/core/testing';

import { ManagementGuard } from './management.guard';

describe('ManagementGuard', () => {
  let guard: ManagementGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(ManagementGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
