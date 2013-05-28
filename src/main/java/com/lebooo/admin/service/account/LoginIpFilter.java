package com.lebooo.admin.service.account;

import com.lebooo.admin.entity.User;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.DateProvider;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author: Wei Liu
 * Date: 13-5-23
 * Time: PM1:28
 */
public class LoginIpFilter extends FormAuthenticationFilter {
    @Autowired
    private AccountService accountService;

    private DateProvider dateProvider = DateProvider.DEFAULT;

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser)subject.getPrincipals().getPrimaryPrincipal();

        User user = accountService.getUser(shiroUser.id);
        user.setLastLoginIp(request.getRemoteAddr());
        user.setLastLoginDate(dateProvider.getDate());
        accountService.updateUser(user);

        return super.onLoginSuccess(token, subject, request, response);
    }
}
