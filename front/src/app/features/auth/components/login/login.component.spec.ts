import { expect, jest } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;

  const authServiceMock = {
    login: jest.fn(),
  };

  const sessionServiceMock = {
    logIn: jest.fn(),
  };

  const routerMock = {
    navigate: jest.fn(() => Promise.resolve(true)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    jest.clearAllMocks();
  });

  it('submit() success: doit appeler login(), logIn() et naviguer vers /sessions', () => {
    const response = {
      id: 1,
      username: 'yoga@studio.com',
      firstName: 'Admin',
      lastName: 'User',
      admin: true,
      token: 't',
      type: 'Bearer',
    };

    authServiceMock.login.mockReturnValue(of(response));

    component.form.setValue({
      email: 'yoga@studio.com',
      password: 'test!1234',
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'yoga@studio.com',
      password: 'test!1234',
    });

    expect(sessionServiceMock.logIn).toHaveBeenCalledWith(response);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('submit() error: doit mettre onError à true', () => {
    authServiceMock.login.mockReturnValue(
      throwError(() => new Error('bad creds'))
    );

    component.form.setValue({
      email: 'bad@test.com',
      password: 'bad',
    });

    component.submit();

    expect(authServiceMock.login).toHaveBeenCalled();
    expect(component.onError).toBe(true);
    expect(sessionServiceMock.logIn).not.toHaveBeenCalled();
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
