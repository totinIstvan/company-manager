DROP TABLE IF EXISTS company CASCADE;
CREATE TABLE IF NOT EXISTS company
(
    id long identity NOT NULL PRIMARY KEY,
    name VARCHAR(255)  NOT NULL,
    registration_number VARCHAR(30) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(30) NOT NULL
);

DROP TABLE IF EXISTS department CASCADE;
CREATE TABLE IF NOT EXISTS department
(
    id long identity NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS company_departments CASCADE;
CREATE TABLE IF NOT EXISTS company_departments
(
    department_id long NOT NULL,
    company_id long NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(id),
    FOREIGN KEY (company_id) REFERENCES company(id)
);

DROP TABLE IF EXISTS employee CASCADE;
CREATE TABLE IF NOT EXISTS employee
(
    id long identity NOT NULL PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    title VARCHAR(80) NOT NULL,
    email VARCHAR(60) NOT NULL,
    salary INT NOT NULL,
    join_date DATETIME NOT NULL,
    company_id long NOT NULL,
    department_id long NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);
