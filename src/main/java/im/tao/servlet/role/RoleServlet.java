package im.tao.servlet.role;

import im.tao.entity.Role;
import im.tao.servlet.base.BaseServlet;
import im.tao.storage.GlobalStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/role")
public class RoleServlet extends BaseServlet {

	private static final long serialVersionUID = -2L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");

		Role role = GlobalStorage.getRoles().get(name);
		if (role != null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: role[" + name + "] already exists");
			return;
		}

		role = new Role();
		role.setName(name);
		GlobalStorage.getRoles().put(name, role);

		resp.getWriter().print("create role[" + name + "] successfully");
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		Role role = GlobalStorage.getRoles().remove(name);
		if (role == null) {
			resp.setStatus(500);
			resp.getWriter().print("Error: role[" + name + "] doesn't exist");
			return;
		}

		resp.getWriter().print("remove role[" + name + "] successfully");
	}
}

