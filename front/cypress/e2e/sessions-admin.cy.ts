/// <reference types="cypress" />

describe('Sessions admin spec', () => {
  it('Admin can access sessions list after login', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'admin',
        firstName: 'A',
        lastName: 'D',
        admin: true,
      },
    }).as('login');

    cy.intercept('GET', '/api/session', { body: [] }).as('sessions');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);

    cy.wait('@login');
    cy.wait('@sessions');
    cy.url().should('include', '/sessions');
  });

  it('Admin can create a session', () => {
    // login
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'admin',
        firstName: 'A',
        lastName: 'D',
        admin: true,
      },
    }).as('login');
    cy.intercept('GET', '/api/session', { body: [] }).as('sessions');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`);
    cy.wait('@sessions');

    // mocks écran create
    cy.intercept('GET', '/api/teacher', {
      body: [{ id: 1, firstName: 'John', lastName: 'Doe' }],
    }).as('teachers');

    cy.intercept('POST', '/api/session', {
      statusCode: 200,
      body: {
        id: 99,
        name: 'New',
        date: '2024-02-01',
        teacher_id: 1,
        description: 'Desc',
      },
    }).as('createSession');

    // ⚠️ adapte selon ton UI : bouton create / add
    cy.contains(/create/i).click();

    cy.wait('@teachers');

    cy.get('input[formControlName=name]').type('New');
    cy.get('input[formControlName=date]').type('2024-02-01');
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.contains('John Doe').click();
    cy.get('textarea[formControlName=description]').type('Desc');

    cy.contains(/save/i).click();
    cy.wait('@createSession');

    cy.url().should('include', '/sessions');
  });
});
