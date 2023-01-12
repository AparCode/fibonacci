import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { ProductService } from './product.service';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductSearchComponent } from './product-search/product-search.component';
import { HomeComponent } from './home/home.component';
import { MessagesComponent } from './messages/messages.component';
import { LoginComponent } from './login/login.component';
import { LoginService } from './login/login.service';
import { CartComponent } from './cart/cart.component';
import { ShoppingcartService } from './cart/shoppingcart.service';
import { ManagementComponent } from './management/management.component';
import { NavigationComponent } from './navigation/navigation.component';
import { SignupComponent } from './signup/signup.component';
import { SignupService } from './signup/signup.service';
import { ManagementGuard } from './management/management.guard';
import { CartGuard } from './cart/cart.guard';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';


@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ProductDetailComponent,
    ProductSearchComponent,
    HomeComponent,
    MessagesComponent,
    LoginComponent,

    CartComponent,
    ManagementComponent,
    NavigationComponent,
    SignupComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [LoginService, SignupService, ShoppingcartService, ProductService, ManagementGuard, CartGuard],
  bootstrap: [AppComponent]
})
export class AppModule { }
