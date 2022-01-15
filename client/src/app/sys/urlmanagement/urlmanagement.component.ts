import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import {UrlmanagementService} from './urlmanagement.service';
import { Subject } from 'rxjs';

class DataTablesResponse {
  data!: any[];
  draw!: number;
  recordsFiltered!: number;
  recordsTotal!: number;
}

@Component({
  selector: 'app-urlmanagement',
  templateUrl: './urlmanagement.component.html',
  styleUrls: ['./urlmanagement.component.css']
})

export class UrlmanagementComponent implements OnInit {

  dtOptions: DataTables.Settings = {};
  urls:any[] = [];
  closeResult = '';
  editForm: FormGroup;
  isEditSubmitted: boolean = false;
  dtTrigger: Subject<any> = new Subject<any>();
  
  constructor(
    private http: HttpClient,
    private modalService: NgbModal,
    private urlmanagementService: UrlmanagementService
    ){ this.editForm = new FormGroup({
      shortUrl: new FormControl('', [Validators.required]),
      longUrl: new FormControl('', [Validators.required]),
      timeStamp: new FormControl('', [Validators.required]),
    });}

  
  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'simple_numbers',
      processing: true
    };
    this.urlmanagementService.getAllUrl().subscribe(res => {
      this.urls = res.list;
      console.log(this.urls)
      this.dtTrigger.next();
    });
  }

  delete(id:string){
    console.log(id)
    this.urlmanagementService.deleteUrl(id).subscribe(res => {
      this.urlmanagementService.getAllUrl().subscribe(res => {
        this.urls = res.list;
      });
    })
  }

  onEditSubmit(){
    this.isEditSubmitted = true;
    if (this.editForm.valid) {
      const payload = {
        shorturl: this.editForm.value.shortUrl,
        longurl: this.editForm.value.longUrl,
        date: this.editForm.value.timeStamp
      };
      //update api
      this.modalService.dismissAll();
    }
  }
  open(content: any,id:string) {
    this.urlmanagementService.getUrl(id).subscribe(res =>{
      this.editForm.patchValue({
        shortUrl : res.data.shortUrl,
        longUrl : res.data.longUrl,
        timeStamp : res.data.expireTime
      })
      this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
    }); 
  } 

}
