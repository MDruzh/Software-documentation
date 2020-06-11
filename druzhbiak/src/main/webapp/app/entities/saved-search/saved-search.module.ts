import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NetflixSharedModule } from 'app/shared/shared.module';
import { SavedSearchComponent } from './saved-search.component';
import { SavedSearchDetailComponent } from './saved-search-detail.component';
import { SavedSearchUpdateComponent } from './saved-search-update.component';
import { SavedSearchDeleteDialogComponent } from './saved-search-delete-dialog.component';
import { savedSearchRoute } from './saved-search.route';

@NgModule({
  imports: [NetflixSharedModule, RouterModule.forChild(savedSearchRoute)],
  declarations: [SavedSearchComponent, SavedSearchDetailComponent, SavedSearchUpdateComponent, SavedSearchDeleteDialogComponent],
  entryComponents: [SavedSearchDeleteDialogComponent]
})
export class NetflixSavedSearchModule {}
