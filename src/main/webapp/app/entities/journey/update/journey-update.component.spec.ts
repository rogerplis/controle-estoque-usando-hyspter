jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JourneyService } from '../service/journey.service';
import { IJourney, Journey } from '../journey.model';

import { JourneyUpdateComponent } from './journey-update.component';

describe('Component Tests', () => {
  describe('Journey Management Update Component', () => {
    let comp: JourneyUpdateComponent;
    let fixture: ComponentFixture<JourneyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let journeyService: JourneyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JourneyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JourneyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JourneyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      journeyService = TestBed.inject(JourneyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const journey: IJourney = { id: 456 };

        activatedRoute.data = of({ journey });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(journey));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const journey = { id: 123 };
        spyOn(journeyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ journey });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: journey }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(journeyService.update).toHaveBeenCalledWith(journey);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const journey = new Journey();
        spyOn(journeyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ journey });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: journey }));
        saveSubject.complete();

        // THEN
        expect(journeyService.create).toHaveBeenCalledWith(journey);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const journey = { id: 123 };
        spyOn(journeyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ journey });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(journeyService.update).toHaveBeenCalledWith(journey);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
