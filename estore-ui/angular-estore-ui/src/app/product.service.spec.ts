import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {HttpClientModule} from '@angular/common/http';
import { ProductService } from './product.service';

describe('ProductService', () => {
  let service: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule], 
      providers: [ProductService]});
    service = TestBed.inject(ProductService);
  });

  it('should be created', () => {
    const service: ProductService = TestBed.get(ProductService);
    expect(service).toBeTruthy();
  });

  it('should have getData function', () => {
    const service: ProductService = TestBed.get(ProductService);
    expect(service.getProduct).toBeTruthy(); //cghanged getData to getProduct
   });
});
