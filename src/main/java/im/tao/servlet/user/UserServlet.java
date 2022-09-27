package im.tao.servlet.user;

import im.tao.entity.User;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;
import im.tao.util.Constant;
import im.tao.util.Sms4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends BaseServlet {

	private static final long serialVersionUID = -1L;

	/**
	 * create user
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if (username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0) {
			resp.setStatus(500);
			resp.getWriter().print("Error: username or password invalid");
			return;
		}
		String encryptedPassword = Sms4.encrypt(password, Constant.ENCRYPT_KEY);

		User user = GlobalStorage.getUsers().get(username);
		if (user != null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] already exists");
			return;
		}

		user = new User();
		user.setUsername(username);
		user.setPassword(encryptedPassword);
		GlobalStorage.getUsers().put(username, user);

		resp.getWriter().print("create user[" + username + "] successfully");
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		User user = GlobalStorage.getUsers().remove(username);
		if (user == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] doesn't exist");
			return;
		}

		resp.getWriter().print("remove user[" + username + "] successfully");
	}
}
