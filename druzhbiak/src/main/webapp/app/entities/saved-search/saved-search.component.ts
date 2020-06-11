import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISavedSearch } from 'app/shared/model/saved-search.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SavedSearchService } from './saved-search.service';
import { SavedSearchDeleteDialogComponent } from './saved-search-delete-dialog.component';

@Component({
  selector: 'jhi-saved-search',
  templateUrl: './saved-search.component.html'
})
export class SavedSearchComponent implements OnInit, OnDestroy {
  savedSearches: ISavedSearch[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected savedSearchService: SavedSearchService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.savedSearches = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.savedSearchService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ISavedSearch[]>) => this.paginateSavedSearches(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.savedSearches = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSavedSearches();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISavedSearch): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSavedSearches(): void {
    this.eventSubscriber = this.eventManager.subscribe('savedSearchListModification', () => this.reset());
  }

  delete(savedSearch: ISavedSearch): void {
    const modalRef = this.modalService.open(SavedSearchDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.savedSearch = savedSearch;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSavedSearches(data: ISavedSearch[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.savedSearches.push(data[i]);
      }
    }
  }
}
