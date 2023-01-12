import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ManagementGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot):boolean | UrlTree{
    let isLoggedIn = localStorage.getItem('userLogin')
    if (isLoggedIn && localStorage.getItem('username') == 'admin'){
      return true
    } else {
      this.router.navigate(['PageNotFound'])
        var UrlTree = this.router.createUrlTree(['PageNotFound'])
        return UrlTree
    }
  }
  
}
