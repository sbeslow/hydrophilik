import com.hydrophilik.javaCrons.utils.Config;
import play.Application;
import play.GlobalSettings;

import java.io.File;

public class Global extends GlobalSettings {

    public static Config config = null;

    @Override
    public void onStart(Application app) {
        String tempDir = "/Users/scottbeslow/Documents/OrangeWall/Hydrophilik/hydrophilikAWS.config";
        try {
            File ec2Dir = new File("/home/ec2-user/hydrophilik");
            if (ec2Dir.exists()) {
                config = new Config("/home/ec2-user/hydrophilik/hydrophilikAWS.config");
            } else {
                config = new Config(tempDir);
            }

            System.out.println("Config file connection");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
