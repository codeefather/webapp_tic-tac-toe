package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LogicServlet", value="/start")
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession httpSession = request.getSession(true);

        // create game field
        Field gameField = new Field();
        Map<Integer, Sign> fieldData = gameField.getField();

        // create list of data game Field
        List<Sign> dataField = gameField.getFieldData();

        // add game field in Session
        httpSession.setAttribute("field", gameField);
        httpSession.setAttribute("data", dataField);

        // redirect and forward to index.jsp
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

    }
}
