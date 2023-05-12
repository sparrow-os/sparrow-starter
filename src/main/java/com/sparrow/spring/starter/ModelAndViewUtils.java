package com.sparrow.spring.starter;

import com.sparrow.constant.Config;
import com.sparrow.core.Pair;
import com.sparrow.protocol.POJO;
import com.sparrow.protocol.Param;
import com.sparrow.protocol.Query;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.VO;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.servlet.ServletContainer;
import com.sparrow.support.web.HttpContext;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.ConfigUtility;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewUtils {
    private static final String QUERY = "query";

    /**
     * 重定向
     * <p>
     * 不指定参数，因为有失败场景现场保留
     *
     * @param url
     * @return
     */
    public static ModelAndView redirect(String url) {
        ServletContainer servletContainer = SpringContext.getContext().getBean(ServletContainer.class);
        successFlash(servletContainer.getRequest(), url);
        String rootPath = ConfigUtility.getValue(Config.ROOT_PATH);
        return new ModelAndView("redirect:" + rootPath + "/transit?" + url);
    }

    public static <T extends POJO> String extractedPojoKey(T o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Query) {
            return QUERY;
        }
        return ClassUtility.getEntityNameByClass(o.getClass());
    }

    /**
     * flash param aspect 预先加载参数
     *
     * @param <T>
     * @return
     */
    public static <T extends Query> T query() {
        return (T) flash(QUERY);
    }

    public static Object flash(String key) {
        ServletContainer servletContainer = SpringContext.getContext().getBean(ServletContainer.class);
        String flashUrl = ServletUtility.getInstance().assembleActualUrl(servletContainer.getActionKey());
        Pair<String, Map<String, Object>> flash = (Pair<String, Map<String, Object>>) servletContainer.getRequest().getSession().getAttribute(Constant.FLASH_KEY);
        if (flash == null) {
            return null;
        }
        if (!flash.getFirst().equals(flashUrl)) {
            return null;
        }
        return flash.getSecond().get(key);
    }

    public static void successFlash(HttpServletRequest request, String url) {
        flash(request, null, url);
    }

    public static void failFlash(HttpServletRequest request, Result errorResult) {
        flash(request, errorResult, null);
    }

    private static void flash(HttpServletRequest request, Result errorResult, String url) {
        Map<String, Object> result = new HashMap<>();
        ServletUtility servletUtility = ServletUtility.getInstance();
        Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = attributes.nextElement();
            Object value = request.getAttribute(attribute);
            if (value instanceof POJO) {
                result.put(attribute, request.getAttribute(attribute));
            }
        }

        Map<String, Object> map = HttpContext.getContext().getHolder();
        for (String key : map.keySet()) {
            result.put(key, map.get(key));
        }
        String referer = servletUtility.referer(request);
        result.put("ref", referer);
        String flashUrl = servletUtility.assembleActualUrl(url != null ? url : referer);
        if (errorResult != null) {
            result.put(Constant.FLASH_EXCEPTION_RESULT, errorResult);
        }
        Pair<String, Map<String, Object>> sessionMap = Pair.create(flashUrl, result);
        request.getSession().setAttribute(Constant.FLASH_KEY, sessionMap);
    }
}
