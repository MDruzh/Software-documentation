import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMyList } from 'app/shared/model/my-list.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MyListService } from './my-list.service';
import { MyListDeleteDialogComponent } from './my-list-delete-dialog.component';

@Component({
  selector: 'jhi-my-list',
  templateUrl: './my-list.component.html'
})
export class MyListComponent implements OnInit, OnDestroy {
  myLists: IMyList[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected myListService: MyListService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.myLists = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.myListService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IMyList[]>) => this.paginateMyLists(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.myLists = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMyLists();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMyList): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMyLists(): void {
    this.eventSubscriber = this.eventManager.subscribe('myListListModification', () => this.reset());
  }

  delete(myList: IMyList): void {
    const modalRef = this.modalService.open(MyListDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.myList = myList;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateMyLists(data: IMyList[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.myLists.push(data[i]);
      }
    }
  }
}
