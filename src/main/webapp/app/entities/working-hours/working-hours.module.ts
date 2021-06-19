import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WorkingHoursComponent } from './list/working-hours.component';
import { WorkingHoursDetailComponent } from './detail/working-hours-detail.component';
import { WorkingHoursUpdateComponent } from './update/working-hours-update.component';
import { WorkingHoursDeleteDialogComponent } from './delete/working-hours-delete-dialog.component';
import { WorkingHoursRoutingModule } from './route/working-hours-routing.module';

@NgModule({
  imports: [SharedModule, WorkingHoursRoutingModule],
  declarations: [WorkingHoursComponent, WorkingHoursDetailComponent, WorkingHoursUpdateComponent, WorkingHoursDeleteDialogComponent],
  entryComponents: [WorkingHoursDeleteDialogComponent],
})
export class WorkingHoursModule {}
