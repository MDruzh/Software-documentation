import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INetflixUser, NetflixUser } from 'app/shared/model/netflix-user.model';
import { NetflixUserService } from './netflix-user.service';
import { NetflixUserComponent } from './netflix-user.component';
import { NetflixUserDetailComponent } from './netflix-user-detail.component';
import { NetflixUserUpdateComponent } from './netflix-user-update.component';

@Injectable({ providedIn: 'root' })
export class NetflixUserResolve implements Resolve<INetflixUser> {
  constructor(private service: NetflixUserService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INetflixUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((netflixUser: HttpResponse<NetflixUser>) => {
          if (netflixUser.body) {
            return of(netflixUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NetflixUser());
  }
}

export const netflixUserRoute: Routes = [
  {
    path: '',
    component: NetflixUserComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NetflixUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: NetflixUserDetailComponent,
    resolve: {
      netflixUser: NetflixUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NetflixUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: NetflixUserUpdateComponent,
    resolve: {
      netflixUser: NetflixUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NetflixUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: NetflixUserUpdateComponent,
    resolve: {
      netflixUser: NetflixUserResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'NetflixUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];
