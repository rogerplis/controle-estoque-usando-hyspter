import * as dayjs from 'dayjs';
import { IJob } from 'app/entities/job/job.model';
import { IJourney } from 'app/entities/journey/journey.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  cpf?: string | null;
  pis?: string | null;
  ctps?: string | null;
  birthDate?: dayjs.Dayjs | null;
  jobs?: IJob[] | null;
  manager?: IEmployee | null;
  journeys?: IJourney[] | null;
  department?: IDepartment | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public cpf?: string | null,
    public pis?: string | null,
    public ctps?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public jobs?: IJob[] | null,
    public manager?: IEmployee | null,
    public journeys?: IJourney[] | null,
    public department?: IDepartment | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
