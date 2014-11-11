package edu.uci.ics.crawler4j.examples.basic;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@Component
public class MyServlet extends HttpServlet {
	

	@Autowired
	BasicCrawlController basicCrawlController;

	public void init(ServletConfig config) {
		    try {
				super.init(config);
			} catch (ServletException e) { 
				e.printStackTrace();
			}
		    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
		      config.getServletContext());
		  }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String name = request.getParameter("name");

        if (request.getParameter("button1") != null) {
        	try {
				basicCrawlController.startCrawl(new String[]{"./data", "3", name});
				/*while(!basicCrawlController.getController().isFinished()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        if (request.getParameter("button2") != null) {
				basicCrawlController.stop();
				/*while(basicCrawlController.getController().isFinished()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
	
        }
        request.setAttribute("message", "somemessage");

        request.getRequestDispatcher("/WEB-INF/jsp/crawl.jsp").forward(request, response);
    }

}