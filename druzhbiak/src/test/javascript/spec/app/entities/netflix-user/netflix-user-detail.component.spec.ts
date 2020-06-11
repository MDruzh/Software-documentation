import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NetflixTestModule } from '../../../test.module';
import { NetflixUserDetailComponent } from 'app/entities/netflix-user/netflix-user-detail.component';
import { NetflixUser } from 'app/shared/model/netflix-user.model';

describe('Component Tests', () => {
  describe('NetflixUser Management Detail Component', () => {
    let comp: NetflixUserDetailComponent;
    let fixture: ComponentFixture<NetflixUserDetailComponent>;
    const route = ({ data: of({ netflixUser: new NetflixUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [NetflixTestModule],
        declarations: [NetflixUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NetflixUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NetflixUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load netflixUser on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.netflixUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
