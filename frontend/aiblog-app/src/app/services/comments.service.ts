import {Injectable} from '@angular/core';
import {environment} from "../../environments/environments";
import {HttpClient} from "@angular/common/http";
import {ExceptionService} from "./exception.service";
import {catchError, map, Observable} from "rxjs";
import {CommentsDTO} from "../models/comments";

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private apiUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private errorHandlingService: ExceptionService
  ) {
  }

  getComments(articleId: number | undefined): Observable<CommentsDTO[]> {
    return this.http.get<CommentsDTO[]>(`${this.apiUrl}/api/v1/comments/${articleId}`)
      .pipe(
        map(comments => {
          return comments.map(comment => {
            const dateArray = comment.dateOfCreated;
            // @ts-ignore
            const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5], dateArray[6] / 1000000);
            comment.dateOfCreated = date.toISOString();
            return comment;
          });
        }),
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  addComment(articleId: number | undefined, text: string, parentId: number | null): Observable<CommentsDTO> {
    return this.http.post<CommentsDTO>(`${this.apiUrl}/api/v1/comments/${articleId}`,
      {text: text, parentId: parentId})
      .pipe(
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  updateComment(commentId: number, text: string): Observable<CommentsDTO> {
    return this.http.patch<CommentsDTO>(`${this.apiUrl}/api/v1/comments/${commentId}`, {text: text})
      .pipe(
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }

  deleteComment(commentId: number): Observable<{}> {
    return this.http.delete<{}>(`${this.apiUrl}/api/v1/comments/${commentId}`)
      .pipe(
        catchError(error => {
          this.errorHandlingService.handleHttpError(error);
          throw error;
        })
      );
  }
}
