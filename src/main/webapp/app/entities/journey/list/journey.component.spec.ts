import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { JourneyService } from '../service/journey.service';

import { JourneyComponent } from './journey.component';

describe('Component Tests', () => {
  describe('Journey Management Component', () => {
    let comp: JourneyComponent;
    let fixture: ComponentFixture<JourneyComponent>;
    let service: JourneyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JourneyComponent],
      })
        .overrideTemplate(JourneyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JourneyComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(JourneyService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.journeys?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
