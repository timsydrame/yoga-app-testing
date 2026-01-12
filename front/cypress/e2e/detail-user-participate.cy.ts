/// <reference types="cypress" />

describe('Session detail (user)', () => {
  it('User participates then cancels participation', () => {
    cy.visit('/login');

    cy.intercept('POST', '**/api/auth/login', {
      body: {
        id: 2,
        username: 'user',
        firstName: 'User',
        lastName: 'Test',
        admin: false,
      },
    }).as('login');

    cy.intercept('GET', '**/api/session', {
      body: [
        {
          id: 1,
          name: 'Yoga Flow',
          date: '2024-01-02',
          teacher_id: 1,
          description: 'Desc',
        },
      ],
    }).as('sessions');

    cy.intercept('GET', '**/api/teacher/1', {
      body: { id: 1, firstName: 'John', lastName: 'Doe' },
    }).as('teacher');

    let call = 0;
    cy.intercept('GET', '**/api/session/1', (req) => {
      call++;
      const base = {
        id: 1,
        name: 'Yoga Flow',
        date: '2024-01-02',
        teacher_id: 1,
        description: 'Desc',
        createdAt: '2024-01-01T00:00:00.000Z',
        updatedAt: '2024-01-02T00:00:00.000Z',
      };
      if (call === 1) req.reply({ ...base, users: [] });
      else if (call === 2) req.reply({ ...base, users: [2] });
      else req.reply({ ...base, users: [] });
    }).as('detail');

    cy.intercept('POST', '**/api/session/1/participate/2', {
      statusCode: 200,
      body: null,
    }).as('participate');
    cy.intercept('DELETE', '**/api/session/1/participate/2', {
      statusCode: 200,
      body: null,
    }).as('unparticipate');

    cy.get('input[formControlName=email]').type('user@test.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

    cy.wait('@login');
    cy.wait('@sessions');
    cy.url().should('include', '/sessions');

    cy.window().then((win) => {
      win.history.pushState({}, '', '/sessions/detail/1');
      win.dispatchEvent(new PopStateEvent('popstate'));
    });
    cy.wait('@detail');
    cy.wait('@teacher');

    cy.contains('Participate').click();
    cy.wait('@participate');
    cy.wait('@detail');

    cy.contains('Do not participate').click();
    cy.wait('@unparticipate');
    cy.wait('@detail');

    cy.contains('Participate').should('be.visible');
  });
});
