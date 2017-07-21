import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class Timer extends Thread {
    final String PRODURI = "http://localhost:3000/prod";
    final String DEVURI = "http://localhost:3000/dev";
    final String PATH = "/sub";
    final String ENV;

    public Timer (String env) {
       this.ENV = env;
    }

    public void run () {
        try {
            System.out.println("Send reqeust!");
            String response = this.readResponse(new URL(this.setRequestUrl()).openStream());
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readResponse (InputStream response) throws IOException {
        byte[] buffer = new byte[2048];
        while(response.read(buffer) != -1) {}
        return new String(buffer);
    }

    private String setRequestUrl () {
        return (this.ENV.equals("dev") ? this.DEVURI : this.PRODURI) + this.PATH;
    }

    /*
        arg: dev / prod
        run CLI example: java Timer dev
    */
    public static void main (String[] args) {
        int initialDelay = 0;
        int period = 5;
        Executors.newScheduledThreadPool(1)
                 .scheduleAtFixedRate(new Timer(args[0]), initialDelay, period, TimeUnit.SECONDS);
    }
}
