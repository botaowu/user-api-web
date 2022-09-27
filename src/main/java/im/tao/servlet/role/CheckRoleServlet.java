package im.tao.servlet.role;

import im.tao.entity.Role;
import im.tao.entity.Token;
import im.tao.entity.User;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;
import im.tao.util.RoleUtil;
import im.tao.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/role/check")
public class CheckRoleServlet extends BaseServlet {

	private static final long serialVersionUID = -2L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String tokenContent = req.getParameter("token");
		String roleName = req.getParameter("role");

		Role role = GlobalStorage.getRoles().get(roleName);
		if (role == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: role[" + roleName + "] is invalid");
			return;
		}

		Token token = TokenUtil.getToken(tokenContent);
		if (token == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: token[" + tokenContent + "] is invalid");
			return;
		}

		if (token.getCreateTime() == null || LocalDateTime.now().minusHours(2).isAfter(token.getCreateTime())) {
			resp.setStatus(500);
			resp.getWriter().print("Error: token[" + tokenContent + "] is expired");
			return;
		}

		String username = token.getUsername();
		User user = GlobalStorage.getUsers().get(username);
		if (user == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] not found");
			return;
		}

		List<Role> roles = user.getRoles();
		if (RoleUtil.contains(roles, role)) {
			resp.getWriter().print("true");
			return;
		}

		resp.getWriter().print("false");
	}

}

