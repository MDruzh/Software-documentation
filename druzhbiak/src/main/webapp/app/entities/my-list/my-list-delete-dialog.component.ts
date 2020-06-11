import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMyList } from 'app/shared/model/my-list.model';
import { MyListService } from './my-list.service';

@Component({
  templateUrl: './my-list-delete-dialog.component.html'
})
export class MyListDeleteDialogComponent {
  myList?: IMyList;

  constructor(protected myListService: MyListService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.myListService.delete(id).subscribe(() => {
      this.eventManager.broadcast('myListListModification');
      this.activeModal.close();
    });
  }
}
