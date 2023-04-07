package score;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/score")
public class GetScoreServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Score score = objectMapper.readValue(req.getInputStream(), Score.class);
        for(int i = 0; i < 9; i++) {
            System.out.println(score.content[i].courseName);
            System.out.println(score.content[i].score);
        }
        System.out.println();




    }
}
