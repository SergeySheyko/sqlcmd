package sqlcmd.view;

/**
 * Created by s.sheyko on 01.08.2017.
 */
public interface View {
    void write(String string);

    String read();
}
