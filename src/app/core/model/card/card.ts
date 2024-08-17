import {User} from "../user/user";
import {Lists} from "../lists/lists";

export class Card{
  id: any;
  createdAt: any;
  description: any;
  startDate: any;
  dueDate: any;
  completed: boolean | unknown;
  position: any;
  title: any;
  userId?: User;
  listsId?: any;
  status :boolean | unknown;
}
