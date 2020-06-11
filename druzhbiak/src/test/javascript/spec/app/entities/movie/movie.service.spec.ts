import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { MovieService } from 'app/entities/movie/movie.service';
import { IMovie, Movie } from 'app/shared/model/movie.model';
import { Type } from 'app/shared/model/enumerations/type.model';
import { Genre } from 'app/shared/model/enumerations/genre.model';

describe('Service Tests', () => {
  describe('Movie Service', () => {
    let injector: TestBed;
    let service: MovieService;
    let httpMock: HttpTestingController;
    let elemDefault: IMovie;
    let expectedResult: IMovie | IMovie[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(MovieService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Movie(0, 'AAAAAAA', 'AAAAAAA', currentDate, Type.SERIES, Genre.ACTION, 'AAAAAAA', 0, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            releaseDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Movie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            releaseDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.create(new Movie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Movie', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            releaseDate: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            genre: 'BBBBBB',
            creator: 'BBBBBB',
            rating: 1,
            link: 'BBBBBB',
            availableInHD: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Movie', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            releaseDate: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            genre: 'BBBBBB',
            creator: 'BBBBBB',
            rating: 1,
            link: 'BBBBBB',
            availableInHD: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Movie', () => {
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
