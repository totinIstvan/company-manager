INSERT INTO company (name, registration_number, address) VALUES ( 'AllAccess Doe', 'PD763549', '4615 First Ave. Pittsburg, PA 15342' );
INSERT INTO company (name, registration_number, address) VALUES ( 'Building Doe', 'PD167300', '1768 Fifth Ave. New York, NY 10342' );
INSERT INTO company (name, registration_number, address) VALUES ( 'Luxury Doe', 'PH893881', '1245 Eighth Ave. Philadelphia, PA 15872' );

INSERT INTO department (name) VALUES ( 'Management' );
INSERT INTO department (name) VALUES ( 'Service Department' );
INSERT INTO department (name) VALUES ( 'Finance & Accounting Department' );
INSERT INTO department (name) VALUES ( 'H.R. Department' );
INSERT INTO department (name) VALUES ( 'Marketing & Sales Department' );
INSERT INTO department (name) VALUES ( 'Purchasing Department' );
INSERT INTO department (name) VALUES ( 'Planning Department' );
INSERT INTO department (name) VALUES ( 'IT Department' );
INSERT INTO department (name) VALUES ( 'Production Department' );
INSERT INTO department (name) VALUES ( 'R&D Department' );

INSERT INTO company_departments (company_id, department_id) VALUES ( 1, 1 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 1, 2 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 1, 3 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 1, 5 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 1, 8 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 2, 1 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 2, 3 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 2, 8 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 3, 1 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 3, 2 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 3, 4 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 3, 6 );
INSERT INTO company_departments (company_id, department_id) VALUES ( 3, 7 );

INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'John Doe', 'CEO', 15000, '1980-06-15',  1, 1);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jack Doe', 'CEO', 15000, '1989-10-10',  2, 1);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jason Doe', 'CEO', 15000, '1980-06-15',  3, 1);

INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jane Doe', 'CMO', 15000, '1980-06-15',  1, 2);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jessica Doe', 'CTO', 15000, '1980-06-15',  1, 3);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jake Doe', 'CBO', 15000, '1980-06-15',  1, 5);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Julia Doe', 'COO', 15000, '1980-06-15',  1, 8);

INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Josh Doe', 'CFO', 15000, '1980-06-15',  2, 3);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Joe Doe', 'CBO', 15000, '1980-06-15',  2, 8);

INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jacquelyn Doe', 'CBO', 15000, '1980-06-15',  3, 1);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jaime Doe', 'CMO', 15000, '1980-06-15',  3, 7);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jeffrey Doe', 'CFO', 15000, '1980-06-15',  3, 2);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'Jasper Doe', 'COO', 15000, '1980-06-15',  3, 6);
INSERT INTO employee (name, title, salary, join_date, company_id, department_id) VALUES ( 'James Doe', 'CTO', 15000, '1980-06-15',  3, 4);