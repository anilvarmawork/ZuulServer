package com.zuul.publicServer.RequestFilters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


public class PreFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        int authorizationStatus = isAuthorized(request);
        if (authorizationStatus != 202){
            // blocks the request
            ctx.setSendZuulResponse(false);

            // response to client
            ctx.setResponseBody("API key not authorized");
            ctx.getResponse().setHeader("Content-Type", "text/plain;charset=UTF-8");
            ctx.setResponseStatusCode(authorizationStatus);
        }

        return null;
    }

    private int isAuthorized(HttpServletRequest httpServletRequest) {

        int status = 401;

        if(httpServletRequest.getHeader("Authorization") != null) {
            RestTemplate restTemplate = new RestTemplate();
            String uri = "http://localhost:8080/forward";


            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
             while (headerNames.hasMoreElements()) {
                    String tmpHeader = headerNames.nextElement();
                    headers.set(tmpHeader,httpServletRequest.getHeader(tmpHeader));
                    }

             HttpEntity<String> entity = new HttpEntity<>("parameters", headers);


            ResponseEntity<String> resp2 = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
//            System.out.println(resp2.getStatusCode());
//            System.out.println(resp2.getHeaders());
            status = resp2.getStatusCode().value();
        }


        return status;
    }
}
