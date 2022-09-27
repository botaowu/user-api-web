package im.tao.servlet.user;

import im.tao.entity.Token;
import im.tao.entity.User;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;
import im.tao.util.EncryptUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@WebServlet("/auth")
public class AuthServlet extends BaseServlet {

	private static final long serialVersionUID = -3L;

	/**
	 * user auth
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
		if (password == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: password invalid");
			return;
		}
		String encryptedPassword = EncryptUtil.encode(password);

		User user = GlobalStorage.getUsers().get(username);
		if (user == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] not found");
			return;
		}

		if (!Objects.equals(encryptedPassword, user.getPassword())) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] auth failed");
			return;
		}

		String content = UUID.randomUUID().toString();
		Token token = new Token();
		token.setCreateTime(LocalDateTime.now());
		token.setContent(content);
		token.setUsername(username);
		GlobalStorage.getTokens().put(username, token);

		resp.getWriter().print(token.getContent());
	}

}
