import { ILocation } from 'app/entities/location/location.model';

export interface ICompanion {
  id?: number;
  companyName?: string | null;
  cnpj?: string | null;
  location?: ILocation | null;
}

export class Companion implements ICompanion {
  constructor(public id?: number, public companyName?: string | null, public cnpj?: string | null, public location?: ILocation | null) {}
}

export function getCompanionIdentifier(companion: ICompanion): number | undefined {
  return companion.id;
}
