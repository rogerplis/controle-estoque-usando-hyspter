import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompanion, Companion } from '../companion.model';
import { CompanionService } from '../service/companion.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

@Component({
  selector: 'jhi-companion-update',
  templateUrl: './companion-update.component.html',
})
export class CompanionUpdateComponent implements OnInit {
  isSaving = false;

  locationsCollection: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    companyName: [],
    cnpj: [],
    location: [],
  });

  constructor(
    protected companionService: CompanionService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ companion }) => {
      this.updateForm(companion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const companion = this.createFromForm();
    if (companion.id !== undefined) {
      this.subscribeToSaveResponse(this.companionService.update(companion));
    } else {
      this.subscribeToSaveResponse(this.companionService.create(companion));
    }
  }

  trackLocationById(index: number, item: ILocation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompanion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(companion: ICompanion): void {
    this.editForm.patchValue({
      id: companion.id,
      companyName: companion.companyName,
      cnpj: companion.cnpj,
      location: companion.location,
    });

    this.locationsCollection = this.locationService.addLocationToCollectionIfMissing(this.locationsCollection, companion.location);
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query({ filter: 'companion-is-null' })
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(locations, this.editForm.get('location')!.value)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsCollection = locations));
  }

  protected createFromForm(): ICompanion {
    return {
      ...new Companion(),
      id: this.editForm.get(['id'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      cnpj: this.editForm.get(['cnpj'])!.value,
      location: this.editForm.get(['location'])!.value,
    };
  }
}
