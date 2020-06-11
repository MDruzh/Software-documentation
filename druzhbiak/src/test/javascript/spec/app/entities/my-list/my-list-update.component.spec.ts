import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { MyListUpdateComponent } from 'app/entities/my-list/my-list-update.component';
import { MyListService } from 'app/entities/my-list/my-list.service';
import { MyList } from 'app/shared/model/my-list.model';

describe('Component Tests', () => {
  describe('MyList Management Update Component', () => {
    let comp: MyListUpdateComponent;
    let fixture: ComponentFixture<MyListUpdateComponent>;
    let service: MyListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [MyListUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MyListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MyListUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MyListService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MyList(123);
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
        const entity = new MyList();
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
