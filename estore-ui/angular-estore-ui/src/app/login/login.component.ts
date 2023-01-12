import { Component, OnInit, DoCheck } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { User } from 'src/model/user';
import { Status } from 'src/model/Status';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit {
  userLogin: boolean = false;

  messageUsername = "Invalid Username!";
  messagePassword = "Invalid Password!";

  constructor(private router: Router, private loginService: LoginService) {
  }

  ngOnInit(): void {

  }

  ngDoCheck() {
    if (this.userLogin == false) {
      localStorage.removeItem('username');
      localStorage.removeItem('userLogin')
    } else {

    }
  }

  onSubmit(login: any, userTooltip: any, passTooltip: any): void {
    var username = String(login.form.controls.username.value);
    var password = String(login.form.controls.password.value);
    this.userLogin = true;

    var user: User = { username: username, password: password };

    this.loginUser(user, userTooltip, passTooltip); // Code after statement will run immediatly
  }

  loginUser(user: User, userTooltip: any, passTooltip: any): void {
    this.loginService.authenticateUser(user).subscribe(status => {
      if (status === Status.valid) {

        localStorage.setItem('username', user.username);
        localStorage.setItem('userLogin', 'true');
        //Route away after success
        this.router.navigate(['home']);

      } else if (status == Status.invalidPassword) {
        this.displayToolTipSubmit(passTooltip, false, "Wrong Password!");
      } else if (status == Status.doesNotExist) {
        this.displayToolTipSubmit(userTooltip, true, "User does not exist.");
      } else {
        console.error("Invalid Status: ", status);
      }
    });
  }


  displayToolTipBlur(tooltip: any, flag: boolean, message: String, input: any) {
    //Username Form, true == username
    if (flag == true) {
      if (input.invalid && input.touched) {
        tooltip.open({message});
      }
    }

    //Password Form, false == password
    if (flag == false) {
      if (input.invalid && input.touched) {
        tooltip.open({message});
      }
    }
  }

  displayToolTipSubmit(tooltip: any, flag: boolean, message: String) {
    //Username Form, true == username
    if (flag == true) {
      tooltip.open({message})
    }

    //Password Form, false == password
    if (flag == false) {
      tooltip.open({message})
    }
  }
}