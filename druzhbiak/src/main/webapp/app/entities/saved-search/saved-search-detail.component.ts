import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISavedSearch } from 'app/shared/model/saved-search.model';

@Component({
  selector: 'jhi-saved-search-detail',
  templateUrl: './saved-search-detail.component.html'
})
export class SavedSearchDetailComponent implements OnInit {
  savedSearch: ISavedSearch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ savedSearch }) => (this.savedSearch = savedSearch));
  }

  previousState(): void {
    window.history.back();
  }
}
