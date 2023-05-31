import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {CommentsService} from "../../services/comments.service";
import {CommentsDTO} from "../../models/comments";
import {ActiveComment} from "../../models/activeComment";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit, OnChanges {
  @Input() currentUserId!: number;
  @Input() currentArticleId!: number;

  comments: CommentsDTO[] = [];
  mainComments: CommentsDTO[] = [];
  activeComment: ActiveComment | null = null;

  constructor(private commentsService: CommentsService) {
  }

  ngOnInit(): void {
    this.getComments(this.currentArticleId);
  }

  getComments(articleId?: number): void {
    this.commentsService.getComments(articleId).subscribe((comments) => {
      this.comments = comments;
      this.refreshMainComments();
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currentArticleId'] && !changes['currentArticleId'].firstChange) {
      const newArticleId = changes['currentArticleId'].currentValue;
      this.getComments(newArticleId);
    }
  }

  refreshMainComments(): void {
    this.mainComments = this.comments.filter(comment => comment.parentId === null);
  }

  addComment({text, parentId}: { text: string; parentId: null | number; }): void {
    this.commentsService.addComment(this.currentArticleId, text, parentId).subscribe(createdComment => {
      this.comments = [...this.comments, createdComment];
      this.refreshMainComments();
      this.activeComment = null;
    })
  }

  public updateComment({text, commentId}: {text: string, commentId: number}): void {
    this.commentsService.updateComment(commentId, text).subscribe((updatedComment) =>{
      this.comments = this.comments.map(comment => {
        if (comment.id === commentId) {
          return updatedComment;
        }
        return comment;
      });
      this.refreshMainComments();
      this.activeComment = null;
    })
  }

  public deleteComment(commentId: number): void {
    this.commentsService.deleteComment(commentId).subscribe(() => {
      this.comments = this.comments.filter(comment => comment.id !== commentId);
      this.refreshMainComments();
    })
  }

  getReplies(commentId: number): CommentsDTO[] {
    return this.comments
      .filter((comment) => comment.parentId === commentId)
      .sort(
        (a, b) =>
          new Date(a.dateOfCreated).getMilliseconds() -
          new Date(b.dateOfCreated).getMilliseconds()
      );
  }

  setActiveComment(activeComment: ActiveComment | null): void {
    this.activeComment = activeComment;
  }
}
