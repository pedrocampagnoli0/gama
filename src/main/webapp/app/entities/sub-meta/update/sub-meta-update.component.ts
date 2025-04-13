import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMeta } from 'app/entities/meta/meta.model';
import { MetaService } from 'app/entities/meta/service/meta.service';
import { ISubMeta } from '../sub-meta.model';
import { SubMetaService } from '../service/sub-meta.service';
import { SubMetaFormGroup, SubMetaFormService } from './sub-meta-form.service';

@Component({
  selector: 'jhi-sub-meta-update',
  templateUrl: './sub-meta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubMetaUpdateComponent implements OnInit {
  isSaving = false;
  subMeta: ISubMeta | null = null;

  metasSharedCollection: IMeta[] = [];

  protected subMetaService = inject(SubMetaService);
  protected subMetaFormService = inject(SubMetaFormService);
  protected metaService = inject(MetaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubMetaFormGroup = this.subMetaFormService.createSubMetaFormGroup();

  compareMeta = (o1: IMeta | null, o2: IMeta | null): boolean => this.metaService.compareMeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subMeta }) => {
      this.subMeta = subMeta;
      if (subMeta) {
        this.updateForm(subMeta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subMeta = this.subMetaFormService.getSubMeta(this.editForm);
    if (subMeta.id !== null) {
      this.subscribeToSaveResponse(this.subMetaService.update(subMeta));
    } else {
      this.subscribeToSaveResponse(this.subMetaService.create(subMeta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubMeta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(subMeta: ISubMeta): void {
    this.subMeta = subMeta;
    this.subMetaFormService.resetForm(this.editForm, subMeta);

    this.metasSharedCollection = this.metaService.addMetaToCollectionIfMissing<IMeta>(this.metasSharedCollection, subMeta.meta);
  }

  protected loadRelationshipsOptions(): void {
    this.metaService
      .query()
      .pipe(map((res: HttpResponse<IMeta[]>) => res.body ?? []))
      .pipe(map((metas: IMeta[]) => this.metaService.addMetaToCollectionIfMissing<IMeta>(metas, this.subMeta?.meta)))
      .subscribe((metas: IMeta[]) => (this.metasSharedCollection = metas));
  }
}
