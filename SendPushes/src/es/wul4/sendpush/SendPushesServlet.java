package es.wul4.sendpush;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

@SuppressWarnings("serial")
public class SendPushesServlet extends HttpServlet {

    public static final Logger log = Logger.getLogger(SendPushesServlet.class.getName());

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        log.warning("Mensaje recibido a las " + dateFormat.format(date));

        String reg_id = req.getParameter("token");
        String mensaje = req.getParameter("mensaje");
        String senderId = req.getParameter("key");

        if (reg_id != null) {

            Sender sender = new Sender(senderId);

            Message message = new Message.Builder().collapseKey("1").delayWhileIdle(true).timeToLive(20).addData("mensaje", mensaje).build();
            try {
                Result result = sender.send(message, reg_id, 5);
                date = new Date();
                log.warning("Mensaje enviado a las " + dateFormat.format(date));
                log.warning("Resultado: " + result.toString());

            } catch (IllegalArgumentException e) {
                log.warning("IllegalArgumentException: " + e.getLocalizedMessage());
            } catch (InvalidRequestException e) {
                log.warning("InvalidRequestException: " + e.getLocalizedMessage());
            } catch (IOException e) {
                log.warning("IOException: " + e.getLocalizedMessage());
            }

        } else {
            log.warning("Error!! RegId no recibido!");
        }

        log.warning("Mensaje-> " + mensaje + " || Token-> " + reg_id);

    }
}