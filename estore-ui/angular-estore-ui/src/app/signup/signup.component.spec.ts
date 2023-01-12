import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import {HttpClientModule} from '@angular/common/http';

import { SignupComponent } from './signup.component';
import { FormsModule, NgForm } from '@angular/forms';
import { NgbModule, NgbTooltip } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';

describe('SignupComponent', () => {
  let component: SignupComponent;
  let fixture: ComponentFixture<SignupComponent>;
 
  beforeEach(async () => {
    await TestBed.configureTestingModule({imports: [HttpClientTestingModule, FormsModule, NgbModule], 
      providers: [SignupComponent, NgbTooltip],
      declarations: [ SignupComponent ],
      
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    const component: SignupComponent = TestBed.get(SignupComponent);
    expect(component).toBeTruthy();
  });
});
