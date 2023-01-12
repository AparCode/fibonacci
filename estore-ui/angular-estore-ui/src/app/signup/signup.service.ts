import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import { User } from 'src/model/user';
import { Status } from 'src/model/Status';
import { MessageService } from '../message.service';

@Injectable({
  providedIn: 'root'
})
export class SignupService {
  private signupURL = 'http://localhost:8080/login/';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private messageService: MessageService) { }

  //Post to authenticate user
  signupUser(user: User): Observable<Status> {
    return this.http.post<Status>(this.signupURL, user, this.httpOptions).pipe(
      tap((status: Status) => this.log(`Registered: ${user.username} with Status: ${status}`)),
      catchError(this.handleError<Status>('authenticateUser'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a ProductService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`SignupService: ${message}`);
  }

}
