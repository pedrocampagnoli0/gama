import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISubMeta } from '../sub-meta.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../sub-meta.test-samples';

import { RestSubMeta, SubMetaService } from './sub-meta.service';

const requireRestSample: RestSubMeta = {
  ...sampleWithRequiredData,
  dataLimite: sampleWithRequiredData.dataLimite?.format(DATE_FORMAT),
};

describe('SubMeta Service', () => {
  let service: SubMetaService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubMeta | ISubMeta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SubMetaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SubMeta', () => {
      const subMeta = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(subMeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubMeta', () => {
      const subMeta = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(subMeta).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubMeta', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubMeta', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SubMeta', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubMetaToCollectionIfMissing', () => {
      it('should add a SubMeta to an empty array', () => {
        const subMeta: ISubMeta = sampleWithRequiredData;
        expectedResult = service.addSubMetaToCollectionIfMissing([], subMeta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subMeta);
      });

      it('should not add a SubMeta to an array that contains it', () => {
        const subMeta: ISubMeta = sampleWithRequiredData;
        const subMetaCollection: ISubMeta[] = [
          {
            ...subMeta,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubMetaToCollectionIfMissing(subMetaCollection, subMeta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubMeta to an array that doesn't contain it", () => {
        const subMeta: ISubMeta = sampleWithRequiredData;
        const subMetaCollection: ISubMeta[] = [sampleWithPartialData];
        expectedResult = service.addSubMetaToCollectionIfMissing(subMetaCollection, subMeta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subMeta);
      });

      it('should add only unique SubMeta to an array', () => {
        const subMetaArray: ISubMeta[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subMetaCollection: ISubMeta[] = [sampleWithRequiredData];
        expectedResult = service.addSubMetaToCollectionIfMissing(subMetaCollection, ...subMetaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subMeta: ISubMeta = sampleWithRequiredData;
        const subMeta2: ISubMeta = sampleWithPartialData;
        expectedResult = service.addSubMetaToCollectionIfMissing([], subMeta, subMeta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subMeta);
        expect(expectedResult).toContain(subMeta2);
      });

      it('should accept null and undefined values', () => {
        const subMeta: ISubMeta = sampleWithRequiredData;
        expectedResult = service.addSubMetaToCollectionIfMissing([], null, subMeta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subMeta);
      });

      it('should return initial array if no SubMeta is added', () => {
        const subMetaCollection: ISubMeta[] = [sampleWithRequiredData];
        expectedResult = service.addSubMetaToCollectionIfMissing(subMetaCollection, undefined, null);
        expect(expectedResult).toEqual(subMetaCollection);
      });
    });

    describe('compareSubMeta', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubMeta(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 26324 };
        const entity2 = null;

        const compareResult1 = service.compareSubMeta(entity1, entity2);
        const compareResult2 = service.compareSubMeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 26324 };
        const entity2 = { id: 3972 };

        const compareResult1 = service.compareSubMeta(entity1, entity2);
        const compareResult2 = service.compareSubMeta(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 26324 };
        const entity2 = { id: 26324 };

        const compareResult1 = service.compareSubMeta(entity1, entity2);
        const compareResult2 = service.compareSubMeta(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
