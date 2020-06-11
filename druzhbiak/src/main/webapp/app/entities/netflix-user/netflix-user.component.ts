import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INetflixUser } from 'app/shared/model/netflix-user.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { NetflixUserService } from './netflix-user.service';
import { NetflixUserDeleteDialogComponent } from './netflix-user-delete-dialog.component';

@Component({
  selector: 'jhi-netflix-user',
  templateUrl: './netflix-user.component.html'
})
export class NetflixUserComponent implements OnInit, OnDestroy {
  netflixUsers: INetflixUser[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected netflixUserService: NetflixUserService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.netflixUsers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.netflixUserService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<INetflixUser[]>) => this.paginateNetflixUsers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.netflixUsers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInNetflixUsers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: INetflixUser): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInNetflixUsers(): void {
    this.eventSubscriber = this.eventManager.subscribe('netflixUserListModification', () => this.reset());
  }

  delete(netflixUser: INetflixUser): void {
    const modalRef = this.modalService.open(NetflixUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.netflixUser = netflixUser;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateNetflixUsers(data: INetflixUser[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.netflixUsers.push(data[i]);
      }
    }
  }
}
