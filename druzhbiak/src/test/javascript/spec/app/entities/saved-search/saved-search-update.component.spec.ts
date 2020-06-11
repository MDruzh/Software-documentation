import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { SavedSearchUpdateComponent } from 'app/entities/saved-search/saved-search-update.component';
import { SavedSearchService } from 'app/entities/saved-search/saved-search.service';
import { SavedSearch } from 'app/shared/model/saved-search.model';

describe('Component Tests', () => {
  describe('SavedSearch Management Update Component', () => {
    let comp: SavedSearchUpdateComponent;
    let fixture: ComponentFixture<SavedSearchUpdateComponent>;
    let service: SavedSearchService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [SavedSearchUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SavedSearchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SavedSearchUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SavedSearchService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SavedSearch(123);
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
        const entity = new SavedSearch();
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
