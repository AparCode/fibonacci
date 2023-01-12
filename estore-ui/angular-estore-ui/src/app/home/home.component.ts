import { Component, OnInit } from '@angular/core';
import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { Product } from '../../model/product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: [ './home.component.css' ]
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  displayCaroursel = true;

  constructor(private productService: ProductService, config:NgbCarouselConfig) {
    // customize carousel
    config.interval = 3000;
    config.showNavigationIndicators = false;
   }


  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products.slice(0, 6));
  }
}