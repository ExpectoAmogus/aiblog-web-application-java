import { ArticleDTO } from "./article";

export interface ImageDTO {
        id: number;
        name: string;
        originalFileName: string;
        size: number;
        contentType: string;
        isPreviewImage: boolean;
        bytes: Blob;
        article: ArticleDTO;
}