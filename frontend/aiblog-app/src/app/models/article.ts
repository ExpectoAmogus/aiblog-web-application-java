import { ImageDTO } from "./image";
import { UserDTO } from "./user";

export interface ArticleDTO {
    id: number;
    title: string;
    content: string;
    images: ImageDTO[];
    previewImage: number;
    user: UserDTO;
    dateOfCreated: Date;
}

export class ArticleDTO implements ArticleDTO {
    constructor(){}
}