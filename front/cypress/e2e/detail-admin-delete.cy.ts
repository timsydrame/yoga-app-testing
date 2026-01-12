/// <reference types="cypress" />

describe('Session detail (admin delete)', () => {
  it('Admin can delete a session from detail', () => {
    cy.visit('/login');

    cy.intercept('POST', '**/api/auth/login', {
      body: {
        id: 1,
        username: 'admin',
        firstName: 'Admin',
        lastName: 'User',
        admin: true,
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

    cy.intercept('GET', '**/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga Flow',
        date: '2024-01-02',
        teacher_id: 1,
        description: 'Desc',
        users: [2],
        createdAt: '2024-01-01T00:00:00.000Z',
        updatedAt: '2024-01-02T00:00:00.000Z',
      },
    }).as('detail');

    cy.intercept('DELETE', '**/api/session/1', {
      statusCode: 200,
      body: {},
    }).as('deleteSession');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

    cy.wait('@login');
    cy.wait('@sessions');

    cy.window().then((win) => {
      win.history.pushState({}, '', '/sessions/detail/1');
      win.dispatchEvent(new PopStateEvent('popstate'));
    });


    cy.wait('@detail');
    cy.wait('@teacher');

    cy.contains('Delete').click();
    cy.wait('@deleteSession');
    cy.url().should('include', '/sessions');
  });
});
