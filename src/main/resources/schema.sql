CREATE TABLE IF NOT EXISTS department (
  dept_id int(8),
  dept_name varchar(20) NOT NULL,
  PRIMARY KEY (dept_id)
);

CREATE TABLE IF NOT EXISTS employee (
  emp_id int(8),
  emp_name varchar(20) NOT NULL,
  email varchar(255) NOT NULL,
  birth_date date NOT NULL,
  salary int(255) NOT NULL,
  dept_id int(8) NOT NULL,
  password varchar(10) NOT NULL,
  PRIMARY KEY (emp_id),
  FOREIGN KEY (dept_id) REFERENCES department (dept_id)
);
