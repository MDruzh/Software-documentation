import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'netflix-user',
        loadChildren: () => import('./netflix-user/netflix-user.module').then(m => m.NetflixNetflixUserModule)
      },
      {
        path: 'my-list',
        loadChildren: () => import('./my-list/my-list.module').then(m => m.NetflixMyListModule)
      },
      {
        path: 'saved-search',
        loadChildren: () => import('./saved-search/saved-search.module').then(m => m.NetflixSavedSearchModule)
      },
      {
        path: 'movie',
        loadChildren: () => import('./movie/movie.module').then(m => m.NetflixMovieModule)
      },
      {
        path: 'episode',
        loadChildren: () => import('./episode/episode.module').then(m => m.NetflixEpisodeModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class NetflixEntityModule {}
