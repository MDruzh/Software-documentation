import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NetflixSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [NetflixSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent]
})
export class NetflixHomeModule {}
