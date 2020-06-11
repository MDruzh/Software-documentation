import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NetflixSharedModule } from 'app/shared/shared.module';
import { MyListComponent } from './my-list.component';
import { MyListDetailComponent } from './my-list-detail.component';
import { MyListUpdateComponent } from './my-list-update.component';
import { MyListDeleteDialogComponent } from './my-list-delete-dialog.component';
import { myListRoute } from './my-list.route';

@NgModule({
  imports: [NetflixSharedModule, RouterModule.forChild(myListRoute)],
  declarations: [MyListComponent, MyListDetailComponent, MyListUpdateComponent, MyListDeleteDialogComponent],
  entryComponents: [MyListDeleteDialogComponent]
})
export class NetflixMyListModule {}
