import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { SignupService } from './signup.service';
import { User } from 'src/model/user';
import { Status } from 'src/model/Status';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  messageUsername = "Invalid Username!";
  messagePassword = "Invalid Password!";

  constructor(private router: Router, private signupService: SignupService) { }

  ngOnInit(): void {
  }

  onSubmit(signUp: any, userTooltip: any, passTooltip: any): void {
    var username = String(signUp.controls.username.value);
    var password = String(signUp.controls.password.value);

    var user: User = { username: username, password: password };

    this.registerUser(user, userTooltip, passTooltip); // Code after statement will run immediatly
  }

  registerUser(user: User, userTooltip: any, passTooltip: any) {
    this.signupService.signupUser(user).subscribe(status => {
      if (status === Status.valid) {
        
        //Route to login page after success
        this.router.navigate(['login']);
      } else if (status === Status.userAlreadyExists) {
        this.displayToolTipSubmit(userTooltip, true, "User already exists!");
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
