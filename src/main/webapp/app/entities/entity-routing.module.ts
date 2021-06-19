import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        data: { pageTitle: 'controleestoquehyspterApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'controleestoquehyspterApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'controleestoquehyspterApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'controleestoquehyspterApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'controleestoquehyspterApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'controleestoquehyspterApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'controleestoquehyspterApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'controleestoquehyspterApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.module').then(m => m.JobHistoryModule),
      },
      {
        path: 'companion',
        data: { pageTitle: 'controleestoquehyspterApp.companion.home.title' },
        loadChildren: () => import('./companion/companion.module').then(m => m.CompanionModule),
      },
      {
        path: 'working-hours',
        data: { pageTitle: 'controleestoquehyspterApp.workingHours.home.title' },
        loadChildren: () => import('./working-hours/working-hours.module').then(m => m.WorkingHoursModule),
      },
      {
        path: 'journey',
        data: { pageTitle: 'controleestoquehyspterApp.journey.home.title' },
        loadChildren: () => import('./journey/journey.module').then(m => m.JourneyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
