import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompanion, Companion } from '../companion.model';
import { CompanionService } from '../service/companion.service';

@Injectable({ providedIn: 'root' })
export class CompanionRoutingResolveService implements Resolve<ICompanion> {
  constructor(protected service: CompanionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompanion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((companion: HttpResponse<Companion>) => {
          if (companion.body) {
            return of(companion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Companion());
  }
}
