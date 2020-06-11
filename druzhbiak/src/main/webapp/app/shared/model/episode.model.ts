import { Moment } from 'moment';
import { IMovie } from 'app/shared/model/movie.model';

export interface IEpisode {
  id?: number;
  name?: string;
  description?: string;
  producer?: string;
  releaseDate?: Moment;
  videoURL?: string;
  series?: IMovie;
}

export class Episode implements IEpisode {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public producer?: string,
    public releaseDate?: Moment,
    public videoURL?: string,
    public series?: IMovie
  ) {}
}
