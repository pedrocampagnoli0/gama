import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubMetaDetailComponent } from './sub-meta-detail.component';

describe('SubMeta Management Detail Component', () => {
  let comp: SubMetaDetailComponent;
  let fixture: ComponentFixture<SubMetaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubMetaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./sub-meta-detail.component').then(m => m.SubMetaDetailComponent),
              resolve: { subMeta: () => of({ id: 26324 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubMetaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubMetaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load subMeta on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SubMetaDetailComponent);

      // THEN
      expect(instance.subMeta()).toEqual(expect.objectContaining({ id: 26324 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
