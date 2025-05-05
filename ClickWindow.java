import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ClickWindow {

    static Timer clickTimer = null;  // 클래스 전체에서 공유되도록 static 변수로 선언
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("자동 클릭기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

        // 전체 레이아웃
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 입력 필드 영역 (위쪽)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel labelX = new JLabel("X 좌표:");
        JTextField xField = new JTextField();

        JLabel labelY = new JLabel("Y 좌표:");
        JTextField yField = new JTextField();

        JLabel labelInterval = new JLabel("클릭 간격 (ms):");
        JTextField intervalField = new JTextField("1000");

        inputPanel.add(labelX);
        inputPanel.add(xField);
        inputPanel.add(labelY);
        inputPanel.add(yField);
        inputPanel.add(labelInterval);
        inputPanel.add(intervalField);

        // 버튼 영역 (아래쪽)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton previewButton = new JButton("미리보기");
        JButton startButton = new JButton("시작");
        JButton stopButton = new JButton("중지");

        buttonPanel.add(previewButton);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        // 메인 패널에 입력 영역 + 버튼 영역 추가
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);

        // ✅ 미리보기 버튼: 마우스 이동만
        previewButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());

                Robot robot = new Robot();
                robot.mouseMove(x, y);
                System.out.println("미리보기 이동: (" + x + ", " + y + ")");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "숫자를 정확히 입력하세요!", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ✅ 시작 버튼: 입력 간격마다 클릭 반복
        startButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                int interval = Integer.parseInt(intervalField.getText());

                if (interval < 100) {
                    JOptionPane.showMessageDialog(frame, "간격은 최소 100ms 이상이어야 해요!", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (clickTimer != null) clickTimer.cancel();

                clickTimer = new Timer();
                clickTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Robot robot = new Robot();
                            robot.mouseMove(x, y);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            System.out.println("클릭됨: (" + x + ", " + y + ") / " + interval + "ms 간격");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, 0, interval);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "숫자를 정확히 입력하세요!", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ✅ 중지 버튼: 클릭 중단
        stopButton.addActionListener(e -> {
            if (clickTimer != null) {
                clickTimer.cancel();
                clickTimer = null;
                System.out.println("클릭 중단됨.");
            }
        });
    }
}