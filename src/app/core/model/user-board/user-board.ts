import {Board} from "../board/board";
import {User} from "../user/user";

export class UserBoard{
  id: any;
  assignedAt: any;
  role: any;
  board?: Board;
  userId?: User;
}
