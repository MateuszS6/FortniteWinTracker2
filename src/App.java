import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App implements ActionListener {
    private final File file = new File("D:\\My Stuff\\Program Stuff\\Fortnite\\wins.txt");
    private final int[] values = new int[3];
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel soloLabel, duoLabel, squadLabel;
    private JButton[][] buttons;
    private JButton confirmButton, cancelButton, exitButton;
    private int soloCount, duoCount, squadCount;

    public App() {
        // Window setup
        JFrame window = new JFrame("Fortnite Win Tracker");
        window.setIconImage(new ImageIcon("D:\\MY STUFF\\Saved Pictures\\Icons\\My Programs\\logo2.png").getImage());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(450, 450);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
        window.add(mainPanel);

        // Read the values
        try {
            Scanner reader = new Scanner(file);
            for (int i = 0; i < values.length; i++) values[i] = Integer.parseInt(reader.nextLine());
            reader.close();
        } catch (IOException | NoSuchElementException e) {
            throw new RuntimeException(e);
        }

        // Update the screen
        resetLabels();

        // Button setup
        confirmButton.addActionListener(e -> {
            save(soloCount, duoCount, squadCount);
            resetLabels();
        });
        cancelButton.addActionListener(e -> resetLabels());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void createUIComponents() {
        leftPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttons = new JButton[2][3];
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton(new ImageIcon(
                        "D:\\MY STUFF\\Java\\FortniteWinTracker2\\src\\images\\"
                                + ((i == 0) ? "minus" : "plus")
                                + ".png"));
                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
                if (i == 0) leftPanel.add(buttons[i][j]);
                else rightPanel.add(buttons[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            int delta = (i == 0) ? -1 : 1;
            for (JButton button : buttons[i]) {
                if (e.getSource() == button) {
                    if (button == buttons[i][0]) {
                        soloCount += delta;
                        update(soloLabel);
                    } else if (button == buttons[i][1]) {
                        duoCount += delta;
                        update(duoLabel);
                    } else if (button == buttons[i][2]) {
                        squadCount += delta;
                        update(squadLabel);
                    }
                }
            }
        }
    }

    public int getValue(GameMode gameMode) {
        int index = switch (gameMode) {
            case SOLO -> 0;
            case DUOS -> 1;
            case SQUADS -> 2;
        };
        return values[index];
    }

    /**
     * Sets {@link #soloCount}, {@link #duoCount}, and {@link #squadCount} to 0,
     * then updates the label for each win count.
     */
    private void resetLabels() {
        // Reset values
        soloCount = getValue(GameMode.SOLO);
        duoCount = getValue(GameMode.DUOS);
        squadCount = getValue(GameMode.SQUADS);

        // Update labels
        update(soloLabel);
        update(duoLabel);
        update(squadLabel);
    }

    private void update(JLabel textField) {
        // Update win text
        if (textField == soloLabel) {
            textField.setText("Solos: " + soloCount);
            textField.setForeground((soloCount == getValue(GameMode.SOLO)) ? Colour.GOLD : Colour.DARK_GOLD);
        }
        else if (textField == duoLabel) {
            textField.setText("Duos: " + duoCount);
            textField.setForeground((duoCount == getValue(GameMode.DUOS)) ? Colour.SILVER : Colour.DARK_SILVER);
        }
        else if (textField == squadLabel) {
            textField.setText("Squads: " + squadCount);
            textField.setForeground((squadCount == getValue(GameMode.SQUADS)) ? Colour.BRONZE : Colour.DARK_BRONZE);
        }
//        textField.setFont(new Font(textField.getText(), Font.BOLD, 26));

        // Update buttons
        boolean condition = soloCount != getValue(GameMode.SOLO)
                || duoCount != getValue(GameMode.DUOS)
                || squadCount != getValue(GameMode.SQUADS);
        cancelButton.setEnabled(condition);
        confirmButton.setEnabled(condition);
        exitButton.setEnabled(!condition);
    }

    /**
     * Adds the new values to {@link #values}, clears the {@link #file}, then copies the contents over.
     * @param solos the number of solo wins to be added.
     * @param duos the number of duo wins to be added.
     * @param squads the number of squad wins to be added.
     */
    public void save(int solos, int duos, int squads) {
        if (values[0] != solos) values[0] = solos;
        if (values[1] != duos) values[1] = duos;
        if (values[2] != squads) values[2] = squads;
        try {
            new PrintWriter(file).close(); // Clear file contents
            FileWriter writer = new FileWriter(file);
            writer.write(values[0] + "\n" + values[1] + "\n" + values[2]);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
