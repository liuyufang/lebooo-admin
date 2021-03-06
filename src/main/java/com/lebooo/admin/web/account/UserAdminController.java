package com.lebooo.admin.web.account;

import com.google.common.collect.Maps;
import com.lebooo.admin.entity.User;
import com.lebooo.admin.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 管理员管理用户的Controller.
 * 
 * @author calvin
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserAdminController {

    private static final int PAGE_SIZE = 5;

	@Autowired
	private AccountService accountService;

    private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
    static {
        sortTypes.put("auto", "自动");
        sortTypes.put("name", "名字");
    }

	//@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<User> users = accountService.getAllUser();
		model.addAttribute("users", users);

		return "account/adminUserList";
	}

    @RequestMapping(method = RequestMethod.GET)
    public String listPage(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<User> users = accountService.getPagingUser(searchParams, pageNumber, PAGE_SIZE, sortType);
		model.addAttribute("users", users);

        model.addAttribute("users", users);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortTypes", sortTypes);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "account/adminUserList";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("user", accountService.getUser(id));
		return "account/adminUserForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@Valid @ModelAttribute("preloadUser") User user) {
		try{
            accountService.updateUser(user);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		User user = accountService.getUser(id);
		accountService.deleteUser(id);
		redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
		return "redirect:/admin/user";
	}

    @RequestMapping(value = "deleteMulti")
    public ResponseEntity<?> deleteMulti(@RequestParam(value = "id") Long[] id){
         for(Long i : id){
             accountService.deleteUser(i);
         }
         return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "enableMulti")
    public ResponseEntity<?> enableMulti(@RequestParam(value = "id") Long[] id){
        for(Long i : id){
            User user = accountService.getUser(i);
            user.setStatus("enabled");
            accountService.updateUser(user);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "disableMulti")
    public ResponseEntity<?> disableMulti(@RequestParam(value = "id") Long[] id){
        for(Long i : id){
            User user = accountService.getUser(i);
            user.setStatus("disabled");
            accountService.updateUser(user);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserJson(@PathVariable("id") Long id) {
        User user = accountService.getUser(id);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(user, HttpStatus.OK);
    }


	/**
	 * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
	 */
	@ModelAttribute("preloadUser")
	public User getUser(@RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			return accountService.getUser(id);
		}
		return null;
	}
}
