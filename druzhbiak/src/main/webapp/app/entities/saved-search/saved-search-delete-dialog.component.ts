import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISavedSearch } from 'app/shared/model/saved-search.model';
import { SavedSearchService } from './saved-search.service';

@Component({
  templateUrl: './saved-search-delete-dialog.component.html'
})
export class SavedSearchDeleteDialogComponent {
  savedSearch?: ISavedSearch;

  constructor(
    protected savedSearchService: SavedSearchService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.savedSearchService.delete(id).subscribe(() => {
      this.eventManager.broadcast('savedSearchListModification');
      this.activeModal.close();
    });
  }
}
