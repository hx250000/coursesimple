package com.zjgsu.hx.schoolcoursesimple.controller;

import com.zjgsu.hx.schoolcoursesimple.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class HealthController {
    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/api/health/db")
    public ApiResponse<String> checkDatabaseHealth() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(2)) {
                return ApiResponse.success("✅ 数据库连接正常");
            } else {
                return ApiResponse.error(500, "⚠️ 数据库连接异常");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "❌ 数据库无法连接：" + e.getMessage());
        }
    }
}
