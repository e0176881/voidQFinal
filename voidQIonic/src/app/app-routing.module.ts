import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
	{
		path: '',
		redirectTo: 'home',
		pathMatch: 'full'
	},
	{ path: 'home', loadChildren: './home/home.module#HomePageModule' },
	{ path: 'register', loadChildren: './register/register.module#RegisterPageModule' },
	{ path: 'paymentpage', loadChildren: './paymentpage/paymentpage.module#PaymentpagePageModule' },
	{ path: 'profile', loadChildren: './mainscreen/profile/profile.module#ProfilePageModule' },
	{ path: 'view-booking', loadChildren: './mainscreen/view-booking/view-booking.module#ViewBookingPageModule' },
	{ path: 'clinics', loadChildren: './mainscreen/clinics/clinics.module#ClinicsPageModule' },
	{ path: 'clinic-details', loadChildren: './clinic-details/clinic-details.module#ClinicDetailsPageModule' },
  	{ path: 'tabs', loadChildren: './mainscreen/tabs/tabs.module#TabsPageModule' },
  	{ path: 'resetpassword', loadChildren: './resetpassword/resetpassword.module#ResetpasswordPageModule' },
  


];
@NgModule({
	imports: [
		RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
	],
	exports: [RouterModule]
})
export class AppRoutingModule { }
