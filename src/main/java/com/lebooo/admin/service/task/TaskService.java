package com.lebooo.admin.service.task;

import com.google.common.collect.Maps;
import com.lebooo.admin.entity.Task;
import com.lebooo.admin.repository.TaskDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;

import java.util.Date;
import java.util.List;
import java.util.Map;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class TaskService {

	private TaskDao taskDao;

	public Task getTask(Long id) {
		return taskDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveTask(Task entity) {
		taskDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteTask(Long id) {
		taskDao.delete(id);
	}

	public List<Task> getAllTask() {
		return (List<Task>) taskDao.findAll();
	}

	public Page<Task> getUserTask(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
			String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<Task> spec = buildSpecification(userId, searchParams);

		return taskDao.findAll(spec, pageRequest);
	}

    /**
     * 查询到期任务
     */
    public List<Task> getDueTask(Date time, String type) {
        Map<String, SearchFilter> filters = Maps.newHashMap();
        filters.put("scheduleTime", new SearchFilter("scheduleTime", Operator.LTE, time));
        filters.put("status", new SearchFilter("status", Operator.EQ, Task.STATUS_VALUE_TODO));
        filters.put("type", new SearchFilter("type", Operator.EQ, type));
        Specification<Task> spec = DynamicSpecifications.bySearchFilter(filters.values(), Task.class);

        return taskDao.findAll(spec);
    }

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("title".equals(sortType)) {
			sort = new Sort(Direction.ASC, "title");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Task> buildSpecification(Long userId, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("user.id", new SearchFilter("user.id", Operator.EQ, userId));
		Specification<Task> spec = DynamicSpecifications.bySearchFilter(filters.values(), Task.class);
		return spec;
	}

	@Autowired
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
}
