import { Component, OnInit } from '@angular/core';
import { Product } from '../../model/product';
import { ShoppingCart } from 'src/model/shoppingcart';
import { ShoppingcartService } from './shoppingcart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  currentUser: string | null = null;
  currentCart: ShoppingCart | undefined;

  checkedOut: boolean = false;
  reservationShown: boolean = false;

  constructor(private shoppingCartService: ShoppingcartService) { }

  ngOnInit(): void {
    this.currentUser = localStorage.getItem("username");

    if(this.currentUser != null) {
      this.getCart(this.currentUser);
    }
    this.checkedOut = false;
  }

  getCart(currentUser: String): void {
    this.shoppingCartService.getCart(currentUser).subscribe(currentCart => this.currentCart = currentCart);
  }

  removeProduct(product: Product): void {
    this.shoppingCartService.removeFromCart(this.currentUser, product).subscribe(currentCart => this.currentCart = currentCart);
  }

  addAnother(product: Product): void {
    this.shoppingCartService.addToCart(this.currentUser, product).subscribe(currentCart => this.currentCart = currentCart);
  }

  checkoutCart(): void {
    this.shoppingCartService.checkoutCart(this.currentUser).subscribe(currentCart => this.currentCart = currentCart);
    this.checkedOut = true;
  }

  reserveItem(product: Product): void {
    this.shoppingCartService.reserveItem(this.currentUser, product).subscribe(currentCart => this.currentCart = currentCart)
  }

  unReserveItem(): void {
    this.shoppingCartService.unreserveItem(this.currentUser).subscribe(currentCart => this.currentCart = currentCart)
  }

}
