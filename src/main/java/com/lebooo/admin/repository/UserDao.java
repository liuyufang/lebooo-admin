package com.lebooo.admin.repository;

import com.lebooo.admin.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends PagingAndSortingRepository<User, Long> , JpaSpecificationExecutor<User> {
	User findByLoginName(String loginName);
}
