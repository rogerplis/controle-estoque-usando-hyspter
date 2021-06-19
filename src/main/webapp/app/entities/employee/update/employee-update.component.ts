import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IJourney } from 'app/entities/journey/journey.model';
import { JourneyService } from 'app/entities/journey/service/journey.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  employeesSharedCollection: IEmployee[] = [];
  journeysSharedCollection: IJourney[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    cpf: [],
    pis: [],
    ctps: [],
    birthDate: [],
    manager: [],
    journeys: [],
    department: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected journeyService: JourneyService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackEmployeeById(index: number, item: IEmployee): number {
    return item.id!;
  }

  trackJourneyById(index: number, item: IJourney): number {
    return item.id!;
  }

  trackDepartmentById(index: number, item: IDepartment): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      firstName: employee.firstName,
      lastName: employee.lastName,
      email: employee.email,
      phoneNumber: employee.phoneNumber,
      cpf: employee.cpf,
      pis: employee.pis,
      ctps: employee.ctps,
      birthDate: employee.birthDate,
      manager: employee.manager,
      journeys: employee.journeys,
      department: employee.department,
    });

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      employee.manager
    );
    this.journeysSharedCollection = this.journeyService.addJourneyToCollectionIfMissing(
      this.journeysSharedCollection,
      ...(employee.journeys ?? [])
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      employee.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('manager')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.journeyService
      .query()
      .pipe(map((res: HttpResponse<IJourney[]>) => res.body ?? []))
      .pipe(
        map((journeys: IJourney[]) =>
          this.journeyService.addJourneyToCollectionIfMissing(journeys, ...(this.editForm.get('journeys')!.value ?? []))
        )
      )
      .subscribe((journeys: IJourney[]) => (this.journeysSharedCollection = journeys));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      pis: this.editForm.get(['pis'])!.value,
      ctps: this.editForm.get(['ctps'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      manager: this.editForm.get(['manager'])!.value,
      journeys: this.editForm.get(['journeys'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
