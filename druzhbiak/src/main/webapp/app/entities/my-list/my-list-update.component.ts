import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMyList, MyList } from 'app/shared/model/my-list.model';
import { MyListService } from './my-list.service';

@Component({
  selector: 'jhi-my-list-update',
  templateUrl: './my-list-update.component.html'
})
export class MyListUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: []
  });

  constructor(protected myListService: MyListService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ myList }) => {
      this.updateForm(myList);
    });
  }

  updateForm(myList: IMyList): void {
    this.editForm.patchValue({
      id: myList.id
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const myList = this.createFromForm();
    if (myList.id !== undefined) {
      this.subscribeToSaveResponse(this.myListService.update(myList));
    } else {
      this.subscribeToSaveResponse(this.myListService.create(myList));
    }
  }

  private createFromForm(): IMyList {
    return {
      ...new MyList(),
      id: this.editForm.get(['id'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMyList>>): void {
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
}
