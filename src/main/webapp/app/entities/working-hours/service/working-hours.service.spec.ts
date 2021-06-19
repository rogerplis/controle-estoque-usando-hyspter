import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Days } from 'app/entities/enumerations/days.model';
import { IWorkingHours, WorkingHours } from '../working-hours.model';

import { WorkingHoursService } from './working-hours.service';

describe('Service Tests', () => {
  describe('WorkingHours Service', () => {
    let service: WorkingHoursService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkingHours;
    let expectedResult: IWorkingHours | IWorkingHours[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkingHoursService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        entry: currentDate,
        leavingWork: currentDate,
        extraTime: currentDate,
        extraTime2: currentDate,
        entryRest: currentDate,
        returnRest: currentDate,
        dayWeek: currentDate,
        day: Days.SEGUNDA,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            entry: currentDate.format(DATE_FORMAT),
            leavingWork: currentDate.format(DATE_TIME_FORMAT),
            extraTime: currentDate.format(DATE_TIME_FORMAT),
            extraTime2: currentDate.format(DATE_TIME_FORMAT),
            entryRest: currentDate.format(DATE_TIME_FORMAT),
            returnRest: currentDate.format(DATE_TIME_FORMAT),
            dayWeek: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a WorkingHours', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            entry: currentDate.format(DATE_FORMAT),
            leavingWork: currentDate.format(DATE_TIME_FORMAT),
            extraTime: currentDate.format(DATE_TIME_FORMAT),
            extraTime2: currentDate.format(DATE_TIME_FORMAT),
            entryRest: currentDate.format(DATE_TIME_FORMAT),
            returnRest: currentDate.format(DATE_TIME_FORMAT),
            dayWeek: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entry: currentDate,
            leavingWork: currentDate,
            extraTime: currentDate,
            extraTime2: currentDate,
            entryRest: currentDate,
            returnRest: currentDate,
            dayWeek: currentDate,
          },
          returnedFromService
        );

        service.create(new WorkingHours()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkingHours', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entry: currentDate.format(DATE_FORMAT),
            leavingWork: currentDate.format(DATE_TIME_FORMAT),
            extraTime: currentDate.format(DATE_TIME_FORMAT),
            extraTime2: currentDate.format(DATE_TIME_FORMAT),
            entryRest: currentDate.format(DATE_TIME_FORMAT),
            returnRest: currentDate.format(DATE_TIME_FORMAT),
            dayWeek: currentDate.format(DATE_FORMAT),
            day: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entry: currentDate,
            leavingWork: currentDate,
            extraTime: currentDate,
            extraTime2: currentDate,
            entryRest: currentDate,
            returnRest: currentDate,
            dayWeek: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkingHours', () => {
        const patchObject = Object.assign(
          {
            entry: currentDate.format(DATE_FORMAT),
            leavingWork: currentDate.format(DATE_TIME_FORMAT),
            extraTime: currentDate.format(DATE_TIME_FORMAT),
            dayWeek: currentDate.format(DATE_FORMAT),
            day: 'BBBBBB',
          },
          new WorkingHours()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            entry: currentDate,
            leavingWork: currentDate,
            extraTime: currentDate,
            extraTime2: currentDate,
            entryRest: currentDate,
            returnRest: currentDate,
            dayWeek: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkingHours', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entry: currentDate.format(DATE_FORMAT),
            leavingWork: currentDate.format(DATE_TIME_FORMAT),
            extraTime: currentDate.format(DATE_TIME_FORMAT),
            extraTime2: currentDate.format(DATE_TIME_FORMAT),
            entryRest: currentDate.format(DATE_TIME_FORMAT),
            returnRest: currentDate.format(DATE_TIME_FORMAT),
            dayWeek: currentDate.format(DATE_FORMAT),
            day: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            entry: currentDate,
            leavingWork: currentDate,
            extraTime: currentDate,
            extraTime2: currentDate,
            entryRest: currentDate,
            returnRest: currentDate,
            dayWeek: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a WorkingHours', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkingHoursToCollectionIfMissing', () => {
        it('should add a WorkingHours to an empty array', () => {
          const workingHours: IWorkingHours = { id: 123 };
          expectedResult = service.addWorkingHoursToCollectionIfMissing([], workingHours);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingHours);
        });

        it('should not add a WorkingHours to an array that contains it', () => {
          const workingHours: IWorkingHours = { id: 123 };
          const workingHoursCollection: IWorkingHours[] = [
            {
              ...workingHours,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, workingHours);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkingHours to an array that doesn't contain it", () => {
          const workingHours: IWorkingHours = { id: 123 };
          const workingHoursCollection: IWorkingHours[] = [{ id: 456 }];
          expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, workingHours);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingHours);
        });

        it('should add only unique WorkingHours to an array', () => {
          const workingHoursArray: IWorkingHours[] = [{ id: 123 }, { id: 456 }, { id: 3974 }];
          const workingHoursCollection: IWorkingHours[] = [{ id: 123 }];
          expectedResult = service.addWorkingHoursToCollectionIfMissing(workingHoursCollection, ...workingHoursArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workingHours: IWorkingHours = { id: 123 };
          const workingHours2: IWorkingHours = { id: 456 };
          expectedResult = service.addWorkingHoursToCollectionIfMissing([], workingHours, workingHours2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workingHours);
          expect(expectedResult).toContain(workingHours2);
        });

        it('should accept null and undefined values', () => {
          const workingHours: IWorkingHours = { id: 123 };
          expectedResult = service.addWorkingHoursToCollectionIfMissing([], null, workingHours, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workingHours);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
