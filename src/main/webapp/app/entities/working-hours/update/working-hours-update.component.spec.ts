jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkingHoursService } from '../service/working-hours.service';
import { IWorkingHours, WorkingHours } from '../working-hours.model';
import { IJourney } from 'app/entities/journey/journey.model';
import { JourneyService } from 'app/entities/journey/service/journey.service';

import { WorkingHoursUpdateComponent } from './working-hours-update.component';

describe('Component Tests', () => {
  describe('WorkingHours Management Update Component', () => {
    let comp: WorkingHoursUpdateComponent;
    let fixture: ComponentFixture<WorkingHoursUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workingHoursService: WorkingHoursService;
    let journeyService: JourneyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkingHoursUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkingHoursUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkingHoursUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workingHoursService = TestBed.inject(WorkingHoursService);
      journeyService = TestBed.inject(JourneyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Journey query and add missing value', () => {
        const workingHours: IWorkingHours = { id: 456 };
        const journeys: IJourney[] = [{ id: 64220 }];
        workingHours.journeys = journeys;

        const journeyCollection: IJourney[] = [{ id: 95428 }];
        spyOn(journeyService, 'query').and.returnValue(of(new HttpResponse({ body: journeyCollection })));
        const additionalJourneys = [...journeys];
        const expectedCollection: IJourney[] = [...additionalJourneys, ...journeyCollection];
        spyOn(journeyService, 'addJourneyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ workingHours });
        comp.ngOnInit();

        expect(journeyService.query).toHaveBeenCalled();
        expect(journeyService.addJourneyToCollectionIfMissing).toHaveBeenCalledWith(journeyCollection, ...additionalJourneys);
        expect(comp.journeysSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const workingHours: IWorkingHours = { id: 456 };
        const journeys: IJourney = { id: 56502 };
        workingHours.journeys = [journeys];

        activatedRoute.data = of({ workingHours });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workingHours));
        expect(comp.journeysSharedCollection).toContain(journeys);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workingHours = { id: 123 };
        spyOn(workingHoursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHours });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingHours }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workingHoursService.update).toHaveBeenCalledWith(workingHours);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workingHours = new WorkingHours();
        spyOn(workingHoursService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHours });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workingHours }));
        saveSubject.complete();

        // THEN
        expect(workingHoursService.create).toHaveBeenCalledWith(workingHours);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workingHours = { id: 123 };
        spyOn(workingHoursService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workingHours });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workingHoursService.update).toHaveBeenCalledWith(workingHours);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackJourneyById', () => {
        it('Should return tracked Journey primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackJourneyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedJourney', () => {
        it('Should return option if no Journey is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedJourney(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Journey for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedJourney(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Journey is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedJourney(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
