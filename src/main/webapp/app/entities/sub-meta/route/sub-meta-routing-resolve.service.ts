import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubMeta } from '../sub-meta.model';
import { SubMetaService } from '../service/sub-meta.service';

const subMetaResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubMeta> => {
  const id = route.params.id;
  if (id) {
    return inject(SubMetaService)
      .find(id)
      .pipe(
        mergeMap((subMeta: HttpResponse<ISubMeta>) => {
          if (subMeta.body) {
            return of(subMeta.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default subMetaResolve;
