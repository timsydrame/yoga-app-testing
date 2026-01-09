import { expect, jest } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    service = new SessionService();
  });

  it('doit être initialisé avec isLogged=false et sessionInformation=undefined', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('$isLogged() doit émettre la valeur initiale false', (done) => {
    service.$isLogged().subscribe((v) => {
      expect(v).toBe(false);
      done();
    });
  });

  it('logIn() doit stocker sessionInformation, passer isLogged=true et émettre true', (done) => {
    const user: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'yoga@studio.com',
      firstName: 'Yoga',
      lastName: 'Admin',
      admin: true,
    };

    const values: boolean[] = [];
    const sub = service.$isLogged().subscribe((v) => values.push(v));

    service.logIn(user);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(user);

    // values contient [false, true]
    expect(values).toContain(true);

    sub.unsubscribe();
    done();
  });

  it('logOut() doit vider sessionInformation, passer isLogged=false et émettre false', (done) => {
    const user: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'yoga@studio.com',
      firstName: 'Yoga',
      lastName: 'Admin',
      admin: true,
    };

    const values: boolean[] = [];
    const sub = service.$isLogged().subscribe((v) => values.push(v));

    service.logIn(user);
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    // values contient [false, true, false]
    expect(values[values.length - 1]).toBe(false);

    sub.unsubscribe();
    done();
  });
});
