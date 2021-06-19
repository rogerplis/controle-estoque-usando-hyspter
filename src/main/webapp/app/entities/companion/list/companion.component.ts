import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompanion } from '../companion.model';
import { CompanionService } from '../service/companion.service';
import { CompanionDeleteDialogComponent } from '../delete/companion-delete-dialog.component';

@Component({
  selector: 'jhi-companion',
  templateUrl: './companion.component.html',
})
export class CompanionComponent implements OnInit {
  companions?: ICompanion[];
  isLoading = false;

  constructor(protected companionService: CompanionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.companionService.query().subscribe(
      (res: HttpResponse<ICompanion[]>) => {
        this.isLoading = false;
        this.companions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICompanion): number {
    return item.id!;
  }

  delete(companion: ICompanion): void {
    const modalRef = this.modalService.open(CompanionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.companion = companion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
