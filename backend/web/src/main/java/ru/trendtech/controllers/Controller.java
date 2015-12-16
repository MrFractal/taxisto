package ru.trendtech.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 07.10.2014.
 */

@org.springframework.stereotype.Controller
@RequestMapping("http://localhost:8080/bla/findAddress")
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        try {
            String term = request.getParameter("term");
            LOGGER.info("Data from ajax call " + term);

            ArrayList<String> list = new ArrayList<>();
            list.add("123");
            list.add("1123");
            list.add("11123");

            //ArrayList<String> list = dataDao.getFrameWork(term);

            String searchList = new Gson().toJson(list);
            response.getWriter().write(searchList);


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

//
//    @RequestMapping(method = RequestMethod.POST)
//    public @ResponseBody
//    String findAddressByMask(HttpServletRequest request, HttpServletResponse response)
//            throws Exception {
//
//        ArrayList<String> list = new ArrayList<>();
//        list.add("123");
//        list.add("1123");
//        list.add("11123");
//
//        //ArrayList<String> list = dataDao.getFrameWork(term);
//
//        String searchList = new Gson().toJson(list);
//        response.getWriter().write(searchList);
//
//        //employee.setEmail(email);
//
//    }
}