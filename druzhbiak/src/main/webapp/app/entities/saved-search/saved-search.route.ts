import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISavedSearch, SavedSearch } from 'app/shared/model/saved-search.model';
import { SavedSearchService } from './saved-search.service';
import { SavedSearchComponent } from './saved-search.component';
import { SavedSearchDetailComponent } from './saved-search-detail.component';
import { SavedSearchUpdateComponent } from './saved-search-update.component';

@Injectable({ providedIn: 'root' })
export class SavedSearchResolve implements Resolve<ISavedSearch> {
  constructor(private service: SavedSearchService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISavedSearch> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((savedSearch: HttpResponse<SavedSearch>) => {
          if (savedSearch.body) {
            return of(savedSearch.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SavedSearch());
  }
}

export const savedSearchRoute: Routes = [
  {
    path: '',
    component: SavedSearchComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SavedSearches'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SavedSearchDetailComponent,
    resolve: {
      savedSearch: SavedSearchResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SavedSearches'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SavedSearchUpdateComponent,
    resolve: {
      savedSearch: SavedSearchResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SavedSearches'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SavedSearchUpdateComponent,
    resolve: {
      savedSearch: SavedSearchResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'SavedSearches'
    },
    canActivate: [UserRouteAccessService]
  }
];
