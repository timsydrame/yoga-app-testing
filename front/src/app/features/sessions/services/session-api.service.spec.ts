import { expect, jest } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';

describe('SessionApiService (HTTP integration)', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('all() doit faire GET api/session', () => {
    service.all().subscribe((sessions) => {
      expect(sessions.length).toBe(1);
      expect(sessions[0].name).toBe('Yoga');
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');

    req.flush([{ id: 1, name: 'Yoga' }]);
  });

  it('detail(id) doit faire GET api/session/:id', () => {
    service.detail('12').subscribe((session) => {
      expect((session as any).id).toBe(12);
    });

    const req = httpMock.expectOne('api/session/12');
    expect(req.request.method).toBe('GET');

    req.flush({ id: 12, name: 'Yoga Flow' });
  });

  it('delete(id) doit faire DELETE api/session/:id', () => {
    service.delete('9').subscribe((res) => {
      expect(res).toEqual({ ok: true });
    });

    const req = httpMock.expectOne('api/session/9');
    expect(req.request.method).toBe('DELETE');

    req.flush({ ok: true });
  });

  it('create(session) doit faire POST api/session avec le body', () => {
    const body: any = {
      name: 'New',
      date: '2024-01-01',
      teacher_id: 1,
      description: 'Desc',
    };

    service.create(body).subscribe((created) => {
      expect((created as any).name).toBe('New');
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(body);

    req.flush({ id: 99, ...body });
  });

  it('update(id, session) doit faire PUT api/session/:id avec le body', () => {
    const body: any = {
      name: 'Updated',
      date: '2024-02-01',
      teacher_id: 2,
      description: 'Desc2',
    };

    service.update('77', body).subscribe((updated) => {
      expect((updated as any).name).toBe('Updated');
    });

    const req = httpMock.expectOne('api/session/77');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(body);

    req.flush({ id: 77, ...body });
  });

  it('participate(id, userId) doit faire POST api/session/:id/participate/:userId avec body null', () => {
    service.participate('10', '5').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/10/participate/5');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();

    req.flush(null);
  });

  it('unParticipate(id, userId) doit faire DELETE api/session/:id/participate/:userId', () => {
    service.unParticipate('10', '5').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/10/participate/5');
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });
});
