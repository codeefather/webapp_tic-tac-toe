package com.tictactoe;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value="/logic")
public class LogicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get current session
        HttpSession currentSession = request.getSession();

        // get field
        Field field = extractField(currentSession);

        // get index of click-cell
        int index = getSelectedIndex(request);
        // current field of click-cell
        Sign currentSign = field.getField().get(index);

        // check for current field (EMPTY OR NOT)
        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // create X in field
        field.getField().put(index, Sign.CROSS);
        // check winner
        if(checkWin(response, currentSession, field)) return;

        // get empty field index and create in there '0'
        int emptyFieldIndex = field.getEmptyFieldIndex();
        if(emptyFieldIndex >= 0){
            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            // check winner
            if(checkWin(response, currentSession, field)) return;
        }else {
            // add FLAG if game is DRAW
            currentSession.setAttribute("draw", true);

            // count fields
            List<Sign> data = field.getFieldData();

            // update list of session
            currentSession.setAttribute("data", data);

            // send redirect
            response.sendRedirect("/index.jsp");
            return;
        }

        // data count
        List<Sign> data = field.getFieldData();

        // update field && data
        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);

        response.sendRedirect("/index.jsp");
    }

    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            // Добавляем флаг, который показывает что кто-то победил
            currentSession.setAttribute("winner", winner);

            // Считаем список значков
            List<Sign> data = field.getFieldData();

            // Обновляем этот список в сессии
            currentSession.setAttribute("data", data);

            // Шлем редирект
            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }

    // get index of CELL
    private int getSelectedIndex(HttpServletRequest request){
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("field");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }

}
