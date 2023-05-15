import {ActiveCommentTypeEnum} from "./activeCommentType.enum";

export interface ActiveComment {
  id: number,
  type: ActiveCommentTypeEnum;
}
