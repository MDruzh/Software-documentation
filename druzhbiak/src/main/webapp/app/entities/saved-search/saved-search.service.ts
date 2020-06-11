import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISavedSearch } from 'app/shared/model/saved-search.model';

type EntityResponseType = HttpResponse<ISavedSearch>;
type EntityArrayResponseType = HttpResponse<ISavedSearch[]>;

@Injectable({ providedIn: 'root' })
export class SavedSearchService {
  public resourceUrl = SERVER_API_URL + 'api/saved-searches';

  constructor(protected http: HttpClient) {}

  create(savedSearch: ISavedSearch): Observable<EntityResponseType> {
    return this.http.post<ISavedSearch>(this.resourceUrl, savedSearch, { observe: 'response' });
  }

  update(savedSearch: ISavedSearch): Observable<EntityResponseType> {
    return this.http.put<ISavedSearch>(this.resourceUrl, savedSearch, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISavedSearch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISavedSearch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
