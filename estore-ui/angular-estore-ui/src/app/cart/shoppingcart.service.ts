import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, tap } from 'rxjs';
import { MessageService } from '../message.service';
import { Product } from 'src/model/product';
import { ShoppingCart } from 'src/model/shoppingcart';

@Injectable({
  providedIn: 'root'
})
export class ShoppingcartService {

  private shoppingCartUrl = "http://localhost:8080/shopping-carts/"

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** GET cart from the server */
  getCart(user: String | null): Observable<ShoppingCart> {
    return this.http.get<ShoppingCart>(this.shoppingCartUrl + user + "/cart")
      .pipe(
        tap(_ => this.log('fetched shopping cart for ' + user)),
        catchError(this.handleError<ShoppingCart>('getCart'))
      );
  }

  /** POST: add an item to the shopping cart */
  addToCart(user: String | null, product: Product): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.shoppingCartUrl + user + "/cart", product, this.httpOptions).pipe(
      tap((newCart: ShoppingCart) => this.log(`added product w/ id=${product.id} to ${user}'s cart`)),
      catchError(this.handleError<ShoppingCart>('addToCart'))
    );
  }

  removeFromCart(user: String | null, product: Product): Observable<ShoppingCart> {
    return this.http.request<ShoppingCart>('DELETE', this.shoppingCartUrl + user + "/cart", {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: product
    }).pipe(
      tap((newCart: ShoppingCart) => this.log(`removed product w/ id=${product.id} from ${user}'s cart`)),
      catchError(this.handleError<ShoppingCart>('removeFromCart'))
    );
  }

  checkoutCart(user: String | null): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.shoppingCartUrl + user + "/cart/checkout", {}, this.httpOptions).pipe(
      tap((newCart: ShoppingCart) => this.log(`checked out ${user}'s cart`)),
      catchError(this.handleError<ShoppingCart>('checkoutCart'))
    );
  }

  reserveItem(user: String | null, product: Product): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.shoppingCartUrl + user + "/cart/reserve", product, this.httpOptions).pipe(
      tap((newCart: ShoppingCart) => this.log(`reserved item id=${product.id} in ${user}'s cart`)),
      catchError(this.handleError<ShoppingCart>('reserveItem'))
    );
  }

  unreserveItem(user: String | null): Observable<ShoppingCart> {
    return this.http.request<ShoppingCart>('DELETE', this.shoppingCartUrl + user + "/cart/reserve", {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {}
    }).pipe(
      tap((newCart: ShoppingCart) => this.log(`unreserved product in ${user}'s cart`)),
      catchError(this.handleError<ShoppingCart>('unreserveItem'))
    );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a ProductService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
  }

}
