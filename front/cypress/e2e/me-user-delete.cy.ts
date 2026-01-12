/// <reference types="cypress" />

describe('Me page (non-admin) delete account', () => {
  it('Deletes account and redirects to home', () => {
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

    cy.intercept('GET', '**/api/session', { body: [] }).as('sessions');

    cy.intercept('GET', '**/api/user/2', {
      body: {
        id: 2,
        firstName: 'User',
        lastName: 'Test',
        email: 'user@test.com',
        admin: false,
        createdAt: '2024-01-01T00:00:00.000Z',
        updatedAt: '2024-01-02T00:00:00.000Z',
      },
    }).as('user');

    cy.intercept('DELETE', '**/api/user/2', { statusCode: 200, body: {} }).as(
      'deleteUser'
    );

    cy.get('input[formControlName=email]').type('user@test.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

    cy.wait('@login');
    cy.wait('@sessions');

    cy.window().then((win) => {
      win.history.pushState({}, '', '/me');
      win.dispatchEvent(new PopStateEvent('popstate'));
    });

    cy.wait('@user');

    cy.contains('Delete my account:').should('be.visible');
    cy.contains('Detail').click(); // bouton delete dans ton HTML

    cy.wait('@deleteUser');
    cy.url().should('match', /\/$/);
  });
});
