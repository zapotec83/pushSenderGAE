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

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();

        log.warning("Mensaje recibido a las " + dateFormat.format(date));

        String reg_id = req.getParameter("token");
        String api_key = req.getParameter("api_key");
        String mensaje = req.getParameter("mensaje");
        
        log.warning("RegId->" + reg_id + "*** API_KEY->" + api_key + "*** MENSAJE->" + mensaje);

        if (reg_id != null) {

            Sender sender = new Sender(api_key);

            Message message = new Message.Builder().collapseKey("1").delayWhileIdle(true).timeToLive(20).addData("message", mensaje).build();
            try {
                Result result = sender.send(message, reg_id, 5);
                date = new Date();
                log.warning("Mensaje enviado a las " + dateFormat.format(date));
                log.warning("Resultado: " + result.toString());
                if(result.getMessageId() != null) {
                	log.warning("SENT sucessfully!! " + result.getMessageId());
                } else {
                    log.severe("ERROR->" + result.getErrorCodeName());
                }

            } catch (IllegalArgumentException e) {
                log.severe("IllegalArgumentException: " + e.getLocalizedMessage());
            } catch (InvalidRequestException e) {
                log.severe("InvalidRequestException: " + e.getLocalizedMessage());
            } catch (IOException e) {
                log.severe("IOException: " + e.getLocalizedMessage());
            }

        } else {
            log.severe("Error!! RegId no recibido!");
        }
    }
}