package com.atguigu.springmvc.handlers;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.atguigu.springmvc.entities.User;

/**
 * @author CYH
 * @date 2018/1/3 0003
 */
@Controller
@RequestMapping("/springmvc")
@SessionAttributes(value = {"user"}, types = {String.class})
public class SpringMVCTest {

    private static final String SUCCESS = "success";

    @RequestMapping("/testRequestMapping")
    public String testRequestMapping() {
        System.out.println("testRequestMapping");
        return SUCCESS;
    }

    @RequestMapping(value = "/testMethod", method = RequestMethod.POST)
    public String testMethod() {
        System.out.println("testMethod");
        return SUCCESS;
    }

    @RequestMapping(value = "/testParamsAndHeaders", params = {"username", "age!=10"},
            headers = {"Accept-Language=zh-CN,zh;q=0.9"})
    public String testParamsAndHeaders() {
        System.out.println("testParamsAndHeaders");
        return SUCCESS;
    }

    @RequestMapping("/testAntPath/*/abc")
    public String testAntPath() {
        System.out.println("testAntPath");
        return SUCCESS;
    }

    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") String id) {
        System.out.println("testPathVariable: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRestGet/{id}", method = RequestMethod.GET)
    public String testRestGet(@PathVariable String id) {
        System.out.println("testRestGet: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRestPost", method = RequestMethod.POST)
    public String testRestPost() {
        System.out.println("testRestPost");
        return SUCCESS;
    }

    @RequestMapping(value = "/testRestDelete/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable String id) {
        System.out.println("testRestDelete: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRestPut/{id}", method = RequestMethod.PUT)
    public String testRestPut(@PathVariable String id) {
        System.out.println("testRestPut: " + id);
        return SUCCESS;
    }

    @RequestMapping("/testRequestParam")
    public String testRequestParam(@RequestParam("username") String username,
            @RequestParam(value = "age", required = false, defaultValue = "0") Integer age) {

        System.out.println("testRequestParam: username: " + username + ", age: " + age);
        return SUCCESS;
    }

    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(@RequestHeader("Accept-Language") String language) {
        System.out.println("testRequestHeader: " + language);
        return SUCCESS;
    }

    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("testCookieValue: " + sessionId);
        return SUCCESS;
    }

    @RequestMapping("/testPojo")
    public String testPojo(User user) {
        System.out.println("testPojo: " + user);
        return SUCCESS;
    }

    /**
     * 可以使用 Servlet 原生的 API 作为目标方法的参数 具体支持以下类型
     *
     * HttpServletRequest
     * HttpServletResponse
     * HttpSession
     * java.security.Principal
     * Locale InputStream
     * OutputStream
     * Reader
     * Writer
     * @throws IOException
     */
    @RequestMapping("/testServletApi")
    public void testServletApi(HttpServletRequest request, HttpServletResponse response, Writer writer)
            throws IOException {

        System.out.println("testServletApi: request: " + request + ", response: " + response + ", write: " + writer);
        writer.write("testServletApi");
    }

    /**
     * 目标方法的返回值可以是 ModelAndView 类型。
     * 其中可以包含视图和模型信息
     * SpringMVC 会把 ModelAndView 的 model 中数据放入到 request 域对象中.
     * @return
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView() {
        ModelAndView modelAndView = new ModelAndView(SUCCESS);
        modelAndView.addObject("time", new Date());
        return modelAndView;
    }

    /**
     * 目标方法可以添加 Map 类型(实际上也可以是 Model 类型或 ModelMap 类型)的参数.
     * @param map
     * @return
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String, Object> map) {
        System.out.println("map.getClass().getName(): " + map.getClass().getName());
        map.put("names", Arrays.asList("Tom", "Jerry", "Mike"));
        return SUCCESS;
    }

    /**
     * @SessionAttributes 除了可以通过属性名指定需要放到会话中的属性外(实际上使用的是 value 属性值),
     * 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中(实际上使用的是 types 属性值)
     *
     * 注意: 该注解只能放在类的上面. 而不能修饰放方法.
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String, Object> map) {
        User user = new User("Tom", "123456", "tom@qq.com", 15);
        map.put("user", user);
        map.put("school", "atguigu");
        return SUCCESS;
    }

    /**
     * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法执行之前被 SpringMVC 调用!
     * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
     *      1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
     *      2). SpringMVC 会以 value 为 key, POJO 类型的对象为 value, 存入到 request 中.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        System.out.println("ModelAttribute getUser");
        if (id != null) {
            //模拟从数据库中获取对象
            User user = new User("Tom", "123456", "tom@atguigu.com", 12);
            System.out.println("Obtain user from database: " + user);
            map.put("user", user);
        }
    }

    /**
     * 运行流程:
     * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
     * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
     * 3. SpringMVC 把上述对象传入目标方法的参数.
     *
     * 注意: 在 @ModelAttribute 修饰的方法中, 放入到 Map 时的键需要和目标方法入参类型的第一个字母小写的字符串一致!
     *
     * SpringMVC 确定目标方法 POJO 类型入参的过程
     * 1. 确定一个 key:
     *      1). 若目标方法的 POJO 类型的参数木有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的小写
     *      2). 若使用了 @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值.
     * 2. 在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入
     *      1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和在第 1 步中确定的 key 一致, 则会获取到.
     * 3. 若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰,
     *  若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所
     *  对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常.
     * 4. 若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则
     *  会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数
     * 5. SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中.
     *
     * 源代码分析的流程
     * 1. 调用 @ModelAttribute 注解修饰的方法. 实际上把 @ModelAttribute 方法中 Map 中的数据放在了 implicitModel 中.
     * 2. 解析请求处理器的目标参数, 实际上该目标参数来自于 WebDataBinder 对象的 target 属性
     *  1). 创建 WebDataBinder 对象:
     *      ①. 确定 objectName 属性:
     *          若传入的 attrName 属性值为 "", 则 objectName 为类名第一个字母小写.
     *          *注意: attrName. 若目标方法的 POJO 属性使用了 @ModelAttribute 来修饰,
     *          则 attrName 值即为 @ModelAttribute 的 value 属性值
     *
     *      ②. 确定 target 属性:
     * 	        > 在 implicitModel 中查找 attrName 对应的属性值. 若存在, ok
     * 	        > *若不存在: 则验证当前 Handler 是否使用了 @SessionAttributes 进行修饰,
     * 	          若使用了, 则尝试从 Session 中 获取 attrName 所对应的属性值.
     * 	          若 session 中没有对应的属性值, 则抛出了异常.
     * 	        > 若 Handler 没有使用 @SessionAttributes 进行修饰, 或 @SessionAttributes 中没有使用 value 值指定的 key
     *            和 attrName 相匹配, 则通过反射创建了 POJO 对象
     *
     *  2). SpringMVC 把表单的请求参数赋给了 WebDataBinder 的 target 对应的属性.
     *  3). *SpringMVC 会把 WebDataBinder 的 attrName 和 target 给到 implicitModel. 进而传到 request 域对象中.
     *  4). 把 WebDataBinder 的 target 作为参数传递给目标方法的入参.
     */
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(User user) {
        System.out.println("Modifying: " + user);
        return SUCCESS;
    }

    @RequestMapping("/testViewAndViewResolver")
    public String testViewAndViewResolver() {
        System.out.println("testViewAndViewResolver");
        return SUCCESS;
    }

    @RequestMapping("/testView")
    public String testView() {
        System.out.println("testView");
        return "helloView";
    }

    @RequestMapping("/testRedirect")
    public String testRedirect() {
        System.out.println("testRedirect");
        return "redirect:/index.jsp";
    }

}
