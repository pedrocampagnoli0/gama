import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sub-meta.test-samples';

import { SubMetaFormService } from './sub-meta-form.service';

describe('SubMeta Form Service', () => {
  let service: SubMetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubMetaFormService);
  });

  describe('Service methods', () => {
    describe('createSubMetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubMetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            concluida: expect.any(Object),
            dataLimite: expect.any(Object),
            meta: expect.any(Object),
          }),
        );
      });

      it('passing ISubMeta should create a new form with FormGroup', () => {
        const formGroup = service.createSubMetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descricao: expect.any(Object),
            concluida: expect.any(Object),
            dataLimite: expect.any(Object),
            meta: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubMeta', () => {
      it('should return NewSubMeta for default SubMeta initial value', () => {
        const formGroup = service.createSubMetaFormGroup(sampleWithNewData);

        const subMeta = service.getSubMeta(formGroup) as any;

        expect(subMeta).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubMeta for empty SubMeta initial value', () => {
        const formGroup = service.createSubMetaFormGroup();

        const subMeta = service.getSubMeta(formGroup) as any;

        expect(subMeta).toMatchObject({});
      });

      it('should return ISubMeta', () => {
        const formGroup = service.createSubMetaFormGroup(sampleWithRequiredData);

        const subMeta = service.getSubMeta(formGroup) as any;

        expect(subMeta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubMeta should not enable id FormControl', () => {
        const formGroup = service.createSubMetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubMeta should disable id FormControl', () => {
        const formGroup = service.createSubMetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
