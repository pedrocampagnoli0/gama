import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SubMetaResolve from './route/sub-meta-routing-resolve.service';

const subMetaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sub-meta.component').then(m => m.SubMetaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sub-meta-detail.component').then(m => m.SubMetaDetailComponent),
    resolve: {
      subMeta: SubMetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sub-meta-update.component').then(m => m.SubMetaUpdateComponent),
    resolve: {
      subMeta: SubMetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sub-meta-update.component').then(m => m.SubMetaUpdateComponent),
    resolve: {
      subMeta: SubMetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subMetaRoute;
