import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsComponent } from './products/products.component';
import { HomeComponent } from './home/home.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { LoginComponent } from './login/login.component';
import { CartComponent } from './cart/cart.component';
import { ManagementComponent } from './management/management.component';
import { SignupComponent } from './signup/signup.component';
import { ManagementGuard } from './management/management.guard';
import { CartGuard } from './cart/cart.guard';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { SignupGuard } from './signup/signup.guard';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent},
  { path: 'detail/:id', component: ProductDetailComponent},
  { path: 'products', component: ProductsComponent},
  { path: 'login', component: LoginComponent},
  { path: 'cart', component: CartComponent, canActivate:[CartGuard]},
  { path: 'management', component: ManagementComponent, canActivate:[ManagementGuard]},
  { path: 'signup', component: SignupComponent, canActivate:[SignupGuard]},
  { path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
