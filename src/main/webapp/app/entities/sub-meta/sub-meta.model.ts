import dayjs from 'dayjs/esm';
import { IMeta } from 'app/entities/meta/meta.model';

export interface ISubMeta {
  id: number;
  descricao?: string | null;
  concluida?: boolean | null;
  dataLimite?: dayjs.Dayjs | null;
  meta?: Pick<IMeta, 'id'> | null;
}

export type NewSubMeta = Omit<ISubMeta, 'id'> & { id: null };
