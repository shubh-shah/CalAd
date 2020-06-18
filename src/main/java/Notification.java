import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class Notification{
    public static void showNotification(String message,Boolean success){
        try{
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            SystemTray tray = SystemTray.getSystemTray();
        
            // If you want to create an icon in the system tray to preview
            Image image = Toolkit.getDefaultToolkit().createImage("IconS.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
        
            TrayIcon trayIcon = new TrayIcon(image, "CalAd");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("CalAd");
            tray.add(trayIcon);
            if(success){
                trayIcon.displayMessage("CalAd", message, MessageType.INFO);
            }
            else{
                trayIcon.displayMessage("CalAd", message, MessageType.ERROR);
            }
            tray.remove(trayIcon);
        }catch(Exception ex){
            System.err.print(ex);
        }
    }
}
