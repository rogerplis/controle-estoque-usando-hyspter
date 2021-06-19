import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkingHours, getWorkingHoursIdentifier } from '../working-hours.model';

export type EntityResponseType = HttpResponse<IWorkingHours>;
export type EntityArrayResponseType = HttpResponse<IWorkingHours[]>;

@Injectable({ providedIn: 'root' })
export class WorkingHoursService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/working-hours');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(workingHours: IWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .post<IWorkingHours>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workingHours: IWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .put<IWorkingHours>(`${this.resourceUrl}/${getWorkingHoursIdentifier(workingHours) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workingHours: IWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .patch<IWorkingHours>(`${this.resourceUrl}/${getWorkingHoursIdentifier(workingHours) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkingHours>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkingHours[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkingHoursToCollectionIfMissing(
    workingHoursCollection: IWorkingHours[],
    ...workingHoursToCheck: (IWorkingHours | null | undefined)[]
  ): IWorkingHours[] {
    const workingHours: IWorkingHours[] = workingHoursToCheck.filter(isPresent);
    if (workingHours.length > 0) {
      const workingHoursCollectionIdentifiers = workingHoursCollection.map(
        workingHoursItem => getWorkingHoursIdentifier(workingHoursItem)!
      );
      const workingHoursToAdd = workingHours.filter(workingHoursItem => {
        const workingHoursIdentifier = getWorkingHoursIdentifier(workingHoursItem);
        if (workingHoursIdentifier == null || workingHoursCollectionIdentifiers.includes(workingHoursIdentifier)) {
          return false;
        }
        workingHoursCollectionIdentifiers.push(workingHoursIdentifier);
        return true;
      });
      return [...workingHoursToAdd, ...workingHoursCollection];
    }
    return workingHoursCollection;
  }

  protected convertDateFromClient(workingHours: IWorkingHours): IWorkingHours {
    return Object.assign({}, workingHours, {
      entry: workingHours.entry?.isValid() ? workingHours.entry.format(DATE_FORMAT) : undefined,
      leavingWork: workingHours.leavingWork?.isValid() ? workingHours.leavingWork.toJSON() : undefined,
      extraTime: workingHours.extraTime?.isValid() ? workingHours.extraTime.toJSON() : undefined,
      extraTime2: workingHours.extraTime2?.isValid() ? workingHours.extraTime2.toJSON() : undefined,
      entryRest: workingHours.entryRest?.isValid() ? workingHours.entryRest.toJSON() : undefined,
      returnRest: workingHours.returnRest?.isValid() ? workingHours.returnRest.toJSON() : undefined,
      dayWeek: workingHours.dayWeek?.isValid() ? workingHours.dayWeek.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.entry = res.body.entry ? dayjs(res.body.entry) : undefined;
      res.body.leavingWork = res.body.leavingWork ? dayjs(res.body.leavingWork) : undefined;
      res.body.extraTime = res.body.extraTime ? dayjs(res.body.extraTime) : undefined;
      res.body.extraTime2 = res.body.extraTime2 ? dayjs(res.body.extraTime2) : undefined;
      res.body.entryRest = res.body.entryRest ? dayjs(res.body.entryRest) : undefined;
      res.body.returnRest = res.body.returnRest ? dayjs(res.body.returnRest) : undefined;
      res.body.dayWeek = res.body.dayWeek ? dayjs(res.body.dayWeek) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workingHours: IWorkingHours) => {
        workingHours.entry = workingHours.entry ? dayjs(workingHours.entry) : undefined;
        workingHours.leavingWork = workingHours.leavingWork ? dayjs(workingHours.leavingWork) : undefined;
        workingHours.extraTime = workingHours.extraTime ? dayjs(workingHours.extraTime) : undefined;
        workingHours.extraTime2 = workingHours.extraTime2 ? dayjs(workingHours.extraTime2) : undefined;
        workingHours.entryRest = workingHours.entryRest ? dayjs(workingHours.entryRest) : undefined;
        workingHours.returnRest = workingHours.returnRest ? dayjs(workingHours.returnRest) : undefined;
        workingHours.dayWeek = workingHours.dayWeek ? dayjs(workingHours.dayWeek) : undefined;
      });
    }
    return res;
  }
}
