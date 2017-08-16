package sqlcmd.view;

import java.util.Scanner;

/**
 * Created by ser on 05.08.2017.
 */
public class Console implements View {

    @Override
    public void write(String string) {
        System.out.println(string);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
