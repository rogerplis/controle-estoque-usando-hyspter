jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICompanion, Companion } from '../companion.model';
import { CompanionService } from '../service/companion.service';

import { CompanionRoutingResolveService } from './companion-routing-resolve.service';

describe('Service Tests', () => {
  describe('Companion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CompanionRoutingResolveService;
    let service: CompanionService;
    let resultCompanion: ICompanion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CompanionRoutingResolveService);
      service = TestBed.inject(CompanionService);
      resultCompanion = undefined;
    });

    describe('resolve', () => {
      it('should return ICompanion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanion).toEqual({ id: 123 });
      });

      it('should return new ICompanion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCompanion).toEqual(new Companion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCompanion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCompanion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
