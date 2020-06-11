import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISavedSearch, SavedSearch } from 'app/shared/model/saved-search.model';
import { SavedSearchService } from './saved-search.service';
import { INetflixUser } from 'app/shared/model/netflix-user.model';
import { NetflixUserService } from 'app/entities/netflix-user/netflix-user.service';

@Component({
  selector: 'jhi-saved-search-update',
  templateUrl: './saved-search-update.component.html'
})
export class SavedSearchUpdateComponent implements OnInit {
  isSaving = false;
  netflixusers: INetflixUser[] = [];

  editForm = this.fb.group({
    id: [],
    searchText: [],
    user: []
  });

  constructor(
    protected savedSearchService: SavedSearchService,
    protected netflixUserService: NetflixUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ savedSearch }) => {
      this.updateForm(savedSearch);

      this.netflixUserService.query().subscribe((res: HttpResponse<INetflixUser[]>) => (this.netflixusers = res.body || []));
    });
  }

  updateForm(savedSearch: ISavedSearch): void {
    this.editForm.patchValue({
      id: savedSearch.id,
      searchText: savedSearch.searchText,
      user: savedSearch.user
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const savedSearch = this.createFromForm();
    if (savedSearch.id !== undefined) {
      this.subscribeToSaveResponse(this.savedSearchService.update(savedSearch));
    } else {
      this.subscribeToSaveResponse(this.savedSearchService.create(savedSearch));
    }
  }

  private createFromForm(): ISavedSearch {
    return {
      ...new SavedSearch(),
      id: this.editForm.get(['id'])!.value,
      searchText: this.editForm.get(['searchText'])!.value,
      user: this.editForm.get(['user'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISavedSearch>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: INetflixUser): any {
    return item.id;
  }
}
