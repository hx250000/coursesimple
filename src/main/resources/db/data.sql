USE course_db;enrollment
-- 初始化学生表
INSERT INTO student (id, student_id, name, major, grade, email, created_at) VALUES
(UUID(), '20250001', '张三', '计算机科学与技术', 3, 'zhangsan@zjgsu.edu.cn', NOW()),
(UUID(), '20250002', '李四', '软件工程', 2, 'lisi@zjgsu.edu.cn', NOW()),
(UUID(), '20250003', '王五', '信息管理', 1, 'wangwu@zjgsu.edu.cn', NOW());

-- 初始化课程表
INSERT INTO course (id, course_id, title, capacity, enrolled, email, instructor_id, name, day_of_week, start_time, end_time, expected_attendance, created_at) VALUES
(UUID(), 'CS101', 'Java 程序设计', 60, 0, 'zhanglaoshi@zjgsu.edu.cn', 'T001', '张老师', '周一', '08:00', '09:40', 0, NOW()),
(UUID(), 'CS102', '数据库原理', 50, 0, 'lilaoshi@zjgsu.edu.cn', 'T002', '李老师', '周三', '10:00', '11:40', 0, NOW()),
(UUID(), 'CS103', 'Web 应用开发', 45, 0, 'wulaoshi@zjgsu.edu.cn', 'T003', '吴老师', '周五', '14:00', '15:40', 0, NOW());

-- 初始化选课表（学生选课程）
-- 使用子查询确保外键一致
INSERT INTO enrollment (id, student_id, course_id, status, created_at)
VALUES
(UUID(),
 (SELECT id FROM student WHERE student_id='20250001'),
 (SELECT id FROM course WHERE course_id='CS101'),
 'ACTIVE', NOW()),
(UUID(),
 (SELECT id FROM student WHERE student_id='20250002'),
 (SELECT id FROM course WHERE course_id='CS102'),
 'ACTIVE', NOW()),
(UUID(),
 (SELECT id FROM student WHERE student_id='20250001'),
 (SELECT id FROM course WHERE course_id='CS103'),
 'ACTIVE', NOW());
