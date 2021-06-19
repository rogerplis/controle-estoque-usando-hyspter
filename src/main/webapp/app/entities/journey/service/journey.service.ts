import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJourney, getJourneyIdentifier } from '../journey.model';

export type EntityResponseType = HttpResponse<IJourney>;
export type EntityArrayResponseType = HttpResponse<IJourney[]>;

@Injectable({ providedIn: 'root' })
export class JourneyService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/journeys');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(journey: IJourney): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journey);
    return this.http
      .post<IJourney>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(journey: IJourney): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journey);
    return this.http
      .put<IJourney>(`${this.resourceUrl}/${getJourneyIdentifier(journey) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(journey: IJourney): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(journey);
    return this.http
      .patch<IJourney>(`${this.resourceUrl}/${getJourneyIdentifier(journey) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJourney>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJourney[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addJourneyToCollectionIfMissing(journeyCollection: IJourney[], ...journeysToCheck: (IJourney | null | undefined)[]): IJourney[] {
    const journeys: IJourney[] = journeysToCheck.filter(isPresent);
    if (journeys.length > 0) {
      const journeyCollectionIdentifiers = journeyCollection.map(journeyItem => getJourneyIdentifier(journeyItem)!);
      const journeysToAdd = journeys.filter(journeyItem => {
        const journeyIdentifier = getJourneyIdentifier(journeyItem);
        if (journeyIdentifier == null || journeyCollectionIdentifiers.includes(journeyIdentifier)) {
          return false;
        }
        journeyCollectionIdentifiers.push(journeyIdentifier);
        return true;
      });
      return [...journeysToAdd, ...journeyCollection];
    }
    return journeyCollection;
  }

  protected convertDateFromClient(journey: IJourney): IJourney {
    return Object.assign({}, journey, {
      startJourney: journey.startJourney?.isValid() ? journey.startJourney.toJSON() : undefined,
      endJourney: journey.endJourney?.isValid() ? journey.endJourney.toJSON() : undefined,
      dayOut: journey.dayOut?.isValid() ? journey.dayOut.format(DATE_FORMAT) : undefined,
      startDate: journey.startDate?.isValid() ? journey.startDate.format(DATE_FORMAT) : undefined,
      endDate: journey.endDate?.isValid() ? journey.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startJourney = res.body.startJourney ? dayjs(res.body.startJourney) : undefined;
      res.body.endJourney = res.body.endJourney ? dayjs(res.body.endJourney) : undefined;
      res.body.dayOut = res.body.dayOut ? dayjs(res.body.dayOut) : undefined;
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((journey: IJourney) => {
        journey.startJourney = journey.startJourney ? dayjs(journey.startJourney) : undefined;
        journey.endJourney = journey.endJourney ? dayjs(journey.endJourney) : undefined;
        journey.dayOut = journey.dayOut ? dayjs(journey.dayOut) : undefined;
        journey.startDate = journey.startDate ? dayjs(journey.startDate) : undefined;
        journey.endDate = journey.endDate ? dayjs(journey.endDate) : undefined;
      });
    }
    return res;
  }
}
