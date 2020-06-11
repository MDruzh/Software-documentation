import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { INetflixUser, NetflixUser } from 'app/shared/model/netflix-user.model';
import { NetflixUserService } from './netflix-user.service';
import { IMyList } from 'app/shared/model/my-list.model';
import { MyListService } from 'app/entities/my-list/my-list.service';

@Component({
  selector: 'jhi-netflix-user-update',
  templateUrl: './netflix-user-update.component.html'
})
export class NetflixUserUpdateComponent implements OnInit {
  isSaving = false;
  lists: IMyList[] = [];
  birthDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [],
    email: [],
    password: [],
    bio: [],
    category: [],
    birthDate: [],
    list: []
  });

  constructor(
    protected netflixUserService: NetflixUserService,
    protected myListService: MyListService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netflixUser }) => {
      this.updateForm(netflixUser);

      this.myListService
        .query({ 'userId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IMyList[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IMyList[]) => {
          if (!netflixUser.list || !netflixUser.list.id) {
            this.lists = resBody;
          } else {
            this.myListService
              .find(netflixUser.list.id)
              .pipe(
                map((subRes: HttpResponse<IMyList>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IMyList[]) => (this.lists = concatRes));
          }
        });
    });
  }

  updateForm(netflixUser: INetflixUser): void {
    this.editForm.patchValue({
      id: netflixUser.id,
      name: netflixUser.name,
      email: netflixUser.email,
      password: netflixUser.password,
      bio: netflixUser.bio,
      category: netflixUser.category,
      birthDate: netflixUser.birthDate,
      list: netflixUser.list
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const netflixUser = this.createFromForm();
    if (netflixUser.id !== undefined) {
      this.subscribeToSaveResponse(this.netflixUserService.update(netflixUser));
    } else {
      this.subscribeToSaveResponse(this.netflixUserService.create(netflixUser));
    }
  }

  private createFromForm(): INetflixUser {
    return {
      ...new NetflixUser(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      email: this.editForm.get(['email'])!.value,
      password: this.editForm.get(['password'])!.value,
      bio: this.editForm.get(['bio'])!.value,
      category: this.editForm.get(['category'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      list: this.editForm.get(['list'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INetflixUser>>): void {
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

  trackById(index: number, item: IMyList): any {
    return item.id;
  }
}
