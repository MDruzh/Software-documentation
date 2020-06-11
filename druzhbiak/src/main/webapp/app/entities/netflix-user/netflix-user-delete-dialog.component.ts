import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { INetflixUser } from 'app/shared/model/netflix-user.model';
import { NetflixUserService } from './netflix-user.service';

@Component({
  templateUrl: './netflix-user-delete-dialog.component.html'
})
export class NetflixUserDeleteDialogComponent {
  netflixUser?: INetflixUser;

  constructor(
    protected netflixUserService: NetflixUserService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.netflixUserService.delete(id).subscribe(() => {
      this.eventManager.broadcast('netflixUserListModification');
      this.activeModal.close();
    });
  }
}
