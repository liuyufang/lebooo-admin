package com.lebooo.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.lebooo.admin.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
	User findByLoginName(String loginName);
}
