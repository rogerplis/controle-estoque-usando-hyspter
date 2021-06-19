import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JourneyDetailComponent } from './journey-detail.component';

describe('Component Tests', () => {
  describe('Journey Management Detail Component', () => {
    let comp: JourneyDetailComponent;
    let fixture: ComponentFixture<JourneyDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [JourneyDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ journey: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(JourneyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JourneyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load journey on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.journey).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
