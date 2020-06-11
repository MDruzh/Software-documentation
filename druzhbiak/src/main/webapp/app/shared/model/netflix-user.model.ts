import { Moment } from 'moment';
import { IMyList } from 'app/shared/model/my-list.model';
import { ISavedSearch } from 'app/shared/model/saved-search.model';
import { Category } from 'app/shared/model/enumerations/category.model';

export interface INetflixUser {
  id?: number;
  name?: string;
  email?: string;
  password?: string;
  bio?: string;
  category?: Category;
  birthDate?: Moment;
  list?: IMyList;
  savedSearches?: ISavedSearch[];
}

export class NetflixUser implements INetflixUser {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string,
    public password?: string,
    public bio?: string,
    public category?: Category,
    public birthDate?: Moment,
    public list?: IMyList,
    public savedSearches?: ISavedSearch[]
  ) {}
}
