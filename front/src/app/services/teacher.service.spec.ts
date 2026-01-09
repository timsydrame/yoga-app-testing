import { expect, jest } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { TeacherService } from './teacher.service';

describe('TeacherService (HTTP integration)', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('all() doit faire GET api/teacher', () => {
    service.all().subscribe((teachers) => {
      expect(teachers.length).toBe(1);
      expect(teachers[0].id).toBe(1);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');

    req.flush([{ id: 1, firstName: 'John', lastName: 'Doe' }]);
  });

  it('detail(id) doit faire GET api/teacher/:id', () => {
    service.detail('5').subscribe((teacher) => {
      expect(teacher.id).toBe(5);
    });

    const req = httpMock.expectOne('api/teacher/5');
    expect(req.request.method).toBe('GET');

    req.flush({ id: 5, firstName: 'Jane', lastName: 'Doe' });
  });
});
