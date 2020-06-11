import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NetflixSharedModule } from 'app/shared/shared.module';
import { EpisodeComponent } from './episode.component';
import { EpisodeDetailComponent } from './episode-detail.component';
import { EpisodeUpdateComponent } from './episode-update.component';
import { EpisodeDeleteDialogComponent } from './episode-delete-dialog.component';
import { episodeRoute } from './episode.route';

@NgModule({
  imports: [NetflixSharedModule, RouterModule.forChild(episodeRoute)],
  declarations: [EpisodeComponent, EpisodeDetailComponent, EpisodeUpdateComponent, EpisodeDeleteDialogComponent],
  entryComponents: [EpisodeDeleteDialogComponent]
})
export class NetflixEpisodeModule {}
