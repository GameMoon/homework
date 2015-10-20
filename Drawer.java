import javax.swing.*;
import java.awt.*;

/**
 * Created by ugrin on 2015. 10. 12..
 */
public class Drawer extends JFrame {
    private Draw jp;

    public Drawer(){
        super("Test");
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        /*JButton button = new JButton();
        button.setSize(10,10);
        button.setVerticalAlignment(AbstractButton.CENTER);
        button.setHorizontalAlignment(AbstractButton.CENTER);
        gl.addLayoutComponent("valami", button);
        gl.setAutoCreateContainerGaps(true);
        setContentPane(pane);*/

        jp = new Draw();
        add(jp);
    }
    public void setCard(int i){
        jp.setCard(i);
    }
}
