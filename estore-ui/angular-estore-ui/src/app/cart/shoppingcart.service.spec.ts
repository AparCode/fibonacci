import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {HttpClientModule} from '@angular/common/http';
import { ShoppingcartService } from './shoppingcart.service';

describe('ShoppingcartService', () => {
  let service: ShoppingcartService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule], 
      providers: [ShoppingcartService]});
    service = TestBed.inject(ShoppingcartService);
  });

  it('should be created', () => {
    const service: ShoppingcartService = TestBed.get(ShoppingcartService);
    expect(service).toBeTruthy();
  });
});
