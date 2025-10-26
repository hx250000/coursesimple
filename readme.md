# 校园选课系统（School Course Management System）

# 一、系统概述

本系统模拟了高校的**选课业务逻辑**，实现了学生管理、课程管理与选课管理三大模块。  
采用 Spring Boot 架构，以 **RESTful API** 的形式提供统一接口，支持标准 JSON 格式输入输出。  

系统数据通过内存中的 Map 进行存储。

# 二、系统架构设计

com.zjgsu.hx.schoolcoursesimple
 ├── controller/         # 控制层：处理 HTTP 请求与响应
 │   ├── StudentController.java
 │   ├── CourseController.java
 │   └── EnrollmentController.java
 ├── service/            # 业务逻辑层：实现核心功能与校验
 │   ├── StudentService.java
 │   ├── CourseService.java
 │   └── EnrollmentService.java
 ├── repository/         # 数据访问层：使用 Map 模拟存储
 │   ├── StudentRepository.java
 │   ├── CourseRepository.java
 │   └── EnrollmentRepository.java
 ├── model/              # 实体类：学生、课程、选课信息
 │   ├── Student.java
 │   ├── Course.java
 │   └── Enrollment.java
 ├── exception/          # 异常类与全局异常处理
 │   ├── GlobalExceptionHandler.java
 │   ├── ResourceNotFoundException.java
 │   └── BusinessException.java
 ├── common/
 │   └── ApiResponse.java   # 统一响应体
 └── SchollcoursesimpleApplication.java



# 三、功能设计

| 模块     | 功能说明                                                   |
| -------- | ---------------------------------------------------------- |
| 学生管理 | 新增、修改、删除、查询学生信息；删除前检查是否存在选课记录 |
| 课程管理 | 新增、修改、删除、查询课程信息；删除前检查是否存在选课记录 |
| 选课管理 | 学生选课、退课、查询选课记录；检查容量上限与重复选课       |
| 异常处理 | 统一异常返回机制；邮箱格式验证；容量、重复、缺失校验       |


# 四、接口说明（RESTful Api）

## 1. 学生模块 `/students`

| 操作         | 方法   | URL              | 说明                     |
| ------------ | ------ | ---------------- | ------------------------ |
| 查询所有学生 | GET    | `/students`      | 获取全部学生信息         |
| 查询单个学生 | GET    | `/students/{id}` | 通过 id 查询             |
| 新增学生     | POST   | `/students`      | 创建新学生               |
| 更新学生     | PUT    | `/students/{id}` | 修改学生信息             |
| 删除学生     | DELETE | `/students/{id}` | 若存在选课记录则禁止删除 |

示例请求（POST）：

```
{
  "studentId": "2023001",
  "name": "张三",
  "email": "zs@xx.com",
  "grade": 2023
  "major": "计算机科学"
}
```

## 2. 课程模块 `/courses`

| 操作         | 方法   | URL             | 说明                         |
| ------------ | ------ | --------------- | ---------------------------- |
| 查询课程     | GET    | `/courses`      | 获取所有课程                 |
| 查询单个课程 | GET    | `/courses/{id}` | 按 ID 获取                   |
| 新增课程     | POST   | `/courses`      | 创建课程（需课程代码与名称） |
| 更新课程     | PUT    | `/courses/{id}` | 修改课程信息                 |
| 删除课程     | DELETE | `/courses/{id}` | 若存在选课记录则禁止删除     |

示例请求（POST）：

```
{
  "courseId": "C001",
  "title": "操作系统",
  "capacity": 60,
  "instructor": {},
  "scheduleslot": {}
}
```

## 3. 选课模块 `/enrollments`

| 操作         | 方法   | URL                                            | 说明             |
| ------------ | ------ | ---------------------------------------------- | ---------------- |
| 查询所有选课 | GET    | `/enrollments`                                 | 获取所有选课记录 |
| 按学生查询   | GET    | `/enrollments/student/{studentId}`             | 某学生的选课     |
| 按课程查询   | GET    | `/enrollments/course/{courseId}`               | 某课程的学生列表 |
| 学生选课     | POST   | `/enrollments?studentId=2023001&courseId=C001` | 学生选课         |
| 学生退课     | DELETE | `/enrollments?studentId=2023001&courseId=C001` | 学生退课         |
| 删除选课记录 | DELETE | `/enrollments/{id}`                            | 管理员删除       |


# 五、异常处理机制

系统通过 `GlobalExceptionHandler` 统一捕获异常并以 `ApiResponse` 格式返回。

| 异常类型                    | 返回码 | 示例说明                       |
| --------------------------- | ------ | ------------------------------ |
| `ResourceNotFoundException` | 404    | 学生或课程不存在               |
| `IllegalArgumentException`  | 400    | 参数错误，如邮箱格式           |
| `ResourceConflictException` | 409    | 业务冲突，如容量已满、重复选课 |
| 其他未捕获异常              | 500    | 系统内部错误                   |

返回示例：

```
{
  "code": 409,
  "message": "选课失败：课程容量已满",
  "data": null,
  "timestamp": "2025-10-25T22:50:23.241"
}
```

# 六、运行环境与配置

| 项目        | 版本             |
| ----------- | ---------------- |
| JDK         | 17+              |
| Spring Boot | 3.x              |
| 构建工具    | Maven            |
| 测试工具    | Postman / Apifox |
| 端口号      | 8080             |

## 启动方式
```bash
mvn spring-boot:run
```
启动后访问：

```bash
http://localhost:8080
```

或者直接在IntelliJ IDEA中启动项目

# 七、测试说明

## 测试工具
测试工具： **Apifox**
## 测试内容
### API 测试

**测试场景 1：完整的课程管理流程 **
1.创建 3 门不同的课程
2.查询所有课程，验证返回 3 条记录
3.根据 ID 查询某门课程
4.更新该课程的信息
5.删除该课程
6.再次查询，验证返回 404
**测试场景 2：选课业务流程 **
1.创建一门容量为 2 的课程
2.学生 S001 选课，验证成功
3.学生 S002 选课，验证成功
4.学生 S003 选课，验证失败（容量已满）
5.学生 S001 再次选课，验证失败（重复选课）
6.查询课程，验证 enrolled 字段为 2
**测试场景 3：学生管理流程 **
1.创建 3 个不同学号的学生（如 S2024001, S2024002, S2024003）
2.查询所有学生，验证返回 3 条记录
3.根据 ID 查询某个学生，验证返回正确信息
4.更新该学生的专业和邮箱信息，验证更新成功
5.尝试让一个不存在的学生选课，验证返回 404 错误
6.让学生 S2024001 选课，然后尝试删除该学生，验证返回错误（存在选课记录）
7.删除没有选课记录的学生 S2024003，验证删除成功
**测试场景 4：错误处理 **
1.查询不存在的课程 ID，验证返回 404
2.创建课程时缺少必填字段，验证返回 400
3.选课时提供不存在的课程 ID，验证返回 404
4.创建学生时使用重复的 studentId，验证返回错误
5.创建学生时使用无效的邮箱格式，验证返回错误

### 测试文档
创建测试文档（Markdown 或 HTTP 文件），记录：
1.每个测试场景的请求示例
2.实际响应结果
3.遇到的问题和解决方案

