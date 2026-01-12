import { expect, jest } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let fixture: ComponentFixture<RegisterComponent>;
  let component: RegisterComponent;

  const authServiceMock = {
    register: jest.fn(),
  };

  const routerMock = {
    navigate: jest.fn(() => Promise.resolve(true)),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    jest.clearAllMocks();
  });

  it('submit() success: doit appeler register() et naviguer vers /login', () => {
    authServiceMock.register.mockReturnValue(of(void 0));

    component.form.setValue({
      email: 'new@test.com',
      firstName: 'Fatim',
      lastName: 'Dramé',
      password: 'test!1234',
    });

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalledWith({
      email: 'new@test.com',
      firstName: 'Fatim',
      lastName: 'Dramé',
      password: 'test!1234',
    });

    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('submit() error: doit mettre onError à true', () => {
    authServiceMock.register.mockReturnValue(
      throwError(() => new Error('register failed'))
    );

    component.form.setValue({
      email: 'bad@test.com',
      firstName: 'Bad',
      lastName: 'User',
      password: 'bad',
    });

    component.submit();

    expect(authServiceMock.register).toHaveBeenCalled();
    expect(component.onError).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
