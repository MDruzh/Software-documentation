import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { NetflixUserService } from 'app/entities/netflix-user/netflix-user.service';
import { INetflixUser, NetflixUser } from 'app/shared/model/netflix-user.model';
import { Category } from 'app/shared/model/enumerations/category.model';

describe('Service Tests', () => {
  describe('NetflixUser Service', () => {
    let injector: TestBed;
    let service: NetflixUserService;
    let httpMock: HttpTestingController;
    let elemDefault: INetflixUser;
    let expectedResult: INetflixUser | INetflixUser[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(NetflixUserService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new NetflixUser(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', Category.CHILD, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a NetflixUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            birthDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate
          },
          returnedFromService
        );

        service.create(new NetflixUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a NetflixUser', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            email: 'BBBBBB',
            password: 'BBBBBB',
            bio: 'BBBBBB',
            category: 'BBBBBB',
            birthDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of NetflixUser', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            email: 'BBBBBB',
            password: 'BBBBBB',
            bio: 'BBBBBB',
            category: 'BBBBBB',
            birthDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a NetflixUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
