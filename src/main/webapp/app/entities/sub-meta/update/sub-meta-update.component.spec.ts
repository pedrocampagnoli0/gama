import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMeta } from 'app/entities/meta/meta.model';
import { MetaService } from 'app/entities/meta/service/meta.service';
import { SubMetaService } from '../service/sub-meta.service';
import { ISubMeta } from '../sub-meta.model';
import { SubMetaFormService } from './sub-meta-form.service';

import { SubMetaUpdateComponent } from './sub-meta-update.component';

describe('SubMeta Management Update Component', () => {
  let comp: SubMetaUpdateComponent;
  let fixture: ComponentFixture<SubMetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subMetaFormService: SubMetaFormService;
  let subMetaService: SubMetaService;
  let metaService: MetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubMetaUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SubMetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubMetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subMetaFormService = TestBed.inject(SubMetaFormService);
    subMetaService = TestBed.inject(SubMetaService);
    metaService = TestBed.inject(MetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Meta query and add missing value', () => {
      const subMeta: ISubMeta = { id: 3972 };
      const meta: IMeta = { id: 14440 };
      subMeta.meta = meta;

      const metaCollection: IMeta[] = [{ id: 14440 }];
      jest.spyOn(metaService, 'query').mockReturnValue(of(new HttpResponse({ body: metaCollection })));
      const additionalMetas = [meta];
      const expectedCollection: IMeta[] = [...additionalMetas, ...metaCollection];
      jest.spyOn(metaService, 'addMetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subMeta });
      comp.ngOnInit();

      expect(metaService.query).toHaveBeenCalled();
      expect(metaService.addMetaToCollectionIfMissing).toHaveBeenCalledWith(
        metaCollection,
        ...additionalMetas.map(expect.objectContaining),
      );
      expect(comp.metasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const subMeta: ISubMeta = { id: 3972 };
      const meta: IMeta = { id: 14440 };
      subMeta.meta = meta;

      activatedRoute.data = of({ subMeta });
      comp.ngOnInit();

      expect(comp.metasSharedCollection).toContainEqual(meta);
      expect(comp.subMeta).toEqual(subMeta);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubMeta>>();
      const subMeta = { id: 26324 };
      jest.spyOn(subMetaFormService, 'getSubMeta').mockReturnValue(subMeta);
      jest.spyOn(subMetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subMeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subMeta }));
      saveSubject.complete();

      // THEN
      expect(subMetaFormService.getSubMeta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subMetaService.update).toHaveBeenCalledWith(expect.objectContaining(subMeta));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubMeta>>();
      const subMeta = { id: 26324 };
      jest.spyOn(subMetaFormService, 'getSubMeta').mockReturnValue({ id: null });
      jest.spyOn(subMetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subMeta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subMeta }));
      saveSubject.complete();

      // THEN
      expect(subMetaFormService.getSubMeta).toHaveBeenCalled();
      expect(subMetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubMeta>>();
      const subMeta = { id: 26324 };
      jest.spyOn(subMetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subMeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subMetaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMeta', () => {
      it('should forward to metaService', () => {
        const entity = { id: 14440 };
        const entity2 = { id: 7336 };
        jest.spyOn(metaService, 'compareMeta');
        comp.compareMeta(entity, entity2);
        expect(metaService.compareMeta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
