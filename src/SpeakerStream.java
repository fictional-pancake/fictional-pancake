import java.io.IOException;
import java.io.PrintStream;

/**
 * An OutputStream wrapper for stdout
 * @author Colin Reeder and Tony Brar
 */
public class SpeakerStream extends PrintStream {

    private String thing;

    public SpeakerStream(String thing) {
        super(System.out);
    }

    @Override
    public void println(String x) {
        super.println(x);
        synchronized(this) {
            try {
                Runtime.getRuntime().exec(new String[]{thing, x}).waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
