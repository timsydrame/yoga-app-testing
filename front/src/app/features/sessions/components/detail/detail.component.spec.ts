import { expect, jest } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

describe('DetailComponent', () => {
  let fixture: ComponentFixture<DetailComponent>;
  let component: DetailComponent;

  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1'),
      },
    },
  };

  const routerMock = {
    navigate: jest.fn(() => Promise.resolve(true)),
  };

  const snackBarMock = {
    open: jest.fn(),
  };

  const sessionServiceMock = {
    sessionInformation: { id: 2, admin: false },
  };

  const sessionApiServiceMock = {
    detail: jest.fn(),
    delete: jest.fn(),
    participate: jest.fn(),
    unParticipate: jest.fn(),
  };

  const teacherServiceMock = {
    detail: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
      ],
    }).compileComponents();

    jest.clearAllMocks();

    // valeurs par défaut
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue('1');
    sessionServiceMock.sessionInformation.id = 2;
    sessionServiceMock.sessionInformation.admin = false;

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('ngOnInit() doit appeler fetchSession() et charger session + teacher + isParticipate=false', () => {
    sessionApiServiceMock.detail.mockReturnValue(
      of({
        id: 1,
        name: 'Yoga Flow',
        date: '2024-01-02',
        teacher_id: 1,
        description: 'Desc',
        users: [], // user 2 pas inscrit
      })
    );

    teacherServiceMock.detail.mockReturnValue(
      of({ id: 1, firstName: 'John', lastName: 'Doe' })
    );

    component.ngOnInit();

    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(teacherServiceMock.detail).toHaveBeenCalledWith('1');

    expect(component.session).toBeTruthy();
    expect(component.teacher).toBeTruthy();
    expect(component.isParticipate).toBe(false);
  });

  it('fetchSession() doit mettre isParticipate=true si users contient id user', () => {
    sessionApiServiceMock.detail.mockReturnValue(
      of({
        id: 1,
        name: 'Yoga Flow',
        date: '2024-01-02',
        teacher_id: 1,
        description: 'Desc',
        users: [2], // user 2 inscrit
      })
    );
    teacherServiceMock.detail.mockReturnValue(
      of({ id: 1, firstName: 'John', lastName: 'Doe' })
    );

    component.ngOnInit();

    expect(component.isParticipate).toBe(true);
  });

  it('delete() doit appeler sessionApiService.delete, afficher snackbar, puis naviguer vers sessions', () => {
    sessionApiServiceMock.delete.mockReturnValue(of({}));

    component.delete();

    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith('1');
    expect(snackBarMock.open).toHaveBeenCalledWith(
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('participate() doit appeler participate(sessionId,userId) puis refetch (detail appelé à nouveau)', () => {
    // 1er fetch (ngOnInit) + refetch après participate
    sessionApiServiceMock.detail.mockReturnValue(
      of({
        id: 1,
        teacher_id: 1,
        users: [],
      })
    );
    teacherServiceMock.detail.mockReturnValue(of({ id: 1 }));

    component.ngOnInit();
    expect(sessionApiServiceMock.detail).toHaveBeenCalledTimes(1);

    sessionApiServiceMock.participate.mockReturnValue(of(void 0));

    component.participate();

    expect(sessionApiServiceMock.participate).toHaveBeenCalledWith('1', '2');
    expect(sessionApiServiceMock.detail).toHaveBeenCalledTimes(2);
  });

  it('unParticipate() doit appeler unParticipate(sessionId,userId) puis refetch (detail appelé à nouveau)', () => {
    sessionApiServiceMock.detail.mockReturnValue(
      of({
        id: 1,
        teacher_id: 1,
        users: [2],
      })
    );
    teacherServiceMock.detail.mockReturnValue(of({ id: 1 }));

    component.ngOnInit();
    expect(sessionApiServiceMock.detail).toHaveBeenCalledTimes(1);

    sessionApiServiceMock.unParticipate.mockReturnValue(of(void 0));

    component.unParticipate();

    expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith('1', '2');
    expect(sessionApiServiceMock.detail).toHaveBeenCalledTimes(2);
  });
});
