package io.ads21.clientchat.chat;

import java.net.*;
import java.io.*;
import java.awt.*;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ChatClient extends Frame implements Runnable {

  private static final long serialVersionUID = 3944557475584591603L;
  
  private Socket soc;
  private TextField tf;
  private TextArea ta;
  private Button btnSend, btnClose;
  private String sendTo;
  private String LoginName;
  private Thread t = null;
  private DataOutputStream dout;
  private DataInputStream din;

  ChatClient(String LoginName, String chatwith) throws Exception {
    super(LoginName);
    this.LoginName = LoginName;
    sendTo = chatwith;
    tf = new TextField(50);
    ta = new TextArea(50, 50);
    btnSend = new Button("Send");
    btnClose = new Button("Close");
    soc = new Socket("127.0.0.1", 5217);

    din = new DataInputStream(soc.getInputStream());
    dout = new DataOutputStream(soc.getOutputStream());
    dout.writeUTF(LoginName);

    t = new Thread(this);
    t.start();

  }

  void setup() {
    setSize(600, 400);
    setLayout(new GridLayout(2, 1));

    add(ta);
    Panel p = new Panel();

    p.add(tf);
    p.add(btnSend);
    p.add(btnClose);
    add(p);
    show();
  }

  public boolean action(Event e, Object o) {
    if (e.arg.equals("Send")) {
      try {
        dout.writeUTF(sendTo + " " + "DATA" + " " + tf.getText().toString());
        ta.append("\n" + LoginName + " Says:" + tf.getText().toString());
        tf.setText("");
      } catch (Exception ex) {
      }
    } else if (e.arg.equals("Close")) {
      try {
        dout.writeUTF(LoginName + " LOGOUT");
        System.exit(1);
      } catch (Exception ex) {
      }

    }

    return super.action(e, o);
  }

  public static void main(String args[]) throws Exception {
    
    SwingUtilities.invokeAndWait(new Runnable() {
      
      @Override
      public void run() {
        // TODO Auto-generated method stub
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
          // TODO: handle exception
          System.out.println("Erro ao carregar interface!" + e);
        }
        
        ChatClient Client1;
        try {
          Client1 = new ChatClient(args[0], args[1]);
          Client1.setup();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
    });
    
  }

  public void run() {
    while (true) {
      try {
        ta.append("\n" + sendTo + " Diz :" + din.readUTF());

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
