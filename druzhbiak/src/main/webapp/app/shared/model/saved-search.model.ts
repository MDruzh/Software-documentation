import { INetflixUser } from 'app/shared/model/netflix-user.model';

export interface ISavedSearch {
  id?: number;
  searchText?: string;
  user?: INetflixUser;
}

export class SavedSearch implements ISavedSearch {
  constructor(public id?: number, public searchText?: string, public user?: INetflixUser) {}
}
