import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CommentsDTO} from "../../models/comments";
import {ActiveCommentTypeEnum} from "../../models/activeCommentType.enum";
import {ActiveComment} from "../../models/activeComment";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit {

  canReply: boolean = false;
  canEdit: boolean = false;
  canDelete: boolean = false;
  activeCommentType = ActiveCommentTypeEnum;
  replyId: number | null = null;
  @Input() currentUserId!: number;
  @Input() replies!: CommentsDTO[];
  @Input() comment!: CommentsDTO;
  @Input() activeComment!: ActiveComment | null;
  @Input() parentId: number | null = null;
  @Output() setActiveComment = new EventEmitter<ActiveComment | null>();
  @Output() addComment = new EventEmitter<{text: string; parentId: number | null;}>();
  @Output() updateComment = new EventEmitter<{text: string; commentId: number;}>();
  @Output() deleteComment = new EventEmitter<number>();

  ngOnInit(): void {
    const fiveMinutes = 300000;
    const date = new Date(this.comment.dateOfCreated)
    //@ts-ignore
    const timePassed = (new Date().getMilliseconds() - date.getMilliseconds()) > fiveMinutes;
    this.canReply = Boolean(this.currentUserId);
    this.canEdit = this.currentUserId === this.comment.userId && !timePassed;
    this.canDelete = this.currentUserId === this.comment.userId && !timePassed && this.replies.length === 0;
    this.replyId = this.parentId ? this.parentId : this.comment.id;
  }

  isReplying(): boolean {
    if (!this.activeComment){
      return false;
    }
    return (
      this.activeComment.id === this.comment.id &&
      this.activeComment.type === this.activeCommentType.replying);
  }
  isEditing(): boolean {
    if (!this.activeComment){
      return false;
    }
    return (
      this.activeComment.id === this.comment.id &&
      this.activeComment.type === this.activeCommentType.editing);
  }
}
