import { Pipe, PipeTransform } from '@angular/core';
import {CommentsDTO} from "./models/comments";

@Pipe({
  name: 'filterCommentsByParentId'
})
export class FilterCommentsByParentIdPipe implements PipeTransform {

  transform(comments: CommentsDTO[], parentId: number | null): CommentsDTO[] {
    return comments.filter(comment => comment.parentId === parentId);
  }

}
