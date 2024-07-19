package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";
    private static final String PATH_WITHOUT_NUMBERS = "/api/posts";
    private static final String PATH_WITH_NUMBERS = "/api/posts/\\d+";


    private PostController controller;

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = (PostController) context.getBean("postController");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final String path = req.getRequestURI();
            final String method = req.getMethod();
            // primitive routing
            if (method.equals(METHOD_GET) && path.equals(PATH_WITHOUT_NUMBERS)) {//all
                controller.all(resp);//send empty List<Post> with JSON content type
                return;
            }
            if (method.equals(METHOD_GET) && path.matches(PATH_WITH_NUMBERS)) {//getById
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(METHOD_POST) && path.equals(PATH_WITHOUT_NUMBERS)) {//save
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(METHOD_DELETE) && path.matches(PATH_WITH_NUMBERS)) {//removeById
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

