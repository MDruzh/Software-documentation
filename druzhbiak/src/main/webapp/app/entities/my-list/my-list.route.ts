import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMyList, MyList } from 'app/shared/model/my-list.model';
import { MyListService } from './my-list.service';
import { MyListComponent } from './my-list.component';
import { MyListDetailComponent } from './my-list-detail.component';
import { MyListUpdateComponent } from './my-list-update.component';

@Injectable({ providedIn: 'root' })
export class MyListResolve implements Resolve<IMyList> {
  constructor(private service: MyListService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMyList> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((myList: HttpResponse<MyList>) => {
          if (myList.body) {
            return of(myList.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MyList());
  }
}

export const myListRoute: Routes = [
  {
    path: '',
    component: MyListComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyLists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MyListDetailComponent,
    resolve: {
      myList: MyListResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyLists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MyListUpdateComponent,
    resolve: {
      myList: MyListResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyLists'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MyListUpdateComponent,
    resolve: {
      myList: MyListResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'MyLists'
    },
    canActivate: [UserRouteAccessService]
  }
];
