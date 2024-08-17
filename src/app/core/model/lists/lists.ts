import {Board} from "../board/board";
import {Card} from "../card/card";

export class Lists{
  id: any;
  name: string;
  position: number;
  boardId?: string;
  status: boolean |unknown;
  cards: Card[];
  constructor(id: any, name: any, position: any, cards: Card[]) {
    this.id = id;
    this.name = name;
    this.position = position;
    this.cards = cards || []; // Khởi tạo mảng thẻ, nếu không có sẵn thẻ nào
  }
}
