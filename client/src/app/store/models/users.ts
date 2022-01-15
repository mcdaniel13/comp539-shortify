export interface LoginResponse {
  data: string;
  success: boolean;
  userid: string;
  name:string;
  password: string;
}

export interface SignupResponse {
  userid: string;
  name:string;
  password: string;
}

export class User {
  userid!: string;
  name: string;
  password?: string;
  token: string;
  
  constructor() {
    this.name = '';
    this.token = '';
  }
}