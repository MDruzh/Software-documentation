import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMyList } from 'app/shared/model/my-list.model';

type EntityResponseType = HttpResponse<IMyList>;
type EntityArrayResponseType = HttpResponse<IMyList[]>;

@Injectable({ providedIn: 'root' })
export class MyListService {
  public resourceUrl = SERVER_API_URL + 'api/my-lists';

  constructor(protected http: HttpClient) {}

  create(myList: IMyList): Observable<EntityResponseType> {
    return this.http.post<IMyList>(this.resourceUrl, myList, { observe: 'response' });
  }

  update(myList: IMyList): Observable<EntityResponseType> {
    return this.http.put<IMyList>(this.resourceUrl, myList, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMyList>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMyList[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
