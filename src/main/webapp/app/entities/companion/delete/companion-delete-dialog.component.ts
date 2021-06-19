import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanion } from '../companion.model';
import { CompanionService } from '../service/companion.service';

@Component({
  templateUrl: './companion-delete-dialog.component.html',
})
export class CompanionDeleteDialogComponent {
  companion?: ICompanion;

  constructor(protected companionService: CompanionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.companionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
