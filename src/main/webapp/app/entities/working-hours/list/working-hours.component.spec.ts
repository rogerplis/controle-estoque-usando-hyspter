import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WorkingHoursService } from '../service/working-hours.service';

import { WorkingHoursComponent } from './working-hours.component';

describe('Component Tests', () => {
  describe('WorkingHours Management Component', () => {
    let comp: WorkingHoursComponent;
    let fixture: ComponentFixture<WorkingHoursComponent>;
    let service: WorkingHoursService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkingHoursComponent],
      })
        .overrideTemplate(WorkingHoursComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkingHoursComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(WorkingHoursService);

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
      expect(comp.workingHours?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
