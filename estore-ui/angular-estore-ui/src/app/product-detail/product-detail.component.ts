import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../../model/product';
import { ProductService } from '../product.service';
import { ShoppingcartService } from '../cart/shoppingcart.service';
//import { Reserve } from '../reserve';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: [ './product-detail.component.css' ]
})
export class ProductDetailComponent implements OnInit {
  product: Product | undefined;
  username: string | undefined | null;
  adminName: string = "admin";
  empty: string = " ";

  formValid: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private shoppingCartService: ShoppingcartService
  ) {}

  ngOnInit(): void {
    this.username = localStorage.getItem("username");
    this.getProduct();
  }

  getProduct(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.productService.getProduct(id)
      .subscribe(product => this.product = product);
  }

  displayToolTipBlur(tooltip: any, input: any, form: any) {
    var message = this.validateForm(input, form)
    if (message == true){
      tooltip.close()
    } else {
      tooltip.open({message})
    }
  }

  validateForm(input: any, form: any): string | boolean{
    if (input.invalid){
      this.formValid = false;
      if (input.control.errors.required){
        return "Input Required"
      } else if (input.control.errors.pattern && input.name != "inventoryAmt"){
        return "Input must be a number"
      } else if (input.control.errors.pattern && input.name == "inventoryAmt"){
        return "Input must be a whole number"
      } else {
        return "Invalid"
      }
    } else {
      if (!form.invalid){
        this.formValid = true;
        return true
      } else {
        return true
      }
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product)
        .subscribe(() => this.goBack());
    }
  }

  addToCart(): void {
    if(this.product && this.username) {
      console.log("username:" + this.username);
      this.shoppingCartService.addToCart(this.username, this.product).subscribe();
    }
  }
  /*
  reserveProduct(): void{
    if(this.product && this.username){
      console.log("username:"+ this.username);
      const list: Reserve[] = [];
        for(let i =0; i<list.length;i++){
          list.push(); //include a the this.product into the reserved list array
        }
        console.log(list);
    }
  }
  */
}