import { ArticleDTO } from "./article";

export interface UserDTO {
    id: number;
    firstName: string;
    email: string;
    role: Set<SimpleGrantedAuthority>;
    username: string;
    articles: ArticleDTO[];
    dateOfCreated: Date;
}

export interface SimpleGrantedAuthority {
    authority: string;
  }
  
  export enum Role {
    ROLE_USER = 'ROLE_USER',
    ROLE_ADMIN = 'ROLE_ADMIN',
    ROLE_MODERATOR = 'ROLE_MODERATOR'
  }
  
  export const getAuthorities = (role: Role): SimpleGrantedAuthority[] => {
    switch (role) {
      case Role.ROLE_USER:
        return [{ authority: 'users:read' }];
      case Role.ROLE_ADMIN:
        return [
          { authority: 'devs:read' },
          { authority: 'devs:write' }
        ];
      case Role.ROLE_MODERATOR:
        return [
          { authority: 'devs:read' },
          { authority: 'mods:write' }
        ];
      default:
        return [];
    }
  };