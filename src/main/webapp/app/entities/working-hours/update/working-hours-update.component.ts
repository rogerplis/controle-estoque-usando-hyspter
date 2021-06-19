import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWorkingHours, WorkingHours } from '../working-hours.model';
import { WorkingHoursService } from '../service/working-hours.service';
import { IJourney } from 'app/entities/journey/journey.model';
import { JourneyService } from 'app/entities/journey/service/journey.service';

@Component({
  selector: 'jhi-working-hours-update',
  templateUrl: './working-hours-update.component.html',
})
export class WorkingHoursUpdateComponent implements OnInit {
  isSaving = false;

  journeysSharedCollection: IJourney[] = [];

  editForm = this.fb.group({
    id: [],
    entry: [],
    leavingWork: [],
    extraTime: [],
    extraTime2: [],
    entryRest: [],
    returnRest: [],
    dayWeek: [],
    day: [],
    journeys: [],
  });

  constructor(
    protected workingHoursService: WorkingHoursService,
    protected journeyService: JourneyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workingHours }) => {
      if (workingHours.id === undefined) {
        const today = dayjs().startOf('day');
        workingHours.leavingWork = today;
        workingHours.extraTime = today;
        workingHours.extraTime2 = today;
        workingHours.entryRest = today;
        workingHours.returnRest = today;
      }

      this.updateForm(workingHours);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workingHours = this.createFromForm();
    if (workingHours.id !== undefined) {
      this.subscribeToSaveResponse(this.workingHoursService.update(workingHours));
    } else {
      this.subscribeToSaveResponse(this.workingHoursService.create(workingHours));
    }
  }

  trackJourneyById(index: number, item: IJourney): number {
    return item.id!;
  }

  getSelectedJourney(option: IJourney, selectedVals?: IJourney[]): IJourney {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkingHours>>): void {
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

  protected updateForm(workingHours: IWorkingHours): void {
    this.editForm.patchValue({
      id: workingHours.id,
      entry: workingHours.entry,
      leavingWork: workingHours.leavingWork ? workingHours.leavingWork.format(DATE_TIME_FORMAT) : null,
      extraTime: workingHours.extraTime ? workingHours.extraTime.format(DATE_TIME_FORMAT) : null,
      extraTime2: workingHours.extraTime2 ? workingHours.extraTime2.format(DATE_TIME_FORMAT) : null,
      entryRest: workingHours.entryRest ? workingHours.entryRest.format(DATE_TIME_FORMAT) : null,
      returnRest: workingHours.returnRest ? workingHours.returnRest.format(DATE_TIME_FORMAT) : null,
      dayWeek: workingHours.dayWeek,
      day: workingHours.day,
      journeys: workingHours.journeys,
    });

    this.journeysSharedCollection = this.journeyService.addJourneyToCollectionIfMissing(
      this.journeysSharedCollection,
      ...(workingHours.journeys ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.journeyService
      .query()
      .pipe(map((res: HttpResponse<IJourney[]>) => res.body ?? []))
      .pipe(
        map((journeys: IJourney[]) =>
          this.journeyService.addJourneyToCollectionIfMissing(journeys, ...(this.editForm.get('journeys')!.value ?? []))
        )
      )
      .subscribe((journeys: IJourney[]) => (this.journeysSharedCollection = journeys));
  }

  protected createFromForm(): IWorkingHours {
    return {
      ...new WorkingHours(),
      id: this.editForm.get(['id'])!.value,
      entry: this.editForm.get(['entry'])!.value,
      leavingWork: this.editForm.get(['leavingWork'])!.value
        ? dayjs(this.editForm.get(['leavingWork'])!.value, DATE_TIME_FORMAT)
        : undefined,
      extraTime: this.editForm.get(['extraTime'])!.value ? dayjs(this.editForm.get(['extraTime'])!.value, DATE_TIME_FORMAT) : undefined,
      extraTime2: this.editForm.get(['extraTime2'])!.value ? dayjs(this.editForm.get(['extraTime2'])!.value, DATE_TIME_FORMAT) : undefined,
      entryRest: this.editForm.get(['entryRest'])!.value ? dayjs(this.editForm.get(['entryRest'])!.value, DATE_TIME_FORMAT) : undefined,
      returnRest: this.editForm.get(['returnRest'])!.value ? dayjs(this.editForm.get(['returnRest'])!.value, DATE_TIME_FORMAT) : undefined,
      dayWeek: this.editForm.get(['dayWeek'])!.value,
      day: this.editForm.get(['day'])!.value,
      journeys: this.editForm.get(['journeys'])!.value,
    };
  }
}
