import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {HttpClientModule} from '@angular/common/http';

import { LoginComponent } from './login.component';
import { FormsModule, NgForm } from '@angular/forms';
import { NgbModule, NgbTooltip } from '@ng-bootstrap/ng-bootstrap';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({imports: [HttpClientTestingModule, FormsModule, NgbModule], 
      providers: [LoginComponent, NgbTooltip],
      declarations: [ LoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    const component: LoginComponent = TestBed.get(LoginComponent);
    expect(component).toBeTruthy();
  });
});
