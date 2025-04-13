import { Component, input, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAluno } from '../aluno.model';
import { IMeta } from '../../meta/meta.model';
import { MetaService } from '../../meta/service/meta.service';

@Component({
  selector: 'jhi-aluno-detail',
  templateUrl: './aluno-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AlunoDetailComponent {
  aluno = signal<IAluno | null>(null);
  metas: IMeta[] = []; // Store Metas
  constructor(
    private route: ActivatedRoute,
    private metaService: MetaService,
  ) {}
  ngOnInit(): void {
    // Subscribe to the route data to receive the aluno information.
    this.route.data.subscribe(({ aluno }) => {
      // Update the signal with the new value
      this.aluno.set(aluno);
      const alunoId = this.aluno()?.id;
      if (alunoId) {
        this.loadMetas(alunoId);
      }
    });
  }

  loadMetas(alunoId: number): void {
    this.metaService.getMetasByAlunoId(alunoId).subscribe({
      next: (data: IMeta[]) => {
        this.metas = data;
      },
      error: () => {},
    });
  }
  previousState(): void {
    window.history.back();
  }
}
