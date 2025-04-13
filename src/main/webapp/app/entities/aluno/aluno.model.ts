export interface IAluno {
  id: number;
  nome?: string | null;
  matricula?: string | null;
}

export type NewAluno = Omit<IAluno, 'id'> & { id: null };
