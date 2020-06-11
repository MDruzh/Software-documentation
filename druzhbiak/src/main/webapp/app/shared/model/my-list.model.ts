import { IMovie } from 'app/shared/model/movie.model';
import { INetflixUser } from 'app/shared/model/netflix-user.model';

export interface IMyList {
  id?: number;
  movies?: IMovie[];
  user?: INetflixUser;
}

export class MyList implements IMyList {
  constructor(public id?: number, public movies?: IMovie[], public user?: INetflixUser) {}
}
