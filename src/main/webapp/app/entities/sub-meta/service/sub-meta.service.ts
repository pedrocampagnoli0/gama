import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubMeta, NewSubMeta } from '../sub-meta.model';
import { IMeta } from '../../meta/meta.model';

export type PartialUpdateSubMeta = Partial<ISubMeta> & Pick<ISubMeta, 'id'>;

type RestOf<T extends ISubMeta | NewSubMeta> = Omit<T, 'dataLimite'> & {
  dataLimite?: string | null;
};

export type RestSubMeta = RestOf<ISubMeta>;

export type NewRestSubMeta = RestOf<NewSubMeta>;

export type PartialUpdateRestSubMeta = RestOf<PartialUpdateSubMeta>;

export type EntityResponseType = HttpResponse<ISubMeta>;
export type EntityArrayResponseType = HttpResponse<ISubMeta[]>;

@Injectable({ providedIn: 'root' })
export class SubMetaService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-metas');

  create(subMeta: NewSubMeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subMeta);
    return this.http
      .post<RestSubMeta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(subMeta: ISubMeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subMeta);
    return this.http
      .put<RestSubMeta>(`${this.resourceUrl}/${this.getSubMetaIdentifier(subMeta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(subMeta: PartialUpdateSubMeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subMeta);
    return this.http
      .patch<RestSubMeta>(`${this.resourceUrl}/${this.getSubMetaIdentifier(subMeta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSubMeta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSubMeta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubMetaIdentifier(subMeta: Pick<ISubMeta, 'id'>): number {
    return subMeta.id;
  }

  compareSubMeta(o1: Pick<ISubMeta, 'id'> | null, o2: Pick<ISubMeta, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubMetaIdentifier(o1) === this.getSubMetaIdentifier(o2) : o1 === o2;
  }

  getSubmetasByMetaId(metaId: number): Observable<ISubMeta[]> {
    return this.http.get<ISubMeta[]>(`${this.resourceUrl}/by-meta/${metaId}`);
  }

  addSubMetaToCollectionIfMissing<Type extends Pick<ISubMeta, 'id'>>(
    subMetaCollection: Type[],
    ...subMetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subMetas: Type[] = subMetasToCheck.filter(isPresent);
    if (subMetas.length > 0) {
      const subMetaCollectionIdentifiers = subMetaCollection.map(subMetaItem => this.getSubMetaIdentifier(subMetaItem));
      const subMetasToAdd = subMetas.filter(subMetaItem => {
        const subMetaIdentifier = this.getSubMetaIdentifier(subMetaItem);
        if (subMetaCollectionIdentifiers.includes(subMetaIdentifier)) {
          return false;
        }
        subMetaCollectionIdentifiers.push(subMetaIdentifier);
        return true;
      });
      return [...subMetasToAdd, ...subMetaCollection];
    }
    return subMetaCollection;
  }

  protected convertDateFromClient<T extends ISubMeta | NewSubMeta | PartialUpdateSubMeta>(subMeta: T): RestOf<T> {
    return {
      ...subMeta,
      dataLimite: subMeta.dataLimite?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSubMeta: RestSubMeta): ISubMeta {
    return {
      ...restSubMeta,
      dataLimite: restSubMeta.dataLimite ? dayjs(restSubMeta.dataLimite) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSubMeta>): HttpResponse<ISubMeta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSubMeta[]>): HttpResponse<ISubMeta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
