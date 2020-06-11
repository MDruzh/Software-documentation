import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { MyListDetailComponent } from 'app/entities/my-list/my-list-detail.component';
import { MyList } from 'app/shared/model/my-list.model';

describe('Component Tests', () => {
  describe('MyList Management Detail Component', () => {
    let comp: MyListDetailComponent;
    let fixture: ComponentFixture<MyListDetailComponent>;
    const route = ({ data: of({ myList: new MyList(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [MyListDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MyListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MyListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load myList on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.myList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
