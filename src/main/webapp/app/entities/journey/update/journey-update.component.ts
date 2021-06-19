import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IJourney, Journey } from '../journey.model';
import { JourneyService } from '../service/journey.service';

@Component({
  selector: 'jhi-journey-update',
  templateUrl: './journey-update.component.html',
})
export class JourneyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    journeyName: [],
    tolerance: [],
    startJourney: [],
    endJourney: [],
    dayOut: [],
    startDate: [],
    endDate: [],
  });

  constructor(protected journeyService: JourneyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ journey }) => {
      if (journey.id === undefined) {
        const today = dayjs().startOf('day');
        journey.startJourney = today;
        journey.endJourney = today;
      }

      this.updateForm(journey);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const journey = this.createFromForm();
    if (journey.id !== undefined) {
      this.subscribeToSaveResponse(this.journeyService.update(journey));
    } else {
      this.subscribeToSaveResponse(this.journeyService.create(journey));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJourney>>): void {
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

  protected updateForm(journey: IJourney): void {
    this.editForm.patchValue({
      id: journey.id,
      journeyName: journey.journeyName,
      tolerance: journey.tolerance,
      startJourney: journey.startJourney ? journey.startJourney.format(DATE_TIME_FORMAT) : null,
      endJourney: journey.endJourney ? journey.endJourney.format(DATE_TIME_FORMAT) : null,
      dayOut: journey.dayOut,
      startDate: journey.startDate,
      endDate: journey.endDate,
    });
  }

  protected createFromForm(): IJourney {
    return {
      ...new Journey(),
      id: this.editForm.get(['id'])!.value,
      journeyName: this.editForm.get(['journeyName'])!.value,
      tolerance: this.editForm.get(['tolerance'])!.value,
      startJourney: this.editForm.get(['startJourney'])!.value
        ? dayjs(this.editForm.get(['startJourney'])!.value, DATE_TIME_FORMAT)
        : undefined,
      endJourney: this.editForm.get(['endJourney'])!.value ? dayjs(this.editForm.get(['endJourney'])!.value, DATE_TIME_FORMAT) : undefined,
      dayOut: this.editForm.get(['dayOut'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
    };
  }
}
