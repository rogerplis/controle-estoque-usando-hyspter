import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompanion, getCompanionIdentifier } from '../companion.model';

export type EntityResponseType = HttpResponse<ICompanion>;
export type EntityArrayResponseType = HttpResponse<ICompanion[]>;

@Injectable({ providedIn: 'root' })
export class CompanionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/companions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(companion: ICompanion): Observable<EntityResponseType> {
    return this.http.post<ICompanion>(this.resourceUrl, companion, { observe: 'response' });
  }

  update(companion: ICompanion): Observable<EntityResponseType> {
    return this.http.put<ICompanion>(`${this.resourceUrl}/${getCompanionIdentifier(companion) as number}`, companion, {
      observe: 'response',
    });
  }

  partialUpdate(companion: ICompanion): Observable<EntityResponseType> {
    return this.http.patch<ICompanion>(`${this.resourceUrl}/${getCompanionIdentifier(companion) as number}`, companion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompanion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompanion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompanionToCollectionIfMissing(
    companionCollection: ICompanion[],
    ...companionsToCheck: (ICompanion | null | undefined)[]
  ): ICompanion[] {
    const companions: ICompanion[] = companionsToCheck.filter(isPresent);
    if (companions.length > 0) {
      const companionCollectionIdentifiers = companionCollection.map(companionItem => getCompanionIdentifier(companionItem)!);
      const companionsToAdd = companions.filter(companionItem => {
        const companionIdentifier = getCompanionIdentifier(companionItem);
        if (companionIdentifier == null || companionCollectionIdentifiers.includes(companionIdentifier)) {
          return false;
        }
        companionCollectionIdentifiers.push(companionIdentifier);
        return true;
      });
      return [...companionsToAdd, ...companionCollection];
    }
    return companionCollection;
  }
}
