package im.tao;

import im.tao.servlet.role.CheckRoleServlet;
import im.tao.servlet.role.ListRoleServlet;
import im.tao.servlet.role.RoleServlet;
import im.tao.servlet.user.AuthServlet;
import im.tao.servlet.user.InvalidateServlet;
import im.tao.servlet.user.UserRoleServlet;
import im.tao.servlet.user.UserServlet;
import im.tao.storage.GlobalStorage;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.JVM)
public class UserServletTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    UserServlet userServlet;

    RoleServlet roleServlet;

    UserRoleServlet userRoleServlet;

    AuthServlet authServlet;

    InvalidateServlet invalidateServlet;

    CheckRoleServlet checkRoleServlet;


    ListRoleServlet listRoleServlet;

    @Before
    public void before() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        userServlet = new UserServlet();
        roleServlet = new RoleServlet();
        userRoleServlet = new UserRoleServlet();
        authServlet = new AuthServlet();
        invalidateServlet = new InvalidateServlet();
        checkRoleServlet = new CheckRoleServlet();
        listRoleServlet = new ListRoleServlet();
    }

    @Test
    public void testUserCreate() throws IOException, ServletException {
        String username = "Spring";
        String password = "Test";

        request.setParameter("username", username);
        request.setParameter("password", password);
        userServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "create user[" + username + "] successfully");
    }

    @Test
    public void testUserCreateDuplicate() throws IOException, ServletException {
        String username = "Spring";
        String password = "Test";

        request.setParameter("username", username);
        request.setParameter("password", password);
        userServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "Error: user[" + username + "] already exists");
    }

    @Test
    public void testRoleCreate() throws IOException, ServletException {
        String name = "Admin";

        request.setParameter("name", name);
        roleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "create role[" + name + "] successfully");
    }

    @Test
    public void testRoleCreateDuplicate() throws IOException, ServletException {
        String name = "Admin";

        request.setParameter("name", name);
        roleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "Error: role[" + name + "] already exists");
    }

    @Test
    public void testAddRole() throws IOException, ServletException {
        String username = "Spring";
        String roleName = "Admin";
        request.setParameter("username", username);
        request.setParameter("role", roleName);
        userRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "add role[" + roleName + "] to user[" + username + "] successfully");
    }

    @Test
    public void testAddRoleDuplicate() throws IOException, ServletException {
        String username = "Spring";
        String roleName = "Admin";
        request.setParameter("username", username);
        request.setParameter("role", roleName);
        userRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "role[" + roleName + "] already added to the user[" + username + "]");
    }

    @Test
    public void testRoleDelete() throws IOException, ServletException {
        String username = "Admin";

        request.setParameter("name", username);
        roleServlet.doDelete(request, response);

        assertEquals(response.getContentAsString(), "remove role[" + username + "] successfully");
    }

    @Test
    public void testRoleDeleteDuplicate() throws IOException, ServletException {
        String username = "Admin";

        request.setParameter("name", username);
        roleServlet.doDelete(request, response);

        assertEquals(response.getContentAsString(), "Error: role[" + username + "] doesn't exist");
    }

    @Test
    public void testUserDelete() throws IOException, ServletException {
        String username = "Spring";

        request.setParameter("username", username);
        userServlet.doDelete(request, response);

        assertEquals(response.getContentAsString(), "remove user[" + username + "] successfully");
    }

    @Test
    public void testUserDeleteDuplicate() throws IOException, ServletException {
        String username = "Spring";

        request.setParameter("username", username);
        userServlet.doDelete(request, response);

        assertEquals(response.getContentAsString(), "Error: user[" + username + "] doesn't exist");
    }

    @Test
    public void testUserCreate2() throws IOException, ServletException {
        String username = "Spring";
        String password = "Test";

        request.setParameter("username", username);
        request.setParameter("password", password);
        userServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "create user[" + username + "] successfully");
    }

    @Test
    public void testRoleCreate2() throws IOException, ServletException {
        String name = "Admin";

        request.setParameter("name", name);
        roleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "create role[" + name + "] successfully");
    }

    @Test
    public void testAddRole2() throws IOException, ServletException {
        String username = "Spring";
        String roleName = "Admin";
        request.setParameter("username", username);
        request.setParameter("role", roleName);
        userRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "add role[" + roleName + "] to user[" + username + "] successfully");
    }

    @Test
    public void testUserAuth() throws IOException, ServletException {
        String username = "Spring";
        String password = "Test";

        request.setParameter("username", username);
        request.setParameter("password", password);
        authServlet.doPost(request, response);

        String token = GlobalStorage.getTokens().get(username).getContent();

        assertEquals(response.getContentAsString(), token);
    }

    @Test
    public void testRoleCreate3() throws IOException, ServletException {
        String name = "User";

        request.setParameter("name", name);
        roleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "create role[" + name + "] successfully");
    }

    @Test
    public void testAddRole3() throws IOException, ServletException {
        String username = "Spring";
        String roleName = "User";
        request.setParameter("username", username);
        request.setParameter("role", roleName);
        userRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "add role[" + roleName + "] to user[" + username + "] successfully");
    }

    @Test
    public void testCheckRole() throws IOException, ServletException {
        String username = "Spring";
        String role = "Admin";
        String token = GlobalStorage.getTokens().get(username).getContent();
        request.setParameter("token", token);
        request.setParameter("role", role);
        checkRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "true");
    }

    @Test
    public void testListRole() throws IOException, ServletException {
        String username = "Spring";
        String token = GlobalStorage.getTokens().get(username).getContent();
        request.setParameter("token", token);
        listRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "Admin, User");
    }

    @Test
    public void testInvalidate() throws IOException, ServletException {
        String username = "Spring";
        String token = GlobalStorage.getTokens().get(username).getContent();
        request.setParameter("token", token);
        invalidateServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "token[" + token + "] has been invalidated");
    }

    @Test
    public void testCheckRole2() throws IOException, ServletException {
        String username = "Spring";
        String role = "Admin";
        String token = GlobalStorage.getTokens().get(username).getContent();
        request.setParameter("token", token);
        request.setParameter("role", role);
        checkRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "Error: token[" + token + "] is expired");
    }

    @Test
    public void testListRole2() throws IOException, ServletException {
        String username = "Spring";
        String token = GlobalStorage.getTokens().get(username).getContent();
        request.setParameter("token", token);
        listRoleServlet.doPost(request, response);

        assertEquals(response.getContentAsString(), "Error: token[" + token + "] is expired");
    }

}
