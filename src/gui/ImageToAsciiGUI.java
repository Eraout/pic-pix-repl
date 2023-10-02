package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The ImageToAsciiGUI class provides a GUI for converting images to ASCII art.
 */
public class ImageToAsciiGUI {
    private JFrame frame;
    private JTextArea asciiTextArea;
    private JButton selectFileButton;
    private JButton openInBrowserButton;

    /**
     * Constructor for the ImageToAsciiGUI class.
     * Initializes the GUI components.
     */
    public ImageToAsciiGUI() {
        initialize();
    }

    /**
     * Initializes the GUI components and layouts.
     */
    private void initialize() {
        // Configure the main frame
        frame = new JFrame();
        frame.setTitle("Pic Replacer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        // Add an icon to the frame
        File iconFile = new File("src/resource/PicIco.ico");
        ImageIcon icon = new ImageIcon(iconFile.getAbsolutePath());
        frame.setIconImage(icon.getImage());
        // Set background color
        frame.getContentPane().setBackground(Color.decode("#383838"));

        // Configure the ASCII text area
        asciiTextArea = new JTextArea();
        asciiTextArea.setEditable(false);
        asciiTextArea.setBackground(Color.decode("#383838"));
        asciiTextArea.setForeground(Color.decode("#e0e0e0"));
        JScrollPane scrollPane = new JScrollPane(asciiTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Configure the Open File button
        selectFileButton = new JButton("Open file");
        //selectFileButton.setBorder(BorderFactory.createEmptyBorder());
        selectFileButton.setBackground(Color.decode("#383838"));
        selectFileButton.setForeground(Color.decode("#e0e0e0"));
        selectFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        // Configure the Open in Browser button
        openInBrowserButton = new JButton("Open in browser");
        openInBrowserButton.setBackground(Color.decode("#383838"));
        openInBrowserButton.setForeground(Color.decode("#e0e0e0"));
        openInBrowserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openInBrowser();
            }
        });

        // Add buttons to the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#383838"));
        buttonPanel.add(selectFileButton);
        buttonPanel.add(openInBrowserButton);

        // Add button panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }
    /**
     * Opens a file dialog to allow the user to select an image.
     * The selected image is then converted to ASCII art.
     */
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();
            String asciiImage = convertToAscii(imagePath);
            asciiTextArea.setText(asciiImage);
        }
    }
    /**
     * Converts an image to ASCII art.
     * @param imagePath The path to the image file.
     * @return The ASCII art as a String.
     */
    private String convertToAscii(String imagePath) {
        StringBuilder asciiImage = new StringBuilder();

        // Perform image to ASCII conversion
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = resizedImage.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    int gray = (r + g + b) / 3;

                    char asciiChar = getAsciiChar(gray);
                    asciiImage.append(asciiChar);
                }
                asciiImage.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return asciiImage.toString();
    }

    /**
     * Map grayscale values to ASCII characters.
     * @param gray The grayscale value.
     * @return The corresponding ASCII character.
     */
    public static char getAsciiChar(int gray) {
        // Map grayscale to ASCII
        char asciiChar;

        if (gray >= 230) {
            asciiChar = ' ';
        } else if (gray >= 200) {
            asciiChar = '.';
        } else if (gray >= 180) {
            asciiChar = ':';
        } else if (gray >= 160) {
            asciiChar = '*';
        } else if (gray >= 130) {
            asciiChar = '+';
        } else if (gray >= 100) {
            asciiChar = '#';
        } else if (gray >= 70) {
            asciiChar = '%';
        } else if (gray >= 50) {
            asciiChar = 'O';
        } else {
            asciiChar = '@';
        }

        return asciiChar;
    }

    /**
     * Opens the ASCII art in the default web browser.
     */
    private void openInBrowser() {
        // Open ASCII art in browser
        try {
            File tempFile = File.createTempFile("ascii", ".html");
            String asciiImage = asciiTextArea.getText();
            FileWriter writer = new FileWriter(tempFile);
            writer.write("<pre>" + asciiImage + "</pre>");
            writer.close();

            Desktop desktop = Desktop.getDesktop();
            desktop.browse(tempFile.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows the GUI.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * The main entry point for the application.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ImageToAsciiGUI imageToAsciiGUI = new ImageToAsciiGUI();
                imageToAsciiGUI.show();
            }
        });
    }
}
