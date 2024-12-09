package Main;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.*;

public class DialogBox extends JLabel {

    private Queue<String> printQueue = new ConcurrentLinkedQueue<>();
    private Queue<Pair<String,QueueRunnable>> threadLog = new ConcurrentLinkedQueue<>();
    private ExecutorService printExecutor = Executors.newCachedThreadPool();
    private boolean printdone = true;
    private String queuedtext = "";
    public DialogBox() {
        setVisible(false);
        setFont(new Font("Comic Sans MS",Font.PLAIN,30));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE,5));
        setOpaque(true);
        setBounds(94,418,812,214);
        //setLocation(94,418);
        //setPreferredSize(new Dimension(812,214));
    }

    @Override
    public void setText(String text) {
        text = "<html><div style='padding: 10px 20px;'>" + text + "</div></html>";
        super.setText(text);
    }

    public void printText(String str,DoublePoint playerPosition) {
        if(playerPosition.y > 350) {
            setBounds(94,50,812,214);
        }
        else {
            setBounds(94,418,812,214);
        }
        setVisible(true);
        printQueue.add(str);
        QueueRunnable a = threadRun();
        threadLog.add(Pair.of(str,a));
        printExecutor.submit(a);
    }

    public void whenyoupresstheinteractkeywhendialogboxisopen() {
        if(printdone) {
            //threadLog.remove();
            if(printQueue.isEmpty()) {
                printdone = false;
                setVisible(false);
            }
            else {
                QueueRunnable a = threadRun();
                threadLog.peek().setSecond(a);
                printExecutor.submit(a);
            }
        }
    }

    public void whenyoupressthenotinteractkeywhilethedialogboxisvisible() {
        printdone = true;
        setText(queuedtext);
        try {
            threadLog.peek().getSecond().setStop(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private QueueRunnable threadRun() {
        QueueRunnable runnable = new QueueRunnable() {
            @Override
            public void run() {
                try {
                    if(!printQueue.isEmpty()) {
                        if(!Thread.currentThread().getName().substring(14).equals("1")) {
                            return;
                        }
                        printdone = false;
                        queuedtext = printQueue.remove();
                        String c = "";
                        for(int i = 0; i<queuedtext.length(); i++) {
                            if(this.getStop()) {
                                threadLog.remove();
                                return;
                            }
                            c += queuedtext.charAt(i);
                            setText(c);
                            Thread.sleep(45);
                        }
                        threadLog.remove();
                        printdone = true;
                    }
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        };
        return runnable;
    }

    public boolean isQueueEmpty() {
        return printQueue.isEmpty();
    }

    public boolean isPrintDone() {
        return printdone;
    }
}

class QueueRunnable implements Runnable {
    private volatile boolean stop = false;
    @Override
    public void run() {

    }

    public boolean getStop() {
        return stop;
    }

    public void setStop(boolean a) {
        stop = a;
    }
}
