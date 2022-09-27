package im.tao.servlet.user;

import im.tao.entity.Token;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@WebServlet("/token/invalidate")
public class InvalidateServlet extends BaseServlet {

	private static final long serialVersionUID = -3L;

	/**
	 * invalidate token
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content = req.getParameter("token");

		String username = null;
		Token token = null;

		Map<String, Token> tokens = GlobalStorage.getTokens();
		for (String key: tokens.keySet()) {
			Token t = tokens.get(key);
			if (content != null && content.equals(t.getContent())) {
				username = key;
				token = t;
				break;
			}
		}

		if (username == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: token[" + content + "] not found");
			return;
		}

		LocalDateTime createTime = token.getCreateTime();
		if (createTime == null) {
			createTime = LocalDateTime.now().minusHours(24);
		}

		if (LocalDateTime.now().minusHours(2).isBefore(createTime)) {
			createTime = LocalDateTime.now().minusHours(24);
		}

		token.setCreateTime(createTime);
		GlobalStorage.getTokens().put(username, token);
		resp.getWriter().print("token[" + token.getContent() + "] has been invalidated");
	}

}
