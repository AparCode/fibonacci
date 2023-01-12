import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SignupGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean | UrlTree{
      let isLoggedIn = localStorage.getItem('userLogin')
      if (isLoggedIn){
        this.router.navigate(['PageNotFound'])
        var UrlTree = this.router.createUrlTree(['PageNotFound'])
        return UrlTree
      } else {
        return true
      }
    }
  }
