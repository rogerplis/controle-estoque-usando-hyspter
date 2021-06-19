import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompanion, Companion } from '../companion.model';

import { CompanionService } from './companion.service';

describe('Service Tests', () => {
  describe('Companion Service', () => {
    let service: CompanionService;
    let httpMock: HttpTestingController;
    let elemDefault: ICompanion;
    let expectedResult: ICompanion | ICompanion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CompanionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        companyName: 'AAAAAAA',
        cnpj: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Companion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Companion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Companion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            companyName: 'BBBBBB',
            cnpj: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Companion', () => {
        const patchObject = Object.assign({}, new Companion());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Companion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            companyName: 'BBBBBB',
            cnpj: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Companion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCompanionToCollectionIfMissing', () => {
        it('should add a Companion to an empty array', () => {
          const companion: ICompanion = { id: 123 };
          expectedResult = service.addCompanionToCollectionIfMissing([], companion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companion);
        });

        it('should not add a Companion to an array that contains it', () => {
          const companion: ICompanion = { id: 123 };
          const companionCollection: ICompanion[] = [
            {
              ...companion,
            },
            { id: 456 },
          ];
          expectedResult = service.addCompanionToCollectionIfMissing(companionCollection, companion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Companion to an array that doesn't contain it", () => {
          const companion: ICompanion = { id: 123 };
          const companionCollection: ICompanion[] = [{ id: 456 }];
          expectedResult = service.addCompanionToCollectionIfMissing(companionCollection, companion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companion);
        });

        it('should add only unique Companion to an array', () => {
          const companionArray: ICompanion[] = [{ id: 123 }, { id: 456 }, { id: 19357 }];
          const companionCollection: ICompanion[] = [{ id: 123 }];
          expectedResult = service.addCompanionToCollectionIfMissing(companionCollection, ...companionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const companion: ICompanion = { id: 123 };
          const companion2: ICompanion = { id: 456 };
          expectedResult = service.addCompanionToCollectionIfMissing([], companion, companion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(companion);
          expect(expectedResult).toContain(companion2);
        });

        it('should accept null and undefined values', () => {
          const companion: ICompanion = { id: 123 };
          expectedResult = service.addCompanionToCollectionIfMissing([], null, companion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(companion);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
