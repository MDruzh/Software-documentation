import { Moment } from 'moment';
import { IEpisode } from 'app/shared/model/episode.model';
import { IMyList } from 'app/shared/model/my-list.model';
import { Type } from 'app/shared/model/enumerations/type.model';
import { Genre } from 'app/shared/model/enumerations/genre.model';

export interface IMovie {
  id?: number;
  name?: string;
  description?: string;
  releaseDate?: Moment;
  type?: Type;
  genre?: Genre;
  creator?: string;
  rating?: number;
  link?: string;
  availableInHD?: boolean;
  episodes?: IEpisode[];
  myList?: IMyList;
}

export class Movie implements IMovie {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public releaseDate?: Moment,
    public type?: Type,
    public genre?: Genre,
    public creator?: string,
    public rating?: number,
    public link?: string,
    public availableInHD?: boolean,
    public episodes?: IEpisode[],
    public myList?: IMyList
  ) {
    this.availableInHD = this.availableInHD || false;
  }
}
