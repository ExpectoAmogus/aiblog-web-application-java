import {UserDTO} from "./user";

export interface ArticleDTO {
    id: number;
    uuid: string;
    title: string;
    content: string;
    images: string[];
    previewImage: number;
    user: UserDTO;
    dateOfCreated: Date;
}

export class ArticleDTO implements ArticleDTO {
    constructor(){}
}
