import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEpisode } from 'app/shared/model/episode.model';

type EntityResponseType = HttpResponse<IEpisode>;
type EntityArrayResponseType = HttpResponse<IEpisode[]>;

@Injectable({ providedIn: 'root' })
export class EpisodeService {
  public resourceUrl = SERVER_API_URL + 'api/episodes';

  constructor(protected http: HttpClient) {}

  create(episode: IEpisode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(episode);
    return this.http
      .post<IEpisode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(episode: IEpisode): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(episode);
    return this.http
      .put<IEpisode>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEpisode>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEpisode[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(episode: IEpisode): IEpisode {
    const copy: IEpisode = Object.assign({}, episode, {
      releaseDate: episode.releaseDate && episode.releaseDate.isValid() ? episode.releaseDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.releaseDate = res.body.releaseDate ? moment(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((episode: IEpisode) => {
        episode.releaseDate = episode.releaseDate ? moment(episode.releaseDate) : undefined;
      });
    }
    return res;
  }
}
