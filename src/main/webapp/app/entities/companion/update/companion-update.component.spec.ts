jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompanionService } from '../service/companion.service';
import { ICompanion, Companion } from '../companion.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

import { CompanionUpdateComponent } from './companion-update.component';

describe('Component Tests', () => {
  describe('Companion Management Update Component', () => {
    let comp: CompanionUpdateComponent;
    let fixture: ComponentFixture<CompanionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let companionService: CompanionService;
    let locationService: LocationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompanionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      companionService = TestBed.inject(CompanionService);
      locationService = TestBed.inject(LocationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call location query and add missing value', () => {
        const companion: ICompanion = { id: 456 };
        const location: ILocation = { id: 76819 };
        companion.location = location;

        const locationCollection: ILocation[] = [{ id: 65329 }];
        spyOn(locationService, 'query').and.returnValue(of(new HttpResponse({ body: locationCollection })));
        const expectedCollection: ILocation[] = [location, ...locationCollection];
        spyOn(locationService, 'addLocationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ companion });
        comp.ngOnInit();

        expect(locationService.query).toHaveBeenCalled();
        expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, location);
        expect(comp.locationsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const companion: ICompanion = { id: 456 };
        const location: ILocation = { id: 73788 };
        companion.location = location;

        activatedRoute.data = of({ companion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(companion));
        expect(comp.locationsCollection).toContain(location);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companion = { id: 123 };
        spyOn(companionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(companionService.update).toHaveBeenCalledWith(companion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companion = new Companion();
        spyOn(companionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: companion }));
        saveSubject.complete();

        // THEN
        expect(companionService.create).toHaveBeenCalledWith(companion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const companion = { id: 123 };
        spyOn(companionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ companion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(companionService.update).toHaveBeenCalledWith(companion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLocationById', () => {
        it('Should return tracked Location primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
