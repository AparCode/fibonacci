import { TestBed } from '@angular/core/testing';

import { SignupService } from './signup.service';

describe('SignupService', () => {
  let sign: SignupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sign = TestBed.inject(SignupService);
  });

  it('account should be created', () => {
    expect(sign).toBeTruthy();
  });
});
