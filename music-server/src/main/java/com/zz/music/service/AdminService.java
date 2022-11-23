package com.zz.music.service;

/**
 * 管理员service窗口
 */
public interface AdminService {
    /**
     * 验证密码是否正确
     */
    public boolean verifyPassword(String username, String password);
}
