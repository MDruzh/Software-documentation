import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { NetflixUserUpdateComponent } from 'app/entities/netflix-user/netflix-user-update.component';
import { NetflixUserService } from 'app/entities/netflix-user/netflix-user.service';
import { NetflixUser } from 'app/shared/model/netflix-user.model';

describe('Component Tests', () => {
  describe('NetflixUser Management Update Component', () => {
    let comp: NetflixUserUpdateComponent;
    let fixture: ComponentFixture<NetflixUserUpdateComponent>;
    let service: NetflixUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [NetflixUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(NetflixUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NetflixUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(NetflixUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new NetflixUser(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new NetflixUser();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
