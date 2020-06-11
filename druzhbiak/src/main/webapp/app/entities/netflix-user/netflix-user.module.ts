import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NetflixSharedModule } from 'app/shared/shared.module';
import { NetflixUserComponent } from './netflix-user.component';
import { NetflixUserDetailComponent } from './netflix-user-detail.component';
import { NetflixUserUpdateComponent } from './netflix-user-update.component';
import { NetflixUserDeleteDialogComponent } from './netflix-user-delete-dialog.component';
import { netflixUserRoute } from './netflix-user.route';

@NgModule({
  imports: [NetflixSharedModule, RouterModule.forChild(netflixUserRoute)],
  declarations: [NetflixUserComponent, NetflixUserDetailComponent, NetflixUserUpdateComponent, NetflixUserDeleteDialogComponent],
  entryComponents: [NetflixUserDeleteDialogComponent]
})
export class NetflixNetflixUserModule {}
