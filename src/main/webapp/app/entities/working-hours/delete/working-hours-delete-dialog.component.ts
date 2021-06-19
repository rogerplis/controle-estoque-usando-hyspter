import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursService } from '../service/working-hours.service';

@Component({
  templateUrl: './working-hours-delete-dialog.component.html',
})
export class WorkingHoursDeleteDialogComponent {
  workingHours?: IWorkingHours;

  constructor(protected workingHoursService: WorkingHoursService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workingHoursService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
