import { Injectable } from '@angular/core';
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AdminStatusService {
  private isAdmin: boolean | undefined;

  constructor(private authService: AuthService) {}

  async initializeAdminStatus(): Promise<void> {
    this.isAdmin = await this.authService.isAdmin();
  }

  get isAdminLoaded(): boolean {
    return this.isAdmin !== undefined;
  }

  get isAdminStatus(): boolean | undefined {
    return this.isAdmin;
  }
}
