import {Card} from "../card/card";
import {User} from "../user/user";

export class Comment{
  id: any;
  content: any;
  createdAt: any;
  cardId?: Card;
  userId?: User;
  status: boolean | unknown;
}
