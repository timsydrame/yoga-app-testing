/// <reference types="cypress" />

describe('Me page (admin)', () => {
  it('Loads user information and shows admin badge', () => {
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

    cy.intercept('GET', '**/api/session', { body: [] }).as('sessions');

    cy.intercept('GET', '**/api/user/1', {
      body: {
        id: 1,
        firstName: 'Admin',
        lastName: 'User',
        email: 'yoga@studio.com',
        admin: true,
        createdAt: '2024-01-01T00:00:00.000Z',
        updatedAt: '2024-01-02T00:00:00.000Z',
      },
    }).as('user');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

    cy.wait('@login');
    cy.wait('@sessions');

    // ✅ aller sur /me sans reload
    cy.window().then((win) => {
      win.history.pushState({}, '', '/me');
      win.dispatchEvent(new PopStateEvent('popstate'));
    });

    cy.wait('@user');

    cy.contains('User information').should('be.visible');
    cy.contains('You are admin').should('be.visible');
  });
});
