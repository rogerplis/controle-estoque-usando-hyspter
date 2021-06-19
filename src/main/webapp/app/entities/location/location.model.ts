import { ICountry } from 'app/entities/country/country.model';

export interface ILocation {
  id?: number;
  streetAddress?: string | null;
  numberAddress?: number | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  country?: ICountry | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public streetAddress?: string | null,
    public numberAddress?: number | null,
    public postalCode?: string | null,
    public city?: string | null,
    public stateProvince?: string | null,
    public country?: ICountry | null
  ) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
