jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IJourney, Journey } from '../journey.model';
import { JourneyService } from '../service/journey.service';

import { JourneyRoutingResolveService } from './journey-routing-resolve.service';

describe('Service Tests', () => {
  describe('Journey routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: JourneyRoutingResolveService;
    let service: JourneyService;
    let resultJourney: IJourney | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(JourneyRoutingResolveService);
      service = TestBed.inject(JourneyService);
      resultJourney = undefined;
    });

    describe('resolve', () => {
      it('should return IJourney returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJourney = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJourney).toEqual({ id: 123 });
      });

      it('should return new IJourney if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJourney = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultJourney).toEqual(new Journey());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJourney = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJourney).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
