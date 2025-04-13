import { IAluno, NewAluno } from './aluno.model';

export const sampleWithRequiredData: IAluno = {
  id: 8806,
  nome: 'boo tomorrow',
  matricula: 'more than',
};

export const sampleWithPartialData: IAluno = {
  id: 2189,
  nome: 'insecure supposing untimely',
  matricula: 'kissingly unzip quit',
};

export const sampleWithFullData: IAluno = {
  id: 25732,
  nome: 'oof near likewise',
  matricula: 'furiously',
};

export const sampleWithNewData: NewAluno = {
  nome: 'energetically yet webbed',
  matricula: 'sock fooey likewise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
