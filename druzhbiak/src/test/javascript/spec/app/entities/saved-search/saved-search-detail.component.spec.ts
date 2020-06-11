import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { SavedSearchDetailComponent } from 'app/entities/saved-search/saved-search-detail.component';
import { SavedSearch } from 'app/shared/model/saved-search.model';

describe('Component Tests', () => {
  describe('SavedSearch Management Detail Component', () => {
    let comp: SavedSearchDetailComponent;
    let fixture: ComponentFixture<SavedSearchDetailComponent>;
    const route = ({ data: of({ savedSearch: new SavedSearch(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [SavedSearchDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SavedSearchDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SavedSearchDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load savedSearch on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.savedSearch).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
