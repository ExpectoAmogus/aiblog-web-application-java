export interface CommentsDTO {
  id: number;
  username: string;
  email: string;
  text: string;
  userId: number;
  parentId: number | null;
  dateOfCreated: string;
}

export class CommentsDTO implements CommentsDTO {
  constructor() {
  }
}
