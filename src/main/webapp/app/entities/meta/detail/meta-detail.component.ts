import { Component, input, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IMeta } from '../meta.model';
import { MetaService } from '../service/meta.service';
import { ISubMeta } from '../../sub-meta/sub-meta.model';
import { SubMetaService } from '../../sub-meta/service/sub-meta.service';
import { IAluno } from '../../aluno/aluno.model';

@Component({
  selector: 'jhi-meta-detail',
  templateUrl: './meta-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class MetaDetailComponent {
  meta = signal<IMeta | null>(null);

  subMetas: ISubMeta[] = [];

  constructor(
    private route: ActivatedRoute,
    private subMetaService: SubMetaService,
  ) {}

  ngOnInit(): void {
    // Subscribe to the route data to receive the aluno information.
    this.route.data.subscribe(({ meta }) => {
      // Update the signal with the new value
      this.meta.set(meta);
      const metaId = this.meta()?.id;
      if (metaId) {
        this.loadSubMetas(metaId);
      }
    });
  }
  loadSubMetas(metaId: number): void {
    this.subMetaService.getSubmetasByMetaId(metaId).subscribe({
      next: (data: ISubMeta[]) => {
        this.subMetas = data;
      },
      error: () => {},
    });
  }

  previousState(): void {
    window.history.back();
  }
}
