import * as dayjs from 'dayjs';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IWorkingHours } from 'app/entities/working-hours/working-hours.model';

export interface IJourney {
  id?: number;
  journeyName?: string | null;
  tolerance?: number | null;
  startJourney?: dayjs.Dayjs | null;
  endJourney?: dayjs.Dayjs | null;
  dayOut?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  employees?: IEmployee[] | null;
  workinghours?: IWorkingHours[] | null;
}

export class Journey implements IJourney {
  constructor(
    public id?: number,
    public journeyName?: string | null,
    public tolerance?: number | null,
    public startJourney?: dayjs.Dayjs | null,
    public endJourney?: dayjs.Dayjs | null,
    public dayOut?: dayjs.Dayjs | null,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public employees?: IEmployee[] | null,
    public workinghours?: IWorkingHours[] | null
  ) {}
}

export function getJourneyIdentifier(journey: IJourney): number | undefined {
  return journey.id;
}
