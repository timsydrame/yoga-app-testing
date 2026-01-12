/// <reference types="cypress" />

describe('Register spec', () => {
  it('Register successful', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'ok' },
    }).as('register');

    cy.get('input[formControlName=firstName]').type('Fatim');
    cy.get('input[formControlName=lastName]').type('Dramé');
    cy.get('input[formControlName=email]').type('fatim@test.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}`);

    cy.wait('@register');

    // selon ton app, ajuste si ça va vers /login
    cy.url().should('include', '/login');
  });
});

describe('Register spec', () => {
  it('Register successful', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'ok' },
    }).as('register');

    cy.get('input[formControlName=firstName]').type('Fatim');
    cy.get('input[formControlName=lastName]').type('Dramé');
    cy.get('input[formControlName=email]').type('fatim@test.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}`);

    cy.wait('@register');

    // selon ton app, ajuste si ça va vers /login
    cy.url().should('include', '/login');
  });
});
