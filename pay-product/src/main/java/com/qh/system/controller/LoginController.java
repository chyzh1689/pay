package com.qh.system.controller;


import java.util.List;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qh.common.annotation.Log;
import com.qh.common.controller.BaseController;
import com.qh.common.domain.Tree;
import com.qh.common.utils.CacheUtils;
import com.qh.common.utils.R;
import com.qh.common.utils.ShiroUtils;
import com.qh.pay.api.utils.ParamUtil;
import com.qh.system.domain.MenuDO;
import com.qh.system.domain.UserDO;
import com.qh.system.service.MenuService;
import com.qh.system.service.UserService;

@Controller
public class LoginController extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserService userService;
	
	@GetMapping({ "/", "" })
	String welcome(Model model) {
		return "redirect:/index";
	}

	@Log("请求访问主页")
	@GetMapping({ "/index" })
	String index(Model model) {
		List<Tree<MenuDO>> menus = menuService.listMenuTree(getUserId());
		model.addAttribute("menus", menus);
		model.addAttribute("name", getUser().getName());
		logger.info(getUser().getName());
		return "index_v1";
	}

	@GetMapping("/login")
	String login() {
		return "login";
	}

	 /**
     * 
     * @Description 用户登录 获取盐
     * @param username 用户名
     * @return
     */
	@PostMapping("/salt")
    @ResponseBody
    R salt(@RequestParam("username") String username){
		int salt = ParamUtil.generateCode6();
		CacheUtils.setLoginSalt(username, String.valueOf(salt));
        return R.okData(salt);
    }
	
	@Log("登录")
	@PostMapping("/login")
	@ResponseBody
	R ajaxLogin(@RequestParam("username") String username, @RequestParam("password")String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return R.ok();
		} catch (AuthenticationException e) {
			return R.error("用户或密码错误");
		}
	}

	@Log("修改密码")
	@PostMapping("/chgPass")
	@ResponseBody
	R chgPass(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword")String newPassword) {
		UserDO userDO = getUser();
		if(userDO == null || ParamUtil.isEmpty(userDO.getName())){
			return R.error("请登录在修改密码！");
		}
		try {
			UserDO dataUserDo =  userService.get(userDO.getUserId());
			if(dataUserDo!= null && dataUserDo.getPassword().equals(oldPassword)){
				dataUserDo.setPassword(newPassword);
				userService.updatePassword(dataUserDo);
			}else{
				return R.error("原密码错误！");
			}
			
			return R.ok();
		} catch (AuthenticationException e) {
			return R.error("用户或密码错误");
		}
	}
	
	
	@GetMapping("/logout")
	String logout() {
		ShiroUtils.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

	@GetMapping("/403")
	String error403() {
		return "403";
	}

}
