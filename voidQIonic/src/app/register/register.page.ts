import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

import { SessionService } from '../session.service';
import { PatientService } from '../patient.service';
import { Patient } from '../patient';


@Component({
	selector: 'app-register',
	templateUrl: './register.page.html',
	styleUrls: ['./register.page.scss'],
})

export class RegisterPage implements OnInit {

	submitted: boolean;
	// email: string;
	// firstName: string;
	// lastName: string;
	// phoneNumber: string;
	// password: string;
	newPatient: Patient;
	registerError: boolean;
	registerErrorMessage: string;
	message: string;
	toast: any;

	constructor(private router: Router,
		public sessionService: SessionService,
		private patientService: PatientService,) {
		this.submitted = false;
		this.registerError = false;
		this.newPatient = new Patient();
	}

	ngOnInit() {
	}

	clear() {
		this.submitted = false;
		this.newPatient = new Patient();
	}

	patientRegister(patientRegisterForm: NgForm) {
		this.submitted = true;

		if (patientRegisterForm.valid) {
			// this.sessionService.setEmail(this.email);
			// this.sessionService.setFirstName(this.firstName);
			// this.sessionService.setLastName(this.lastName);
			// this.sessionService.setPhoneNumber(this.phoneNumber);
			// this.sessionService.setPassword(this.password);

			this.patientService.patientRegister(this.newPatient).subscribe(
				response => {
					let newPatientId: number = response.userId;
					this.message = "Patient " + newPatientId + "successfully registered";

					// if(newPatientId != null)
					// {
					// 	this.sessionService.setIsLogin(true);
					// 	this.sessionService.setCurrentPatient(this.newPatient);
					// 	this.loginError = false;					
					// 	window.location.reload();						
					// }
					// else
					// {
					// 	this.loginError = true;
					// }
				},
				error => {
					this.registerError = true;
					this.registerErrorMessage = error
				}
			);
		}
		else {

		}
	}
}
