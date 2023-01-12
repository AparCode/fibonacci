import { Component, OnInit, DoCheck } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login/login.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  admin: boolean = false;
  cart: boolean = false;
  userLogin:boolean = false;
  username: string | null;

  constructor(private router: Router, private loginService: LoginService ) {
    this.username = " ";
   }

  ngOnInit(): void {
    
  }

  ngDoCheck(){
    if (localStorage.getItem('username') == 'admin' && localStorage.getItem('userLogin') == 'true'){
      this.username = localStorage.getItem('username');
      this.admin = true;
      this.cart = false;
      this.userLogin = true;
    } else if (localStorage.getItem('username') != 'admin' && localStorage.getItem('userLogin') == 'true') {
      this.username = localStorage.getItem('username');
      this.admin = false;
      this.cart = true;
      this.userLogin = true;
    }
  }

  logout(): void{
    localStorage.removeItem('username');
    localStorage.removeItem('userLogin');
    this.admin = false;
    this.cart = false;
    this.userLogin = false;
    this.router.navigate(['home'])
  }

}
