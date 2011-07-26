package guestbook;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class SignGuestbookServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(SignGuestbookServlet.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

//        String content = req.getParameter("content");
//        if (content == null) {
//            content = "(No greeting)";
//        }
//        if (user != null) {
//            log.info("Greeting posted by user " + user.getNickname() + ": " + content);
//        } else {
//            log.info("Greeting posted anonymously: " + content);
//        }
//        resp.sendRedirect("/guestbook.jsp");
// We have one entity group per Guestbook with all Greetings residing
		// in the same entity group as the Guestbook to which they belong.
		// This lets us run an ancestor query to retrieve all Greetings for a
		// given Guestbook. However, the write rate to each Guestbook should be
		// limited to ~1/second.
		String guestbookName = req.getParameter("guestbookName");
		Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
		String content = req.getParameter("content");
		Date date = new Date();
		Entity greeting = new Entity("Greeting", guestbookKey);
		greeting.setProperty("user", user);
		greeting.setProperty("date", date);
		greeting.setProperty("content", content);

		DatastoreService datastore =
				DatastoreServiceFactory.getDatastoreService();
		datastore.put(greeting);

		resp.sendRedirect("/guestbook.jsp?guestbookName="
				+ guestbookName);
    }
}
