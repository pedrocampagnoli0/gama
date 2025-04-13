import dayjs from 'dayjs/esm';

import { ISubMeta, NewSubMeta } from './sub-meta.model';

export const sampleWithRequiredData: ISubMeta = {
  id: 8961,
  descricao: 'stingy commandeer suspiciously',
  concluida: false,
  dataLimite: dayjs('2025-04-12'),
};

export const sampleWithPartialData: ISubMeta = {
  id: 18364,
  descricao: 'consistency',
  concluida: true,
  dataLimite: dayjs('2025-04-12'),
};

export const sampleWithFullData: ISubMeta = {
  id: 24523,
  descricao: 'mechanic coin',
  concluida: false,
  dataLimite: dayjs('2025-04-12'),
};

export const sampleWithNewData: NewSubMeta = {
  descricao: 'unfortunately',
  concluida: false,
  dataLimite: dayjs('2025-04-11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
