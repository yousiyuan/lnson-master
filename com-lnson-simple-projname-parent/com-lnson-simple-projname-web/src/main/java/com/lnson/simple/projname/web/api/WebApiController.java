package com.lnson.simple.projname.web.api;

import com.lnson.simple.projname.entity.EbBrand;
import com.lnson.simple.projname.service.EbBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//学习参考链接
//  https://www.cnblogs.com/daimajun/p/7152970.html

/**
 * 
 * 注解@ResponseBody使用说明：
 * 
 * 注解@ResponseBody是作用在方法上的，@ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中，
 * 一般在异步获取数据时使用【也就是AJAX】，在使用 @RequestMapping后，返回值通常解析为跳转路径， 但是加上 @ResponseBody
 * 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。 比如异步获取 json 数据，加上 @ResponseBody
 * 后，会直接返回 json 数据。
 * 
 * ==================================================================================================================
 * 
 * 注解@RequestBody使用说明：
 * 
 * 注解@RequestBody是作用在形参列表上，用于将前台发送过来固定格式的数据【xml 格式或者 json等】封装为对应的 JavaBean 对象，
 * 封装时使用到的一个对象是系统默认配置的 HttpMessageConverter进行解析，然后封装到形参上。
 * 
 * ==================================================================================================================
 * 
 * 注解@RequestParam使用说明：
 * 
 * 注解@RequestParam修饰方法中形参，获取请求中特定的请求参数值并赋值给形参，同时可以对特定的请求参数进行验证、设置默认值等等
 */
@Controller
@RequestMapping(value = "/webapi")
public class WebApiController {

	@Autowired
	private EbBrandService ebBrandService;

	// 测试链接===》》http://localhost/lnsonweb/webapi/brand.do?cid=3029
	@RequestMapping(value = "/brand", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
	EbBrand queryBrandById(Long cid) {

		return ebBrandService.queryForObject(cid);
	}

	// 测试链接===》》http://localhost/lnsonweb/webapi/brand2.do?cid=3029
	@RequestMapping(value = "/brand2", method = { RequestMethod.GET })
	public @ResponseBody
    EbBrand queryBrand(@RequestParam(value = "cid") Long id) {

		return ebBrandService.queryForObject(id);
	}

	// POST MAN 工具测试
	// 测试链接===》》http://localhost/lnsonweb/webapi/brand1.do
	// 参数 {"cid":3029}
	@RequestMapping(value = "/brand1", method = { RequestMethod.POST })
	public @ResponseBody
    EbBrand queryBrandById(HttpServletRequest request, @RequestBody Map<String, Object> map) {

		Long cid = Long.valueOf(map.get("cid").toString());

		return ebBrandService.queryForObject(cid);
	}

}
