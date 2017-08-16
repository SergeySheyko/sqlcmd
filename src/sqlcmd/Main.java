package sqlcmd;

import sqlcmd.controller.CommandProcessor;


/**
 * Created by s.sheyko on 01.08.2017.
 */
public class Main  {

    public static void main(String[] args) {

        CommandProcessor processor = new CommandProcessor();
        processor.run();


//        if (!manager.connect("test","sa","arintt21")) {
//            System.out.println("Can't connect to database!");
//            return;
//        }

    }


}
