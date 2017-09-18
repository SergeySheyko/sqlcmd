package sqlcmd.view;

public interface View {
    void write(String string);

    void write(String string,Object...args);

    String read();
}
