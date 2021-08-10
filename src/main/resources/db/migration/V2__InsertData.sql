INSERT INTO company (name, registration_number, address, phone_number)
VALUES ('AllAccess Doe', 'PD763549', '4615 First Ave. Pittsburg, PA 15342', '+1 111-222-3333');
INSERT INTO company (name, registration_number, address, phone_number)
VALUES ('Building Doe', 'PD167300', '1768 Fifth Ave. New York, NY 10342', '+1 444-555-6666');
INSERT INTO company (name, registration_number, address, phone_number)
VALUES ('Luxury Doe', 'PH893881', '1245 Eighth Ave. Philadelphia, PA 15872', '+1 777-888-9999');

INSERT INTO department (name)
VALUES ('Management');
INSERT INTO department (name)
VALUES ('Service Department');
INSERT INTO department (name)
VALUES ('Finance & Accounting Department');
INSERT INTO department (name)
VALUES ('H.R. Department');
INSERT INTO department (name)
VALUES ('Marketing & Sales Department');
INSERT INTO department (name)
VALUES ('Purchasing Department');
INSERT INTO department (name)
VALUES ('Planning Department');
INSERT INTO department (name)
VALUES ('IT Department');
INSERT INTO department (name)
VALUES ('Production Department');
INSERT INTO department (name)
VALUES ('R&D Department');

INSERT INTO company_departments (company_id, department_id)
VALUES (1, 1);
INSERT INTO company_departments (company_id, department_id)
VALUES (1, 2);
INSERT INTO company_departments (company_id, department_id)
VALUES (1, 3);
INSERT INTO company_departments (company_id, department_id)
VALUES (1, 5);
INSERT INTO company_departments (company_id, department_id)
VALUES (1, 8);
INSERT INTO company_departments (company_id, department_id)
VALUES (2, 1);
INSERT INTO company_departments (company_id, department_id)
VALUES (2, 3);
INSERT INTO company_departments (company_id, department_id)
VALUES (2, 8);
INSERT INTO company_departments (company_id, department_id)
VALUES (3, 1);
INSERT INTO company_departments (company_id, department_id)
VALUES (3, 2);
INSERT INTO company_departments (company_id, department_id)
VALUES (3, 4);
INSERT INTO company_departments (company_id, department_id)
VALUES (3, 6);
INSERT INTO company_departments (company_id, department_id)
VALUES (3, 7);

INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('John Doe', 'CEO', 'john.doe@doecorporation.com', 12800, '1996-06-15', 1, 1);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jack Doe', 'CEO', 'jack.doe@doecorporation.com', 12800, '1989-10-10', 2, 1);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jason Doe', 'CEO', 'jason.doe@doecorporation.com', 12800, '1990-06-11', 3, 1);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jane Doe', 'CMO', 'jane.doe@doecorporation.com', 13500, '2000-09-05', 1, 2);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jessica Doe', 'CTO', 'jessica.doe@doecorporation.com', 13800, '2007-06-25', 1, 3);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jake Doe', 'CBO', 'jake.doe@doecorporation.com', 12200, '2009-06-20', 1, 5);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Julia Doe', 'COO', 'julia.doe@doecorporation.com', 12300, '2001-06-03', 1, 8);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Josh Doe', 'CFO', 'josh.doe@doecorporation.com', 11600, '2014-10-13', 2, 3);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Joe Doe', 'CBO', 'joe.doe@doecorporation.com', 12200, '2020-09-15', 2, 8);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jacquelyn Doe', 'CBO', 'jacquelyn.doe@doecorporation.com', 12200, '2004-02-24', 3, 1);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jamie Doe', 'CMO', 'jamie.doe@doecorporation.com', 13500, '1997-12-08', 3, 7);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jeffrey Doe', 'CFO', 'jeffrey.doe@doecorporation.com', 11600, '2003-03-10', 3, 2);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('Jasper Doe', 'COO', 'jasper.doe@doecorporation.com', 12300, '2009-05-17', 3, 6);
INSERT INTO employee (name, title, email, salary, join_date, company_id, department_id)
VALUES ('James Doe', 'CTO', 'james.doe@doecorporation.com', 13800, '2001-01-15', 3, 4);
