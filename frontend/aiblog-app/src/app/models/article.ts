import {UserDTO} from "./user";

export interface ArticleDTO {
  id: number;
  uuid: string;
  title: string;
  content: string;
  category: string;
  views: number;
  images: string[];
  previewImage: number;
  user: UserDTO;
  dateOfCreated: string;
}

export class ArticleDTO implements ArticleDTO {
  constructor() {
  }
}
