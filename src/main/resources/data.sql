INSERT INTO department (dept_id, dept_name) VALUES 
(101, '営業部'),
(102, '総務部'),
(103, '開発部');

INSERT INTO employee (emp_id, emp_name, email, birth_date, salary, dept_id, password) VALUES
(50001, '佐藤太郎', 'sato.tarou@example.com', '1980-01-01', 500000, 103, 'sato2023'),
(50002, '鈴木花子', 'suzuki.hanako@example.com', '1990-03-15', 400000, 102, 'suzu2023'),
(50003, '高橋一郎', 'takahashi.ichiro@example.com', '2000-10-20', 300000, 101, 'taka2023');
