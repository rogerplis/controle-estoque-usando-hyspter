import { ILocation } from 'app/entities/location/location.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { ICompanion } from 'app/entities/companion/companion.model';

export interface IDepartment {
  id?: number;
  departmentName?: string;
  location?: ILocation | null;
  employees?: IEmployee[] | null;
  companion?: ICompanion | null;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public departmentName?: string,
    public location?: ILocation | null,
    public employees?: IEmployee[] | null,
    public companion?: ICompanion | null
  ) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
