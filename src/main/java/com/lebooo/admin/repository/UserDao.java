package com.lebooo.admin.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.lebooo.admin.entity.User;

public interface UserDao extends PagingAndSortingRepository<User, Long> , JpaSpecificationExecutor<User> {
	User findByLoginName(String loginName);
}
