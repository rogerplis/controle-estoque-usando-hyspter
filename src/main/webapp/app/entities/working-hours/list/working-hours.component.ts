import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursService } from '../service/working-hours.service';
import { WorkingHoursDeleteDialogComponent } from '../delete/working-hours-delete-dialog.component';

@Component({
  selector: 'jhi-working-hours',
  templateUrl: './working-hours.component.html',
})
export class WorkingHoursComponent implements OnInit {
  workingHours?: IWorkingHours[];
  isLoading = false;

  constructor(protected workingHoursService: WorkingHoursService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.workingHoursService.query().subscribe(
      (res: HttpResponse<IWorkingHours[]>) => {
        this.isLoading = false;
        this.workingHours = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWorkingHours): number {
    return item.id!;
  }

  delete(workingHours: IWorkingHours): void {
    const modalRef = this.modalService.open(WorkingHoursDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.workingHours = workingHours;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
