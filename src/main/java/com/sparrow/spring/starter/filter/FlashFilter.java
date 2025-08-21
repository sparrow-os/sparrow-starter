package com.sparrow.spring.starter.filter;

import com.sparrow.core.Pair;
import com.sparrow.protocol.constant.Constant;
import com.sparrow.support.web.ServletUtility;
import com.sparrow.utility.StringUtility;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * spring 实现的flash 是url 变化后自动清除session
 * 本方案支持中间跳转状态保持功能
 * ModelAndViewUtils 为实现该功能提供工具类
 */
public class FlashFilter extends OncePerRequestFilter implements Filter {
    private static ServletUtility servletUtility = ServletUtility.getInstance();

    /**
     * flash key -->/template/action-url.jsp
     * <p>
     * direct mode action url-->action-url
     * <p>
     * <p>
     * transit mode transit url--> transit-url?action_url
     */
    private boolean matchUrl(String flashKey, String actionKey, HttpServletRequest request) {
        //redirect url
        if (StringUtility.matchUrl(flashKey, actionKey)) {
            return true;
        }
        //redirect actual url
        String actualUrl = servletUtility.assembleActualUrl(actionKey);
        if (StringUtility.matchUrl(flashKey, actualUrl))
        //transit url

        actionKey = request.getQueryString();
        if (actionKey == null) {
            return false;
        }
        if (StringUtility.matchUrl(flashKey, actionKey)) {
            return true;
        }

        //transit actual url
        String actualTransitUrl = servletUtility.assembleActualUrl(actionKey);
        if (StringUtility.matchUrl(flashKey, actualTransitUrl)) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String actionKey = servletUtility.getActionKey(request);
        Pair<String, Map<String, Object>> sessionPair = (Pair<String, Map<String, Object>>) request.getSession().getAttribute(Constant.FLASH_KEY);
        if (sessionPair == null) {
            chain.doFilter(request, response);
            return;
        }

        if (this.matchUrl(sessionPair.getFirst(), actionKey, request)) {
            Map<String, Object> values = sessionPair.getSecond();
            for (String key : values.keySet()) {
                request.setAttribute(key, values.get(key));
            }
            chain.doFilter(request, response);
            return;
        }
        //url换掉时，则session 被清空 （非include）
        request.getSession().removeAttribute(Constant.FLASH_KEY);
        chain.doFilter(request, response);
    }

    /**
     * @param request
     * @return
     * @throws ServletException
     * @see https://github.com/spring-projects/spring-boot/issues/7426
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String actionKey = servletUtility.getActionKey(request);
        return actionKey.endsWith("favicon.ico");
    }
}
