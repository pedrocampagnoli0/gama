import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubMeta } from '../sub-meta.model';
import { SubMetaService } from '../service/sub-meta.service';

@Component({
  templateUrl: './sub-meta-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubMetaDeleteDialogComponent {
  subMeta?: ISubMeta;

  protected subMetaService = inject(SubMetaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subMetaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
