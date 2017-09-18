package sqlcmd.view;

import java.util.Scanner;

public class Console implements View {

    @Override
    public void write(String string) {
        System.out.println(string);
    }

    @Override
    public void write(String string, Object... args) {
        System.out.printf(string,args);
        System.out.println();
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
