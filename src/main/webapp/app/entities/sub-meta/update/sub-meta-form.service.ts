import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISubMeta, NewSubMeta } from '../sub-meta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubMeta for edit and NewSubMetaFormGroupInput for create.
 */
type SubMetaFormGroupInput = ISubMeta | PartialWithRequiredKeyOf<NewSubMeta>;

type SubMetaFormDefaults = Pick<NewSubMeta, 'id' | 'concluida'>;

type SubMetaFormGroupContent = {
  id: FormControl<ISubMeta['id'] | NewSubMeta['id']>;
  descricao: FormControl<ISubMeta['descricao']>;
  concluida: FormControl<ISubMeta['concluida']>;
  dataLimite: FormControl<ISubMeta['dataLimite']>;
  meta: FormControl<ISubMeta['meta']>;
};

export type SubMetaFormGroup = FormGroup<SubMetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubMetaFormService {
  createSubMetaFormGroup(subMeta: SubMetaFormGroupInput = { id: null }): SubMetaFormGroup {
    const subMetaRawValue = {
      ...this.getFormDefaults(),
      ...subMeta,
    };
    return new FormGroup<SubMetaFormGroupContent>({
      id: new FormControl(
        { value: subMetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descricao: new FormControl(subMetaRawValue.descricao, {
        validators: [Validators.required],
      }),
      concluida: new FormControl(subMetaRawValue.concluida, {
        validators: [Validators.required],
      }),
      dataLimite: new FormControl(subMetaRawValue.dataLimite, {
        validators: [Validators.required],
      }),
      meta: new FormControl(subMetaRawValue.meta),
    });
  }

  getSubMeta(form: SubMetaFormGroup): ISubMeta | NewSubMeta {
    return form.getRawValue() as ISubMeta | NewSubMeta;
  }

  resetForm(form: SubMetaFormGroup, subMeta: SubMetaFormGroupInput): void {
    const subMetaRawValue = { ...this.getFormDefaults(), ...subMeta };
    form.reset(
      {
        ...subMetaRawValue,
        id: { value: subMetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubMetaFormDefaults {
    return {
      id: null,
      concluida: false,
    };
  }
}
