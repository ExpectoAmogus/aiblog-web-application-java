import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  private apiUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getImage(articleId: string, imageId: number): Observable<ArrayBuffer> {
    return this.http.get(`${this.apiUrl}/api/v1/images/${articleId}/${imageId}`, { responseType: 'arraybuffer' });
  }
}
