import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompanion } from '../companion.model';

@Component({
  selector: 'jhi-companion-detail',
  templateUrl: './companion-detail.component.html',
})
export class CompanionDetailComponent implements OnInit {
  companion: ICompanion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companion }) => {
      this.companion = companion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
