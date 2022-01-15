import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import {UsermanagementService} from './usermanagement.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-usermanagement',
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.css']
})
export class UsermanagementComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  persons:any[] = [];
  closeResult = '';
  editForm: FormGroup;
  isEditSubmitted: boolean = false;
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(
    private http: HttpClient,
    private modalService: NgbModal,
    private UsermanagementService: UsermanagementService,
    ){ this.editForm = new FormGroup({
      userid: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      FreeUser: new FormControl(false),
      ProUser: new FormControl(false),
      BulkAccess: new FormControl(false),
      Manager: new FormControl(false),
    });}

  
  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'simple_numbers',
      processing: true
    };
    this.UsermanagementService.getAllUser().subscribe(res => {
      this.persons = res.list;
      console.log(this.persons)
      this.dtTrigger.next();
    });
  }

  delete(id:string){
    this.UsermanagementService.deleteUser(id).subscribe(res => {
      this.UsermanagementService.getAllUser().subscribe(res => {
        this.persons = res.list;
      });
    });
  }

  onEditSubmit(){
    this.isEditSubmitted = true;
    if (this.editForm.valid) {
      let role = [{
        "FreeUser":this.editForm.value.FreeUser,
        "ProUser":this.editForm.value.ProUser,
        "BulkAccess":this.editForm.value.BulkAccess,
        "Manager":this.editForm.value.Manager
      }];
      const payload = {
        userid: this.editForm.value.userid,
        name: this.editForm.value.name,
        password: this.editForm.value.password,
        authorities: role
      };
      //update api
      console.log(role)
      // this.UsermanagementService.update(payload)
      this.modalService.dismissAll();
    }
  }

  open(content: any,id:string) {
    this.UsermanagementService.getUser(id).subscribe(res =>{
      this.editForm.patchValue({
        userid : res.data.userId,
        name : res.data.name,
        password : res.data.password
      })
      this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
    }); 
  }  
}
