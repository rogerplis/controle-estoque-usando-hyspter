import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJourney } from '../journey.model';
import { JourneyService } from '../service/journey.service';
import { JourneyDeleteDialogComponent } from '../delete/journey-delete-dialog.component';

@Component({
  selector: 'jhi-journey',
  templateUrl: './journey.component.html',
})
export class JourneyComponent implements OnInit {
  journeys?: IJourney[];
  isLoading = false;

  constructor(protected journeyService: JourneyService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.journeyService.query().subscribe(
      (res: HttpResponse<IJourney[]>) => {
        this.isLoading = false;
        this.journeys = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IJourney): number {
    return item.id!;
  }

  delete(journey: IJourney): void {
    const modalRef = this.modalService.open(JourneyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.journey = journey;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
