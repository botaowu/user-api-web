package im.tao.servlet.user;

import im.tao.entity.Role;
import im.tao.entity.User;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;
import im.tao.util.RoleUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user/role")
public class UserRoleServlet extends BaseServlet {

	private static final long serialVersionUID = -3L;

	/**
	 * add role to user
	 *
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String roleName = req.getParameter("role");

		User user = GlobalStorage.getUsers().get(username);
		if (user == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: user[" + username + "] doesn't exist");
			return;
		}

		Role role = GlobalStorage.getRoles().get(roleName);
		if (role == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: role[" + username + "] doesn't exist");
			return;
		}

		List<Role> roles = user.getRoles();
		if (roles == null) {
			roles = new ArrayList<>();
		}
		if (!RoleUtil.contains(roles, role)) {
			roles.add(role);
			user.setRoles(roles);
			GlobalStorage.getUsers().put(username, user);
			resp.getWriter().print("add role[" + roleName + "] to user[" + username + "] successfully");
			return;
		}

		resp.getWriter().print("role[" + roleName + "] already added to the user[" + username + "]");
	}

}

