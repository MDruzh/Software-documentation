import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INetflixUser } from 'app/shared/model/netflix-user.model';

@Component({
  selector: 'jhi-netflix-user-detail',
  templateUrl: './netflix-user-detail.component.html'
})
export class NetflixUserDetailComponent implements OnInit {
  netflixUser: INetflixUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netflixUser }) => (this.netflixUser = netflixUser));
  }

  previousState(): void {
    window.history.back();
  }
}
