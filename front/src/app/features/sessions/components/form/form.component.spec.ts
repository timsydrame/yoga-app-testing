import { expect, jest } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';

import { FormComponent } from './form.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('FormComponent', () => {
  let fixture: ComponentFixture<FormComponent>;
  let component: FormComponent;

  // Mocks
  const routerMock: {
    url: string;
    navigate: (...args: any[]) => Promise<boolean>;
  } = {
    url: '/sessions/create',
    navigate: jest.fn(() => Promise.resolve(true)),
  };



  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('12'),
      },
    },
  };

  const snackBarMock = {
    open: jest.fn(),
  };

  const sessionApiServiceMock = {
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
  };

  const sessionServiceMock = {
    sessionInformation: { admin: true }, // on change selon tests
  };

  const teacherServiceMock = {
    all: jest
      .fn()
      .mockReturnValue(of([{ id: 1, firstName: 'John', lastName: 'Doe' }])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
      ],
      // évite d'importer Angular Material / FlexLayout en tests
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;

    // reset mocks
    jest.clearAllMocks();

    // valeurs par défaut
    routerMock.url = '/sessions/create';
    sessionServiceMock.sessionInformation.admin = true;
    activatedRouteMock.snapshot.paramMap.get.mockReturnValue('12');
  });

  it('redirige vers /sessions si pas admin', () => {
    sessionServiceMock.sessionInformation.admin = false;

    component.ngOnInit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('en mode create: onUpdate=false et le form est initialisé vide', () => {
    routerMock.url = '/sessions/create';

    component.ngOnInit();

    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeTruthy();
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
  });

  it('en mode update: onUpdate=true, appelle detail(id) et préremplit le form', () => {
    routerMock.url = '/sessions/update/12';

    const sessionFromApi = {
      id: 12,
      name: 'Yoga Flow',
      date: '2024-01-02T00:00:00.000Z',
      teacher_id: 1,
      description: 'Desc',
    };

    sessionApiServiceMock.detail.mockReturnValue(of(sessionFromApi));

    component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(activatedRouteMock.snapshot.paramMap.get).toHaveBeenCalledWith('id');
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('12');
    expect(component.sessionForm).toBeTruthy();

    // initForm transforme la date en YYYY-MM-DD
    expect(component.sessionForm?.value).toEqual({
      name: 'Yoga Flow',
      date: '2024-01-02',
      teacher_id: 1,
      description: 'Desc',
    });
  });

  it("submit() en create: appelle create() puis snackbar + navigate('sessions')", () => {
    routerMock.url = '/sessions/create';
    sessionApiServiceMock.create.mockReturnValue(of({}));

    component.ngOnInit();

    component.sessionForm?.setValue({
      name: 'Test',
      date: '2024-01-05',
      teacher_id: 1,
      description: 'Hello',
    });

    component.submit();

    expect(sessionApiServiceMock.create).toHaveBeenCalledWith({
      name: 'Test',
      date: '2024-01-05',
      teacher_id: 1,
      description: 'Hello',
    });

    expect(snackBarMock.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it("submit() en update: appelle update(id) puis snackbar + navigate('sessions')", () => {
    routerMock.url = '/sessions/update/12';
    const sessionFromApi = {
      id: 12,
      name: 'Yoga',
      date: '2024-01-02T00:00:00.000Z',
      teacher_id: 1,
      description: 'Desc',
    };
    sessionApiServiceMock.detail.mockReturnValue(of(sessionFromApi));
    sessionApiServiceMock.update.mockReturnValue(of({}));

    component.ngOnInit();

    component.sessionForm?.patchValue({
      name: 'Yoga Updated',
      date: '2024-02-01',
      teacher_id: 1,
      description: 'Updated',
    });

    component.submit();

    expect(sessionApiServiceMock.update).toHaveBeenCalledWith('12', {
      name: 'Yoga Updated',
      date: '2024-02-01',
      teacher_id: 1,
      description: 'Updated',
    });

    expect(snackBarMock.open).toHaveBeenCalledWith(
      'Session updated !',
      'Close',
      { duration: 3000 }
    );
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
