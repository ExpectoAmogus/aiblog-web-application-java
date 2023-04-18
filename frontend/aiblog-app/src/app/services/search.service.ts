import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private searchQuery = new BehaviorSubject<string>('');

  setSearchQuery(query: string) {
    this.searchQuery.next(query);
  }

  getSearchQuery() {
    return this.searchQuery.asObservable();
  }

  clearSearchQuery() {
    this.searchQuery.next('');
  }
}
