import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CompanionService } from '../service/companion.service';

import { CompanionComponent } from './companion.component';

describe('Component Tests', () => {
  describe('Companion Management Component', () => {
    let comp: CompanionComponent;
    let fixture: ComponentFixture<CompanionComponent>;
    let service: CompanionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompanionComponent],
      })
        .overrideTemplate(CompanionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompanionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CompanionService);

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
      expect(comp.companions?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
