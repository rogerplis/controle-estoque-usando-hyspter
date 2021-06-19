import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompanionDetailComponent } from './companion-detail.component';

describe('Component Tests', () => {
  describe('Companion Management Detail Component', () => {
    let comp: CompanionDetailComponent;
    let fixture: ComponentFixture<CompanionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CompanionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ companion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CompanionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompanionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load companion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.companion).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
