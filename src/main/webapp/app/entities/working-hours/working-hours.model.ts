import * as dayjs from 'dayjs';
import { IJourney } from 'app/entities/journey/journey.model';
import { Days } from 'app/entities/enumerations/days.model';

export interface IWorkingHours {
  id?: number;
  entry?: dayjs.Dayjs | null;
  leavingWork?: dayjs.Dayjs | null;
  extraTime?: dayjs.Dayjs | null;
  extraTime2?: dayjs.Dayjs | null;
  entryRest?: dayjs.Dayjs | null;
  returnRest?: dayjs.Dayjs | null;
  dayWeek?: dayjs.Dayjs | null;
  day?: Days | null;
  journeys?: IJourney[] | null;
}

export class WorkingHours implements IWorkingHours {
  constructor(
    public id?: number,
    public entry?: dayjs.Dayjs | null,
    public leavingWork?: dayjs.Dayjs | null,
    public extraTime?: dayjs.Dayjs | null,
    public extraTime2?: dayjs.Dayjs | null,
    public entryRest?: dayjs.Dayjs | null,
    public returnRest?: dayjs.Dayjs | null,
    public dayWeek?: dayjs.Dayjs | null,
    public day?: Days | null,
    public journeys?: IJourney[] | null
  ) {}
}

export function getWorkingHoursIdentifier(workingHours: IWorkingHours): number | undefined {
  return workingHours.id;
}
