import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkingHoursDetailComponent } from './working-hours-detail.component';

describe('Component Tests', () => {
  describe('WorkingHours Management Detail Component', () => {
    let comp: WorkingHoursDetailComponent;
    let fixture: ComponentFixture<WorkingHoursDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkingHoursDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ workingHours: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkingHoursDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkingHoursDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workingHours on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workingHours).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
