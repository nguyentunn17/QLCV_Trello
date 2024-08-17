import { User } from "../user/user";

export class Board {
  id?: any; // Mark id as optional with `?`
  createdAt: any;
  description: string; // Adjust types as needed, assuming description is a string
  name: string; // Assuming name is a string
  status: number;

  constructor(name: string, description: string, createdAt: any, status: number, id?: any) {
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.status = status;
    this.id = id;
  }
}
